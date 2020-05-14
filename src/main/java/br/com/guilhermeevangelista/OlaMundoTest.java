package br.com.guilhermeevangelista;

import io.restassured.http.Method;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class OlaMundoTest {

    @Test
    public void test01OlaMundo(){
        Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
        assertEquals("Ola Mundo!", response.getBody().asString(), "A mensagem deveria ser: 'Ola Mundo!'");
        assertEquals(200, response.statusCode(), "O StatusCode deveria ser 200");
    }

    @Test
    public void devoConhecerOutrasFormas(){
        get("http://restapi.wcaquino.me/ola").then().statusCode(200);

        given()
                //Pré conddições para requisição
        .when()
            .get("http://restapi.wcaquino.me/ola")
        .then()
            .assertThat()
            .statusCode(200);
    }

    @Test
    public void devoConhecerOutrasFormasMatchersHamcrest(){
        assertEquals(128d, Matchers.is(128d));
    }

    @Test
    public void devoValdiarOBody(){
        given()
            //Pré condições para requisição
        .when()
            .get("http://restapi.wcaquino.me/ola")
        .then()
            .assertThat()
            .statusCode(200)
            .body(Matchers.is("Ola Mundo!"))
            .body(Matchers.containsString("Mundo"))
            .body(Matchers.is(Matchers.not(Matchers.nullValue())))
        ;

    }
}
