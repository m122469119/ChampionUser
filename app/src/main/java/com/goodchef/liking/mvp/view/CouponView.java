package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.http.result.CouponsPersonResult;
import com.goodchef.liking.http.result.CouponsResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 下午2:12
 */
public interface CouponView extends BaseView{
    void updateCouponData(CouponsResult.CouponData couponData);

    void updateMyCouponData(CouponsPersonResult.DataBean dataBean);
}
