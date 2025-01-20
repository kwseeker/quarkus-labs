package top.kwseeker.market;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * 这个不是真正的主类
 */
@QuarkusMain
public class AppMain implements QuarkusApplication {

    @Override
    public int run(String... args) {
        //这里可以拓展自定义启动逻辑
        //...
        Quarkus.waitForExit();  // 要使用 @QuarkusMain 自定义“主类”，需要使用 Quarkus.waitForExit() 避免其Web组件自动退出
        return 0;
    }

    // 注意这个并不是程序入口
    public static void main(String... args) {
        Quarkus.run(AppMain.class, args);
    }
}