package com.goodchef.liking.module.gym.details;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.GymDetailsResult;
import com.goodchef.liking.module.data.remote.rxobserver.ProgressObserver;
import com.goodchef.liking.module.gym.GymModel;

/**
 * Created on 2017/05/22
 * desc: 场馆详情
 *
 * @author: chenlei
 * @version:1.0
 */

public interface GymDetailsContract {

    interface GymDetailsView extends BaseView {
        void updateGymDetailsView(GymDetailsResult.GymDetailsData gymDetailsData);
    }

    class GymDetailsPresenter extends BasePresenter<GymDetailsView> {

        private GymModel mGymModel = null;

        public GymDetailsPresenter(Context context, GymDetailsView mainView) {
            super(context, mainView);
            mGymModel = new GymModel();
        }

        public void getGymDetails(String gymId) {
            mGymModel.getGymDetails(gymId)
                    .subscribe(new ProgressObserver<GymDetailsResult>(mContext, R.string.loading_data, mView) {
                        @Override
                        public void onNext(GymDetailsResult value) {
                            if (value == null) return;
                            mView.updateGymDetailsView(value.getData());
                        }
                    });
        }
    }
}
