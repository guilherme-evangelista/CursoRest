package br.com.guilhermeevangelista;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserXMLTest {

    public static ResponseSpecification responseSpecification;
    public static RequestSpecification requestSpecification;

    @BeforeAll
    public static void definirPreCondicoesGerais(){
        RestAssured.baseURI = "http://restapi.wcaquino.me";
//        RestAssured.port = 433;
//        RestAssured.basePath = "user";

        requestSpecification = new RequestSpecBuilder().log(LogDetail.ALL).build();

        responseSpecification = new ResponseSpecBuilder().expectStatusCode(200).build();

        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseSpecification;
    }

    @Test
    public void devoTrabalharComXML(){
        given()
        .when()
            .get("/usersXML/3")
        .then()
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
    public void devoFazerPesquisasAvancadasComXml(){
        given()
        .when()
            .get("/usersXML")
        .then()
            .body("users.user.size()", is(3))
            .body("users.user.findAll{it.age.toInteger() <= 25}.size()", is(2))
            .body("users.user.@id", hasItems("1", "2", "3"))
            .body("users.user.name", hasItems("João da Silva", "Maria Joaquina", "Ana Julia"))
            .body("users.user.find{it.age == 25}.name", is("Maria Joaquina"))
            .body("users.user.findAll{it.name.toString().contains('n')}.name" , hasItems("Maria Joaquina", "Ana Julia"))
        ;
    }

    @Test
    public void devoFazerPesquisasAvancadasComXmlEJava(){
        ArrayList<NodeImpl> nomes = given()
            .when()
                .get("/usersXML")
            .then()
//            .extract().path("users.user.name.findAll{it.toString().startsWith('Maria')}");
            .extract().path("users.user.name.findAll{it.toString().contains('n')}");

        Assertions.assertEquals(2, nomes.size());
        Assertions.assertEquals("Maria Joaquina", nomes.get(0).toString());
        Assertions.assertEquals("Ana Julia", nomes.get(1).toString());
    }

    @Test
    public void devoFazerPesquisasAvancadasComXmlEXpath(){
        given()
        .when()
            .get("/usersXML")
        .then()
            .body(hasXPath("count(//user[@id])", is("3")))
            .body(hasXPath("//user[@id='1']"))
            .body(hasXPath("//name[text() = 'Zezinho']/ancestor::user/name", is("Ana Julia")))
            ;

    }




}
