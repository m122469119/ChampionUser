package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.CheckGymListResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.CheckGymView;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/12 下午2:20
 */
public class CheckGymPresenter extends BasePresenter<CheckGymView> {

    public CheckGymPresenter(Context context, CheckGymView mainView) {
        super(context, mainView);
    }

    public void getGymList(int cityId, String longitude, String latitude) {
        LiKingApi.getCheckGymList(Preference.getToken(), cityId, longitude, latitude, new RequestCallback<CheckGymListResult>() {
            @Override
            public void onSuccess(CheckGymListResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateCheckGymView(result.getData());
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
