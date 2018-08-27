package com.example.administrator.diaokes.custom;

/**
 * Created by Administrator on 2018/8/26.
 */

public class QQLoginInfo {
    String ret;
    String pay_token;
    String pf;
    String expires_in;
    String openid;
    String pfkey;
    String msg;
    String access_token;

    public String getAccess_token() {
        return access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public String getMsg() {
        return msg;
    }

    public String getOpenid() {
        return openid;
    }

    public String getPay_token() {
        return pay_token;
    }

    public String getPf() {
        return pf;
    }

    public String getPfkey() {
        return pfkey;
    }

    public String getRet() {
        return ret;
    }
}
