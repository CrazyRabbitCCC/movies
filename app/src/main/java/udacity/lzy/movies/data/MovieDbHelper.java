package udacity.lzy.movies.data;
// @author: lzy  time: 2016/10/18.


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import udacity.lzy.movies.Bean.MovieBean;

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
                MovieBean.ID + " INTEGER PRIMARY KEY," +
                MovieBean.POSTER_PATH + " TEXT NOT NULL," +
                MovieBean.ADULT + " BOOLEAN DEFAULT FALSE," +
                MovieBean.OVERVIEW + " TEXT NOT NULL," +
                MovieBean.RELEASE_DATE + " TEXT NOT NULL," +
                MovieBean.ORIGINAL_TITLE + " TEXT NOT NULL," +
                MovieBean.ORIGINAL_LANGUAGE + " TEXT NOT NULL," +
                MovieBean.TITLE + " TEXT NOT NULL," +
                MovieBean.BACKDROP_PATH + " TEXT NOT NULL," +
                MovieBean.POPULARITY + " TEXT NOT NULL," +
                MovieBean.VOTE_COUNT + " INTEGER NOT NULL," +
                MovieBean.VIDEO + " BOOLEAN DEFAULT FALSE," +
                MovieBean.VOTE_AVERAGE + " TEXT NOT NULL," +
                MovieBean.GENRE_IDS + " TEXT NOT NULL);";
//        final String SQL_CREATE_REVIEW = "CREATE TABLE "+ ReviewBean.TABLE_NAME+ " ("+
//                ReviewBean. ID+" PRIMARY KEY,"+
//                ReviewBean. AUTHOR+" NOT NULL,"+
//                ReviewBean. CONTENT+" NOT NULL,"+
//                ReviewBean. URL+" NOT NULL,"+
//                ReviewBean.MOVIE_ID+"NOT NULL);";
//
//        final String SQL_CREATE_VIDEO = "CREATE TABLE " +VideoBean.TABLE_NAME+"("+
//        VideoBean.ID+" PRIMARY KEY,"+
//        VideoBean.ISO_639_1+" NOT NULL,"+
//        VideoBean.ISO_3166_1+" NOT NULL,"+
//        VideoBean.KEY+" NOT NULL,"+
//        VideoBean.NAME+" NOT NULL,"+
//        VideoBean.SITE+" NOT NULL,"+
//        VideoBean.SIZE+" NOT NULL,"+
//        VideoBean.TYPE+" NOT NULL,"+
//        VideoBean.MOVIE_ID+" NOT NULL);";

        db.execSQL(SQL_CREATE_MOVIE);
//        db.execSQL(SQL_CREATE_REVIEW);
//        db.execSQL(SQL_CREATE_VIDEO);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MovieBean.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + ReviewBean.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + VideoBean.TABLE_NAME);
        onCreate(db);
    }
}
