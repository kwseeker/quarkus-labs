package top.kwseeker.vertx.http;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class HttpRouteWithMIMEApp {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.route()
                // 可以根据请求头的Content-Type来匹配路由， 这里匹配所有请求体内容为 text/html 类型的请求
                .consumes("text/html")
                // 可以根据请求头的Accept来匹配路由, 这里匹配所有接受响应内容为 text/plain 类型的请求
                .produces("text/plain")
                .handler(event -> {
                    String result = "result from route by matching request mime text/html response mime text/plain";
                    event.response()
                            //.putHeader("Content-Length", String.valueOf(result.length()))
                            .putHeader("Content-Type", "text/plain")
                            .end(result);
                });
        router.route()
                .consumes("text/plain")
                .handler(event -> {
                    String result = "result from route by matching mime text/plain";
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
                ar.cause().printStackTrace();
            }
        });
    }
}
