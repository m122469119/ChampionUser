package com.goodchef.liking.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkPagerLoaderRecyclerViewFragment;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.GroupLessonDetailsActivity;
import com.goodchef.liking.adapter.MyGroupCoursesAdapter;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.MyGroupCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.MyGroupCoursesPresenter;
import com.goodchef.liking.mvp.view.MyGroupCourseView;
import com.goodchef.liking.storage.Preference;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/31 下午4:42
 */
public class MyGroupLessonFragment extends NetworkPagerLoaderRecyclerViewFragment implements MyGroupCourseView {
    public static final String INTENT_KEY_STATE = "intent_key_state";
    public static final String INTENT_KEY_ORDER_ID = "intent_key_order_id";
    private MyGroupCoursesAdapter mGroupLessonAdapter;

    private MyGroupCoursesPresenter mMyGroupCoursesPresenter;

    @Override
    protected void requestData(int page) {
        sendRequest(page);
    }

    @Override
    protected void initViews() {
    }

    private void sendRequest(int page) {
        mMyGroupCoursesPresenter = new MyGroupCoursesPresenter(getActivity(), this);
        mMyGroupCoursesPresenter.getMyGroupList(page, MyGroupLessonFragment.this);
    }


    @Override
    public void updateMyGroupCoursesView(MyGroupCoursesResult.MyGroupCoursesData myGroupCoursesData) {
        List<MyGroupCoursesResult.MyGroupCoursesData.MyGroupCourses> myGroupCoursesDataList = myGroupCoursesData.getMyGroupCourses();
        if (myGroupCoursesDataList != null && myGroupCoursesDataList.size() > 0) {
            setPullType(PullMode.PULL_BOTH);
            mGroupLessonAdapter = new MyGroupCoursesAdapter(getActivity());
            mGroupLessonAdapter.setData(myGroupCoursesDataList);
            setRecyclerAdapter(mGroupLessonAdapter);
            mGroupLessonAdapter.setCancelListener(cancelListener);
            mGroupLessonAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    TextView textView = (TextView) view.findViewById(R.id.group_lesson_period_of_validity);
                    if (textView != null) {
                        MyGroupCoursesResult.MyGroupCoursesData.MyGroupCourses data = (MyGroupCoursesResult.MyGroupCoursesData.MyGroupCourses) textView.getTag();
                        if (data != null) {
                            Intent intent = new Intent(getActivity(), GroupLessonDetailsActivity.class);
                            intent.putExtra(INTENT_KEY_STATE,data.getStatus());
                            intent.putExtra(LikingLessonFragment.KEY_SCHEDULE_ID,data.getScheduleId());
                            intent.putExtra(INTENT_KEY_ORDER_ID,data.getOrderId());
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public boolean onItemLongClick(View view, int position) {
                    return false;
                }
            });
        }

    }

    /**
     * 取消预约事件
     */
    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView textView = (TextView) v.findViewById(R.id.cancel_order_btn);
            if (textView != null) {
                MyGroupCoursesResult.MyGroupCoursesData.MyGroupCourses data = (MyGroupCoursesResult.MyGroupCoursesData.MyGroupCourses) textView.getTag();
                if (data != null) {
                    sendCancelCoursesRequest(data.getOrderId());
                }
            }
        }
    };

    //发送取消请求
    private void sendCancelCoursesRequest(String orderId) {
        LiKingApi.cancelGroupCourses(Preference.getToken(), orderId, new RequestUiLoadingCallback<BaseResult>(getActivity(), R.string.loading_data) {
            @Override
            public void onSuccess(BaseResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(getActivity(), result)) {
                    PopupUtils.showToast("取消成功");
                    loadHomePage();
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
