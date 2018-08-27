package com.example.administrator.diaokes.recyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.diaokes.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/7/25.
 */

public class rankadapter extends RecyclerView.Adapter<rankadapter.ViewHolder> {
    private Context context;
    private List<rankitem> list;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rank;
        CircleImageView circleImageView;
        TextView name;
        TextView num;
        public ViewHolder(View view){
            super(view);
            rank = view.findViewById(R.id.rank_item3);
            circleImageView = view.findViewById(R.id.rank_itemh);
            name = view.findViewById(R.id.rank_name);
            num = view.findViewById(R.id.rank_num);
        }
    }
    public rankadapter(Context context,List<rankitem> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.rank_item_layout,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        rankitem item = list.get(position);

        holder.name.setText(item.getName());
        holder.rank.setText(position+4+"");
        holder.num.setText(item.getNum()+"");
        Glide.with(context).load(item.getUrl()).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
