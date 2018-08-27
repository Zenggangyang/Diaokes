package com.example.administrator.diaokes.Fujin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.db.talkingMsg;
import com.example.administrator.diaokes.recyclerView.talkAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/27.
 */

public class talking extends AppCompatActivity {
    private ArrayList<talkingMsg> list;
    private EditText input;
    private Button send;
    private RecyclerView recyclerView;
    private talkAdapter adapter;
    private Toolbar toolbar;
    private String name = null;
    private Socket socket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talking_layout);

        toolbar = findViewById(R.id.talktoolbar);
        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.floatcolor));
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        toolbar.setTitle(name);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        input = findViewById(R.id.talk_content);
        send = findViewById(R.id.talk_send);
        recyclerView = findViewById(R.id.talk_recycler);

        list = new ArrayList<>();
        adapter = new talkAdapter(this);

        final Handler handler = new MyHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket = new Socket("192.168.1.103", 10010);
                    // 接收
                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        String s = new String(buffer, 0, len);
                        //
                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = s;
                        handler.sendMessage(message);
                    }

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();

        /*
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new talkAdapter(list);
        recyclerView.setAdapter(adapter);
*/
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String content = input.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            OutputStream outputStream = socket.getOutputStream();
                            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");    //设置日期格式
                            outputStream.write((socket.getLocalPort() + "//" + content + "//" + df.format(new Date())).getBytes("utf-8"));
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                input.setText("");
                /*
                if(!"".equals(content)){
                    talkingMsg msg = new talkingMsg(content,talkingMsg.TYPE_SEND,":张三");
                    list.add(msg);
                    adapter.notifyItemInserted(list.size()-1);
                    recyclerView.scrollToPosition(list.size()-1);
                    input.setText("");
                }
                */
            }
        });
    }
    /*
    private void inits(){
        talkingMsg msg = new talkingMsg("你好啊",talkingMsg.TYPE_RECEIVE,name+":");
        list.add(msg);
        talkingMsg msg1 = new talkingMsg("你好，请问你是？",talkingMsg.TYPE_SEND,":张三");
        list.add(msg1);
        talkingMsg msg2 = new talkingMsg("我是XX，很高兴认识你",talkingMsg.TYPE_RECEIVE,name+":");
        list.add(msg2);
    }*/

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //
                int localPort = socket.getLocalPort();
                String[] split = ((String) msg.obj).split("//");
                if (split[0].equals(localPort + "")) {
                    talkingMsg bean = new talkingMsg(split[1], 1, split[2], "我：");
                    list.add(bean);
                } else {
                    talkingMsg bean = new talkingMsg(split[1], 0, split[2],"来自"+split[0]);
                    list.add(bean);
                }
                adapter.setData(list);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager manager = new LinearLayoutManager(talking.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(manager);
                recyclerView.scrollToPosition(list.size()-1);
            }
        }
    }
}
