package top.kwseeker.market.infrastructure.event;

import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.quarkiverse.rabbitmqclient.RabbitMQClient;
import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;
import top.kwseeker.market.types.event.BaseEvent;

import java.io.IOException;
import java.io.UncheckedIOException;

@Slf4j
public abstract class AbstractEventClient {

    protected RabbitMQClient rabbitMQClient;
    protected Channel channel;
    protected RabbitChannelConfigProperties channelProperties;

    public AbstractEventClient(RabbitMQClient rabbitMQClient, RabbitChannelConfigProperties channelProperties) {
        this.rabbitMQClient = rabbitMQClient;
        this.channelProperties = channelProperties;
    }

    public void onApplicationStart(StartupEvent event) {
        log.debug("RabbitMq setup queues ...");
        setupQueues();
    }

    private void setupQueues() {
        try {
            // You do not need to worry about closing connections as the RabbitMQClient
            // will close them for you on application shutdown.
            Connection connection = rabbitMQClient.connect();
            channel = connection.createChannel();
            RabbitChannelConfigProperties.Exchange exchange = channelProperties.exchange();
            RabbitChannelConfigProperties.Queue queue = channelProperties.queue();
            RabbitChannelConfigProperties.Binding binding = channelProperties.binding();
            // 声明一个持久化交换机
            channel.exchangeDeclare(exchange.name(), exchange.type(), exchange.durable());
            // 声明一个持久化、非独占、不自动删除的队列
            channel.queueDeclare(queue.name(), queue.durable(), queue.exclusive(), queue.autoDelete(), null);
            // 绑定队列到交换机， 队列接收交换机 pattern 类型消息
            channel.queueBind(exchange.name(), queue.name(), binding.pattern());
        } catch (IOException e) {
            log.error("setup rabbitmq queues failed", e);
            throw new UncheckedIOException(e);
        }
    }

    protected void publish(String topic, BaseEvent.EventMessage<?> eventMessage) throws IOException {
        publish(topic, JSON.toJSONString(eventMessage));
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
}
