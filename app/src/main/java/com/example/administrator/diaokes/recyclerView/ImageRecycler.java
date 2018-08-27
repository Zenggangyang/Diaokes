package com.example.administrator.diaokes.recyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.diaokes.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/7/14.
 */

public class ImageRecycler extends RecyclerView.Adapter<ImageRecycler.ViewHolder> {
    private Context context;
    private List<imageItem> images;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        CircleImageView circleImageView;
        public ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.imageDT);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            circleImageView = view.findViewById(R.id.cancel);
        }
    }

    public ImageRecycler(Context context,List<imageItem> list){
        this.context = context;
        this.images = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.image_layout,parent,false);

        final ViewHolder holder = new ViewHolder(view);
        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if(position != 0) {
                    deleteImage(position);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position  == 0){
            holder.circleImageView.setVisibility(View.INVISIBLE);
        }
        imageItem imageView1 = images.get(position);
        Bitmap bitmap = BitmapFactory.decodeFile(imageView1.getImageView());
        holder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void addimage(int position,String path){
        images.add(position,new imageItem(path));
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    public void deleteImage(int position){
        images.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
