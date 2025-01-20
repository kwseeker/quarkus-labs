package top.kwseeker.vertx.http;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.apache.commons.io.ThreadUtils;

import java.time.Duration;

public class HttpWithBlockingHandlerApp {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);
        // 阻塞式处理器
        router.route("/calculate").blockingHandler(event -> {
            //假设执行密集计算任务
            try {
                ThreadUtils.sleep(Duration.ofMillis(3000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String result = "result: ...";
            event.response()
                    .putHeader("Content-Length", String.valueOf(result.length()))
                    .write(result);
            event.end();
        });

        Future<HttpServer> future = httpServer
                .requestHandler(router)
                .listen(8080);
        future.andThen(ar -> {
            if (ar.succeeded()) {
                System.out.println("Server start success, listening on port 8080");
            } else {
                System.out.println("Server start failed!");
                ar.cause().printStackTrace();
            }
        });
    }
}
