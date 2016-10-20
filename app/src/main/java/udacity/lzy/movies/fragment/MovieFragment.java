package udacity.lzy.movies.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import udacity.lzy.movies.Adapter.DetailAdapter;
import udacity.lzy.movies.Adapter.MainAdapter;
import udacity.lzy.movies.Adapter.OnItemClickListener;
import udacity.lzy.movies.Adapter.OnItemLongClickListener;
import udacity.lzy.movies.ApiService.ApiHelper;
import udacity.lzy.movies.Bean.MovieBean;
import udacity.lzy.movies.R;
import udacity.lzy.movies.iMainListener;

// @author: lzy  time: 2016/10/19.


public class MovieFragment extends Fragment implements iMainListener {

    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.rv)
    RecyclerView rv;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;
    @Bind(R.id.progress)
    ProgressBar progress;
    private View v;
    public static final String TAG = "MovieFragment";

    private ApiHelper apiHelper;
    private DetailAdapter detailAdapter;
    private MainAdapter mainAdapter;
    private int type = 0;
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;
    private boolean twoPanel;
    private boolean isLoading = true;
    private int page = 1;
    private int totalPage = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_movie, container, false);
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


//        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        rv.setLayoutManager(manager);
        mainAdapter = new MainAdapter(getActivity());
        mainAdapter.setTwoPanel(twoPanel);
        mainAdapter.setListener(listener);
        mainAdapter.setLongListener(longListener);
        apiHelper = new ApiHelper(this);
        rv.setAdapter(mainAdapter);
        String[] spinnerStrings = new String[]{"最受欢迎", "评分最高","收藏"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerStrings);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position;
                refresh.setRefreshing(true);
                apiHelper.loadMovies(type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        refresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.GRAY);
        refresh.setOnRefreshListener(() -> {
            refresh.setRefreshing(true);
            apiHelper.loadMovies(type);
        });


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
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        width = (dm.widthPixels - 32) / 2;
    }

    @Override
    public void addData(Object movie) {
        if (movie instanceof MovieBean)
            mainAdapter.add((MovieBean) movie);
    }

    @Override
    public void clearData() {
        mainAdapter.clear();
    }

    private static Toast toast;

    @Override
    public void ShowToast(String msg) {
        if (toast == null)
            toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        else toast.setText(msg);

        toast.show();
    }

    @Override
    public void setRefresh(boolean flag) {
        refresh.setRefreshing(flag);
    }

    @Override
    public void loadMore(boolean flag) {
        if (flag) {
            if (isLoading)
                return;
            if (page < totalPage) {
                page++;
                isLoading = true;
                progress.setVisibility(View.VISIBLE);
                apiHelper.loadMovies(type,page);
            }else {
                if (type!=2)
                ShowToast("已经加载完所有电影");
            }
        } else {
            isLoading = false;
            progress.setVisibility(View.GONE);
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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(getActivity());
    }

    public void setTwoPanel(boolean flag) {
        this.twoPanel = flag;
    }

    public void setLongListener(OnItemLongClickListener listener) {
        this.longListener = listener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MovieBean getItem(int position) {
        return mainAdapter.getItem(position);
    }
}
