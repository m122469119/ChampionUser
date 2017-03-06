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

    public void getMyCard(RequestCallback requestCallback) {
        LiKingApi.getMyCard(Preference.getToken(), requestCallback);
    }
}
