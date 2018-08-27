package com.example.administrator.diaokes.tongzhi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.diaokes.R;

import java.util.List;

/**
 * Created by Administrator on 2018/7/30.
 */

public class tongzhiAdapter extends RecyclerView.Adapter<tongzhiAdapter.ViewHolder> {
    private List<tzitem> list;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView content;
        TextView time;
        public ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.tongzhi_name);
            content = view.findViewById(R.id.tongzhi_content);
            time = view.findViewById(R.id.tongzhi_time);
        }
    }

    public tongzhiAdapter(List<tzitem> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.tongzhi_item_layout,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        tzitem item = list.get(position);
        holder.name.setText(item.getName());
        holder.content.setText(item.getPath());
        holder.time.setText("上线时间："+item.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
