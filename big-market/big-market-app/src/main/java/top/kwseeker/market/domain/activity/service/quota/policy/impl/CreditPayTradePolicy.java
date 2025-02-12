package top.kwseeker.market.domain.activity.service.quota.policy.impl;

import top.kwseeker.market.domain.activity.adapter.repository.IActivityRepository;
import top.kwseeker.market.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import top.kwseeker.market.domain.activity.model.valobj.OrderStateVO;
import top.kwseeker.market.domain.activity.service.quota.policy.ITradePolicy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 积分兑换，支付类订单
 * @create 2024-06-08 18:12
 */
@ApplicationScoped
@Named("credit_pay_trade")
public class CreditPayTradePolicy implements ITradePolicy {

    private final IActivityRepository activityRepository;

    public CreditPayTradePolicy(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        createQuotaOrderAggregate.setOrderState(OrderStateVO.wait_pay);
        activityRepository.doSaveCreditPayOrder(createQuotaOrderAggregate);
    }

}
