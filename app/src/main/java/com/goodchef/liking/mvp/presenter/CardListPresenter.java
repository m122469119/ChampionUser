package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.CardResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.CardListView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/17 下午12:32
 */
public class CardListPresenter extends BasePresenter<CardListView> {
    public CardListPresenter(Context context, CardListView mainView) {
        super(context, mainView);
    }

    public void getCardList() {
        LiKingApi.getCardList(new RequestUiLoadingCallback<CardResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(CardResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateCardListView(result.getCardData());
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }
}
