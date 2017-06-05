package com.goodchef.liking.module.about;

import android.os.Bundle;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.utils.LikingCallUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/26 下午2:02
 */
public class AboutActivity extends AppBarMVPSwipeBackActivity<AboutContract.Presenter> implements AboutContract.View {
    @BindView(R.id.version_number)
    TextView mVersionNumberTextView;
    @BindView(R.id.WeChat_public_account)
    TextView mWeChatPublicAccountTextView;
    @BindView(R.id.cooperate_phone)
    TextView mCooperatePhoneTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        mPresenter.init();
        setTitle(getString(R.string.title_activity_about));
    }

    @Override
    public void updateVersionText(String version) {
        mVersionNumberTextView.setText(version);
    }

    @Override
    public void updateCooperatePhoneText(String cooperatePhone) {
        mCooperatePhoneTextView.setText(cooperatePhone);
    }

    @Override
    public void updateWeChatPublicAccountText(String weChatPublicAccount) {
        mWeChatPublicAccountTextView.setText(weChatPublicAccount);
    }

    @OnClick(R.id.cooperate_phone)
    public void onClick() {
        String phone = mCooperatePhoneTextView.getText().toString().trim();
        if (!StringUtils.isEmpty(phone)) {
            LikingCallUtil.showCallDialog(AboutActivity.this, getString(R.string.confrim_call_business), phone);
        }
    }

    @Override
    public void setPresenter() {
        mPresenter = new AboutContract.Presenter();

    }
}
