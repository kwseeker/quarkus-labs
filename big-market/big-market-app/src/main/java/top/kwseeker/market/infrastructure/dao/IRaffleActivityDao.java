package top.kwseeker.market.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kwseeker.market.infrastructure.dao.po.RaffleActivity;

@Mapper
public interface IRaffleActivityDao {

    RaffleActivity queryRaffleActivityByActivityId(Long activityId);

    Long queryStrategyIdByActivityId(Long activityId);

    Long queryActivityIdByStrategyId(Long strategyId);

}