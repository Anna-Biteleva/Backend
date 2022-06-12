package lesson_3;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;


public class ShoppingListTest extends AbstractTest {
    String id;

    @Test
    void AddToSoppingList(){

            id = given()
                    .queryParam("hash", "80ffcb42496cfffbff0e1b0ae8c7a9f52c28fc19")
                    .queryParam("apiKey", getApiKey())
                    .body("{\n" +
                            "\"item\": \"1 package baking powder\",\n" +
                            "\"aisle\": \"Baking\",\n" +
                            "\"parse\": true\n" +
                            "}")
                    .when()
                    .post(getBaseUrl() + "mealplanner/anbi17/shopping-list/items")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .jsonPath()
                    .get("id")
                    .toString();
            System.out.println(id);

        given()
                .queryParam("hash", "80ffcb42496cfffbff0e1b0ae8c7a9f52c28fc19")
                .queryParam("apiKey", getApiKey())
                .response()
                .contentType(ContentType.JSON)
                .expect()
                .statusCode(200)
                .body("aisles[0].items[0].name", is("baking powder"))
                .when()
                .get(getBaseUrl() + "mealplanner/anbi17/shopping-list");

    }

            @AfterEach
            void tearDown(){
            given()
                    .queryParam("hash", "80ffcb42496cfffbff0e1b0ae8c7a9f52c28fc19")
                    .queryParam("apiKey", getApiKey())
                    .delete(getBaseUrl() + "mealplanner/anbi17/shopping-list/items/" + id)
                    .then()
                    .statusCode(200);

        }
        }
