package com.goodchef.liking.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.ui.BaseActivity;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.InitApiFinishedMessage;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.data.PatchData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.utils.NavigationBarUtil;
import com.goodchef.liking.utils.PatchDowner;

import cn.jiajixin.nuwa.Nuwa;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/8 上午10:15
 */
public class LoadingActivity extends BaseActivity {
    private final int DURATION = 1500;
    private Handler handler = new Handler();
    private PatchData previousPatchData;
    private LinearLayout mCompleteLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mCompleteLayout = (LinearLayout) findViewById(R.id.company_info);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        previousPatchData = Preference.getPatchData();
        if (EnvironmentUtils.Network.isNetWorkAvailable()) {
            LiKingVerifyUtils.initApi(this);
        }
        setCompleteLayoutView();
    }

    private void setCompleteLayoutView() {
        WindowManager wmManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        boolean hasSoft = NavigationBarUtil.hasSoftKeys(wmManager);//判断是否有虚拟键盘
        if (hasSoft) {
            int navigationBarHeight = NavigationBarUtil.getNavigationBarHeight(this);//获取虚拟键盘的高度
            //这一行很重要，将dialog对话框设置在虚拟键盘上面
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mCompleteLayout.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, (navigationBarHeight + 30));
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mCompleteLayout.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 30);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Preference.isNewVersion()) {
                    Preference.setIsUpdateApp(true);//设置可以弹出更新对话框
                    jumpToGuideActivity();
                } else {
                    jumpToMainActivity();
                }
            }
        }, DURATION);
    }

    private void jumpToGuideActivity() {
        Intent guideIntent = new Intent(this, GuideActivity.class);
        startActivity(guideIntent);
        finish();
    }

    private void jumpToMainActivity() {
        Intent intent = new Intent(this, LikingHomeActivity.class);
        startActivity(intent);
        finish();
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
            BaseConfigResult baseConfigResult = LiKingVerifyUtils.sBaseConfigResult;
            if (baseConfigResult != null) {
                BaseConfigResult.BaseConfigData baseConfigData = baseConfigResult.getBaseConfigData();
                if (baseConfigData != null) {
                    PatchData patchData = baseConfigData.getPatchData();
                    if (patchData != null) {
                        if (previousPatchData == null) {
                            if (patchData.isPatchNeed())//如果没有已下载好的补丁，并且当前补丁需要加载，去下载补丁
                                downPatch(patchData);
                        } else {
                            int previousId = previousPatchData.getPatchId();
                            int currentId = patchData.getPatchId();
                            if (previousId == currentId) {
                                if (patchData.isPatchNeed()) {
                                    //需要加载当前补丁
                                    patchData.setPatchFile(previousPatchData.getPatchFile());
                                    loadPatch(patchData);
                                } else {
                                    loadPatch(previousPatchData);//先加载老补丁
                                    downPatch(patchData);//下载新的补丁，下次重启app才能加载新补丁
                                }
                            } else {   //如果已下载过的补丁和当前补丁id不同
                                loadPatch(previousPatchData);//先加载老补丁
                                downPatch(patchData);//下载新的补丁，下次重启app才能加载新补丁
                            }
                        }
                    }
                }
            } else {
                //如果获取当前补丁信息失败，尝试加载老补丁
                loadPatch(previousPatchData);
            }
        } else {
            //如果获取当前补丁信息失败，尝试加载老补丁
            loadPatch(previousPatchData);
        }
    }

}
