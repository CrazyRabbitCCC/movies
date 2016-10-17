package udacity.lzy.movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import udacity.lzy.movies.Adapter.MainAdapter;
import udacity.lzy.movies.ApiService.ApiHelper;

public class MainActivity extends AppCompatActivity implements iMainListener {

    @Bind(R.id.rv)
    RecyclerView rv;
    private MainAdapter mainAdapter;
    private int width=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

//        LinearLayoutManager manager = new LinearLayoutManager(this);
        GridLayoutManager  manager=new GridLayoutManager(this,2);
        rv.setLayoutManager(manager);
        mainAdapter=new MainAdapter(this);
        setListener();
        rv.setAdapter(mainAdapter);
        new ApiHelper(this).getPopular(ApiHelper.API_KEy);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        width=(dm.widthPixels-32)/2;

    }

    private void setListener() {
        mainAdapter.setListener((position, view) -> {
            Map<String ,Object> map=mainAdapter.getItem(position);
            if (map.get("title")!=null)
                Toast.makeText(this,map.get("title").toString(),Toast.LENGTH_SHORT).show();
        });
        mainAdapter.setLongListener((position, view) -> {
            Map<String ,Object> map=mainAdapter.getItem(position);
            if (map.get("title")!=null) {
                Toast.makeText(this, map.get("title").toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }


    @Override
    public void addData(Map<String, Object> map) {
        mainAdapter.add(map);
    }

    @Override
    public void clearData() {
        mainAdapter.clear();
    }

    @Override
    public void ShowToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

}
