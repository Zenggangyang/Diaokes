package com.example.administrator.diaokes.DongTai;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.administrator.diaokes.R;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/7/12.
 */

public class Shipin extends AppCompatActivity{
    public static final int CHOOSE_VIDEO = 1;
    private VideoView videoView;
    private Toolbar toolbar;
    private ImageView imageadd;
    private TextView timeVideo;
    private RelativeLayout relativeLayout;
    private TextView biaoqian;
    private TextView weizhi;
    private boolean click = true;
    private boolean bqclick = true;
    private boolean ddclick = true;
    private CircleImageView weibo;
    private TextView sendVideo;
    private String path = null;
    private EditText content;
    private EditText name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipin_layout);
        videoView = findViewById(R.id.video_view);
        toolbar = findViewById(R.id.sptoolbar);
        imageadd = findViewById(R.id.chooseVideo);
        timeVideo = findViewById(R.id.timeVideo);
        relativeLayout = findViewById(R.id.xuxian1);
        biaoqian = findViewById(R.id.videobq);
        weizhi = findViewById(R.id.videodd);
        weibo = findViewById(R.id.weibovideo);
        sendVideo = findViewById(R.id.sendVideo);
        content = findViewById(R.id.videocontent);
        name =findViewById(R.id.videoname);

        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.floatcolor));

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            //去除默认显示标题
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("分享");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imageadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(Shipin.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Shipin.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    openVideo();
                }
            }
        });

        biaoqian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bqclick) {
                    biaoqian.setBackgroundResource(R.drawable.recban);
                    bqclick = false;
                }else {
                    biaoqian.setBackgroundResource(R.drawable.reccircle);
                    bqclick = true;
                }
            }
        });

        weizhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ddclick) {
                    weizhi.setBackgroundResource(R.drawable.recban);
                    ddclick = false;
                }else {
                    weizhi.setBackgroundResource(R.drawable.reccircle);
                    ddclick =true;
                }
            }
        });

        weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click == true) {
                    weibo.setImageResource(R.drawable.weibo2);
                    click = false;
                }else {
                    weibo.setImageResource(R.drawable.weibo);
                    click = true;
                }
            }
        });

        sendVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("video",path);
                intent.putExtra("content",content.getText().toString());
                intent.putExtra("name",name.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        final MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.start();
                videoView.setBackground(null);
            }
        };
        videoView.setVisibility(View.INVISIBLE);
    }

    private void openVideo(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("video/*");
        startActivityForResult(intent,CHOOSE_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHOOSE_VIDEO:
                if(resultCode == RESULT_OK){
                    videoView.setVisibility(View.VISIBLE);
                    relativeLayout.setBackground(null);
                    imageadd.setImageResource(R.drawable.spicon);
                    imageadd.setClickable(false);
                    int time = 0;
                    Cursor cursor1 = getContentResolver().query( data.getData(), new String[] { MediaStore.Video.Media.DISPLAY_NAME,MediaStore.Video.Media.SIZE,
                            MediaStore.Video.Media.DURATION,MediaStore.Video.Media.DATA},null,null,null);
                    if(cursor1.moveToFirst()){
                        path = cursor1.getString(cursor1.getColumnIndex(MediaStore.Video.Media.DATA));
                        time = cursor1.getInt((int) cursor1.getColumnIndex(MediaStore.Video.Media.DURATION));
                    }
                    cursor1.close();
                    displayVideo(path,time);
                }
        }
    }
    private Bitmap getVideoThumbnail(String path,int width,int height,int kind){
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(path, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openVideo();
                }else {
                    Toast.makeText(Shipin.this,"拒绝权限将无法使用应用程序",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    private void displayVideo(String path,int time){
        if(path != null){
            videoView.setVideoPath(path);
            Bitmap bitmap = getVideoThumbnail(path,1200,650, MediaStore.Video.Thumbnails.MINI_KIND);
            Drawable drawable = new BitmapDrawable(bitmap);
            videoView.setBackground(drawable);
            int time0 = time / 1000;
            if(time0 < 60) {
                if(time0<10){
                    String time1 = "00:0"+ time0;
                    timeVideo.setText(time1);
                }else {
                    String time1 = "00:" + time0;
                    timeVideo.setText(time1);
                }
            }else {
                int miao = time0 % 60;
                int min = (time0-miao)/60;
                if(min <10) {
                    if(miao <10) {
                        timeVideo.setText("0" + min + ":" + "0" + miao);
                    }else {
                        timeVideo.setText("0" + min + ":" + miao);
                    }
                }else{
                    if(miao < 10) {
                        timeVideo.setText(min + ":" + "0" + miao);
                    }else {
                        timeVideo.setText(min + ":" + miao);
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(videoView != null){
            videoView.suspend();
        }
    }
}
