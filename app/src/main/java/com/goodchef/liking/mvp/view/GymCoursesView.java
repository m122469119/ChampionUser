package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.GymCoursesResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/3 下午8:16
 */
public interface GymCoursesView extends BaseView{
    void updateGymCoursesView(GymCoursesResult.GymCoursesData gymCoursesData);
}
