package cn.tanjianff.simpleemailapp.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import Decoder.BASE64Encoder;

/**
 * Created by tanjian on 16/10/30.
 * 通过此类中的方法连接邮箱,并获取信息
 */

public class connectEmail {
    private String sender;
    private String reciever;
    private String password;

    //对用户名和密码进行Base64加密
    private String user;
    private String passwd;
    public connectEmail(){
            sender="tanjian20150101@163.com";
            password="tj1002";
            user=new BASE64Encoder().encode(sender.substring(0,sender.indexOf("@")).getBytes());
            passwd=new BASE64Encoder().encode(password.getBytes());
            System.out.println(user+"\n"+passwd);
    }


    public boolean connectServer() throws IOException {
        Socket socket=new Socket("smtp.163.com",25);
        InputStream inputStream=socket.getInputStream();
        OutputStream outputStream=socket.getOutputStream();
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter writer=new PrintWriter(outputStream,true);
        //HELo
        writer.println("HELO huan");
        System.out.println(reader.readLine());
        //AUTH LOGIN
        writer.println("auth login");
        System.out.println(reader.readLine());
        writer.println(user);
        System.out.println(reader.readLine());
        writer.println(passwd);
        System.out.println(reader.readLine());
        //Above   Authentication successful
        return true;
    }

    public static void main(String [] args){
        try {
            System.out.println(new connectEmail().connectServer());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
