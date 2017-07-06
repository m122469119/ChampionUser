package com.goodchef.liking.module.card.buy.confirm;

import android.content.Context;
import android.content.DialogInterface;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.LiKingRequestCode;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.ConfirmBuyCardResult;
import com.goodchef.liking.data.remote.retrofit.result.SubmitPayResult;
import com.goodchef.liking.data.remote.retrofit.result.data.PayResultData;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;
import com.goodchef.liking.data.remote.rxobserver.StateViewLoadingObserver;
import com.goodchef.liking.eventmessages.BuyCardListMessage;

import java.util.List;

/**
 * Created on 2017/05/19
 * desc: 购卡确认页面
 *
 * @author: chenlei
 * @version:1.0
 */

interface BuyCardConfirmContract {

    interface View extends BaseStateView {
        void updateConfirmBuyCardView(ConfirmBuyCardResult.DataBean confirmBuyCardData);

        void updateSubmitPayView(PayResultData payResultData);

        void updateErrorView(String errorMessage);

        void showBuyCardConfirmDialog(String message);

        void setCardTimeAdapter(List<ConfirmBuyCardResult.DataBean.CardsBean.TimeLimitBean> mTimeLimitBeanList);
    }

    class Presenter extends RxBasePresenter<View> {

        private BuyCardConfirmModel mModel = null;

        public Presenter() {
            mModel = new BuyCardConfirmModel();
        }

        /**
         * 购卡确认
         *
         */
        public void confirmBuyCard() {

            mModel.confirmCard()
                    .subscribe(addObserverToCompositeDisposable(new StateViewLoadingObserver<ConfirmBuyCardResult>(mView) {
                        @Override
                        public void onNext(ConfirmBuyCardResult value) {
                            super.onNext(value);
                            if (value == null) return;
                            mView.updateConfirmBuyCardView(value.getData());
                            setCardTimeAdapter();
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            switch (apiException.getErrorCode()) {
                                case LiKingRequestCode.BUY_CARD_CONFIRM:
                                    mView.showBuyCardConfirmDialog(apiException.getErrorMessage());
                                    break;
                                case LiKingRequestCode.NO_GYM:
                                case LiKingRequestCode.HAS_OTHER_GYM_CARD:
                                    mView.updateErrorView(apiException.getErrorMessage());
                                    break;
                                default:
                                    super.apiError(apiException);
                            }
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            super.networkError(throwable);
                        }
                    }));
        }

        private void setCardTimeAdapter() {
            mView.setCardTimeAdapter(mModel.mTimeLimitBeanList);
        }

        /***
         * 提交买卡订单
         *
         * @param couponCode 优惠券code
         * @param payType    支付方式
         */
        public void submitBuyCardData(Context context, String couponCode, String payType) {
            mModel.submitBuyCardData(couponCode, payType)
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<SubmitPayResult>(context, R.string.loading, mView) {
                        @Override
                        public void onNext(SubmitPayResult value) {
                            if (value == null) return;
                            mView.updateSubmitPayView(value.getPayData());
                        }
                    }));
        }

        public void setIntentParams(String cardName, String categoryId, int buyType, String gymId, String cardId) {
            mModel.setIntentParams(cardName, categoryId,  buyType, gymId, cardId);

        }

        public String getCardId() {
            return mModel.mCardId;
        }

        public String getSubmitGymId() {
            return mModel.mSubmitGymId;
        }

        public String getCardName() {
            return mModel.mCardName;
        }

        public int getBuyType() {
            return mModel.mBuyType;
        }
    }


}
