package top.kwseeker.market.infrastructure.dao;

import top.kwseeker.market.infrastructure.dao.po.UserBehaviorRebateOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 用户行为返利流水订单表
 * @create 2024-04-30 13:48
 */
@Mapper
//@DBRouterStrategy(splitTable = true)  //TODO
public interface IUserBehaviorRebateOrderDao {

    void insert(UserBehaviorRebateOrder userBehaviorRebateOrder);

    //@DBRouter
    List<UserBehaviorRebateOrder> queryOrderByOutBusinessNo(UserBehaviorRebateOrder userBehaviorRebateOrderReq);

}
