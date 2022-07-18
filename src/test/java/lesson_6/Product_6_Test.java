package lesson_6;

import Lesson_5.api.ProductService;
import Lesson_5.dto.Product;
import Lesson_5.utils.RetrofitUtils;
import com.github.javafaker.Faker;
import db.model.Products;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class Product_6_Test {

    static ProductService productService;
    Product product = null;
    Faker faker = new Faker();
    int id;
    String resource  = "mybatis-config.xml";
    db.dao.ProductsMapper productsMapper;
    db.model.ProductsExample productsExample;
    db.model.Products products = new db.model.Products();
    SqlSession sqlSession;


    @BeforeAll
    static void beforeAll()  {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);

    }

    @BeforeEach
    void setUp() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        sqlSession = sqlSessionFactory.openSession();
        productsMapper =  sqlSession.getMapper(db.dao.ProductsMapper.class);
        productsExample = new db.model.ProductsExample();


    }

    @Test
    void createProductInFoodCategoryTest() throws IOException {
        product = new Product  ()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food").withPrice((int) (Math.random() * 10000));
        Response<Product> response = productService.createProduct(product)
                .execute();
        id =  response.body().getId();
        product = new Product  ()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food").withPrice((int) (Math.random() * 10000));
        Response<Product> response1 = productService.createProduct(product)
                .execute();
        db.model.Products selected = productsMapper.selectByPrimaryKey((long) id);
        System.out.println ("********** \nFirst item -ID: " + selected.getId() + " Title: " + selected.getTitle());
        id =  response1.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        db.model.Products selected1 = productsMapper.selectByPrimaryKey((long) id);
        System.out.println ("Second item -ID: " + selected1.getId() + " Title: " + selected1.getTitle());
        productsExample.createCriteria().andCategory_idEqualTo(1l);
        System.out.println("Amount of food items with 2 added: " + productsMapper.countByExample(productsExample));
        productsMapper.deleteByExample(productsExample);
        System.out.println("Amount of items left after removal : " + productsMapper.countByExample(productsExample)+"\n**********");
        sqlSession.commit();

    }

    @Test
    void createDeleteProductInDBTest(){
        products = new db.model.Products();
        products.setTitle("NewProduct");
        products.setPrice(150);
        products.setCategory_id(1L);
        productsMapper.insert(products);
        productsExample.createCriteria().andTitleEqualTo("NewProduct");
        sqlSession.commit();
        productsExample.createCriteria().andTitleEqualTo("NewProduct");
        List<Products> list = productsMapper.selectByExample(productsExample);
        System.out.println("**********\nID:"+ list.get(0).getId() + "\nTitle:"+list.get(0).getTitle()+
                "\nPrice:" + list.get(0).getPrice()+  "\nCategory_id:" +list.get(0).getCategory_id());

        System.out.println(productsMapper.countByExample(productsExample)+ " item is on the list \n**********");
        productsMapper.deleteByExample(productsExample);
        sqlSession.commit();
    }



}
