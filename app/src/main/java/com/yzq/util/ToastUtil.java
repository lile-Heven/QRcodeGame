package com.yzq.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/4/18.
 */

public class ToastUtil {

    public static void showTexts(Context context, String content, boolean isShort){
        if(isShort){
            Toast.makeText(((Activity)context).getApplicationContext(),
                    content, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(((Activity)context).getApplicationContext(),
                    content, Toast.LENGTH_LONG).show();
        }
    }

}
