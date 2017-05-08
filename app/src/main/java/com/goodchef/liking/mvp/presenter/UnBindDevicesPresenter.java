package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.goodchef.liking.http.result.LikingResult;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.UnBindDevicesView;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午6:39
 * version 1.0.0
 */

public class UnBindDevicesPresenter extends BasePresenter<UnBindDevicesView> {

    public UnBindDevicesPresenter(Context context, UnBindDevicesView mainView) {
        super(context, mainView);
    }

    public void unBindDevices(String devicesId) {
        LiKingApi.unBindDevices(devicesId, new RequestCallback<LikingResult>() {
            @Override
            public void onSuccess(LikingResult likingResult) {
                if (LiKingVerifyUtils.isValid(mContext, likingResult)) {
                    mView.updateUnBindDevicesView();
                } else {
                    mView.showToast(likingResult.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {

            }
        });
    }
}
