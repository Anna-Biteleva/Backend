package lesson_6;

import Lesson_5.api.Category;
import Lesson_5.api.ProductService;
import Lesson_5.dto.GetCategoryResponse;
import Lesson_5.utils.RetrofitUtils;
import org.apache.ibatis.session.SqlSession;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;

import static java.lang.System.out;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.InputStream;
import java.util.List;

public class Category_6_Test {
    static Category category;
    String resource = "mybatis-config.xml";
    static ProductService productService;
    SqlSession sqlSession;
    db.dao.CategoriesMapper categoriesMapper;
    db.model.CategoriesExample categoriesExample;


    @BeforeAll
    static void beforeAll() {
        category = RetrofitUtils.getRetrofit().create(Category.class);

    }

    @BeforeEach
    void setUpCat() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        sqlSession = sqlSessionFactory.openSession();
        categoriesMapper = sqlSession.getMapper(db.dao.CategoriesMapper.class);
        categoriesExample = new db.model.CategoriesExample();

    }


    @Test
    void getCategoryPositiveTest() throws IOException {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
        Response<GetCategoryResponse> response = category.getCategory(2).execute();
        out.println(response.body().getTitle());

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(2));
        assertThat(response.body().getTitle(), equalTo("Electronic"));
        out.println(response.body().getProducts());
        response.body().getProducts().forEach(product ->
                assertThat(product.getCategoryTitle(), equalTo("Electronic")));
        categoriesExample.createCriteria().andIdEqualTo(2L);
        List<db.model.Categories> list = categoriesMapper.selectByExample(categoriesExample);
        System.out.println("**********\n Title of category with ID:"+ list.get(0).getId() + " is - "+list.get(0).getTitle()+ "\n**********");



    }

    @Test
    void GetCategoryNotButFoodTest() throws IOException {

        Response<GetCategoryResponse> response = category.getCategory(1).execute();
        out.println("Title: " + response.body().getTitle());
        out.println(response);
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(1));
        assertThat(response.body().getTitle(), not("Electronic"));
        categoriesExample.createCriteria().andIdNotBetween(2L, 100L);
        List<db.model.Categories> list = categoriesMapper.selectByExample(categoriesExample);
        System.out.println("**********\nID:"+ list.get(0).getId() + "\nTitle:"+list.get(0).getTitle()+ "\n**********");

    }

    @Test
    void GetCategoryNegativeTest() throws IOException {
        Response<GetCategoryResponse> response = category.getCategory(100).execute();
        assertThat(response.code(), equalTo(404));
        categoriesExample.createCriteria().andIdEqualTo(100L);
        System.out.println("**********\n Amount of categories with ID=100: " + categoriesMapper.countByExample( categoriesExample)+ "\n**********");

    }


    @Test
    void CreateClothesCategoryTest()  {
        db.model.Categories categories = new db.model.Categories();
        categories.setTitle("Clothes");
        categoriesMapper.insert(categories);
        sqlSession.commit();
        out.println( );
        categoriesExample.createCriteria().andTitleEqualTo("Clothes");
        List<db.model.Categories> list = categoriesMapper.selectByExample(categoriesExample);
        System.out.println("**********\n New category is created with:\nID:"+ list.get(0).getId() + "\nTitle:"+list.get(0).getTitle()+ "\n**********");
        categoriesMapper.deleteByExample(categoriesExample);
        sqlSession.commit();

    }
}
