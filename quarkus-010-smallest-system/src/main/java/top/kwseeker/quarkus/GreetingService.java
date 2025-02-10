package top.kwseeker.quarkus;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

    public GreetingService() {
        System.out.println("GreetingService instantiate");
    }

    public String greet(String name) {
        return "Hello, " + name + "!";
    }
}