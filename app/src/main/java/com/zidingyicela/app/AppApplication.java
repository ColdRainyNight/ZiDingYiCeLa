package com.zidingyicela.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * 类描述：
 * 创建人：xuyaxi
 * 创建时间：2017/7/14 20:50
 */
public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

        SharedPreferences sp = getSharedPreferences("user_setting", MODE_PRIVATE);
        boolean isNight = sp.getBoolean("night", false);
        if (isNight) {
            //这是设置成夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            //这是设置成非夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
