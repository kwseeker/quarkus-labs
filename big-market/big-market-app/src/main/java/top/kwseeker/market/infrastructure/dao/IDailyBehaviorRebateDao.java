package top.kwseeker.market.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kwseeker.market.infrastructure.dao.po.DailyBehaviorRebate;

import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 日常行为返利活动配置
 * @create 2024-04-30 13:48
 */
@Mapper
public interface IDailyBehaviorRebateDao {

    List<DailyBehaviorRebate> queryDailyBehaviorRebateByBehaviorType(String behaviorType);

}
