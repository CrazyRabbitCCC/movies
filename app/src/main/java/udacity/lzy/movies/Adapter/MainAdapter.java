package udacity.lzy.movies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import udacity.lzy.movies.R;

// @author: lzy  time: 2016/10/17.


public class MainAdapter extends RecyclerView.Adapter<MyHolder> {
    private List<Map<String,Object>> list;

    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;
    private Context mContext;
    private int width=0;
    private final String BaseUri="http://image.tmdb.org/t/p/w185";

    public MainAdapter(Context context) {
        mContext=context;
        this.list = new ArrayList<>();
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        width = dm.widthPixels;
    }

    public MainAdapter(Context context,List<Map<String,Object>> list) {
        mContext=context;
        this.list = list;
    }

    public void add(Map<String,Object> map){
        list.add(map);
        notifyDataSetChanged();
    }
    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }
    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setLongListener(OnItemLongClickListener longListener) {
        this.longListener = longListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new MyHolder(item, listener, longListener);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        Map<String, Object> map = list.get(position);
        Picasso.with(mContext).load(BaseUri+map.get("poster_path").toString()).into(holder.image);

        ViewTreeObserver vto2 = holder.itemView.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                holder.itemView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                params.height=holder.itemView.getWidth()*40/27;
                holder.itemView.setLayoutParams(params);
            }
        });
        holder.text.setText(map.get("title").toString());
    }

    public Map<String, Object> getItem(int position){
        if (position<list.size())
            return list.get(position);
        return new HashMap<>();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
