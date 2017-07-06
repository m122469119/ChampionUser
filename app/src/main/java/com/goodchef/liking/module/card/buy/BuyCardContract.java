package com.goodchef.liking.module.card.buy;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.goodchef.liking.data.remote.retrofit.result.CardResult;
import com.goodchef.liking.data.remote.rxobserver.StateViewLoadingObserver;
import com.goodchef.liking.module.home.LikingHomeActivity;

import java.util.List;

/**
 * Created on 2017/05/19
 * desc: 购卡列表
 *
 * @author: chenlei
 * @version:1.0
 */

interface BuyCardContract {

    interface View extends BaseStateView {
        void updateCardListView(CardResult.CardData cardData);

        void setAdapter(List<CardResult.CardData.Category.CardBean> cardList);

        void setAllAndStaggerTitleText(String text);

        void setAllAndStaggerTimeText(String text);
    }

    class Presenter extends RxBasePresenter<View> {

        private BuyCardModel mModel;

        public Presenter() {
            mModel = new BuyCardModel();
        }

        void getCardList(String gymId, int buyType) {
            if (gymId.equals("-1")) {
                gymId = LikingHomeActivity.gymId;
            }
            mModel.getLocationData();
            mModel.getCardList(gymId, buyType)
                    .subscribe(addObserverToCompositeDisposable(new StateViewLoadingObserver<CardResult>(mView) {
                @Override
                public void onNext(CardResult value) {
                    super.onNext(value);
                    mView.setAdapter(mModel.getCardList());
                    mView.updateCardListView(value.getCardData());
                    setCheckedTitleAndTime();
                }
            }));

        }

        public void setAllAndStaggerChecked(int allCard) {
            mModel.mCheckedType = allCard;
            mView.setAdapter(mModel.getCardList());
            setCheckedTitleAndTime();
        }


        public void setCheckedTitleAndTime(){
            mView.setAllAndStaggerTimeText(mModel.getTimeText());
            mView.setAllAndStaggerTitleText(mModel.getTitleText());
        }

        public String getGymId() {
            return mModel.getGymId();
        }
    }
}
