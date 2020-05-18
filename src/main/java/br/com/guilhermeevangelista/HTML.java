package br.com.guilhermeevangelista;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class HTML {
    @Test
    public void devoTrabalharComHtml(){
        given()
            .log().all()
        .when()
            .get("http://restapi.wcaquino.me/v2/users")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(ContentType.HTML)
            .body("//td[text() = 'Ana Júlia']", notNullValue())
            .appendRootPath("html.body.div.table.tbody.")
                .body("tr.size()", is(3))
                .body("tr[1].td[2]", is("25"))
        ;
    }
}
