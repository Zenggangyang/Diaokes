package com.example.administrator.diaokes.Fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Trace;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.diaokes.Pinglun.personInfo;
import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.custom.Login;
import com.example.administrator.diaokes.db.FoundDate;
import com.example.administrator.diaokes.db.PersonData;
import com.example.administrator.diaokes.recyclerView.PerAdapter;
import com.example.administrator.diaokes.recyclerView.PersonItem;
import com.example.administrator.diaokes.services.AutoUpdateService;
import com.example.administrator.diaokes.tongzhi.tongzhi;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/6/27.
 */

public class Personal extends Fragment {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private Button bianji;
    private TextView city;
    private TextView name;
    private TextView huoyue;
    private TextView rank;
    private TextView watch;
    private TextView fans;
    private TextView xingzuo;

    private PersonItem[] personItems = {new PersonItem("个性换肤",R.drawable.huanfu),new PersonItem("我的收藏",R.drawable.shou),new PersonItem("我的路线",R.drawable.route),
                                        new PersonItem("我的好友",R.drawable.person),new PersonItem("附近的人",R.drawable.fujin),new PersonItem("我的商城",R.drawable.shop),
                                        new PersonItem("我的排名",R.drawable.rank)};
    private List<PersonItem> perList = new ArrayList<>();
    private PerAdapter perAdapter;
    private CircleImageView head;
    String data = null;
    private static String username = null;
    String s = null;
    String ss = null;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    data = (String) msg.obj;
                    break;
                case 2:
                    s = (String) msg.obj;
                    break;
                case 3:
                    ss = (String) msg.obj;
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_layout, container, false);
        setHasOptionsMenu(true);
        appBarLayout = view.findViewById(R.id.appBar1);
        bianji = view.findViewById(R.id.perbianji);
        name = view.findViewById(R.id.person_headname);
        head = view.findViewById(R.id.touimage);
        city = view.findViewById(R.id.person_city);
        huoyue = view.findViewById(R.id.huoyue);
        fans = view.findViewById(R.id.fans);
        watch = view.findViewById(R.id.watch);
        xingzuo = view.findViewById(R.id.xingzuo);
        rank = view.findViewById(R.id.rank);

        final ImageView imageView = view.findViewById(R.id.background_image1);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String date = prefs.getString("img",null);
        Glide.with(this).load(date).into(imageView);

        toolbar = view.findViewById(R.id.toolbar1);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar1);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset <= -imageView.getHeight() / 2){
                    collapsingToolbarLayout.setTitle("我");
                    collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
                    Window window = getActivity().getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getActivity().getResources().getColor(R.color.floatcolor));
                }else {
                    collapsingToolbarLayout.setTitle("");
                    Window window = getActivity().getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getActivity().getResources().getColor(R.color.transparent));
                }
            }
        });
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        LitePal.initialize(getActivity());

        ruquestMSG();
        Request();
        RequestShopCar();
        inits();

        RecyclerView recyclerView = view.findViewById(R.id.person_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        perAdapter = new PerAdapter(perList,getActivity(),s,ss);
        recyclerView.setAdapter(perAdapter);

        bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), personInfo.class);
                intent.putExtra("name",data.substring(1,data.length()-1).split(",,")[0]);
                intent.putExtra("sex",data.substring(1,data.length()-1).split(",,")[1]);
                intent.putExtra("birth",data.substring(1,data.length()-1).split(",,")[2]);
                intent.putExtra("address",data.substring(1,data.length()-1).split(",,")[3]);
                intent.putExtra("person",data.substring(1,data.length()-1).split(",,")[6]);
                intent.putExtra("img",data.substring(1,data.length()-1).split(",,")[4]);
                startActivityForResult(intent,1);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.tongzhi1:
                Intent intent = new Intent(getActivity(),tongzhi.class);
                startActivity(intent);
                break;
            case R.id.action_settings:
                notifymessage();
                Intent intent1 = new Intent(getActivity(), Login.class);
                startActivity(intent1);

                break;
        }
        return true;
    }

    public void inits(){
        perList.clear();
        DataSupport.deleteAll(PersonData.class);
        for(int i = 0;i<personItems.length;i++){
            PersonData data = new PersonData(personItems[i].getPername(),personItems[i].getImageId());
            data.save();
        }
        List<PersonData> persons = DataSupport.findAll(PersonData.class);
        PersonItem[] personItems1 = new PersonItem[persons.size()];
        int i1 = persons.size() - 1;
        for (PersonData data : persons){
            PersonItem item = new PersonItem(data.getName(),data.getPath());
            personItems1[i1] = item;
            i1--;
        }
        for (int i = 0;i<personItems1.length;i++){
            perList.add(personItems1[i]);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == -1){
                    name.setText(data.getStringExtra("text"));
                    Bitmap bitmap = BitmapFactory.decodeFile(data.getStringExtra("path"));
                    head.setImageBitmap(bitmap);
                }
        }
    }

    public void Request(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://192.168.1.103:8080/tomcats/queryrank")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String data = response.body().string();
                    s = data.substring(1,data.length()-3);
                    Message message = new Message();
                    message.what = 2;
                    message.obj = s;
                    handler.sendMessage(message);
                }catch (Exception e){
                    Log.e("error",e.getMessage());
                }
            }
        }).start();

    }
    public void RequestShopCar(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://192.168.1.103:8080/tomcats/querycar")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String data = response.body().string();
                    ss = data.substring(1,data.length()-3);
                    Message message = new Message();
                    message.what = 3;
                    message.obj = ss;
                    handler.sendMessage(message);
                }catch (Exception e){
                    Log.e("error",e.getMessage());
                }
            }
        }).start();

    }


    public void ruquestMSG(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("name",username)
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://192.168.1.103:8080/tomcats/queryperson")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    data = response.body().string();
                    Message message = new Message();
                    message.what = 1;
                    message.obj = data;
                    handler.sendMessage(message);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            name.setText(data.substring(1,data.length()-1).split(",,")[0]);
                            city.setText(data.substring(1,data.length()-1).split(",,")[3]);
                            watch.setText(data.substring(1,data.length()-1).split(",,")[8]);
                            fans.setText(data.substring(1,data.length()-1).split(",,")[9]);
                            rank.setText(data.substring(1,data.length()-1).split(",,")[5]);
                            Glide.with(getActivity()).load(data.substring(1,data.length()-1).split(",,")[4]).into(head);
                        }
                    });
                }catch (Exception e){
                    Log.e("错误信息",e.getMessage());
                }
            }
        }).start();
    }

    private void notifymessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String data1 = df.format(new Date()).toString();
                    RequestBody requestBody1 = new FormBody.Builder()
                            .add("name", "钓客管理员")
                            .add("content", "你的账号已下线")
                            .add("time", data1)
                            .build();
                    OkHttpClient client1 = new OkHttpClient();
                    Request request1 = new Request.Builder()
                            .url("http://192.168.1.103:8080/tomcats/insertnotify")
                            .post(requestBody1)
                            .build();
                    Response response1 = client1.newCall(request1).execute();
                }catch (Exception e){
                    Log.e("错误信息",e.getMessage());
                }
            }
        }).start();

    }
    public static Fragment newInstance(String username1){
        Personal _personal = new Personal();
        username = username1;
        return _personal;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("PERSONAL","ONSTART");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("PERSONAL","ONRESUME");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("PERSONAL","ONPAUSE");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("PERSONAL","ONSTOP");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v("PERSONAL","ONDESTORY");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("PERSONAL","ONDESTORY");
    }
}


