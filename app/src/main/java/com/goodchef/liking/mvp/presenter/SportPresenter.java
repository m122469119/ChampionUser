package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.codelibrary.utils.SecurityUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.DeviceUtils;
import com.aaron.android.framework.utils.PopupUtils;
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

    public void sendSportData(String sportData) {
        LiKingApi.sendSportData(sportData, SecurityUtils.MD5.get16MD5String(DeviceUtils.getDeviceInfo(BaseApplication.getInstance())), new RequestCallback<BaseResult>() {
            @Override
            public void onSuccess(BaseResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateSendSportDataView();
                } else {
                    PopupUtils.showToast(result.getMessage());
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
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {

            }
        });
    }
}
