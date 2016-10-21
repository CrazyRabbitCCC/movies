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
    private static  MovieFragment movieFragment=new MovieFragment();
    private static DetailFragment detailFragment=new DetailFragment();
    private static final int DETAIL = 0X9f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();
        if (findViewById(R.id.detail) != null) {//sw600
            twoPanel = true;
            if (detailFragment==null)
                detailFragment = new DetailFragment();
            fm.beginTransaction().addToBackStack(MovieFragment.TAG)
                    .replace(R.id.detail, detailFragment)
                    .commit();
            detailFragment.setStateChangeListener(state -> {
                if (movieFragment.getType() == 2)
                    movieFragment.loadData();
            });
        }
        if (movieFragment == null)
            movieFragment = new MovieFragment();
        movieFragment.setTwoPanel(twoPanel);
        setListener();
        fm.beginTransaction().addToBackStack(MovieFragment.TAG)
                .replace(R.id.movies, movieFragment)
                .commit();
    }

    private void setListener() {
        movieFragment.setListener((position, view) -> {
            MovieBean map = movieFragment.getItem(position);
            if (twoPanel) {
                detailFragment.show();
                detailFragment.clearData();
                detailFragment.replaceHeader(map);
            } else {
                Intent intent = new Intent(this, DetailActivity.class);

                if (map != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("movie", map);
                    intent.putExtras(bundle);
                }
                startActivityForResult(intent, DETAIL);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DETAIL) {
            if (resultCode == DetailActivity.STATE_CHANGED) {
                if (movieFragment.getType() == 2)
                    movieFragment.loadData();
            }
        }
    }
}
