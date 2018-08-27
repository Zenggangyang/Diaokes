package com.example.administrator.diaokes.recyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.diaokes.Bean.CommentDetailBean;
import com.example.administrator.diaokes.Bean.ReplyDetailBean;
import com.example.administrator.diaokes.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/7/19.
 */

public class plAdapter extends BaseExpandableListAdapter {
    private List<CommentDetailBean> commentDetails;
    private Context context;

    public plAdapter(Context context,List<CommentDetailBean> list){
        this.context = context;
        this.commentDetails = list;
    }

    @Override
    public int getGroupCount() {
        return commentDetails.size();
    }

    @Override
    public int getChildrenCount(int i) {
        if(commentDetails.get(i).getList() == null){
            return 0;
        }else {
            return commentDetails.get(i).getList().size()>0?commentDetails.get(i).getList().size():0;
        }
    }

    @Override
    public Object getGroup(int i) {
        return commentDetails.get(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public Object getChild(int i, int i1) {
        return commentDetails.get(i).getList().get(i1);
    }

    @Override
    public long getChildId(int i, int i1) {
        return getCombinedChildId(i,i1);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    boolean isLike = false;
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        final GroupHolder groupHolder;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.comment_item_layout,viewGroup,false);
            groupHolder = new GroupHolder(view);
            view.setTag(groupHolder);
        }else {
            groupHolder = (GroupHolder)view.getTag();
        }
        Glide.with(context).load(commentDetails.get(i).getImgpath())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .into(groupHolder.logo);
        /*
        if(commentDetails.get(i).getBitmap() == null) {

        }else {
            Bitmap bitmap = commentDetails.get(i).getBitmap();
            groupHolder.logo.setImageBitmap(bitmap);
        }
        */
        groupHolder.tv_name.setText(commentDetails.get(i).getName());
        groupHolder.tv_time.setText(commentDetails.get(i).getCreateDate());
        groupHolder.tv_content.setText(commentDetails.get(i).getContent());

        groupHolder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLike){
                    isLike = false;
                    groupHolder.iv_like.setColorFilter(Color.parseColor("#000000"));
                }else {
                    isLike = true;
                    groupHolder.iv_like.setColorFilter(Color.parseColor("#FF5C5C"));
                }
            }
        });
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final ChildHolder childHolder;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.comment_reply_item_layout,viewGroup, false);
            childHolder = new ChildHolder(view);
            view.setTag(childHolder);
        }
        else {
            childHolder = (ChildHolder) view.getTag();
        }
        String replyUser = commentDetails.get(i).getList().get(i1).getName();
        if(!TextUtils.isEmpty(replyUser)){
            childHolder.tv_name.setText(replyUser + ":");
        }

        childHolder.tv_content.setText(commentDetails.get(i).getList().get(i1).getContent());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    private class GroupHolder{
        private CircleImageView logo;
        private TextView tv_name, tv_content, tv_time;
        private ImageView iv_like;
        public GroupHolder(View view) {
            logo =  view.findViewById(R.id.comment_item_logo);
            tv_content = view.findViewById(R.id.comment_item_content);
            tv_name = view.findViewById(R.id.comment_item_userName);
            tv_time = view.findViewById(R.id.comment_item_time);
            iv_like = view.findViewById(R.id.comment_item_like);
        }
    }

    private class ChildHolder{
        private TextView tv_name, tv_content;
        public ChildHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.reply_item_user);
            tv_content = (TextView) view.findViewById(R.id.reply_item_content);
        }
    }
    public void addTheCommentData(CommentDetailBean commentDetailBean){
        if(commentDetailBean!=null){
            commentDetails.add(commentDetailBean);
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("评论数据为空!");
        }
    }

    public void addTheReplyData(ReplyDetailBean replyDetailBean, int groupPosition){
        if(replyDetailBean!=null){
            if(commentDetails.get(groupPosition).getList() != null ){
                commentDetails.get(groupPosition).getList().add(replyDetailBean);
            }else {
                List<ReplyDetailBean> replyList = new ArrayList<>();
                replyList.add(replyDetailBean);
                commentDetails.get(groupPosition).setList(replyList);
            }
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("回复数据为空!");
        }

    }
    private void addReplyList(List<ReplyDetailBean> replyBeanList, int groupPosition){
        if(commentDetails.get(groupPosition).getList() != null ){
            commentDetails.get(groupPosition).getList().clear();
            commentDetails.get(groupPosition).getList().addAll(replyBeanList);
        }else {
            commentDetails.get(groupPosition).setList(replyBeanList);
        }
        notifyDataSetChanged();
    }

}
