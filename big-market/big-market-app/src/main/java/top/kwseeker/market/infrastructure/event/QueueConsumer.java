package top.kwseeker.market.infrastructure.event;

import com.rabbitmq.client.Consumer;

public interface QueueConsumer extends Consumer {

    String bindQueue();
}
