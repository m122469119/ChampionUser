package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.PrivateCoursesTrainItemAdapter;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.mvp.presenter.PrivateCoursesConfirmPresenter;
import com.goodchef.liking.mvp.view.PrivateCoursesConfirmView;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/15 下午6:01
 */
public class OrderPrivateCoursesConfirmActivity extends AppBarActivity implements PrivateCoursesConfirmView {
    private RecyclerView mRecyclerView;
    private TextView mCoursesPeopleTextView;
    private TextView mCoursesNumberTextView;
    private TextView mEndTimeTextView;


    private PrivateCoursesTrainItemAdapter mPrivateCoursesTrainItemAdapter;
    private PrivateCoursesConfirmPresenter mPrivateCoursesConfirmPresenter;

    private String trainerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_private_courses_confirm);
        initView();
        initData();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.confirm_recyclerView);
        mCoursesPeopleTextView = (TextView) findViewById(R.id.courses_people);
        mCoursesNumberTextView = (TextView) findViewById(R.id.courses_number);
        mEndTimeTextView = (TextView) findViewById(R.id.end_time);

    }

    private void initData() {
        trainerId = getIntent().getStringExtra(LikingLessonFragment.KEY_TRAINER_ID);
        sendRequest();
    }

    private void sendRequest() {
        mPrivateCoursesConfirmPresenter = new PrivateCoursesConfirmPresenter(this, this);
        mPrivateCoursesConfirmPresenter.orderPrivateCoursesConfirm(trainerId);
    }

    @Override
    public void updatePrivateCoursesConfirm(PrivateCoursesConfirmResult.PrivateCoursesConfirmData coursesConfirmData) {
        List<PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses> trainItemList = coursesConfirmData.getCourses();
        setTrainItem(trainItemList);
        mCoursesPeopleTextView.setText(coursesConfirmData.getPeopleNum());
        mEndTimeTextView.setText(coursesConfirmData.getEndTime());
    }

    private void setTrainItem(List<PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses> trainItemList) {
        if (trainItemList != null && trainItemList.size() > 0) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            mPrivateCoursesTrainItemAdapter = new PrivateCoursesTrainItemAdapter(this);
            mPrivateCoursesTrainItemAdapter.setData(trainItemList);
            mRecyclerView.setAdapter(mPrivateCoursesTrainItemAdapter);
        }
    }
}
