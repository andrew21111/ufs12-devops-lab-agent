package it.zucchetti.lab;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

/**
 * Test degli esercizi, spenti con @Disabled.
 * Quando finisci un esercizio, cancella la riga @Disabled sopra il suo test e lancia ./mvnw test.
 * Nei test /crash non spegne l'app (app.crash.exit=false).
 */
@QuarkusTest
class EserciziTest {

    @Disabled("Esercizio 1: cancella questa riga quando hai finito")
    @Test
    void esercizio1_paginaPrincipale() {
        given().when().get("/")
                .then().statusCode(200)
                .body(containsString("Ciao dal corso DevOps!"))
                .body(containsString("1.0"));
    }

    @Disabled("Esercizio 2: cancella questa riga quando hai finito")
    @Test
    void esercizio2_crash() {
        given().when().post("/crash")
                .then().statusCode(202)
                .body("status", equalTo("crashing"));
    }

    @Disabled("Esercizio 3: cancella questa riga quando hai finito")
    @Test
    void esercizio3_uptime() {
        given().when().get("/info")
                .then().statusCode(200)
                .body("uptimeSeconds", greaterThanOrEqualTo(0));
    }

    @Disabled("Esercizio 4: cancella questa riga quando hai finito")
    @Test
    void esercizio4_saluto() {
        given().when().get("/saluto?nome=Mario")
                .then().statusCode(200).body("saluto", equalTo("Ciao Mario!"));
        given().when().get("/saluto")
                .then().statusCode(200).body("saluto", equalTo("Ciao mondo!"));
    }
}
