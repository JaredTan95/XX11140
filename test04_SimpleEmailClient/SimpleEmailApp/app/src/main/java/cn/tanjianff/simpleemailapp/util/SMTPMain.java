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
 * 封装对SMTP的操作
 */

public class SMTPMain {
    private String sender = "tanjian20150101@163.com";
    private String receiver = "773767470@qq.com";
    private String password = "tj1002";
    public SMTPMain(){

    }
    public SMTPMain(String sender, String receiver, String password) {
        this.sender = sender;
        this.receiver = receiver;
        this.password = password;
    }

    public void connect() throws IOException {
        String user = new BASE64Encoder().encode(sender.substring(0, sender.indexOf("@")).getBytes());
        String pass = new BASE64Encoder().encode(password.getBytes());

            Socket socket = new Socket("smtp.163.com", 25);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter writter = new PrintWriter(outputStream, true);
            System.out.println(reader.readLine());
            //HELO
            writter.println("HELO 163");
            System.out.println(reader.readLine());
            //AUTH LOGIN
            writter.println("auth login");
            System.out.println(reader.readLine());
            writter.println(user);
            System.out.println(reader.readLine());
            writter.println(pass);
            System.out.println(reader.readLine());

            //Above   Authentication successful <pre name="code" class="java">

            /*
            //Set mail from   and   rcpt to
            writter.println("mail from:<" + sender +">");
            System.out.println(reader.readLine());
            writter.println("rcpt to:<" + receiver +">");
            System.out.println(reader.readLine());

            //Set data
            writter.println("data");
            System.out.println(reader.readLine());
            writter.println("subject:女神，是我");
            writter.println("from:" + sender);
            writter.println("to:" + receiver);
            writter.println("Content-Type: text/plain;charset=\"gb2312\"");
            writter.println();
            writter.println("女神，晚上可以共进晚餐吗？");
            writter.println(".");
            writter.println("");
            System.out.println(reader.readLine());

            //Say GoodBye
            writter.println("rset");
            System.out.println(reader.readLine());
            writter.println("quit");
            System.out.println(reader.readLine());
            */

    }

    public static void main(String [] args) throws IOException {
        new SMTPMain().connect();
    }
}
