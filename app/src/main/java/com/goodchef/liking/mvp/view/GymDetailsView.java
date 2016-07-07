package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.GymDetailsResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/7 下午4:36
 */
public interface GymDetailsView extends BaseView {
    void updateGymDetailsView(GymDetailsResult.GymDetailsData gymDetailsData);
}
