package com.learn.shoushi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.learn.shoushi.Utils.LogUtils;
import com.learn.shoushi.Utils.SettingManager;
import com.learn.shoushi.activity.testView.testViewActivity;
import com.learn.shoushi.fragment.home.MainTabHostActivity;
import com.learn.shoushi.userManager.UserInfo;

/**
 * Created by a0153-00401 on 15/11/9.
 */
public class SignInActivity extends BaseActivity{
    public static int opentIntent;

    public static void openSignInActivity(Context context, int type)
    {
        Intent intent = new Intent(context,SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        opentIntent = type;
        context.startActivity(intent);
        if (context instanceof Activity && !(context instanceof SignInActivity)) {
            ((Activity) context).finish();
        }
    }


    @Override
    protected void onCreate(Bundle args) {
        super.onCreate(args);


        boolean islogin = SettingManager.getInstance().isLogin();
        LogUtils.d("islogin  ====" + islogin);
        if (!islogin) {
            MainTabHostActivity.openHomeActivity(SignInActivity.this);

           /* Intent intent = new Intent(SignInActivity.this,testViewActivity.class);
            startActivity(intent);*/
            SignInActivity.this.finish();
            return;
        }

        //登录状态未设置手势密码,先设置手势密码
       /* if (TextUtils.isEmpty(UserInfo.getInstance().getCurrentPatternPassword())) {
            SetUnlockPatternFragment.showForSetPassword(SignInActivity.this);
            finish();
            return;
        } else {
            Intent intent = new Intent(SignInActivity.this, UnLockPatternActivity.class);
            intent.putExtra(UnLockPatternActivity.mOpenIntent, opentIntent);
            startActivity(intent);
            finish();
            return;
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
