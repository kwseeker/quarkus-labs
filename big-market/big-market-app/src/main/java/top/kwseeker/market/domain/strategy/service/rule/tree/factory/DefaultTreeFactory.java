package top.kwseeker.market.domain.strategy.service.rule.tree.factory;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.kwseeker.market.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import top.kwseeker.market.domain.strategy.model.valobj.RuleTreeVO;
import top.kwseeker.market.domain.strategy.service.rule.tree.ILogicTreeNode;
import top.kwseeker.market.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import top.kwseeker.market.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngine;
import top.kwseeker.market.infrastructure.quarkus.CDIBeanSelector;

import java.util.Map;

@ApplicationScoped
public class DefaultTreeFactory {

    public IDecisionTreeEngine openLogicTree(RuleTreeVO ruleTreeVO) {
        Map<String, ILogicTreeNode> logicTreeNodeGroup = CDIBeanSelector.selectMap(ILogicTreeNode.class);
        return new DecisionTreeEngine(logicTreeNodeGroup, ruleTreeVO);
    }

    /**
     * 决策树个动作实习
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TreeActionEntity {
        private RuleLogicCheckTypeVO ruleLogicCheckType;
        private StrategyAwardVO strategyAwardVO;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardVO {
        /** 抽奖奖品ID - 内部流转使用 */
        private Integer awardId;
        /** 抽奖奖品规则 */
        private String awardRuleValue;
    }

}
