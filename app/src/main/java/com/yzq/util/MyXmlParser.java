package com.yzq.util;

import android.app.Application;
import android.util.Log;
import android.util.Xml;

import com.yzq.app.MyApplication;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2018/4/18.
 */

public class MyXmlParser {

    public String getDataFromXML(InputStream is, String tagNameDestination){
        XmlPullParser parser = Xml.newPullParser();
        try{
            parser.setInput(is, "utf-8");
            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_TAG){
                     String tagName = parser.getName();
                     if(tagName != null){
                         if(tagName.equals(tagNameDestination)){
                             String data = parser.nextText();
                             Log.d("findbugs", tagNameDestination + ":"+data);
                             return data;
                         }
                     }
                }
                eventType = parser.next();
            }
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
