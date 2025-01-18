package top.kwseeker.vertx.http;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class HttpRouteWithVirtualHostApp {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.route().virtualHost("localhost")
                .handler(event -> {
                    String result = "result from route by matching virtualhost";
                    event.response()
                            .putHeader("Content-Type", "text/plain")
                            .end(result);
                });

        Future<HttpServer> future = httpServer
                .requestHandler(router)
                .listen(8080);
        future.andThen(ar -> {
            if (ar.succeeded()) {
                System.out.println("Server start success, listening on port 8080");
            } else {
                System.out.println("Server start failed!");
                System.out.println(ar.cause().getMessage());
            }
        });
    }
}
