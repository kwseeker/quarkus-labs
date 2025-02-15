package org.acme.rabbitmq.producer;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class QuotesResourceTest {

    @Test
    void testQuotesEventStream() {
        String body = given()
            .when()
            .post("/quotes/request")
            .then()
            .statusCode(200)
            .extract().body()
            .asString();
        System.out.println("Received response: " + body);
        assertDoesNotThrow(() -> UUID.fromString(body));
    }

    @Test
    void testGetQuotesEventStream() {
        String body = given()
                .when()
                .get("/quotes")
                .then()
                .statusCode(200)
                .extract().body()
                .asString();
        System.out.println("Received quotes: " + body);
        assertDoesNotThrow(() -> UUID.fromString(body));
    }
}
