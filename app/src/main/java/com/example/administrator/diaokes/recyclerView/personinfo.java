package com.example.administrator.diaokes.recyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.diaokes.Bean.person;
import com.example.administrator.diaokes.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/24.
 */

public class personinfo extends RecyclerView.Adapter<personinfo.ViewHolder> {
    private List<person> list = new ArrayList<>();
    private Context context;
    private String strings ="1";

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView content;

        public ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.person_name);
            content = view.findViewById(R.id.person_content);
        }
    }
    public personinfo(List<person> list,Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        person person = list.get(position);
        holder.name.setText(person.getName());
        holder.content.setText(person.getContent());
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                final AlertDialog alertDialog;
                Context mContext = context;
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.person_dialog,null);
                builder = new AlertDialog.Builder(mContext);
                builder.setView(layout);
                alertDialog = builder.create();
                alertDialog.show();
                final Button submit = layout.findViewById(R.id.dialog_submit);
                final EditText content1 = layout.findViewById(R.id.dialog_edit);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.content.setText(content1.getText());
                        strings += content1.getText() + ",";
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public String getStrings(){
        return strings;
    }

}
