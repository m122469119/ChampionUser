package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.thirdparty.pay.alipay.AliPay;
import com.aaron.android.thirdparty.pay.alipay.OnAliPayListener;
import com.aaron.android.thirdparty.pay.weixin.WeixinPay;
import com.aaron.android.thirdparty.pay.weixin.WeixinPayListener;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.PrivateCoursesTrainItemAdapter;
import com.goodchef.liking.eventmessages.BuyPrivateCoursesMessage;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.OrderCalculateResult;
import com.goodchef.liking.http.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.http.result.data.PayResultData;
import com.goodchef.liking.http.result.data.PlacesData;
import com.goodchef.liking.mvp.presenter.PrivateCoursesConfirmPresenter;
import com.goodchef.liking.mvp.view.PrivateCoursesConfirmView;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.PayType;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.base.LikingStateView;
import com.goodchef.liking.wxapi.WXPayEntryActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:私教课确认购买页
 * Author shaozucheng
 * Time:16/6/15 下午6:01
 */
public class OrderPrivateCoursesConfirmActivity extends AppBarActivity implements PrivateCoursesConfirmView, View.OnClickListener {
    private static final int INTENT_REQUEST_CODE_COUPON = 100;
    private static final int INTENT_REQUEST_CODE_CHANGE_PLACE = 110;

    private static final int PAY_TYPE = 3;//3 免金额支付
    public static final String KEY_CHANGE_ADDRESS = "key_change_address";

    private RecyclerView mRecyclerView;
    private TextView mCoursesPeopleTextView;//上课人数
    private TextView mCoursesNumberTextView;//约课次数
    private TextView mEndTimeTextView;//截止日期
    private TextView mCouponTitleTextView;//优惠券信息
    private RelativeLayout mCouponsLayout;
    private TextView mCoursesMoneyTextView;//课程金额
    private TextView mImmediatelyBuyBtn;//立即购买
    private TextView mCoursesAddressTextView;//课程地址
    private TextView mCoursesTimeTextView;//课程时间
    private ImageView mMinusImageView;
    private ImageView mAddImageView;

    private RelativeLayout mAlipayLayout;//支付布局
    private RelativeLayout mWechatLayout;
    private CheckBox mAlipayCheckBox;
    private CheckBox mWechatCheckBox;

    private PrivateCoursesTrainItemAdapter mPrivateCoursesTrainItemAdapter;
    private PrivateCoursesConfirmPresenter mPrivateCoursesConfirmPresenter;

    private String trainerId;//训练项目id
    private String teacherName;//教练姓名
    private String coursesId;//课程id
    private CouponsResult.CouponData.Coupon mCoupon;//优惠券对象

    private AliPay mAliPay;//支付宝
    private WeixinPay mWeixinPay;//微信
    private PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses coursesItem;//训练项目对象
    private String payType = "-1";//支付方式

    private LikingStateView mStateView;
    private int mPeopleNum = 0;
    private ArrayList<PlacesData> mPlacesDataList;
    private String gymId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_private_courses_confirm);
        initView();
        initData();
        initPayModule();
        setViewOnClickListener();
        setPayDefaultType();
    }

    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.private_courses_confirm_state_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.confirm_recyclerView);
        mCoursesPeopleTextView = (TextView) findViewById(R.id.courses_people);
        mCoursesNumberTextView = (TextView) findViewById(R.id.courses_number);
        mEndTimeTextView = (TextView) findViewById(R.id.end_time);
        mCouponsLayout = (RelativeLayout) findViewById(R.id.layout_coupons_courses);
        mCoursesMoneyTextView = (TextView) findViewById(R.id.courses_money);
        mImmediatelyBuyBtn = (TextView) findViewById(R.id.immediately_buy_btn);
        mCouponTitleTextView = (TextView) findViewById(R.id.select_coupon_title);
        mCoursesAddressTextView = (TextView) findViewById(R.id.courses_address);
        mCoursesTimeTextView = (TextView) findViewById(R.id.courses_time);
        mMinusImageView = (ImageView) findViewById(R.id.courses_time_minus);
        mAddImageView = (ImageView) findViewById(R.id.courses_time_add);

        mAlipayLayout = (RelativeLayout) findViewById(R.id.layout_alipay);
        mWechatLayout = (RelativeLayout) findViewById(R.id.layout_wechat);
        mAlipayCheckBox = (CheckBox) findViewById(R.id.pay_type_alipay_checkBox);
        mWechatCheckBox = (CheckBox) findViewById(R.id.pay_type_wechat_checkBox);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                sendRequest();
            }
        });
    }

    private void setViewOnClickListener() {
        mCouponsLayout.setOnClickListener(this);
        mImmediatelyBuyBtn.setOnClickListener(this);
        mAlipayLayout.setOnClickListener(this);
        mWechatLayout.setOnClickListener(this);
        mMinusImageView.setOnClickListener(this);
        mAddImageView.setOnClickListener(this);
        mCoursesAddressTextView.setOnClickListener(this);
    }

    private void initPayModule() {
        mAliPay = new AliPay(this, mOnAliPayListener);
        mWeixinPay = new WeixinPay(this, mWeixinPayListener);
    }

    private void setPayDefaultType() {
        mAlipayCheckBox.setChecked(true);
        mWechatCheckBox.setChecked(false);
        payType = "1";
    }

    private void initData() {
        trainerId = getIntent().getStringExtra(LikingLessonFragment.KEY_TRAINER_ID);
        teacherName = getIntent().getStringExtra(LikingLessonFragment.KEY_TEACHER_NAME);
        setTitle(teacherName);
        mPrivateCoursesConfirmPresenter = new PrivateCoursesConfirmPresenter(this, this);
        mStateView.setState(StateView.State.LOADING);
        sendRequest();
    }

    private void sendRequest() {
        mPrivateCoursesConfirmPresenter.orderPrivateCoursesConfirm(trainerId);
    }

    @Override
    public void updatePrivateCoursesConfirm(PrivateCoursesConfirmResult.PrivateCoursesConfirmData coursesConfirmData) {
        if (coursesConfirmData != null) {
            mStateView.setState(StateView.State.SUCCESS);
            List<PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses> trainItemList = coursesConfirmData.getCourses();
            setTrainItem(trainItemList);
            mCoursesPeopleTextView.setText(coursesConfirmData.getPeopleNum() + "");
            mEndTimeTextView.setText(coursesConfirmData.getEndTime());
            mPlacesDataList = (ArrayList<PlacesData>) coursesConfirmData.getPlacesList();
            setPlacesDataListData();
            mCoursesTimeTextView.setText(coursesConfirmData.getDuration());
        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }
    }

    private void setPlacesDataListData() {
        if (mPlacesDataList != null && mPlacesDataList.size() > 0) {
            for (int i = 0; i < mPlacesDataList.size(); i++) {
                if (i == 0) {
                    mPlacesDataList.get(i).setSelect(true);
                } else {
                    mPlacesDataList.get(i).setSelect(false);
                }
            }
            gymId = mPlacesDataList.get(0).getGymId();
            mCoursesAddressTextView.setText(mPlacesDataList.get(0).getAddress());
        }
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
            coursesItem = trainItemList.get(0);
            coursesId = coursesItem.getCourseId();
            mCoursesNumberTextView.setText(coursesItem.getMinTimes() + " 次");
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
                        coursesItem = (PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses) tv.getTag();
                        if (coursesItem != null) {
                            coursesId = coursesItem.getCourseId();//记录coursesId
                            mCouponTitleTextView.setText("");//清空优惠券需要重新选择
                            mCoursesNumberTextView.setText(coursesItem.getMinTimes() + " 次");
                            for (int i = 0; i < trainItemList.size(); i++) {
                                if (trainItemList.get(i).getCourseId().equals(coursesId)) {
                                    trainItemList.get(i).setSelect(true);
                                } else {
                                    trainItemList.get(i).setSelect(false);
                                }
                            }
                        }
//                        mCoursesMoneyTextView.setText("¥ " + coursesItem.getPrice());
//                        mCoursesTimeTextView.setText(coursesItem.getDuration());
//                        mCoursesAddressTextView.setText(coursesItem.getCourseAddress());
                        mPrivateCoursesTrainItemAdapter.notifyDataSetChanged();
                        mCoupon = null;
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
            if (coursesId != null) {
                UMengCountUtil.UmengCount(this, UmengEventId.COUPONSACTIVITY);
                Intent intent = new Intent(this, CouponsActivity.class);
                intent.putExtra(CouponsActivity.KEY_COURSE_ID, coursesId);
                if (mCoupon != null && !StringUtils.isEmpty(mCoupon.getCouponCode())) {
                    intent.putExtra(CouponsActivity.KEY_COUPON_ID, mCoupon.getCouponCode());
                }
                intent.putExtra(CouponsActivity.TYPE_MY_COUPONS, "DishesConfirmActivity");
                startActivityForResult(intent, INTENT_REQUEST_CODE_COUPON);
            }
        } else if (v == mImmediatelyBuyBtn) {
            if (payType.equals("-1")) {
                PopupUtils.showToast("请选择支付方式");
                return;
            }
            UMengCountUtil.UmengBtnCount(OrderPrivateCoursesConfirmActivity.this, UmengEventId.PRIVATE_IMMEDIATELY_BUY_BUTTON);
            if (mCoupon != null) {
                mPrivateCoursesConfirmPresenter.submitPrivateCourses(coursesId, mCoupon.getCouponCode(), payType);
            } else {
                mPrivateCoursesConfirmPresenter.submitPrivateCourses(coursesId, "", payType);
            }
        } else if (v == mAlipayLayout) {
            mAlipayCheckBox.setChecked(true);
            mWechatCheckBox.setChecked(false);
            payType = "1";
        } else if (v == mWechatLayout) {
            mAlipayCheckBox.setChecked(false);
            mWechatCheckBox.setChecked(true);
            payType = "0";
        } else if (v == mMinusImageView) {
            ++mPeopleNum;
            mCoursesPeopleTextView.setText(mPeopleNum + "");
        } else if (v == mAddImageView) {
            --mPeopleNum;
            mCoursesPeopleTextView.setText(mPeopleNum + "");
        } else if (v == mCoursesAddressTextView) {
            Intent intent = new Intent(this, ChangeAddressActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(KEY_CHANGE_ADDRESS, mPlacesDataList);
            intent.putExtras(bundle);
            startActivityForResult(intent, INTENT_REQUEST_CODE_CHANGE_PLACE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_REQUEST_CODE_COUPON) {
                mCoupon = (CouponsResult.CouponData.Coupon) data.getSerializableExtra(CouponsActivity.INTENT_KEY_COUPONS_DATA);
                if (mCoupon != null) {
                    handleCoupons(mCoupon);
                }
            } else if (requestCode == INTENT_REQUEST_CODE_CHANGE_PLACE) {
                PlacesData placesData = (PlacesData) data.getSerializableExtra(ChangeAddressActivity.KEY_ADDRESS_DATA);
                if (placesData != null) {
                    mCoursesAddressTextView.setText(placesData.getAddress());
                    gymId = placesData.getGymId();
                    for (PlacesData place : mPlacesDataList) {
                        if (place.getGymId().equals(placesData.getGymId())) {
                            place.setSelect(placesData.isSelect());
                        } else {
                            place.setSelect(false);
                        }
                    }
                }
            }
        }
    }


    /**
     * 处理优惠券
     */
    private void handleCoupons(CouponsResult.CouponData.Coupon mCoupon) {
        String minAmountStr = mCoupon.getMinAmount();//优惠券最低使用标准
        String couponAmountStr = mCoupon.getAmount();//优惠券的面额
//        String priceStr = coursesItem.getPrice();//课程价格
        String priceStr = "20";
        double coursesPrice = Double.parseDouble(priceStr);
        double couponAmount = Double.parseDouble(couponAmountStr);
        double minAmount = Double.parseDouble(minAmountStr);
        if (coursesPrice >= minAmount) {//课程价格>优惠券最低使用值，该优惠券可用
            mCouponTitleTextView.setText(mCoupon.getTitle() + mCoupon.getAmount() + " 元");
            if (coursesPrice >= couponAmount) {
                //课程的价格大于优惠券的面额
                double amount = coursesPrice - couponAmount;
                if (amount >= 0) {
                    mCoursesMoneyTextView.setText("¥ " + amount);
                }
            } else {//课程的面额小于优惠券的面额
                mCoursesMoneyTextView.setText("¥ " + "0.00");
            }
        } else {//优惠券不可用
            mCouponTitleTextView.setText("");
        }
    }

    @Override
    public void updateSubmitOrderCourses(PayResultData payData) {
        int payType = payData.getPayType();
        if (payType == PAY_TYPE) {//3 免金额支付
            PopupUtils.showToast("支付成功");
            jumpToMyCoursesActivity();
        } else {
            handlePay(payData);
        }
    }

    @Override
    public void updateOrderCalculate(boolean isSuccess, OrderCalculateResult.OrderCalculateData orderCalculateData) {
        if (isSuccess){

        }
    }


    private void handlePay(PayResultData data) {
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
            jumpToMyCoursesActivity();
            postEvent(new BuyPrivateCoursesMessage());
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
        if (wechatMessage.isPaySuccess()) {
            jumpToMyCoursesActivity();
        }
    }

    /**
     * 跳转我的课程列表
     */
    private void jumpToMyCoursesActivity() {
        Intent intent = new Intent(this, MyLessonActivity.class);
        intent.putExtra(MyLessonActivity.KEY_CURRENT_ITEM, 1);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
    }
}
