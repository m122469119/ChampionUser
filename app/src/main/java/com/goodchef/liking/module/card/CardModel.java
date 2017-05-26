package com.goodchef.liking.module.card;

import android.text.TextUtils;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.CardResult;
import com.goodchef.liking.data.remote.retrofit.result.ConfirmBuyCardResult;
import com.goodchef.liking.data.remote.retrofit.result.MyCardResult;
import com.goodchef.liking.data.remote.retrofit.result.MyOrderCardDetailsResult;
import com.goodchef.liking.data.remote.retrofit.result.OrderCardListResult;
import com.goodchef.liking.data.remote.retrofit.result.SubmitPayResult;

import java.util.HashMap;
import java.util.Map;

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
     *
     * @param longitude
     * @param latitude
     * @param cityId
     * @param districtId
     * @param gymId
     * @param type
     */
    public Observable<CardResult> getCardList(String longitude, String latitude, String cityId, String districtId, String gymId, int type) {
        Map<String, String> map = new HashMap<>();
        String token = LikingPreference.getToken();
        if (!TextUtils.isEmpty(token)) {
            map.put("token", token);
        }
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("city_id", cityId);
        map.put("district_id", districtId);
        map.put("gym_id", gymId);
        map.put("type", String.valueOf(type));
        return LikingNewApi.getInstance().getCardList(LikingNewApi.sHostVersion, map)
                .compose(RxUtils.<CardResult>applyHttpSchedulers());
    }

    /**
     * 获取我的会员卡
     *
     * @return
     */
    public Observable<MyCardResult> getMyCard() {
        return LikingNewApi.getInstance().getMyCard(LikingNewApi.sHostVersion, LikingPreference.getToken())
                .compose(RxUtils.<MyCardResult>applyHttpSchedulers());
    }

    /**
     * 我的会员卡订单详情
     *
     * @param orderId
     * @return
     */
    public Observable<MyOrderCardDetailsResult> getMyOrderCardDetails(String orderId) {
        return LikingNewApi.getInstance().getMyOrderCardDetails(LikingNewApi.sHostVersion, LikingPreference.getToken(), orderId)
                .compose(RxUtils.<MyOrderCardDetailsResult>applyHttpSchedulers());
    }

    /***
     * 获取我的会员卡订单列表
     *
     * @param page     页数
     * @return
     */
    public Observable<OrderCardListResult> getCardOrderList(int page) {
        return LikingNewApi.getInstance().getCardOrderList(LikingNewApi.sHostVersion, LikingPreference.getToken(), page)
                .compose(RxUtils.<OrderCardListResult>applyHttpSchedulers());
    }

    /***
     * 确认购卡
     *
     * @param type       类型 1购卡页 2续卡 3升级卡
     * @param categoryId 类别ID
     */
    public Observable<ConfirmBuyCardResult> confirmCard(int type, int categoryId, String gymId) {
        Map<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(type));
        map.put("category_id", String.valueOf(categoryId));
        map.put("gym_id", gymId);
        String token = LikingPreference.getToken();
        if (!TextUtils.isEmpty(token)) {
            map.put("token", token);
        }
        return LikingNewApi.getInstance().cardConfirm(LikingNewApi.sHostVersion, map)
                .compose(RxUtils.<ConfirmBuyCardResult>applyHttpSchedulers());
    }

    /***
     * 提交买卡订单
     *
     * @param cardId     cardId
     * @param type       类型 1购卡页 2续卡 3升级卡
     * @param couponCode 优惠券code
     * @param payType    支付方式
     */
    public Observable<SubmitPayResult> submitBuyCardData(int cardId, int type, String couponCode, String payType, String gymId) {
        Map<String, String> map = new HashMap<>();
        map.put("token", LikingPreference.getToken());
        map.put("card_id", String.valueOf(cardId));
        map.put("type", String.valueOf(type));
        map.put("coupon_code", couponCode);
        map.put("pay_type", payType);
        map.put("gym_id", gymId);
        return LikingNewApi.getInstance().submitBuyCardData(LikingNewApi.sHostVersion, map)
                .compose(RxUtils.<SubmitPayResult>applyHttpSchedulers());
    }
}
