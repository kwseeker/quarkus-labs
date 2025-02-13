package top.kwseeker.quarkus;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Quarkus 全局异常处理: ExceptionMapper 实现
 *
 * @Provider 也是 JAX-RS 规范中的一部分，
 * JAX-RS（Java API for RESTful Web Services）是 Java 平台上的一个规范，用于构建 RESTful Web 服务。
 * 它定义了一组 API 和注解，使得开发者可以轻松地创建、部署和使用 RESTful 服务。
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Internal Server Error: " + exception.getMessage())
                .build();
    }
}
