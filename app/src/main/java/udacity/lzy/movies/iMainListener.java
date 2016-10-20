package udacity.lzy.movies;
// @author: lzy  time: 2016/10/17.


import android.content.Context;

public interface iMainListener {
    void addData(Object map);
    void clearData();
    void ShowToast(String msg);
    void setRefresh(boolean flag);
    void loadMore(boolean flag);
    void setTotalPage(int totalPage);
    void setPage(int page);
    Context getActivity();
    void setCollect(boolean flag);
}
