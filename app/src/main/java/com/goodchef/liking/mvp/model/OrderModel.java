package com.goodchef.liking.mvp.model;

import com.aaron.android.framework.base.widget.refresh.PagerRequestCallback;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author: chenlei
 * Time: 上午11:24
 */

public class OrderModel {

    public void getCardOrderList(int page, PagerRequestCallback requestCallback) {
        LiKingApi.getCardOrderList(Preference.getToken(), page, requestCallback);
    }
}
