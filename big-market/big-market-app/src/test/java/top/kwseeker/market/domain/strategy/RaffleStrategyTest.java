package top.kwseeker.market.domain.strategy;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.kwseeker.market.app.util.json.JSONUtil;
import top.kwseeker.market.domain.strategy.model.entity.RaffleAwardEntity;
import top.kwseeker.market.domain.strategy.model.entity.RaffleFactorEntity;
import top.kwseeker.market.domain.strategy.service.IRaffleStrategy;
import top.kwseeker.market.domain.strategy.service.armory.IStrategyArmory;

/**
 * 抽奖(raffle)策略测试
 */
@QuarkusTest
public class RaffleStrategyTest {

    private static final Logger log = LoggerFactory.getLogger(RaffleStrategyTest.class);

    /**
     * Quarkus 推荐使用默认包权限
     */
    @Inject
    IStrategyArmory strategyArmory;
    @Inject
    IRaffleStrategy raffleStrategy;
    //@Resource
    //private RuleWeightLogicChain ruleWeightLogicChain;
    //@Resource
    //private RuleLockLogicTreeNode ruleLockLogicTreeNode;
    //@Resource
    //private IRaffleStock raffleStock;
    //@Resource
    //private IRaffleRule raffleRule;

    @BeforeEach
    public void setUp() {
        // 策略装配 100001、100002、100003
        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100001L));
        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100006L));
    }

    @Test
    public void test_performRaffle() throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                    .userId("Arvin")
                    .strategyId(100006L)
                    .build();
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

            log.info("请求参数：{}", JSONUtil.toJSONString(raffleFactorEntity));
            log.info("测试结果：{}", JSONUtil.toJSONString(raffleAwardEntity));
        }

        // 等待 UpdateAwardStockJob 消费队列
        //new CountDownLatch(1).await();
    }

    //@Test
    //public void test_performRaffle_blacklist() {
    //    RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
    //            .userId("user003")  // 黑名单用户 user001,user002,user003
    //            .strategyId(100001L)
    //            .build();
    //
    //    RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
    //
    //    log.info("请求参数：{}", JSONUtil.toJSONString(raffleFactorEntity));
    //    log.info("测试结果：{}", JSONUtil.toJSONString(raffleAwardEntity));
    //}
    //
    ///**
    // * 次数错校验，抽奖n次后解锁。100003 策略，你可以通过调整 @Before 的 setUp 方法中个人抽奖次数来验证。比如最开始设置0，之后设置10
    // * ReflectionTestUtils.setField(ruleLockLogicFilter, "userRaffleCount", 10L);
    // */
    //@Test
    //public void test_raffle_center_rule_lock() {
    //    RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
    //            .userId("xiaofuge")
    //            .strategyId(100003L)
    //            .build();
    //
    //    RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
    //
    //    log.info("请求参数：{}", JSONUtil.toJSONString(raffleFactorEntity));
    //    log.info("测试结果：{}", JSONUtil.toJSONString(raffleAwardEntity));
    //}
    //
    //@Test
    //public void test_takeQueueValue() throws InterruptedException {
    //    StrategyAwardStockKeyVO strategyAwardStockKeyVO = raffleStock.takeQueueValue();
    //    log.info("测试结果：{}", JSONUtil.toJSONString(strategyAwardStockKeyVO));
    //}
    //
    //@Test
    //public void test_raffleRule() {
    //    List<RuleWeightVO> ruleWeightVOS = raffleRule.queryAwardRuleWeightByActivityId(100301L);
    //    log.info("测试结果：{}", JSONUtil.toJSONString(ruleWeightVOS));
    //}

}
