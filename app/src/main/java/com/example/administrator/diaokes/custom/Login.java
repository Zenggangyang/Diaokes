package com.example.administrator.diaokes.custom;

import android.animation.ObjectAnimator;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.diaokes.MainActivity;
import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.services.AutoUpdateService;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import org.litepal.util.LogUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/31.
 */

public class Login extends AppCompatActivity implements View.OnClickListener {
    public static Tencent mTencent;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private RippleView rvUsername;
    private RippleView rvEmail;
    private RippleView rvPassword;
    private StereoView stereoView;
    private LinearLayout lyWelcome;
    private TextView tvWelcome;
    private int translateY;
    private Toolbar toolbar;
    private ImageView qq;
    private ImageView weixin;
    private TextView pwd;
    private TextView register;
    private TextView login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        qq = findViewById(R.id.qq_login);
        weixin = findViewById(R.id.weixin_login);
        login = findViewById(R.id.youke_login);

        toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.haokan));

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            //去除默认显示标题
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("钓客江湖");

        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTencent = Tencent.createInstance("tencent100000", Login.this);

                if (mTencent == null) {
                    mTencent = Tencent.createInstance("tencent100000", Login.this);
                }
                if (!mTencent.isSessionValid())
                {
                    mTencent.login(Login.this, "all", new QQCallback(Login.this));
                }
            }
        });
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        inits();

        Intent intent = new Intent(this, AutoUpdateService.class);
        this.startService(intent);

        pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,findPwd.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Login.this,Register.class);
                startActivity(intent1);
            }
        });
        stereoView.setStartScreen(2);
        stereoView.post(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                stereoView.getLocationOnScreen(location);
                translateY = location[1];
            }
        });
        stereoView.setiStereoListener(new StereoView.IStereoListener() {
            @Override
            public void toPre(int curScreen) {
            }

            @Override
            public void toNext(int curScreen) {
            }
        });
    }

    private void inits(){
        pwd = findViewById(R.id.login_pwd);
        register = findViewById(R.id.login_register);
        stereoView = (StereoView) findViewById(R.id.stereoView);
        etUsername = (EditText) findViewById(R.id.et_username);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        rvUsername = (RippleView) findViewById(R.id.rv_username);
        rvEmail = (RippleView) findViewById(R.id.rv_email);
        rvPassword = (RippleView) findViewById(R.id.rv_password);
        lyWelcome = (LinearLayout) findViewById(R.id.ly_welcome);
        tvWelcome = (TextView) findViewById(R.id.tv_welcome);
        rvUsername.setOnClickListener(this);
        rvEmail.setOnClickListener(this);
        rvPassword.setOnClickListener(this);
        tvWelcome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rv_username:
                rvUsername.setiRippleAnimListener(new RippleView.IRippleAnimListener() {
                    @Override
                    public void onComplete(View view) {
                        stereoView.toPre();
                    }
                });
                break;
            case R.id.rv_email:
                rvEmail.setiRippleAnimListener(new RippleView.IRippleAnimListener() {
                    @Override
                    public void onComplete(View view) {
                        stereoView.toPre();
                    }
                });
                break;
            case R.id.rv_password:
                rvPassword.setiRippleAnimListener(new RippleView.IRippleAnimListener() {
                    @Override
                    public void onComplete(View view) {
                        stereoView.toPre();
                    }
                });
                break;
            case R.id.tv_welcome:
                if (TextUtils.isEmpty(etUsername.getText())) {
                    Toast.makeText(this,"请输入用户名",Toast.LENGTH_LONG).show();
                    stereoView.setItem(2);
                    return;
                }
                if (TextUtils.isEmpty(etEmail.getText())) {
                    Toast.makeText(this,"请输入邮箱",Toast.LENGTH_LONG).show();
                    stereoView.setItem(0);
                    return;
                }
                if (TextUtils.isEmpty(etPassword.getText())) {
                    Toast.makeText(this,"请输入密码",Toast.LENGTH_LONG).show();
                    stereoView.setItem(1);
                    return;
                }
                startExitAnim();
                break;
        }
    }

    private void startExitAnim() {
        //ObjectAnimator animator = ObjectAnimator.ofFloat(stereoView, "translationY", 0, 100, -translateY);
        //animator.setDuration(500).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    RequestBody requestBody = new FormBody.Builder()
                            .add("name",etUsername.getText().toString())
                            .add("pwd",etPassword.getText().toString())
                            .add("email",etEmail.getText().toString())
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://192.168.1.103:8080/tomcats/login")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    final String re = response.body().string().substring(0,4);
                    if(re.equals("登录成功")){
                        Login.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Login.this,"登录成功",Toast.LENGTH_LONG).show();
                            }
                        });
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String data1 = df.format(new Date()).toString();
                        RequestBody requestBody1 = new FormBody.Builder()
                                .add("name", "钓客管理员")
                                .add("content", "你的账号已上线")
                                .add("time", data1)
                                .build();
                        OkHttpClient client1 = new OkHttpClient();
                        Request request1 = new Request.Builder()
                                .url("http://192.168.1.103:8080/tomcats/insertnotify")
                                .post(requestBody1)
                                .build();
                        Response response1 = client1.newCall(request1).execute();
                        Intent intent = new Intent(getApplication(), MainActivity.class);
                        intent.putExtra("name",etUsername.getText().toString());
                        startActivity(intent);

                    }else {
                        Login.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Login.this,"用户名或密码不匹配",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }catch (Exception e){
                    Log.e("error",e.getMessage());
                }
            }
        }).start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11101) {
            Tencent.onActivityResultData(requestCode, resultCode, data, new QQCallback(Login.this));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
