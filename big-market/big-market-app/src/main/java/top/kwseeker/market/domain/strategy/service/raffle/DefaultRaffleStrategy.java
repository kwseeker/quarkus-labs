package top.kwseeker.market.domain.strategy.service.raffle;

import jakarta.inject.Inject;
import top.kwseeker.market.domain.strategy.model.entity.StrategyAwardEntity;
import top.kwseeker.market.domain.strategy.model.valobj.RuleTreeVO;
import top.kwseeker.market.domain.strategy.model.valobj.RuleWeightVO;
import top.kwseeker.market.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import top.kwseeker.market.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import top.kwseeker.market.domain.strategy.repository.IStrategyRepository;
import top.kwseeker.market.domain.strategy.service.AbstractRaffleStrategy;
import top.kwseeker.market.domain.strategy.service.IRaffleAward;
import top.kwseeker.market.domain.strategy.service.IRaffleRule;
import top.kwseeker.market.domain.strategy.service.IRaffleStock;
import top.kwseeker.market.domain.strategy.service.armory.IStrategyDispatch;
import top.kwseeker.market.domain.strategy.service.rule.chain.ILogicChain;
import top.kwseeker.market.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import top.kwseeker.market.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import top.kwseeker.market.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 默认的抽奖策略实现
 */
@Slf4j
@ApplicationScoped
public class DefaultRaffleStrategy extends AbstractRaffleStrategy implements IRaffleStock, IRaffleAward, IRaffleRule {

    public DefaultRaffleStrategy() {
    }

    @Inject
    public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch,
                                 DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        super(repository, strategyDispatch, defaultChainFactory, defaultTreeFactory);
    }

    @Override
    public DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        log.info("抽奖策略-责任链 userId:{} strategyId:{}", userId, strategyId);
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);
        return logicChain.logic(userId, strategyId);
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        return raffleLogicTree(userId, strategyId, awardId, null);
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId, Date endDateTime) {
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);
        if (null == strategyAwardRuleModelVO) {
            return DefaultTreeFactory.StrategyAwardVO.builder().awardId(awardId).build();
        }
        log.info("抽奖策略-规则树 userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);
        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModels());
        if (null == ruleTreeVO) {
            throw new RuntimeException("存在抽奖策略配置的规则模型 Key，未在库表 rule_tree、rule_tree_node、rule_tree_line 配置对应的规则树信息 " + strategyAwardRuleModelVO.getRuleModels());
        }
        IDecisionTreeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        return treeEngine.process(userId, strategyId, awardId, endDateTime);
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException {
        return repository.takeQueueValue();
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue(Long strategyId, Integer awardId) throws InterruptedException {
        return repository.takeQueueValue(strategyId, awardId);
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        repository.updateStrategyAwardStock(strategyId, awardId);
    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId) {
        return repository.queryStrategyAwardList(strategyId);
    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Long activityId) {
        Long strategyId = repository.queryStrategyIdByActivityId(activityId);
        return queryRaffleStrategyAwardList(strategyId);
    }

    @Override
    public List<StrategyAwardStockKeyVO> queryOpenActivityStrategyAwardList() {
        return repository.queryOpenActivityStrategyAwardList();
    }

    @Override
    public Map<String, Integer> queryAwardRuleLockCount(String[] treeIds) {
        return repository.queryAwardRuleLockCount(treeIds);
    }

    @Override
    public List<RuleWeightVO> queryAwardRuleWeight(Long strategyId) {
        return repository.queryAwardRuleWeight(strategyId);
    }

    @Override
    public List<RuleWeightVO> queryAwardRuleWeightByActivityId(Long activityId) {
        Long strategyId = repository.queryStrategyIdByActivityId(activityId);
        return queryAwardRuleWeight(strategyId);
    }

}
