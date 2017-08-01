package com.goodchef.liking.module.course.group.details.charge;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.MyChargeGroupCoursesDetailsResult;
import com.goodchef.liking.module.course.group.MyGroupLessonFragment;
import com.goodchef.liking.widgets.base.LikingStateView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 说明:收费团体课详情界面
 * Author shaozucheng
 * Time:16/8/22 下午3:22
 */
public class MyChargeGroupCoursesDetailsActivity extends AppBarMVPSwipeBackActivity<MyChargeTeamCourseDetailsContract.Presenter> implements MyChargeTeamCourseDetailsContract.View {

    @BindView(R.id.my_charge_group_courses_details_state_view)
    LikingStateView mStateView;
    @BindView(R.id.charge_courses_name)
    TextView mCoursesNameTextView;//课程名称
    @BindView(R.id.charge_courses_teacher)
    TextView mTeacherNameTextView;//教练名称
    @BindView(R.id.courses_length)
    TextView mCoursesLengthTextView;//课程时长
    @BindView(R.id.courses_time)
    TextView mCoursesTimeTextView;//上课时间
    @BindView(R.id.rating_courses)
    RatingBar mRatingBar;//课程强度
    @BindView(R.id.shop_address)
    TextView mCoursesAddressTextView;//上课地点
    @BindView(R.id.order_number)
    TextView mOrderNumberTextView;//订单号
    @BindView(R.id.order_time)
    TextView mOrderTimeTextView;//下单时间
    @BindView(R.id.order_price)
    TextView mPriceTextView;//价格
    @BindView(R.id.order_coupons)
    TextView mCouponsTextView;//优惠
    @BindView(R.id.order_amount)
    TextView mCoursesAmountTextView;//实付
    @BindView(R.id.order_pay_type)
    TextView mPayTypeTextView;//支付方式
    @BindView(R.id.order_state)
    TextView mOrderStateTextView;//订单状态

    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_charge_group_courese_details);
        ButterKnife.bind(this);
        setTitle(getString(R.string.activity_title_my_group_details));
        initView();
        getIntentData();
        sedCoursesDetailsRequest();
    }

    private void initView() {
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

    private void getIntentData() {
        orderId = getIntent().getStringExtra(MyGroupLessonFragment.INTENT_KEY_ORDER_ID);
    }

    private void sedCoursesDetailsRequest() {
        mStateView.setState(StateView.State.LOADING);
        mPresenter.getChargeGroupCoursesDetails(orderId);
    }


    @Override
    public void updateChargeGroupCoursesDetailsView(MyChargeGroupCoursesDetailsResult.MyChargeGroupCoursesDetails groupCoursesDetails) {
        if (groupCoursesDetails != null) {
            mStateView.setState(StateView.State.SUCCESS);
            mCoursesNameTextView.setText(groupCoursesDetails.getCourseName());
            mTeacherNameTextView.setText(groupCoursesDetails.getTrainerName());
            mCoursesLengthTextView.setText(groupCoursesDetails.getDuration());
            mCoursesTimeTextView.setText(groupCoursesDetails.getCourseTime());
            int intensity = groupCoursesDetails.getIntensity();
            mRatingBar.setRating(Float.parseFloat(String.valueOf(intensity)));
            mCoursesAddressTextView.setText(groupCoursesDetails.getCourseAddress());
            mOrderNumberTextView.setText(groupCoursesDetails.getOrderId());
            mOrderTimeTextView.setText(groupCoursesDetails.getOrderTime());
            mPriceTextView.setText(groupCoursesDetails.getTotalAmount());
            mCouponsTextView.setText(groupCoursesDetails.getCouponAmount());
            mCoursesAmountTextView.setText(groupCoursesDetails.getActualAmount());
            String payDesc = groupCoursesDetails.getPayDesc();
            mPayTypeTextView.setText(payDesc);
            int state = groupCoursesDetails.getStatus();//0 未开始  1 进行中 2 已结束 3已取消
            if (state == 0) {
                mOrderStateTextView.setText(R.string.no_start);
            } else if (state == 1) {
                mOrderStateTextView.setText(R.string.is_doing);
            } else if (state == 2) {
                mOrderStateTextView.setText(R.string.is_over);
            } else if (state == 3) {
                mOrderStateTextView.setText(R.string.is_cancel);
            }

        }
    }

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }

    @Override
    public void setPresenter() {
        mPresenter = new MyChargeTeamCourseDetailsContract.Presenter();
    }
}
