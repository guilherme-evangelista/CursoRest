package br.com.guilhermeevangelista;

import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class SchemaTest {

    @Test
    public void devoValidarEsquemaJson(){
        given()
            .log().all()
        .when()
            .get("http://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(200)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("users.json"))
        ;
    }
}
