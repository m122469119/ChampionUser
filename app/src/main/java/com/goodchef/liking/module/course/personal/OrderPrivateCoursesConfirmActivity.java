package com.goodchef.liking.module.course.personal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.base.widget.web.HDefaultWebActivity;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.pay.alipay.AliPay;
import com.aaron.pay.alipay.OnAliPayListener;
import com.aaron.pay.weixin.WeixinPay;
import com.aaron.pay.weixin.WeixinPayListener;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.PrivateCoursesTrainItemAdapter;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.BaseConfigResult;
import com.goodchef.liking.data.remote.retrofit.result.CouponsResult;
import com.goodchef.liking.data.remote.retrofit.result.OrderCalculateResult;
import com.goodchef.liking.data.remote.retrofit.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.data.remote.retrofit.result.data.PayResultData;
import com.goodchef.liking.data.remote.retrofit.result.data.PlacesData;
import com.goodchef.liking.eventmessages.BuyPrivateCoursesMessage;
import com.goodchef.liking.eventmessages.CoursesErrorMessage;
import com.goodchef.liking.module.coupons.CouponsActivity;
import com.goodchef.liking.module.course.MyLessonActivity;
import com.goodchef.liking.module.home.LikingHomeActivity;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;
import com.goodchef.liking.umeng.UmengEventId;
import com.goodchef.liking.utils.PayType;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.base.LikingStateView;
import com.goodchef.liking.wxapi.WXPayEntryActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:私教课确认购买页
 * Author shaozucheng
 * Time:16/6/15 下午6:01
 */
public class OrderPrivateCoursesConfirmActivity extends AppBarMVPSwipeBackActivity<PrivateCoursesConfirmContract.Presenter> implements PrivateCoursesConfirmContract.View {

    private static final int INTENT_REQUEST_CODE_COUPON = 100;

    private static final int PAY_TYPE = 3;//3 免金额支付

    @BindView(R.id.confirm_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.courses_times)
    TextView mCoursesTimesTextView;//上课次数
    @BindView(R.id.courses_number)
    TextView mCoursesNumberTextView;//上课人数
    @BindView(R.id.end_time)
    TextView mEndTimeTextView;//截止日期

    @BindView(R.id.layout_coupons_courses)
    RelativeLayout mCouponsLayout;
    @BindView(R.id.select_coupon_title)
    TextView mCouponTitleTextView;//优惠券信息

    @BindView(R.id.courses_money)
    TextView mCoursesMoneyTextView;//课程金额
    @BindView(R.id.immediately_buy_btn)
    TextView mImmediatelyBuyBtn;//立即购买
    @BindView(R.id.courses_address)
    TextView mCoursesAddressTextView;//课程地址
    @BindView(R.id.courses_time)
    TextView mCoursesTimeTextView;//课程时长
    @BindView(R.id.courses_times_prompt)
    TextView mCoursesTimesPrompt;//课程次数提示
    @BindView(R.id.courses_time_minus)
    ImageView mMinusImageView;//减号
    @BindView(R.id.courses_time_add)
    ImageView mAddImageView;//加号

    @BindView(R.id.layout_alipay)
    RelativeLayout mAlipayLayout;//支付布局
    @BindView(R.id.layout_wechat)
    RelativeLayout mWechatLayout;
    @BindView(R.id.pay_type_alipay_checkBox)
    CheckBox mAlipayCheckBox;
    @BindView(R.id.pay_type_wechat_checkBox)
    CheckBox mWechatCheckBox;

    @BindView(R.id.private_courses_confirm_state_view)
    LikingStateView mStateView;

    @BindView(R.id.private_buy_protocol_content)
    TextView mProtocolContentView;

    @BindView(R.id.private_buy_protocol_checkbox)
    CheckBox mProtocolCheckBox;

    private PrivateCoursesTrainItemAdapter mPrivateCoursesTrainItemAdapter;

    private String trainerId;//训练项目id
    private String teacherName;//教练姓名
    private String coursesId;//课程id
    private CouponsResult.CouponData.Coupon mCoupon;//优惠券对象

    private AliPay mAliPay;//支付宝
    private WeixinPay mWeixinPay;//微信
    private String payType = "-1";//支付方式
    private PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses coursesItem;//训练项目对象

    private int mCoursesTimes = 0;//上课次数
    private int mCoursesMinTimes;//上课最小次数
    private int mCoursesMaxTimes;//上课最大次数
    private boolean isGetMoneySuccess = true;//加或者减请求后台金额返回是否成功
    private String mAmountCount;//课程总金额

    private ArrayList<PlacesData> mPlacesDataList;//上课地点集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_private_courses_confirm);
        ButterKnife.bind(this);
        initView();
        initData();
        initPayModule();
        setPayDefaultType();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                sendRequest();
            }
        });

        mProtocolCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mImmediatelyBuyBtn.setBackgroundColor(ContextCompat.getColor(OrderPrivateCoursesConfirmActivity.this, R.color.liking_green_btn_back));
                } else {
                    mImmediatelyBuyBtn.setBackgroundColor(ContextCompat.getColor(OrderPrivateCoursesConfirmActivity.this, R.color.liking_grey_btn_back));
                }
            }
        });
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
        mStateView.setState(StateView.State.LOADING);
        sendRequest();
    }

    private void sendRequest() {
        mPresenter.orderPrivateCoursesConfirm(this, trainerId, LikingHomeActivity.gymId);
    }

    @Override
    public void updatePrivateCoursesConfirm(PrivateCoursesConfirmResult.PrivateCoursesConfirmData coursesConfirmData) {
        if (coursesConfirmData != null) {
            mStateView.setState(StateView.State.SUCCESS);
            List<PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses> trainItemList = coursesConfirmData.getCourses();
            setTrainItem(trainItemList);
            mEndTimeTextView.setText(coursesConfirmData.getEndTime());
            mPlacesDataList = (ArrayList<PlacesData>) coursesConfirmData.getPlacesList();
            setPlacesDataListData();
            mCoursesTimeTextView.setText(coursesConfirmData.getDuration());
            mCoursesNumberTextView.setText(coursesConfirmData.getPeopleNum() + getString(R.string.people));
            mAmountCount = coursesConfirmData.getAmount();
            mCoursesMoneyTextView.setText(getString(R.string.money_symbol) + mAmountCount);
        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }
    }

    /**
     * 设置上课地址
     */
    private void setPlacesDataListData() {
        if (mPlacesDataList != null && mPlacesDataList.size() > 0) {
            for (int i = 0; i < mPlacesDataList.size(); i++) {
                if (i == 0) {
                    mPlacesDataList.get(i).setSelect(true);
                } else {
                    mPlacesDataList.get(i).setSelect(false);
                }
            }
            mCoursesAddressTextView.setText(mPlacesDataList.get(0).getAddress());
        }
    }

    /**
     * 设置训练项目
     *
     * @param trainItemList 训练项目列表
     */
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
            mCoursesMinTimes = Integer.parseInt(coursesItem.getMinTimes());//获取上课最低次数
            mCoursesMaxTimes = Integer.parseInt(coursesItem.getMaxTimes());//获取上课最高次数
            mCoursesTimes = Integer.parseInt(coursesItem.getMinTimes());
            mCoursesTimesTextView.setText(mCoursesMinTimes + "");
            mCoursesTimesPrompt.setText(coursesItem.getPrompt());
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mPrivateCoursesTrainItemAdapter = new PrivateCoursesTrainItemAdapter(this);
            mPrivateCoursesTrainItemAdapter.setData(trainItemList);
            mRecyclerView.setAdapter(mPrivateCoursesTrainItemAdapter);
            setTrainItemListener();
        }
    }

    /**
     * 设置训练项目点击事件
     */
    private void setTrainItemListener() {
        mPrivateCoursesTrainItemAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(android.view.View view, int position) {
                if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
                    showToast(getString(R.string.network_error));
                    return;
                }
                List<PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses> trainItemList = mPrivateCoursesTrainItemAdapter.getDataList();
                TextView tv = (TextView) view.findViewById(R.id.train_item_text);
                if (tv != null) {
                    coursesItem = (PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses) tv.getTag();
                    if (coursesItem != null) {
                        coursesId = coursesItem.getCourseId();//记录coursesId
                        mCouponTitleTextView.setText("");//清空优惠券需要重新选择
                        mCoursesMinTimes = Integer.parseInt(coursesItem.getMinTimes());
                        mCoursesMaxTimes = Integer.parseInt(coursesItem.getMaxTimes());
                        mCoursesTimes = Integer.parseInt(coursesItem.getMinTimes());
                        mCoursesTimesTextView.setText(mCoursesMinTimes + "");
                        mCoursesTimesPrompt.setText(coursesItem.getPrompt());
                        for (int i = 0; i < trainItemList.size(); i++) {
                            if (trainItemList.get(i).getCourseId().equals(coursesId)) {
                                trainItemList.get(i).setSelect(true);
                            } else {
                                trainItemList.get(i).setSelect(false);
                            }
                        }
                    }
                    mPresenter.orderCalculate(coursesId, String.valueOf(mCoursesTimes));
                    mPrivateCoursesTrainItemAdapter.notifyDataSetChanged();
                    mCoupon = null;//置空优惠券
                }
            }

            @Override
            public boolean onItemLongClick(android.view.View view, int position) {
                return false;
            }
        });
    }

    private void showAgreeProtocolCheckBoxDialog() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_one_content, null, false);
        TextView titleTextView = (TextView) view.findViewById(R.id.one_dialog_title);
        TextView contentTextView = (TextView) view.findViewById(R.id.one_dialog_content);
        titleTextView.setText(getString(R.string.notice_prompt));
        contentTextView.setText(R.string.agree_private_agrement);
        builder.setCustomView(view);
        builder.setPositiveButton(R.string.dialog_know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        HBaseDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    @OnClick({R.id.layout_coupons_courses,
            R.id.immediately_buy_btn,
            R.id.layout_alipay,
            R.id.layout_wechat,
            R.id.courses_time_minus,
            R.id.courses_time_add,
            R.id.courses_times,
            R.id.private_buy_protocol_content,
            R.id.private_buy_protocol_view})
    public void onClick(android.view.View v) {
        switch (v.getId()) {
            case R.id.layout_coupons_courses:
                if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
                    showToast(getString(R.string.network_error));
                    return;
                } else {
                    if (coursesId != null) {
                        UMengCountUtil.UmengCount(this, UmengEventId.COUPONSACTIVITY);
                        Intent intent = new Intent(this, CouponsActivity.class);
                        intent.putExtra(CouponsActivity.KEY_COURSE_ID, coursesId);
                        intent.putExtra(CouponsActivity.KEY_SELECT_TIMES, mCoursesTimes + "");
                        intent.putExtra(LikingLessonFragment.KEY_GYM_ID, LikingHomeActivity.gymId);
                        if (mCoupon != null && !StringUtils.isEmpty(mCoupon.getCoupon_code())) {
                            intent.putExtra(CouponsActivity.KEY_COUPON_ID, mCoupon.getCoupon_code());
                        }
                        intent.putExtra(CouponsActivity.TYPE_MY_COUPONS, "DishesConfirmActivity");
                        startActivityForResult(intent, INTENT_REQUEST_CODE_COUPON);
                    }
                }
                break;
            case R.id.immediately_buy_btn:
                if (!mProtocolCheckBox.isChecked()) {
                    showAgreeProtocolCheckBoxDialog();
                } else {
                    if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
                        showToast(getString(R.string.network_error));
                        return;
                    } else {
                        sendBuyCoursesRequest();
                    }
                }
                break;
            case R.id.layout_alipay:
                mAlipayCheckBox.setChecked(true);
                mWechatCheckBox.setChecked(false);
                payType = "1";
                break;
            case R.id.layout_wechat:
                mAlipayCheckBox.setChecked(false);
                mWechatCheckBox.setChecked(true);
                payType = "0";
                break;
            case R.id.courses_time_minus:
                if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
                    showToast(getString(R.string.network_error));
                    return;
                } else {
                    doMinusCoursesTimes();
                }
                break;
            case R.id.courses_time_add:
                if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
                    showToast(getString(R.string.network_error));
                    return;
                } else {
                    doAddCoursesTimes();
                }
                break;
            case R.id.courses_times:
                if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
                    showToast(getString(R.string.network_error));
                    return;
                } else {
                    showCoursesTimesDialog();
                }
                break;
            case R.id.private_buy_protocol_content:
                BaseConfigResult baseConfigResult = LikingPreference.getBaseConfig();
                if (baseConfigResult != null) {
                    BaseConfigResult.ConfigData baseConfigData = baseConfigResult.getBaseConfigData();
                    if (baseConfigData != null) {
                        String agreeUrl = baseConfigData.getTrainerProtocol();
                        if (!StringUtils.isEmpty(agreeUrl)) {
                            HDefaultWebActivity.launch(this, agreeUrl, getString(R.string.platform_private_teacher_pro));
                        }
                    }
                }
                break;
            case R.id.private_buy_protocol_view:
                mProtocolCheckBox.setChecked(!mProtocolCheckBox.isChecked());
                break;
        }
    }

    private void showCoursesTimesDialog() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        android.view.View view = LayoutInflater.from(this).inflate(R.layout.layout_courses_time, null, false);
        final EditText mEditView = (EditText) view.findViewById(R.id.courses_times_editText);
        builder.setCustomView(view);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String times = mEditView.getText().toString();
                mCoursesTimes = Integer.parseInt(times);
                if (mCoursesTimes < mCoursesMinTimes) {
                    showToast(getString(R.string.courses_minnum_buy) + mCoursesMinTimes + getString(R.string.times_courses));
                } else if (mCoursesTimes > mCoursesMaxTimes) {
                    showToast(getString(R.string.courses_macnum_buy) + mCoursesMaxTimes + getString(R.string.times_courses));
                } else {
                    mCoursesTimesTextView.setText(mCoursesTimes + "");
                    mPresenter.orderCalculate(coursesId, String.valueOf(mCoursesTimes));
                    mCouponTitleTextView.setText("");//清空优惠券需要重新选择
                    mCoupon = null;
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    /**
     * 发送购买私教课请求
     */
    private void sendBuyCoursesRequest() {
        if (payType.equals("-1")) {
            showToast(getString(R.string.please_select_pay_type));
            return;
        }
        UMengCountUtil.UmengBtnCount(OrderPrivateCoursesConfirmActivity.this, UmengEventId.PRIVATE_IMMEDIATELY_BUY_BUTTON);
        if (mCoupon != null) {
            mPresenter.submitPrivateCourses(this, mCoupon.getCoupon_code(), payType, mCoursesTimes, LikingHomeActivity.gymId, coursesId);
        } else {
            mPresenter.submitPrivateCourses(this, "", payType, mCoursesTimes, LikingHomeActivity.gymId, coursesId);
        }
    }

    /**
     * 计算上课次数 - 减法
     */
    private void doMinusCoursesTimes() {
        if (isGetMoneySuccess) {
            if (mCoursesTimes > mCoursesMinTimes) {
                mCoursesTimes--;
                mCoursesTimesTextView.setText(mCoursesTimes + "");
                mPresenter.orderCalculate(coursesId, String.valueOf(mCoursesTimes));
                mCouponTitleTextView.setText("");//清空优惠券需要重新选择
                mCoupon = null;
            } else {
                showToast(getString(R.string.courses_minnum_buy) + mCoursesMinTimes + getString(R.string.times_courses));
            }
        }
    }

    /**
     * 计算上课次数 - 加法
     */
    private void doAddCoursesTimes() {
        if (isGetMoneySuccess) {
            if (mCoursesTimes < mCoursesMaxTimes) {
                mCoursesTimes++;
                mCoursesTimesTextView.setText(mCoursesTimes + "");
                mPresenter.orderCalculate(coursesId, String.valueOf(mCoursesTimes));
                mCouponTitleTextView.setText("");//清空优惠券需要重新选择
                mCoupon = null;
            } else {
                showToast(getString(R.string.courses_macnum_buy) + mCoursesMaxTimes + getString(R.string.times_courses));
            }
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
            }
        }
    }

    /**
     * 处理优惠券
     */
    private void handleCoupons(CouponsResult.CouponData.Coupon mCoupon) {
        String couponAmountStr = mCoupon.getAmount();//优惠券的面额
        double coursesPrice = Double.parseDouble(mAmountCount);
        double couponAmount = Double.parseDouble(couponAmountStr);
        mCouponTitleTextView.setText(mCoupon.getAmount() + getString(R.string.yuan));
        if (coursesPrice >= couponAmount) {
            //课程的价格大于优惠券的面额
            double amount = coursesPrice - couponAmount;
            if (amount >= 0) {
                mCoursesMoneyTextView.setText(getString(R.string.money_symbol) + amount);
            }
        } else {//课程的面额小于优惠券的面额
            mCoursesMoneyTextView.setText(getString(R.string.money_symbol) + "0.00");
        }
    }

    @Override
    public void updateSubmitOrderCourses(PayResultData payData) {
        int payType = payData.getPayType();
        if (payType == PAY_TYPE) {//3 免金额支付
            PopupUtils.showToast(this, R.string.pay_success);
            postEvent(new BuyPrivateCoursesMessage());
            jumpToMyCoursesActivity();
        } else {
            handlePay(payData);
        }
    }

    @Override
    public void updateOrderCalculate(boolean isSuccess, OrderCalculateResult.OrderCalculateData orderCalculateData) {
        if (isSuccess) {
            isGetMoneySuccess = isSuccess;
            mAmountCount = orderCalculateData.getAmount();
            mCoursesMoneyTextView.setText(getString(R.string.money_symbol) + mAmountCount);
            mEndTimeTextView.setText(orderCalculateData.getEndTime());
            mCoursesTimeTextView.setText(orderCalculateData.getDuration());
        } else {
            isGetMoneySuccess = false;
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
            setPayFailView();
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
        } else {
            setPayFailView();
        }
    }

    private void setPayFailView() {
        if (mCoupon != null && !StringUtils.isEmpty(mCoupon.getAmount())) {
            mCouponTitleTextView.setText("");
            mCoursesMoneyTextView.setText(getString(R.string.money_symbol) + mAmountCount);
            mCoupon = null;
        }
    }

    public void onEvent(CoursesErrorMessage message) {
        this.finish();
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
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }

    @Override
    public void setPresenter() {
        mPresenter = new PrivateCoursesConfirmContract.Presenter();
    }
}
