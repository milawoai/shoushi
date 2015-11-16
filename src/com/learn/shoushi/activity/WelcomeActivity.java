package com.learn.shoushi.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import com.learn.shoushi.R;
import com.learn.shoushi.Utils.AppMethods;
import com.learn.shoushi.aChangeView.BaseImageView;
import com.learn.shoushi.appManager.HelperApplication;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by a0153-00401 on 15/11/5.
 */
public class WelcomeActivity extends BaseActivity {

    public static String tag = "WelcomeActivity";

    private Timer timer = new Timer();
    //    ServiceConnection conn;
 //   private AutoAttachRecyclingImageView welcomeImageView;
    private BaseImageView welcomeImageView;

    public class DrawChangeListener implements BaseImageView.SizeChangeListener
    {
        public void sizeChanged(int w, int h, int oldw, int oldh)
        {
            //Log.i(tag,"onSizeChanged,w="+w+",h="+h+",oldw="+oldw+",oldh="+oldh);
            //int ImageViewWidth = welcomeImageView.getWidth();
            //int ImageViewHeight = welcomeImageView.getHeight();
            //Log.i(tag,"ImageViewWidth :" + ImageViewWidth);
            //Log.i(tag, "ImageViewHeight :" + ImageViewHeight);
        }
    }

    /**
     * 打开首页重新跳转登陆页或主页
     *
     * @param context
     */
    public static void openWelcomeActivity(Context context) {
        Intent intent = new Intent(context, WelcomeActivity.class);
        // 关闭之前打开的所有activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle args) {
        super.onCreate(args);
        initView();

       // AppMethods.isAppOnForceground(HelperApplication.getContext());
       timer.schedule(taskToDesktop, 1000);
    }

    @Override
    protected  void onResume()
    {
        super.onResume();

    }

    private void initView() {
        setContentView(R.layout.welcome_screen);
        welcomeImageView = (BaseImageView) findViewById(R.id.image_welcome_bg);
        welcomeImageView.setImageResource(R.drawable.welcome_bg);
        welcomeImageView.setSizeChangeListener(new DrawChangeListener());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeigh = dm.heightPixels;

        Log.i(tag,"screenWidth :" + screenWidth);
        Log.i(tag,"screenHeigh :" + screenHeigh);


    }


    /* 欢迎页结束，正式进入app */
    TimerTask taskToDesktop = new TimerTask() {
        public void run() {
            // 欢迎页面已显示, 用户未登陆的话，显示登陆页面
            if (getIntent().getData() != null) {
                Uri data = getIntent().getData();
                if (data.getHost().equals("cus") && data.getScheme().equals("rrmoney")) {
                  //  SignInActivity.openSignInActivity(FirstActivity.this, UnLockPatternActivity.OPEN_OTHER);
                  //  WelcomeActivity.this.finish();
                   // overridePendingTransition(0,R.animator.fade_left_move);
                }
                return;
            }
            else
            {
                Log.i(tag,"getIntent().getData() = null");
               // WelcomeActivity.this.finish();
               // overridePendingTransition(0, R.animator.fade_left_move);
            }
            SignInActivity.openSignInActivity(WelcomeActivity.this, 1);
            WelcomeActivity.this.finish();
            overridePendingTransition(0, R.anim.fade_alpha);
        }
    };


    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
//        unbindService(conn);
        super.onDestroy();
    }

}
