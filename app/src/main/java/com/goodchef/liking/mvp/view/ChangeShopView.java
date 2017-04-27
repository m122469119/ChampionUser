package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.http.result.GymListResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/27 上午9:53
 */
public interface ChangeShopView extends BaseView {
    void updateChangeShopView(GymListResult.GymData gymData);
}
