package top.kwseeker.quarkus.extension.redisson.it;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
//@Testcontainers   // 使用 Docker 容器启动某些依赖的中间件（如：redis服务器）
public class QuarkusExtensionRedissonResourceTest {

    @Inject
    RedissonClient redisson;

    @Test
    public void testRedissonClient() {
        RMap<String, Integer> m = redisson.getMap("test");
        m.put("1", 2);
        assertEquals("2", m.get("1").toString());
    }
}
