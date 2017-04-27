package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.http.result.ConfirmBuyCardResult;
import com.goodchef.liking.http.result.data.PayResultData;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/29 下午4:47
 */
public interface ConfirmBuyCardView extends BaseNetworkLoadView{
    void updateConfirmBuyCardView(ConfirmBuyCardResult.ConfirmBuyCardData confirmBuyCardData);
    void updateSubmitPayView(PayResultData payResultData);
    void updateErrorView(String errorMessage);
}
