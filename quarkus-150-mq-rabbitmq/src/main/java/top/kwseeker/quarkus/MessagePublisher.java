package top.kwseeker.quarkus;

import io.quarkiverse.rabbitmqclient.NamedRabbitMQClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.*;
import io.quarkiverse.rabbitmqclient.RabbitMQClient;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class MessagePublisher {

    @Inject
    @NamedRabbitMQClient("client_pub")
    RabbitMQClient rabbitMQClient;

    private Channel channel;

    public void onApplicationStart(@Observes StartupEvent event) {
        setupQueues();
    }

    private void setupQueues() {
        try {
            // create a connection
            Connection connection = rabbitMQClient.connect();
            // create a channel
            channel = connection.createChannel();
            // 声明一个名为 test 的持久化交换机
            channel.exchangeDeclare("test", BuiltinExchangeType.TOPIC, true);
            // 声明一个名为 sample.queue 的持久化、非独占、不自动删除的队列
            channel.queueDeclare("sample.queue", true, false, false, null);
            // 绑定 sample.queue 到 test 交换机， 队列接收 test 交换机所有消息
            channel.queueBind("sample.queue", "test", "#");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void send(String message) {
        try {
            // send a message to the exchange
            channel.basicPublish("test", "#", null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
