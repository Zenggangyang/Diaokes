package com.example.administrator.diaokes.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.Shop.shopDetail;

import java.util.List;

/**
 * Created by Administrator on 2018/7/28.
 */

public class shopitemAdapter extends RecyclerView.Adapter<shopitemAdapter.ViewHolder> {
    private List<shopitem> list;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name;
        TextView cost;
        CardView cardView;

        public ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.shop_item_img);
            name = view.findViewById(R.id.shop_item_name);
            cost = view.findViewById(R.id.shop_item_cost);
            cardView = view.findViewById(R.id.shop_item_cardview);
        }
    }

    public shopitemAdapter(List<shopitem> list,Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.shop_item_layout,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final shopitem item = list.get(position);
        holder.name.setText(item.getName());
        holder.cost.setText("$"+item.getCost());
        Glide.with(context).load(item.getPath()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,shopDetail.class);
                intent.putExtra("name",item.getName());
                intent.putExtra("cost",item.getCost()+"");
                intent.putExtra("path",item.getPath()+"");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
