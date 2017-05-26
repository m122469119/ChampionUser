package com.goodchef.liking.module.course.selfhelp.list;

import android.content.Context;
import android.text.TextUtils;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.result.SelfGroupCoursesListResult;
import com.goodchef.liking.module.course.CourseModel;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.rxobserver.PagerLoadingObserver;

import java.util.List;

/**
 * Created on 2017/05/12
 * desc: 自助课列表
 *
 * @author: chenlei
 * @version:1.0
 */

public interface SelfHelpCourseListContract {

    interface SelectCoursesListView extends BaseStateView {
        void updateSelectCoursesListView(SelfGroupCoursesListResult.SelfGroupCoursesData data);
    }

    class SelectCoursesListPresenter extends BasePresenter<SelectCoursesListView> {

        private String mSelectCoursesId = null;
        private CourseModel mCourseModel = null;

        public SelectCoursesListPresenter(Context context, SelectCoursesListView mainView) {
            super(context, mainView);
            mCourseModel = new CourseModel();
        }

        public void getCoursesList(int page) {
            mCourseModel.getSelfCoursesList(page)
                    .subscribe(new PagerLoadingObserver<SelfGroupCoursesListResult>(mContext, mView) {
                        @Override
                        public void onNext(SelfGroupCoursesListResult result) {
                            super.onNext(result);
                            if (result == null) return;
                            setCoursesSelectState(result.getData().getList());
                            mView.updateSelectCoursesListView(result.getData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            super.apiError(apiException);
                            mView.changeStateView(StateView.State.FAILED);
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            super.networkError(throwable);
                            mView.changeStateView(StateView.State.FAILED);
                        }
                    });
        }

        public String getSelectCoursesId() {
            return mSelectCoursesId;
        }

        public void setSelectCoursesId(String selectCoursesId) {
            mSelectCoursesId = selectCoursesId;
        }

        /**
         * 设置选中状态
         *
         * @param list
         */
        private void setCoursesSelectState(List<SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData> list) {
            if (!TextUtils.isEmpty(getSelectCoursesId())) {
                for (SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData coursesData : list) {
                    if (getSelectCoursesId().equals(coursesData.getCourseId())) {
                        coursesData.setSelect(true);
                    } else {
                        coursesData.setSelect(false);
                    }
                }
            }
        }
    }

}
