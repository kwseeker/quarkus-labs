package top.kwseeker.vertx.http;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

/**
 * 路由异常处理
 * 两种错误处理器，一种用在路由器上，一种用在路由上
 */
public class HttpRouteExceptionHandlingApp {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);

        // 路由器上的错误处理器
        // RoutingContextImpl 中匹配处理器时如果没有找到匹配的处理器会调用 checkHandleNoMatch()
        // 会查找 Router errorHandlers 中 404 状态码对应的处理器
        // 这里 errorHandler 方法就注册了一个 404 错误处理器到 errorHandlers 中
        router.errorHandler(404, routingContext -> {    // 404 Not Found, 即找不到资源（没有匹配的路由）
            // 这里修改默认返回值
            Throwable failure = routingContext.failure();
            HttpServerResponse response = routingContext.response()
                    .setStatusCode(404)
                    .putHeader("Content-Type", "application/json; charset=UTF-8");
            if (failure != null) {
                String errorMessage = failure.getMessage();
                System.out.println("error: " + errorMessage);
                response.end("{\"error\": \"" + errorMessage + "\"}");
            } else {
                response.end("{\"error\": \"Bad Request\"}");
            }
        });

        router.get("/hello")
                // 这个路由专用的错误处理
                .failureHandler(frCtx -> {
                    int statusCode = frCtx.statusCode();
                    // RuntimeException的状态码将为500
                    // 或403，表示其他失败
                    HttpServerResponse response = frCtx.response();
                    response.setStatusCode(statusCode)
                            .putHeader("Content-Type", "text/plain; charset=UTF-8")
                            .end("服务器内部错误");

                })
                .handler(ctx -> {
                    long l = System.currentTimeMillis();
                    System.out.println(l);
                    if ((l & 1) == 0) {
                        ctx.fail(500);
                    } else {
                        ctx.end("hello");
                    }
                });

        Future<HttpServer> future = httpServer.requestHandler(router).listen(8080);
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
