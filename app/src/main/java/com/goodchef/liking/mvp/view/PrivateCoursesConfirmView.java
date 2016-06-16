package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.http.result.data.PayData;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 上午11:20
 */
public interface PrivateCoursesConfirmView extends BaseView {
    void updatePrivateCoursesConfirm(PrivateCoursesConfirmResult.PrivateCoursesConfirmData coursesConfirmData);
    void updateSubmitOrderCourses(PayData payData);
}
