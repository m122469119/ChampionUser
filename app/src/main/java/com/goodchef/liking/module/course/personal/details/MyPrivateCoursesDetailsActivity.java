package com.goodchef.liking.module.course.personal.details;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.MyPrivateCoursesCompleteMessage;
import com.goodchef.liking.http.result.MyPrivateCoursesDetailsResult;
import com.goodchef.liking.module.course.personal.MyPrivateCoursesFragment;
import com.goodchef.liking.utils.LikingCallUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:我的私教课详情
 * Author shaozucheng
 * Time:16/6/20 下午7:08
 */
public class MyPrivateCoursesDetailsActivity extends AppBarActivity implements MyPersonalCourseDetailsContract.MyPrivateCoursesDetailsView {

    private static final int COURSES_STATE_PAYED = 0;//0 已支付
    private static final int COURSES_STATE_COMPLETE = 1;//1 已完成
    private static final int COURSES_STATE_CANCEL = 2; //  2已取消

    @BindView(R.id.my_private_courses_state_view)
    LikingStateView mStateView;
    @BindView(R.id.details_contact_teacher)
    TextView mContactTeacherBtn;//联系教练
    @BindView(R.id.details_courses_title)
    TextView mCoursesTitleTextView;//训练项目
    @BindView(R.id.details_courses_state)
    TextView mCoursesStateTextView;//状态
    @BindView(R.id.details_private_teacher_name)
    TextView mPrivateTeacherNameTextView;//教练名称
    @BindView(R.id.details_courses_tags)
    TextView mCoursesTagsTextView;//tag
    @BindView(R.id.details_period_of_validity)
    TextView mPeriodOfValidityTextView;//有效期
    @BindView(R.id.details_order_number)
    TextView mOrderNumberTextView;//订单号
    @BindView(R.id.details_order_time)
    TextView mOrderTimeTextView;//下单时间
    @BindView(R.id.details_courses_person_number)
    TextView mCoursesPersonNumberTextView;//上课人数
    @BindView(R.id.details_courses_person_time)
    TextView mTimesTextView;//上课时长
    @BindView(R.id.details_courses_price)
    TextView mCoursesPriceTextView;//价格
    @BindView(R.id.details_favourable)
    TextView mFavourableTextView;//优惠
    @BindView(R.id.details_reality_price)
    TextView mRealityPriceTextView;//实际价格
    @BindView(R.id.details_pay_type)
    TextView mPayTypeTextView;//支付方式
    @BindView(R.id.details_private_lesson_image)
    HImageView mHImageView;
    @BindView(R.id.layout_bottom)
    LinearLayout mBottomLayout;

    @BindView(R.id.courses_notice)
    TextView CoursesNotice;//公告
    @BindView(R.id.surplus_courses_times)
    TextView mSurplusCoursesTimesTextView;//剩余课次
    @BindView(R.id.buy_courses_times)
    TextView mBuyCoursesTimesTextView;//购买次数
    @BindView(R.id.do_class_times)
    TextView mGoClassTimesTextView;//上课次数
    @BindView(R.id.cancel_courses_times)
    TextView mCancelCoursesTimesTextView;//消课次数
    @BindView(R.id.break_a_promise_times)
    TextView mBreakPromiseTimesTextView;//失约次数

    private MyPersonalCourseDetailsContract.MyPrivateCoursesDetailsPresenter mCoursesDetailsPresenter;
    private String orderId;
    private String mTeacherPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_private_courses_details);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_courses_details));
        initView();
        initData();
        setRightMenu();
    }

    private void initView() {
        mStateView.setState(StateView.State.LOADING);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                sendRequest();
            }
        });
    }

    private void setRightMenu() {
        setRightIcon(R.drawable.icon_phone, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikingCallUtil.showPhoneDialog(MyPrivateCoursesDetailsActivity.this);
            }
        });
    }

    private void initData() {
        orderId = getIntent().getStringExtra(MyPrivateCoursesFragment.KEY_ORDER_ID);
        mCoursesDetailsPresenter = new MyPersonalCourseDetailsContract.MyPrivateCoursesDetailsPresenter(this, this);
        sendRequest();
    }

    private void sendRequest() {
        mCoursesDetailsPresenter.getMyPrivateCoursesDetails(orderId);
    }


    @Override
    public void updateMyPrivateCoursesDetailsView(MyPrivateCoursesDetailsResult.MyPrivateCoursesDetailsData data) {
        if (data != null) {
            mStateView.setState(StateView.State.SUCCESS);
            Typeface typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/Impact.ttf");
            mSurplusCoursesTimesTextView.setTypeface(typeFace);
            mBuyCoursesTimesTextView.setTypeface(typeFace);
            mCancelCoursesTimesTextView.setTypeface(typeFace);
            mGoClassTimesTextView.setTypeface(typeFace);
            mBreakPromiseTimesTextView.setTypeface(typeFace);

            CoursesNotice.setText(data.getPrompt());
            mSurplusCoursesTimesTextView.setText(data.getLeft_times());
            mBuyCoursesTimesTextView.setText(data.getBuyTimes());
            mCancelCoursesTimesTextView.setText(data.getDestroy_times());
            mGoClassTimesTextView.setText(data.getCompleteTimes());
            mBreakPromiseTimesTextView.setText(data.getMiss_times());

            mCoursesTitleTextView.setText(getString(R.string.courses_project) + data.getCourseName());
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
            mCoursesPriceTextView.setText(getString(R.string.money_symbol) + data.getTotalAmount());
            mFavourableTextView.setText(getString(R.string.money_symbol) + data.getCouponAmount());
            mRealityPriceTextView.setText(getString(R.string.money_symbol) + data.getActualAmount());

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
                HImageLoaderSingleton.loadImage(mHImageView, imageUrl, this);
            }

            mTimesTextView.setText(data.getDuration());
        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }
    }

    @Override
    public void updateComplete() {
        showToast(getString(R.string.my_courses_details_complete));
        postEvent(new MyPrivateCoursesCompleteMessage());
        sendRequest();
    }

    @OnClick(R.id.details_contact_teacher)
    public void click(View view) {
        if (!StringUtils.isEmpty(mTeacherPhone)) {
            LikingCallUtil.showCallDialog(this,getString(R.string.confirm_call),mTeacherPhone);
        }
    }

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }
}
