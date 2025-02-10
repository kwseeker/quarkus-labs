package top.kwseeker.quarkus;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;

@QuarkusMain
public class Main implements QuarkusApplication {

    public Main() {
        System.out.println("Main instantiate");
    }

    @Inject
    Greeter greeter;

    @Override
    public int run(String... args) throws Exception {
        System.out.println(greeter.greet("World"));
        return 0;
    }
}