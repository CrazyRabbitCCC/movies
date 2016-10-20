package udacity.lzy.movies.ApiService;
// @author: lzy  time: 2016/10/17.


import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import udacity.lzy.movies.Bean.ApiBean;
import udacity.lzy.movies.Bean.ReviewsResult;
import udacity.lzy.movies.Bean.VideosResult;

public interface MovieApi {
//    @GET("movie/popular")
//    Observable<Map<String,Object>> getPopular(@Query("api_key") String key);
//    @GET("movie/top_rated")
//    Observable<Map<String,Object>> getTop(@Query("api_key") String key);
    @GET("movie/popular")
    Observable<ApiBean> getPopular(@Query("page") int page,@Query("api_key") String key);
    @GET("movie/top_rated")
    Observable<ApiBean> getTop(@Query("page") int page,@Query("api_key") String key);

    @GET("movie/{id}/videos")
    Observable<VideosResult> getVideos(@Path("id")int id, @Query("api_key") String key);
    @GET("movie/{id}/reviews")
    Observable<ReviewsResult> getReviews(@Path("id")int id,@Query("page") int page, @Query("api_key") String key);

    // http://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]
    // movie/{id}/videos
    // movie/{id}/reviews


}
