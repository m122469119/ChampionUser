package com.goodchef.liking.module.loading;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aaron.android.framework.base.ui.BaseActivity;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.LikingBaseRequestHelper;
import com.goodchef.liking.data.remote.retrofit.result.BaseConfigResult;
import com.goodchef.liking.data.remote.retrofit.result.data.PatchData;
import com.goodchef.liking.eventmessages.InitApiFinishedMessage;
import com.goodchef.liking.module.guide.GuideActivity;
import com.goodchef.liking.module.home.LikingHomeActivity;
import com.goodchef.liking.utils.NavigationBarUtil;
import com.goodchef.liking.utils.NumberConstantUtil;
import com.goodchef.liking.utils.PatchDowner;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * 说明:启动页
 * Author shaozucheng
 * Time:16/7/8 上午10:15
 */
public class LoadingActivity extends BaseActivity {
    private final int DURATION = 1500;
    public static String STRING_NULL = "";
    private Handler handler = new Handler();
    private PatchData previousPatchData;
    private LinearLayout mCompleteLayout;
    private RxPermissions mRxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mRxPermissions = new RxPermissions(this);
        mCompleteLayout = (LinearLayout) findViewById(R.id.company_info);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        previousPatchData = LikingPreference.getPatchData();
        setCompleteLayoutView();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int number = clearToken();
                if (number < 0) {
                    LikingPreference.setToken(STRING_NULL);
                }
                if (LikingPreference.isNewVersion()) {
                    LikingPreference.setIsUpdateApp(true);//设置可以弹出更新对话框
                    jumpToGuideActivity();
                } else {
                    jumpToMainActivity();
                }
            }
        }, DURATION);
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

    /**
     * 1、4.2之前的版本都要清空token，因为1.4.2之后的版本登录后的规则发生改变
     * 如果是<0都要将前面的token清空
     */
    private int clearToken() {
        String appVersion = LikingPreference.getAppVersion();
        String currentVersion = "1.4.5";
        LogUtils.i(TAG, "lastappVersion== " + appVersion + "currentVersion == " + currentVersion);
        if (!StringUtils.isEmpty(appVersion)) {
            String lastversion[] = appVersion.split("\\.");
            String currentversion[] = currentVersion.split("\\.");

            //将数组转为list集合
            ArrayList<String> lastVersionList = new ArrayList<>();
            for (int i = 0; i < lastversion.length; i++) {
                lastVersionList.add(lastversion[i]);
            }

            ArrayList<String> currentVersionList = new ArrayList<>();
            for (int i = 0; i < currentversion.length; i++) {
                currentVersionList.add(currentversion[i]);
            }

            int length = currentVersionList.size() - lastVersionList.size();
            if (length > 0) {
                lastVersionList.add(NumberConstantUtil.STR_ZERO);
            } else if (length < 0) {
                currentVersionList.add(NumberConstantUtil.STR_ZERO);
            }
            for (int i = 0; i < currentVersionList.size(); i++) {
                int number = Integer.parseInt(lastVersionList.get(i)) - Integer.parseInt(currentVersionList.get(i));
                if (number != 0) {
                    return number / Math.abs(number);
                }
            }
        }
        return 0;
    }

    /**
     * 跳转到引导页
     */
    private void jumpToGuideActivity() {
        Intent guideIntent = new Intent(this, GuideActivity.class);
        startActivity(guideIntent);
        finish();
    }

    /**
     * 跳转到首页
     */
    private void jumpToMainActivity() {
        Intent intent = new Intent(this, LikingHomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadPatch(PatchData patchData) {
        if (patchData != null && patchData.isPatchNeed() && !TextUtils.isEmpty(patchData.getPatchFile())) {
            LogUtils.i("Dust", "加载补丁");
            TinkerInstaller.onReceiveUpgradePatch(this.getApplication(), previousPatchData.getPatchFile());
        } else {
            LogUtils.i("Dust", "不加载补丁");
        }
    }

    /**
     * 下载补丁，或更新已存在补丁状态
     *
     * @param patchData
     */
    private void downPatch(final PatchData patchData) {
        mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) return;
                        PatchDowner patchDowner = new PatchDowner(getApplicationContext());
                        patchDowner.execute(patchData);
                    }
                });
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(InitApiFinishedMessage initApiFinishedMessage) {
        if (initApiFinishedMessage.isSuccess()) {
            BaseConfigResult baseConfigResult = LikingBaseRequestHelper.sBaseConfigResult;
            if (baseConfigResult != null) {
                BaseConfigResult.ConfigData baseConfigData = baseConfigResult.getBaseConfigData();
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
