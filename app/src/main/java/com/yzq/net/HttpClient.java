package com.yzq.net;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yzq.app.MyApplication;
import com.yzq.util.MyXmlParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2018/4/18.
 */

public class HttpClient{

    private static HttpClient SINGLE;

    private HttpClient(){
    }

    public static HttpClient getSingleInstance(){
        if(HttpClient.SINGLE != null){
            return SINGLE;
        }else{
            HttpClient.SINGLE = new HttpClient();
            return SINGLE;
        }
    }

    public  void doGet(final Handler handler, final String urlStr){
        new Thread(){
            @Override
            public void run() {
                super.run();
                if(!MyApplication.ver.equals(getVersion())){
                    handler.sendEmptyMessage(0);
                }
            }

            private String getVersion(){
                HttpURLConnection conn = null;
                InputStream is = null;
                try{
                    URL url = new URL(urlStr);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    if(conn.getResponseCode() == 200){
                        Log.d("findbugs", "into conn.getResponseCode() == 200");
                        is = conn.getInputStream();
                        handler.sendEmptyMessage(1);
                        MyXmlParser mParser = new MyXmlParser();
                        String ver = null;
                        if((ver = mParser.getDataFromXML(is, "ver")) != null){
                            return ver;
                        }else{
                            Message msg =new Message();
                            msg.what = 1;
                            msg.obj = "读取到ver值为null";
                            handler.sendMessage(msg);

                        }
                    }
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(9);
                return null;
            }
        }.start();
    }
    public  void doGet2(final Handler handler, final String urlStr){
        new Thread(){
            @Override
            public void run() {
                super.run();
                getWriteText();
            }

            private void getWriteText(){
                HttpURLConnection conn = null;
                InputStream is = null;
                try{
                    URL url = new URL(urlStr);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    if(conn.getResponseCode() == 200){
                        Log.d("findbugs", "into conn.getResponseCode() == 200");
                        is = conn.getInputStream();
                        MyXmlParser mParser = new MyXmlParser();
                        String writeText = null;
                        if((writeText = mParser.getDataFromXML(is, "string")) != null){
                            Message msg = new Message();
                            msg.what = 4;
                            msg.obj = writeText;
                            handler.sendMessage(msg);
                        }else{
                            handler.sendEmptyMessage(3);
                        }
                    }
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public  void doPost(){
    }
}
