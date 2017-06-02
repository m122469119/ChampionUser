package com.goodchef.liking.module.gym.details;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.GymDetailsResult;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;
import com.goodchef.liking.module.gym.GymModel;

/**
 * Created on 2017/05/22
 * desc: 场馆详情
 *
 * @author: chenlei
 * @version:1.0
 */

interface GymDetailsContract {

    interface View extends BaseView {
        void updateGymDetailsView(GymDetailsResult.GymDetailsData gymDetailsData);
    }

    class Presenter extends RxBasePresenter<View> {

        private GymModel mGymModel = null;

        public Presenter() {
            mGymModel = new GymModel();
        }

        void getGymDetails(Context context, String gymId) {
            mGymModel.getGymDetails(gymId)
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<GymDetailsResult>(context, R.string.loading_data, mView) {
                        @Override
                        public void onNext(GymDetailsResult value) {
                            if (value == null) return;
                            mView.updateGymDetailsView(value.getData());
                        }
                    }));
        }
    }
}
