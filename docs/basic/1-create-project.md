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
mvn clean package -U -Dquarkus.package.type=uber-jar
```

uber-jar 会带有 -runner.jar 后缀，比如 010-quarkus-hello-1.0-SNAPSHOT-runner.jar 。

**打包二进制可执行文件**：

```
mvn clean package -U -DskipTests -Dnative -Dquarkus.native.container-build=true
mvn clean package -U -DskipTests -Dnative -Dquarkus.native.native-image-xmx=4096m -Dquarkus.native.container-build=true
```

打包过程中可以看到有拉取 `quarkus/ubi-quarkus-mandrel-builder-image` 这个docker镜像，这个镜像用于编译代码生成可执行文件的，会生成一个以 `-runner` 结尾的二进制文件。

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

