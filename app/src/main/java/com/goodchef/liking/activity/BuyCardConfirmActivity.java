package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.CardRecyclerAdapter;
import com.goodchef.liking.fragment.LikingBuyCardFragment;
import com.goodchef.liking.http.result.ConfirmBuyCardResult;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.data.ConfirmCard;
import com.goodchef.liking.mvp.presenter.ConfirmBuyCardPresenter;
import com.goodchef.liking.mvp.view.ConfirmBuyCardView;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/17 下午5:55
 */
public class BuyCardConfirmActivity extends AppBarActivity implements View.OnClickListener, ConfirmBuyCardView {
    private static final int INTENT_REQUEST_CODE_COUPON = 101;
    private HImageView mHImageView;
    private TextView mPeriodOfValidityTextView;//有效期
    private RecyclerView mCardRecyclerView;

    private RelativeLayout mCouponsLayout;
    private TextView mCoursesMoneyTextView;

    private RelativeLayout mAlipayLayout;
    private RelativeLayout mWechatLayout;
    private CheckBox mAlipayCheckBox;
    private CheckBox mWechatCheckBox;

    private TextView mCardMoneyTextView;
    private TextView mImmediatelyBuyBtn;

    private String mCardName;
    private int mCategoryId;
    private CouponsResult.CouponData.Coupon mCoupon;//优惠券对象
    private int payType;//支付方式
    private ConfirmBuyCardPresenter mConfirmBuyCardPresenter;

    private CardRecyclerAdapter mCardRecyclerAdapter;
    private List<ConfirmCard> confirmCardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_card_confirm);
        initView();
        setViewOnClickListener();
        initData();
    }

    private void initView() {
        mHImageView = (HImageView) findViewById(R.id.buy_card_confirm_image);
        mPeriodOfValidityTextView = (TextView) findViewById(R.id.period_of_validity);

        mCardRecyclerView = (RecyclerView) findViewById(R.id.card_recyclerView);

        mCouponsLayout = (RelativeLayout) findViewById(R.id.layout_coupons_courses);
        mCoursesMoneyTextView = (TextView) findViewById(R.id.courses_money);

        mAlipayLayout = (RelativeLayout) findViewById(R.id.layout_alipay);
        mWechatLayout = (RelativeLayout) findViewById(R.id.layout_wechat);
        mAlipayCheckBox = (CheckBox) findViewById(R.id.pay_type_alipay_checkBox);
        mWechatCheckBox = (CheckBox) findViewById(R.id.pay_type_wechat_checkBox);

        mCardMoneyTextView = (TextView) findViewById(R.id.card_money);
        mImmediatelyBuyBtn = (TextView) findViewById(R.id.immediately_buy_btn);
    }

    private void setViewOnClickListener() {

        mAlipayLayout.setOnClickListener(this);
        mWechatLayout.setOnClickListener(this);
        mCouponsLayout.setOnClickListener(this);
        mImmediatelyBuyBtn.setOnClickListener(this);
    }

    private void initData() {
        mCardName = getIntent().getStringExtra(LikingBuyCardFragment.KEY_CARD_CATEGORY);
        mCategoryId = getIntent().getIntExtra(LikingBuyCardFragment.KEY_CATEGORY_ID, -1);
        setTitle("购买" + mCardName);
        mCardRecyclerAdapter = new CardRecyclerAdapter(this);
        sendConfirmCardRequest();
    }

    private void sendConfirmCardRequest() {
        mConfirmBuyCardPresenter = new ConfirmBuyCardPresenter(this, this);
        mConfirmBuyCardPresenter.confirmBuyCard(1, mCategoryId);
    }

    @Override
    public void onClick(View v) {
        if (v == mAlipayLayout) {//选择支付宝
            mAlipayCheckBox.setChecked(true);
            mWechatCheckBox.setChecked(false);
            payType = 1;
        } else if (v == mWechatLayout) {//选择微信
            mAlipayCheckBox.setChecked(false);
            mWechatCheckBox.setChecked(true);
            payType = 0;
        } else if (v == mCouponsLayout) {//选优惠券
            Intent intent = new Intent(this, CouponsActivity.class);
            intent.putExtra(CouponsActivity.TYPE_MY_COUPONS, "BuyCardConfirmActivity");
            startActivityForResult(intent, INTENT_REQUEST_CODE_COUPON);
        } else if (v == mImmediatelyBuyBtn) {
            PopupUtils.showToast("开发中");
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
        String minAmountStr = mCoupon.getMinAmount();//优惠券最低使用标准
        String couponAmountStr = mCoupon.getAmount();//优惠券的面额
        double couponAmount = Double.parseDouble(couponAmountStr);
        double minAmount = Double.parseDouble(minAmountStr);

    }

    @Override
    public void updateConfirmBuyCardView(ConfirmBuyCardResult.ConfirmBuyCardData confirmBuyCardData) {
        String imageUrl = confirmBuyCardData.getAdsUrl();
        if (!StringUtils.isEmpty(imageUrl)) {
            HImageLoaderSingleton.getInstance().requestImage(mHImageView, imageUrl);
        }
        mPeriodOfValidityTextView.setText(confirmBuyCardData.getDeadLine());
        mCardMoneyTextView.setText(confirmBuyCardData.getPrice());

        confirmCardList = confirmBuyCardData.getCardList();
        setCardView(confirmCardList);
        mCardRecyclerAdapter.setLayoutOnClickListner(mClickListener);
    }

    /**
     * 设置
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LinearLayout mLayout = (LinearLayout) v.findViewById(R.id.layout_confirm_card);
            if (mLayout != null) {
                ConfirmCard object = (ConfirmCard) mLayout.getTag();
                if (object != null) {
                    for (ConfirmCard data : confirmCardList) {
                        if (data.getType() == object.getType()) {
                            data.setSelect(true);
                        } else {
                            data.setSelect(false);
                        }
                    }
                    mCardRecyclerAdapter.notifyDataSetChanged();
                }
            }
        }
    };


    /**
     * 设置card
     */
    private void setCardView(List<ConfirmCard> confirmCardList) {
        if (confirmCardList != null && confirmCardList.size() > 0) {
            for (ConfirmCard data : confirmCardList) {
                if (data.getType() == 2) {
                    data.setSelect(true);
                } else {
                    data.setSelect(false);
                }
            }
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mCardRecyclerView.setLayoutManager(mLayoutManager);
            mCardRecyclerAdapter.setData(confirmCardList);
            mCardRecyclerView.setAdapter(mCardRecyclerAdapter);
        }
    }


}
