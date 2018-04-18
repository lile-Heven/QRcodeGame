package com.yzq.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.yzq.util.ToastUtil;
import com.yzq.zxing.MainActivity;
import com.yzq.zxing.R;

public class BottomDialog extends Dialog implements View.OnClickListener{

    private View inflate;
    private TextView choosePhoto;
    private TextView takePhoto;
    private Context context;

    public BottomDialog(Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //填充对话框的布局
        inflate = LayoutInflater.from(context).inflate(R.layout.dialog_bottom, null);
        //初始化控件
        choosePhoto = (TextView) inflate.findViewById(R.id.choosePhoto);
        takePhoto = (TextView) inflate.findViewById(R.id.takePhoto);
        choosePhoto.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        //将布局设置给Dialog
        this.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = this.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.takePhoto:
                //ToastUtil.showTexts(context,"正在下载请稍后...",true);
                ((MainActivity)context).downloadNewApk();
                break;
            case R.id.choosePhoto:
                ToastUtil.showTexts(context,"sorry,非最新版无法运行",true);
                ((MainActivity)context).finish();
                break;
        }
        this.dismiss();
    }

}