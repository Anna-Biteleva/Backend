package lesson_3;

import io.restassured.path.json.JsonPath;

import static org.hamcrest.MatcherAssert.assertThat;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

public class CuisineTest extends AbstractTest {

    String id;

    @Test
    void SearchOffsetNumberTest() {
        JsonPath response = given()

                .queryParam("apiKey", getApiKey())
                .queryParam("diet", "Vegetarian")
                .queryParam("cuisine", "French")
                .queryParam("offset", 2)
                .queryParam("number", 1)
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();
        assertThat(response.get("offset"), equalTo(2));
        assertThat(response.get("number"), equalTo(1));

    }

    @Test
    void SearchByMaxSugar() {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("maxSugar", 50)
                .queryParam("number", 1)
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .body()
                .jsonPath();
        assertThat(response.get("results[0].nutrition.nutrients[0].name"), equalTo("Sugar"));
        assertThat(response.get("results[0].nutrition.nutrients[0].amount"), lessThan(50.0F));

    }


    @Test
    void SearchWithRecipe() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("number", 1)
                .queryParam("addRecipeInformation", "true")
                .queryParam("titleMatch", "Garlicky Kale")
                .response()
                .contentType(ContentType.JSON)
                .header("Connection", "keep-alive")
                .expect()
                .body("results[0].title", is("Garlicky Kale"))
                .body("results[0].vegan", is(true))
                .body("results[0].license", equalTo("CC BY 3.0"))
                .when()
                .get( getBaseUrl() + "recipes/complexSearch");


    }

    @Test
    void SearchIndianCuisine (){
      JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("number", 5)
                .queryParam("cuisine", "Indian")
                .queryParam("diet","Vegetarian")
                .response()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();
        assertThat(response.get("results[1].title"), equalTo("Gujarati Dry Mung Bean Curry"));




    }


    @Test
    void ClassifyByTitle() {
        given()
                .queryParam("apiKey", getApiKey())
                .contentType(ContentType.URLENC)
                .formParam("title","Ratatouille")
                .response()
                .contentType(ContentType.JSON)
                .expect()
                .body("cuisines[2]", equalTo("French"))
                .when()
                .post( getBaseUrl() + "recipes/cuisine");

    }


    @Test
    void ClassifyByIngredients() {
        given()
                .queryParam("apiKey", getApiKey())
                .contentType(ContentType.URLENC)
                .formParam("ingredientList","1 cup breadcrumbs or Panko", "1/2 Tablespoon Cornflour", "1 Egg, lightly beaten" )
                .response()
                .contentType(ContentType.JSON)
                .expect()
                .body("cuisine", equalTo("Mediterranean"))
                .when()
                .post( getBaseUrl() + "recipes/cuisine");

    }

    @Test
            void ClassifyByFetchedTitle(){

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
        //System.out.println("title is " +title );

        given()
                .queryParam("apiKey", getApiKey())
                .contentType(ContentType.URLENC)
                .formParam("title",title)
                .response()
                .contentType(ContentType.JSON)
                .expect()
                .body("cuisines[2]", equalTo("French"))
                .when()
                .post( getBaseUrl() + "recipes/cuisine");

    }

    @Test
    void SearchBritishDesert () {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("number", 5)
                .queryParam("cuisine", "British")
                .queryParam("type", "dessert")
                .response()
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();
        assertThat(response.get("results[0].title"), equalTo("Fresh Cherry Scones"));

    }
    @Test
    void ClassifyBritishDesert(){

        given()
                .queryParam("apiKey", getApiKey())
                .contentType(ContentType.URLENC)
                .formParam("title","Fresh Cherry Scones ")
                .response()
                .contentType(ContentType.JSON)
                .expect()
                .statusCode(200)
                .body("cuisines[2]", equalTo("British"))
                .when()
                .post( getBaseUrl() + "recipes/cuisine");

    }
    @Test
    void ClassifyByTitleAndIngredients() {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .contentType(ContentType.URLENC)
                .formParam("title","Spinach Goats Cheese Roulade Main Dish ")
                .formParam("ingredientList","1 cup breadcrumbs or Panko", "1/2 Tablespoon Cornflour", "1 Egg, lightly beaten")
                .response()
                .contentType(ContentType.JSON)
                .when()
                .post( getBaseUrl() + "recipes/cuisine")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();
        assertThat(response.get("cuisines[1]"), equalTo("German"));

    }

    }
