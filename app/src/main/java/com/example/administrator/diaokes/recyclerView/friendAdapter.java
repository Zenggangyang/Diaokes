package com.example.administrator.diaokes.recyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.db.frienditem;

import java.util.List;

/**
 * Created by Administrator on 2018/8/8.
 */

public class friendAdapter extends RecyclerView.Adapter<friendAdapter.ViewHolder> {
    private Context context;
    private List<frienditem> list;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView rank;
        TextView content;
        ImageView imageView;

        public ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.friend_name);
            rank = view.findViewById(R.id.friend_rank);
            content = view.findViewById(R.id.friend_content);
            imageView = view.findViewById(R.id.friend_img);
        }
    }

    public friendAdapter(Context context,List<frienditem> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.friend_item_layout,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        frienditem item = list.get(position);

        holder.name.setText(item.getName());
        holder.content.setText(item.getContent());
        holder.rank.setText(item.getRank());
        Glide.with(context).load(item.getImg()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
