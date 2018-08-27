package com.example.administrator.diaokes.custom;

import android.app.Activity;

import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import javax.xml.transform.sax.TemplatesHandler;

/**
 * Created by Administrator on 2018/8/26.
 */

public class QQCallback implements IUiListener {
    private Activity activity;
    public QQCallback(Activity activity){
        this.activity = activity;
    }
    @Override
    public void onComplete(Object o) {
        String result = o.toString();
        QQLoginInfo qqLoginInfo = new Gson().fromJson(result,QQLoginInfo.class);
        Login.mTencent.setOpenId(qqLoginInfo.getOpenid());
        Login.mTencent.setAccessToken(qqLoginInfo.getAccess_token(), qqLoginInfo.getExpires_in());

        UserInfo userInfo = new UserInfo(activity, Login.mTencent.getQQToken());
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                QQUserInfo qqUserInfo = new Gson().fromJson(o.toString(),QQUserInfo.class);
                String nickname = qqUserInfo.getNickname();
                String avatar = qqUserInfo.getAvatar();
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }
}
