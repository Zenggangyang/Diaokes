package com.example.administrator.diaokes.SQLServer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.administrator.diaokes.recyclerView.recyclerItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/11.
 */

public class sqlConn {
        private static Connection getSQLConnection(String ip,String user,String pwd,String db){
            Connection con = null;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:jtds:sqlserver://"+ip+":1433/"+db,user,pwd);
            }catch (ClassNotFoundException e){
                Log.e("con",e.getMessage());
            }catch (SQLException e){
                Log.e("con",e.getMessage());
            }
            return con;
        }

        public static String[] QuerySQL(String query){
            String[] result = new String[5];
            int i = 0;
            try {
                Connection conn = getSQLConnection("192.168.1.103","sa","shuizhimeng","sde");
                String sql = "select name from sde.MERGESUGGESTIONS where name like '%"+query+"%'";
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()){
                    String address = rs.getString("name");
                    result[i] = address;
                    i++;
                }
                rs.close();
                statement.close();
                conn.close();
            }catch (SQLException e){
                Log.e("con",e.getMessage());
            }
            return result;
        }


        public static void insertDT(String name,String content,String path,int zan){
            try {
                Connection conn = getSQLConnection("192.168.1.103", "sa", "shuizhimeng", "test");
                String sql = "insert into dbo.dtfound values(?,?,?,?)";
                /*
                File f =new File(path);
                FileInputStream input= new FileInputStream(f);
                */
                PreparedStatement ps=conn.prepareStatement(sql);
                ps.setString(1,name);
                ps.setString(2,content);
                //ps.setBinaryStream(3, input,(int)f.length());
                ps.setString(3,path);
                ps.setInt(4,zan);
                ps.executeUpdate();
                ps.close();
                //input.close();
                conn.close();
            }catch (SQLException e){
                Log.e("con",e.getMessage());
            }
        }

        public static recyclerItem[] queryDT(){
            recyclerItem[] recyclerItems = null;
            try {
                Connection conn = getSQLConnection("192.168.1.103","sa","shuizhimeng","test");
                //OutputStream out = new FileOutputStream("tupian.jpg");
                String sql = "select * from dbo.dtfound";
                PreparedStatement statement = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = statement.executeQuery();
                rs.last();
                recyclerItems = new recyclerItem[rs.getRow()];
                rs.beforeFirst();
                int i = 0;
                while(rs.next()) {
                    String name = rs.getString("name");
                    String content = rs.getString("dcontent");
                    String path = rs.getString("path");

                    recyclerItems[i] = new recyclerItem(name,0,content,0,null,path,"最近");
                    i = i + 1;
                    /*
                    int tmpi=0;
                    InputStream ins = rs.getBinaryStream("img");//列名
                    while ((tmpi = ins.read()) != -1) {
                        out.write(tmpi);
                    }
                    ins.close();
                    */
                }
                rs.close();
                statement.close();
                conn.close();
            }catch (SQLException e1){
                Log.e("ss",e1.getMessage());
            }
            return recyclerItems;
        }

}
