//package top.kwseeker.quarkus;
//
//import io.quarkus.runtime.annotations.RegisterForReflection;
//import io.vertx.ext.web.RoutingContext;
//import jakarta.ws.rs.core.Response;
//import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
//
///**
// * Quarkus 全局异常处理: Quarkus @ServerExceptionMapper 实现
// */
//public class DefaultExceptionMapper {
//
//    @ServerExceptionMapper // 标记为全局异常处理器
//    public Response handleCustomException(Throwable exception, RoutingContext context) {
//        return Response
//                .status(Response.Status.BAD_REQUEST)
//                .entity(new ErrorResponse(exception.getMessage()))
//                .build();
//    }
//
//    @RegisterForReflection // 确保异常类在原生模式下可用, TODO ?
//    public record ErrorResponse(String message) {
//    }
//}
