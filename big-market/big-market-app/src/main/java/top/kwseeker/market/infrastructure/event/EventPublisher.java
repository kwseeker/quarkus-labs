package top.kwseeker.market.infrastructure.event;

import io.quarkiverse.rabbitmqclient.RabbitMQClient;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import top.kwseeker.market.types.event.BaseEvent;

import java.io.IOException;

@Slf4j
@ApplicationScoped
public class EventPublisher extends AbstractEventClient {

    @Inject
    public EventPublisher(RabbitMQClient rabbitMQClient, RabbitChannelConfigProperties channelProperties) {
        super(rabbitMQClient, channelProperties);
    }

    @Override
    public void onApplicationStart(@Observes StartupEvent event) {
        super.onApplicationStart(event);
    }

    public void publish(String topic, BaseEvent.EventMessage<?> eventMessage) {
        try {
            super.publish(topic, eventMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void publish(String topic, String message) {
        try {
            super.publish(topic, message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
