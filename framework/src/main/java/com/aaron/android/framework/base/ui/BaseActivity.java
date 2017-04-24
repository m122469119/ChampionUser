package com.aaron.android.framework.base.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aaron.common.utils.LogUtils;
import com.aaron.android.framework.base.eventbus.BaseMessage;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * Created on 15/6/3.
 *
 * @author ran.huang
 * @version 3.0.1
 *
 *
 * 基类，
 */
public class BaseActivity extends AppCompatActivity {
    public final String TAG = getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, TAG + "----onCreate");
        if (isEventTarget()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d(TAG, TAG + "----onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(TAG, TAG + "----onResume");
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d(TAG, TAG + "----onPause");
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(TAG, TAG + "----onStop");
    }

    protected void postEvent(BaseMessage object) {
        EventBus.getDefault().post(object);
    }

    protected void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    @Override
    protected void onDestroy() {
        if (isEventTarget()) {
            EventBus.getDefault().unregister(this);
        }
        LogUtils.d(TAG, TAG + "----onDestroy");
        super.onDestroy();
    }

    protected boolean isEventTarget() {
        return false;
    }

}
