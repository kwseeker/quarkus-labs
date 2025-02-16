package top.kwseeker.market.domain.activity.adapter.event;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import top.kwseeker.market.types.event.BaseEvent;

import java.util.Date;

/**
 * 活动sku库存清空消息
 */
@ApplicationScoped
public class ActivitySkuStockZeroMessageEvent extends BaseEvent<Long> {

    @ConfigProperty(name = "app.rabbitmq.topic.activity_sku_stock_zero")
    String topic;

    @Override
    public EventMessage<Long> buildEventMessage(Long sku) {
        return EventMessage.<Long>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(sku)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }

}