package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseNetworkLoadView;
import com.goodchef.liking.http.result.FoodDetailsResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/22 上午11:31
 */
public interface FoodDetailsView extends BaseNetworkLoadView {
    void updateFoodDetailsView(FoodDetailsResult.FoodDetailsData foodDetailsData);
}
