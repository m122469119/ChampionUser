package com.goodchef.liking.mvp.model;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.module.data.local.Preference;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午6:01
 */

public class CardModel {

    /**
     * 健身卡列表
     * @param longitude
     * @param latitude
     * @param cityId
     * @param districtId
     * @param gymId
     * @param type
     * @param requestCallback
     */
    public void getCardList(String longitude, String latitude, String cityId, String districtId, String gymId, int type, RequestCallback requestCallback) {
        LiKingApi.getCardList(Preference.getToken(), longitude, latitude, cityId, districtId, gymId, type, requestCallback);
    }

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
