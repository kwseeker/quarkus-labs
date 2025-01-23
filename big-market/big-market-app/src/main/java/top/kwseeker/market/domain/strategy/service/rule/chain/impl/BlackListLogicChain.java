package top.kwseeker.market.domain.strategy.service.rule.chain.impl;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import top.kwseeker.market.domain.strategy.repository.IStrategyRepository;
import top.kwseeker.market.domain.strategy.service.rule.chain.AbstractLogicChain;
import top.kwseeker.market.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import top.kwseeker.market.types.common.Constants;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 黑名单责任链
 * @create 2024-01-20 10:23
 */
@Slf4j
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//@Component("rule_blacklist")
@Dependent  // Quarkus 中类似 Spring 原型模式， @Dependent Bean 的生命周期与注入它的 Bean 绑定
@Named("rule_blacklist")
public class BlackListLogicChain extends AbstractLogicChain {

    @Inject
    IStrategyRepository repository;

    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        log.info("抽奖责任链-黑名单开始 userId:{} strategyId:{} ruleModel:{}", userId, strategyId, ruleModel());

        // 查询规则值配置
        String ruleValue = repository.queryStrategyRuleValue(strategyId, ruleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(splitRuleValue[0]);

        // 黑名单抽奖判断
        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);
        for (String userBlackId : userBlackIds) {
            if (userId.equals(userBlackId)) {
                log.info("抽奖责任链-黑名单接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
                return DefaultChainFactory.StrategyAwardVO.builder()
                        .awardId(awardId)
                        .logicModel(ruleModel())
                        // 写入默认配置黑名单奖品值 0.01 ~ 1 积分，也可以配置到数据库表中
                        .awardRuleValue("0.01,1")
                        .build();
            }
        }

        // 过滤其他责任链
        log.info("抽奖责任链-黑名单放行 userId:{} strategyId:{} ruleModel:{}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }

    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_BLACKLIST.getCode();
    }

}
