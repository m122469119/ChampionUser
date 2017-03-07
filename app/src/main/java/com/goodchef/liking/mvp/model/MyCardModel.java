package com.goodchef.liking.mvp.model;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午6:01
 */

public class MyCardModel {

    /**
     * 获取我的会员卡
     * @param requestCallback
     */
    public void getMyCard(RequestCallback requestCallback) {
        LiKingApi.getMyCard(Preference.getToken(), requestCallback);
    }

    /**
     * 我的会员卡详情
     * @param orderId
     * @param requestCallback
     */
    public void getCardDetails(String orderId, RequestCallback requestCallback) {
        LiKingApi.getCardDetails(Preference.getToken(), orderId, requestCallback);
    }
}
