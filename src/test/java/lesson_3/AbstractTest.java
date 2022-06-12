package lesson_3;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import java.io.IOException;


public abstract class AbstractTest {

    private static String apiKey;
    private static String baseUrl;


    @BeforeAll
    static void initTest() throws IOException {

        baseUrl = new String("https://api.spoonacular.com/");
        apiKey = new String("9a0a4fd89fa34539b3e732ed393abb70");
    }

    static void setUp() {

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }


    public static String getApiKey() {
        return apiKey;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

}


