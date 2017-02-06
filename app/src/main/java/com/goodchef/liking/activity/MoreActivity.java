package com.goodchef.liking.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.LoginOutMessage;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.http.result.VerificationCodeResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.LoginPresenter;
import com.goodchef.liking.mvp.view.LoginView;
import com.goodchef.liking.storage.Preference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:更多
 * Author : shaozucheng
 * Time: 下午3:17
 * version 1.0.0
 */

public class MoreActivity extends AppBarActivity implements View.OnClickListener, LoginView {

    @BindView(R.id.login_out_btn)
    TextView mLoginOutBtn;

    private LinearLayout mAboutUsLayout;//关于我们
    private LinearLayout mCheckUpdateLayout;//检测更新

    public static final String NULL_STRING = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_activity_more));
        initView();
        initViewIconAndText();
        setViewOnClickListener();
    }

    private void initView() {
        mAboutUsLayout = (LinearLayout) findViewById(R.id.layout_about_us);
        mCheckUpdateLayout = (LinearLayout) findViewById(R.id.layout_check_update);
    }

    private void initViewIconAndText() {
        setMySettingCard(mAboutUsLayout, R.string.layout_about_us, true);
        setMySettingCard(mCheckUpdateLayout, R.string.layout_check_update, false);
        if (Preference.isLogin()) {
            mLoginOutBtn.setVisibility(View.VISIBLE);
        } else {
            mLoginOutBtn.setVisibility(View.GONE);
        }
    }


    private void setMySettingCard(View view, int text, boolean isShowLine) {
        TextView textView = (TextView) view.findViewById(R.id.standard_my_text);
        View line = view.findViewById(R.id.standard_view_line);
        textView.setText(text);
        if (isShowLine) {
            line.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
        }
    }

    private void setViewOnClickListener() {
        mAboutUsLayout.setOnClickListener(this);
        mCheckUpdateLayout.setOnClickListener(this);
        mLoginOutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mAboutUsLayout) {//关于我们
            startActivity(AboutActivity.class);
        } else if (v == mCheckUpdateLayout) {//检测更新
            showCheckUpdateDialog();
        } else if (v == mLoginOutBtn) {
            if (Preference.isLogin()) {
                showExitDialog();
            } else {
                PopupUtils.showToast(getString(R.string.not_login));
            }
        }
    }


    private void showCheckUpdateDialog() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage("去升级");
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 退出登录dialog
     */
    private void showExitDialog() {
        final HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(getString(R.string.login_exit_message));
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exitLoginRequest();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 退出登录发送请求
     */
    private void exitLoginRequest() {
        LoginPresenter loginPresenter = new LoginPresenter(this, this);
        loginPresenter.userLoginOut();
    }

    @Override
    public void updateVerificationCodeView(VerificationCodeResult.VerificationCodeData verificationCodeData) {

    }

    @Override
    public void updateLoginView(UserLoginResult.UserLoginData userLoginData) {

    }

    @Override
    public void updateLoginOut() {
        Preference.setToken(NULL_STRING);
        Preference.setNickName(NULL_STRING);
        Preference.setUserPhone(NULL_STRING);
        Preference.setIsNewUser(null);
        Preference.setUserIconUrl(NULL_STRING);
        Preference.setIsBind("0");
        PopupUtils.showToast(getString(R.string.exit_login_success));
        postEvent(new LoginOutMessage());
        mLoginOutBtn.setVisibility(View.GONE);
        finish();
    }
}
