package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.mvp.view.ChangeCityView;

/**
 * @author MZ
 * @email sanfenruxi1@163.com
 * @date 2017/2/17.
 */

public class ChangeCityPresenter extends BasePresenter<ChangeCityView> {

    public ChangeCityPresenter(Context context, ChangeCityView mainView) {
        super(context, mainView);
    }

    public void getCitySearch(String search){

    }

}
