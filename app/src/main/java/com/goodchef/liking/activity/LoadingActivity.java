package com.goodchef.liking.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.BaseActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.InitApiFinishedMessage;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.data.PatchData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.utils.PatchDowner;

import cn.jiajixin.nuwa.Nuwa;

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
    private PatchData previousPatchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        getPackageInfo();
        previousPatchData = Preference.getPatchData();
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

    private void loadPatch(PatchData patchData) {
        if (patchData != null && patchData.isPatchNeed() && !TextUtils.isEmpty(patchData.getPatchFile())) {
            LogUtils.i("Dust", "加载补丁");
            Nuwa.loadPatch(this, previousPatchData.getPatchFile());
        } else {
            LogUtils.i("Dust", "不加载补丁");
        }
    }

    /**
     * 下载补丁，或更新已存在补丁状态
     *
     * @param patchData
     */
    private void downPatch(PatchData patchData) {
        PatchDowner patchDowner = new PatchDowner(this);
        patchDowner.execute(patchData);
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(InitApiFinishedMessage initApiFinishedMessage) {
        if (initApiFinishedMessage.isSuccess()) {
            BaseConfigResult.BaseConfigData baseConfigData = LiKingVerifyUtils.sBaseConfigResult.getBaseConfigData();
            if (baseConfigData != null) {
                PatchData patchData = baseConfigData.getPatchData();
                if (patchData != null) {
                    if (previousPatchData == null) {
                        if (patchData.isPatchNeed())//如果没有已下载好的补丁，并且当前补丁需要加载，去下载补丁{
                            downPatch(patchData);
                    }
                } else {
                    int previousId = previousPatchData.getPatchId();
                    int currentId = patchData.getPatchId();
                    if (previousId == currentId) {
                        //如果补丁文件已存在，并且是相同的补丁id,更新已存在补丁信息
                        downPatch(patchData);
                        if (patchData.isPatchNeed()) {
                            //需要加载当前补丁
                            patchData.setPatchFile(previousPatchData.getPatchFile());
                            loadPatch(patchData);
                        }
                    } else {   //如果已下载过的补丁和当前补丁id不同
                        loadPatch(previousPatchData);//先加载老补丁
                        downPatch(patchData);//下载新的补丁，下次重启app才能加载新补丁
                    }
                }
            }
        } else {
            //如果获取当前补丁信息失败，尝试加载老补丁
            loadPatch(previousPatchData);
        }
    }

}
