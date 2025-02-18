package top.kwseeker.market.trigger.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import top.kwseeker.market.app.util.json.JSONUtil;
import top.kwseeker.market.domain.activity.service.IRaffleActivitySkuStockService;
import top.kwseeker.market.infrastructure.event.AbstractQueueConsumer;
import top.kwseeker.market.infrastructure.event.DefaultEventClient;
import top.kwseeker.market.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 活动sku库存耗尽
 * @create 2024-03-30 12:31
 */
@Slf4j
@ApplicationScoped
public class ActivitySkuStockZeroConsumer extends AbstractQueueConsumer {

    @ConfigProperty(name = "app.rabbitmq.topic.activity_sku_stock_zero")
    String topic;

    private IRaffleActivitySkuStockService skuStock;

    public ActivitySkuStockZeroConsumer() {
    }

    @Inject
    public ActivitySkuStockZeroConsumer(DefaultEventClient eventClient, IRaffleActivitySkuStockService skuStock) {
        super(eventClient.channel());
        this.skuStock = skuStock;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                               byte[] body) throws IOException {
        String message = new String(body, StandardCharsets.UTF_8);
        try {
            log.info("监听活动sku库存消耗为0消息 topic: {} message: {}", topic, message);
            // 转换对象
            BaseEvent.EventMessage<Long> eventMessage = JSONUtil.parseObject(message,
                    new TypeReference<BaseEvent.EventMessage<Long>>() {});
            Long sku = eventMessage.getData();
            // 更新库存
            skuStock.clearActivitySkuStock(sku);
            // 清空队列
            skuStock.clearQueueValue(sku);
        } catch (Exception e) {
            log.error("监听活动sku库存消耗为0消息，消费失败 topic: {} message: {}", topic, message);
            throw e;
        }
    }

    @Override
    public String bindQueue() {
        return topic;
    }
}
