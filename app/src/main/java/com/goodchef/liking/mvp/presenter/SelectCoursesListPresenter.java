package com.goodchef.liking.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.aaron.http.code.RequestError;
import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.widget.refresh.BasePagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PagerRequestCallback;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.SelfGroupCoursesListResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.SelectCoursesListView;

import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午2:43
 */

public class SelectCoursesListPresenter extends BasePresenter<SelectCoursesListView> {

    private String mSelectCoursesId = null;

    public SelectCoursesListPresenter(Context context, SelectCoursesListView mainView) {
        super(context, mainView);
    }

    public void getCoursesList(int page, BasePagerLoaderFragment fragment) {
        LiKingApi.getSelfCoursesList(page, new PagerRequestCallback<SelfGroupCoursesListResult>(fragment) {
            @Override
            public void onSuccess(SelfGroupCoursesListResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    setCoursesSelectState(result.getData().getList());
                    mView.updateSelectCoursesListView(result.getData());
                } else {
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
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
    private void setCoursesSelectState(List<SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData> list){
        if(!TextUtils.isEmpty(getSelectCoursesId())){
            for (SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData coursesData:list){
                if(getSelectCoursesId().equals(coursesData.getCourseId())){
                    coursesData.setSelect(true);
                }else {
                    coursesData.setSelect(false);
                }
            }
        }
    }
}
