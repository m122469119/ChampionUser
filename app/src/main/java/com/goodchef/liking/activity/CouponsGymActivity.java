package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.CouponsCitysFragment;

public class CouponsGymActivity extends AppBarActivity {

    public static final String ACTION_SHOW_GYM = "action_show_gym";
    public static final String COUPONS_CODE = "coupons_code";

    public String mCouponsCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons_gym);
        setTitle(getString(R.string.apply_venue));
        Intent intent = getIntent();
        if (ACTION_SHOW_GYM.equals(intent.getAction())) {
            mCouponsCode = intent.getStringExtra(COUPONS_CODE);
            initView(savedInstanceState);
        }
    }

    private void initView(Bundle savedInstanceState) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_coupons_gym, CouponsCitysFragment.newInstance(mCouponsCode))
                .commit();
    }
}
