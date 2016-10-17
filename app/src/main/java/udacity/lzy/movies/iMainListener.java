package udacity.lzy.movies;
// @author: lzy  time: 2016/10/17.


import java.util.Map;

public interface iMainListener {
    void addData(Map<String ,Object> map);
    void clearData();
    void ShowToast(String msg);
}
