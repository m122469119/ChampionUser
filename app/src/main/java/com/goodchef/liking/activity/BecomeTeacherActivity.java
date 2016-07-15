package com.goodchef.liking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.codelibrary.utils.ValidateUtils;
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.mvp.presenter.ContactJoinPresenter;
import com.goodchef.liking.mvp.view.ContactJoinView;

/**
 * 说明:成为私教
 * Author shaozucheng
 * Time:16/5/26 下午2:28
 */
public class BecomeTeacherActivity extends AppBarActivity implements View.OnClickListener, ContactJoinView {
    private EditText mNameEditText;
    private EditText mPhoneEditText;
    private EditText mCityEditText;
    private TextView mImmediatelyBtn;

    private ContactJoinPresenter mContactJoinPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_teacher);
        setTitle(getString(R.string.title_become_private_teacher));
        initView();
    }

    private void initView() {
        mNameEditText = (EditText) findViewById(R.id.become_teacher_name_editText);
        mPhoneEditText = (EditText) findViewById(R.id.become_teacher_phone_editText);
        mCityEditText = (EditText) findViewById(R.id.become_teacher_city_editText);
        mImmediatelyBtn = (TextView) findViewById(R.id.become_teacher_immediately_submit);

        mImmediatelyBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mImmediatelyBtn) {
            sendConfirmRequest();
        }
    }

    private void sendConfirmRequest() {
        String name = mNameEditText.getText().toString().trim();
        String phone = mPhoneEditText.getText().toString().trim();
        String city = mCityEditText.getText().toString().trim();

        if (StringUtils.isEmpty(name)) {
            PopupUtils.showToast("姓名不能为空");
            return;
        } else if (name.length() > 15) {
            PopupUtils.showToast("姓名不能超过15个字");
            return;
        }
        if (StringUtils.isEmpty(phone)) {
            PopupUtils.showToast("手机号不能为空");
            return;
        }
        if (!ValidateUtils.isMobile(phone)) {
            PopupUtils.showToast("手机号码输入不正确");
            return;
        }
        if (StringUtils.isEmpty(city)) {
            PopupUtils.showToast("城市不能为空");
            return;
        } else if (city.length() > 20) {
            PopupUtils.showToast("城市不能超过20个字");
            return;
        }
        mContactJoinPresenter = new ContactJoinPresenter(this, this);
        mContactJoinPresenter.joinAllpy(name, phone, city, 1);
    }

    @Override
    public void updateContactJoinView() {
        PopupUtils.showToast("提交成功,请等待工作人员与您联系");
        mNameEditText.setText("");
        mPhoneEditText.setText("");
        mCityEditText.setText("");
        finish();
    }
}
