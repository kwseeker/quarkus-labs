package top.kwseeker.vertx.http;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(VertxExtension.class)
class HttpRoutingContextAppTest {

    private static WebClient webClient;

    @BeforeAll
    static void init() {
        webClient = WebClient.create(Vertx.vertx());
    }

    @Test
    public void testGreetEndpoint(Vertx vertx, VertxTestContext testContext) {
        List<String> names = Arrays.asList("Arvin", "Bob", "Nancy");
        for (String name : names) {
            webClient.get(8080, "localhost", "/user/greet")
                    .addQueryParam("name", name)
                    .send(ar -> {
                        if (ar.succeeded()) {
                            HttpResponse<Buffer> response = ar.result();
                            System.out.println("Response: code: " + response.statusCode() + ", content: " + response.bodyAsString());
                            testContext.completeNow();
                        } else {
                            System.out.println("Error occurred: " + ar.cause().getMessage());
                            testContext.failNow(ar.cause());
                        }
                    });
        }
    }
}