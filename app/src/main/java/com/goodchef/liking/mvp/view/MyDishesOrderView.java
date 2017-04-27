package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.http.result.data.PayResultData;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/29 上午10:16
 */
public interface MyDishesOrderView extends BaseView{
    void updateMyDishesPayView(PayResultData payResultData);
    void updateCancelDishesOrder();
    void updateCompleteDishesOrder();
}
