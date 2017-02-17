package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;

/**
 * @author MZ
 * @email sanfenruxi1@163.com
 * @date 2017/2/17.
 */

public interface ChangeCityView extends BaseView{

    void setLocationCityNameTextViewText(String text);

    CharSequence getLocationCityNameTextViewText();

    void setTitle(String text);
}
