package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.ConfirmBuyCardResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/29 下午4:47
 */
public interface ConfirmBuyCardView extends BaseView{
    void updateConfirmBuyCardView(ConfirmBuyCardResult.ConfirmBuyCardData confirmBuyCardData);
}
