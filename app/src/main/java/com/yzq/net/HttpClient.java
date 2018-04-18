package com.yzq.net;

import android.util.Log;

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

    String urlStr = "http://www.bachinmaker.com/api/write/ver.php";

    public HttpClient(){
    }

    public  void doGet(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpURLConnection conn = null;
                InputStream is = null;
                String resultData = "";
                try{
                    URL url = new URL(urlStr);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    if(conn.getResponseCode() == 200){
                        Log.d("findbugs", "into conn.getResponseCode() == 200");
                        is = conn.getInputStream();
                        MyXmlParser mParser = new MyXmlParser();
                        boolean verRight = mParser.getDataFromXML(is);

                        /*InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        String inputLine = "";
                        while((inputLine = br.readLine()) != null){
                            Log.d("findbugs", "into (inputLine = br.readLine()) != null");
                            resultData += inputLine + "\n";
                            Log.d("findbugs", "inputLine:" + inputLine);
                        }
                        System.out.println("GET请求获取的内容：" + resultData);
                        */

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
