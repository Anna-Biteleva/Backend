package lesson_3;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


public class MealPlanTest extends AbstractTest {
    private String id;



    @Test
    void SearchOffsetNumberTest() {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .body("{\n"
                        + "\"username\": \"anbi\", \n"
                        + "\"firstName\": \"Anna\",\n"
                        + "\"lastName\": \"Biteleva\",\n"
                        + " \"email\": \"an.biteleva@yandex.ru\", \n" + "}")
                .when()
                .post(getBaseUrl() + "users/connect")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();
        System.out.println((String) response.get("username"));
        String username = response.get("username");
        String password = response.get("spoonacularPassword");
        String hash = response.get("hash");
        System.out.println(username + "\n" + password + "\n" + hash);

    }

    @Test

    void GetPlanTemplates(){
        given()
                .queryParam("apiKey", getApiKey())
              //  .queryParam("username", "anbi17")
                .queryParam("hash","80ffcb42496cfffbff0e1b0ae8c7a9f52c28fc19")
                .when()
                .get(getBaseUrl() + "mealplanner/anbi17/templates");}


    @Test
    void AddItem() {

        String id = given()
                .queryParam("hash", "80ffcb42496cfffbff0e1b0ae8c7a9f52c28fc19")
                .queryParam("apiKey", getApiKey())
                .body("{\n"
                        + " \"date\": 1657746982,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"INGREDIENTS\",\n"
                        + " \"value\": {\n"
                        + " \"ingredients\": [\n"
                        + " {\n"
                        + " \"name\": \"5 apples\"\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "}")
                .when()
                .post(getBaseUrl() + "mealplanner/anbi17/items")
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
                .expect()
                .body("status", equalTo("success"))
                .when()
                .delete(getBaseUrl() + "mealplanner/anbi17/items/" + id );

    }

}
