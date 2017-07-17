package com.zidingyicela.celaatvity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.zidingyicela.R;


/**
 * 类描述：
 * 创建人：xuyaxi
 * 创建时间：2017/7/14 21:20
 */
public class OneActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oneactivity);
        initqu();//去状态栏
    }

    private void initqu() {
        getSupportActionBar().hide();
        //去状态栏 加沉浸式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
