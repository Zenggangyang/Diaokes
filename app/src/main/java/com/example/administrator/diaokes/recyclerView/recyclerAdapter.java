package com.example.administrator.diaokes.recyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.administrator.diaokes.DongTai.Shipin;
import com.example.administrator.diaokes.Fragment.Found;
import com.example.administrator.diaokes.Pinglun.Pingluns;
import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.db.FoundDate;

import org.litepal.crud.DataSupport;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/6/29.
 */

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.ViewHolder> {
    private Context context;
    private List<recyclerItem> recyclerItemList;
    private Drawable drawable = null;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView;
        TextView textView;
        TextView content;
        VideoView videoView;
        RelativeLayout relativeLayout;
        LinearLayout linearLayout;
        ImageView play;
        TextView foundType;
        ImageView zan;
        ImageView pinlun;
        ImageView fenx;
        TextView zan1;
        Button gz;
        TextView time;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            imageView = view.findViewById(R.id.pimage1);
            textView = view.findViewById(R.id.pname);
            content = view.findViewById(R.id.wcontent);
            videoView = view.findViewById(R.id.pvideo1);
            relativeLayout = view.findViewById(R.id.floatvideo);
            linearLayout = view.findViewById(R.id.rongqi);
            play = view.findViewById(R.id.foundplay);
            foundType = view.findViewById(R.id.foundtype);
            zan = view.findViewById(R.id.foundzan);
            pinlun = view.findViewById(R.id.foundpinlun);
            fenx = view.findViewById(R.id.foundfenx);
            zan1 = view.findViewById(R.id.foundzan1);
            gz = view.findViewById(R.id.foundgz);
            time = view.findViewById(R.id.foundtime);
        }
    }

    public recyclerAdapter(Context context,List<recyclerItem> list){
        this.context = context;
        recyclerItemList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_layout,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        recyclerItem recyclerItem = recyclerItemList.get(position);
        holder.textView.setText(recyclerItem.getName());
        holder.content.setText(recyclerItem.getContent());
        holder.time.setText(recyclerItem.getTime());

        if(recyclerItem.getType() == 0) { //type为0,为图片
            holder.relativeLayout.setVisibility(View.INVISIBLE);
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.foundType.setText("发送图片");
            if(recyclerItem.getBitmap() != null){
                holder.imageView.setImageBitmap(recyclerItem.getBitmap());
            }else {
                Glide.with(context).load(recyclerItem.getImageId()).into(holder.imageView);
            }
        }else { //type为1,为视频
            holder.relativeLayout.setVisibility(View.VISIBLE);
            holder.linearLayout.setVisibility(View.INVISIBLE);
            holder.videoView.setVisibility(View.VISIBLE);
            holder.foundType.setText("发送视频");
            holder.play.setVisibility(View.VISIBLE);
            holder.videoView.setVideoPath(recyclerItem.getPath());
            drawable = new BitmapDrawable(recyclerItem.getBitmap());
            holder.videoView.setBackground(drawable);
        }

        List<FoundDate> list = DataSupport.findAll(FoundDate.class);
        if(list.get(list.size()- position -1).getZan()>0){
            holder.zan1.setText(list.get(list.size()-position-1).getZan()+" ");
            holder.zan.setImageResource(R.drawable.zanred);
        }

        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!holder.videoView.isPlaying()) {
                    holder.videoView.start();
                    holder.play.setVisibility(View.INVISIBLE);
                    holder.videoView.setBackground(null);
                }
            }
        });

        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                holder.play.setVisibility(View.VISIBLE);
                holder.videoView.setBackground(drawable);
            }
        });
        holder.pinlun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int position = holder.getAdapterPosition();
                            List<FoundDate> list = DataSupport.findAll(FoundDate.class);
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("id",list.get(list.size()-position-1).getId()+"")
                                    .build();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://192.168.1.103:8080/tomcats/myServlet")
                                    .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String re = response.body().string();
                            Intent intent = new Intent(context,Pingluns.class);
                            intent.putExtra("reply11",re);
                            intent.putExtra("pid",list.get(list.size()-position-1).getId());
                            intent.putExtra("name",list.get(list.size()-position-1).getName());
                            intent.putExtra("content",list.get(list.size()-position-1).getContent());
                            intent.putExtra("path",list.get(list.size()-position-1).getPath());
                            intent.putExtra("type",list.get(list.size()-position-1).getType());
                            context.startActivity(intent);
                        }catch (Exception e){
                            Log.e("错误信息",e.getMessage());
                        }
                    }
                };
                new Thread(runnable).start();

            }
        });
        holder.zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                List<FoundDate> list = DataSupport.findAll(FoundDate.class);
                int i = list.get(list.size()-position-1).getZan() + 1;
                if(list.get(list.size()-position-1).getZan()>0) {
                    list.get(list.size()-position-1).setZan(i);
                    holder.zan1.setText(i+" ");
                    holder.zan.setImageResource(R.drawable.zanred);
                }else {
                    list.get(list.size()-position-1).setZan(1);
                    holder.zan1.setText("1");
                    holder.zan.setImageResource(R.drawable.zanred);
                }
                list.get(list.size()-position-1).save();
            }
        });


        holder.fenx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                AlertDialog alertDialog;
                Context mContext = context;
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.fenxdia_layout,null);
                builder = new AlertDialog.Builder(mContext);
                builder.setView(layout);
                alertDialog = builder.create();
                alertDialog.show();
                ImageView weibo = layout.findViewById(R.id.fenxwb);
                ImageView douban = layout.findViewById(R.id.fenxdb);
                ImageView pengyou = layout.findViewById(R.id.fenxpy);
                weibo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context,"成功分享到微博",Toast.LENGTH_LONG).show();
                    }
                });
                douban.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context,"成功分享到豆瓣",Toast.LENGTH_LONG).show();
                    }
                });
                pengyou.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context,"成功分享到朋友圈",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        holder.gz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.gz.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recyclerItemList.size();
    }

    public void addImageItem(int position,String name,String content,int type,Bitmap bitmap){
        recyclerItemList.add(position,new recyclerItem(name,0,content,type,bitmap,null,"最近"));
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    public void addVideoItem(int position,String name,String content,int type,Bitmap bitmap,String path){
        recyclerItemList.add(position,new recyclerItem(name,0,content,type,bitmap,path,"最近"));
        notifyItemInserted(position);
        notifyDataSetChanged();
    }
}
