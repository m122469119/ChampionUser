package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.CouponsCities;

/**
 * Created on 2017/3/15
 * Created by sanfen
 *
 * @version 1.0.0
 */

public interface CouponsCitysView extends BaseView {
    void updateCouponData(CouponsCities.DataBean data);
}
