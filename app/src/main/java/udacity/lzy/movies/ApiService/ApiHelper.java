package udacity.lzy.movies.ApiService;
// @author: lzy  time: 2016/10/17.


import com.orhanobut.logger.Logger;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import udacity.lzy.movies.Bean.ApiBean;
import udacity.lzy.movies.Bean.ReviewsResult;
import udacity.lzy.movies.Bean.VideosResult;
import udacity.lzy.movies.iMainListener;

public class ApiHelper {
    private iMainListener main;
    MovieApi movieApi;
    int page = 0;
    int totalPage = 0;

    public ApiHelper(iMainListener main) {
        this.main = main;
        movieApi = createRetrofitService(MovieApi.class);
    }

    public static final String API_KEY = "ba2d44e32a7d377c08a53c5e3f5d4ce4";

    public void loadMovies(int type) {
        main.clearData();
        loadMovies(type, 1);
    }

    public void loadMovies(int type, int page) {
        switch (type) {
            case 0:
                dealMovies(movieApi.getPopular(page, API_KEY));
                break;
            case 1:
                dealMovies(movieApi.getTop(page, API_KEY));
                break;
        }
    }

    public void getVideos(int id) {
        movieApi.getVideos(id, API_KEY)
                .subscribeOn(Schedulers.io())
                .map(VideosResult::getResults)
                .flatMap(Observable::from)
                .map(videoBean -> {
                    videoBean.setMovie_id(id);
                    return videoBean;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(video -> main.addData(video)
                        , throwable -> Logger.d(throwable.getMessage()),
                        () -> Logger.d("Success"));
    }

    public void getReviews(int id) {
        getReviews(id, 1);
    }

    public void getReviews(int id, int page) {
        movieApi.getReviews(id, page, API_KEY)
                .subscribeOn(Schedulers.io())
                .map(result -> {
                    main.setPage(result.getPage());
                    main.setTotalPage(result.getTotal_pages());
                    return result;
                })
                .map(ReviewsResult::getResults)
                .flatMap(Observable::from)
                .map(reviewBean -> {
                    reviewBean.setMovie_id(id);
                    return reviewBean;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(video -> main.addData(video), throwable -> {
                    main.ShowToast("网络连接超时");
                    main.setRefresh(false);
                    Logger.d(throwable.getMessage());
                    main.loadMore(false);
                }, () -> {
                    Logger.d("Success");
                    main.setRefresh(false);
                    main.loadMore(false);
                });
    }

    private void dealMovies(Observable<ApiBean> observable) {
        observable.subscribeOn(Schedulers.io())
                .map(result -> {
                    main.setPage(result.getPage());
                    main.setTotalPage(result.getTotal_pages());
                    return result;
                })
                .map(ApiBean::getResults)
                .flatMap(Observable::from)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(map -> main.addData(map), throwable -> {
                    main.ShowToast("网络连接超时");
                    main.setRefresh(false);
                    Logger.d(throwable.getMessage());
                    main.loadMore(false);
                }, () -> {
                    Logger.d("Success");
                    main.setRefresh(false);
                    main.loadMore(false);
                });
    }

    public static <T> T createRetrofitService(final Class<T> service) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .client(addBuilder(builder).build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.themoviedb.org/3/")
                .build();

        return retrofit.create(service);
    }

    public static OkHttpClient.Builder addBuilder(OkHttpClient.Builder builder) {

        if (builder == null) {
            builder = new OkHttpClient.Builder();
        }
        return builder;
    }
}
