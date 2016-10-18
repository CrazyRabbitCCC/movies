package udacity.lzy.movies.ApiService;
// @author: lzy  time: 2016/10/17.


import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import udacity.lzy.movies.iMainListener;

public class ApiHelper {
    private iMainListener main;

    public ApiHelper(iMainListener main){
        this.main=main;
    }
    public static final String API_KEy="[my key]";

    public void loadData(int type) {
        main.clearData();
        switch (type){
            case 0:
                getPopular(ApiHelper.API_KEy);
                break;
            case 1:
                getTop(ApiHelper.API_KEy);
                break;
        }
    }

    private  void getPopular(String key) {
        MovieApi movieApi= createRetrofitService(MovieApi.class);
        dealData(movieApi.getPopular(key));
    }
    private  void getTop(String key) {
        MovieApi movieApi = createRetrofitService(MovieApi.class);
        dealData(movieApi.getTop(key));
    }


    private  void dealData(Observable<Map<String, Object>> observable){
        observable.subscribeOn(Schedulers.io())
                .map(map->(List<Map<String, Object>>) map.get("results"))
                .flatMap(Observable::from)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(map ->main.addData(map), throwable ->
                {
                    main.ShowToast("网络连接超时");
                    Logger.d(throwable.getMessage());
                }, ()->Logger.d("Success"));
        //                .map( data ->{
//                    List<Map<String,Object>> list=new ArrayList<>();
//                    if (data!=null&&data.get("list")!=null){
//                        list= (List<Map<String, Object>>) data.get("list");
//                    }
//                    return list;
//                })
//                .flatMap(Observable::from)
//                .map(listBean -> {
//                    String s="";
//                    if (listBean.get("dt_txt")!=null)
//                        s+=listBean.get("dt_txt").toString()+"\n";
//                    List<Map<String,Object>> weather = (List<Map<String, Object>>) listBean.get("weather");
//                    if (weather!=null)
//                        s+="weather:"+weather.get(0).get("main");
//                    Map<String,Object> main = (Map<String, Object>) listBean.get("main");
//                    if (main!=null){
//                        s+=" temp:"+main.get("temp")+", min" +main.get("temp_min")+ ", max"+main.get("temp_max");
//                    }
//                    return s;
//                })
//                .subscribe(
//                        s -> {
//                            arrayData.add(s);
//                            arrayAdapter.notifyDataSetChanged();
//                        },
//                        throwable -> Logger.d(throwable.getMessage()),
//                        ()-> Logger.d("Success"));

    }

    public static <T>T createRetrofitService(final Class<T> service) {
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
