package top.kwseeker.market.infrastructure.adapter.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.ibatis.exceptions.PersistenceException;
import top.kwseeker.market.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import top.kwseeker.market.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import top.kwseeker.market.domain.rebate.model.entity.TaskEntity;
import top.kwseeker.market.domain.rebate.model.valobj.BehaviorTypeVO;
import top.kwseeker.market.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import top.kwseeker.market.domain.rebate.repository.IBehaviorRebateRepository;
import top.kwseeker.market.infrastructure.dao.IDailyBehaviorRebateDao;
import top.kwseeker.market.infrastructure.dao.ITaskDao;
import top.kwseeker.market.infrastructure.dao.IUserBehaviorRebateOrderDao;
import top.kwseeker.market.infrastructure.dao.po.DailyBehaviorRebate;
import top.kwseeker.market.infrastructure.dao.po.Task;
import top.kwseeker.market.infrastructure.dao.po.UserBehaviorRebateOrder;
//import top.kwseeker.market.infrastructure.event.EventPublisher;
//import top.kwseeker.market.middleware.db.router.strategy.IDBRouterStrategy;
import top.kwseeker.market.infrastructure.event.EventPublisher;
import top.kwseeker.market.infrastructure.quarkus.TransactionTemplate;
import top.kwseeker.market.types.enums.ResponseCode;
import top.kwseeker.market.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 行为返利服务仓储实现
 * @create 2024-04-30 16:21
 */
@Slf4j
@ApplicationScoped
public class BehaviorRebateRepository implements IBehaviorRebateRepository {

    @Inject
    IDailyBehaviorRebateDao dailyBehaviorRebateDao;
    @Inject
    IUserBehaviorRebateOrderDao userBehaviorRebateOrderDao;
    @Inject
    ITaskDao taskDao;
    @Inject
    TransactionTemplate transactionTemplate;
    @Inject
    EventPublisher eventPublisher;
    //@Inject       //TODO
    //IDBRouterStrategy dbRouter;

    @Override
    public List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO) {
        List<DailyBehaviorRebate> dailyBehaviorRebates = dailyBehaviorRebateDao.queryDailyBehaviorRebateByBehaviorType(
                behaviorTypeVO.getCode().toLowerCase());
        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOS = new ArrayList<>(dailyBehaviorRebates.size());
        for (DailyBehaviorRebate dailyBehaviorRebate : dailyBehaviorRebates) {
            dailyBehaviorRebateVOS.add(DailyBehaviorRebateVO.builder()
                    .behaviorType(dailyBehaviorRebate.getBehaviorType())
                    .rebateDesc(dailyBehaviorRebate.getRebateDesc())
                    .rebateType(dailyBehaviorRebate.getRebateType())
                    .rebateConfig(dailyBehaviorRebate.getRebateConfig())
                    .build());
        }
        return dailyBehaviorRebateVOS;
    }

    @Override
    public void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates) {
        try {
            //dbRouter.doRouter(userId);
            //transactionTemplate.execute(status -> {
            transactionTemplate.execute(() -> {
                try {
                    for (BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
                        BehaviorRebateOrderEntity behaviorRebateOrderEntity = behaviorRebateAggregate.getBehaviorRebateOrderEntity();
                        // 用户行为返利订单对象
                        UserBehaviorRebateOrder userBehaviorRebateOrder = new UserBehaviorRebateOrder();
                        userBehaviorRebateOrder.setUserId(behaviorRebateOrderEntity.getUserId());
                        userBehaviorRebateOrder.setOrderId(behaviorRebateOrderEntity.getOrderId());
                        userBehaviorRebateOrder.setBehaviorType(behaviorRebateOrderEntity.getBehaviorType());
                        userBehaviorRebateOrder.setRebateDesc(behaviorRebateOrderEntity.getRebateDesc());
                        userBehaviorRebateOrder.setRebateType(behaviorRebateOrderEntity.getRebateType());
                        userBehaviorRebateOrder.setRebateConfig(behaviorRebateOrderEntity.getRebateConfig());
                        userBehaviorRebateOrder.setOutBusinessNo(behaviorRebateOrderEntity.getOutBusinessNo());
                        userBehaviorRebateOrder.setBizId(behaviorRebateOrderEntity.getBizId());
                        userBehaviorRebateOrderDao.insert(userBehaviorRebateOrder);

                        // 任务对象
                        TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
                        Task task = new Task();
                        task.setUserId(taskEntity.getUserId());
                        task.setTopic(taskEntity.getTopic());
                        task.setMessageId(taskEntity.getMessageId());
                        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
                        task.setState(taskEntity.getState().getCode());
                        taskDao.insert(task);
                    }
                    //return 1;
                //} catch (DuplicateKeyException e) {
                } catch (Exception e) {
                    //status.setRollbackOnly();
                    if (e instanceof PersistenceException
                            && e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                        log.error("写入返利记录，唯一索引冲突 userId: {}", userId, e);
                    } else {
                        log.error("写入返利记录，出现异常 userId: {}", userId, e);
                    }
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), ResponseCode.INDEX_DUP.getInfo());
                }
            });
        } finally {
            //dbRouter.clear();
        }

        // 同步发送MQ消息
        for (BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
            TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
            Task task = new Task();
            task.setUserId(taskEntity.getUserId());
            task.setMessageId(taskEntity.getMessageId());
            try {
                // 发送消息【在事务外执行，如果失败还有任务补偿】
                eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
                // 更新数据库记录，task 任务表
                taskDao.updateTaskSendMessageCompleted(task);
            } catch (Exception e) {
                log.error("写入返利记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
                taskDao.updateTaskSendMessageFail(task);
            }
        }

    }

    @Override
    public List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String userId, String outBusinessNo) {
        // 1. 请求对象
        UserBehaviorRebateOrder userBehaviorRebateOrderReq = new UserBehaviorRebateOrder();
        userBehaviorRebateOrderReq.setUserId(userId);
        userBehaviorRebateOrderReq.setOutBusinessNo(outBusinessNo);
        // 2. 查询结果
        List<UserBehaviorRebateOrder> userBehaviorRebateOrderResList = userBehaviorRebateOrderDao.queryOrderByOutBusinessNo(userBehaviorRebateOrderReq);
        List<BehaviorRebateOrderEntity> behaviorRebateOrderEntities = new ArrayList<>(userBehaviorRebateOrderResList.size());
        for (UserBehaviorRebateOrder userBehaviorRebateOrder : userBehaviorRebateOrderResList) {
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity.builder()
                        .userId(userBehaviorRebateOrder.getUserId())
                        .orderId(userBehaviorRebateOrder.getOrderId())
                        .behaviorType(userBehaviorRebateOrder.getBehaviorType())
                        .rebateDesc(userBehaviorRebateOrder.getRebateDesc())
                        .rebateType(userBehaviorRebateOrder.getRebateType())
                        .rebateConfig(userBehaviorRebateOrder.getRebateConfig())
                        .outBusinessNo(userBehaviorRebateOrder.getOutBusinessNo())
                        .bizId(userBehaviorRebateOrder.getBizId())
                        .build();
            behaviorRebateOrderEntities.add(behaviorRebateOrderEntity);
        }
        return behaviorRebateOrderEntities;
    }

}
