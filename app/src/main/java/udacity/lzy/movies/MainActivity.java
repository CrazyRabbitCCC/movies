package udacity.lzy.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import udacity.lzy.movies.Adapter.MainAdapter;
import udacity.lzy.movies.ApiService.ApiHelper;

public class MainActivity extends AppCompatActivity implements iMainListener {

    @Bind(R.id.rv)
    RecyclerView rv;
    @Bind(R.id.spinner)
    Spinner spinner;
    int type=0;
    private MainAdapter mainAdapter;
    private ApiHelper apiHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

//        LinearLayoutManager manager = new LinearLayoutManager(this);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rv.setLayoutManager(manager);
        mainAdapter = new MainAdapter(this);
        apiHelper = new ApiHelper(this);
        setListener();
        rv.setAdapter(mainAdapter);
        String[] spinnerStrings=new String[]{"最受欢迎","评分最高"};
        ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,spinnerStrings);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=position;
                apiHelper.loadData(type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        width = (dm.widthPixels - 32) / 2;
    }

    private void setListener() {
        mainAdapter.setListener((position, view) -> {
            Map<String, Object> map = mainAdapter.getItem(position);
            Intent intent=new Intent(this,DetailActivity.class);
            if (map.get("title") != null) {
                intent.putExtra("poster_path", map.get("poster_path").toString());
                intent.putExtra("overview",map.get("overview").toString());
                intent.putExtra("title",map.get("title").toString());
                intent.putExtra("release_date",map.get("release_date").toString());
                intent.putExtra("vote_average",map.get("vote_average").toString());
            }
            startActivity(intent);
        });
        mainAdapter.setLongListener((position, view) -> {
            Map<String, Object> map = mainAdapter.getItem(position);
            if (map.get("title") != null) {
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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
