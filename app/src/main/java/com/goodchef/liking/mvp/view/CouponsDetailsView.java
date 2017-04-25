package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.CouponsDetailsResult;

/**
 * Created on 2017/3/10
 * Created by sanfen
 *
 * @version 1.0.0
 */

public interface CouponsDetailsView extends BaseView {
    void updateCouponData(CouponsDetailsResult.DataBean couponData);
}
