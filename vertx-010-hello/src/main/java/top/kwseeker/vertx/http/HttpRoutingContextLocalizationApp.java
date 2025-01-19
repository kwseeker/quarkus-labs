package top.kwseeker.vertx.http;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.LanguageHeader;
import io.vertx.ext.web.Router;

/**
 * 本地化支持
 * Vert.x Web解析 Accept-Language 请求头， 并提供一些帮助方法来确定哪个是客户端的首选语言环境或按质量排序的首选语言环境列表。
 * 测试时可以借助 Chrome 插件 SIMPLE MODIFY HEADERS 修改 Accept-Language 请求头
 */
public class HttpRoutingContextLocalizationApp {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.get("/localized").handler(ctx -> {
            ctx.response().putHeader("Content-Type", "text/plain; charset=utf-8");
            // 尽管通过switch运行循环可能看起来很奇怪，
            // 但我们可以确保在使用用户语言进行响应时，
            // 保留了语言环境的优先顺序。
            for (LanguageHeader language : ctx.acceptableLanguages()) { // 解析 Accept-Language 请求头
                switch (language.tag()) {
                    case "zh":
                        ctx.response().end("你好!");
                        return;
                    case "en":
                        ctx.response().end("Hello!");
                        return;
                    case "fr":
                        ctx.response().end("Bonjour!");
                        return;
                    case "pt":
                        ctx.response().end("Olá!");
                        return;
                    case "es":
                        ctx.response().end("Hola!");
                        return;
                }
            }
            // 我们不知道用户使用的语言，因此请告知
            ctx.response().end("Sorry we don't speak: " + ctx.preferredLanguage());
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
