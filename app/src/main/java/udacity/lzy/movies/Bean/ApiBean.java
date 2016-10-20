package udacity.lzy.movies.Bean;
// @author: lzy  time: 2016/10/17.


import java.util.List;

public class ApiBean {
    private int page;
    private int total_results;
    private int total_pages;
    private List<MovieBean> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<MovieBean> getResults() {
        return results;
    }

    public void setResults(List<MovieBean> results) {
        this.results = results;
    }


}
