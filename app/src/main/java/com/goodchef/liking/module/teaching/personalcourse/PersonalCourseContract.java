package com.goodchef.liking.module.teaching.personalcourse;

import android.content.Context;

import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.base.rxobserver.PagerLoadingObserver;
import com.goodchef.liking.module.teaching.CourseModel;

/**
 * Created on 2017/05/09
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public interface PersonalCourseContract {

    interface MyPrivateCoursesView extends BaseView {
        void updatePrivateCoursesView(MyPrivateCoursesResult.PrivateCoursesData privateCoursesData);
    }

    class MyPrivateCoursesPresenter extends BasePresenter<MyPrivateCoursesView> {

        private CourseModel mCourseModel = null;

        public MyPrivateCoursesPresenter(Context context, MyPrivateCoursesView mainView) {
            super(context, mainView);
            mCourseModel = new CourseModel();
        }

        public void getMyPrivateCourses(int page) {

            mCourseModel.getMyPrivateCourses(page)
                    .subscribe(new PagerLoadingObserver<MyPrivateCoursesResult>(mView) {
                        @Override
                        public void onNext(MyPrivateCoursesResult result) {
                            super.onNext(result);
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updatePrivateCoursesView(result.getData());
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }
                    });
        }
    }

}
