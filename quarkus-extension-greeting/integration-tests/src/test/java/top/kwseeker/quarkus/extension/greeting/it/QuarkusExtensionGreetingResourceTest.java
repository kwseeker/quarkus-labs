package top.kwseeker.quarkus.extension.greeting.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class QuarkusExtensionGreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/quarkus-extension-greeting")
                .then()
                .statusCode(200)
                .body(is("Hello quarkus-extension-greeting"));
    }
}
