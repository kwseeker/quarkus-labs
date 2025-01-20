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

@ExtendWith(VertxExtension.class)
class HttpRouteWithMIMEAppTest {

    private static WebClient webClient;

    @BeforeAll
    static void init() {
        webClient = WebClient.create(Vertx.vertx());
    }

    @Test
    public void testGreetEndpoint(Vertx vertx, VertxTestContext testContext) {
        webClient.get(8080, "localhost", "/")
                .putHeader("Content-Type", "text/html")
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
        webClient.get(8080, "localhost", "/")
                .putHeader("Content-Type", "text/plain")
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