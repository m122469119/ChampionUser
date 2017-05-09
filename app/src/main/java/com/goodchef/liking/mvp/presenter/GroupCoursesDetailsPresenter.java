package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.goodchef.liking.http.result.LikingResult;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.verify.LiKingRequestCode;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.GroupCourserDetailsView;
import com.goodchef.liking.module.data.local.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/15 下午5:30
 */
public class GroupCoursesDetailsPresenter extends BasePresenter<GroupCourserDetailsView> {
    public GroupCoursesDetailsPresenter(Context context, GroupCourserDetailsView mainView) {
        super(context, mainView);
    }

    public void getGroupCoursesDetails(String scheduleId) {
        LiKingApi.getGroupLessonDetails(scheduleId, new RequestCallback<GroupCoursesResult>() {
                    @Override
                    public void onSuccess(GroupCoursesResult result) {
                        if (LiKingVerifyUtils.isValid(mContext, result)) {
                            mView.updateGroupLessonDetailsView(result.getGroupLessonData());
                        } else {
                            mView.showToast(result.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(RequestError error) {
                        mView.handleNetworkFailure();
                    }
                }
        );
    }

    public void orderGroupCourses(String gymId, String scheduleId, String token) {
        LiKingApi.orderGroupCourses(gymId, scheduleId, token, new RequestUiLoadingCallback<LikingResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(LikingResult likingResult) {
                super.onSuccess(likingResult);
                if (LiKingVerifyUtils.isValid(mContext, likingResult)) {
                    mView.updateOrderGroupCourses();
                } else if (likingResult.getCode() == LiKingRequestCode.BUY_COURSES_NO_CARD) {
                    mView.updateErrorNoCard(likingResult.getMessage());
                } else if (likingResult.getCode() != LiKingRequestCode.BUY_COURSES_ERROR) {
                    mView.showToast(likingResult.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }


    //发送取消请求
    public void sendCancelCoursesRequest(String orderId) {
        LiKingApi.cancelGroupCourses(Preference.getToken(), orderId, new RequestUiLoadingCallback<LikingResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(LikingResult likingResult) {
                super.onSuccess(likingResult);
                if (LiKingVerifyUtils.isValid(mContext, likingResult)) {
                    mView.updateCancelOrderView();
                } else {
                    mView.showToast(likingResult.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }
}
