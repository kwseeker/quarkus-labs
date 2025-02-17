package top.kwseeker.market.trigger.listener;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import top.kwseeker.market.domain.activity.model.entity.DeliveryOrderEntity;
import top.kwseeker.market.domain.activity.service.IRaffleActivityAccountQuotaService;
import top.kwseeker.market.domain.credit.event.CreditAdjustSuccessMessageEvent;
import top.kwseeker.market.infrastructure.event.AbstractQueueConsumer;
import top.kwseeker.market.infrastructure.event.DefaultEventClient;
import top.kwseeker.market.types.enums.ResponseCode;
import top.kwseeker.market.types.event.BaseEvent;
import top.kwseeker.market.types.exception.AppException;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 积分调整成功消息
 * @create 2024-06-08 19:38
 */
@Slf4j
@ApplicationScoped
public class CreditAdjustSuccessConsumer extends AbstractQueueConsumer {

    @ConfigProperty(name = "app.rabbitmq.topic.credit_adjust_success")
    String topic;

    IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    public CreditAdjustSuccessConsumer() {
    }

    @Inject
    public CreditAdjustSuccessConsumer(DefaultEventClient eventClient,
                                       IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService) {
        super(eventClient.channel());
        this.raffleActivityAccountQuotaService = raffleActivityAccountQuotaService;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                               byte[] body) throws IOException {
        String message = new String(body, StandardCharsets.UTF_8);
        try {
            log.info("监听积分账户调整成功消息，进行交易商品发货 topic: {} message: {}", topic, message);
            BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage>>() {
            }.getType());
            CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage creditAdjustSuccessMessage = eventMessage.getData();

            // 积分发货
            DeliveryOrderEntity deliveryOrderEntity = new DeliveryOrderEntity();
            deliveryOrderEntity.setUserId(creditAdjustSuccessMessage.getUserId());
            deliveryOrderEntity.setOutBusinessNo(creditAdjustSuccessMessage.getOutBusinessNo());
            raffleActivityAccountQuotaService.updateOrder(deliveryOrderEntity);
        } catch (AppException e) {
            if (ResponseCode.INDEX_DUP.getCode().equals(e.getCode())) {
                log.warn("监听积分账户调整成功消息，进行交易商品发货，消费重复 topic: {} message: {}", topic, message, e);
                return;
            }
            throw e;
        } catch (Exception e) {
            log.error("监听积分账户调整成功消息，进行交易商品发货失败 topic: {} message: {}", topic, message, e);
            throw e;
        }
    }

    @Override
    public String bindQueue() {
        return topic;
    }
}
