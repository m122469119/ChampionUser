package com.goodchef.liking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.utils.LikingCallUtil;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/26 下午2:02
 */
public class AboutActivity extends AppBarActivity implements View.OnClickListener {
    private TextView mVersionNumberTextView;//版本号
    private TextView mWeChatPublicTextView;
    private TextView mCooperatePhoneTextView;


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
        String phone = Preference.getBusinessServicePhone();
        if (!StringUtils.isEmpty(phone)) {
            mCooperatePhoneTextView.setText(phone);
        }
        BaseConfigResult baseConfigResult = Preference.getBaseConfig();
        if (baseConfigResult != null) {
            BaseConfigResult.BaseConfigData baseConfigData = baseConfigResult.getBaseConfigData();
            if (baseConfigData != null) {
                mWeChatPublicTextView.setText(baseConfigData.getWechat().trim());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mCooperatePhoneTextView) {
            String phone = mCooperatePhoneTextView.getText().toString().trim();
            if (!StringUtils.isEmpty(phone)) {
                LikingCallUtil.showCallDialog(AboutActivity.this, "确定联系商务人员吗？", phone);
            }
        }
    }


}
