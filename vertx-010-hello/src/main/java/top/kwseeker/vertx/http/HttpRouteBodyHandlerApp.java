package top.kwseeker.vertx.http;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * 请求体处理
 */
public class HttpRouteBodyHandlerApp {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);

        router.post("/user/register")
                //注册默认的请求体处理器 BodyHandlerImpl，内部对请求做了一些处理，比如限制请求体大小等，
                //最后通过 ctx.next() 传递给下一个处理器
                .handler(BodyHandler.create())
                .handler(ctx -> {
                    JsonObject jsonObject = ctx.body().asJsonObject();
                    System.out.println("body: " + jsonObject);

                    ctx.end("done");
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
