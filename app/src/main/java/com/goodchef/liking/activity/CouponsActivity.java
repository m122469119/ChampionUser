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
import com.goodchef.liking.fragment.CouponsFragment;
import com.goodchef.liking.fragment.LikingBuyCardFragment;
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

    private EditText mEditCoupons;
    private TextView mExchangeButton;
    private LinearLayout mExchangeCouponsLayout;

    private String intentType = "";
    private String coursesId;
    private ArrayList<Food> confirmBuyList = new ArrayList<>();
    private int cardId;
    private int type;
    private String couponId;
    private int scheduleId;


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
        intentType = getIntent().getStringExtra(TYPE_MY_COUPONS);
        Bundle bundle = getIntent().getExtras();
        confirmBuyList = bundle.getParcelableArrayList(ShoppingCartActivity.INTENT_KEY_CONFIRM_BUY_LIST);
        cardId = getIntent().getIntExtra(BuyCardConfirmActivity.KEY_CARD_ID,0);
        type = getIntent().getIntExtra(LikingBuyCardFragment.KEY_BUY_TYPE,0);
        couponId = getIntent().getStringExtra(KEY_COUPON_ID);
        scheduleId = getIntent().getIntExtra(KEY_SCHEDULE_ID,0);

        if (intentType.equals(TYPE_MY_COUPONS)) {
            mExchangeCouponsLayout.setVisibility(View.VISIBLE);
            setTitle("我的优惠券");
        } else {
            mExchangeCouponsLayout.setVisibility(View.GONE);
            setTitle("选择优惠券");
        }
        setCouponsFragment();
    }

    private void setCouponsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_COURSE_ID, coursesId);
        bundle.putParcelableArrayList(ShoppingCartActivity.INTENT_KEY_CONFIRM_BUY_LIST, confirmBuyList);
        bundle.putString(TYPE_MY_COUPONS, intentType);
        bundle.putInt(BuyCardConfirmActivity.KEY_CARD_ID,cardId);
        bundle.putInt(LikingBuyCardFragment.KEY_BUY_TYPE,type);
        bundle.putInt(KEY_SCHEDULE_ID,0);
        bundle.putString(KEY_COUPON_ID,couponId);
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
                    PopupUtils.showToast("请输入优惠券兑换码");
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
                    PopupUtils.showToast("兑换成功");
                    mEditCoupons.setText("");//清空兑换码
                    setCouponsFragment();
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
                PopupUtils.showToast("您的网络异常,请查看网络");
            }
        });
    }

}
