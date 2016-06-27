package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.GymListResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.ChangeShopView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/27 上午9:55
 */
public class ChangeShopPresenter extends BasePresenter<ChangeShopView> {
    public ChangeShopPresenter(Context context, ChangeShopView mainView) {
        super(context, mainView);
    }

    public void getShopList(String userCityId, String goodInfo) {
        LiKingApi.getGymList(userCityId, goodInfo, new RequestUiLoadingCallback<GymListResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(GymListResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateChangeShopView(result.getGymData());
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }
}
