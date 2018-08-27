package com.example.administrator.diaokes.custom;

import android.content.Intent;
import android.os.Bundle;
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

public class Register extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText name;
    private EditText pwd;
    private EditText repwd;
    private Button submit;
    private EditText email;
    private Button person;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        toolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);

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
        toolbar.setTitle("注册账号");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        name = findViewById(R.id.register_name);
        pwd = findViewById(R.id.register_pwd);
        repwd = findViewById(R.id.register_confirm);
        email = findViewById(R.id.register_email);
        submit = findViewById(R.id.register_submit);
        person = findViewById(R.id.register_person);
        person.setVisibility(View.INVISIBLE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pwd.getText().toString().equals(repwd.getText().toString())){
                    Toast.makeText(Register.this,"密码不一致",Toast.LENGTH_LONG).show();
                    return;
                }
                if(name.getText().toString().equals("") || pwd.getText().toString().equals("") || email.getText().toString().equals("")){
                    Toast.makeText(Register.this,"输入信息不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("name",name.getText().toString())
                                    .add("pwd",pwd.getText().toString())
                                    .add("email",email.getText().toString())
                                    .build();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://192.168.1.103:8080/tomcats/register")
                                    .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            final String re = response.body().string();
                            Register.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Register.this,re+"请先填写完成个人信息",Toast.LENGTH_LONG).show();
                                    person.setVisibility(View.VISIBLE);
                                }
                            });
                        }catch (Exception e){
                            Log.e("error",e.getMessage());
                        }
                    }
                }).start();
            }
        });

        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this,personinit.class);
                intent.putExtra("name",name.getText().toString());
                startActivity(intent);
            }
        });

    }
}
