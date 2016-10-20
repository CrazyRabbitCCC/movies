package udacity.lzy.movies.data;
// @author: lzy  time: 2016/10/18.


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class MovieProvider extends ContentProvider {
    private static final int MOVIE = 0x10;
    private static final int MOVIE_WITH_ID = 0x11;
    private static final int REVIEW = 0x20;
    private static final int REVIEW_WITH_MOVIE_ID = 0x21;
    private static final int REVIEW_WITH_ID = 0x22;
    private static final int VIDEO = 0x30;
    private static final int VIDEO_WITH_MOVIE_ID = 0x31;
    private static final int VIDEO_WITH_ID = 0x32;
    private static final UriMatcher matcher = buildUriMatcher();

    private MovieDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = MovieDbHelper.getInstance(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (matcher.match(uri)) {
            case MOVIE:
                break;
            case MOVIE_WITH_ID:
                break;
            case REVIEW:
                break;
            case REVIEW_WITH_MOVIE_ID:
                break;
            case REVIEW_WITH_ID:
                break;
            case VIDEO:
                break;
            case VIDEO_WITH_MOVIE_ID:
                break;
            case VIDEO_WITH_ID:
                break;
        }

        return null;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (matcher.match(uri)) {
            case MOVIE:
                cursor=getMovies(projection,selection,selectionArgs,sortOrder);
                break;
            case MOVIE_WITH_ID:
                cursor=null;
                break;
            case REVIEW:
                cursor=null;
                break;
            case REVIEW_WITH_MOVIE_ID:
                cursor=null;
                break;
            case REVIEW_WITH_ID:
                cursor=null;
                break;
            case VIDEO:
                cursor=null;
                break;
            case VIDEO_WITH_MOVIE_ID:
                cursor=null;
                break;
            case VIDEO_WITH_ID:
                cursor=null;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    private Cursor getMovies(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
//        database.query("");
        return null;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        switch (matcher.match(uri)) {
            case MOVIE:
                break;
            case MOVIE_WITH_ID:
                break;
            case REVIEW:
                break;
            case REVIEW_WITH_MOVIE_ID:
                break;
            case REVIEW_WITH_ID:
                break;
            case VIDEO:
                break;
            case VIDEO_WITH_MOVIE_ID:
                break;
            case VIDEO_WITH_ID:
                break;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        switch (matcher.match(uri)) {
            case MOVIE:
                break;
            case MOVIE_WITH_ID:
                break;
            case REVIEW:
                break;
            case REVIEW_WITH_MOVIE_ID:
                break;
            case REVIEW_WITH_ID:
                break;
            case VIDEO:
                break;
            case VIDEO_WITH_MOVIE_ID:
                break;
            case VIDEO_WITH_ID:
                break;
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (matcher.match(uri)) {
            case MOVIE:
                break;
            case MOVIE_WITH_ID:
                break;
            case REVIEW:
                break;
            case REVIEW_WITH_MOVIE_ID:
                break;
            case REVIEW_WITH_ID:
                break;
            case VIDEO:
                break;
            case VIDEO_WITH_MOVIE_ID:
                break;
            case VIDEO_WITH_ID:
                break;
        }
        return 0;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/movie_id=*", REVIEW_WITH_MOVIE_ID);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/id=*", REVIEW_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_VIDEO, VIDEO);
        matcher.addURI(authority, MovieContract.PATH_VIDEO + "/movie_id=*", VIDEO_WITH_MOVIE_ID);
        matcher.addURI(authority, MovieContract.PATH_VIDEO + "/id=*", VIDEO_WITH_ID);

        return matcher;
    }
}
