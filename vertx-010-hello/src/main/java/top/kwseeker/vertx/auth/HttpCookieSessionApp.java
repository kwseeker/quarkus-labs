package top.kwseeker.vertx.auth;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.Cookie;
import io.vertx.core.http.CookieSameSite;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

import java.util.Date;
import java.util.UUID;

/**
 * Cookie Session 处理
 */
public class HttpCookieSessionApp {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer httpServer = vertx.createHttpServer();
        LocalSessionStore sessionStore = LocalSessionStore.create(vertx, "tokenMap", 10000);

        Router router = Router.router(vertx);
        router.route()
                // 挂载请求日志处理器
                // 信息: 0:0:0:0:0:0:0:1 - - [Mon, 20 Jan 2025 04:40:29 GMT] "GET /token/refresh HTTP/1.1" 200 12 "-"
                // "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36"
                .handler(LoggerHandler.create())
                // 挂载 Session 处理器, 然后路由上下文中才能访问到 Session 对象
                .handler(SessionHandler.create(sessionStore)
                        .setLazySession(true)
                        .setSessionTimeout(2 * 3600 * 1000));

        router.get("/token/refresh").handler(ctx -> {
            //Set<Cookie> cookies = ctx.request().cookies();
            //for (Cookie cookie : cookies) {
            //    // Cookie 在 Vertx 中只有 name 和 value 两个属性被解析出来
            //    // 因为 HTTP 协议的 Cookie 头只包含这两个字段。当浏览器发送请求时，它只会将 name=value 对的形式发送给服务器，而不会发送其他属性（如 Domain、Path、Max-Age 等）
            //    System.out.println(cookie.getName() + "=" + cookie.getValue());
            //}
            Cookie authCookie = ctx.request().getCookie("auth_token");
            if (authCookie != null) {
                String token = authCookie.getValue();
                System.out.println((String) ctx.session().get(token));
            }

            //刷新 Cookie 和 Session
            System.out.println(new Date());
            String newToken = UUID.randomUUID().toString();
            ctx.session().put(newToken, "{\"name\": \"Arvin\", \"Age\":18}");
            System.out.println("new token: " + newToken + ", session value: " + ctx.session().get(newToken));
            // Cookie 通常存储 认证信息、用户偏好设置、访问记录、本地化数据（地理位置等）等信息
            // 其实这里通过Session存储用户认证信息的话无需再额外添加一个 Cookie
            // SessionHandlerImpl 本身会为 Session 创建一个名为 “vertx-web.session” 的 Cookie （记录sessionId）。
            Cookie tokenCookie = Cookie.cookie("auth_token", newToken)
                    .setDomain("localhost")
                    .setHttpOnly(true)  // 将Cookie标记为仅HTTP可用，防止通过JavaScript访问此Cookie，从而提高安全性，减少XSS攻击的风险
                    .setPath("/")       // 该站点所有路径下有效
                    //.setSecure(true)    // Cookie 只能通过 HTTPS 传输
                    .setSameSite(CookieSameSite.STRICT) // 设置同站策略为严格模式 (STRICT)，这意味着浏览器只会发送与发起请求的网站相同的站点请求中的Cookie，进一步防止CSRF攻击。
                    .setMaxAge(9 * 3600);   // 过期时间，单位秒，这里返回的过期时间看上去是相对于美国的时间 ？？？
            ctx.response().addCookie(tokenCookie);
            ctx.end("refresh done");
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
