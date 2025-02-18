package top.kwseeker.market.infrastructure.event;

import com.rabbitmq.client.*;
import io.quarkiverse.rabbitmqclient.RabbitMQClient;
import io.quarkus.arc.All;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import top.kwseeker.market.app.util.json.JSONUtil;
import top.kwseeker.market.types.event.BaseEvent;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

@Slf4j
@ApplicationScoped
public class DefaultEventClient {

    private final RabbitMQClient rabbitMQClient;
    private final RabbitChannelConfig channelProperties;
    private final List<QueueConsumer> consumers;
    private TopicsConfig topicsConfig;
    private Channel channel;

    @Inject
    public DefaultEventClient(RabbitMQClient rabbitMQClient,
                              RabbitChannelConfig channelProperties,
                              @All List<QueueConsumer> consumers,
                              TopicsConfig topicsConfig) {
        this.rabbitMQClient = rabbitMQClient;
        this.channelProperties = channelProperties;
        this.consumers = consumers;
        this.topicsConfig = topicsConfig;
    }

    public void onApplicationStart(@Observes StartupEvent event) {
        log.debug("RabbitMq setup queues and consumers ...");
        setupQueues();
        setupConsumers();
    }

    private void setupQueues() {
        try {
            // You do not need to worry about closing connections as the RabbitMQClient
            // will close them for you on application shutdown.
            Connection connection = rabbitMQClient.connect();
            channel = connection.createChannel();
            RabbitChannelConfig.Exchange exchange = channelProperties.exchange();
            RabbitChannelConfig.Queue queue = channelProperties.queue();
            // 这里实现一个交换机绑定多个队列（使用主题进行绑定），每一个队列绑定一个消费者
            // 声明一个持久化交换机
            channel.exchangeDeclare(exchange.name(), exchange.type(), exchange.durable());
            for (String topic : topicsConfig.getAllTopics()) {
                // 声明几个持久化、非独占、不自动删除的队列
                channel.queueDeclare(topic, queue.durable(), queue.exclusive(), queue.autoDelete(), null);
                // 绑定队列到交换机， 队列接收交换机 pattern 类型消息
                channel.queueBind(topic, exchange.name(), topic);
            }
        } catch (IOException e) {
            log.error("setup rabbitmq queues failed", e);
            throw new UncheckedIOException(e);
        }
    }

    private void setupConsumers() {
        try {
            for (QueueConsumer consumer : consumers) {
                // 从这个接口看消费者和队列的绑定是通过队列名称进行绑定
                this.channel.basicConsume(consumer.bindQueue(), true, consumer);
            }
        } catch (IOException e) {
            log.error("setup rabbitmq consumers failed", e);
            throw new UncheckedIOException(e);
        }
    }

    protected void publish(String topic, BaseEvent.EventMessage<?> eventMessage) throws IOException {
        publish(topic, JSONUtil.toJSONString(eventMessage));
    }

    protected void publish(String topic, String message) throws IOException {
        publish(channelProperties.exchange().name(), topic, null, message);
    }

    /**
     * routingKey 对 TOPIC 交换机来讲就是 topic
     */
    protected void publish(String exchange, String routingKey, AMQP.BasicProperties basicProperties,
                           String eventMessageJSON) throws IOException {
        try {
            channel.basicPublish(exchange, routingKey, basicProperties, eventMessageJSON.getBytes());
            log.info("发送MQ消息 exchange:{} routingKey:{} basicProperties:{} message:{}",
                    exchange, routingKey, basicProperties, eventMessageJSON);
        } catch (IOException e) {
            log.error("发送MQ消息失败 exchange:{} routingKey:{} basicProperties:{} message:{}",
                    exchange, routingKey, basicProperties, eventMessageJSON, e);
            throw e;
        }
    }

    public Channel channel() {
        return channel;
    }
}
