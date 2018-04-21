package com.yzq.net;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.yzq.app.MyApplication;
import com.yzq.util.MyXmlParser;
import com.yzq.util.StreamUtil;
import com.yzq.zxing.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

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
                            Log.d("findbugs", "into doGet2 writeText:" + writeText);
                            Message msg = new Message();
                            msg.what = 4;
                            msg.obj = writeText;
                            handler.sendMessage(msg);
                        }else{
                            Log.d("findbugs", "into doGet2 writeText is null" + writeText);
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

    public  void doPost(final Handler handler, final String idString, final String aCharStr){
        Log.d("findbugs", "into doPost aCharStr:"+aCharStr);
        new Thread() {
            public void run() {
                try {
                    URL url = new URL("http://www.bachinmaker.com/api/write/save.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    String testDatas = generateTestDatas();
                    String outputDatas = "id="+ URLEncoder.encode(idString,"utf-8")
                            +"&char=" + URLEncoder.encode(aCharStr,"utf-8")
                            +"&data=" + URLEncoder.encode(testDatas,"utf-8");
                    conn.setRequestProperty("Content-Length",String.valueOf(outputDatas.length()));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(outputDatas.getBytes());
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        String result = StreamUtil.readStream(conn.getInputStream());
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    } else {
                        handler.sendEmptyMessage(-1);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    handler.sendEmptyMessage(-1);
                }
            }

            private String generateTestDatas(){
                String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; //62个
                Random random = new Random();
                StringBuffer sb = new StringBuffer();
                for(int i = 0; i<1000; i++){
                    int number = random.nextInt(62);
                    sb.append(str.charAt(number));
                }
                return sb.toString();
            }
        }.start();
    }
}
