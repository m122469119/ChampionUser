package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.http.result.MyOrderCardDetailsResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/1 下午3:07
 */
public interface MyCardDetailsView extends BaseNetworkLoadView{
    void updateMyCardDetailsView(MyOrderCardDetailsResult.OrderCardDetailsData orderCardDetailsData);
}
