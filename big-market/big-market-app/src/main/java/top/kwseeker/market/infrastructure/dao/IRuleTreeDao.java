package top.kwseeker.market.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kwseeker.market.infrastructure.dao.po.RuleTree;

@Mapper
public interface IRuleTreeDao {

    RuleTree queryRuleTreeByTreeId(String treeId);

}