package br.com.guilhermeevangelista;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserJsonTest {
    @Test
    public void deveVerificarPrimeiroNivel(){
        given()
        .when()
            .get("http://restapi.wcaquino.me/users/1")
        .then()
            .statusCode(200)
            .body("id", is(1))
            .body("name", containsString("Silva"))
            .body("age", greaterThan(18))
        ;
    }

    @Test
    public void deveVerificarPrimeiroNivelComOutrasFormas(){
        Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/users/1");
        //path
        Assert.assertEquals(1, response.path("id"));
        Assert.assertEquals(1, response.path("%s","id"));
        //jsonPath
        JsonPath jsonPath = new JsonPath(response.asString());
        Assert.assertEquals(1, jsonPath.getInt("id"));
        //from
        int id = JsonPath.from(response.asString()).getInt("id");
        Assert.assertEquals(1, id);
    }

    @Test
    public void deveVerificarOSegundoNivel(){
        given()
        .when()
            .get("http://restapi.wcaquino.me/users/2")
        .then()
            .statusCode(200)
            .body("id", is(2))
            .body("name", containsString("Joaquina"))
            .body("endereco.rua", is("Rua dos bobos"))
        ;
    }

    @Test
    public void deveVerificarJsonComLista(){
        given()
        .when()
            .get("http://restapi.wcaquino.me/users/3")
        .then()
            .statusCode(200)
            .body("id", is(3))
            .body("name", containsString("Ana"))
            .body("filhos", hasSize(2))
            .body("filhos[0].name",is("Zezinho"))
            .body("filhos[1].name",is("Luizinho"))
            .body("filhos.name", hasItem("Zezinho"))
            .body("filhos.name", hasItem("Luizinho"))
            .body("filhos.name", hasItems("Zezinho", "Luizinho"));
    }

    @Test
    public void deveRetornarErroUsuarioInexistente(){
        given()
        .when()
            .get("http://restapi.wcaquino.me/users/4")
        .then()
            .statusCode(404)
            .body("error", is("Usuário inexistente"))
        ;
    }

    @Test
    public void deveValidarListaNaRaiz(){
        given()
        .when()
            .get("http://restapi.wcaquino.me/users")
        .then()
            .statusCode(200)
            .body("$", hasSize(3))
            .body("name", hasItems("João da Silva","Maria Joaquina","Ana Júlia"))
            .body("age[1]", is(25))
            .body("[2].filhos[0].name", containsString("Zezinho"))
            .body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
            .body("salary", contains(1234.5678f, 2500, null))
        ;
    }

    @Test
    public void devoFazerVerificacoesAvancadas(){
        given()
        .when()
            .get("http://restapi.wcaquino.me/users")
        .then()
            .statusCode(200)
            .body("$", hasSize(3))
            .body("age.findAll{it <= 25}.size()", is(2))
            .body("age.findAll{it <= 25 && it > 20}.size()", is(1))
            .body("findAll{it.age <= 25 && it.age > 20}.name", is(Arrays.asList("Maria Joaquina")))
            .body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
            .body("findAll{it.age <= 25}[0].name", is("Maria Joaquina"))
            .body("findAll{it.age <= 25}[-1].name", is("Ana Júlia")) // -1 faz com que busquemos do ultimo elemento para o primeiro
            .body("find{it.age <= 25}.name", is("Maria Joaquina")) // retorna apenas um registro, caso tenha mais, será o primeiro
            .body("findAll{it.name.contains('n')}.name", hasItems("Ana Júlia", "Maria Joaquina")) //metodos relacionados ao Groovy e não ao java (String)
            .body("findAll{it.name.size() > 9}.name", hasItems("Maria Joaquina", "João da Silva"))
            .body("findAll{it.name.length() > 9}.name", hasItems("Maria Joaquina", "João da Silva"))
            .body("name.collect{it.toUpperCase()}", hasItems("MARIA JOAQUINA"))
            .body("id.max()", is(3))
            .body("id.min()", is(1))
            .body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678f, 0.001)))
            .body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(4000d)))
        ;
    }

    @Test
    public void devoUnirJasonPathComJava(){
        List<String> names =
        given()
        .when()
            .get("http://restapi.wcaquino.me/users")
        .then()
            .statusCode(200)
            .extract().path("name")
        ;

        Assert.assertEquals(names.size(), 3);
        Assert.assertTrue(names.get(0).equalsIgnoreCase("joão da silva"));
    }
}
