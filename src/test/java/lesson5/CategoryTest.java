package lesson5;

import Lesson_5.api.Category;
import Lesson_5.api.ProductService;
import Lesson_5.dto.GetCategoryResponse;
import Lesson_5.utils.RetrofitUtils;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.hamcrest.CoreMatchers;
import retrofit2.Response;

import java.io.IOException;

public class CategoryTest {
    static Category category;
    static ProductService productService;


    @BeforeAll
    static void beforeAll() {
        category = RetrofitUtils.getRetrofit().create(Category.class);
    }


    @Test
    void getCategoryPositiveTest() throws IOException {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
        Response<GetCategoryResponse> response = category.getCategory(1).execute();
        out.println(response.body().getTitle());


        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(1));
        assertThat(response.body().getTitle(), equalTo("Food"));
        out.println(response.body().getProducts());
        response.body().getProducts().forEach(product -> out.print((product.getId()) + ", "));
        response.body().getProducts().forEach(product ->
                assertThat(product.getCategoryTitle(), equalTo("Food")));
    }

    @Test
    void GetCategoryNotFoodTest() throws IOException {

         Response<GetCategoryResponse> response = category.getCategory(2).execute();
        out.println(response.body().getTitle());
        out.println(response);
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(2));
        assertThat(response.body().getTitle(), not("Food"));
    }

    @Test
    void GetCategoryNegativeTest() throws IOException {
        Response<GetCategoryResponse> response = category.getCategory(100).execute();
        assertThat(response.code(), equalTo(404));
    }
}
