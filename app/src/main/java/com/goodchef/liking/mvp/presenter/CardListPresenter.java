package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.CardResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.CardListView;
import com.goodchef.liking.module.data.local.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/17 下午12:32
 */
public class CardListPresenter extends BasePresenter<CardListView> {
    public CardListPresenter(Context context, CardListView mainView) {
        super(context, mainView);
    }

    public void getCardList(String longitude, String latitude, String cityId, String districtId, String gymId, int type) {
        LiKingApi.getCardList(Preference.getToken(), longitude, latitude, cityId, districtId, gymId, type, new RequestCallback<CardResult>() {
            @Override
            public void onSuccess(CardResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateCardListView(result.getCardData());
                } else {
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                mView.handleNetworkFailure();
            }
        });
    }
}
