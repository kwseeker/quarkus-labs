package top.kwseeker.vertx.http;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.nio.charset.StandardCharsets;

/**
 * 子路由器
 */
public class HttpRouteSubRouterApp {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();

        //作为主路由器
        Router mainRouter = Router.router(vertx);
        mainRouter.route("/mall/index.html").handler(ctx -> {
            String content = "<h1>商城主页</h1>";
            ctx.response()
                    //.putHeader("Content-Type", "text/html")   // 会乱码
                    //.putHeader("Content-Length", String.valueOf(content.length()))
                    .putHeader("Content-Type", "text/html; charset=UTF-8")
                    .putHeader("Content-Length", String.valueOf(content.getBytes(StandardCharsets.UTF_8).length))
                    .write(content);
            ctx.end();
        });

        //作为子路由器
        Router productRouter = Router.router(vertx);
        productRouter.get("/:productID").handler(ctx -> {
            System.out.println("查询商品，ID:" + ctx.pathParam("productID"));
            JsonObject goods = new JsonObject()
                    .put("item", "RTX4090")
                    .put("origin_price", 5000);
            ctx.json(new JsonArray().add(goods));
        });
        productRouter.put("/:productID").handler(ctx -> {
            System.out.println("新增商品，ID:" + ctx.pathParam("productID"));
            ctx.response().end();
        });
        productRouter.delete("/:productID").handler(ctx -> {
            System.out.println("下架商品，ID:" + ctx.pathParam("productID"));
            ctx.response().end();
        });

        // 子路由器挂载到主路由器
        mainRouter.route("/mall/product/*").subRouter(productRouter);

        Future<HttpServer> future = httpServer.requestHandler(mainRouter).listen(8080);
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
