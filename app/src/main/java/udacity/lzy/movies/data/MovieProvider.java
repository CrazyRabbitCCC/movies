package udacity.lzy.movies.data;
// @author: lzy  time: 2016/10/18.


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import udacity.lzy.movies.Bean.MovieBean;

public class MovieProvider extends ContentProvider {
    private static final int MOVIE = 0x10;
    private static final int MOVIE_WITH_ID = 0x11;
    //    private static final int REVIEW = 0x20;
//    private static final int REVIEW_WITH_MOVIE_ID = 0x21;
//    private static final int REVIEW_WITH_ID = 0x22;
//    private static final int VIDEO = 0x30;
//    private static final int VIDEO_WITH_MOVIE_ID = 0x31;
//    private static final int VIDEO_WITH_ID = 0x32;
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

        }

        return null;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        String[] strings = {MovieBean.ID, MovieBean.POSTER_PATH,
                MovieBean.ADULT, MovieBean.OVERVIEW, MovieBean.RELEASE_DATE,
                MovieBean.ORIGINAL_TITLE, MovieBean.ORIGINAL_LANGUAGE, MovieBean.TITLE,
                MovieBean.BACKDROP_PATH, MovieBean.POPULARITY, MovieBean.VOTE_COUNT,
                MovieBean.VIDEO, MovieBean.VOTE_AVERAGE, MovieBean.GENRE_IDS};
        switch (matcher.match(uri)) {
            case MOVIE:
                cursor = dbHelper.getReadableDatabase().query(MovieBean.TABLE_NAME, strings, selection, selectionArgs, null, null, sortOrder);
                break;
            case MOVIE_WITH_ID:
                long id = ContentUris.parseId(uri);// 从uri中取出id
                cursor = dbHelper.getReadableDatabase().query(MovieBean.TABLE_NAME,
                        strings, "id=?",
                        new String[]{String.valueOf(id)}, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;


    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        switch (matcher.match(uri)) {
            case MOVIE:
            case MOVIE_WITH_ID:
                long id = dbHelper.getWritableDatabase().insert(MovieBean.TABLE_NAME, null, values);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        switch (matcher.match(uri)) {
            case MOVIE:
                return dbHelper.getWritableDatabase().delete(MovieBean.TABLE_NAME,
                        selection,selectionArgs);
            case MOVIE_WITH_ID:
                long id = ContentUris.parseId(uri);
                return dbHelper.getWritableDatabase().delete(MovieBean.TABLE_NAME,
                        "id=?", new String[]{String.valueOf(id)});
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (matcher.match(uri)) {
            case MOVIE:
                return dbHelper.getWritableDatabase().update(MovieBean.TABLE_NAME,
                        values,selection,selectionArgs);
            case MOVIE_WITH_ID:
                long id=ContentUris.parseId(uri);
                return dbHelper.getWritableDatabase().update(MovieBean.TABLE_NAME,
                        values,"id=?", new String[]{String.valueOf(id)});
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", MOVIE_WITH_ID);
//        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
//        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/movie_id=*", REVIEW_WITH_MOVIE_ID);
//        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/id=*", REVIEW_WITH_ID);
//
//        matcher.addURI(authority, MovieContract.PATH_VIDEO, VIDEO);
//        matcher.addURI(authority, MovieContract.PATH_VIDEO + "/movie_id=*", VIDEO_WITH_MOVIE_ID);
//        matcher.addURI(authority, MovieContract.PATH_VIDEO + "/id=*", VIDEO_WITH_ID);
        return matcher;
    }
}
