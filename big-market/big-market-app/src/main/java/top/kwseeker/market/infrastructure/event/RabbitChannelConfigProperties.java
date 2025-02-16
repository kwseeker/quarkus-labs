package top.kwseeker.market.infrastructure.event;

import com.rabbitmq.client.BuiltinExchangeType;
import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;

@StaticInitSafe
@ConfigMapping(prefix = "app.rabbitmq.channel")
public interface RabbitChannelConfigProperties {

    Exchange exchange();
    Queue queue();

    //// 声明一个名为 test 的持久化交换机
    //channel.exchangeDeclare("test", BuiltinExchangeType.TOPIC, true);
    //// 声明一个名为 sample.queue 的持久化、非独占、不自动删除的队列
    //channel.queueDeclare("sample.queue", true, false, false, null);
    //// 绑定 sample.queue 到 test 交换机， 队列接收 test 交换机所有消息
    //channel.queueBind("sample.queue", "test", "#");
    interface Exchange {
        String name();
        BuiltinExchangeType type();
        Boolean durable();
    }

    interface Queue {
        Boolean durable();
        Boolean exclusive();
        Boolean autoDelete();
    }
}