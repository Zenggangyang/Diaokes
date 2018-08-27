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

public class personinit extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText sex;
    private EditText birth;
    private EditText address;
    private EditText person;
    private String name = null;
    private Button submit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_init_layout);

        toolbar = findViewById(R.id.person_init_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
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
        toolbar.setTitle("个人信息");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sex = findViewById(R.id.personinit_name);
        birth = findViewById(R.id.personinit_birth);
        address = findViewById(R.id.personinit_address);
        person = findViewById(R.id.personinit_qian);
        submit = findViewById(R.id.personinit_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sex.getText().toString().equals("") || birth.getText().toString().equals("") || address.getText().toString().equals("")){
                    Toast.makeText(personinit.this,"输入信息不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("name",name)
                                    .add("sex",sex.getText().toString())
                                    .add("birth",birth.getText().toString())
                                    .add("person",person.getText().toString())
                                    .add("address",address.getText().toString())
                                    .build();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://192.168.1.102:8080/tomcats/insertperson")
                                    .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            final String re = response.body().string().substring(0,4);
                            if(!re.equals("插入失败")){
                                personinit.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(personinit.this,"信息保存成功",Toast.LENGTH_LONG).show();
                                        finish();
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
