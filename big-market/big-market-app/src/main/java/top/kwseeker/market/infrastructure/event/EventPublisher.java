package top.kwseeker.market.infrastructure.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import top.kwseeker.market.types.event.BaseEvent;

import java.io.IOException;

@Slf4j
@ApplicationScoped
public class EventPublisher {

    private final DefaultEventClient eventClient;

    @Inject
    public EventPublisher(DefaultEventClient eventClient) {
        this.eventClient = eventClient;
    }

    public void publish(String topic, BaseEvent.EventMessage<?> eventMessage) {
        try {
            eventClient.publish(topic, eventMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void publish(String topic, String message) {
        try {
            eventClient.publish(topic, message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
