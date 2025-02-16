package top.kwseeker.market.infrastructure.adapter.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.ibatis.exceptions.PersistenceException;
import top.kwseeker.market.domain.award.model.valobj.AccountStatusVO;
import top.kwseeker.market.domain.credit.model.aggregate.TradeAggregate;
import top.kwseeker.market.domain.credit.model.entity.CreditAccountEntity;
import top.kwseeker.market.domain.credit.model.entity.CreditOrderEntity;
import top.kwseeker.market.domain.credit.model.entity.TaskEntity;
import top.kwseeker.market.domain.credit.repository.ICreditRepository;
import top.kwseeker.market.infrastructure.dao.ITaskDao;
import top.kwseeker.market.infrastructure.dao.IUserCreditAccountDao;
import top.kwseeker.market.infrastructure.dao.IUserCreditOrderDao;
import top.kwseeker.market.infrastructure.dao.po.Task;
import top.kwseeker.market.infrastructure.dao.po.UserCreditAccount;
import top.kwseeker.market.infrastructure.dao.po.UserCreditOrder;
//import top.kwseeker.market.infrastructure.event.EventPublisher;
import top.kwseeker.market.infrastructure.event.EventPublisher;
import top.kwseeker.market.infrastructure.quarkus.TransactionTemplate;
import top.kwseeker.market.infrastructure.redis.IRedisService;
//import top.kwseeker.market.middleware.db.router.strategy.IDBRouterStrategy;
import top.kwseeker.market.types.common.Constants;
import top.kwseeker.market.types.enums.ResponseCode;
import top.kwseeker.market.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;

import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.concurrent.TimeUnit;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 用户积分仓储
 * @create 2024-06-01 10:07
 */
@Slf4j
@ApplicationScoped
public class CreditRepository implements ICreditRepository {

    @Inject
    IRedisService redisService;
    @Inject
    IUserCreditAccountDao userCreditAccountDao;
    @Inject
    IUserCreditOrderDao userCreditOrderDao;
    @Inject
    ITaskDao taskDao;
    @Inject
    TransactionTemplate transactionTemplate;
    @Inject
    EventPublisher eventPublisher;
    //@Inject   //TODO
    //IDBRouterStrategy dbRouter;

    @Override
    public void saveUserCreditTradeOrder(TradeAggregate tradeAggregate) {
        String userId = tradeAggregate.getUserId();
        CreditAccountEntity creditAccountEntity = tradeAggregate.getCreditAccountEntity();
        CreditOrderEntity creditOrderEntity = tradeAggregate.getCreditOrderEntity();
        TaskEntity taskEntity = tradeAggregate.getTaskEntity();

        // 积分账户
        UserCreditAccount userCreditAccountReq = new UserCreditAccount();
        userCreditAccountReq.setUserId(userId);
        userCreditAccountReq.setTotalAmount(creditAccountEntity.getAdjustAmount());
        // 知识；仓储往上有业务语义，仓储往下到 dao 操作是没有业务语义的。所以不用在乎这块使用的字段名称，直接用持久化对象即可。
        userCreditAccountReq.setAvailableAmount(creditAccountEntity.getAdjustAmount());
        userCreditAccountReq.setAccountStatus(AccountStatusVO.open.getCode());

        // 积分订单
        UserCreditOrder userCreditOrderReq = new UserCreditOrder();
        userCreditOrderReq.setUserId(creditOrderEntity.getUserId());
        userCreditOrderReq.setOrderId(creditOrderEntity.getOrderId());
        userCreditOrderReq.setTradeName(creditOrderEntity.getTradeName().getName());
        userCreditOrderReq.setTradeType(creditOrderEntity.getTradeType().getCode());
        userCreditOrderReq.setTradeAmount(creditOrderEntity.getTradeAmount());
        userCreditOrderReq.setOutBusinessNo(creditOrderEntity.getOutBusinessNo());

        Task task = new Task();
        task.setUserId(taskEntity.getUserId());
        task.setTopic(taskEntity.getTopic());
        task.setMessageId(taskEntity.getMessageId());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setState(taskEntity.getState().getCode());

        RLock lock = redisService.getLock(Constants.RedisKey.USER_CREDIT_ACCOUNT_LOCK + userId + Constants.UNDERLINE + creditOrderEntity.getOutBusinessNo());
        try {
            lock.lock(3, TimeUnit.SECONDS);
            //dbRouter.doRouter(userId);
            // 编程式事务
            //transactionTemplate.execute(status -> {
            transactionTemplate.execute(() -> {
                try {
                    // 1. 保存账户积分
                    UserCreditAccount userCreditAccount = userCreditAccountDao.queryUserCreditAccount(userCreditAccountReq);
                    if (null == userCreditAccount) {
                        userCreditAccountDao.insert(userCreditAccountReq);
                    } else {
                        BigDecimal availableAmount = userCreditAccountReq.getAvailableAmount();
                        if (availableAmount.compareTo(BigDecimal.ZERO) >= 0) {
                            userCreditAccountDao.updateAddAmount(userCreditAccountReq);
                        } else {
                            int subtractionCount = userCreditAccountDao.updateSubtractionAmount(userCreditAccountReq);
                            if (1 != subtractionCount) {
                                //status.setRollbackOnly();
                                throw new AppException(ResponseCode.USER_CREDIT_ACCOUNT_NO_AVAILABLE_AMOUNT.getCode(), ResponseCode.USER_CREDIT_ACCOUNT_NO_AVAILABLE_AMOUNT.getInfo());
                            }
                        }
                    }
                    // 2. 保存账户订单
                    userCreditOrderDao.insert(userCreditOrderReq);
                    // 3. 写入任务
                    taskDao.insert(task);
                //} catch (DuplicateKeyException e) {
                } catch (Exception e) {
                    if (e instanceof PersistenceException &&
                            e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                        log.error("调整账户积分额度异常，唯一索引冲突 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                    } else {
                        log.error("调整账户积分额度失败 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                    }
                    throw e;
                }
                //return 1;
            });
        } finally {
            //dbRouter.clear();
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        try {
            // 发送消息【在事务外执行，如果失败还有任务补偿】
            eventPublisher.publish(task.getTopic(), task.getMessage());
            // 更新数据库记录，task 任务表
            taskDao.updateTaskSendMessageCompleted(task);
            log.info("调整账户积分记录，发送MQ消息完成 userId: {} orderId:{} topic: {}", userId, creditOrderEntity.getOrderId(), task.getTopic());
        } catch (Exception e) {
            log.error("调整账户积分记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
            taskDao.updateTaskSendMessageFail(task);
        }

    }

    @Override
    public CreditAccountEntity queryUserCreditAccount(String userId) {
        UserCreditAccount userCreditAccountReq = new UserCreditAccount();
        userCreditAccountReq.setUserId(userId);
        try {
            //dbRouter.doRouter(userId);
            UserCreditAccount userCreditAccount = userCreditAccountDao.queryUserCreditAccount(userCreditAccountReq);
            BigDecimal availableAmount = BigDecimal.ZERO;
            if (null != userCreditAccount) {
                availableAmount = userCreditAccount.getAvailableAmount();
            }
            return CreditAccountEntity.builder().userId(userId).adjustAmount(availableAmount).build();
        } finally {
            //dbRouter.clear();
        }
    }

}
