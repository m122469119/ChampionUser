package com.goodchef.liking.module.coupons;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.InputMethodManagerUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.module.card.buy.confirm.BuyCardConfirmActivity;
import com.goodchef.liking.eventmessages.CouponErrorMessage;
import com.goodchef.liking.eventmessages.ExchangeCouponsMessage;
import com.goodchef.liking.module.card.buy.LikingBuyCardFragment;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;
import com.goodchef.liking.data.remote.retrofit.result.CouponsPersonResult;
import com.goodchef.liking.data.remote.retrofit.result.CouponsResult;
import com.goodchef.liking.data.remote.retrofit.result.data.Food;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:我的优惠券
 * Author shaozucheng
 * Time:16/6/16 下午2:17
 */
public class CouponsActivity extends AppBarActivity  implements CouponContract.CouponView{
    public static final String KEY_COURSE_ID = "key_course_id";
    public static final String TYPE_MY_COUPONS = "MyCoupons";
    public static final String INTENT_KEY_COUPONS_DATA = "intent_key_coupons_data";
    public static final String KEY_COUPON_ID = "key_coupon_id";
    public static final String KEY_SCHEDULE_ID = "schedule_id";
    public static final String KEY_SELECT_TIMES = "select_times";

    @BindView(R.id.edit_coupons_number)
    EditText mEditCoupons;//填写优惠券
    @BindView(R.id.exchange_coupons_button)
    TextView mExchangeButton;//兑换优惠券按钮
    @BindView(R.id.layout_exchange_coupon)
    LinearLayout mExchangeCouponsLayout;//优惠券布局

    private String intentType = "";
    private String coursesId;//课程id
    private String selectTimes;//私教课购买次数
    private ArrayList<Food> confirmBuyList = new ArrayList<>();
    private String cardId;//会员卡id
    private String type;//类型
    private String couponId;//优惠券id
    private String scheduleId;//排期id
    private String gymId;//场馆id

    private CouponContract.CouponPresenter mCouponPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);
        ButterKnife.bind(this);
        mCouponPresenter = new CouponContract.CouponPresenter(this,this);
        initData();
        doExchangeCoupons();
    }

    private void initData() {
        coursesId = getIntent().getStringExtra(KEY_COURSE_ID);
        selectTimes = getIntent().getStringExtra(KEY_SELECT_TIMES);
        intentType = getIntent().getStringExtra(TYPE_MY_COUPONS);
        Bundle bundle = getIntent().getExtras();
        cardId = getIntent().getStringExtra(BuyCardConfirmActivity.KEY_CARD_ID);
        type = getIntent().getStringExtra(LikingBuyCardFragment.KEY_BUY_TYPE);
        couponId = getIntent().getStringExtra(KEY_COUPON_ID);
        scheduleId = getIntent().getStringExtra(KEY_SCHEDULE_ID);
        gymId = getIntent().getStringExtra(LikingLessonFragment.KEY_GYM_ID);

        mExchangeCouponsLayout.setVisibility(View.VISIBLE);
        if (intentType.equals(TYPE_MY_COUPONS)) {
            setTitle(getString(R.string.title_activity_my_coupons));
        } else {
            setTitle(getString(R.string.title_activity_select_coupon));
        }
        setCouponsFragment();
    }

    private void setCouponsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_COURSE_ID, coursesId);
        bundle.putString(KEY_SELECT_TIMES, selectTimes);
        bundle.putString(TYPE_MY_COUPONS, intentType);
        bundle.putString(BuyCardConfirmActivity.KEY_CARD_ID, cardId);
        bundle.putString(LikingBuyCardFragment.KEY_BUY_TYPE, type);
        bundle.putString(KEY_SCHEDULE_ID, scheduleId);
        bundle.putString(KEY_COUPON_ID, couponId);
        bundle.putString(LikingLessonFragment.KEY_GYM_ID, gymId);
        fragmentTransaction.add(R.id.my_coupons_fragment, CouponsFragment.newInstance(bundle));
        fragmentTransaction.commit();
    }

    private void doExchangeCoupons() {
        mExchangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManagerUtils.hideKeyboard(mEditCoupons);
                String couponsNumber = mEditCoupons.getText().toString().trim();
                if (StringUtils.isEmpty(couponsNumber)) {
                    showToast(getString(R.string.input_coupon_code));
                } else {
                    mCouponPresenter.sendExchangeCouponsRequest(couponsNumber);
                }
            }
        });
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(CouponErrorMessage message) {
        finish();
    }

    @Override
    public void updateExchangeCode() {
        showToast(getString(R.string.exchange_success));
        mEditCoupons.setText("");//清空兑换码
        postEvent(new ExchangeCouponsMessage());
    }

    @Override
    public void updateMyCouponData(CouponsPersonResult.DataBean dataBean) {

    }

    @Override
    public void updateCouponData(CouponsResult.CouponData couponData) {

    }

}
