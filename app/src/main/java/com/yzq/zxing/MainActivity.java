package com.yzq.zxing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.net.HttpClient;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;
import com.yzq.zxinglibrary.encode.CodeCreator;

import java.util.List;


/**
 * @author: yzq
 * @date: 2017/10/26 15:17
 * @declare :
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button scanBtn;
    private ImageView iv_refresh_blue;
    private TextView result, tv_connecting;
    private Toolbar toolbar;

    private boolean flag_connect_successfully = false;
    private int REQUEST_CODE_SCAN = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        inquiryServer();
    }

    private void inquiryServer(){
        HttpClient client = new HttpClient();
        client.doGet();
    }

    private void initView() {
        /*扫描按钮*/
        scanBtn = findViewById(R.id.scanBtn);
        scanBtn.setEnabled(false);
        scanBtn.setOnClickListener(this);

        iv_refresh_blue = findViewById(R.id.iv_refresh_blue);

        tv_connecting = findViewById(R.id.tv_connecting);

        result = findViewById(R.id.result);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("二维码小游戏");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        result = (TextView) findViewById(R.id.result);
        scanBtn = (Button) findViewById(R.id.scanBtn);

        setMyAnimation();
        startTextAnimation();
    }

    @Override
    public void onClick(View v) {

        Bitmap bitmap = null;
        switch (v.getId()) {
            case R.id.scanBtn:

                AndPermission.with(this)
                        .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);

                                /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
                                 * 也可以不传这个参数
                                 * 不传的话  默认都为默认不震动  其他都为true
                                 * */

                                ZxingConfig config = new ZxingConfig();
                                config.setPlayBeep(true);
                                config.setShake(true);
                                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);

                                startActivityForResult(intent, REQUEST_CODE_SCAN);
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                Uri packageURI = Uri.parse("package:" + getPackageName());
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);

                                Toast.makeText(MainActivity.this, "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                            }
                        }).start();

                break;
            default:
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                result.setText("扫描结果为：" + content);
            }
        }
    }

    private void setMyAnimation(){
        Animation anima_rotate_round= AnimationUtils.loadAnimation(this, R.anim.rotate);
        if(anima_rotate_round != null){
            anima_rotate_round.setInterpolator(new LinearInterpolator());
            iv_refresh_blue.startAnimation(anima_rotate_round);
        }
    }

    private void startTextAnimation(){
        new Thread(){
            private int i = 0;
            private String[] texts = {"正在连接到服务器", "正在连接到服务器.","正在连接到服务器..","正在连接到服务器..."};
            @Override
            public void run() {
                super.run();
                while(!flag_connect_successfully){
                    mySleep();
                    i_increase();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_connecting.setText(texts[i]);
                        }
                    });

                }
            }

            private void mySleep(){
                try{
                    sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

            private void i_increase(){
                if(i < 3){
                    i++;
                }else{
                    i = 0;
                }

            }
        }.start();

    }
}
