package com.goodchef.liking.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.MyGroupLessonFragment;
import com.goodchef.liking.http.result.MyChargeGroupCoursesDetailsResult;
import com.goodchef.liking.mvp.presenter.MyChargeGroupCoursesDetailsPresenter;
import com.goodchef.liking.mvp.view.MyChargeGroupCoursesDetailsView;
import com.goodchef.liking.widgets.base.LikingStateView;

/**
 * 说明:收费团体课详情界面
 * Author shaozucheng
 * Time:16/8/22 下午3:22
 */
public class MyChargeGroupCoursesDetailsActivity extends AppBarActivity implements MyChargeGroupCoursesDetailsView{

    private LikingStateView mStateView;
    private TextView mCoursesNameTextView;


    private MyChargeGroupCoursesDetailsPresenter mCoursesDetailsPresenter;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_charge_group_courese_details);
        initView();
        getIntentData();
        sedCoursesDetailsRequest();
    }

    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.my_charge_group_courses_details_state_view);
        mCoursesNameTextView = (TextView) findViewById(R.id.charge_courses_name);
        initStateView();
    }

    private void initStateView() {
        if (EnvironmentUtils.Network.isNetWorkAvailable()) {
            mStateView.setState(StateView.State.SUCCESS);
        } else {
            mStateView.setState(StateView.State.FAILED);
        }
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                initStateView();
            }
        });
    }

    private void getIntentData(){
        orderId = getIntent().getStringExtra(MyGroupLessonFragment.INTENT_KEY_ORDER_ID);
    }

    private void sedCoursesDetailsRequest() {
        mCoursesDetailsPresenter = new MyChargeGroupCoursesDetailsPresenter(this,this);
        mCoursesDetailsPresenter.getMyGroupDetails(orderId);
    }

    @Override
    public void updateMyChargeGroupCoursesDetailsView(MyChargeGroupCoursesDetailsResult.MyChargeGroupCoursesDetails groupCoursesDetails) {
        mCoursesNameTextView.setText(groupCoursesDetails.getCourseName());
    }
}
