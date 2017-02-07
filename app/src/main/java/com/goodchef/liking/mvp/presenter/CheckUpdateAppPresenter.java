package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.CheckUpdateAppResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.CheckUpdateAppView;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:01
 * version 1.0.0
 */

public class CheckUpdateAppPresenter extends BasePresenter<CheckUpdateAppView> {
    public CheckUpdateAppPresenter(Context context, CheckUpdateAppView mainView) {
        super(context, mainView);
    }

    public void getUpdateApp() {
        LiKingApi.getUpdateApp(new RequestCallback<CheckUpdateAppResult>() {
            @Override
            public void onSuccess(CheckUpdateAppResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateCheckUpdateAppView(result.getData());
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {

            }
        });
    }
}
