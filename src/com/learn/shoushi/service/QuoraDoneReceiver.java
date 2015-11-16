package com.learn.shoushi.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.learn.shoushi.Utils.Constant;
import com.learn.shoushi.Utils.LogUtils;

/**
 * Created by a0153-00401 on 15/11/12.
 */
public class QuoraDoneReceiver extends BroadcastReceiver {

    private final String TAG = "QuoraDoneReceiver";

    private QuoraCallBack quoraCallBack;

    private QuoraDoneReceiver() {
    }

    public QuoraDoneReceiver(QuoraCallBack quoraCallBack) {
        this.quoraCallBack = quoraCallBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtils.i(TAG, "QuoraDoneReceiver Get ... " + action);
        if (Constant.QUORA_FINISH.equals(action)) {
            quoraCallBack.quoraFinish(intent);
        }
    }

    public interface QuoraCallBack {
        void quoraFinish(Intent intent);
    }
}