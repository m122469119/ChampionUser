package com.goodchef.liking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PhoneUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.MyPrivateCoursesCompleteMessage;
import com.goodchef.liking.fragment.MyPrivateCoursesFragment;
import com.goodchef.liking.http.result.MyPrivateCoursesDetailsResult;
import com.goodchef.liking.mvp.presenter.MyPrivateCoursesDetailsPresenter;
import com.goodchef.liking.mvp.view.MyPrivateCoursesDetailsView;
import com.goodchef.liking.widgets.base.LikingStateView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/20 下午7:08
 */
public class MyPrivateCoursesDetailsActivity extends AppBarActivity implements MyPrivateCoursesDetailsView, View.OnClickListener {
    private static final int COURSES_STATE_PAYED = 0;//0 已支付
    private static final int COURSES_STATE_COMPLETE = 1;//1 已完成
    private static final int COURSES_STATE_CANCEL = 2; //  2已取消
    private TextView mContactTeacherBtn;//联系教练
    private TextView mCompleteCoursesBtn;//完成课程

    private TextView mCoursesTitleTextView;//训练项目
    private TextView mCoursesStateTextView;//状态
    private TextView mPrivateTeacherNameTextView;//教练名称
    private TextView mCoursesTagsTextView;//tag
    private TextView mPeriodOfValidityTextView;//有效期
    private TextView mOrderNumberTextView;//订单号
    private TextView mOrderTimeTextView;//下单时间
    private TextView mCoursesPersonNumberTextView;//上课人数
    private TextView mCoursesPriceTextView;//价格
    private TextView mFavourableTextView;//优惠
    private TextView mRealityPriceTextView;//实际价格
    private TextView mPayTypeTextView;//支付方式
    private HImageView mHImageView;
    private LinearLayout mBottomLayout;


    private MyPrivateCoursesDetailsPresenter mCoursesDetailsPresenter;
    private String orderId;
    private String mTeacherPhone;
    private LikingStateView mStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_private_courses_details);
        setTitle(getString(R.string.title_courses_details));
        initView();
        initData();
    }

    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.my_private_courses_state_view);
        mContactTeacherBtn = (TextView) findViewById(R.id.details_contact_teacher);
        mCompleteCoursesBtn = (TextView) findViewById(R.id.details_complete_courses_btn);
        mCoursesTitleTextView = (TextView) findViewById(R.id.details_courses_title);
        mCoursesStateTextView = (TextView) findViewById(R.id.details_courses_state);
        mPrivateTeacherNameTextView = (TextView) findViewById(R.id.details_private_teacher_name);
        mCoursesTagsTextView = (TextView) findViewById(R.id.details_courses_tags);
        mPeriodOfValidityTextView = (TextView) findViewById(R.id.details_period_of_validity);
        mOrderNumberTextView = (TextView) findViewById(R.id.details_order_number);
        mOrderTimeTextView = (TextView) findViewById(R.id.details_order_time);
        mCoursesPersonNumberTextView = (TextView) findViewById(R.id.details_courses_person_number);
        mCoursesPriceTextView = (TextView) findViewById(R.id.details_courses_price);
        mFavourableTextView = (TextView) findViewById(R.id.details_favourable);
        mRealityPriceTextView = (TextView) findViewById(R.id.details_reality_price);
        mPayTypeTextView = (TextView) findViewById(R.id.details_pay_type);
        mHImageView = (HImageView) findViewById(R.id.details_private_lesson_image);
        mBottomLayout = (LinearLayout) findViewById(R.id.layout_bottom);

        mCompleteCoursesBtn.setOnClickListener(this);
        mContactTeacherBtn.setOnClickListener(this);
        mStateView.setState(StateView.State.LOADING);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                sendRequest();
            }
        });
    }

    private void initData() {
        orderId = getIntent().getStringExtra(MyPrivateCoursesFragment.KEY_ORDER_ID);
        mCoursesDetailsPresenter = new MyPrivateCoursesDetailsPresenter(this, this);
        sendRequest();
    }

    private void sendRequest() {
        mCoursesDetailsPresenter.getMyPrivateCoursesDetails(orderId);
    }

    private void sendCompleteRequest() {
        mCoursesDetailsPresenter.completeMyPrivateCourses(orderId);
    }

    @Override
    public void updateMyPrivateCoursesDetailsView(MyPrivateCoursesDetailsResult.MyPrivateCoursesDetailsData data) {
        if (data != null) {
            mStateView.setState(StateView.State.SUCCESS);
            mCoursesTitleTextView.setText("课程项目: " + data.getCourseName());
            int state = data.getStatus();
            if (state == COURSES_STATE_PAYED) {
                mCoursesStateTextView.setText(R.string.courses_state_payed);
                mBottomLayout.setVisibility(View.VISIBLE);
            } else if (state == COURSES_STATE_COMPLETE) {
                mCoursesStateTextView.setText(R.string.courses_state_complete);
                mBottomLayout.setVisibility(View.GONE);
            } else if (state == COURSES_STATE_CANCEL) {
                mCoursesStateTextView.setText(R.string.courses_state_cancel);
                mBottomLayout.setVisibility(View.GONE);
            }
            mPrivateTeacherNameTextView.setText(data.getTrainerName());

            mPeriodOfValidityTextView.setText(data.getStartTime() + " ~ " + data.getEndTime());
            mOrderNumberTextView.setText(data.getOrderId());
            mOrderTimeTextView.setText(data.getOrderTime());
            mCoursesPersonNumberTextView.setText(data.getPeopleNum() + "");
            mCoursesPriceTextView.setText("¥ " + data.getTotalAmount());
            mFavourableTextView.setText("¥ " + data.getCouponAmount());
            mRealityPriceTextView.setText("¥ " + data.getActualAmount());

            int payTpe = data.getPayType();
            if (payTpe == 0) {
                mPayTypeTextView.setText(R.string.pay_wechat_type);
            } else if (payTpe == 1) {
                mPayTypeTextView.setText(R.string.pay_alipay_type);
            } else if (payTpe == 3) {
                mPayTypeTextView.setText(R.string.pay_free_type);
            }
            mTeacherPhone = data.getTrainerPhone();
            String imageUrl = data.getTrainerAvatar();
            if (!StringUtils.isEmpty(imageUrl)) {
                HImageLoaderSingleton.getInstance().requestImage(mHImageView, imageUrl);
            }
        } else {
            mStateView.initNoDataView(R.drawable.icon_no_data, "暂无数据", "刷新看看", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendRequest();
                }
            });
        }
    }

    @Override
    public void updateComplete() {
        PopupUtils.showToast("该课程以确定完成");
        postEvent(new MyPrivateCoursesCompleteMessage());
        sendRequest();
    }

    @Override
    public void onClick(View v) {
        if (v == mCompleteCoursesBtn) {
            sendCompleteRequest();
        } else if (v == mContactTeacherBtn) {
            if (!StringUtils.isEmpty(mTeacherPhone)) {
                PhoneUtils.phoneCall(this, mTeacherPhone);
            }
        }
    }

    @Override
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
    }
}
