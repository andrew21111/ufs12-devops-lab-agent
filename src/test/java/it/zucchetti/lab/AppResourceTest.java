package it.zucchetti.lab;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/** Test delle parti già pronte: /health e /info. */
@QuarkusTest
class AppResourceTest {

    @Test
    void healthRispondeOk() {
        given().when().get("/health")
                .then().statusCode(200).body("status", equalTo("ok"));
    }

    @Test
    void infoContieneHostnameVersioneEAvvio() {
        given().when().get("/info")
                .then().statusCode(200)
                .body("hostname", notNullValue())
                .body("version", equalTo("1.0"))
                .body("startedAt", notNullValue());
    }
}
