package top.kwseeker.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class MessagePublisherTest {

    @Inject
    MessagePublisher publisher;

    @Test
    public void testPublish() throws InterruptedException {
        publisher.send("Hello, RabbitMQ!");
        Thread.sleep(1000);
    }
}