package com.goodchef.liking.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.aaron.android.framework.base.BaseActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/8 上午10:15
 */
public class LoadingActivity extends BaseActivity {
    private String appName = "";
    private String versionName = "";
    private final int DURATION = 1500;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        getPackageInfo();
        initView();
        LiKingVerifyUtils.initApi(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (Preference.isNewVersion()) {
//                    jumpToGuideActivity();
//                } else {
//                    jumpToMainActivity();
//                }
                jumpToMainActivity();
            }
        }, DURATION);
    }

    private void jumpToGuideActivity() {
//        Intent guideIntent = new Intent(this, GuideActivity.class);
//        startActivity(guideIntent);
//        finish();
    }

    private void jumpToMainActivity() {
        Intent intent = new Intent(this, LikingHomeActivity.class);
        startActivity(intent);
        finish();
    }


    private void initView() {
        TextView appNameTextView = (TextView) findViewById(R.id.appNameTextView);
        TextView appVersionTextView = (TextView) findViewById(R.id.appVersionTextView);
        appNameTextView.setText(appName);
        appVersionTextView.setText(versionName);
    }

    private void getPackageInfo() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo packageInfo = manager.getPackageInfo(this.getPackageName(), 0);
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            appName = appInfo.loadLabel(manager).toString();
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }



}
