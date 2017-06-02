package com.goodchef.liking.module.course.selfhelp.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PullMode;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.SelectCoursesListAdapter;
import com.goodchef.liking.data.remote.retrofit.result.SelfGroupCoursesListResult;
import com.goodchef.liking.eventmessages.SelectCoursesMessage;
import com.goodchef.liking.module.course.selfhelp.details.SelfLessonDetailsActivity;

import java.util.List;

/**
 * 说明: 选择排课列表
 * Author : shaozucheng
 * Time: 下午2:13
 */

public class SelectCoursesListFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment<SelfHelpCourseListContract.Presenter> implements SelfHelpCourseListContract.View {

    private SelectCoursesListAdapter mSelectCoursesListAdapter;

    private String mCurrCourseId = null;

    public static SelectCoursesListFragment newInstance() {
        Bundle args = new Bundle();
        SelectCoursesListFragment fragment = new SelectCoursesListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void requestData(int page) {
        sendRequest(page);
    }

    private void sendRequest(int page) {
        mPresenter.getCoursesList(page);
        mPresenter.setSelectCoursesId(mCurrCourseId);
    }

    @Override
    protected void initViews() {
        setPullMode(PullMode.PULL_BOTH);
        initData();
    }

    private void initData() {
        getStateView().findViewById(R.id.text_view_fail_refresh).setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                loadHomePage();
            }
        });
        setNoDataView();
        if (mSelectCoursesListAdapter == null) {
            mSelectCoursesListAdapter = new SelectCoursesListAdapter(getActivity());
        }

        setRecyclerAdapter(mSelectCoursesListAdapter);
        mSelectCoursesListAdapter.setViewOnClickListener(mViewListener);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mCurrCourseId = bundle.getString(SelectCoursesListActivity.KEY_SELECT_COURSES_ID, "");
            }
        }
    }

    private void setNoDataView() {
        android.view.View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
        ImageView noDataImageView = (ImageView) noDataView.findViewById(R.id.imageview_no_data);
        TextView noDataText = (TextView) noDataView.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) noDataView.findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.icon_no_coureses_data);
        noDataText.setText(R.string.no_data);
        refreshView.setText(R.string.refresh_btn_text);
        refreshView.setOnClickListener(refreshOnClickListener);
        getStateView().setNodataView(noDataView);
    }

    /***
     * 刷新事件
     */
    private android.view.View.OnClickListener refreshOnClickListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            loadHomePage();
        }
    };


    /**
     * 设置选择事件
     */
    private android.view.View.OnClickListener mViewListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {

            switch (v.getId()) {
                case R.id.layout_select_courses:
                    LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout_select_courses);
                    if (layout != null) {
                        SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData coursesData = (SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData) v.getTag();
                        if (coursesData != null) {
                            Intent intent = new Intent(getActivity(), SelfLessonDetailsActivity.class);
                            intent.putExtra(SelfLessonDetailsActivity.KEY_SELF_LESSON_DETAILS, coursesData);
                            startActivity(intent);
                        }
                    }
                    break;
                case R.id.layout_select_courses_checkbox:
                    LinearLayout selectlayout = (LinearLayout) v.findViewById(R.id.layout_select_courses_checkbox);
                    if (selectlayout != null) {
                        SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData coursesData = (SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData) v.getTag();
                        if (coursesData != null) {
                            postEvent(new SelectCoursesMessage(coursesData));
                            getActivity().finish();
                        }
                    }
                    break;
            }

        }
    };

    @Override
    public void updateSelectCoursesListView(SelfGroupCoursesListResult.SelfGroupCoursesData data) {
        List<SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData> list = data.getList();
        updateListView(list);
    }

    @Override
    public void changeStateView(StateView.State state) {
        getStateView().setState(state);
    }

    @Override
    public void setPresenter() {
        mPresenter = new SelfHelpCourseListContract.Presenter();
    }
}
