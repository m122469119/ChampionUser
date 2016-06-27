package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.NutritionMealConfirmResult;
import com.goodchef.liking.http.result.data.PayResultData;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/23 下午8:56
 */
public interface NutritionMealConfirmView extends BaseView {
    void updateNutritionMealConfirmView(NutritionMealConfirmResult.NutritionMealConfirmData confirmData);
    void updateSubmitOrderView(PayResultData payResultData);
}
