package top.kwseeker.market.domain.activity.service.quota.policy.impl;

import top.kwseeker.market.domain.activity.adapter.repository.IActivityRepository;
import top.kwseeker.market.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import top.kwseeker.market.domain.activity.model.valobj.OrderStateVO;
import top.kwseeker.market.domain.activity.service.quota.policy.ITradePolicy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.math.BigDecimal;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 返利无支付交易订单，直接充值到账
 * @create 2024-06-08 18:10
 */
@ApplicationScoped
@Named("rebate_no_pay_trade")
public class RebateNoPayTradePolicy implements ITradePolicy {

    private final IActivityRepository activityRepository;

    public RebateNoPayTradePolicy(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        // 不需要支付则修改订单金额为0，状态为完成，直接给用户账户充值
        createQuotaOrderAggregate.setOrderState(OrderStateVO.completed);
        createQuotaOrderAggregate.getActivityOrderEntity().setPayAmount(BigDecimal.ZERO);
        activityRepository.doSaveNoPayOrder(createQuotaOrderAggregate);
    }

}
