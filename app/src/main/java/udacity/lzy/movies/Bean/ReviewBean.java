package udacity.lzy.movies.Bean;
// @author: lzy  time: 2016/10/18.


public  class ReviewBean {
    public static final String TABLE_NAME="review_table";
    public static final String ID="review_id TEXT";
    public static final String AUTHOR="author TEXT";
    public static final String CONTENT="content TEXT";
    public static final String URL="url TEXT";
    public static final String MOVIE_ID="movie_id INTEGER";
    private String id;
    private String author;
    private String content;
    private String url;
    private int movie_id;


    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}