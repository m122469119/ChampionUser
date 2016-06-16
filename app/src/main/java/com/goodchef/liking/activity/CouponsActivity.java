package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.CouponsFragment;

/**
 * 说明:我的优惠券
 * Author shaozucheng
 * Time:16/6/16 下午2:17
 */
public class CouponsActivity extends AppBarActivity {
    public static final String KEY_COURSE_ID = "key_course_id";
    public static final String INTENT_KEY_COUPONS_DATA="intent_key_coupons_data";
    private EditText mEditCoupons;
    private TextView mExchangeButton;
    private LinearLayout mExchangeCouponsLayout;


    private String coursesId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);
        initView();
        initData();
    }

    private void initView() {
        mEditCoupons = (EditText) findViewById(R.id.edit_coupons_number);
        mExchangeButton = (TextView) findViewById(R.id.exchange_coupons_button);
        mExchangeCouponsLayout = (LinearLayout) findViewById(R.id.layout_exchange_coupon);
    }

    private void initData() {
        coursesId = getIntent().getStringExtra(KEY_COURSE_ID);
        setCouponsFragment();
    }

    private void setCouponsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_COURSE_ID,coursesId);
        fragmentTransaction.add(R.id.my_coupons_fragment, CouponsFragment.newInstance(bundle));
        fragmentTransaction.commit();
    }
}
