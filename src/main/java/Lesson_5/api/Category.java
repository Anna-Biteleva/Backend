package Lesson_5.api;

import Lesson_5.dto.GetCategoryResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Category {
    @GET( "categories/{id}")

    Call<GetCategoryResponse> getCategory  (@Path("id") int id);
}
