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

    public boolean getDataFromXML(InputStream is){
        XmlPullParser parser = Xml.newPullParser();
        try{
            parser.setInput(is, "utf-8");
            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_TAG){
                     String tagName = parser.getName();
                     if(tagName != null){
                         if(tagName.equals("ver")){
                             String ver = parser.nextText();
                             Log.d("findbugs", "ver:"+ver);
                             Log.d("findbugs", "asserts ver.equals(1.0):"+ ver.equals(MyApplication.ver));
                             return ver.equals(MyApplication.ver);
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

        return false;
    }

}
