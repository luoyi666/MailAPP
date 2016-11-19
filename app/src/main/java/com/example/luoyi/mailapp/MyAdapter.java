package com.example.luoyi.mailapp;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by luoyi on 2016/11/13.
 */
public class MyAdapter extends BaseAdapter{
    private Context context;
    private ArrayList[] ss;  //传入ArrayList数组，保存发送者地址，主题，内容

    public MyAdapter(Context context, ArrayList[] ss) {
        super();
        this.context = context;
        this.ss = ss;
    }

    @Override
    public int getCount() {
        return ss[0].size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //String str=ss[position];
        String from= (String) ss[0].get(position);
        TextView textView=new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        textView.setText("From:"+from);
        return textView;
    }
}
