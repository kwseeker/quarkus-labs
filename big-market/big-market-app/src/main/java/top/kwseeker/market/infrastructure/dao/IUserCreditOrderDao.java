package top.kwseeker.market.infrastructure.dao;

import top.kwseeker.market.infrastructure.dao.po.UserCreditOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 用户积分流水单 DAO
 * @create 2024-06-01 08:55
 */
@Mapper
//@DBRouterStrategy(splitTable = true)
public interface IUserCreditOrderDao {

    void insert(UserCreditOrder userCreditOrderReq);

}
