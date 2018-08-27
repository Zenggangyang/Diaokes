package com.example.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class myClass {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        boolean isExit = false;

        ClientManage.startServer();
        while(!isExit) {
            line = br.readLine();
            if(line.startsWith("exit")) {
                System.out.println("退出命令");
                break;
            }
            if(line.startsWith("send")) {
                sendMessage(line);
            }else if(line.startsWith("list")) {
                printTotal();
            }else if(line.startsWith("all")) {
                allSendMsg(line);
            }else {
                System.out.println("输入错误 请重新输入");
            }
        }
        ClientManage.shutDown();
    }

    private static void allSendMsg(String line) {
        String[] field = line.split("//");
        if(field.length == 2) {
            ClientManage.sendMagAll(field[1]);
            System.out.println("发送结果为：" + ClientManage.sendMagAll(field[1]) );

        }else {
            System.out.println("格式不正确 例：all//message");
        }
    }
    private static void printTotal() {
        List<String> totalClients = ClientManage.getTotalClients();
        System.out.println("连接数量为：" + totalClients.size());
        for(String total : totalClients) {
            System.out.println(total);
        }
    }

    private static void sendMessage(String line) {
        String[] field = line.split("//");
        if(field.length == 3) {
            ClientManage.sendMessage(field[1], field[2]);
            System.out.println("send结果为:" + ClientManage.sendMessage(field[1],field[2]));

        }else {
            System.out.println("命令不正确。例子：send//name//msg");
        }
    }

}
