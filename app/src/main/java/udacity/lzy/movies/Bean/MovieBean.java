package udacity.lzy.movies.Bean;
// @author: lzy  time: 2016/10/18.

import java.util.List;

public  class MovieBean {
    final String SQL_CREATE_MOVIE = "CREATE TABLE " + MovieBean.TABLE_NAME + "(" +
            MovieBean.RELEASE_DATE + " TEXT NOT NULL," +
            MovieBean.ORIGINAL_TITLE + " TEXT NOT NULL," +
            MovieBean.ORIGINAL_LANGUAGE + " TEXT NOT NULL," +
            MovieBean.TITLE + " TEXT NOT NULL," +
            MovieBean.BACKDROP_PATH + " TEXT NOT NULL," +
            MovieBean.POPULARITY + " TEXT NOT NULL," +
            MovieBean.VOTE_COUNT + " TEXT NOT NULL," +
            MovieBean.VIDEO + " BOOLEAN DEFAULT FALSE," +
            MovieBean.VOTE_AVERAGE + " TEXT NOT NULL," +
            MovieBean.GENRE_IDS + " TEXT NOT NULL);";
    public static final String TABLE_NAME="movie_table";
    public static final String POSTER_PATH="poster_path TEXT";
    public static final String ADULT="adult BOOLEAN DEFAULT FALSE";
    public static final String OVERVIEW="overview TEXT";
    public static final String RELEASE_DATE="release_date  TEXT";
    public static final String ID="movie_id INTEGER";
    public static final String ORIGINAL_TITLE="original_title  TEXT";
    public static final String ORIGINAL_LANGUAGE="original_language  TEXT";
    public static final String TITLE="title  TEXT";
    public static final String BACKDROP_PATH="backdrop_path  TEXT";
    public static final String POPULARITY="popularity  TEXT";
    public static final String VOTE_COUNT="vote_count INTEGER";
    public static final String VIDEO="video BOOLEAN DEFAULT FALSE";
    public static final String VOTE_AVERAGE="vote_average TEXT";
    public static final String GENRE_IDS="genre_ids  TEXT";

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

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }
}
