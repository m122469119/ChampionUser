package com.goodchef.liking.activity;

import android.os.Bundle;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.result.PrivateCoursesResult;
import com.goodchef.liking.mvp.presenter.CoursesDetailsPresenter;
import com.goodchef.liking.mvp.view.CoursesDetailsView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/24 下午5:55
 */
public class PrivateLessonDetailsActivity extends AppBarActivity implements CoursesDetailsView {

    private CoursesDetailsPresenter mCoursesDetailsPresenter;
    private String trainerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_lesson_details);
        setTitle("Hraey教练");
        initData();
    }

    private void initData() {
        trainerId = getIntent().getStringExtra(LikingLessonFragment.KEY_TRAINER_ID);
        initView();
        sendDetailsRequest();
    }

    private void initView() {

    }

    private void sendDetailsRequest() {
        mCoursesDetailsPresenter = new CoursesDetailsPresenter(this, this);
        mCoursesDetailsPresenter.getPrivateCouresDetails(trainerId);
    }

    @Override
    public void updateGroupLessonDetailsView(GroupCoursesResult.GroupLessonData groupLessonData) {

    }

    @Override
    public void updatePrivateCoursesDetailsView(PrivateCoursesResult.PrivateCoursesData privateCoursesData) {
        String name = privateCoursesData.getTrainerName();
    }
}
