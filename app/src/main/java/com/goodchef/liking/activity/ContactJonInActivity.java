package com.goodchef.liking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.codelibrary.utils.ValidateUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.mvp.presenter.ContactJoinPresenter;
import com.goodchef.liking.mvp.view.ContactJoinView;

/**
 * 说明:联系加盟
 * Author shaozucheng
 * Time:16/5/27 下午2:14
 */
public class ContactJonInActivity extends AppBarActivity implements View.OnClickListener, ContactJoinView {
    private EditText mNameEditText;
    private EditText mPhoneEditText;
    private EditText mCityEditText;
    private TextView mImmediatelySubmitBtn;

    private ContactJoinPresenter mContactJoinPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_join);
        setTitle(getString(R.string.title_activity_contact_join));
        initView();
    }

    private void initView() {
        mNameEditText = (EditText) findViewById(R.id.name_editText);
        mPhoneEditText = (EditText) findViewById(R.id.phone_editText);
        mCityEditText = (EditText) findViewById(R.id.city_editText);
        mImmediatelySubmitBtn = (TextView) findViewById(R.id.immediately_submit);

        mImmediatelySubmitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mImmediatelySubmitBtn) {
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
        mContactJoinPresenter.joinAllpy(name, phone, city, 0);
    }

    @Override
    public void updateContactJoinView() {
        PopupUtils.showToast("提交成功,请等待商务人员与您联系");
        mNameEditText.setText("");
        mPhoneEditText.setText("");
        mCityEditText.setText("");
        finish();
    }
}
