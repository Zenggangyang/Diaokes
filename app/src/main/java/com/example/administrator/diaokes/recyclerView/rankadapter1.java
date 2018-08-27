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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/7/25.
 */

public class rankadapter1 extends RecyclerView.Adapter<rankadapter1.ViewHolder> {
    private List<rankitem1> list;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        CircleImageView circleImageView;
        TextView num;
        ImageView imageView;

        public ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.rank_name1);
            num = view.findViewById(R.id.rank_num1);
            imageView = view.findViewById(R.id.rank_three);
            circleImageView = view.findViewById(R.id.rank_head2);
        }
    }

    public rankadapter1(Context context,List<rankitem1> list){
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.rank_item1_layout,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        rankitem1 item = list.get(position);
        holder.name.setText(item.getName());
        holder.num.setText(item.getNum()+"");
        Glide.with(context).load(item.getPath()).into(holder.imageView);
        Glide.with(context).load(item.getUrl()).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
