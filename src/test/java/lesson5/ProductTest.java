package lesson5;
import Lesson_5.api.ProductService;
import Lesson_5.dto.Product;
import Lesson_5.utils.RetrofitUtils;
import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;


import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
;

public class ProductTest {
    static ProductService productService;
    Product product = null;
    Faker faker = new Faker();
    int id;
    String title;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));

    }

    @Test
    void createProductInFoodCategoryTest() throws  IOException {
        Response<Product> response = productService.createProduct(product)
                .execute();
        id =  response.body().getId();
        title = response.body().getTitle();
        System.out.println("ID: "+id + ", Title: " + title);
        assertThat(response.isSuccessful(), CoreMatchers.is(true));

    }
        @SneakyThrows
        @AfterEach
        void tearDown () {
            Response<ResponseBody> response = productService.deleteProduct(id).execute();
            assertThat(response.isSuccessful(), CoreMatchers.is(true));
        }
    }
