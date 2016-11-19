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
 * Created by luoyi on 2016/11/17.
 */
public class Login extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login=(Button)findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {   //登录事件，根据smtp发送用户名及密码，根据返回值判断登录是否成功
            final EditText username=(EditText)findViewById(R.id.username);
            final  EditText userpasseord=(EditText)findViewById(R.id.password);
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    String name=username.getText().toString();
                    String pass=userpasseord.getText().toString();
                    @Override
                    public void run() {
                        if(name.isEmpty() || pass.isEmpty())
                        {
                            Looper.prepare();
                            Toast.makeText(Login.this,"用户名或密码不能为空！！！",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
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
                            writer.write(Base64.encodeToString(name.getBytes(),Base64.DEFAULT));
                            writer.flush();;
                            System.out.println(br.readLine());
                            //密码
                            writer.write(Base64.encodeToString(pass.getBytes(),Base64.DEFAULT));
                            writer.flush();
                            Const.test1=br.readLine();//将返回值保存在Const类中的静态变量中
                            System.out.println(Const.test1);
                            System.out.println("测试");
                        } catch (Exception e) {
                            System.out.println("Error " + e);
                            return;
                        }
                        try {
                            if(Const.test1.equals("235 Authentication successful"))  //如果服务器返回该值，则登录成功
                            {
                                Const.username=name;
                                Const.password=userpasseord.getText().toString();
                                Intent intent=new Intent();
                                intent.setClass(Login.this, HomeActivity.class);
                                startActivity(intent);
                            }
                            else if(Const.test1.equals("535 Error: authentication failed"))//如果服务器返回该值，则登录失败
                            {
                                Looper.prepare();
                                Toast.makeText(Login.this,"用户名或密码错误！！！",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                        } catch (Exception e) {
                            System.out.println("Error " + e);
                            return;
                        }

                    }
                }).start();

            }
        });
    }

}
