package com.example.administrator.diaokes.OkHttp;

/**
 * Created by Administrator on 2018/7/21.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
