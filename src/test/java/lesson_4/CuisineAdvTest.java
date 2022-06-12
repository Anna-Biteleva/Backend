package lesson_4;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lesson_3.AbstractTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

public class CuisineAdvTest extends AbstractTest {
    private static ResponseSpecification responseSpecification;
    private static RequestSpecification requestSpecification;



    @BeforeEach

    void beforeTest(){

        requestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey", getApiKey())
                .build();

    responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();
}


    @Test
    void SearchOffsetNumberTest() {
        responseSpecification = responseSpecification
                         .expect()
                         .body(("offset"), equalTo(2))
                         .body(("number"), equalTo(1));

                        given().spec(requestSpecification)
                .queryParam("diet", "Vegetarian")
                .queryParam("cuisine", "French")
                .queryParam("offset", 2)
                .queryParam("number", 1)
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .spec(responseSpecification);

    }

    @Test
    void SearchByMaxSugar() {
        responseSpecification = responseSpecification
                .expect()
                .body(("results[0].nutrition.nutrients[0].name"), equalTo("Sugar"))
                .body(("results[0].nutrition.nutrients[0].amount"), lessThan(50.0F));

         given().spec(requestSpecification)
                 .queryParam("maxSugar", 50)
                .queryParam("number", 1)
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                 .then()
                 .spec(responseSpecification);
    }


    @Test
    void SearchWithRecipe() {


        requestSpecification = requestSpecification
                .queryParam("number", 1)
                .queryParam("addRecipeInformation", "true")
                .queryParam("titleMatch", "Garlicky Kale");

        responseSpecification = responseSpecification
                .expect()
                .header("Connection", "keep-alive")
                .body("results[0].title", is("Garlicky Kale"))
                .body("results[0].vegan", is(true))
                .body("results[0].license", equalTo("CC BY 3.0"));

              given().spec(requestSpecification)
                .when()
                .get( getBaseUrl() + "recipes/complexSearch")
                .then()
                .spec(responseSpecification);

    }

    @Test
    void SearchIndianCuisine (){
        requestSpecification = requestSpecification
                .queryParam("number", 5)
                .queryParam("cuisine", "Indian")
                .queryParam("diet","Vegetarian");

        responseSpecification = responseSpecification
                .expect()
                .body(("results[1].title"), equalTo("Gujarati Dry Mung Bean Curry"));

          given().spec(requestSpecification)
                  .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .spec(responseSpecification);



    }


    @Test
    void ClassifyByTitle() {
        requestSpecification = requestSpecification
                .contentType(ContentType.URLENC)
                .formParam("title","Ratatouille");

        responseSpecification = responseSpecification
                .expect()
                .body("cuisines[2]", equalTo("French"));

        given().spec(requestSpecification)
                .when()
                .post( getBaseUrl() + "recipes/cuisine")
                .then()
                .spec(responseSpecification);


    }


    @Test
    void ClassifyByIngredients() {
        requestSpecification = requestSpecification
         .contentType(ContentType.URLENC)
         .formParam("ingredientList","1 cup breadcrumbs or Panko", "1/2 Tablespoon Cornflour", "1 Egg, lightly beaten" );

        responseSpecification = responseSpecification
                .expect()
                .body("cuisines[2]", equalTo("Italian"));


        given().spec(requestSpecification)
                .when()
                .post( getBaseUrl() + "recipes/cuisine")
                .then()
                .spec(responseSpecification);

    }

    @Test
    void ClassifyByFetchedTitle(){

        responseSpecification = responseSpecification
                .expect()
                .body("cuisines[2]", equalTo("French"));


        String title= given()
                .queryParam("apiKey", getApiKey())
                .queryParam("cuisine","French")
                .queryParam("number","1")
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .extract()
                .jsonPath()
                .get("results[0].title")
                .toString();
        System.out.println(title);


        given().spec(requestSpecification)
                .contentType(ContentType.URLENC)
                .formParam("title",title)
                .when()
                .post( getBaseUrl() + "recipes/cuisine")
                .then()
                .spec(responseSpecification);

    }

    @Test
    void SearchBritishDesert () {

        requestSpecification = requestSpecification
                .queryParam("number", 5)
                .queryParam("cuisine", "British")
                .queryParam("type","dessert");

        responseSpecification = responseSpecification
                .expect()
                .body(("results[0].title"), equalTo("Fresh Cherry Scones"));


        given().spec(requestSpecification)
                .response()
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .spec(responseSpecification);

    }
    @Test
    void ClassifyBritishDesert(){

        requestSpecification = requestSpecification
                .contentType(ContentType.URLENC)
                .formParam("title","Fresh Cherry Scones ");

        responseSpecification =responseSpecification
                .expect()
                .body("cuisines[2]", equalTo("British"));


        given().spec(requestSpecification)
                .when()
                .post( getBaseUrl() + "recipes/cuisine")
                .then()
                .spec(responseSpecification);


    }
    @Test
    void ClassifyByTitleAndIngredients() {

        requestSpecification = requestSpecification
                .contentType(ContentType.URLENC)
                .formParam("title","Spinach Goats Cheese Roulade Main Dish ")
                .formParam("ingredientList","1 cup breadcrumbs or Panko", "1/2 Tablespoon Cornflour", "1 Egg, lightly beaten" );

        responseSpecification = responseSpecification
                .expect()
                .body(("cuisines[1]"), equalTo("German"));

        given().spec(requestSpecification)
                .when()
                .post( getBaseUrl() + "recipes/cuisine")
                .then()
                .spec(responseSpecification);


    }

}
