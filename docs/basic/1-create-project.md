# Quarkus 创建项目

**自动创建项目有三种方式**：

+ quarkus cli
+ IDEA quarkus
+ mvn plugin

**默认创建的文件**(删除了一些不重要的文件)：

```shell
$ tree -a   
.
├── .dockerignore
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── docker
    │   │   ├── Dockerfile.jvm       	# 基于 jar 包构建 Docker 镜像
    │   │   ├── Dockerfile.legacy-jar   # 基于 uber-jar 包构建 Docker 镜像
    │   │   ├── Dockerfile.native		# 基于二进制文件构建Docker镜像，基础镜像：registry.access.redhat.com/ubi8/ubi-minimal:8.10，相比 native-micro 提供了更多的灵活性和调试功能,适合开发和测试环境
    │   │   └── Dockerfile.native-micro # 基于二进制文件构建Docker镜像，基础镜像：quay.io/quarkus/quarkus-micro-image:2.0, 镜像更小，适合生产环境
    │   ├── java
    │   │   └── top
    │   │       └── kwseeker
    │   │           └── ExampleResource.java
    │   └── resources
    │       └── application.properties
    └── test
        └── java
            └── top
                └── kwseeker
                    ├── ExampleResourceIT.java
                    └── ExampleResourceTest.java
```

**dev模式启动项目**：

```
mvn compile quarkus:dev
```

在 IDEA 中启动和这个命令效果一致；IDEA 启动执行的命令行：

```shell
/lib/jvm/graalvm-21/bin/java -Dmaven.multiModuleProjectDirectory=/home/arvin/mywork/java/quarkus-labs/010-quarkus-hello -Djansi.passthrough=true -Dmaven.home=/opt/idea-IU-232.10227.8/plugins/maven/lib/maven3 -Dclassworlds.conf=/opt/idea-IU-232.10227.8/plugins/maven/lib/maven3/bin/m2.conf -javaagent:/opt/idea-IU-232.10227.8/lib/idea_rt.jar=46339:/opt/idea-IU-232.10227.8/bin -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath /opt/idea-IU-232.10227.8/plugins/maven/lib/maven3/boot/plexus-classworlds-2.7.0.jar:/opt/idea-IU-232.10227.8/plugins/maven/lib/maven3/boot/plexus-classworlds.license org.codehaus.classworlds.Launcher -Didea.version=2023.2.5 quarkus:dev
```

**打包**（JVM执行方式）:

直接 `mvn package`打的包不包含依赖，是无法直接`jar -jar xxx.jar`执行，还需要在命令行指定依赖；

不过可以打 uber-jar (和 fat-jar 含义是一样的)：

```
mvn clean package -U -DskipTests -Dquarkus.package.type=uber-jar
```

uber-jar 会带有 -runner.jar 后缀，比如 010-quarkus-hello-1.0-SNAPSHOT-runner.jar 。

**打包二进制可执行文件**：

```
mvn clean package -U -DskipTests -Dnative -Dquarkus.native.container-build=true
mvn clean package -U -DskipTests -Dnative -Dquarkus.native.native-image-xmx=4096m -Dquarkus.native.container-build=true
```

打包过程中可以看到有拉取 `quarkus/ubi-quarkus-mandrel-builder-image` 这个docker镜像，这个镜像用于编译代码生成可执行文件的，会生成一个以 `-runner` 结尾的二进制文件。

>  **Native Image 的编译过程** (由 DeepSeek 生成准确性有待验证)
>
>  GraalVM 的 Native Image 编译过程可以分为以下几个阶段：
>
>  （1）**静态分析（Static Analysis）**
>
>  - **目标**：确定应用程序在运行时需要哪些类、方法和字段。
>
>  - **过程**：
>
>    GraalVM 会分析应用程序的入口点（如 `main` 方法），并递归地分析所有可达的代码路径。
>
>    通过静态分析，GraalVM 可以确定哪些类和方法需要在运行时可用，哪些可以被优化掉。
>
>  - **挑战**：
>
>    如果应用程序使用了**动态特性**（如反射、动态类加载、注解处理器、动态代理、JNI[即本地方法] 等），静态分析可能无法完全确定运行时所需的类和方法。
>
>  （2）**闭包计算（Reachability Closure）**
>
>  - **目标**：计算应用程序的“闭包”，即所有在运行时需要的类、方法和资源。
>
>  - **过程**：
>
>    GraalVM 会遍历应用程序的代码路径，收集所有可达的类和方法。
>
>    通过闭包计算，GraalVM 可以生成一个最小的运行时镜像，减少可执行文件的大小。
>
>  （3）**提前编译（Ahead-of-Time Compilation）**
>
>  - **目标**：将 Java 字节码编译为本地机器码。
>
>  - **过程**：
>
>    GraalVM 使用其自带的 JIT 编译器（Graal Compiler）将 Java 字节码编译为本地机器码。
>
>    编译后的机器码会被打包到最终的二进制文件中。
>
>  （4）**镜像生成（Image Generation）**
>
>  - **目标**：生成独立的可执行文件。
>
>  - **过程**：
>
>    GraalVM 将编译后的机器码、类文件、资源文件等打包到一个独立的二进制文件中。
>
>    生成的二进制文件可以直接在目标操作系统上运行，无需 JVM。

**制作 Docker 镜像**：

前面已经默认生成了4个Dockerfile文件，只需要执行 docker build 命令即可制作成 Docker 镜像。比如：

```
docker build \
-f src/main/docker/Dockerfile.jvm \
-t kwseeker/010-quarkus-hello:1.0-SNAPSHOT .

docker build \
-f src/main/docker/Dockerfile.native-micro \
-t kwseeker/010-quarkus-hello-micro:1.0-SNAPSHOT .
```

