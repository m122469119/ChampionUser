package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseNetworkLoadView;
import com.goodchef.liking.http.result.ChargeGroupConfirmResult;
import com.goodchef.liking.http.result.data.PayResultData;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/25 上午10:14
 */
public interface ChargeGroupCoursesView extends BaseNetworkLoadView {
    void updateChargeGroupCoursesView(ChargeGroupConfirmResult.ChargeGroupConfirmData chargeGroupConfirmData);
    void updatePaySubmitView(PayResultData payResultData);
    void updateBuyCoursesErrorView();
}
