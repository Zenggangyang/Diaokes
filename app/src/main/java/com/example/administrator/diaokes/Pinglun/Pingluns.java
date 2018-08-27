package com.example.administrator.diaokes.Pinglun;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.administrator.diaokes.Bean.CommentBean;
import com.example.administrator.diaokes.Bean.CommentDetailBean;
import com.example.administrator.diaokes.Bean.ReplyDetailBean;
import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.db.FoundDate;
import com.example.administrator.diaokes.recyclerView.CommentExpandableListView;
import com.example.administrator.diaokes.recyclerView.plAdapter;
import com.example.administrator.diaokes.recyclerView.recyclerItem;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/18.
 */

public class Pingluns extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView bt_comment;
    private CommentExpandableListView expandableListView;
    private plAdapter adapter;
    private CommentBean commentBean;
    private List<CommentDetailBean> commentsList;
    private BottomSheetDialog dialog;
    private TextView name;
    private TextView content;
    private String path;
    private String username;
    private String replyms;
    private int type;
    private ImageView imageView;
    private String dtid;
    private VideoView videoView;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout;
    private ImageView imageView1;
    private Drawable drawable;
    private Bitmap bitmap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinglun_layout);
        toolbar = findViewById(R.id.pltoolbar);
        setSupportActionBar(toolbar);


        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.haokan));

        name = findViewById(R.id.plname);
        content = findViewById(R.id.plcontent);
        imageView = findViewById(R.id.plimage1);
        videoView = findViewById(R.id.plvideo1);
        linearLayout = findViewById(R.id.plrongqi);
        relativeLayout = findViewById(R.id.plvideo);
        imageView1 = findViewById(R.id.plplay);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        content.setText(intent.getStringExtra("content"));
        username = intent.getStringExtra("name");
        path = intent.getStringExtra("path");
        type = intent.getIntExtra("type",0);
        replyms = intent.getStringExtra("reply11");
        replyms = replyms.substring(1,replyms.length()-3);
        dtid = intent.getStringExtra("pid");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            //去除默认显示标题
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("动态");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(type == 0){
            relativeLayout.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
        }else {
            relativeLayout.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.INVISIBLE);
            videoView.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.VISIBLE);
            videoView.setVideoPath(path);
            Bitmap bitmap = getVideoThumbnail(path,1200,650, MediaStore.Video.Thumbnails.MINI_KIND);
            drawable = new BitmapDrawable(bitmap);
            videoView.setBackground(drawable);
        }


        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!videoView.isPlaying()){
                    videoView.start();
                    imageView1.setVisibility(View.INVISIBLE);
                    videoView.setBackground(null);
                }
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                imageView1.setVisibility(View.VISIBLE);
                videoView.setBackground(drawable);
            }
        });
        if(ContextCompat.checkSelfPermission(Pingluns.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Pingluns.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            bitmap = getBitmap(path);
        }

        commentsList = generateTestData(replyms);
        initView();
    }


    private void initView() {
        expandableListView = (CommentExpandableListView) findViewById(R.id.detail_page_lv_comment);
        bt_comment = (TextView) findViewById(R.id.detail_page_do_comment);
        bt_comment.setOnClickListener(this);
        initExpandableListView(commentsList);
    }

    private Bitmap getVideoThumbnail(String path,int width,int height,int kind){
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(path, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    private void initExpandableListView(final List<CommentDetailBean> commentList) {
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new plAdapter(this, commentList);
        expandableListView.setAdapter(adapter);
        for (int i = 0; i < commentList.size(); i++) {
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
//                if(isExpanded){
//                    expandableListView.collapseGroup(groupPosition);
//                }else {
//                    expandableListView.expandGroup(groupPosition, true);
//                }
                showReplyDialog(groupPosition);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(Pingluns.this, "点击了回复", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //toast("展开第"+groupPosition+"个分组");
            }
        });
    }

    private List<CommentDetailBean> generateTestData(String data) {
        Gson gson = new Gson();
        commentBean = gson.fromJson(data, CommentBean.class);
        List<CommentDetailBean> commentList = commentBean.getList();
        return commentList;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.detail_page_do_comment) {
            showCommentDialog();
        }
    }

    private void showCommentDialog() {
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0, 0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String commentContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(commentContent)) {
                    //commentOnWork(commentContent);
                    dialog.dismiss();
                    CommentDetailBean detailBean = new CommentDetailBean(username, commentContent, "刚刚",bitmap);
                    adapter.addTheCommentData(detailBean);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("id",dtid+"")
                                        .add("name",username)
                                        .add("commentid",adapter.getGroupCount()-1+"")
                                        .add("content",commentContent)
                                        .add("time","1631")
                                        .add("zan","0")
                                        .build();
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("http://192.168.1.103:8080/tomcats/dtServlet")
                                        .post(requestBody)
                                        .build();
                                Response response = client.newCall(request).execute();
                                String re = response.body().string();
                            }catch (Exception e){
                                Log.e("错误信息",e.getMessage());
                            }
                        }
                    }).start();
                    Toast.makeText(Pingluns.this, "评论成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Pingluns.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 2) {
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }
    private void showReplyDialog(final int position) {
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(position).getName() + " 的评论:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String replyContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(replyContent)) {
                    dialog.dismiss();
                    ReplyDetailBean detailBean = new ReplyDetailBean(username, replyContent);
                    adapter.addTheReplyData(detailBean, position);
                    expandableListView.expandGroup(position);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("id",dtid+"")
                                        .add("commentid",adapter.getGroupCount()-1+"")
                                        .add("name",username)
                                        .add("content",replyContent)
                                        .build();
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("http://192.168.1.103:8080/tomcats/replyServlet")
                                        .post(requestBody)
                                        .build();
                                Response response = client.newCall(request).execute();
                                String re = response.body().string();
                            }catch (Exception e){
                                Log.e("错误信息",e.getMessage());
                            }
                        }
                    }).start();
                    Toast.makeText(Pingluns.this, "回复成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Pingluns.this, "回复内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 2) {
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    public Bitmap getBitmap(String path){
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getBitmap(path);
                }else {
                    Toast.makeText(Pingluns.this,"You denied the permission",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
