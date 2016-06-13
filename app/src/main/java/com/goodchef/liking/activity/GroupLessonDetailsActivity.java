package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.GroupLessonDetailsAdapter;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.result.PrivateCoursesResult;
import com.goodchef.liking.mvp.presenter.CoursesDetailsPresenter;
import com.goodchef.liking.mvp.view.CoursesDetailsView;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:团体课可详情
 * Author shaozucheng
 * Time:16/5/24 下午3:21
 */
public class GroupLessonDetailsActivity extends AppBarActivity implements CoursesDetailsView {


    private RecyclerView mRecyclerView;
    private GroupLessonDetailsAdapter mGroupLessonDetailsAdapter;
    private CoursesDetailsPresenter mCoursesDetailsPresenter;
    private String scheduleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_lesson_details);
        setTitle("团体课详情");
        initData();
    }

    private void initData() {
        scheduleId = getIntent().getStringExtra(LikingLessonFragment.KEY_SCHEDULE_ID);
        initView();
        requestData();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.group_lesson_details_recyclerView);
    }

    private void requestData() {
        mCoursesDetailsPresenter = new CoursesDetailsPresenter(this, this);
        mCoursesDetailsPresenter.getGroupCoursesDetails(scheduleId);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i + "");
        }

        mGroupLessonDetailsAdapter = new GroupLessonDetailsAdapter(this);
        mGroupLessonDetailsAdapter.setData(list);
        mRecyclerView.setAdapter(mGroupLessonDetailsAdapter);
    }


    @Override
    public void updateGroupLessonDetailsView(GroupCoursesResult.GroupLessonData groupLessonData) {
        String name = groupLessonData.getCourseName();
    }

    @Override
    public void updatePrivateCoursesDetailsView(PrivateCoursesResult.PrivateCoursesData privateCoursesData) {

    }
}
