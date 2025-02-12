package top.kwseeker.market.domain.credit.repository;

import top.kwseeker.market.domain.credit.model.aggregate.TradeAggregate;
import top.kwseeker.market.domain.credit.model.entity.CreditAccountEntity;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 用户积分仓储
 * @create 2024-06-01 09:11
 */
public interface ICreditRepository {

    void saveUserCreditTradeOrder(TradeAggregate tradeAggregate);

    CreditAccountEntity queryUserCreditAccount(String userId);

}
