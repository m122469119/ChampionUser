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
import com.goodchef.liking.activity.MyPrivateCoursesDetailsActivity;
import com.goodchef.liking.adapter.PrivateLessonAdapter;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.MyPrivateCoursesPresenter;
import com.goodchef.liking.mvp.view.MyPrivateCoursesView;
import com.goodchef.liking.storage.Preference;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/31 下午4:43
 */
public class MyPrivateCoursesFragment extends NetworkPagerLoaderRecyclerViewFragment implements MyPrivateCoursesView {

    public static final String KEY_ORDER_ID = "key_order_id";
    private PrivateLessonAdapter mPrivateLessonAdapter;
    private MyPrivateCoursesPresenter mMyPrivateCoursesPresenter;

    @Override
    protected void requestData(int page) {
        sendRequest(page);
    }

    private void sendRequest(int page) {
        mMyPrivateCoursesPresenter = new MyPrivateCoursesPresenter(getActivity(), this);
        mMyPrivateCoursesPresenter.getMyPrivateCourses(page, MyPrivateCoursesFragment.this);
    }


    @Override
    protected void initViews() {
        setPullType(PullMode.PULL_BOTH);
    }

    @Override
    public void updatePrivateCoursesView(MyPrivateCoursesResult.PrivateCoursesData privateCoursesData) {
        List<MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses> list = privateCoursesData.getPrivateCoursesList();
        if (list != null && list.size() > 0) {
            mPrivateLessonAdapter = new PrivateLessonAdapter(getActivity());
            mPrivateLessonAdapter.setData(list);
            setRecyclerAdapter(mPrivateLessonAdapter);
            mPrivateLessonAdapter.setCompleteListener(mCompleteListener);
            mPrivateLessonAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    TextView textView = (TextView) view.findViewById(R.id.private_teacher_name);
                    if (textView != null) {
                        MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses privateCourses = (MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses) textView.getTag();
                        if (privateCourses != null) {
                            Intent intent = new Intent(getActivity(), MyPrivateCoursesDetailsActivity.class);
                            intent.putExtra(KEY_ORDER_ID, privateCourses.getOrderId());
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
     * 完成课程事件
     */
    private View.OnClickListener mCompleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           TextView textView = (TextView) v.findViewById(R.id.complete_courses_btn);
            if (textView !=null){
                MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses data = (MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses) textView.getTag();
                 if (data !=null){
                     completeMyPrivateCourses(data.getOrderId());
                 }
            }
        }
    };

    //发送完成课程请求
    public void completeMyPrivateCourses(String orderId) {
        LiKingApi.completerMyPrivateCourses(Preference.getToken(), orderId, new RequestUiLoadingCallback<BaseResult>(getActivity(), R.string.loading_data) {
            @Override
            public void onSuccess(BaseResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(getActivity(), result)) {
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
