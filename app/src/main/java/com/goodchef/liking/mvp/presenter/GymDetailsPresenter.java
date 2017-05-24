package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.http.code.RequestError;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.GymDetailsResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.GymDetailsView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/7 下午4:37
 */
public class GymDetailsPresenter extends BasePresenter<GymDetailsView> {
    public GymDetailsPresenter(Context context, GymDetailsView mainView) {
        super(context, mainView);
    }

    public void getGymDetails(String gymId) {
        LiKingApi.getGymDetails(gymId, new RequestUiLoadingCallback<GymDetailsResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(GymDetailsResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateGymDetailsView(result.getData());
                } else {
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }
}
