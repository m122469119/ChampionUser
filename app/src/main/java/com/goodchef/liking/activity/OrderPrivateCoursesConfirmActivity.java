package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.PrivateCoursesTrainItemAdapter;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.mvp.presenter.PrivateCoursesConfirmPresenter;
import com.goodchef.liking.mvp.view.PrivateCoursesConfirmView;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/15 下午6:01
 */
public class OrderPrivateCoursesConfirmActivity extends AppBarActivity implements PrivateCoursesConfirmView, View.OnClickListener {
    private static final int INTENT_REQUEST_CODE_COUPON = 100;

    private RecyclerView mRecyclerView;
    private TextView mCoursesPeopleTextView;
    private TextView mCoursesNumberTextView;
    private TextView mEndTimeTextView;
    private RelativeLayout mCouponsLayout;


    private PrivateCoursesTrainItemAdapter mPrivateCoursesTrainItemAdapter;
    private PrivateCoursesConfirmPresenter mPrivateCoursesConfirmPresenter;

    private String trainerId;
    private String coursesId;
    private CouponsResult.CouponData.Coupon mCoupon;

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
        mCouponsLayout = (RelativeLayout) findViewById(R.id.layout_coupons_courses);

        mCouponsLayout.setOnClickListener(this);
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
            coursesId = trainItemList.get(0).getCourseId();
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            mPrivateCoursesTrainItemAdapter = new PrivateCoursesTrainItemAdapter(this);
            mPrivateCoursesTrainItemAdapter.setData(trainItemList);
            mRecyclerView.setAdapter(mPrivateCoursesTrainItemAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mCouponsLayout) {
            Intent intent = new Intent(this, CouponsActivity.class);
            intent.putExtra(CouponsActivity.KEY_COURSE_ID, coursesId);
            startActivityForResult(intent, INTENT_REQUEST_CODE_COUPON);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_REQUEST_CODE_COUPON) {
                mCoupon= (CouponsResult.CouponData.Coupon) data.getSerializableExtra(CouponsActivity.INTENT_KEY_COUPONS_DATA);
                PopupUtils.showToast(mCoupon.getAmount());
            }
        }
    }
}
