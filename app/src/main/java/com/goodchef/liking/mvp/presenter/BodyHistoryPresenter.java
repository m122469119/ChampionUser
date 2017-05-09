package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.widget.refresh.BasePagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PagerRequestCallback;
import com.aaron.http.code.RequestError;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.BodyHistoryResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.BodyHistoryView;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午10:40
 * version 1.0.0
 */

public class BodyHistoryPresenter extends BasePresenter<BodyHistoryView> {

    public BodyHistoryPresenter(Context context, BodyHistoryView mainView) {
        super(context, mainView);
    }

    public void getHistoryData(int page, BasePagerLoaderFragment fragment) {
        LiKingApi.getBodyHistory(page, new PagerRequestCallback<BodyHistoryResult>(fragment) {
            @Override
            public void onSuccess(BodyHistoryResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateBodyHistoryView(result.getData());
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
