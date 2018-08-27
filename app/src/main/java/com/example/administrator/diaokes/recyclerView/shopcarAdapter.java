package com.example.administrator.diaokes.recyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.diaokes.R;

import java.util.List;

/**
 * Created by Administrator on 2018/7/28.
 */

public class shopcarAdapter extends RecyclerView.Adapter<shopcarAdapter.ViewHolder> {
    private List<shopitem> list;
    private Context context;
    private int allcost = 0;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name;
        TextView cost;
        Button add;
        Button jian;
        EditText num;
        CheckBox radioButton;
        public ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.car_img);
            name = view.findViewById(R.id.car_name);
            cost = view.findViewById(R.id.car_cost);
            add = view.findViewById(R.id.car_add);
            jian = view.findViewById(R.id.car_jian);
            num = view.findViewById(R.id.car_num);
            radioButton = view.findViewById(R.id.car_radio);
        }
    }

    public shopcarAdapter(List<shopitem> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.shop_car_item_layout,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final shopitem item = list.get(position);
        final int[] amount = {1};
        holder.name.setText(item.getName());
        holder.cost.setText("$"+item.getCost());
        Glide.with(context).load(item.getPath()).into(holder.imageView);
        holder.num.clearFocus();
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount[0]++;
                holder.num.setText(amount[0] +"");
            }
        });
        holder.jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amount[0] == 1){
                    holder.num.setText(amount[0] +"");
                }else {
                    amount[0]--;
                    holder.num.setText(amount[0] + "");
                }
            }
        });
        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(holder.radioButton.isChecked()){
                    allcost = allcost+amount[0]*item.getCost();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getAllcost(){
        return allcost;
    }
}
