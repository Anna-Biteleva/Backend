package lesson_4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lesson4.dto.AddToShL;
import lesson4.dto.request.AddItem;
import lesson4.dto.request.Ingredient;
import lesson4.dto.request.Value;
import lesson_3.AbstractTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;



import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class ShoppingAdvTest extends AbstractTest {
    private static ResponseSpecification responseSpecification;
    private static RequestSpecification requestSpecification;
    private static final ObjectMapper Mapper = new ObjectMapper();

    String id;
    String url;
    Value value;
    Ingredient ingredients;


    @BeforeEach
    void beforeTest() {

        requestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey", getApiKey())
                .addQueryParam("hash", "80ffcb42496cfffbff0e1b0ae8c7a9f52c28fc19")
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();
    }

    @Test

    void AddToSoppingListGet(){

        AddToShL addToShL = new AddToShL();
        addToShL.setItem("1 package baking powder");
        addToShL.setAisle( "Baking");
        addToShL.setParse(true);

        url = "mealplanner/anbi17/shopping-list";

        responseSpecification = responseSpecification
                .expect()
                .body("aisles[0].items[0].name", is("baking powder"));

        id = given().spec(requestSpecification)
                .body(addToShL)
                .when()
                .post(getBaseUrl() + url +"/items")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .get("id")
                .toString();


      given().spec(requestSpecification)
                .when()
                .get(getBaseUrl() + url)
                .then()
                .spec(responseSpecification);

    }

    @Test

    void AddItemAdv()  {
        url= "mealplanner/anbi17";

        ingredients = new Ingredient();
         value = new Value();
        AddItem addItem= new AddItem();
        addItem.setDate(1657746982);
        addItem.setSlot(1);
        addItem.setPosition(0);
        addItem.setType("INGREDIENTS");
        addItem.setValue(value);
        ingredients.setName("5apples");



                id = given().spec(requestSpecification)
                .body(addItem)
                .when()
                .post(getBaseUrl() + url +"/items")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .get("id")
                .toString();



         }

   @AfterEach
    void tearDownAdv(){
        given()
                .spec(requestSpecification)
                .delete(getBaseUrl() + url + "/items/" + id)
                .then()
                .statusCode(200);

    }
  }

