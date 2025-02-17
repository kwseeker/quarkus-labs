package top.kwseeker.market.infrastructure.event;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

import java.util.List;

@ConfigMapping(prefix = "app.rabbitmq.topic")
public interface TopicsConfig {
    @WithName("activity_sku_stock_zero")
    String activitySkuStockZero();

    @WithName("send_award")
    String sendAward();

    @WithName("send_rebate")
    String sendRebate();

    @WithName("credit_adjust_success")
    String creditAdjustSuccess();

    default List<String> getAllTopics() {
        return List.of(
            activitySkuStockZero(),
            sendAward(),
            sendRebate(),
            creditAdjustSuccess()
        );
    }
}