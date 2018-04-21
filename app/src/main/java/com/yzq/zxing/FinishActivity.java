package com.yzq.zxing;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by lile on 2018/4/19.
 */

public class FinishActivity extends Activity implements View.OnClickListener{
    private Button bt_continue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        initView();
    }

    private void initView(){
        bt_continue = (Button)findViewById(R.id.bt_continue);
        bt_continue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_continue:
                finish();
                break;
        }
    }
}
