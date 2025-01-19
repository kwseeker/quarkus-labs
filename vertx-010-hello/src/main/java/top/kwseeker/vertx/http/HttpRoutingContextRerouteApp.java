package top.kwseeker.vertx.http;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.util.HashMap;
import java.util.Map;

/**
 * RoutingContext 重新路由
 */
public class HttpRoutingContextRerouteApp {

    private static final Map<String, String> vips = new HashMap<>();

    static {
        vips.put("10001", "xxx");
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);
        // 比如对 vip 用户展示额外商品信息, 比如有折扣
        router.get("/mall").handler(ctx -> {
            String userId = ctx.request().getParam("userId");
            String vip = vips.get(userId);
            if (vip != null) {
                ctx.reroute("/mall/vip");   //重路由
            }
            JsonObject goods = new JsonObject()
                    .put("item", "RTX4090")
                    .put("origin_price", 5000);
            ctx.json(new JsonArray().add(goods));
        });
        router.get("/mall/vip").handler(ctx -> {
            String userId = ctx.request().getParam("userId");
            String vip = vips.get(userId);
            if (vip == null) {
                ctx.reroute("/mall");       //重路由
            }
            JsonObject goods = new JsonObject()
                    .put("item", "RTX4090")
                    .put("origin_price", 5000)
                    .put("discount", 0.95);
            ctx.json(new JsonArray().add(goods));
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
