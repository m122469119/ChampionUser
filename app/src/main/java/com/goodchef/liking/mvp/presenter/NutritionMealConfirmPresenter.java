package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.http.code.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.NutritionMealConfirmResult;
import com.goodchef.liking.http.result.SubmitPayResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.NutritionMealConfirmView;
import com.goodchef.liking.module.data.local.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/23 下午8:59
 */
public class NutritionMealConfirmPresenter extends BasePresenter<NutritionMealConfirmView> {
    public NutritionMealConfirmPresenter(Context context, NutritionMealConfirmView mainView) {
        super(context, mainView);
    }

    public void confirmFood(String userCityId, String goodInfo) {
        LiKingApi.confirmFood(Preference.getToken(), userCityId, goodInfo, new RequestUiLoadingCallback<NutritionMealConfirmResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(NutritionMealConfirmResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateNutritionMealConfirmView(result.getConfirmData());
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

    public void submitFoodOrder(String gymId, String takeTime, String couponCode, String goodInfo, String payType) {
        LiKingApi.submitDishesOrder(Preference.getToken(), gymId, takeTime, couponCode, goodInfo, payType, new RequestUiLoadingCallback<SubmitPayResult>(mContext, R.string.loading) {
            @Override
            public void onSuccess(SubmitPayResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateSubmitOrderView(result.getPayData());
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
