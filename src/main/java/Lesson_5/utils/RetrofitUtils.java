package Lesson_5.utils;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

@UtilityClass
public class RetrofitUtils {

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
     LoggingInterceptor logging2 = new LoggingInterceptor();
     OkHttpClient.Builder httpClient = new OkHttpClient.Builder();



    public Retrofit getRetrofit(){
        logging.setLevel(BODY);
        httpClient.addInterceptor(logging2);
        return new Retrofit.Builder()
                .baseUrl("http://localhost:8189/market/api/v1/")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

}
