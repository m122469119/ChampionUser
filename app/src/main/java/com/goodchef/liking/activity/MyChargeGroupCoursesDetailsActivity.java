package com.goodchef.liking.activity;

import android.os.Bundle;
import android.widget.RatingBar;
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
public class MyChargeGroupCoursesDetailsActivity extends AppBarActivity implements MyChargeGroupCoursesDetailsView {
    private LikingStateView mStateView;
    private TextView mCoursesNameTextView;//课程名称
    private TextView mTeacherNameTextView;//教练名称
    private TextView mCoursesLengthTextView;//课程时长
    private TextView mCoursesTimeTextView;//上课时间
    private RatingBar mRatingBar;//课程强度
    private TextView mCoursesAddressTextView;//上课地点
    private TextView mOrderNumberTextView;//订单号
    private TextView mOrderTimeTextView;//下单时间
    private TextView mPriceTextView;//价格
    private TextView mCouponsTextView;//优惠
    private TextView mCoursesAmountTextView;//实付
    private TextView mPayTypeTextView;//支付方式
    private TextView mOrderStateTextView;//订单状态


    private MyChargeGroupCoursesDetailsPresenter mCoursesDetailsPresenter;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_charge_group_courese_details);
        setTitle(getString(R.string.activity_title_my_group_details));
        initView();
        getIntentData();
        sedCoursesDetailsRequest();
    }

    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.my_charge_group_courses_details_state_view);
        mCoursesNameTextView = (TextView) findViewById(R.id.charge_courses_name);
        mTeacherNameTextView = (TextView) findViewById(R.id.charge_courses_teacher);
        mCoursesLengthTextView = (TextView) findViewById(R.id.courses_length);
        mRatingBar = (RatingBar) findViewById(R.id.rating_courses);
        mCoursesTimeTextView = (TextView) findViewById(R.id.courses_time);
        mCoursesAddressTextView = (TextView) findViewById(R.id.shop_address);
        mOrderNumberTextView = (TextView) findViewById(R.id.order_number);
        mOrderTimeTextView = (TextView) findViewById(R.id.order_time);
        mPriceTextView = (TextView) findViewById(R.id.order_price);
        mCouponsTextView = (TextView) findViewById(R.id.order_coupons);
        mCoursesAmountTextView = (TextView) findViewById(R.id.order_amount);
        mPayTypeTextView = (TextView) findViewById(R.id.order_pay_type);
        mOrderStateTextView = (TextView) findViewById(R.id.order_state);
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
        mCoursesDetailsPresenter = new MyChargeGroupCoursesDetailsPresenter(this, this);
        mCoursesDetailsPresenter.getMyGroupDetails(orderId);
    }

    @Override
    public void updateMyChargeGroupCoursesDetailsView(MyChargeGroupCoursesDetailsResult.MyChargeGroupCoursesDetails groupCoursesDetails) {
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
            int payType = groupCoursesDetails.getPayType();
            if (payType == 0) {
                mPayTypeTextView.setText(R.string.pay_wechat_type);
            } else if (payType == 1) {
                mPayTypeTextView.setText(R.string.pay_alipay_type);
            } else if (payType == 3) {
                mPayTypeTextView.setText(R.string.pay_free_type);
            }
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
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
    }
}
