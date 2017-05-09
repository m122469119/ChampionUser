package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.LikingResult;
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
        LiKingApi.bindDevices(braceletName, braceletVersion, deviceId, platform, deviceName, osVersion, new RequestCallback<LikingResult>() {
            @Override
            public void onSuccess(LikingResult likingResult) {
                if (LiKingVerifyUtils.isValid(mContext, likingResult)) {
                    mView.updateBindDevicesView();
                } else {
                    mView.updateBindDevicesView();
                    mView.showToast(likingResult.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {

            }
        });
    }
}
