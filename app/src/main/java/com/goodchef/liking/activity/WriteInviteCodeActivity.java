package com.goodchef.liking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.mvp.WriteInviteCodeContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:兑换邀请码
 * Author shaozucheng
 * Time:16/7/7 上午11:36
 */
public class WriteInviteCodeActivity extends AppBarActivity implements WriteInviteCodeContract.WriteInviteCodeView {

    @BindView(R.id.invite_encourage)
    TextView mInviteEncourage;
    @BindView(R.id.edit_invite_edit_text)
    EditText mEditInviteEditText;
    @BindView(R.id.error_prompt)
    TextView mErrorPrompt;
    @BindView(R.id.confirm_btn)
    TextView mConfirmBtn;

    private String mWriteCode;
    private String confirmTextStr;
    private WriteInviteCodeContract.WriteInviteCodePresenter mWriteInviteCodePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_invite_code);
        mWriteInviteCodePresenter = new WriteInviteCodeContract.WriteInviteCodePresenter(this,this);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_write_invite_code));
        initData();
    }


    private void initData() {
        confirmTextStr = getIntent().getStringExtra(InviteFriendsActivity.KEY_INTENT_CONFIRM_TEXT);
        if (!StringUtils.isEmpty(confirmTextStr)) {
            mInviteEncourage.setText(confirmTextStr);
        }
    }


    @OnClick({R.id.confirm_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_btn:
                mWriteCode = mEditInviteEditText.getText().toString().trim();
                if (!StringUtils.isEmpty(mWriteCode)) {
                    mWriteInviteCodePresenter.sendConfirmRequest(mWriteCode);
                }
                break;
        }
    }

    @Override
    public void updateWriteInviteCodeView() {
        finish();
    }

    @Override
    public void updateErrorPromptView(String message) {
        mErrorPrompt.setText(message);
    }
}
