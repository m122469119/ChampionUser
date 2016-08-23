package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseNetworkLoadView;
import com.goodchef.liking.http.result.OrderCalculateResult;
import com.goodchef.liking.http.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.http.result.data.PayData;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 上午11:20
 */
public interface PrivateCoursesConfirmView extends BaseNetworkLoadView {
    void updatePrivateCoursesConfirm(PrivateCoursesConfirmResult.PrivateCoursesConfirmData coursesConfirmData);
<<<<<<< 24b4996511a7510e080dcd4e8c085770e1903a34
    void updateSubmitOrderCourses(PayData payData);
=======
    void updateSubmitOrderCourses(PayResultData payData);
    void updateOrderCalculate(boolean isSuccess, OrderCalculateResult.OrderCalculateData orderCalculateData);
>>>>>>> 简述：修改私教课确认购买页和加载本地图片
}
