package udacity.lzy.movies.fragment;
// @author: lzy  time: 2016/10/19.


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import udacity.lzy.movies.Adapter.DetailAdapter;
import udacity.lzy.movies.Adapter.DividerItemDecoration;
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
    private StateChangeListener stateChange;
    private boolean first=true;

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
            v.setVisibility(View.INVISIBLE);
        }
        if (v.getParent() != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            parent.removeView(v);
        }
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState!=null)
            if (savedInstanceState.getSerializable("movie")!=null) {
                replaceHeader((MovieBean) savedInstanceState.get("movie"));
                show();
            }
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
        detailAdapter.setListener((position, view) -> {
            int type = detailAdapter.getItemViewType(position);
            if (type==2){
                Object item = detailAdapter.getItem(position);
                if (item!=null&&item instanceof VideoBean) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse("http://www.youtube.com/watch?v="+
                            ((VideoBean) item).getKey()));
                    startActivity(intent);
                }
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
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
        if (movie!=null&&movie.getId()!=0) {
            this.movie=movie;
            detailAdapter.replaceHeader(movie);
            first = true;
            apiHelper.getVideos(movie.getId());
            apiHelper.getReviews(movie.getId());
            apiHelper.getCollectMovies(movie.getId());
        }
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

    public void show(){
        if (!v.isShown())
            v.setVisibility(View.VISIBLE);
    }
    @Override
    public void clearData() {
        detailAdapter.clear();
    }

    private static Toast toast;
    @Override
    public void ShowToast(String msg) {
        if (toast==null)
            toast=Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT);
        else toast.setText(msg);
        toast.show();
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
        if (first){
            first=false;
        }
        isCollecting=false;
        collected = flag;
        detailAdapter.setSwitch(collected);
        if (stateChange!=null)
            stateChange.onStateChange(collected);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("movie",movie);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public MovieBean getMovie() {
        return movie;
    }
    public void setStateChangeListener(StateChangeListener stateChange) {
        this.stateChange = stateChange;
    }
}
