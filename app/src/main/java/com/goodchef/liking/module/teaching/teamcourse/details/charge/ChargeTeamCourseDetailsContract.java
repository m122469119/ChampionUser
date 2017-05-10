package com.goodchef.liking.module.teaching.teamcourse.details.charge;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.MyChargeGroupCoursesDetailsResult;
import com.goodchef.liking.http.verify.LiKingRequestCode;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.base.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.base.rxobserver.ProgressObserver;
import com.goodchef.liking.module.data.local.Preference;
import com.goodchef.liking.module.teaching.CourseModel;
import com.goodchef.liking.mvp.view.MyChargeGroupCoursesDetailsView;

/**
 * Created on 2017/05/10
 * desc: 付费团体课详情
 *
 * @author: chenlei
 * @version:1.0
 */

public interface ChargeTeamCourseDetailsContract {

    interface ChargeGroupCoursesDetailsView extends BaseNetworkLoadView {
        void updateChargeGroupCoursesDetailsView(MyChargeGroupCoursesDetailsResult.MyChargeGroupCoursesDetails groupCoursesDetails);
    }

    class MyChargeGroupCoursesDetailsPresenter extends BasePresenter<ChargeGroupCoursesDetailsView> {

        private CourseModel mCourseModel = null;

        public MyChargeGroupCoursesDetailsPresenter(Context context, ChargeGroupCoursesDetailsView mainView) {
            super(context, mainView);
            mCourseModel = new CourseModel();
        }

        /**
         * 获取我的付费团体课详情
         */
        public void getChargeGroupCoursesDetails(String orderId) {
            mCourseModel.getChargeGroupCoursesDetails(orderId)
                    .subscribe(new LikingBaseObserver<MyChargeGroupCoursesDetailsResult>() {
                        @Override
                        public void onNext(MyChargeGroupCoursesDetailsResult result) {
                            super.onNext(result);
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateChargeGroupCoursesDetailsView(result.getData());
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            mView.handleNetworkFailure();
                        }
                    });
        }

    }
}
