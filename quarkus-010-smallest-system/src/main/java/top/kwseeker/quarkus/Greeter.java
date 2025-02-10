package top.kwseeker.quarkus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Greeter {

    public Greeter() {
        System.out.println("Greeter instantiate");
    }

    @Inject
    GreetingService greetingService;

    public String greet(String name) {
        return greetingService.greet(name);
    }
}