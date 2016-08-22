package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.MyChargeGroupCoursesDetailsResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/22 下午3:56
 */
public interface MyChargeGroupCoursesDetailsView extends BaseView {
    void updateMyChargeGroupCoursesDetailsView(MyChargeGroupCoursesDetailsResult.MyChargeGroupCoursesDetails groupCoursesDetails);
}
