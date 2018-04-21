package com.yzq.zxing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzq.app.MyApplication;
import com.yzq.net.HttpClient;
import com.yzq.util.MyXmlParser;
import com.yzq.util.ToastUtil;

import java.io.ByteArrayInputStream;

/**
 * Created by Administrator on 2018/4/19.
 */

public class TextActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener{

    private int i ;
    private int numChars ;
    private char aChar;
    private String writeText;
    private String idString;

    private TextView tv_char, tv_result_textactivity;
    private ImageView iv_image;
    private Button bt_ok, bt_eraser;
    private static Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        Log.d("findbugs", "into TextActivity onCreate");
        initView();
        initHandler();
        initDatas();
    }

    private void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        String result = (String)msg.obj;
                        ToastUtil.showTexts(TextActivity.this, result, false);
                        isOk(result);
                        break;
                    case -1:
                        ToastUtil.showTexts(TextActivity.this,
                                "无法连接服务器，请检查网络连接", true);
                        break;
                }
            }

            private void isOk(String result){
                MyXmlParser parser = new MyXmlParser();
                String isOk = parser.getDataFromXML(new ByteArrayInputStream(result.getBytes()), "result");
                ToastUtil.showTexts(TextActivity.this, isOk, false);
                tv_result_textactivity.setText("服务器返回:"+isOk);
                Log.d("findbugs", "into TextActivity isOk before i++ :" + i);
                if(isOk != null && isOk.equals("ok")){
                    i++;
                    Log.d("findbugs", "into TextActivity isOk after i++ :" + i);
                    aChar= writeText.charAt(i);
                    tv_char.setText(String.valueOf(writeText.charAt(i)));
                }else{
                    ToastUtil.showTexts(TextActivity.this, isOk, false);
                }
                Log.d("findbugs", "into TextActivity isOk i :" + i);
            }
        };
    }

    private void initDatas(){
        Intent intent = getIntent();
        writeText = intent.getStringExtra(MyApplication.writeText);
        idString = intent.getStringExtra(MainActivity.tagIdString);
        numChars = writeText.length();
        i = 0;
        tv_char.setText(String.valueOf(writeText.charAt(i)));
        Log.d("findbugs", "into TextActivity initDatas writeText:" + writeText);
        Log.d("findbugs", "into TextActivity initDatas idString:" + idString);
        Log.d("findbugs", "into TextActivity initDatas numChars:" + numChars);
    }

    private void dealTexts(){
        Log.d("findbugs", "into TextActivity dealTexts i :" + i);
        if(i == numChars | i == numChars-1){
            Log.d("findbugs", "into TextActivity dealTexts i == numChars:" + numChars);
            Log.d("findbugs", "into =======================================>:" + numChars);
            Intent intent = new Intent(this, FinishActivity.class);
            startActivity(intent);
        }else{
            Log.d("findbugs", "into TextActivity dealTexts ready to doPost aChar:" + String.valueOf(aChar));
            HttpClient.getSingleInstance().doPost(handler, idString, String.valueOf(aChar));
        }

    }

    private void initView(){
        tv_char = (TextView)findViewById(R.id.tv_char);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        bt_ok = (Button)findViewById(R.id.bt_ok);
        bt_eraser = (Button)findViewById(R.id.bt_eraser);
        tv_result_textactivity = (TextView)findViewById(R.id.tv_result_textactivity);

        iv_image.setOnTouchListener(this);
        bt_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_ok:
                dealTexts();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.iv_image:
                if(event.getAction() == MotionEvent.ACTION_DOWN){

                }else if(event.getAction() == MotionEvent.ACTION_MOVE){

                }else if(event.getAction() == MotionEvent.ACTION_UP){

                }
                break;
        }
        return false;
    }
}
