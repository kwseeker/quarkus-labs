package top.kwseeker.market.infrastructure.event;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;

public abstract class AbstractQueueConsumer implements QueueConsumer {

    private Channel _channel;
    private volatile String _consumerTag;

    public AbstractQueueConsumer() {
    }

    public AbstractQueueConsumer(Channel channel) {
        this._channel = channel;
    }

    public void handleConsumeOk(String consumerTag) {
        this._consumerTag = consumerTag;
    }

    public void handleCancelOk(String consumerTag) {
    }

    public void handleCancel(String consumerTag) throws IOException {
    }

    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
    }

    public void handleRecoverOk(String consumerTag) {
    }

    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
    }

    public Channel getChannel() {
        return this._channel;
    }

    public String getConsumerTag() {
        return this._consumerTag;
    }
}
