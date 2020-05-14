package br.com.guilhermeevangelista;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class VerbosTest {

    @Test
    public void deveSalvarUsuario(){
        given()
            .log().all()
            .contentType("application/json")
            .body("{\"name\":\"Josias\", \"age\":24}")
        .when()
            .post("http://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", is(notNullValue()))
            .body("name", is("Josias"))
            .body("age", is(24))
        ;
    }

    @Test
    public void naoDeveSalvaOUsuarioSemNome(){
        given()
            .log().all()
            .contentType("application/json")
            .body("{\"age\":24}")
        .when()
            .post("http://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(400)
            .body("id", is(nullValue()))
            .body("error", is("Name é um atributo obrigatório"))
        ;
    }

    @Test
    public void deveSalvarUsuarioViaXML(){
        given()
            .log().all()
            .contentType(ContentType.XML)
            .body("<user><name>Kuka Beludo</name><age>50</age></user>")
        .when()
         .post("http://restapi.wcaquino.me/usersXML")
        .then()
            .log().all()
            .statusCode(201)
            .body("user.@id", is(notNullValue()))
            .body("user.name", is("Kuka Beludo"))
            .body("user.age", is("50"))
        ;
    }

    @Test
    public void deveAlterarUsuario(){
        given()
            .log().all()
            .contentType("application/json")
            .body("{\"name\":\"Josias\", \"age\":24}")
        .when()
            .put("http://restapi.wcaquino.me/users/1")
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(notNullValue()))
            .body("name", is("Josias"))
            .body("age", is(24))
        ;
    }

    @Test
    public void deveRemoverUsuario(){
        given()
            .log().all()
        .when()
            .delete("http://restapi.wcaquino.me/users/1")
        .then()
            .log().all()
            .statusCode(204)
        ;
    }

    @Test
    public void deveSalvarUsuarioUsandoMap(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Josias Map");
        params.put("age", 24);

        given()
            .log().all()
            .contentType("application/json")
            .body(params)
        .when()
            .post("http://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", is(notNullValue()))
            .body("name", is("Josias Map"))
            .body("age", is(24))
        ;
    }

    @Test
    public void deveSalvarUsuarioUsandoObjeto(){
        User user = new User("Josias Objeto", 25);

        given()
            .log().all()
            .contentType("application/json")
            .body(user)
        .when()
            .post("http://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", is(notNullValue()))
            .body("name", is("Josias Objeto"))
            .body("age", is(25))
        ;
    }

    @Test
    public void deveDescerializarSalvarUsuarioUsandoObjeto(){
        User user = new User("Josias deserializado", 25);

        User retorno = given()
            .log().all()
            .contentType("application/json")
            .body(user)
        .when()
            .post("http://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .extract().body().as(User.class)
        ;

        System.out.println(retorno);

        assertThat(retorno.getId(), notNullValue());

        assertEquals("Josias deserializado", retorno.getName());
        assertEquals(25, retorno.getAge());
    }
}
