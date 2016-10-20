package udacity.lzy.movies.fragment;
// @author: lzy  time: 2016/10/19.


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import udacity.lzy.movies.Adapter.DetailAdapter;
import udacity.lzy.movies.ApiService.ApiHelper;
import udacity.lzy.movies.Bean.MovieBean;
import udacity.lzy.movies.Bean.ReviewBean;
import udacity.lzy.movies.Bean.VideoBean;
import udacity.lzy.movies.R;
import udacity.lzy.movies.iMainListener;

public class DetailFragment extends Fragment implements iMainListener {

    @Bind(R.id.rv)
    RecyclerView rv;

    private View v;

    private ApiHelper apiHelper;
    private DetailAdapter detailAdapter;
    public static final String TAG="DetailFragment";
    private boolean isLoading=true;
    private int page=1;
    private int totalPage=1;
    private MovieBean movie;
    private boolean isCollecting=true;
    private boolean collected=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_detail, container, false);
            initView();
        }
        if (v.getParent() != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            parent.removeView(v);
        }
        return v;
    }

    private void initView() {
        ButterKnife.bind(this, v);
        apiHelper=new ApiHelper(this);
        detailAdapter = new DetailAdapter(getActivity(),new MovieBean());
        detailAdapter.setTwoPanel(true);
        detailAdapter.setOnClickListener(view->{
            if (isCollecting){
                if (collected)
                    Snackbar.make(view, "正在取消收藏", Snackbar.LENGTH_LONG).show();
                else
                    Snackbar.make(view, "正在收藏", Snackbar.LENGTH_LONG).show();
                return;
            }
            isCollecting=true;
            if (collected)
                apiHelper.deleteCollectMovies(movie.getId());
            else
                apiHelper.insertCollectMovies(movie);
        });
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);

        rv.setAdapter(detailAdapter);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = manager.getItemCount();
                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItemPosition + 2)) {
                    //此时是刷新状态
                    loadMore(true);
                    isLoading = true;
                }
            }
        });
    }

    public void replaceHeader(MovieBean movie){
        this.movie=movie;
        detailAdapter.replaceHeader(movie);
        if (movie.getId()!=0)
        apiHelper.getVideos(movie.getId());
        apiHelper.getReviews(movie.getId());
        apiHelper.getCollectMovies(movie.getId());
    }

    @Override
    public void addData(Object map) {
        if (map instanceof VideoBean) {
            detailAdapter.add((VideoBean) map);
        }
        if (map instanceof ReviewBean) {
            detailAdapter.add((ReviewBean) map);
        }
    }

    @Override
    public void clearData() {
        detailAdapter.clear();
    }

    @Override
    public void ShowToast(String msg) {

    }

    @Override
    public void setRefresh(boolean flag) {

    }

    @Override
    public void loadMore(boolean flag) {
        if (flag){
            if (isLoading)
                return;
            if (page<totalPage) {
                page++;
                detailAdapter.setLoading(true);
                apiHelper.getReviews(movie.getId(), page);
            }
        }else {
            isLoading = false;
            detailAdapter.setLoading(false);
        }
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public void setCollect(boolean flag) {
        isCollecting=false;
        collected = flag;
        detailAdapter.setSwitch(collected);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
