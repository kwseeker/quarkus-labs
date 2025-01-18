package top.kwseeker.vertx.http;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class HttpWithRouterApp {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();

        // 路由器本质也是一个Handler, 拓展了一些路由功能
        Router router = Router.router(vertx);
        // 兜底路由 注册一个可以匹配所有请求和失败的路由 TODO
        // Route handler(Handler<RoutingContext> requestHandler);
        // order() 可以设置路由顺序，兜底路由放到最后
        router.route().order(99).handler(event -> {
            event.response()
                    .putHeader("content-type", "text/plain; charset=UTF-8") // 需要指定字符集
                    .end("消息来自兜底的路由");
        });
        // 匹配 /hello 的路由
        router.route("/hello")
                .handler(event -> { // 可以注册多个路由处理器
                    event.response()
                            .setChunked(true)   // 开启分块响应
                            .putHeader("content-type", "text/plain; charset=UTF-8")
                            .write("route1\n");
                    //延迟1秒后执行此路由的其他处理器
                    event.vertx().setTimer(1000, tid -> event.next());
                })
                .handler(event -> {
                    event.response()
                            .write("route2\n");
                    //延迟1秒后执行此路由的其他处理器
                    event.vertx().setTimer(1000, tid -> event.next());
                })
                .handler(event -> {
                    event.response()
                            .write("route3");
                    event.end();
                });
        // 匹配 /greet 的路由, 更加简单的写法
        // :name 表示捕捉路径参数name
        router.get("/greet/:name").respond(event -> {
            String name = event.pathParam("name");
            return Future.succeededFuture(new JsonObject().put("hello", name));
        });
        // 还支持通过正则表达式匹配，需要 -Dio.vertx.web.route.param.extended-pattern=true
        //Route route = router.route().pathRegex(".*foo");
        //router.route().pathRegex("/say-.+").respond(event -> {
        router.routeWithRegex(HttpMethod.GET, "/say-.+").respond(event -> {
            String uri = event.request().uri();
            int i = uri.lastIndexOf("say-") + "say-".length();
            return Future.succeededFuture(new JsonObject().put("say", uri.substring(i)));
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
