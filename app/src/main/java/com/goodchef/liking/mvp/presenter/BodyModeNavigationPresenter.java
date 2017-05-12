package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.BodyModelNavigationResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.BodyModelNavigationView;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午12:20
 * version 1.0.0
 */

public class BodyModeNavigationPresenter extends BasePresenter<BodyModelNavigationView> {
    public BodyModeNavigationPresenter(Context context, BodyModelNavigationView mainView) {
        super(context, mainView);
    }

    public void getBodyModeNavigation(String modules) {
        LiKingApi.getBodyHistoryTitleList(modules, new RequestCallback<BodyModelNavigationResult>() {
            @Override
            public void onSuccess(BodyModelNavigationResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateBodyModelNavigationView(result.getData());
                } else {
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                mView.handleNetworkFailure();
            }
        });
    }
}
