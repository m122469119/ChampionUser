package com.goodchef.liking.activity;

import android.os.Bundle;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.MyPrivateCoursesFragment;
import com.goodchef.liking.http.result.MyPrivateCoursesDetailsResult;
import com.goodchef.liking.mvp.presenter.MyPrivateCoursesDetailsPresenter;
import com.goodchef.liking.mvp.view.MyPrivateCoursesDetailsView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/20 下午7:08
 */
public class MyPrivateCoursesDetailsActivity extends AppBarActivity implements MyPrivateCoursesDetailsView {

    private MyPrivateCoursesDetailsPresenter mCoursesDetailsPresenter;

    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_private_courses_details);
        initData();
    }

    private void initData() {
        orderId = getIntent().getStringExtra(MyPrivateCoursesFragment.KEY_ORDER_ID);
        mCoursesDetailsPresenter = new MyPrivateCoursesDetailsPresenter(this, this);
        sendRequest();
       // sendCompleteRequest();
    }

    private void sendRequest() {
        mCoursesDetailsPresenter.getMyPrivateCoursesDetails(orderId);
    }

    private void sendCompleteRequest() {
        mCoursesDetailsPresenter.completeMyPrivateCourses(orderId);
    }

    @Override
    public void updateMyPrivateCoursesDetailsView(MyPrivateCoursesDetailsResult.MyPrivateCoursesDetailsData myPrivateCoursesDetailsData) {
        myPrivateCoursesDetailsData.getCourseName();
    }

    @Override
    public void updateComplete() {
        PopupUtils.showToast("该课程以确定完成");
    }
}
