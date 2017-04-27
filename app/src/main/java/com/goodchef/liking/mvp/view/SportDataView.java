package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.http.result.SportDataResult;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:42
 * version 1.0.0
 */

public interface SportDataView extends BaseView {
    void updateSendSportDataView();

    void updateGetSportDataView(SportDataResult.SportData sportData);
}
