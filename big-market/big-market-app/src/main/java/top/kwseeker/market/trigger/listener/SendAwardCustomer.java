package top.kwseeker.market.trigger.listener;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import top.kwseeker.market.domain.award.adapter.event.SendAwardMessageEvent;
import top.kwseeker.market.domain.award.model.entity.DistributeAwardEntity;
import top.kwseeker.market.domain.award.service.IAwardService;
import top.kwseeker.market.infrastructure.event.DefaultEventClient;
import top.kwseeker.market.types.event.BaseEvent;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.rabbitmq.client.DefaultConsumer;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import top.kwseeker.market.infrastructure.event.QueueConsumer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 用户奖品记录消息消费者
 * @create 2024-04-06 12:09
 */
@Slf4j
@ApplicationScoped
public class SendAwardCustomer extends DefaultConsumer implements QueueConsumer {

    @ConfigProperty(name = "spring.rabbitmq.topic.send_award")
    String topic;

    private final IAwardService awardService;

    @Inject
    public SendAwardCustomer(DefaultEventClient eventClient, IAwardService awardService) {
        super(eventClient.channel());
        this.awardService = awardService;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                               byte[] body) throws IOException {
        String message = new String(body, StandardCharsets.UTF_8);
        try {
            log.info("监听用户奖品发送消息，发奖开始 topic: {} message: {}", topic, message);
            BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage>>() {
            }.getType());
            SendAwardMessageEvent.SendAwardMessage sendAwardMessage = eventMessage.getData();

            // 发放奖品
            DistributeAwardEntity distributeAwardEntity = new DistributeAwardEntity();
            distributeAwardEntity.setUserId(sendAwardMessage.getUserId());
            distributeAwardEntity.setOrderId(sendAwardMessage.getOrderId());
            distributeAwardEntity.setAwardId(sendAwardMessage.getAwardId());
            distributeAwardEntity.setAwardConfig(sendAwardMessage.getAwardConfig());
            awardService.distributeAward(distributeAwardEntity);

            log.info("监听用户奖品发送消息，发奖完成 topic: {} message: {}", topic, message);
        } catch (Exception e) {
            log.error("监听用户奖品发送消息，消费失败 topic: {} message: {}", topic, message);
            //throw e;
        }
    }

    @Override
    public String bindQueue() {
        return topic;
    }
}
