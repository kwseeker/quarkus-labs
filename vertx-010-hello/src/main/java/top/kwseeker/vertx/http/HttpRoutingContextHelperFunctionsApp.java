package top.kwseeker.vertx.http;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * RoutingContext 帮手函数（helper function）
 */
public class HttpRoutingContextHelperFunctionsApp {

    private static final Map<String, String> userTokens = new HashMap<>();

    static {
        userTokens.put("10001", "xxx");
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);
        // 1 重定向函数：假设获取用户信息，用户没登录，重定向到登录页
        router.get("/user/info").handler(ctx -> {
            String userId = ctx.request().getParam("userId");
            String token = userTokens.get(userId);
            if (token == null) {
                ctx.redirect("http://localhost:8080/login");
            }
            //String responseBody = "{name:Arvin, age:18}";
            //ctx.response()
            //        .putHeader("Content-Type", "application/json")
            //        .putHeader("Content-Length", Integer.toString(responseBody.length()))
            //        .write(responseBody);
            // 2 使用助手函数 json() 简化上面的代码
            ctx.json(new JsonObject().put("name", "Arvin").put("age", 18));
        });
        router.get("/login").handler(ctx -> {
            String content = "<h1>登录页</h1>";
            int length = content.getBytes(StandardCharsets.UTF_8).length;
            System.out.println("content length:" + length);
            HttpServerResponse response = ctx.response();
            // 3 使用助手函数 is() 对content-type进行校验
            if (!response.headers().contains("Content-Length") || !ctx.is("text/html; charset=UTF-8")) {
                response.putHeader("Content-Type", "text/html; charset=UTF-8");
            }
            response.putHeader("Content-Length", Integer.toString(length))
                    .write(content);
            ctx.end();
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
