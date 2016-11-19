package com.example.luoyi.mailapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by luoyi on 2016/11/14.
 */
public class MailRec extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mailcontent);
        final Bundle bundle=getIntent().getExtras();
        String from=bundle.getString("from");  //获取从Rec类传递过来的发送者地址带<>
        String subject=bundle.getString("subject");//获取从Rec类传递过来的主题
        String context=bundle.getString("context");//获取从Rec类传递过来的邮件内容

        TextView textFrom=(TextView)findViewById(R.id.tv_addr);
        TextView textSubject=(TextView)findViewById(R.id.tv_mailsubject);
        TextView textContext=(TextView)findViewById(R.id.tv_mailcontent);
        TextView btn_cancel=(TextView)findViewById(R.id.btn_cancel);
        TextView btn_relay=(TextView)findViewById(R.id.btn_relay);
        textFrom.setText(from);
        textSubject.setText(subject);
        textContext.setText(context);
        final String replyFrom=from.substring(1,from.length()-1);  //发送者的地址不带<>
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MailRec.this, HomeActivity.class));
            }
        });
        btn_relay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Const.reply="reply";   //向Const类中传递reply，用于表示这是转发而不是发送
                Const.replyFrom=replyFrom;//向Const类中传递replyFrom，用于接收者项直接写入地址
                Intent intent=new Intent();
                intent.setClass(MailRec.this,SendMailHome.class);
                startActivity(intent);//跳转到发送界面
            }
        });

    }
}
