package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.BodyTestResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.BodyTestView;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:26
 * version 1.0.0
 */

public class BodyTestPresenter extends BasePresenter<BodyTestView> {
    public BodyTestPresenter(Context context, BodyTestView mainView) {
        super(context, mainView);
    }

    public void getBodyData(String bodyId) {
        LiKingApi.getBodyTestData(bodyId, new RequestUiLoadingCallback<BodyTestResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(BodyTestResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateBodyTestView(result.getData());
                } else {
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
                mView.handleNetworkFailure();
            }
        });
    }

}
