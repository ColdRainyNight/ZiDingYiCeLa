package com.zidingyicela;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zidingyicela.celaatvity.FourActivity;
import com.zidingyicela.celaatvity.LiuActivity;
import com.zidingyicela.celaatvity.OneActivity;
import com.zidingyicela.celaatvity.ThreeActivity;
import com.zidingyicela.celaatvity.TwoActivity;
import com.zidingyicela.celaatvity.WuActivity;
import com.zidingyicela.avtivity.Demo_activity1;
import com.zidingyicela.avtivity.Demo_activity2;
import com.zidingyicela.avtivity.Demo_activity3;
import com.zidingyicela.avtivity.Demo_activity4;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置夜间模式 TabLayout滑动
 * 第三方登录QQ Fragment
 * 自定义侧拉
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String APP_ID = "1105602574";//官方获取的APPID

    private SharedPreferences sp;

    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    /* // 默认是日间模式
     private int theme = R.style.AppTheme;
 */
    @ViewInject(R.id.tab)
    private TabLayout tab;

    @ViewInject(R.id.vp)
    private ViewPager vp;
    private String str[] = new String[]{"热门", "科技", "搜索", "篮球"};
    private List<Fragment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否有主题存储
     /*   if (savedInstanceState != null) {
            theme = savedInstanceState.getInt("theme");
            setTheme(theme);
        }*/
        setContentView(R.layout.activity_main);
        x.view().inject(this);
        sp = getSharedPreferences("user_setting",MODE_PRIVATE);
        initone();//去状态栏
        initCeLa();//侧拉
        //传入参数APPID和全局Context上下文
         mTencent = Tencent.createInstance(APP_ID, MainActivity.this.getApplicationContext());

        initView();
    }

    private void initView() {
        //使用适配器将ViewPager与Fragment绑定在一起
        vp = (ViewPager) findViewById(R.id.vp);
        //添加数据
        listadd();
        Vpadapter adapter = new Vpadapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        //将TabLayout与ViewPager绑定在一起
        tab = (TabLayout) findViewById(R.id.tab);
        //绑定
        tab.setupWithViewPager(vp);
        //字体颜色
        tab.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.hui));
        //指示器颜色（选中状态下划线的颜色）
        tab.setSelectedTabIndicatorColor(getResources().getColor(R.color.hui));
        //模式
        tab.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    class Vpadapter extends FragmentPagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return str[position];
        }

        public Vpadapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    private void listadd() {
        list = new ArrayList<>();
        list.add(new Demo_activity1());
        list.add(new Demo_activity2());
        list.add(new Demo_activity3());
        list.add(new Demo_activity4());
    }

    @Event({R.id.t1, R.id.t2,
            R.id.t3, R.id.t4,
            R.id.t5, R.id.t6, R.id.t7})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.t1:
                Intent it1 = new Intent(MainActivity.this, OneActivity.class);
                startActivity(it1);
                break;
            case R.id.t2:
                Intent it2 = new Intent(MainActivity.this, TwoActivity.class);
                startActivity(it2);
                break;
            case R.id.t3:
                Intent it3 = new Intent(MainActivity.this, ThreeActivity.class);
                startActivity(it3);
                break;
            case R.id.t4:
                Intent it4 = new Intent(MainActivity.this, FourActivity.class);
                startActivity(it4);
                break;
            case R.id.t5:
                Intent it5 = new Intent(MainActivity.this, WuActivity.class);
                startActivity(it5);
                break;
            case R.id.t6:
                Intent it6 = new Intent(MainActivity.this, LiuActivity.class);
                startActivity(it6);
                break;
            case R.id.t7:
                changeNight();
              /*  theme = (theme == R.style.AppTheme) ? R.style.NightAppTheme : R.style.AppTheme;
                MainActivity.this.recreate();
                break;*/
        }
    }

 /*   @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme", theme);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        theme = savedInstanceState.getInt("theme");
    }*/

    public void buttonLogin(View v) {
        /**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，
         * 第二个参数SCOPO 是一个String类型的字符串，表示一些权限
         官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。
         例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
         第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类 */
        mIUiListener = new BaseUiListener();
        //all表示获取所有权限
        mTencent.login(MainActivity.this, "all", mIUiListener);
    }

    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        Log.e(TAG, "登录成功" + response.toString());
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "登录失败" + uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "登录取消");
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initCeLa() {
        // 创建对象
        SlidingMenu smenu = new SlidingMenu(this);
        // 设置出现在左边还是右边
        smenu.setMode(SlidingMenu.LEFT);
        // 设置调出slidingmenu的区域
        smenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        // 设置阴影的宽度
        smenu.setShadowWidth(8);
        smenu.setShadowDrawable(R.drawable.shadow);
        // 设置slidingmenu滑出来时的宽度
        smenu.setBehindOffset(100);
        // 设置刚拉出来的时候颜色，1为全黑
        smenu.setFadeDegree(0.3f);
        // 添加到Activity上面
        smenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        // 此处加入要侧滑的布局文件
        smenu.setMenu(R.layout.menu);
        // 关闭监听
        smenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                Toast.makeText(MainActivity.this, "关闭了", Toast.LENGTH_SHORT).show();

            }
        });
        // 打开监听
        smenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {

            @Override
            public void onOpened() {
                Toast.makeText(MainActivity.this, "打开了", Toast.LENGTH_SHORT).show();

            }
        });
        // 创建动画对象设置显示的时候出现的动画，这里我写的是一个入场动画
        SlidingMenu.CanvasTransformer canvasTransformer = new SlidingMenu.CanvasTransformer() {

            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen * 0.25 + 0.75);
                canvas.scale(scale, scale, canvas.getWidth() / 2, canvas.getHeight() / 2);
            }
        };
        smenu.setBehindCanvasTransformer(canvasTransformer);
    }

    private void initone() {
        getSupportActionBar().hide();
        //去状态栏 加沉浸式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void changeNight() {
        boolean isNight = sp.getBoolean("night", false);
        if (isNight) {
            //这是设置成非夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            sp.edit().putBoolean("night", false).commit();
        } else {
            //这是设置成夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            sp.edit().putBoolean("night", true).commit();
        }
        recreate();
    }
}