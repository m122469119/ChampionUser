package com.aaron.android.framework.base;

import android.content.Context;

import com.aaron.android.framework.base.storage.DiskStorageManager;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.LogUtils;

/**
 * Created on 17/2/8.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class MainProcessInit implements ProcessInit {
    private static final String TAG = "MainProcessInit";
    private final Context mContext;
    private final EnvironmentUtils.Config.ConfigData mConfitData;

    public MainProcessInit(Context context, EnvironmentUtils.Config.ConfigData configData) {
        mContext = context;
        mConfitData = configData;
    }

    @Override
    public void init() {
        /*环境配置信息初始化*/
        EnvironmentUtils.init(mContext, mConfitData);
        /*资源Utils初始化*/
        ResourceUtils.init(mContext);
        /*设备信息初始化*/
        DisplayUtils.init(mContext);
        /*根据配置信息判断是否打印日志信息*/
        LogUtils.setEnable(EnvironmentUtils.Config.isTestMode());
        /*打印应用版本和设备屏幕相关信息*/
        printInfo();
    }

    private void printInfo() {
        LogUtils.d(TAG, "\n---ApkInfo---\nappFlag: " + EnvironmentUtils.Config.getAppFlag() + "\nappVersionName: " + EnvironmentUtils.Config.getAppVersionName() +
                "\nappVersionCode: " + EnvironmentUtils.Config.getAppVersionCode() + "\nappHostUrl: " + EnvironmentUtils.Config.getHttpRequestUrlHost());
        LogUtils.d(TAG, "\n---ScreenInfo---\nscreenWidth: " + DisplayUtils.getWidthPixels() + "\nscreenHeight: " + DisplayUtils.getHeightPixels()
                + "\ndensityDpi: " + DisplayUtils.getDensityDpi() + "\ndensity: " + DisplayUtils.getDensity() + "\ndensityStr: " + DisplayUtils.getBitmapDensityStr());
    }


}
