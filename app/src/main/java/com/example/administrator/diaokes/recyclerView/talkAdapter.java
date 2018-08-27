package com.example.administrator.diaokes.recyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.diaokes.R;
import com.example.administrator.diaokes.db.talkingMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/27.
 */

public class talkAdapter extends RecyclerView.Adapter<talkAdapter.ViewHolder> {
    private ArrayList<talkingMsg> list;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        TextView rname;
        TextView lname;

        public ViewHolder(View view){
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            rightLayout = view.findViewById(R.id.right_layout);
            leftMsg = view.findViewById(R.id.left_msg);
            rightMsg = view.findViewById(R.id.right_msg);
            rname = view.findViewById(R.id.rtalk_name);
            lname = view.findViewById(R.id.ltalk_name);
        }
    }

    public void setData(ArrayList<talkingMsg> data) {
        this.list = data;
        notifyDataSetChanged();
    }
    public talkAdapter(Context context){
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.talking_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        talkingMsg msg = list.get(position);
        if(msg.getType() == talkingMsg.TYPE_RECEIVE){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
            holder.lname.setText(msg.getName());
        }else if(msg.getType() == talkingMsg.TYPE_SEND){
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
            holder.rname.setText(msg.getName());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
