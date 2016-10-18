package udacity.lzy.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {


    String poster_path_text;
    String overview_text;
    String vote_average_text;
    String release_date_text;
    String title_text;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.release_date)
    TextView releaseDate;
    @Bind(R.id.vote_average)
    TextView voteAverage;
    @Bind(R.id.overview)
    TextView overview;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        fab.setVisibility(View.GONE);
//        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());
        Intent intent = getIntent();
        poster_path_text = intent.getStringExtra("poster_path");
        overview_text = intent.getStringExtra("overview");
        title_text = intent.getStringExtra("title");
        release_date_text = intent.getStringExtra("release_date");
        vote_average_text = intent.getStringExtra("vote_average");
        String baseUri = "http://image.tmdb.org/t/p/w185";
        if (!TextUtils.isEmpty(poster_path_text))
            Picasso.with(this).load(baseUri +poster_path_text).into(image);
        if (!TextUtils.isEmpty(overview_text))
            overview.setText(overview_text);
        if (!TextUtils.isEmpty(title_text)) {
            title.setText(title_text);
            toolbar.setTitle(title_text);
        }
        if (!TextUtils.isEmpty(release_date_text))
            releaseDate.setText(release_date_text);
        if (!TextUtils.isEmpty(vote_average_text))
            voteAverage.setText(vote_average_text);
        setSupportActionBar(toolbar);
    }
}
