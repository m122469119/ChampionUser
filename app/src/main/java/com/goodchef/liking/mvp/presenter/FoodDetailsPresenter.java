package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.FoodDetailsResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.FoodDetailsView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/22 上午11:33
 */
public class FoodDetailsPresenter extends BasePresenter<FoodDetailsView> {
    public FoodDetailsPresenter(Context context, FoodDetailsView mainView) {
        super(context, mainView);
    }

    public void getFoodDetails(String useCityId, String goodsId) {
        LiKingApi.getFoodDetails(useCityId, goodsId, new RequestCallback<FoodDetailsResult>() {
            @Override
            public void onSuccess(FoodDetailsResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateFoodDetailsView(result.getData());
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
