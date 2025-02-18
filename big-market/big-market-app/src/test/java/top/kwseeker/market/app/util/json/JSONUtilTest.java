package top.kwseeker.market.app.util.json;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import top.kwseeker.market.domain.award.adapter.event.SendAwardMessageEvent;
import top.kwseeker.market.types.event.BaseEvent;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JSONUtilTest {

    @Test
    public void testJson() {
        User user = new User("Arvin", 18);
        String jsonString = JSONUtil.toJSONString(user);
        assertEquals("{\"name\":\"Arvin\",\"age\":18}", jsonString);

        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> eventMessage = new BaseEvent.EventMessage<>(
                "1", new Date(), new SendAwardMessageEvent.SendAwardMessage("10001", "123", 101, "title", "config"));
        String jsonString2 = JSONUtil.toJSONString(eventMessage);
        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> sendAwardMessage = JSONUtil.parseObject(jsonString2,
                new TypeReference<BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage>>() {});
    }

    @Data
    @AllArgsConstructor
    static class User {
        private String name;
        private int age;
    }
}