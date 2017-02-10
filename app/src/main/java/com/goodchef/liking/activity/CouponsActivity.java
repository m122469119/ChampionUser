package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.InputMethodManagerUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.CouponErrorMessage;
import com.goodchef.liking.eventmessages.ExchangeCouponsMessage;
import com.goodchef.liking.fragment.CouponsFragment;
import com.goodchef.liking.fragment.LikingBuyCardFragment;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.data.Food;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.storage.Preference;

import java.util.ArrayList;

/**
 * 说明:我的优惠券
 * Author shaozucheng
 * Time:16/6/16 下午2:17
 */
public class CouponsActivity extends AppBarActivity {
    public static final String KEY_COURSE_ID = "key_course_id";
    public static final String TYPE_MY_COUPONS = "MyCoupons";
    public static final String INTENT_KEY_COUPONS_DATA = "intent_key_coupons_data";
    public static final String KEY_COUPON_ID = "key_coupon_id";
    public static final String KEY_SCHEDULE_ID = "schedule_id";
    public static final String KEY_SELECT_TIMES = "select_times";

    private EditText mEditCoupons;//填写优惠券
    private TextView mExchangeButton;//兑换优惠券按钮
    private LinearLayout mExchangeCouponsLayout;//优惠券布局

    private String intentType = "";
    private String coursesId;//课程id
    private String selectTimes;//私教课购买次数
    private ArrayList<Food> confirmBuyList = new ArrayList<>();
    private String cardId;//会员卡id
    private String type;//类型
    private String couponId;//优惠券id
    private String scheduleId;//排期id
    private String gymId;//场馆id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);
        initView();
        initData();
        doExchangeCoupons();
    }

    private void initView() {
        mEditCoupons = (EditText) findViewById(R.id.edit_coupons_number);
        mExchangeButton = (TextView) findViewById(R.id.exchange_coupons_button);
        mExchangeCouponsLayout = (LinearLayout) findViewById(R.id.layout_exchange_coupon);
    }

    private void initData() {
        coursesId = getIntent().getStringExtra(KEY_COURSE_ID);
        selectTimes = getIntent().getStringExtra(KEY_SELECT_TIMES);
        intentType = getIntent().getStringExtra(TYPE_MY_COUPONS);
        Bundle bundle = getIntent().getExtras();
        confirmBuyList = bundle.getParcelableArrayList(ShoppingCartActivity.INTENT_KEY_CONFIRM_BUY_LIST);
        cardId = getIntent().getStringExtra(BuyCardConfirmActivity.KEY_CARD_ID);
        type = getIntent().getStringExtra(LikingBuyCardFragment.KEY_BUY_TYPE);
        couponId = getIntent().getStringExtra(KEY_COUPON_ID);
        scheduleId = getIntent().getStringExtra(KEY_SCHEDULE_ID);
        gymId = getIntent().getStringExtra(LikingLessonFragment.KEY_GYM_ID);

        if (intentType.equals(TYPE_MY_COUPONS)) {
            mExchangeCouponsLayout.setVisibility(View.VISIBLE);
            setTitle(getString(R.string.title_activity_my_coupons));
        } else {
            mExchangeCouponsLayout.setVisibility(View.GONE);
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
        bundle.putParcelableArrayList(ShoppingCartActivity.INTENT_KEY_CONFIRM_BUY_LIST, confirmBuyList);
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
                    PopupUtils.showToast(getString(R.string.input_coupon_code));
                } else {
                    sendExchangeCouponsRequest(couponsNumber);
                }
            }
        });
    }

    private void sendExchangeCouponsRequest(String code) {
        LiKingApi.exchangeCoupon(Preference.getToken(), code, new RequestUiLoadingCallback<BaseResult>(this, R.string.loading_data) {
            @Override
            public void onSuccess(BaseResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(CouponsActivity.this, result)) {
                    PopupUtils.showToast(getString(R.string.exchange_success));
                    mEditCoupons.setText("");//清空兑换码
                    postEvent(new ExchangeCouponsMessage());
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
                PopupUtils.showToast(getString(R.string.network_error));
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
}
