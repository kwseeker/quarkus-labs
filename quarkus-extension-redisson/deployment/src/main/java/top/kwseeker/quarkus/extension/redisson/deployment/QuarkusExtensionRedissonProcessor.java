package top.kwseeker.quarkus.extension.redisson.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ExtensionSslNativeSupportBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.HotDeploymentWatchedFileBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.deployment.builditem.nativeimage.RuntimeInitializedClassBuildItem;
import org.redisson.RedissonBucket;
import org.redisson.RedissonMultimap;
import org.redisson.RedissonObject;
import org.redisson.api.RBucket;
import org.redisson.api.RExpirable;
import org.redisson.api.RObject;
import org.redisson.api.RObjectReactive;
import org.redisson.codec.Kryo5Codec;
import org.redisson.config.*;
import org.redisson.executor.RemoteExecutorService;
import org.redisson.executor.RemoteExecutorServiceAsync;
import top.kwseeker.quarkus.extension.redisson.runtime.RedissonClientProducer;
import top.kwseeker.quarkus.extension.redisson.runtime.RedissonClientRecorder;

import java.io.IOException;

class QuarkusExtensionRedissonProcessor {

    private static final String FEATURE = "redisson";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    ExtensionSslNativeSupportBuildItem sslNativeSupport() {
        return new ExtensionSslNativeSupportBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem addProducer() {
        // 用于指定在 bean 发现期间要分析的一个或多个额外的 bean 类；默认情况下，如果认为这些 bean 未使用并且启用了 ArcConfig#removeUnusedBeans ，
        // 则生成的 bean 可能会被移除。您可以通过设置 #removable 到 false 以及通过 Builder#setUnremovable() 来更改默认行为。
        // 这里指定 RedissonClientProducer 不可被移除
        return AdditionalBeanBuildItem.unremovableOf(RedissonClientProducer.class);
    }

    @BuildStep
    void addConfig(BuildProducer<NativeImageResourceBuildItem> nativeResources,
                   BuildProducer<HotDeploymentWatchedFileBuildItem> watchedFiles,
                   BuildProducer<RuntimeInitializedClassBuildItem> staticItems,
                   BuildProducer<ReflectiveClassBuildItem> reflectiveItems) {
        nativeResources.produce(new NativeImageResourceBuildItem("redisson.yaml"));
        nativeResources.produce(new NativeImageResourceBuildItem("META-INF/services/org.jboss.marshalling.ProviderDescriptor"));
        // 记录热部署文件，如果修改，在开发模式下会导致热重新部署
        watchedFiles.produce(new HotDeploymentWatchedFileBuildItem("redisson.yaml"));
        // ReflectiveClassBuildItem 用于在原生模式（native mode）下注册类以供反射使用
        reflectiveItems.produce(ReflectiveClassBuildItem.builder(Kryo5Codec.class)
                .methods(false)
                .fields(false)
                .build()
        );
        reflectiveItems.produce(ReflectiveClassBuildItem.builder(
                        RemoteExecutorService.class,
                        RemoteExecutorServiceAsync.class)
                .methods(true)
                .fields(false)
                .build()
        );
        reflectiveItems.produce(ReflectiveClassBuildItem.builder(
                        Config.class,
                        BaseConfig.class,
                        BaseMasterSlaveServersConfig.class,
                        SingleServerConfig.class,
                        ReplicatedServersConfig.class,
                        SentinelServersConfig.class,
                        ClusterServersConfig.class)
                .methods(true)
                .fields(true)
                .build()
        );
        reflectiveItems.produce(ReflectiveClassBuildItem.builder(
                        RBucket.class,
                        RedissonBucket.class,
                        RedissonObject.class,
                        RedissonMultimap.class)
                .methods(true)
                .fields(true)
                .build()
        );
        reflectiveItems.produce(ReflectiveClassBuildItem.builder(
                        RObjectReactive.class,
                        RExpirable.class,
                        RObject.class)
                .methods(true)
                .build()
        );
    }

    // 这个方法分两部处理：
    // 1 构建时增强生成一个 StartupTask 类，如 QuarkusExtensionRedissonProcessor$build620656674.class，这个类 deploy_0() 方法内执行 recorder.createProducer()
    // 2 在运行时执行 StartupTask 类的 deploy_0() 方法，执行 recorder.createProducer(), 创建 RedissonClientProducer Bean
    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    RedissonClientItemBuild build(RedissonClientRecorder recorder) throws IOException {
        recorder.createProducer();
        return new RedissonClientItemBuild();
    }
    // QuarkusExtensionRedissonProcessor$build620656674.class
    //    public void deploy(StartupContext var1) {
    //        var1.setCurrentBuildStepName("QuarkusExtensionRedissonProcessor.build");
    //        Object[] var2 = this.$quarkus$createArray();
    //        this.deploy_0(var1, var2);
    //    }
    //
    //    public void deploy_0(StartupContext var1, Object[] var2) {
    //        (new RedissonClientRecorder()).createProducer();
    //    }
}
