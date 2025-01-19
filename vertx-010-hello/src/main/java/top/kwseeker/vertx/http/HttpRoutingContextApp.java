package top.kwseeker.vertx.http;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

import java.util.HashMap;
import java.util.Map;

/**
 * RoutingContext 可用于保存任何在请求生命周期内您想在多个handler之间共享的数据
 */
public class HttpRoutingContextApp {

    private static final Map<String, Gender> userInfos = new HashMap<>();

    static {
        userInfos.put("Arvin", Gender.Male);
        userInfos.put("Nancy", Gender.Female);
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);
        // RoutingContext 上下文数据在多个Handler中共享
        router.get("/user/*")
                .putMetadata("default-gender", Gender.Unknown)  // 路由元数据
                .handler(ctx -> {
                    String name = ctx.request().getParam("name");
                    Gender gender = userInfos.get(name);
                    ctx.put("gender", gender);
                    ctx.next();
                });
        router.get("/user/greet").handler(ctx -> {
            Gender gender = ctx.get("gender");
            if (gender == null) {
                gender = ctx.get("default-gender");
            }
            String greetWords = gender == Gender.Male ? "Mr." : gender == Gender.Female ? "Ms." : "Hello";
            ctx.response().end(greetWords);
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

    enum Gender {
        Unknown, Male, Female;
    }
}
