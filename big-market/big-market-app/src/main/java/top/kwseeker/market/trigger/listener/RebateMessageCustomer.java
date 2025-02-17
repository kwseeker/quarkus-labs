package top.kwseeker.market.trigger.listener;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import top.kwseeker.market.domain.activity.model.entity.SkuRechargeEntity;
import top.kwseeker.market.domain.activity.model.valobj.OrderTradeTypeVO;
import top.kwseeker.market.domain.activity.service.IRaffleActivityAccountQuotaService;
import top.kwseeker.market.domain.credit.model.entity.TradeEntity;
import top.kwseeker.market.domain.credit.model.valobj.TradeNameVO;
import top.kwseeker.market.domain.credit.model.valobj.TradeTypeVO;
import top.kwseeker.market.domain.credit.service.ICreditAdjustService;
import top.kwseeker.market.domain.rebate.event.SendRebateMessageEvent;
import top.kwseeker.market.infrastructure.event.AbstractQueueConsumer;
import top.kwseeker.market.infrastructure.event.DefaultEventClient;
import top.kwseeker.market.types.enums.ResponseCode;
import top.kwseeker.market.types.event.BaseEvent;
import top.kwseeker.market.types.exception.AppException;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 监听；行为返利消息
 * @create 2024-05-01 13:58
 */
@Slf4j
@ApplicationScoped
public class RebateMessageCustomer extends AbstractQueueConsumer {

    @ConfigProperty(name = "app.rabbitmq.topic.send_rebate")
    String topic;

    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;
    private ICreditAdjustService creditAdjustService;

    public RebateMessageCustomer() {
    }

    @Inject
    public RebateMessageCustomer(DefaultEventClient eventClient,
                                 IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService,
                                 ICreditAdjustService creditAdjustService) {
        super(eventClient.channel());
        this.raffleActivityAccountQuotaService = raffleActivityAccountQuotaService;
        this.creditAdjustService = creditAdjustService;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                               byte[] body) throws IOException {
        String message = new String(body, StandardCharsets.UTF_8);
        try {
            log.info("监听用户行为返利消息 topic: {} message: {}", topic, message);
            // 1. 转换消息
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage>>() {
            }.getType());
            SendRebateMessageEvent.RebateMessage rebateMessage = eventMessage.getData();

            // 2. 入账奖励
            switch (rebateMessage.getRebateType()) {
                case "sku":
                    SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
                    skuRechargeEntity.setUserId(rebateMessage.getUserId());
                    skuRechargeEntity.setSku(Long.valueOf(rebateMessage.getRebateConfig()));
                    skuRechargeEntity.setOutBusinessNo(rebateMessage.getBizId());
                    skuRechargeEntity.setOrderTradeType(OrderTradeTypeVO.rebate_no_pay_trade);
                    raffleActivityAccountQuotaService.createOrder(skuRechargeEntity);
                    break;
                case "integral":
                    TradeEntity tradeEntity = new TradeEntity();
                    tradeEntity.setUserId(rebateMessage.getUserId());
                    tradeEntity.setTradeName(TradeNameVO.REBATE);
                    tradeEntity.setTradeType(TradeTypeVO.FORWARD);
                    tradeEntity.setAmount(new BigDecimal(rebateMessage.getRebateConfig()));
                    tradeEntity.setOutBusinessNo(rebateMessage.getBizId());
                    creditAdjustService.createOrder(tradeEntity);
                    break;
            }
        } catch (AppException e) {
            if (ResponseCode.INDEX_DUP.getCode().equals(e.getCode())) {
                log.warn("监听用户行为返利消息，消费重复 topic: {} message: {}", topic, message, e);
                return;
            }
            if (ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getCode().equals(e.getCode())) {
                log.warn("监听用户行为返利消息，活动库存不足 topic: {} message: {}", topic, message, e);
                return;
            }
            throw e;
        } catch (Exception e) {
            log.error("监听用户行为返利消息，消费失败 topic: {} message: {}", topic, message, e);
            throw e;
        }
    }

    @Override
    public String bindQueue() {
        return topic;
    }
}
