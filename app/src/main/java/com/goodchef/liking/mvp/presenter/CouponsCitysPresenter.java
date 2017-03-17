package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.mvp.view.CouponsCitysView;

/**
 * Created on 2017/3/15
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsCitysPresenter extends BasePresenter<CouponsCitysView> {




    public CouponsCitysPresenter(Context context, CouponsCitysView mainView) {
        super(context, mainView);
    }



    public void requestCityPage(int page){

    }

}
