package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.BodyAnalyzeHistoryResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.BodyAnalyzeHistoryView;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午7:03
 * version 1.0.0
 */

public class BodyAnalyzeHistoryPresenter extends BasePresenter<BodyAnalyzeHistoryView> {
    public BodyAnalyzeHistoryPresenter(Context context, BodyAnalyzeHistoryView mainView) {
        super(context, mainView);
    }

    public void getBodyAnalyzeHistory(String column) {
        LiKingApi.getBodyHistoryList(column, new RequestCallback<BodyAnalyzeHistoryResult>() {
            @Override
            public void onSuccess(BodyAnalyzeHistoryResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateBodyAnalyzeHistoryView(result.getData());
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
