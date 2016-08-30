package com.goodchef.liking.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PullMode;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.MyPrivateCoursesDetailsActivity;
import com.goodchef.liking.adapter.MyPrivateCoursesAdapter;
import com.goodchef.liking.eventmessages.MyPrivateCoursesCompleteMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.MyPrivateCoursesPresenter;
import com.goodchef.liking.mvp.view.MyPrivateCoursesView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.UMengCountUtil;

import java.util.List;

/**
 * 说明:我的私教列表
 * Author shaozucheng
 * Time:16/5/31 下午4:43
 */
public class MyPrivateCoursesFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment implements MyPrivateCoursesView {

    public static final String KEY_ORDER_ID = "key_order_id";
    private MyPrivateCoursesAdapter mPrivateLessonAdapter;
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
        setPullMode(PullMode.PULL_BOTH);
        setNoDataView();

        mPrivateLessonAdapter = new MyPrivateCoursesAdapter(getActivity());
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

    private void setNoDataView() {
        View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
        ImageView noDataImageView = (ImageView) noDataView.findViewById(R.id.imageview_no_data);
        TextView noDataText = (TextView) noDataView.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) noDataView.findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.icon_no_coureses_data);
        noDataText.setText(R.string.no_courese_data);
        refreshView.setText(R.string.refresh_btn_text);
        refreshView.setOnClickListener(refreshOnClickListener);
        getStateView().setNodataView(noDataView);
    }

    /***
     * 刷新事件
     */
    private View.OnClickListener refreshOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadHomePage();
        }
    };

    @Override
    public void updatePrivateCoursesView(MyPrivateCoursesResult.PrivateCoursesData privateCoursesData) {
        List<MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses> list = privateCoursesData.getPrivateCoursesList();
        if (list != null) {
            updateListView(list);
        }
    }


    /**
     * 完成课程事件
     */
    private View.OnClickListener mCompleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView textView = (TextView) v.findViewById(R.id.complete_courses_btn);
            if (textView != null) {
                MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses data = (MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses) textView.getTag();
                if (data != null) {
                    showCompleteDialog(data.getOrderId());
                }
            }
        }
    };

    private void showCompleteDialog(final String orderId) {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(getActivity());
        builder.setMessage(ResourceUtils.getString(R.string.confirm_complete_courese));
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UMengCountUtil.UmengBtnCount(getActivity(), UmengEventId.COMPLETE_MYPRIVATE_COURSES);
                completeMyPrivateCourses(orderId);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

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

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(MyPrivateCoursesCompleteMessage message) {
        loadHomePage();
    }
}
