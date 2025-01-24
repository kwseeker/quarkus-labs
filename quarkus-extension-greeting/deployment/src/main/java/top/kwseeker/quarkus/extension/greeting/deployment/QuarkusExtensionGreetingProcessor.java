package top.kwseeker.quarkus.extension.greeting.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.undertow.deployment.ServletBuildItem;
import top.kwseeker.quarkus.extension.greeting.runtime.GreetingServlet;

class QuarkusExtensionGreetingProcessor {

    private static final String FEATURE = "quarkus-extension-greeting";

    //标记当前方法为构建步骤，在部署（deployment）期间必须执行
    //构建步骤在增强阶段并发（调试可以看到有很多build-x线程）运行增强应用程序
    @BuildStep
    FeatureBuildItem feature() {
        //FeatureBuildItem 用于在应用程序启动时向用户显示信息（FEATURE）
        return new FeatureBuildItem(FEATURE);
    }

    //ServletBuildItem 用于在构建时生成 Servlet 注册的字节码
    @BuildStep
    ServletBuildItem createServlet() {
        return ServletBuildItem.builder("greeting", GreetingServlet.class.getName())
                .addMapping("/greeting")
                .build();
    }
}
