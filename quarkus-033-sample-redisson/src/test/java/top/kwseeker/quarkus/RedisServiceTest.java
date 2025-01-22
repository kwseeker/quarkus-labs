package top.kwseeker.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class RedisServiceTest {

    @Inject
    RedisService redisService;

    @Test
    public void test() throws InterruptedException {
        String key = "quarkus-redisson:one-key";
        String value = "Hello Quarkus Redisson";
        redisService.setValue(key, value, 3000);
        String returnedValue = redisService.getValue(key);
        System.out.println(returnedValue);
        assertEquals(value, returnedValue);

        Thread.sleep(3500);
        returnedValue = redisService.getValue(key);
        assertNull(returnedValue);
    }
}