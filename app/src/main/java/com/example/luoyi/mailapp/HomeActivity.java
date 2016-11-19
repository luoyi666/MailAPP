package com.example.luoyi.mailapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by luoyi on 2016/11/5.
 */
public class HomeActivity extends Activity{
    private ListView lv_home;
    private String[][] boxs = {
            {
                    "收邮件", "发邮件"
            },
            {
                    "INBOX", "OUTBOX"
            }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mailhome);

        lv_home = (ListView) findViewById(R.id.mailhome);
        lv_home.setAdapter(new ArrayAdapter<String>(this, R.layout.mailhome_item, boxs[0])); //建立适配器
        lv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (boxs[1][position].equals("INBOX")) { //收件箱
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {                           //通过pop3读出邮箱中的邮件
                                Socket socket;
                                OutputStream output;
                                InputStream input;
                                ArrayList listfrom =new ArrayList();   //传递发件者信息
                                ArrayList listsub =new ArrayList();     //传递邮件主题
                                ArrayList listcontext =new ArrayList();//传递邮件内容
                                String flag="yes";                    //flag标志，用于判断邮箱中是否有邮件

                                try {
                                    socket = new Socket("pop3.163.com", 110);

                                    output = socket.getOutputStream();
                                    input = socket.getInputStream();
                                } catch (IOException e1) {
                                    throw new RuntimeException("连接失败");
                                }
                                PrintWriter writer = new PrintWriter(output);
                                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                                writer.flush();
                                System.out.println(br.readLine());
                                //用户名
                                writer.println("user " + Const.username);
                                writer.flush();
                                System.out.println(br.readLine());    //读出返回信息

                                //密码
                                writer.println("pass "+Const.password);
                                writer.flush();
                                System.out.println(br.readLine());
                                //邮箱长度和大小
                                writer.println("stat");
                                writer.flush();
                                String test=br.readLine();
                                String temp[] = test.split(" ");
                                System.out.println(test);

                                int count = Integer.parseInt(temp[1]);  //邮箱中邮件数量

                                //  System.out.println(count);
                                writer.println("list");
                                writer.flush();
                                System.out.println(br.readLine());
                                if (count == 0) {
                                    flag = "no";
                                } else {

                                    for (int i = 1; i <= count + 1; i++) {//读出list后服务器返回值
                                        writer.flush();
                                        System.out.println(br.readLine());

                                    }
                                    for (int i = 1; i < count + 1; i++) {
                                        writer.println("retr " + i);
                                        writer.flush();
                                        while (true) {
                                            String s = br.readLine(); //读出一行
                                            writer.flush();
                                            String ss = br.readLine();//读出下一行
                                            if (s.startsWith("Received:")) {  //如果读出From则该行是发送者地址
                                                listfrom.add(s.substring(14,s.indexOf("(")));
                                                 System.out.println(listfrom.get(0));
                                            } else if (ss.startsWith("Received:")) {
                                                listfrom.add(ss.substring(14,ss.indexOf("(")));
                                                System.out.println(listfrom.get(0));
                                            }

                                            if (s.startsWith("Subject")||s.startsWith("SUBJECT")) {//如果读出Subject则该行是邮件主题
                                                listsub.add(s.substring(8));
                                                System.out.println(listsub.get(0));
                                            } else if (ss.startsWith("Subject")||ss.startsWith("SUBJECT")) {
                                                listsub.add(ss.substring(8));
                                                System.out.println(listsub.get(0));
                                            }
                                            if(s.equals(""))
                                            {
                                                String r="";
                                                while(true)
                                                {
                                                    String t=br.readLine();
                                                    r+=t;
                                                    System.out.println(t);
                                                    if (br.readLine().toLowerCase().equals(".")) {//读出邮内容并跳转
                                                        break;
                                                    }
                                                }
                                                listcontext.add(r);
                                                break;
                                            }
                                            if(ss.equals(""))
                                            {
                                                String r="";
                                                while(true)
                                                {
                                                    String t=br.readLine();
                                                    r+=t;
                                                    System.out.println(t);
                                                    if (br.readLine().toLowerCase().equals(".")) {//读出邮内容并跳转
                                                        break;
                                                    }
                                                }
                                                listcontext.add(r);
                                                break;
                                            }

                                        }
                                    }
                                    }
                                    socket.close();
                                    br.close();
                                    writer.close();
                                Intent intent=new Intent();
                                intent.setClass(HomeActivity.this, Rec.class);
                                Bundle bundle=new Bundle();
                                bundle.putParcelableArrayList("listfrom",listfrom);//发送传递过来的发送者信息到Rec类
                                bundle.putParcelableArrayList("listsub",listsub);
                                bundle.putParcelableArrayList("listcontext",listcontext);
                                intent.putExtras(bundle);
                                if(flag.equals("no"))//没有邮件
                                {
                                    Looper.prepare();
                                    Toast.makeText(HomeActivity.this,"收件箱没有邮件哦",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }else      //收件箱有邮件跳转到Rec类
                                {
                                    startActivity(intent);
                                }

                            }catch (Exception e) {
                                System.out.println("Error " + e);
                                return;
                            }
                        }
                    }).start();


                   //startActivity(new Intent(HomeActivity.this, Rec.class).putExtra("TYPE", boxs[1][position]));
                } else {
                    startActivity(new Intent(HomeActivity.this, SendMailHome.class));
                }
            }
        });
    }
}
