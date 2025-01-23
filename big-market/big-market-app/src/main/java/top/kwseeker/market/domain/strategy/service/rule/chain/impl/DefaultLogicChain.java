package top.kwseeker.market.domain.strategy.service.rule.chain.impl;

import jakarta.inject.Inject;
import top.kwseeker.market.domain.strategy.service.armory.IStrategyDispatch;
import top.kwseeker.market.domain.strategy.service.rule.chain.AbstractLogicChain;
import top.kwseeker.market.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 默认的责任链「作为最后一个链」
 * @create 2024-01-20 10:06
 */
@Slf4j
//@Component("rule_default")
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Dependent  // Quarkus 中类似 Spring 原型模式， @Dependent Bean 的生命周期与注入它的 Bean 绑定
@Named("rule_default")
public class DefaultLogicChain extends AbstractLogicChain {

    @Inject
    protected IStrategyDispatch strategyDispatch;

    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        Integer awardId = strategyDispatch.getRandomAwardId(strategyId);
        log.info("抽奖责任链-默认处理 userId:{} strategyId:{} ruleModel:{} awardId:{}", userId, strategyId, ruleModel(), awardId);
        return DefaultChainFactory.StrategyAwardVO.builder()
                .awardId(awardId)
                .logicModel(ruleModel())
                .build();
    }

    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode();
    }

}
