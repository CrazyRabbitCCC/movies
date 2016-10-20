package udacity.lzy.movies.ApiService;
// @author: lzy  time: 2016/10/17.


import android.net.Uri;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import udacity.lzy.movies.Bean.ApiBean;
import udacity.lzy.movies.Bean.MovieBean;
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

    public static final String API_KEY = " ";
    public static final String BASE_CONTENT_URI = "content://udacity.lzy.movies.data.provider/movies";

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
            case 2:
                getCollectMovies();
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

    private void getCollectMovies(Uri... uris){
        Observable.from(uris)
                .subscribeOn(Schedulers.io())
                .map(uri->  main.getActivity().getContentResolver().query(uri, null, null, null, null))
                .map(cursor -> {
                    boolean collected=cursor.moveToFirst();
                    cursor.close();
                    return collected;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(collected -> main.setCollect(collected), throwable -> {
                    main.ShowToast("读取收藏信息出错");
                    main.setRefresh(false);
                    Logger.d(throwable.getMessage());
                    main.loadMore(false);
                }, () -> {
                    Logger.d("Success");
                    main.setRefresh(false);
                    main.loadMore(false);
                });
    }

    public void getCollectMovies(int id) {
        getCollectMovies(Uri.parse(BASE_CONTENT_URI+"/"+id));
    }

    private void getCollectMovies() {
//        Uri uri = Uri.parse(BASE_CONTENT_URI);
        main.clearData();
        Observable.from(new Uri[]{Uri.parse(BASE_CONTENT_URI)})
                .subscribeOn(Schedulers.io())
                .map(uri -> main.getActivity().getContentResolver().query(uri, null, null, null, null))
                .map(cursor -> {
                    List<MovieBean> list = new ArrayList<>();
                    main.setPage(1);
                    main.setTotalPage(1);
                    while (cursor.moveToNext()) {
                        MovieBean movie = new MovieBean();
                        movie.setId(cursor.getInt(cursor.getColumnIndex(MovieBean.ID)));
                        movie.setPoster_path(cursor.getString(cursor.getColumnIndex(MovieBean.POSTER_PATH)));
                        movie.setAdult(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(MovieBean.ADULT))));
                        movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieBean.OVERVIEW)));
                        movie.setRelease_date(cursor.getString(cursor.getColumnIndex(MovieBean.RELEASE_DATE)));
                        movie.setOriginal_title(cursor.getString(cursor.getColumnIndex(MovieBean.ORIGINAL_TITLE)));
                        movie.setOriginal_language(cursor.getString(cursor.getColumnIndex(MovieBean.ORIGINAL_LANGUAGE)));
                        movie.setPopularity(cursor.getDouble(cursor.getColumnIndex(MovieBean.POPULARITY)));
                        movie.setVote_count(cursor.getInt(cursor.getColumnIndex(MovieBean.VOTE_COUNT)));
                        movie.setVideo(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(MovieBean.VIDEO))));
                        movie.setVote_average(cursor.getDouble(cursor.getColumnIndex(MovieBean.VOTE_AVERAGE)));
                        movie.setGenre_ids(cursor.getString(cursor.getColumnIndex(MovieBean.GENRE_IDS)));
                        list.add(movie);
                    }
                    cursor.close();
                    return list;
                })
                .flatMap(Observable::from)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(map -> main.addData(map), throwable -> {
                    main.ShowToast("读取收藏信息出错");
                    main.setRefresh(false);
                    Logger.d(throwable.getMessage());
                    main.loadMore(false);
                }, () -> {
                    Logger.d("Success");
                    main.setRefresh(false);
                    main.loadMore(false);
                });
    }

    public void insertCollectMovies(MovieBean movie) {
        Observable.from(new Uri[]{Uri.parse(BASE_CONTENT_URI)})
                .subscribeOn(Schedulers.io())
                .map(uri-> main.getActivity().getContentResolver().insert(uri, movie.getContentValues()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(collected -> main.setCollect(true), throwable -> {
                    main.ShowToast("收藏失败");
                    main.setRefresh(false);
                    Logger.d(throwable.getMessage());
                    main.loadMore(false);
                }, () -> {
                    Logger.d("Success");
                    main.setRefresh(false);
                    main.loadMore(false);
                });
    }

    public void deleteCollectMovies(int id) {
        Uri uri1;
        if (id == -1)
            uri1 = Uri.parse(BASE_CONTENT_URI);
        else
            uri1 = Uri.parse(BASE_CONTENT_URI + "/" + id);
        Observable.from(new Uri[]{uri1})
                .subscribeOn(Schedulers.io())
                .map(uri-> main.getActivity().getContentResolver().delete(uri, null, null)
                        )
                .map(i ->{
                    if (i>0)
                        return true;
                    else
                        throw new RuntimeException("取消收藏失败");
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(collected->main.setCollect(!collected), throwable -> {
                    main.ShowToast("取消收藏失败");
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
