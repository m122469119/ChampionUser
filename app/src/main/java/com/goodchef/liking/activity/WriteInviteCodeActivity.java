package com.goodchef.liking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:兑换邀请码
 * Author shaozucheng
 * Time:16/7/7 上午11:36
 */
public class WriteInviteCodeActivity extends AppBarActivity implements View.OnClickListener {

    //  private GridPasswordView mGridPasswordView;
    private TextView mConfirmTextView;
    private String mWriteCode;
    private TextView mErrorTextView;
    private TextView mInviteEncourageTextView;
    private String confirmTextStr;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_invite_code);
        setTitle(getString(R.string.title_write_invite_code));
        iniView();
        initData();
        writeCode();
    }

    private void iniView() {
        //  mGridPasswordView = (GridPasswordView) findViewById(R.id.invite_gridPasswordView);
        mConfirmTextView = (TextView) findViewById(R.id.confirm_btn);
        mErrorTextView = (TextView) findViewById(R.id.error_prompt);
        mEditText = (EditText) findViewById(R.id.edit_invite_edit_text);
//        mGridPasswordView.performClick();
//        mGridPasswordView.setPasswordType(PasswordType.TEXTVISIBLE);
//        mGridPasswordView.setPasswordVisibility(true);
        mInviteEncourageTextView = (TextView) findViewById(R.id.invite_encourage);

        mConfirmTextView.setOnClickListener(this);
    }

    private void initData() {
        confirmTextStr = getIntent().getStringExtra(InviteFriendsActivity.KEY_INTENT_CONFIRM_TEXT);
        if (!StringUtils.isEmpty(confirmTextStr)) {
            mInviteEncourageTextView.setText(confirmTextStr);
        }
    }

    private void writeCode() {
//        mGridPasswordView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
//            @Override
//            public void onTextChanged(String psw) {
//                if (psw.length() == 6) {
//                    mWriteCode = psw;
//                }
//            }
//
//            @Override
//            public void onInputFinish(String psw) {
//
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        if (v == mConfirmTextView) {
            mWriteCode = mEditText.getText().toString().trim();
            if (!StringUtils.isEmpty(mWriteCode)) {
                sendConfirmRequest();
            }
        }
    }

    private void sendConfirmRequest() {
        LiKingApi.exchangeInviteCode(Preference.getToken(), mWriteCode, new RequestUiLoadingCallback<BaseResult>(this, R.string.loading) {
            @Override
            public void onSuccess(BaseResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(WriteInviteCodeActivity.this, result)) {
                    PopupUtils.showToast(getString(R.string.exchange_success));
                    finish();
                } else {
                    mErrorTextView.setText(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }
}
