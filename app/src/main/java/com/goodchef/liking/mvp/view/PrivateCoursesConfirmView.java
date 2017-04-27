package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.http.result.OrderCalculateResult;
import com.goodchef.liking.http.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.http.result.data.PayResultData;


/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 上午11:20
 */
public interface PrivateCoursesConfirmView extends BaseNetworkLoadView {
    void updatePrivateCoursesConfirm(PrivateCoursesConfirmResult.PrivateCoursesConfirmData coursesConfirmData);
    void updateSubmitOrderCourses(PayResultData payData);
    void updateOrderCalculate(boolean isSuccess, OrderCalculateResult.OrderCalculateData orderCalculateData);
}
