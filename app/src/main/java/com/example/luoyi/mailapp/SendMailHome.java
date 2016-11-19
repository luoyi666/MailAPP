package com.example.luoyi.mailapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by luoyi on 2016/11/5.
 */
public class SendMailHome extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mailedit);
        final Button send=(Button)findViewById(R.id.btn_sent);
        Button btn_cancel=(Button)findViewById(R.id.btn_cancel);
        EditText send_to=(EditText)findViewById(R.id.et_addr);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SendMailHome.this, HomeActivity.class));
            }
        });
        if(Const.reply=="reply")  //判断如果是转发则直接在send_to写入收件人
        {
            send_to.setText(Const.replyFrom);
            Const.reply="unreply";
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //点击事件，通过smtp发送邮件

                new Thread(new Runnable() {
                    EditText send_to=(EditText)findViewById(R.id.et_addr);
                    EditText subject=(EditText)findViewById(R.id.et_mailsubject);
                    EditText content=(EditText)findViewById(R.id.et_mailcontent);
                    EditText username=(EditText)findViewById(R.id.username);
                    EditText password=(EditText)findViewById(R.id.password);
                    String user = send_to.getText().toString();
                    @Override
                    public void run() {
                        try {
                            Socket socket;
                            OutputStream output;
                            InputStream input ;
                            try {
                                socket = new Socket("smtp.163.com", 25);

                                output = socket.getOutputStream();
                                input = socket.getInputStream();
                            }
                            catch (IOException e1){
                                throw new RuntimeException("连接失败");
                            }
                            PrintWriter writer=new PrintWriter(output);
                            BufferedReader br=new BufferedReader(new InputStreamReader(input));
                            writer.flush();
                            System.out.println(br.readLine());
                            writer.println("helo 163");
                            writer.flush();
                            String temp=br.readLine();
                            System.out.println(temp);

                            //验证登陆
                            writer.println("auth login");
                            writer.flush();
                            System.out.println(br.readLine());
                            //用户名
                            writer.write(Base64.encodeToString(Const.username.getBytes(),Base64.DEFAULT));
                            writer.flush();
                            System.out.println(br.readLine());

                            //密码

                            writer.write(Base64.encodeToString(Const.password.getBytes(),Base64.DEFAULT));
                            writer.flush();
                            System.out.println(br.readLine());
                            //发件人
                            String name="mail from :<"+Const.username+">";
                            writer.println(name);
                            writer.flush();
                            System.out.println(br.readLine());
                            //收件人
                            System.out.println(this.user);
                            writer.println("RCPT TO:<"+send_to.getText().toString()+">");
                            writer.flush();
                            System.out.println(br.readLine());

                            //内容
                            writer.println("data");
                            writer.flush();
                            System.out.println(br.readLine());

                            writer.println("TO:"+send_to.getText().toString());
                            writer.println("FROM:"+Const.username);
                            writer.println("SUBJECT:"+subject.getText().toString());
                            writer.println("Content-Type: text/plain;charset=\"utf-8\"");
                            writer.println();
                            writer.println(content.getText().toString());
                            writer.println(".");
                            writer.flush();
                            System.out.println(br.readLine());
                            socket.close();
                            br.close();
                            writer.close();
                            System.out.println("Done");
                            Looper.prepare();
                            Toast.makeText(SendMailHome.this,"发送成功！！！",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        } catch (Exception e) {
                            System.out.println("Error " + e);
                            return;
                        }
                    }
                }).start();
               // Toast.makeText(SendMailHome.this,"发送成功！！！",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
