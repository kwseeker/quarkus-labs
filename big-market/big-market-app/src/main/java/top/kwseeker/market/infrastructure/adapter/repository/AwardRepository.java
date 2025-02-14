package top.kwseeker.market.infrastructure.adapter.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import org.apache.ibatis.exceptions.PersistenceException;
import top.kwseeker.market.domain.award.adapter.repository.IAwardRepository;
import top.kwseeker.market.domain.award.model.aggregate.GiveOutPrizesAggregate;
import top.kwseeker.market.domain.award.model.aggregate.UserAwardRecordAggregate;
import top.kwseeker.market.domain.award.model.entity.TaskEntity;
import top.kwseeker.market.domain.award.model.entity.UserAwardRecordEntity;
import top.kwseeker.market.domain.award.model.entity.UserCreditAwardEntity;
import top.kwseeker.market.domain.award.model.valobj.AccountStatusVO;
import top.kwseeker.market.infrastructure.dao.*;
import top.kwseeker.market.infrastructure.dao.po.Task;
import top.kwseeker.market.infrastructure.dao.po.UserAwardRecord;
import top.kwseeker.market.infrastructure.dao.po.UserCreditAccount;
import top.kwseeker.market.infrastructure.dao.po.UserRaffleOrder;
//import top.kwseeker.market.infrastructure.event.EventPublisher;
import top.kwseeker.market.infrastructure.quarkus.TransactionTemplate;
import top.kwseeker.market.infrastructure.redis.IRedisService;
//import top.kwseeker.market.middleware.db.router.strategy.IDBRouterStrategy;
import top.kwseeker.market.types.common.Constants;
import top.kwseeker.market.types.enums.ResponseCode;
import top.kwseeker.market.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.concurrent.TimeUnit;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 奖品仓储服务
 * @create 2024-04-06 10:09
 */
@Slf4j
@ApplicationScoped
public class AwardRepository implements IAwardRepository {

    @Inject
    IAwardDao awardDao;
    @Inject
    ITaskDao taskDao;
    @Inject
    IUserAwardRecordDao userAwardRecordDao;
    @Inject
    IUserRaffleOrderDao userRaffleOrderDao;
    @Inject
    IUserCreditAccountDao userCreditAccountDao;
    @Inject
    TransactionTemplate transactionTemplate;
    // TODO 分库分表、事务控制、消息队列
    //@Inject
    //IDBRouterStrategy dbRouter;
    //@Inject
    //EventPublisher eventPublisher;
    @Inject
    IRedisService redisService;

    @Override
    public void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate) {
        UserAwardRecordEntity userAwardRecordEntity = userAwardRecordAggregate.getUserAwardRecordEntity();
        TaskEntity taskEntity = userAwardRecordAggregate.getTaskEntity();
        String userId = userAwardRecordEntity.getUserId();
        Long activityId = userAwardRecordEntity.getActivityId();
        Integer awardId = userAwardRecordEntity.getAwardId();

        UserAwardRecord userAwardRecord = new UserAwardRecord();
        userAwardRecord.setUserId(userAwardRecordEntity.getUserId());
        userAwardRecord.setActivityId(userAwardRecordEntity.getActivityId());
        userAwardRecord.setStrategyId(userAwardRecordEntity.getStrategyId());
        userAwardRecord.setOrderId(userAwardRecordEntity.getOrderId());
        userAwardRecord.setAwardId(userAwardRecordEntity.getAwardId());
        userAwardRecord.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        userAwardRecord.setAwardTime(userAwardRecordEntity.getAwardTime());
        userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());

        Task task = new Task();
        task.setUserId(taskEntity.getUserId());
        task.setTopic(taskEntity.getTopic());
        task.setMessageId(taskEntity.getMessageId());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setState(taskEntity.getState().getCode());

        UserRaffleOrder userRaffleOrderReq = new UserRaffleOrder();
        userRaffleOrderReq.setUserId(userAwardRecordEntity.getUserId());
        userRaffleOrderReq.setOrderId(userAwardRecordEntity.getOrderId());

        try {
            //dbRouter.doRouter(userId);
            transactionTemplate.execute(() -> {
                try {
                    // 写入记录
                    userAwardRecordDao.insert(userAwardRecord);
                    // 写入任务
                    taskDao.insert(task);
                    // 更新抽奖单
                    int count = userRaffleOrderDao.updateUserRaffleOrderStateUsed(userRaffleOrderReq);
                    if (1 != count) {
                        //status.setRollbackOnly();
                        log.error("写入中奖记录，用户抽奖单已使用过，不可重复抽奖 userId: {} activityId: {} awardId: {}", userId, activityId, awardId);
                        throw new AppException(ResponseCode.ACTIVITY_ORDER_ERROR.getCode(), ResponseCode.ACTIVITY_ORDER_ERROR.getInfo());
                    }
                    //return 1;
                //} catch (DuplicateKeyException e) {
                } catch (Exception e) {
                    if (e instanceof PersistenceException
                            && e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                        log.error("写入中奖记录，唯一索引冲突 userId: {} activityId: {} awardId: {}", userId, activityId, awardId, e);
                    } else {
                        log.error("写入中奖记录，出现异常 userId: {} activityId: {} awardId: {}", userId, activityId, awardId, e);
                    }
                    throw e;
                }
            });
        } finally {
            //dbRouter.clear();
        }

        try {
            // 发送消息【在事务外执行，如果失败还有任务补偿】
            //eventPublisher.publish(task.getTopic(), task.getMessage());
            // 更新数据库记录，task 任务表
            taskDao.updateTaskSendMessageCompleted(task);
            log.info("写入中奖记录，发送MQ消息完成 userId: {} orderId:{} topic: {}", userId, userAwardRecordEntity.getOrderId(), task.getTopic());
        } catch (Exception e) {
            log.error("写入中奖记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
            taskDao.updateTaskSendMessageFail(task);
        }

    }

    @Override
    public String queryAwardConfig(Integer awardId) {
        return awardDao.queryAwardConfigByAwardId(awardId);
    }

    @Override
    public void saveGiveOutPrizesAggregate(GiveOutPrizesAggregate giveOutPrizesAggregate) {
        String userId = giveOutPrizesAggregate.getUserId();
        UserCreditAwardEntity userCreditAwardEntity = giveOutPrizesAggregate.getUserCreditAwardEntity();
        UserAwardRecordEntity userAwardRecordEntity = giveOutPrizesAggregate.getUserAwardRecordEntity();

        // 更新发奖记录
        UserAwardRecord userAwardRecordReq = new UserAwardRecord();
        userAwardRecordReq.setUserId(userId);
        userAwardRecordReq.setOrderId(userAwardRecordEntity.getOrderId());
        userAwardRecordReq.setAwardState(userAwardRecordEntity.getAwardState().getCode());

        // 更新用户积分 「首次则插入数据」
        UserCreditAccount userCreditAccountReq = new UserCreditAccount();
        userCreditAccountReq.setUserId(userCreditAwardEntity.getUserId());
        userCreditAccountReq.setTotalAmount(userCreditAwardEntity.getCreditAmount());
        userCreditAccountReq.setAvailableAmount(userCreditAwardEntity.getCreditAmount());
        userCreditAccountReq.setAccountStatus(AccountStatusVO.open.getCode());

        RLock lock = redisService.getLock(Constants.RedisKey.ACTIVITY_ACCOUNT_LOCK + userId);
        try {
            lock.lock(3, TimeUnit.SECONDS);
            //dbRouter.doRouter(giveOutPrizesAggregate.getUserId());
            //transactionTemplate.execute(status -> {
            transactionTemplate.execute(() -> {
                try {
                    // 更新积分 || 创建积分账户
                    UserCreditAccount userCreditAccountRes = userCreditAccountDao.queryUserCreditAccount(userCreditAccountReq);
                    if (null == userCreditAccountRes) {
                        userCreditAccountDao.insert(userCreditAccountReq);
                    } else {
                        userCreditAccountDao.updateAddAmount(userCreditAccountReq);
                    }

                    // 更新奖品记录
                    int updateAwardCount = userAwardRecordDao.updateAwardRecordCompletedState(userAwardRecordReq);
                    if (0 == updateAwardCount) {
                        log.warn("更新中奖记录，重复更新拦截 userId:{} giveOutPrizesAggregate:{}", userId, JSON.toJSONString(giveOutPrizesAggregate));
                        //status.setRollbackOnly();
                    }
                    //return 1;
                //} catch (DuplicateKeyException e) {
                } catch (Exception e) {
                    //status.setRollbackOnly();
                    if (e instanceof PersistenceException
                            && e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                        log.error("更新中奖记录，唯一索引冲突 userId: {} ", userId, e);
                    } else {
                        log.error("更新中奖记录，出现异常 userId: {} ", userId, e);
                    }
                    throw e;
                }
            });
        } finally {
            //dbRouter.clear();
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public String queryAwardKey(Integer awardId) {
        return awardDao.queryAwardKeyByAwardId(awardId);
    }

}
