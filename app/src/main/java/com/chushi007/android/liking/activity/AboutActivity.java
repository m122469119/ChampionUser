package com.chushi007.android.liking.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.chushi007.android.liking.R;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/26 下午2:02
 */
public class AboutActivity extends AppBarActivity {

    TextView mVersionNumberTextView;//版本号
    TextView mWeChatPublicTextView;
    TextView mWeiboAccountTextView;
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
        mWeiboAccountTextView = (TextView) findViewById(R.id.weibo_account);
        mCooperatePhoneTextView = (TextView) findViewById(R.id.cooperate_phone);

    }

    private void initData() {
        mVersionNumberTextView.setText("版本: " + EnvironmentUtils.Config.getAppVersionName());
    }

}
