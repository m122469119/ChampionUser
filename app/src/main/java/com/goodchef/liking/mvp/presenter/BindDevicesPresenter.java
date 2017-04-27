package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.BindDevicesView;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午10:42
 * version 1.0.0
 */

public class BindDevicesPresenter extends BasePresenter<BindDevicesView> {
    public BindDevicesPresenter(Context context, BindDevicesView mainView) {
        super(context, mainView);
    }

    public void bindDevices(String braceletName, String braceletVersion, String deviceId, String platform, String deviceName, String osVersion) {
        LiKingApi.bindDevices(braceletName, braceletVersion, deviceId, platform, deviceName, osVersion, new RequestCallback<BaseResult>() {
            @Override
            public void onSuccess(BaseResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateBindDevicesView();
                } else {
                    mView.updateBindDevicesView();
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {

            }
        });
    }
}
