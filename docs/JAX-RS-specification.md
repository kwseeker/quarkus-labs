# JAX-RS 规范

参考：https://github.com/jakartaee/rest/tree/main/jaxrs-spec

JAX-RS（Java API for RESTful Web Services）是 **Java EE** 的一部分，现已成为 **Jakarta EE** 的核心组件。它提供了一套用于构建 RESTful Web 服务的标准 API。

JAX-RS 规范的主要内容：

1. **核心注解**

   JAX-RS 提供了一系列注解，用于定义 RESTful 资源和方法。

   + **资源类注解**

     - **`@Path`**：定义资源的 URI 路径。

       ```java
       @Path("/users")
       public class UserResource { ... }
       ```


   + **HTTP 方法注解**

     - **`@GET`**：处理 HTTP GET 请求。
     
     + **`@POST`**：处理 HTTP POST 请求。
     
     + **`@PUT`**：处理 HTTP PUT 请求。
     
     + **`@DELETE`**：处理 HTTP DELETE 请求。
     
     + **`@HEAD`**：处理 HTTP HEAD 请求。
     
     + **`@OPTIONS`**：处理 HTTP OPTIONS 请求。
     
     + **`@PATCH`**：处理 HTTP PATCH 请求。


   + **参数注解**

     + **`@PathParam`**：从 URI 路径中提取参数。

     + **`@QueryParam`**：从查询字符串中提取参数。

     + **`@FormParam`**：从表单数据中提取参数。

     + **`@HeaderParam`**：从 HTTP 头中提取参数。

     + **`@CookieParam`**：从 Cookie 中提取参数。

     + **`@MatrixParam`**：从矩阵参数中提取参数。

     + **`@BeanParam`**：将多个参数绑定到一个对象。

       ```java
       @GET
       @Path("/search")
       public List<User> searchUsers(@QueryParam("name") String name) { ... }
       ```


   + **请求和响应注解**

     - **`@Consumes`**：指定方法接受的媒体类型（如 `application/json`）。
     
      - **`@Produces`**：指定方法返回的媒体类型（如 `application/json`）。
     
        ```java
        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public User createUser(User user) { ... }
        ```


2. **资源类**

   资源类是 JAX-RS 的核心组件，用于处理 HTTP 请求并返回响应。

   ```java
   @Path("/users")
   public class UserResource {
   
       @GET
       @Path("/{id}")
       @Produces(MediaType.APPLICATION_JSON)
       public User getUser(@PathParam("id") int id) {
           return userService.getUser(id);
       }
   
       @POST
       @Consumes(MediaType.APPLICATION_JSON)
       @Produces(MediaType.APPLICATION_JSON)
       public User createUser(User user) {
           return userService.createUser(user);
       }
   }
   ```

3. **请求和响应**

   JAX-RS 提供了 `Request` 和 `Response` 类，用于处理 HTTP 请求和响应。

   + **`javax.ws.rs.core.Response`**

     用于构建 HTTP 响应。

     ```java
     @GET
     @Path("/{id}")
     public Response getUser(@PathParam("id") int id) {
         User user = userService.getUser(id);
         return Response.ok(user).build();
     }
     ```

   + **`javax.ws.rs.core.Request`**

     用于访问 HTTP 请求的元数据。

4. **异常处理**

   JAX-RS 提供了 `ExceptionMapper` 接口，用于全局异常处理。

   ```java
   @Provider
   public class CustomExceptionMapper implements ExceptionMapper<CustomException> {
   
       @Override
       public Response toResponse(CustomException exception) {
           return Response.status(Response.Status.BAD_REQUEST)
                          .entity("Error: " + exception.getMessage())
                          .build();
       }
   }
   ```

5. **内容协商**

   JAX-RS 支持根据客户端请求的媒体类型（如 `application/json` 或 `application/xml`）返回不同的响应。

    ```
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public User getUser(@PathParam("id") int id) {
        return userService.getUser(id);
    }
    ```

6. **过滤器与拦截器**

   JAX-RS 提供了过滤器和拦截器，用于在请求处理前后执行逻辑。

   + **过滤器**

     - **`ContainerRequestFilter`**：在请求到达资源方法之前执行。

     - **`ContainerResponseFilter`**：在资源方法返回响应之后执行。
   
   
      + **拦截器**
   - **`ReaderInterceptor`**：在读取请求体之前执行。
     
   - **`WriterInterceptor`**：在写入响应体之前执行。
   


7. **客户端 API**

   JAX-RS 提供了客户端 API，用于发送 HTTP 请求。

    ```java
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target("http://example.com/api/users");
    Response response = target.request(MediaType.APPLICATION_JSON).get();
    User user = response.readEntity(User.class);
    ```

8. **上下文注入**

   JAX-RS 支持通过 `@Context` 注解注入上下文对象，如 `UriInfo`、`HttpHeaders` 等。

    ```java
    @GET
    @Path("/info")
    public String getInfo(@Context UriInfo uriInfo, @Context HttpHeaders headers) {
        return "Path: " + uriInfo.getPath() + ", Headers: " + headers.getRequestHeaders();
    }
    ```

9. **异步支持**

   JAX-RS 支持异步处理，适用于长时间运行的任务。

    ```java
    @GET
    @Path("/async")
    public void asyncMethod(@Suspended AsyncResponse asyncResponse) {
        new Thread(() -> {
            String result = longRunningTask();
            asyncResponse.resume(result);
        }).start();
    }
    ```

10. **HATEOAS 支持**

    JAX-RS 支持 HATEOAS（Hypermedia as the Engine of Application State），通过 `Link` 类实现。

    HATEOAS（**Hypermedia as the Engine of Application State**，超媒体作为应用状态引擎）是 REST 架构风格的一个核心约束。它的核心思想是通过超媒体（如链接）来驱动客户端与服务器的交互，客户端不需要预先知道如何与服务器交互，而是通过服务器返回的响应中的链接来动态发现可用的操作。

    **HATEOAS 的核心概念**：

    - **超媒体（Hypermedia）**：指包含链接的媒体类型（如 HTML、JSON with links）。
    - **应用状态（Application State）**：客户端通过服务器返回的超媒体链接来驱动应用的状态转换。
    - **无状态（Stateless）**：服务器不保存客户端的状态，客户端通过链接和资源表示来维护状态。

    ```java
    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") int id) {
        User user = userService.getUser(id);
        Link link = Link.fromUri("/users").rel("collection").build();
        return Response.ok(user).links(link).build();
    }
    ```

11. **验证**

    JAX-RS 支持 Bean Validation（JSR 380），用于验证请求参数和实体。

    ```
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid User user) {
        userService.createUser(user);
        return Response.status(Response.Status.CREATED).build();
    }
    ```