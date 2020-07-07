package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
{
    Button get , post;
    EditText show;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get = (Button) findViewById(R.id.get);
        post = (Button) findViewById(R.id.post);
        show = (EditText)findViewById(R.id.show);
        //利用Handler更新UI
        final Handler h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0x123){
                    show.setText(msg.obj.toString());
                }
            }
        };

        get.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Thread(new AccessNetwork("GET", "http://api.bilibili.cn/author_recommend?aid=[id] ", null, h)).start();
            }
        });
        post.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Thread(new AccessNetwork("POST", "https://waishuo.leanapp.cn/api/v1.0/users/login" ,null , h)).start();
            }
        });
    }
}
class AccessNetwork implements Runnable{
    private String op ;
    private String url;
    private String params;
    private Handler h;

    public AccessNetwork(String op, String url, String params,Handler h) {
        super();
        this.op = op;
        this.url = url;
        this.params = params;
        this.h = h;
    }

    @Override
    public void run() {
        Message m = new Message();
        m.what = 0x123;
        if(op.equals("GET")){
            Log.i("iiiiiii","发送GET请求");
            m.obj = GetPostUtil.sendGet(url, params);
            Log.i("iiiiiii","----"+m.obj);
        }
        if(op.equals("POST")){
            Log.i("iiiiiii","发送POST请求");
            m.obj = GetPostUtil.sendPost(url, params);
            Log.i("gggggggg","----"+m.obj);
        }
        h.sendMessage(m);
    }
}