package com.goodchef.liking.activity;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.MyPrivateCoursesCompleteMessage;
import com.goodchef.liking.fragment.MyPrivateCoursesFragment;
import com.goodchef.liking.http.result.MyPrivateCoursesDetailsResult;
import com.goodchef.liking.mvp.presenter.MyPrivateCoursesDetailsPresenter;
import com.goodchef.liking.mvp.view.MyPrivateCoursesDetailsView;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.LikingCallUtil;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

/**
 * 说明:我的私教课详情
 * Author shaozucheng
 * Time:16/6/20 下午7:08
 */
public class MyPrivateCoursesDetailsActivity extends AppBarActivity implements MyPrivateCoursesDetailsView, View.OnClickListener {
    private static final int COURSES_STATE_PAYED = 0;//0 已支付
    private static final int COURSES_STATE_COMPLETE = 1;//1 已完成
    private static final int COURSES_STATE_CANCEL = 2; //  2已取消
    private TextView mContactTeacherBtn;//联系教练
    private TextView mCoursesTitleTextView;//训练项目
    private TextView mCoursesStateTextView;//状态
    private TextView mPrivateTeacherNameTextView;//教练名称
    private TextView mCoursesTagsTextView;//tag
    private TextView mPeriodOfValidityTextView;//有效期
    private TextView mOrderNumberTextView;//订单号
    private TextView mOrderTimeTextView;//下单时间
    private TextView mCoursesPersonNumberTextView;//上课人数
    private TextView mTimesTextView;//上课时长
    private TextView mCoursesPriceTextView;//价格
    private TextView mFavourableTextView;//优惠
    private TextView mRealityPriceTextView;//实际价格
    private TextView mPayTypeTextView;//支付方式
    private HImageView mHImageView;
    private LinearLayout mBottomLayout;

    private TextView CoursesNotice;//公告
    private TextView mSurplusCoursesTimesTextView;//剩余课次
    private TextView mBuyCoursesTimesTextView;//购买次数
    private TextView mGoClassTimesTextView;//上课次数
    private TextView mCancelCoursesTimesTextView;//消课次数
    private TextView mBreakPromiseTimesTextView;//失约次数


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
        mCoursesTitleTextView = (TextView) findViewById(R.id.details_courses_title);
        mCoursesStateTextView = (TextView) findViewById(R.id.details_courses_state);
        mPrivateTeacherNameTextView = (TextView) findViewById(R.id.details_private_teacher_name);
        mCoursesTagsTextView = (TextView) findViewById(R.id.details_courses_tags);
        mPeriodOfValidityTextView = (TextView) findViewById(R.id.details_period_of_validity);
        mOrderNumberTextView = (TextView) findViewById(R.id.details_order_number);
        mOrderTimeTextView = (TextView) findViewById(R.id.details_order_time);
        mCoursesPersonNumberTextView = (TextView) findViewById(R.id.details_courses_person_number);
        mTimesTextView = (TextView) findViewById(R.id.details_courses_person_time);
        mCoursesPriceTextView = (TextView) findViewById(R.id.details_courses_price);
        mFavourableTextView = (TextView) findViewById(R.id.details_favourable);
        mRealityPriceTextView = (TextView) findViewById(R.id.details_reality_price);
        mPayTypeTextView = (TextView) findViewById(R.id.details_pay_type);
        mHImageView = (HImageView) findViewById(R.id.details_private_lesson_image);
        mBottomLayout = (LinearLayout) findViewById(R.id.layout_bottom);

        CoursesNotice = (TextView) findViewById(R.id.courses_notice);
        mSurplusCoursesTimesTextView = (TextView) findViewById(R.id.surplus_courses_times);
        mBuyCoursesTimesTextView = (TextView) findViewById(R.id.buy_courses_times);
        mGoClassTimesTextView = (TextView) findViewById(R.id.do_class_times);
        mCancelCoursesTimesTextView = (TextView) findViewById(R.id.cancel_courses_times);
        mBreakPromiseTimesTextView = (TextView) findViewById(R.id.break_a_promise_times);

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
                HImageLoaderSingleton.getInstance().loadImage(mHImageView, imageUrl);
            }

            mTimesTextView.setText(data.getDuration());
        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }
    }

    @Override
    public void updateComplete() {
        PopupUtils.showToast(getString(R.string.my_courses_details_complete));
        postEvent(new MyPrivateCoursesCompleteMessage());
        sendRequest();
    }

    @Override
    public void onClick(View v) {
      if (v == mContactTeacherBtn) {
            if (!StringUtils.isEmpty(mTeacherPhone)) {
                LikingCallUtil.showCallDialog(MyPrivateCoursesDetailsActivity.this, "确定联系教练吗？", mTeacherPhone);
            }
        }
    }


    private void showCompleteDialog() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(ResourceUtils.getString(R.string.confirm_complete_courese));
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UMengCountUtil.UmengBtnCount(MyPrivateCoursesDetailsActivity.this, UmengEventId.COMPLETE_MYPRIVATE_COURSES);
                sendCompleteRequest();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
    }
}
