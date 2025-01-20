package top.kwseeker.quarkus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class HelloResource {

    @Inject
    HelloService helloService;
    //测试跨模块的依赖注入, 在其他模块中定义的Bean貌似无法注入到当前模块, TODO: 原因
    //@Inject
    //GreetService greetService;
    @ApplicationScoped
    GreetService greetService = new GreetService(); // 但是这样是可以的

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return helloService.getHello();
    }

    @GET
    @Path("/greet")
    @Produces(MediaType.TEXT_PLAIN)
    public String greet() {
        return greetService.getGreet();
    }
}
