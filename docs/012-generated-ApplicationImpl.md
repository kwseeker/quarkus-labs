# Quarkus ApplicationImpl

通过 ClassLoader 读取生成的 ApplicationImpl.class 内容：

```
Files.write(Path.of("/tmp/" + "ApplicationImpl" +".class"),getClass().getClassLoader()
.getResourceAsStream(getClass().getName().replace(".","/")+".class").readAllBytes())
```

生成的 ApplicationImpl.class 内容：

```java
package io.quarkus.runner;

import io.quarkus.bootstrap.naming.DisabledInitialContextManager;
import io.quarkus.bootstrap.runner.Timing;
import io.quarkus.deployment.steps.ArcDevProcessor;
import io.quarkus.deployment.steps.ArcProcessor;
import io.quarkus.deployment.steps.BannerProcessor;
import io.quarkus.deployment.steps.BlockingOperationControlBuildStep;
import io.quarkus.deployment.steps.BuildMetricsDevUIProcessor;
import io.quarkus.deployment.steps.CertificatesProcessor;
import io.quarkus.deployment.steps.ConfigBuildStep;
import io.quarkus.deployment.steps.ConfigGenerationBuildStep;
import io.quarkus.deployment.steps.ConfigurationProcessor;
import io.quarkus.deployment.steps.ContinuousTestingProcessor;
import io.quarkus.deployment.steps.DevUIProcessor;
import io.quarkus.deployment.steps.IdeProcessor;
import io.quarkus.deployment.steps.InitializationTaskProcessor;
import io.quarkus.deployment.steps.LifecycleEventsBuildStep;
import io.quarkus.deployment.steps.LogStreamProcessor;
import io.quarkus.deployment.steps.LoggingResourceProcessor;
import io.quarkus.deployment.steps.MutinyProcessor;
import io.quarkus.deployment.steps.NativeImageConfigBuildStep;
import io.quarkus.deployment.steps.NettyProcessor;
import io.quarkus.deployment.steps.NioThreadPoolDevModeProcessor;
import io.quarkus.deployment.steps.NotFoundProcessor;
import io.quarkus.deployment.steps.ResteasyCommonProcessor;
import io.quarkus.deployment.steps.ResteasyStandaloneBuildStep;
import io.quarkus.deployment.steps.RuntimeConfigSetup;
import io.quarkus.deployment.steps.ShutdownListenerBuildStep;
import io.quarkus.deployment.steps.SmallRyeContextPropagationProcessor;
import io.quarkus.deployment.steps.SyntheticBeansProcessor;
import io.quarkus.deployment.steps.ThreadPoolSetup;
import io.quarkus.deployment.steps.VertxCoreProcessor;
import io.quarkus.deployment.steps.VertxHttpProcessor;
import io.quarkus.deployment.steps.VertxProcessor;
import io.quarkus.deployment.steps.VirtualThreadsProcessor;
import io.quarkus.deployment.steps.WebJarProcessor;
import io.quarkus.dev.appstate.ApplicationStateNotification;
import io.quarkus.dev.console.QuarkusConsole;
import io.quarkus.runtime.Application;
import io.quarkus.runtime.ExecutionModeManager;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.NativeImageRuntimePropertiesRecorder;
import io.quarkus.runtime.PreventFurtherStepsException;
import io.quarkus.runtime.StartupContext;
import io.quarkus.runtime.StartupTask;
import io.quarkus.runtime.configuration.ConfigUtils;
import io.quarkus.runtime.generated.Config;
import io.quarkus.runtime.util.StepTiming;
import java.util.List;
import org.jboss.logging.Logger;

// $FF: synthetic class
public class ApplicationImpl extends Application {
    static Logger LOG;
    public static StartupContext STARTUP_CONTEXT;
    private static final String __QUARKUS_ANALYTICS_QUARKUS_VERSION;

    public ApplicationImpl() {
        super(false);
    }

    static {
        DisabledInitialContextManager.register();
        System.setProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager");
        System.setProperty("io.netty.machineId", "1c:5a:55:ea:10:3a:43:3b");
        System.setProperty("java.util.concurrent.ForkJoinPool.common.threadFactory", "io.quarkus.bootstrap.forkjoin.QuarkusForkJoinWorkerThreadFactory");
        System.setProperty("dev.resteasy.exception.mapper", "false");
        System.setProperty("io.netty.allocator.maxOrder", "3");
        System.setProperty("logging.initial-configurator.min-level", "500");
        System.setProperty("io.quarkus.security.http.test-if-basic-auth-implicitly-required", "true");
        LaunchMode.set(LaunchMode.DEVELOPMENT);
        StepTiming.configureEnabled();
        ExecutionModeManager.staticInit();
        Timing.staticInitStarted(false);
        Config.ensureInitialized();
        LOG = Logger.getLogger("io.quarkus.application");
        __QUARKUS_ANALYTICS_QUARKUS_VERSION = "__quarkus_analytics__quarkus.version=3.17.6";
        StartupContext var0 = new StartupContext();
        STARTUP_CONTEXT = var0;

        try {
            StepTiming.configureStart();
            ((StartupTask)(new MutinyProcessor.buildTimeInit521613965())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new SmallRyeContextPropagationProcessor.buildStatic677493008())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new VertxCoreProcessor.ioThreadDetector1463825589())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new BlockingOperationControlBuildStep.blockingOP558072755())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new NioThreadPoolDevModeProcessor.setupTCCL814206119())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new LoggingResourceProcessor.setupLoggingStaticInit2062061316())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new NativeImageConfigBuildStep.build282698227())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new ResteasyStandaloneBuildStep.addDefaultAuthFailureHandler334719971())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new VertxProcessor.currentContextFactory166049300())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new VirtualThreadsProcessor.setup282338138())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new ConfigurationProcessor.registerConfigs1298594308())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new LogStreamProcessor.handler2041394579())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new SyntheticBeansProcessor.initStatic1190120725())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new ConfigBuildStep.validateStaticInitConfigProperty682828288())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new ArcProcessor.initializeContainer1770303700())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new ArcProcessor.notifyBeanContainerListeners1304312071())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new ResteasyCommonProcessor.setupResteasyInjection131820800())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new ResteasyStandaloneBuildStep.staticInit345281060())).deploy(var0);
            StepTiming.printStepTime(var0);
            ((StartupTask)(new DevUIProcessor.registerDevUiHandlers1983317836())).deploy(var0);
            StepTiming.printStepTime(var0);
        } catch (Throwable var2) {
            ApplicationStateNotification.notifyStartupFailed(var2);
            var0.close();
            throw (Throwable)(new RuntimeException("Failed to start quarkus", var2));
        }
    }

    protected final void doStart(String[] var1) {
        System.setProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager");
        System.setProperty("io.netty.machineId", "1c:5a:55:ea:10:3a:43:3b");
        System.setProperty("java.util.concurrent.ForkJoinPool.common.threadFactory", "io.quarkus.bootstrap.forkjoin.QuarkusForkJoinWorkerThreadFactory");
        System.setProperty("dev.resteasy.exception.mapper", "false");
        System.setProperty("io.netty.allocator.maxOrder", "3");
        System.setProperty("logging.initial-configurator.min-level", "500");
        System.setProperty("io.quarkus.security.http.test-if-basic-auth-implicitly-required", "true");
        NativeImageRuntimePropertiesRecorder.doRuntime();
        ExecutionModeManager.runtimeInit();
        Timing.mainStarted();
        StartupContext var2 = STARTUP_CONTEXT;
        var2.setCommandLineArguments(var1);
        StepTiming.configureEnabled();

        try {
            StepTiming.configureStart();
            ((StartupTask)(new BuildMetricsDevUIProcessor.create399703524())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new VertxCoreProcessor.resetMapper926232739())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new NettyProcessor.eagerlyInitClass1832577802())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new RuntimeConfigSetup())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new VertxCoreProcessor.createVertxThreadFactory1036986175())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new VertxCoreProcessor.createVertxContextHandlers780169010())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new ConfigurationProcessor.registerJsonRpcService1069665497())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new VertxCoreProcessor.eventLoopCount1012482323())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new VertxHttpProcessor.cors1355075351())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new ConfigGenerationBuildStep.releaseConfigOnShutdown561040398())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new ThreadPoolSetup.createExecutor2117483448())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new SmallRyeContextPropagationProcessor.build1909893707())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new ArcProcessor.setupExecutor1831044820())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new MutinyProcessor.runtimeInit866247078())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new ConfigGenerationBuildStep.checkForBuildTimeConfigChange1532146938())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new BannerProcessor.recordBanner921118789())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new IdeProcessor.createOpenInIDEService1670438981())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new VertxCoreProcessor.build1754895780())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new VertxHttpProcessor.bodyHandler1176441513())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new VertxHttpProcessor.createDevUILog540613743())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new VertxHttpProcessor.preinitializeRouter1141331088())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new LoggingResourceProcessor.setupLoggingRuntimeInit1041640541())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new VertxProcessor.build1689596782())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new CertificatesProcessor.initializeCertificate877524439())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new SyntheticBeansProcessor.initRuntime975230615())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new InitializationTaskProcessor.startApplicationInitializer180820092())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new ArcDevProcessor.registerRoutes1821135182())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new ConfigBuildStep.validateRuntimeConfigProperty1282080724())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new ConfigBuildStep.registerConfigClasses1377682816())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new ContinuousTestingProcessor.continuousTestingState1383474851())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new DevUIProcessor.createJsonRpcRouter1067343756())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new ResteasyStandaloneBuildStep.boot345593974())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new WebJarProcessor.processWebJarDevMode1534459532())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new VertxHttpProcessor.initializeRouter938601780())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new NotFoundProcessor.routeNotFound64600991())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new VertxHttpProcessor.finalizeRouter992900573())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new LifecycleEventsBuildStep.startupEvent1144526294())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new ShutdownListenerBuildStep.setupShutdown1533204416())).deploy(var2);
            StepTiming.printStepTime(var2);
            ((StartupTask)(new VertxHttpProcessor.openSocket1753087980())).deploy(var2);
            StepTiming.printStepTime(var2);
            ExecutionModeManager.running();
            List var3 = ConfigUtils.getProfiles();
            Timing.printStartupTime("013-quarkus-hello-lifecycle", "1.0-SNAPSHOT", "3.17.6", "cdi, resteasy, smallrye-context-propagation, vertx", var3, true, false);
            QuarkusConsole.start();
        } catch (PreventFurtherStepsException var5) {
            var2.close();
        } catch (Throwable var6) {
            var2.close();
            throw (Throwable)(new RuntimeException("Failed to start quarkus", var6));
        }

    }

    protected final void doStop() {
        ExecutionModeManager.unset();
        STARTUP_CONTEXT.close();
    }

    public String getName() {
        return "013-quarkus-hello-lifecycle";
    }
}
```

