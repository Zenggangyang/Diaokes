package com.example.administrator.diaokes.custom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.diaokes.R;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/31.
 */

public class findPwd extends AppCompatActivity {
    private Toolbar toolbar;
    private Button submit;
    private EditText name;
    private EditText email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpwd_layout);

        toolbar = findViewById(R.id.find_pwd_toolbar);
        setSupportActionBar(toolbar);
        submit = findViewById(R.id.find_submit);
        name = findViewById(R.id.find_name);
        email = findViewById(R.id.find_email);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.haokan));

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            //去除默认显示标题
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("找回密码");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("name",name.getText().toString())
                                    .add("email",email.getText().toString())
                                    .build();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://192.168.1.103:8080/tomcats/queryPwd")
                                    .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            final String re = response.body().string().substring(0,4);
                            if(!re.equals("信息错误")) {
                                Intent data = new Intent(Intent.ACTION_SENDTO);
                                data.setData(Uri.parse("mailto:"+email.getText().toString()));
                                data.putExtra(Intent.EXTRA_SUBJECT, "找回密码");
                                data.putExtra(Intent.EXTRA_TEXT,"您的密码是："+re);
                                startActivity(data);
                            }else {
                                findPwd.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(findPwd.this,"用户名或邮箱不匹配",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }catch (Exception e){
                            Log.e("error",e.getMessage());
                        }
                    }
                }).start();
            }
        });

    }
}
