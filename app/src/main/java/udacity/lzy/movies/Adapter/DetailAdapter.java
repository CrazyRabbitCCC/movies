package udacity.lzy.movies.Adapter;
// @author: lzy  time: 2016/10/19.


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import udacity.lzy.movies.Bean.MovieBean;
import udacity.lzy.movies.Bean.ReviewBean;
import udacity.lzy.movies.Bean.VideoBean;
import udacity.lzy.movies.R;

public class DetailAdapter extends RecyclerView.Adapter<DetailHolder> {
    private List<VideoBean> videos;
    private List<ReviewBean> reviews;


    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;
    private Context mContext;
    private MovieBean detail;
    private String baseUri = "http://image.tmdb.org/t/p/w500";
    private boolean twoPanel = false;
    private boolean isLoading=true;
    private View.OnClickListener onClickListener;
    private boolean switch_btn =true;

    public DetailAdapter(Context context, MovieBean movieDetail) {
        videos = new ArrayList<>();
        reviews = new ArrayList<>();
        mContext = context;
        this.detail = movieDetail;
    }

    public void add(VideoBean video) {
        videos.add(video);
        notifyDataSetChanged();
    }

    public void add(ReviewBean review) {
        reviews.add(review);
        notifyDataSetChanged();
    }

    public void clear() {
        videos.clear();
        reviews.clear();
    }

    public void setTwoPanel(boolean flag) {
        twoPanel = flag;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyItemChanged(getItemCount()-1);
    }

    public void replaceHeader(MovieBean detail) {
        this.detail = detail;
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setLongListener(OnItemLongClickListener longListener) {
        this.longListener = longListener;
    }

    public void setSwitch(boolean flag){
        switch_btn=flag;
        notifyItemChanged(0);
    }

    @Override
    public DetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item, parent, false);
        if (viewType == 1) {
            View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_header, parent, false);
            return new DetailHeader(headerView, listener, longListener);
        }
        if (viewType == 4) {
            View footView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_foot, parent, false);
            return new DetailFoot(footView, listener, longListener);
        }
        return new DetailHolder(itemView, listener, longListener);
    }

    @Override
    public void onBindViewHolder(DetailHolder holder, int position) {
        if (position == 0) {
            if (holder instanceof DetailHeader) {
                DetailHeader header = (DetailHeader) holder;
                if (twoPanel) {
                    header.collect_row.setVisibility(View.VISIBLE);
                    if (onClickListener!=null)
                    header.collect.setOnClickListener(onClickListener);
                    if (switch_btn) {
                        header.collect.setImageResource(android.R.drawable.btn_star_big_on);
                        header.collect_text.setText(R.string.collected);
                    }
                    else {
                        header.collect.setImageResource(android.R.drawable.btn_star_big_off);
                        header.collect_text.setText(R.string.not_collected);
                    }
                }
                if (twoPanel && !TextUtils.isEmpty(detail.getPoster_path())) {
                    header.image.setVisibility(View.VISIBLE);
                    Picasso.with(mContext).load(baseUri + detail.getPoster_path()).into(header.image);
                } else
                    header.image.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(detail.getOverview()))
                    header.overview.setText(detail.getOverview());
                if (!TextUtils.isEmpty(detail.getRelease_date()))
                    header.releaseDate.setText(detail.getRelease_date());
                if (0 != (detail.getVote_average()))
                    header.voteAverage.setText(String.format("%s", detail.getVote_average()));
                if (!TextUtils.isEmpty(detail.getTitle())) {
                    header.title.setText(detail.getTitle());
                }
            }
        } else if (position - 1 < videos.size()) {
            holder.title.setText(videos.get(position - 1).getName());
            holder.message.setText(videos.get(position - 1).getSite());
        } else if (position - 1 < videos.size() + reviews.size()) {
            holder.title.setText(reviews.get(position - videos.size() - 1).getAuthor());
            holder.message.setText(reviews.get(position - videos.size() - 1).getContent());
        }else {
            if (holder instanceof DetailFoot){
                DetailFoot foot = (DetailFoot) holder;
                if (isLoading) {
                    foot.progress.setVisibility(View.VISIBLE);
                    foot.title.setVisibility(View.VISIBLE);
                    foot.title.setText(R.string.loading_review);
                }
                else {
                    foot.progress.setVisibility(View.GONE);
                    if (reviews.size()==0){
                        foot.title.setVisibility(View.VISIBLE);
                        foot.title.setText(R.string.no_review);
                    }
                    else
                        foot.title.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return videos.size() + reviews.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position - 1 < 0)
            return 1;
        else if (position - 1 < videos.size()) {
            return 2;
        } else if (position - 1 < videos.size() + reviews.size())
            return 3;
        else return 4;
    }



}
class DetailFoot extends DetailHolder {
    @Bind(R.id.progress)
    ProgressBar progress;
    public DetailFoot(View itemView, OnItemClickListener listener, OnItemLongClickListener longListener) {
        super(itemView, listener, longListener);
    }

    @Override
    protected void initHolder() {
        ButterKnife.bind(this, itemView);
    }
}

class DetailHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.title)
    public TextView title;
    @Bind(R.id.message)
    public TextView message;

    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    public DetailHolder(View itemView, OnItemClickListener listener, OnItemLongClickListener longListener) {
        super(itemView);
        this.listener = listener;
        this.longListener = longListener;
        initHolder();
        itemView.setOnClickListener(v -> {
            if (this.listener != null)
                this.listener.onItemClick(getPosition(), v);
        });
        itemView.setOnLongClickListener(v -> this.longListener != null && this.longListener.onItemLongClick(getPosition(), v));
    }

    protected void initHolder() {
        ButterKnife.bind(this, itemView);
    }
}

class DetailHeader extends DetailHolder {

    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.release_date)
    TextView releaseDate;
    @Bind(R.id.vote_average)
    TextView voteAverage;
    @Bind(R.id.overview)
    TextView overview;
    @Bind(R.id.collect)
    ImageView collect;
    @Bind(R.id.collect_text)
    TextView collect_text;
    @Bind(R.id.collect_row)
    TableRow collect_row;

    public DetailHeader(View itemView, OnItemClickListener listener, OnItemLongClickListener longListener) {
        super(itemView, listener, longListener);
    }

    @Override
    protected void initHolder() {
        ButterKnife.bind(this, itemView);
    }
}
