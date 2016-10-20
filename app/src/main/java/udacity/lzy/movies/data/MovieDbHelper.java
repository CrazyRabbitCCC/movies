package udacity.lzy.movies.data;
// @author: lzy  time: 2016/10/18.


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import udacity.lzy.movies.Bean.MovieBean;
import udacity.lzy.movies.Bean.ReviewBean;
import udacity.lzy.movies.Bean.VideoBean;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static MovieDbHelper mHelper;

    public static MovieDbHelper getInstance(Context context) {
        if (mHelper == null) {
            synchronized (MovieDbHelper.class) {
                if (mHelper == null) {
                    mHelper = new MovieDbHelper(context);
                }
            }
        }
        return mHelper;
    }

    private MovieDbHelper(Context context) {
        super(context, "movie.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE = "CREATE TABLE " + MovieBean.TABLE_NAME + "(" +
                MovieBean.ID + " PRIMARY KEY," +
                MovieBean.POSTER_PATH + " NOT NULL," +
                MovieBean.ADULT + "," +
                MovieBean.OVERVIEW + " NOT NULL," +
                MovieBean.RELEASE_DATE + " NOT NULL," +
                MovieBean.ORIGINAL_TITLE + " NOT NULL," +
                MovieBean.ORIGINAL_LANGUAGE + " NOT NULL," +
                MovieBean.TITLE + " NOT NULL," +
                MovieBean.BACKDROP_PATH + " NOT NULL," +
                MovieBean.POPULARITY + " NOT NULL," +
                MovieBean.VOTE_COUNT + " NOT NULL," +
                MovieBean.VIDEO + "," +
                MovieBean.VOTE_AVERAGE + " NOT NULL," +
                MovieBean.GENRE_IDS + " NOT NULL);";
        final String SQL_CREATE_REVIEW = "CREATE TABLE "+ ReviewBean.TABLE_NAME+ " ("+
                ReviewBean. ID+" PRIMARY KEY,"+
                ReviewBean. AUTHOR+" NOT NULL,"+
                ReviewBean. CONTENT+" NOT NULL,"+
                ReviewBean. URL+" NOT NULL,"+
                ReviewBean.MOVIE_ID+"NOT NULL);";

        final String SQL_CREATE_VIDEO = "CREATE TABLE " +VideoBean.TABLE_NAME+"("+
        VideoBean.ID+" PRIMARY KEY,"+
        VideoBean.ISO_639_1+" NOT NULL,"+
        VideoBean.ISO_3166_1+" NOT NULL,"+
        VideoBean.KEY+" NOT NULL,"+
        VideoBean.NAME+" NOT NULL,"+
        VideoBean.SITE+" NOT NULL,"+
        VideoBean.SIZE+" NOT NULL,"+
        VideoBean.TYPE+" NOT NULL,"+
        VideoBean.MOVIE_ID+" NOT NULL);";

        db.execSQL(SQL_CREATE_MOVIE);
        db.execSQL(SQL_CREATE_REVIEW);
        db.execSQL(SQL_CREATE_VIDEO);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MovieBean.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReviewBean.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VideoBean.TABLE_NAME);
        onCreate(db);
    }
}
