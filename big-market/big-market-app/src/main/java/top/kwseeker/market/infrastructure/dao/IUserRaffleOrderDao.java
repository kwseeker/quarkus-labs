package top.kwseeker.market.infrastructure.dao;

import top.kwseeker.market.infrastructure.dao.po.UserRaffleOrder;
//import top.kwseeker.market.middleware.db.router.annotation.DBRouter;
//import top.kwseeker.market.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 用户抽奖订单表
 * @create 2024-04-03 15:57
 */
@Mapper
//@DBRouterStrategy(splitTable = true)  //TODO
public interface IUserRaffleOrderDao {

    void insert(UserRaffleOrder userRaffleOrder);

    //@DBRouter
    UserRaffleOrder queryNoUsedRaffleOrder(UserRaffleOrder userRaffleOrderReq);

    int updateUserRaffleOrderStateUsed(UserRaffleOrder userRaffleOrderReq);

}
