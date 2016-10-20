package udacity.lzy.movies.Bean;
// @author: lzy  time: 2016/10/18.


import java.util.List;

public class ReviewsResult {



    private int id;
    private int page;
    private int total_pages;
    private int total_results;
    private List<ReviewBean> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public List<ReviewBean> getResults() {
        return results;
    }

    public void setResults(List<ReviewBean> results) {
        this.results = results;
    }
}
