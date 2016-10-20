package udacity.lzy.movies.Bean;
// @author: lzy  time: 2016/10/18.

import android.content.ContentValues;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public  class MovieBean implements Serializable {

    public static final String TABLE_NAME="movie_table";
    public static final String POSTER_PATH="poster_path";
    public static final String ADULT="adult";
    public static final String OVERVIEW="overview";
    public static final String RELEASE_DATE="release_date";
    public static final String ID="id";
    public static final String ORIGINAL_TITLE="original_title";
    public static final String ORIGINAL_LANGUAGE="original_language";
    public static final String TITLE="title";
    public static final String BACKDROP_PATH="backdrop_path";
    public static final String POPULARITY="popularity";
    public static final String VOTE_COUNT="vote_count";
    public static final String VIDEO="video";
    public static final String VOTE_AVERAGE="vote_average";
    public static final String GENRE_IDS="genre_ids";

    private String poster_path;
    private boolean adult;
    private String overview;
    private String release_date;
    private int id=0;
    private String original_title;
    private String original_language;
    private String title;
    private String backdrop_path;
    private double popularity;
    private int vote_count;
    private boolean video;
    private double vote_average;
    private List<Integer> genre_ids;

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public String getGenreIds(){
        String s="";
        for (Integer id :
                genre_ids) {
            s += id + ",";
        }
        if (!TextUtils.isEmpty(s))
            s=s.substring(0,s.length()-1);
        return s;
    }

    public void setGenre_ids(List<Integer> genre_id) {this.genre_ids = genre_id;}
    public void setGenre_ids(String genre_id) {
        String[] split = genre_id.split(",");
        List<Integer> genre_ids=new ArrayList<>();
        for (String id : split) {
            try {
                genre_ids.add(Integer.parseInt(id));
            } catch (Exception e) {

            }
        }
        this.genre_ids=genre_ids;

    }

    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();
        values.put(MovieBean.ID, getId());
        values.put(MovieBean.POSTER_PATH, getPoster_path());
        values.put(MovieBean.ADULT, adult);
        values.put(MovieBean.OVERVIEW,getOverview());
        values.put(MovieBean.RELEASE_DATE,getRelease_date());
        values.put(MovieBean.ORIGINAL_TITLE, getOriginal_title());
        values.put(MovieBean.ORIGINAL_LANGUAGE, getOriginal_language());
        values.put(MovieBean.TITLE, getTitle());
        values.put(MovieBean.BACKDROP_PATH,getBackdrop_path());
        values.put(MovieBean.POPULARITY, getPopularity());
        values.put(MovieBean.VOTE_COUNT, getVote_average());
        values.put(MovieBean.VIDEO, isVideo());
        values.put(MovieBean.VOTE_AVERAGE, getVote_average());
        values.put(MovieBean.GENRE_IDS,getGenreIds());
        return values;
    }

}
