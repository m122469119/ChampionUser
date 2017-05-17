package com.goodchef.liking.module.card;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.module.data.remote.RxUtils;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.CardResult;
import com.goodchef.liking.http.result.MyCardResult;
import com.goodchef.liking.http.result.MyOrderCardDetailsResult;
import com.goodchef.liking.http.result.OrderCardListResult;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.module.data.remote.LikingNewApi;

import io.reactivex.Observable;

/**
 * Created on 2017/04/25
 * desc: 会员购卡、购卡订单
 *
 * @author: chenlei
 * @version:1.0
 */

public class CardModel extends BaseModel {

    /**
     * 健身卡列表
     * @param longitude
     * @param latitude
     * @param cityId
     * @param districtId
     * @param gymId
     * @param type
     */
    public Observable<CardResult> getCardList(String longitude, String latitude, String cityId, String districtId, String gymId, int type) {
        return LikingNewApi.getInstance().getCardList(UrlList.sHostVersion, LikingPreference.getToken(), longitude, latitude, cityId, districtId, gymId, type)
                .compose(RxUtils.<CardResult>applyHttpSchedulers());
    }

    /**
     * 获取我的会员卡
     * @return
     */
    public Observable<MyCardResult> getMyCard() {
        return LikingNewApi.getInstance().getMyCard(UrlList.sHostVersion, LikingPreference.getToken())
                .compose(RxUtils.<MyCardResult>applyHttpSchedulers());
    }

    /**
     * 我的会员卡订单详情
     * @param orderId
     * @return
     */
    public Observable<MyOrderCardDetailsResult> getMyOrderCardDetails(String orderId) {
        return LikingNewApi.getInstance().getMyOrderCardDetails(UrlList.sHostVersion, LikingPreference.getToken(), orderId)
                .compose(RxUtils.<MyOrderCardDetailsResult>applyHttpSchedulers());
    }

    /***
     * 获取我的会员卡订单列表
     *
     * @param page     页数
     * @return
     */
    public Observable<OrderCardListResult> getCardOrderList(int page) {
        return LikingNewApi.getInstance().getCardOrderList(UrlList.sHostVersion, LikingPreference.getToken(), page)
                .compose(RxUtils.<OrderCardListResult>applyHttpSchedulers());
    }
}
