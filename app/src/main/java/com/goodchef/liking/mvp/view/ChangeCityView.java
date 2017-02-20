package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.eventbus.BaseMessage;
import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.data.City;

import java.util.List;

/**
 * @author MZ
 * @email sanfenruxi1@163.com
 * @date 2017/2/17.
 */

public interface ChangeCityView extends BaseView{

    void showCityListWindow(List<City.RegionsData.CitiesData> list);

    void dismissWindow();

    void setLocationCityNameTextViewText(String text);

    CharSequence getLocationCityNameTextViewText();

    void setTitle(String text);

    void postEvent(BaseMessage object);

    void finish();

    String getDefaultCityName();
}
