package com.goodchef.liking.module.course.personal;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.data.remote.retrofit.result.MyPrivateCoursesResult;
import com.goodchef.liking.data.remote.rxobserver.PagerLoadingObserver;
import com.goodchef.liking.module.course.CourseModel;
/**
 * Created on 2017/05/09
 * desc: 我的私教列表
 *
 * @author: chenlei
 * @version:1.0
 */

interface MyPersonalCourseContract {

    interface View extends BaseView {
        void updatePrivateCoursesView(MyPrivateCoursesResult.PrivateCoursesData privateCoursesData);
    }

    class Presenter extends RxBasePresenter<View> {

        private CourseModel mCourseModel = null;

        public Presenter() {
            mCourseModel = new CourseModel();
        }

        void getMyPrivateCourses(int page) {

            mCourseModel.getMyPrivateCourses(page)
                    .subscribe(addObserverToCompositeDisposable(new PagerLoadingObserver<MyPrivateCoursesResult>(mView) {

                        @Override
                        public void onNext(MyPrivateCoursesResult result) {
                            super.onNext(result);
                            if (result == null) return;
                            mView.updatePrivateCoursesView(result.getData());
                        }
                    }));
        }
    }

}
