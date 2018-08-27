package com.example.administrator.diaokes.Add;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.diaokes.R;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/8/2.
 */

public class addaddress extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imageView;
    private TextView submit;
    private EditText name;
    private EditText locate;
    private EditText type;
    private EditText yu;
    private EditText detail;
    private EditText lianxi;
    private RadioGroup radioGroup;
    private String x =null;
    private String y = null;
    private String type1 = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address_layout);

        imageView = findViewById(R.id.choose_address1);
        toolbar = findViewById(R.id.add_address_toolbar);
        submit = findViewById(R.id.address_submit);
        name = findViewById(R.id.address_name);
        locate = findViewById(R.id.address_address);
        type = findViewById(R.id.address_cost_type);
        yu = findViewById(R.id.address_type_yu);
        detail = findViewById(R.id.address_detail);
        lianxi = findViewById(R.id.address_lianxi);
        radioGroup = findViewById(R.id.address_radio);

        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.haokan));
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("添加钓场");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addaddress.this,choose.class);
                startActivityForResult(intent,1);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                // 通过id实例化选中的这个RadioButton
                RadioButton choise = (RadioButton) findViewById(id);
                type1 = choise.getText().toString();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("name",name.getText().toString())
                                    .add("address",locate.getText().toString())
                                    .add("locate",x+","+y)
                                    .add("costtype",type.getText().toString())
                                    .add("type",type1)
                                    .add("yu",yu.getText().toString())
                                    .add("detail",detail.getText().toString())
                                    .add("lianxi",lianxi.getText().toString())
                                    .build();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://192.168.1.103:8080/tomcats/addaddress")
                                    .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            final String re = response.body().string();
                            addaddress.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(addaddress.this,re+"等待后台管理员审核",Toast.LENGTH_LONG).show();
                                }
                            });
                        }catch (Exception e){
                            Log.e("错误信息",e.getMessage());
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == -1){
                    x = data.getStringExtra("x");
                    y = data.getStringExtra("y");
                }
        }
    }
}
