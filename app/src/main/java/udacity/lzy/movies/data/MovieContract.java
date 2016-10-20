package udacity.lzy.movies.data;
// @author: lzy  time: 2016/10/18.


import android.net.Uri;

public class MovieContract {
    public static final String CONTENT_AUTHORITY ="udacity.lzy.movies.data.provider";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_MOVIE = "movies";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_VIDEO = "video";


}
