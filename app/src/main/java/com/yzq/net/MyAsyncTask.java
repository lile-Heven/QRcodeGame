package com.yzq.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.yzq.util.ToastUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载APK的异步任务

 */

public class MyAsyncTask extends AsyncTask<String, Integer, String> {
    private ProgressDialog progressDialog;
    private File file;
    private Context context;

    public MyAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showDownloadProgressDialog();
    }

    @Override
    protected String doInBackground(String... params) {
        URL url;
        HttpURLConnection conn;
        BufferedInputStream bis = null;
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            if(params[0] == null){
                return "args error";
            }
            url = new URL(params[0]);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Accept-Encoding", "identity");
            bis = new BufferedInputStream(conn.getInputStream());


            String fileName = url.toString().substring(url.toString().lastIndexOf("/"));
            String filePath = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/" + fileName;
            file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }else{
                file.delete();
                file.createNewFile();
            }

            int fileLength = conn.getContentLength();
            Log.d("findbugs", "fileLength:"+fileLength);

            fos = new FileOutputStream(file);
            byte data[] = new byte[128];
            long total = 0;
            int count;
            while ((count = bis.read(data)) != -1) {
                //Log.d("findbugs", "into (count = bis.read(data)) != -1  count:"+count);
                Log.d("findbugs", new String(data,0, count));
                total += count;
                fos.write(data, 0, count);
                fos.flush();
                publishProgress((int) (total * 100 / fileLength));
            }
            publishProgress(101);

        } catch (IOException e) {
            Log.d("findbugs", "into catch(IOException e)");
            e.printStackTrace();
            publishProgress(-1);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        if(progress[0] == -1){
            ToastUtil.showTexts(context,"url可能有误",true);
            return;
        }else if(progress[0] == 101){
            ToastUtil.showTexts(context,"下载完成",true);
            return;
        }
        Log.d("findbugs", "into onProgressUpdate");
        progressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        openFile(file);
        progressDialog.dismiss();
    }

    private void openFile(File file) {
        if (file!=null){
            Log.d("findbugs", "into openFile");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }

    }

    private void showDownloadProgressDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在下载...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);//设置不可点击界面之外的区域让对话框消失
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//进度条类型
        progressDialog.show();
    }
}