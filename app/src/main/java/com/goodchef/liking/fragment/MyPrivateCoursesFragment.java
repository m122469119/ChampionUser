package com.goodchef.liking.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PullMode;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.MyPrivateCoursesDetailsActivity;
import com.goodchef.liking.adapter.MyPrivateCoursesAdapter;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.eventmessages.MyPrivateCoursesCompleteMessage;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;
import com.goodchef.liking.mvp.presenter.MyPrivateCoursesPresenter;
import com.goodchef.liking.mvp.view.MyPrivateCoursesView;

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
        if (mMyPrivateCoursesPresenter == null) {
            mMyPrivateCoursesPresenter = new MyPrivateCoursesPresenter(getActivity(), this);
        }
        mMyPrivateCoursesPresenter.getMyPrivateCourses(page, MyPrivateCoursesFragment.this);
    }


    @Override
    protected void initViews() {
        setPullMode(PullMode.PULL_BOTH);
        setNoDataView();
        getRecyclerView().setBackgroundColor(ResourceUtils.getColor(R.color.app_content_background));
        setRecyclerViewPadding(0, 0, 0, DisplayUtils.dp2px(10));
        mPrivateLessonAdapter = new MyPrivateCoursesAdapter(getActivity());
        setRecyclerAdapter(mPrivateLessonAdapter);
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

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(MyPrivateCoursesCompleteMessage message) {
        loadHomePage();
    }

    public void onEvent(LoginOutFialureMessage message) {
        getActivity().finish();
    }
}
