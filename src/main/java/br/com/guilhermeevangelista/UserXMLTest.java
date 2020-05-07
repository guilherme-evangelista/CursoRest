package br.com.guilhermeevangelista;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserXMLTest {

    @Test
    public void devoTrabalharComXML(){
        given()
        .when()
            .get("http://restapi.wcaquino.me/usersXML/3")
        .then()
            .statusCode(200)
            .rootPath("user")
                .body("@id", is("3"))
            .rootPath("user.filhos")
                .body("name.size()", is(2))
            .detachRootPath("filhos")
                .body("filhos.name[0]", is("Zezinho"))
                .body("filhos.name[1]", is("Luizinho"))
            .appendRootPath("filhos")
                .body("name", hasItem("Luizinho"))
                .body("name", hasItem("Zezinho"))
                .body("name", hasItems("Zezinho", "Zezinho"))
        ;
    }

    @Test
    public void testeNovoMetodo(){
        System.out.println("eae");
    }
}
