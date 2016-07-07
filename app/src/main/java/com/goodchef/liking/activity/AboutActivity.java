package com.goodchef.liking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.PhoneUtils;
import com.goodchef.liking.R;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/26 下午2:02
 */
public class AboutActivity extends AppBarActivity implements View.OnClickListener {
    TextView mVersionNumberTextView;//版本号
    TextView mWeChatPublicTextView;
    TextView mCooperatePhoneTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle(getString(R.string.title_activity_about));
        initView();
        initData();
    }

    private void initView() {
        mVersionNumberTextView = (TextView) findViewById(R.id.version_number);
        mWeChatPublicTextView = (TextView) findViewById(R.id.WeChat_public_account);
        mCooperatePhoneTextView = (TextView) findViewById(R.id.cooperate_phone);
        mCooperatePhoneTextView.setOnClickListener(this);
    }

    private void initData() {
        mVersionNumberTextView.setText("版本: " + EnvironmentUtils.Config.getAppVersionName());
        mCooperatePhoneTextView.setText("13345456780");
    }

    @Override
    public void onClick(View v) {
        if (v == mCooperatePhoneTextView) {
            String phone = mCooperatePhoneTextView.getText().toString().trim();
            if (!StringUtils.isEmpty(phone)) {
                PhoneUtils.phoneCall(this, phone);
            }
        }
    }
}
