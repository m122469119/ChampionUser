package com.goodchef.liking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.goodchef.liking.R;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/6 下午6:37
 */
public class InviteFriendsActivity extends AppBarActivity implements View.OnClickListener {
    private TextView mInviteCodeTextView;
    private TextView mInvitePromptTextView;
    private TextView mInviteFriendsBtn;
    private TextView mEditInviteCodeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        setTitle("邀请好友");
        initView();
        initData();
    }

    private void initView() {
        mInviteCodeTextView = (TextView) findViewById(R.id.invite_code);
        mInvitePromptTextView = (TextView) findViewById(R.id.invite_prompt);
        mInviteFriendsBtn = (TextView) findViewById(R.id.invite_friend_btn);
        mEditInviteCodeBtn = (TextView) findViewById(R.id.edit_invite_code_btn);

        mInviteFriendsBtn.setOnClickListener(this);
        mEditInviteCodeBtn.setOnClickListener(this);
    }

    private void initData() {

    }


    @Override
    public void onClick(View v) {
        if (v == mInviteFriendsBtn) {

        } else if (v == mEditInviteCodeBtn) {

        }
    }
}
