package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.mvp.view.CouponsDetailsView;

/**
 * Created on 2017/3/10
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsDetailsPresenter extends BasePresenter<CouponsDetailsView> {
    public CouponsDetailsPresenter(Context context, CouponsDetailsView mainView) {
        super(context, mainView);
    }
}
