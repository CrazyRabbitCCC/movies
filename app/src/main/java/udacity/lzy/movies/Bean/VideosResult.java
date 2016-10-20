package udacity.lzy.movies.Bean;
// @author: lzy  time: 2016/10/18.


import java.util.List;

public class VideosResult {

    private int id;
    private List<VideoBean> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<VideoBean> getResults() {
        return results;
    }

    public void setResults(List<VideoBean> results) {
        this.results = results;
    }
}
