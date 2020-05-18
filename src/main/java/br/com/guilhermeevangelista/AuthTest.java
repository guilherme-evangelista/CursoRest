package br.com.guilhermeevangelista;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public class AuthTest {

//    590cd1e7bfb0fc138992c75ddb0fb3d4
//    http://api.openweathermap.org/data/2.5/weather?q=SaoPaulo,BR&appid=590cd1e7bfb0fc138992c75ddb0fb3d4
    @Test
    public void devoAutenticarMinhaChamada(){
        given()
            .log().all()
            .queryParam("q", "Carapicuiba,BR")
            .queryParam("appid", "590cd1e7bfb0fc138992c75ddb0fb3d4")
            .queryParam("units", "metric")
        .when()
            .get("http://api.openweathermap.org/data/2.5/weather")
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(6322185))
            .body("name", is("Carapicuíba"))
        ;
    }

    @Test
    public void naoDeveAcessarSemSenha(){
        given()
            .log().all()
        .when()
            .get("http://restapi.wcaquino.me/basicauth")
        .then()
            .log().all()
            .statusCode(401)
        ;
    }

    @Test
    public void deveAcessarComUsuarioESenhaNaUrl(){
        given()
            .log().all()
        .when()
            .get("http://admin:senha@restapi.wcaquino.me/basicauth")
        .then()
            .log().all()
            .statusCode(200)
            .body("status", is("logado"))
        ;
    }

    @Test
    public void deveAcessarComUsuarioESenhaNoGiven(){
        given()
            .log().all()
            .auth().basic("admin", "senha")
        .when()
            .get("http://restapi.wcaquino.me/basicauth")
        .then()
            .log().all()
            .statusCode(200)
            .body("status", is("logado"))
        ;
    }

    @Test
    public void deveAcessarComUsuarioESenhaDeOutraForma(){
        given()
            .log().all()
            .auth().preemptive().basic("admin", "senha")
        .when()
            .get("http://restapi.wcaquino.me/basicauth2")
        .then()
            .log().all()
            .statusCode(200)
            .body("status", is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacaoComTokenJWT(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", "cyeTest@1");
        map.put("senha", "test1");

        //Logar na aplicação
        //Capturar Token

        String token = given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(map)
        .when()
            .post("http://barrigarest.wcaquino.me/signin")
        .then()
            .log().all()
            .statusCode(200)
            .extract().path("token")
        ;

        given()
            .log().all()
            .header("Authorization", "JWT " + token)
        .when()
            .get("http://barrigarest.wcaquino.me/contas")
        .then()
            .log().all()
            .statusCode(200)
            .body("nome", hasItem("testConta"))
        ;
    }
}
