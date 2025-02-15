参考：https://github.com/quarkusio/quarkus-quickstarts/tree/main/rabbitmq-quickstart

这个是Quarkus官方给的Demo, 但是在启动过程中出现了些问题，所以复制过来进行修改了一下。

+ **DevServices方式启动会拉取依赖的镜像，从 DockerHub 拉取失败**

  DockerHub 应该也是被墙了，发现阿里云镜像源也不好用了，最终找到了一个可用的镜像源 [毫秒镜像](https://1ms.run)；
  手动拉取镜像，由于毫秒镜像源上的镜像被重命名了，所以还需要 docker tag 修改下镜像名称。

  > !!! 使用毫秒镜像源需要先登录 docker login docker.1ms.run

+ **Dev Services**

  Quarkus Dev Services 是 Quarkus 提供的一项开发时服务自动配置功能，
  旨在简化开发环境中的依赖服务（如数据库、消息队列、缓存等）的配置和管理。
  它的核心目标是让开发者能够专注于业务逻辑，而无需手动设置和管理本地开发环境中的外部服务。

  Quarkus Dev Services 的主要功能包括： 

  + **自动启动服务**：在开发模式下，自动启动所需的外部服务（如数据库、Kafka、Redis 等）。 
  + **零配置**：无需手动配置服务连接信息（如数据库 URL、用户名、密码等），Quarkus 会自动处理。
  + **容器化支持**：使用 Docker 容器启动服务，确保开发环境与生产环境一致。 
  + **开发效率提升**：减少开发者在本地环境中手动设置服务的时间。

  Quarkus Dev Services 的工作原理如下：

  1. **检测依赖**：Quarkus 检测项目中的依赖（如数据库驱动、Kafka 客户端等）。
  2. **启动容器**：如果检测到需要外部服务，Quarkus 会自动启动相应的 Docker 容器。
  3. **配置注入**：Quarkus 自动将服务的连接信息（如数据库 URL）注入到应用中。
  4. **开发模式支持**：仅在开发模式（`quarkus:dev`）下生效，生产环境中需要手动配置。

  这个Demo 启用了 RabbitMQ DevServices:

  ```
  quarkus.rabbitmq.devservices.port=5672
  ```

+ **CORS 跨域设置**

+ **消息响应式通信**

  这个Demo展示的响应式消息通信接口。

