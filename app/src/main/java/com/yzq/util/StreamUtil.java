package com.yzq.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lile on 2018/4/19.
 */

public class StreamUtil {

    public static String readStream(InputStream inputStream){
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        byte[] buffer = new byte[4 * 1024];
        int count = 0;
        int total = 0;
        String result = "";
        try{
            while ((count = bis.read(buffer)) != -1){
                result += new String(buffer, 0 ,count);
            }
            return result;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(bis != null){
                    bis.close();
                }
                if(inputStream != null){
                    inputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
