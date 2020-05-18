package br.com.guilhermeevangelista;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class EnvioDadosTest {

    @Test
    public void deveEnviarValorViaQuery(){
        given()
            .log().all()
        .when()
            .get("http://restapi.wcaquino.me/v2/users?format=json")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(ContentType.JSON)
        ;
    }

    @Test
    public void deveEnviarValorViaQueryViaParametro(){
        given()
            .log().all()
            .queryParam("format", "xml")
        .when()
            .get("http://restapi.wcaquino.me/v2/users")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(ContentType.XML)
            .contentType(containsString("utf-8"))
        ;
    }

    @Test
    public void deveEnviarValorViaQueryViaHeader(){
        given()
            .log().all()
            .accept(ContentType.JSON)
        .when()
            .get("http://restapi.wcaquino.me/v2/users")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(ContentType.JSON)
        ;
    }


}
