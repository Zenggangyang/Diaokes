package com.example.administrator.diaokes.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.diaokes.Friends.friend;
import com.example.administrator.diaokes.Fujin.fujin;
import com.example.administrator.diaokes.Navigation.diaryroutes;
import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.Rank.rank;
import com.example.administrator.diaokes.Shop.shop;

import java.util.List;

/**
 * Created by Administrator on 2018/7/4.
 */

public class PerAdapter extends RecyclerView.Adapter<PerAdapter.ViewHolder> {
    private Context context;
    private List<PersonItem> list;
    private String s = null;
    private String ss = null;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.perimg);
            textView = view.findViewById(R.id.pertxt);
        }
    }

    public PerAdapter(List<PersonItem> list1,Context context,String s,String ss){
        list = list1;
        this.context = context;
        this.s = s;
        this.ss = ss;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.person_recycler,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        PersonItem personItem = list.get(position);
        holder.textView.setText(personItem.getPername());
        Glide.with(context).load(personItem.getImageId()).into(holder.imageView);

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(context, rank.class);
                            intent.putExtra("data",s);
                            context.startActivity(intent);
                        }
                    }).start();

                }else if(position == 2){
                    Intent intent1 = new Intent(context, fujin.class);
                    context.startActivity(intent1);
                }else if(position == 1){
                    Intent intent = new Intent(context,shop.class);
                    intent.putExtra("data",ss);
                    context.startActivity(intent);
                }else if (position == 4){
                    Intent intent = new Intent(context,diaryroutes.class);
                    context.startActivity(intent);
                }else if(position == 3){
                    Intent intent = new Intent(context,friend.class);
                    context.startActivity(intent);
                }
                //Toast.makeText(context,"你点击了",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
