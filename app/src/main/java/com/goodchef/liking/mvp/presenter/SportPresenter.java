package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.goodchef.liking.http.result.LikingResult;
import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.SportDataResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.SportDataView;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:45
 * version 1.0.0
 */

public class SportPresenter extends BasePresenter<SportDataView> {
    public SportPresenter(Context context, SportDataView mainView) {
        super(context, mainView);
    }

    public void sendSportData(String sportData ,String deviceId) {
        LiKingApi.sendSportData(sportData, deviceId, new RequestCallback<LikingResult>() {
            @Override
            public void onSuccess(LikingResult likingResult) {
                if (LiKingVerifyUtils.isValid(mContext, likingResult)) {
                    mView.updateSendSportDataView();
                } else {
                    mView.showToast(likingResult.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {

            }
        });
    }

    public void getSortData() {
        LiKingApi.getSportData(new RequestCallback<SportDataResult>() {
            @Override
            public void onSuccess(SportDataResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateGetSportDataView(result.getData());
                } else {
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {

            }
        });
    }
}
