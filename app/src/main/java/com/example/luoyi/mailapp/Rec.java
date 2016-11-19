package com.example.luoyi.mailapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by luoyi on 2016/11/13.
 */
public class Rec extends Activity {

    Context context;
    ArrayList[] array=new ArrayList[3];  //建立ArrayList数组，用于保存发送者地址，主题，邮件内容
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive);
        Bundle bundle=getIntent().getExtras();              //接收从HomeActivity传递过来的发送者地址，主题，邮件内容
        ArrayList listfrom=bundle.getParcelableArrayList("listfrom");
        ArrayList listsub=bundle.getParcelableArrayList("listsub");
        ArrayList listcontext=bundle.getParcelableArrayList("listcontext");
       array[0]=listfrom;
        array[1]=listsub;
        array[2]=listcontext;



        ListView lisetView=(ListView)findViewById(R.id.listView1);
        //ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_checked,Const.ss);
       MyAdapter adapter=new MyAdapter(this,array);  //创建自定义的适配器
       // lisetView.setAdapter(adapter);
       // ArrayAdapter adapter=new ArrayAdapter(Rec.this,android.R.layout.simple_list_item_checked,ss);
        lisetView.setAdapter(adapter);

       // new Receive().start();

        lisetView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  //根据点击某一行进入收件箱界面
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String from= (String) array[0].get(position);//获取array数组中保存的listfrom的posion项
                String subject= (String) array[1].get(position);//获取array数组中保存的listsubject的posion项
                String context= (String) array[2].get(position);//获取array数组中保存的listcontext的posion项
                Intent intent1=new Intent();
                Bundle bundle2=new Bundle();
                intent1.setClass(Rec.this,MailRec.class);
                bundle2.putString("from",from);
                bundle2.putString("subject",subject);
                bundle2.putString("context",context);
                intent1.putExtras(bundle2);
                startActivity(intent1);
            }
        });
    }
}
