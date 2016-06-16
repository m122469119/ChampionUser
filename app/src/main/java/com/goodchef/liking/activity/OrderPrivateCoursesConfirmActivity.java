package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.thirdparty.pay.alipay.AliPay;
import com.aaron.android.thirdparty.pay.alipay.OnAliPayListener;
import com.aaron.android.thirdparty.pay.weixin.WeixinPay;
import com.aaron.android.thirdparty.pay.weixin.WeixinPayListener;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.PrivateCoursesTrainItemAdapter;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.http.result.data.PayData;
import com.goodchef.liking.mvp.presenter.PrivateCoursesConfirmPresenter;
import com.goodchef.liking.mvp.view.PrivateCoursesConfirmView;
import com.goodchef.liking.utils.PayType;
import com.goodchef.liking.wxapi.WXPayEntryActivity;

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
    private TextView mCouponTitleTextView;
    private RelativeLayout mCouponsLayout;
    private TextView mCoursesMoneyTextView;
    private TextView mImmediatelyBuyBtn;

    private PrivateCoursesTrainItemAdapter mPrivateCoursesTrainItemAdapter;
    private PrivateCoursesConfirmPresenter mPrivateCoursesConfirmPresenter;

    private String trainerId;
    private String coursesId;
    private CouponsResult.CouponData.Coupon mCoupon;

    private AliPay mAliPay;//支付宝
    private WeixinPay mWeixinPay;//微信

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_private_courses_confirm);
        initView();
        initData();
        initPayModule();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.confirm_recyclerView);
        mCoursesPeopleTextView = (TextView) findViewById(R.id.courses_people);
        mCoursesNumberTextView = (TextView) findViewById(R.id.courses_number);
        mEndTimeTextView = (TextView) findViewById(R.id.end_time);
        mCouponsLayout = (RelativeLayout) findViewById(R.id.layout_coupons_courses);
        mCoursesMoneyTextView = (TextView) findViewById(R.id.courses_money);
        mImmediatelyBuyBtn = (TextView) findViewById(R.id.immediately_buy_btn);
        mCouponTitleTextView = (TextView) findViewById(R.id.select_coupon_title);

        mCouponsLayout.setOnClickListener(this);
        mImmediatelyBuyBtn.setOnClickListener(this);
        mCoursesMoneyTextView.setText("¥ 55.0");
    }

    private void initPayModule() {
        mAliPay = new AliPay(this, mOnAliPayListener);
        mWeixinPay = new WeixinPay(this, mWeixinPayListener);

    }

    private void initData() {
        trainerId = getIntent().getStringExtra(LikingLessonFragment.KEY_TRAINER_ID);
        mPrivateCoursesConfirmPresenter = new PrivateCoursesConfirmPresenter(this, this);
        sendRequest();
    }

    private void sendRequest() {
        mPrivateCoursesConfirmPresenter.orderPrivateCoursesConfirm(trainerId);
    }

    @Override
    public void updatePrivateCoursesConfirm(PrivateCoursesConfirmResult.PrivateCoursesConfirmData coursesConfirmData) {
        List<PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses> trainItemList = coursesConfirmData.getCourses();
        setTrainItem(trainItemList);
        mCoursesPeopleTextView.setText(coursesConfirmData.getPeopleNum() + " 人");
        mEndTimeTextView.setText(coursesConfirmData.getEndTime());
    }


    private void setTrainItem(List<PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses> trainItemList) {
        if (trainItemList != null && trainItemList.size() > 0) {
            for (int i = 0; i < trainItemList.size(); i++) {
                if (i == 0) {
                    trainItemList.get(0).setSelect(true);
                } else {
                    trainItemList.get(i).setSelect(false);
                }
            }
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mPrivateCoursesTrainItemAdapter = new PrivateCoursesTrainItemAdapter(this);
            mPrivateCoursesTrainItemAdapter.setData(trainItemList);
            mRecyclerView.setAdapter(mPrivateCoursesTrainItemAdapter);
            mPrivateCoursesTrainItemAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    List<PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses> trainItemList = mPrivateCoursesTrainItemAdapter.getDataList();
                    TextView tv = (TextView) view.findViewById(R.id.train_item_text);
                    if (tv != null) {
                        PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses coursesItem = (PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses) tv.getTag();
                        if (coursesItem != null) {
                            coursesId = coursesItem.getCourseId();
                            mCoursesNumberTextView.setText(coursesItem.getTimes() + " 次");
                            for (int i = 0; i < trainItemList.size(); i++) {
                                if (trainItemList.get(i).getCourseId().equals(coursesId)) {
                                    trainItemList.get(i).setSelect(true);
                                } else {
                                    trainItemList.get(i).setSelect(false);
                                }
                            }
                        }
                        mPrivateCoursesTrainItemAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public boolean onItemLongClick(View view, int position) {
                    return false;
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mCouponsLayout) {
            Intent intent = new Intent(this, CouponsActivity.class);
            intent.putExtra(CouponsActivity.KEY_COURSE_ID, coursesId);
            startActivityForResult(intent, INTENT_REQUEST_CODE_COUPON);
        } else if (v == mImmediatelyBuyBtn) {
            if (mCoupon != null) {
                mPrivateCoursesConfirmPresenter.submitPrivateCourses(coursesId, mCoupon.getCouponCode(), "1");
            } else {
                mPrivateCoursesConfirmPresenter.submitPrivateCourses(coursesId, "", "1");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_REQUEST_CODE_COUPON) {
                mCoupon = (CouponsResult.CouponData.Coupon) data.getSerializableExtra(CouponsActivity.INTENT_KEY_COUPONS_DATA);
                mCouponTitleTextView.setText(mCoupon.getTitle() + mCoupon.getAmount() + "元");
            }
        }
    }

    @Override
    public void updateSubmitOrderCourses(PayData payData) {
        handlePay(payData);
    }

    private void handlePay(PayData data) {
        int payType = data.getPayType();
        switch (payType) {
            case PayType.PAY_TYPE_ALI://支付宝支付
                mAliPay.setPayOrderInfo(data.getAliPayToken());
                mAliPay.doPay();
                break;
            case PayType.PAY_TYPE_WECHAT://微信支付
                WXPayEntryActivity.payType = WXPayEntryActivity.PAY_TYPE_ORDER;
                WXPayEntryActivity.orderId = data.getOrderId();
                mWeixinPay.setPrePayId(data.getWxPrepayId());
                mWeixinPay.doPay();
                break;
        }
    }

    /**
     * 支付宝支付结果处理
     */
    private final OnAliPayListener mOnAliPayListener = new OnAliPayListener() {
        @Override
        public void onStart() {
            LogUtils.e(TAG, "alipay start");
        }

        @Override
        public void onSuccess() {
            //  jumpToOrderDetailActivity();
        }

        @Override
        public void onFailure(String errorMessage) {

        }

        @Override
        public void confirm() {
        }
    };

    /**
     * 微信支付结果处理
     */
    private WeixinPayListener mWeixinPayListener = new WeixinPayListener() {
        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess() {
        }

        @Override
        public void onFailure(String errorMessage) {
        }
    };

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(WXPayEntryActivity.WechatPayMessage wechatMessage) {

    }

}
