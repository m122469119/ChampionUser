package com.goodchef.liking.module.card.buy.confirm;

import android.content.Context;
import android.content.DialogInterface;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.BuyCardListMessage;
import com.goodchef.liking.data.remote.retrofit.result.ConfirmBuyCardResult;
import com.goodchef.liking.data.remote.retrofit.result.SubmitPayResult;
import com.goodchef.liking.data.remote.retrofit.result.data.PayResultData;
import com.goodchef.liking.data.remote.LiKingRequestCode;
import com.goodchef.liking.module.card.CardModel;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;

/**
 * Created on 2017/05/19
 * desc: 购卡确认页面
 *
 * @author: chenlei
 * @version:1.0
 */

public interface BuyCardConfirmContract {

    interface ConfirmBuyCardView extends BaseStateView {
        void updateConfirmBuyCardView(ConfirmBuyCardResult.ConfirmBuyCardData confirmBuyCardData);

        void updateSubmitPayView(PayResultData payResultData);

        void updateErrorView(String errorMessage);
    }

    class ConfirmBuyCardPresenter extends BasePresenter<ConfirmBuyCardView> {

        private CardModel mCardModel = null;

        public ConfirmBuyCardPresenter(Context context, ConfirmBuyCardView mainView) {
            super(context, mainView);
            mCardModel = new CardModel();
        }

        /**
         * 购卡确认
         *
         * @param type
         * @param categoryId
         * @param gymId
         */
        public void confirmBuyCard(int type, int categoryId, String gymId) {

            mCardModel.confirmCard(type, categoryId, gymId)
                    .subscribe(new LikingBaseObserver<ConfirmBuyCardResult>(mContext, mView) {
                        @Override
                        public void onNext(ConfirmBuyCardResult value) {
                            if (value == null) return;
                            mView.updateConfirmBuyCardView(value.getData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            switch (apiException.getErrorCode()) {
                                case LiKingRequestCode.BUY_CARD_CONFIRM:
                                    HBaseDialog.Builder builder = new HBaseDialog.Builder(mContext);
                                    builder.setMessage(apiException.getErrorMessage());
                                    builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (mContext != null && mContext instanceof BuyCardConfirmActivity) {
                                                dialog.dismiss();
                                                postEvent(new BuyCardListMessage());
                                                ((BuyCardConfirmActivity) mContext).finish();
                                            }
                                        }
                                    });
                                    builder.create().setCancelable(false);
                                    builder.create().show();
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
                            mView.changeStateView(StateView.State.FAILED);
                        }
                    });
        }

        /***
         * 提交买卡订单
         *
         * @param cardId     cardId
         * @param type       类型 1购卡页 2续卡 3升级卡
         * @param couponCode 优惠券code
         * @param payType    支付方式
         */
        public void submitBuyCardData(int cardId, int type, String couponCode, String payType, String gymId) {
            mCardModel.submitBuyCardData(cardId, type, couponCode, payType, gymId)
                    .subscribe(new ProgressObserver<SubmitPayResult>(mContext, R.string.loading, mView) {
                        @Override
                        public void onNext(SubmitPayResult value) {
                            if (value == null) return;
                            mView.updateSubmitPayView(value.getPayData());
                        }
                    });
        }
    }


}
