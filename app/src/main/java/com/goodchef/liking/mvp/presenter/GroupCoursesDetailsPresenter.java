package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.GroupLessonDetailsActivity;
import com.goodchef.liking.eventmessages.CancelGroupCoursesMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.verify.LiKingRequestCode;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.GroupCourserDetailsView;
import com.goodchef.liking.storage.Preference;

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
                            PopupUtils.showToast(result.getMessage());
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
        LiKingApi.orderGroupCourses(gymId, scheduleId, token, new RequestUiLoadingCallback<BaseResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(BaseResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateOrderGroupCourses();
                } else if (result.getCode() == LiKingRequestCode.BUY_COURSES_NO_CARD) {
                    mView.updateErrorNoCard(result.getMessage());
                } else if (result.getCode() != LiKingRequestCode.BUY_COURSES_ERROR) {
                    PopupUtils.showToast(result.getMessage());
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
        LiKingApi.cancelGroupCourses(Preference.getToken(), orderId, new RequestUiLoadingCallback<BaseResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(BaseResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateCancelOrderView();
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }
}
