package top.kwseeker.quarkus.extension.greeting.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class QuarkusExtensionGreetingProcessor {

    private static final String FEATURE = "quarkus-extension-greeting";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
