package top.kwseeker.market.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kwseeker.market.infrastructure.dao.po.RaffleActivityCount;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 抽奖活动次数配置表Dao
 * @create 2024-03-09 10:07
 */
@Mapper
public interface IRaffleActivityCountDao {

    RaffleActivityCount queryRaffleActivityCountByActivityCountId(Long activityCountId);

}
