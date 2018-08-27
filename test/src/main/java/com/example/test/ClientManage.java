package com.example.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/6.
 */

public class ClientManage {
    private static ServerThread serverThread = null;
    private static int sum = 0;
    private static Map<String,Socket> clientMap = new HashMap<>();
    private static List<String> clientList = new ArrayList<>();
    private static class ServerThread implements Runnable{
        private ServerSocket server;
        private int port = 10086;
        private boolean isExit = false;

        public ServerThread(){
            try {
                server = new ServerSocket(port);
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                while(!isExit) {
                    System.out.println("等待手机的连接中... ...");
                    final Socket socket = server.accept();
                    new Thread(new Runnable() {
                        private String text;
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            try {
                                synchronized (this) {
                                    ++sum;
                                    String string = socket.getRemoteSocketAddress().toString();
                                    clientList.add(string);
                                    clientMap.put(string, socket);
                                }
                                InputStream is = socket.getInputStream();
                                OutputStream os = socket.getOutputStream();

                                byte[] buffer = new byte[1024];
                                int len;
                                while((len = is.read(buffer)) != -1) {
                                    text = new String(buffer,0,len);
                                    System.out.println("收到的数据为：" + text);
                                    os.write("已收到消息".getBytes("utf-8"));
                                }
                            }catch(IOException e) {
                                e.printStackTrace();
                            }finally {
                                System.out.println("关闭连接：" + socket.getRemoteSocketAddress().toString());
                                synchronized (this) {
                                    --sum;
                                    String string = socket.getRemoteSocketAddress().toString();
                                    clientMap.remove(string);
                                    clientList.remove(string);
                                }
                            }
                        }
                    }).start();
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
        }

        public void stop() {
            isExit = true;
            if(server != null) {
                try {
                    server.close();
                    System.out.println("已关闭server");
                }catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ServerThread startServer() {
        System.out.println("开启server");
        if(serverThread != null) {
            System.out.println("server不为null正在重启server");
            shutDown();
        }
        serverThread = new ServerThread();
        new Thread(serverThread).start();
        System.out.println("开启server成功");
        return serverThread;
    }

    public static boolean sendMessage(String name,String mag) {
        try {
            Socket socket = clientMap.get(name);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(mag.getBytes("utf-8"));
            return true;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean sendMagAll(String msg) {
        try {
            for(Socket socket: clientMap.values()) {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(msg.getBytes("utf-8"));
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static int sunTotal() {
        return sum;
    }

    public static List<String> getTotalClients() {
        return clientList;
    }

    public static void shutDown() {
        for(Socket socket : clientMap.values()) {
            try {
                socket.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        serverThread.stop();
        clientMap.clear();
        clientList.clear();
    }
}
