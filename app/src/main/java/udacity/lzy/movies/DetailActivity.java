package udacity.lzy.movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import udacity.lzy.movies.Adapter.DetailAdapter;
import udacity.lzy.movies.Adapter.DividerItemDecoration;
import udacity.lzy.movies.ApiService.ApiHelper;
import udacity.lzy.movies.Bean.MovieBean;
import udacity.lzy.movies.Bean.ReviewBean;
import udacity.lzy.movies.Bean.VideoBean;

public class DetailActivity extends AppCompatActivity implements iMainListener {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.rv)
    RecyclerView rv;
    @Bind(R.id.image_top)
    ImageView imageTop;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

    private int page=1;
    private int totalPage=1;
    private ApiHelper apiHelper;
    private DetailAdapter detailAdapter;
    private boolean isLoading=true;
    private MovieBean movie;
    private boolean isCollecting=true;
    private boolean collected=false;
    public static final int STATE_CHANGED=0xF9;
    public static final int STATE_NOT_CHANGE=0xfa;
    private boolean first =true;
    private boolean state=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        apiHelper = new ApiHelper(this);
        fab.setImageResource(android.R.drawable.btn_star_big_off);
        fab.setOnClickListener(view -> {
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
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        movie = (MovieBean) bundle.getSerializable("movie");

        if (movie!=null&&!TextUtils.isEmpty(movie.getTitle())) {
            toolbarLayout.setTitle(movie.getTitle());
        }
        setSupportActionBar(toolbar);

        String baseUri = "http://image.tmdb.org/t/p/w500";
        if (!TextUtils.isEmpty(movie.getPoster_path()))
            Picasso.with(DetailActivity.this).load(baseUri + movie.getPoster_path()).into(imageTop);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
//
        detailAdapter = new DetailAdapter(this,movie);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
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

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public Context getActivity() {
        return this;
    }

    @Override
    public void setCollect(boolean flag) {
        if (first){
            first=false;
            state=flag;
        }
        isCollecting=false;
        collected=flag;
        if (collected)
            fab.setImageResource(android.R.drawable.btn_star_big_on);
        else
            fab.setImageResource(android.R.drawable.btn_star_big_off);

    }

    @Override
    public void loadMore(boolean flag) {
        if (flag){
            if (isLoading)
                return;
            if (page<totalPage) {
                page++;
                apiHelper.getReviews(movie.getId(), page);
            }
        }else {
            isLoading = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (state==collected)
            setResult(STATE_NOT_CHANGE);
        else
            setResult(STATE_CHANGED);
        super.onBackPressed();
    }
}
