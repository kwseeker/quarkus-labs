package top.kwseeker.market.infrastructure.dao;

import top.kwseeker.market.infrastructure.dao.po.RaffleActivityOrder;
//import top.kwseeker.market.middleware.db.router.annotation.DBRouter;
//import top.kwseeker.market.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 抽奖活动单Dao
 * @create 2024-03-09 10:08
 */
@Mapper
//@DBRouterStrategy(splitTable = true)  //TODO
public interface IRaffleActivityOrderDao {

    //@DBRouter(key = "userId")
    void insert(RaffleActivityOrder raffleActivityOrder);

    //@DBRouter
    List<RaffleActivityOrder> queryRaffleActivityOrderByUserId(String userId);

    //@DBRouter
    RaffleActivityOrder queryRaffleActivityOrder(RaffleActivityOrder raffleActivityOrderReq);

    int updateOrderCompleted(RaffleActivityOrder raffleActivityOrderReq);

    //@DBRouter
    RaffleActivityOrder queryUnpaidActivityOrder(RaffleActivityOrder raffleActivityOrderReq);

}
