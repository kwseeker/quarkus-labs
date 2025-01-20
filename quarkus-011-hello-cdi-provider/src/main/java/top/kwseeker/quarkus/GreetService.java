package top.kwseeker.quarkus;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetService {

    public String getGreet() {
        return "Hi man!";
    }
}
