package udacity.lzy.movies.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import udacity.lzy.movies.R;

// @author: lzy  time: 2016/10/17.


public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
    OnItemClickListener listener;
    OnItemLongClickListener longListener;
    @Bind(R.id.image)
    public ImageView image;
    @Bind(R.id.text)
    public TextView text;

    public MyHolder(View itemView,OnItemClickListener listener, OnItemLongClickListener longListener) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        itemView.getWidth();
        this.listener=listener;
        this.longListener=longListener;
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (listener!=null)
            listener.onItemClick(getPosition(),v);
    }

    @Override
    public boolean onLongClick(View v) {
        return longListener != null && longListener.onItemLongClick(getPosition(), v);
    }
}
