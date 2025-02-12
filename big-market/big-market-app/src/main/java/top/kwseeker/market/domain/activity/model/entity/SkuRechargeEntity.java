package top.kwseeker.market.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.kwseeker.market.domain.activity.model.valobj.OrderTradeTypeVO;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 活动商品充值实体对象
 * @create 2024-03-23 09:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuRechargeEntity {

    /** 用户ID */
    private String userId;
    /** 商品SKU - activity + activity count */
    private Long sku;
    /** 幂等业务单号，外部谁充值谁透传，这样来保证幂等（多次调用也能确保结果唯一，不会多次充值）。 */
    private String outBusinessNo;
    /** 用户ID */
    @Builder.Default    //支持对此字段的 Builder 链式初始化
    private OrderTradeTypeVO orderTradeType = OrderTradeTypeVO.rebate_no_pay_trade;
}
