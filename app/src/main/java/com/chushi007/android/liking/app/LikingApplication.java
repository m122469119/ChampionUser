package com.chushi007.android.liking.app;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.chushi007.android.liking.BuildConfig;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingApplication extends BaseApplication {
    @Override
    protected void initialize() {
        LogUtils.i(TAG, "initialize---");
    }

    @Override
    protected void backgroundInitialize() {
        LogUtils.i(TAG, "backgroundInitialize---");
    }

    @Override
    protected EnvironmentUtils.Config.ConfigData buildConfigData() {
        EnvironmentUtils.Config.ConfigData configData = new EnvironmentUtils.Config.ConfigData();
        configData.setAppFlag(BuildConfig.APP_FLAG);
        configData.setAppVersionCode(BuildConfig.VERSION_CODE);
        configData.setAppVersionName(BuildConfig.VERSION_NAME);
        configData.setIsTestMode(BuildConfig.TEST_MODE);
        configData.setUrlHost(BuildConfig.HTTP_HOST);
        return configData;
    }
}
