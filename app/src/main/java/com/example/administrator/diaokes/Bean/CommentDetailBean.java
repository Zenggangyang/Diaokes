package com.example.administrator.diaokes.Bean;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Administrator on 2018/7/19.
 */

public class CommentDetailBean {

    /*
    private int id;
    private String nickName;
    private String userLogo;
    private String content;
    private String imgId;
    private int replyTotal;
    private String createDate;
    private List<ReplyDetailBean> replyList;
    private Bitmap bitmap;

    public CommentDetailBean(String nickName,  String content, String createDate,Bitmap bitmap) {
        this.nickName = nickName;
        this.content = content;
        this.createDate = createDate;
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getNickName() {
        return nickName;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }
    public String getUserLogo() {
        return userLogo;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
    public String getImgId() {
        return imgId;
    }

    public void setReplyTotal(int replyTotal) {
        this.replyTotal = replyTotal;
    }
    public int getReplyTotal() {
        return replyTotal;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public String getCreateDate() {
        return createDate;
    }

    public void setReplyList(List<ReplyDetailBean> replyList) {
        this.replyList = replyList;
    }
    public List<ReplyDetailBean> getReplyList() {
        return replyList;
    }
    */
    String name;
    String content;
    String imgpath;
    String zan;
    List<ReplyDetailBean> lists;
    String time;
    Bitmap bitmap;

    public CommentDetailBean(String name,String content,String time,Bitmap imgpath) {
        this.name = name;
        this.content = content;
        this.time = time;
        this.bitmap = imgpath;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public void setCreateDate(String createDate) {
        this.time = createDate;
    }
    public String getCreateDate() {
        return time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getZan() {
        return zan;
    }

    public void setZan(String zan) {
        this.zan = zan;
    }

    public List<ReplyDetailBean> getList(){
        return lists;
    }

    public void setList(List<ReplyDetailBean> lists) {
        this.lists = lists;
    }
}
