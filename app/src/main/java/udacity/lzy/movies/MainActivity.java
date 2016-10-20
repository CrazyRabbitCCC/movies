package udacity.lzy.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import udacity.lzy.movies.Bean.MovieBean;
import udacity.lzy.movies.fragment.DetailFragment;
import udacity.lzy.movies.fragment.MovieFragment;

public class MainActivity extends AppCompatActivity {

    private boolean twoPanel = false;
    private MovieFragment movieFragment;
    private DetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        FragmentManager fm = getSupportFragmentManager();
        if (findViewById(R.id.detail)!=null){
            twoPanel=true;
            if (detailFragment==null)
                detailFragment=new DetailFragment();
            fm.beginTransaction().addToBackStack(MovieFragment.TAG)
                    .replace(R.id.detail,detailFragment)
                    .commit();
        }
        if (movieFragment==null)
            movieFragment=new MovieFragment();
        movieFragment.setTwoPanel(twoPanel);
        setListener();
        fm.beginTransaction().addToBackStack(MovieFragment.TAG)
                .replace(R.id.movies,movieFragment)
                .commit();
    }

    private void setListener() {
        movieFragment.setListener((position, view) -> {
            MovieBean map = movieFragment.getItem(position);
            if (twoPanel){
                detailFragment.clearData();
                detailFragment.replaceHeader(map);
            }else {
                Intent intent = new Intent(this, DetailActivity.class);

                if (map != null) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("movie",map);
                    intent.putExtras(bundle);
                }
                startActivity(intent);
            }
        });
        movieFragment.setLongListener((position, view) -> {
            MovieBean map = movieFragment.getItem(position);
            if (map != null) {
                movieFragment.ShowToast(map.getTitle());
                return true;
            }
            return false;
        });
    }
}
