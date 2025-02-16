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
public class MessageSubscriber {

    @Inject
    @NamedRabbitMQClient("client_sub")
    RabbitMQClient rabbitMQClient;

    private Channel channel;

    public void onApplicationStart(@Observes StartupEvent event) {
        setupQueues();
        setupReceiving();
    }

    private void setupQueues() {
        try {
            // create a connection
            Connection connection = rabbitMQClient.connect();
            // create a channel
            channel = connection.createChannel();
            // declare exchanges and queues
            channel.exchangeDeclare("test", BuiltinExchangeType.TOPIC, true);
            channel.queueDeclare("sample.queue", true, false, false, null);
            channel.queueBind("sample.queue", "test", "#");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void setupReceiving() {
        try {
            // register a consumer for messages
            channel.basicConsume("sample.queue", true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    System.out.println("Received: " + new String(body, StandardCharsets.UTF_8));
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
