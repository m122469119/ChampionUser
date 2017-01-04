package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.codelibrary.utils.RegularUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.dialog.SelectCityDialog;
import com.goodchef.liking.http.result.data.City;
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
    private TextView mCityTextView;
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
        mCityTextView = (TextView) findViewById(R.id.city_editText);
        mImmediatelySubmitBtn = (TextView) findViewById(R.id.immediately_submit);

        mImmediatelySubmitBtn.setOnClickListener(this);
        mCityTextView.setOnClickListener(this);

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mNameEditText.requestFocus()) {
                    mNameEditText.setBackgroundResource(R.drawable.shape_four_card_green_background);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mPhoneEditText.requestFocus()) {
                    mPhoneEditText.setBackgroundResource(R.drawable.shape_four_card_green_background);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == mImmediatelySubmitBtn) {
            sendConfirmRequest();
        } else if (v == mCityTextView) {
            showSelectCityDialog();
        }
    }

    private void showSelectCityDialog() {
        SelectCityDialog dialog = new SelectCityDialog(this);
        dialog.setNegativeClickListener(new SelectCityDialog.cancelClickListener() {
            @Override
            public void onCancelClickListener(AppCompatDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveClickListener(new SelectCityDialog.confirmClickListener() {
            @Override
            public void OnConfirmClickListener(AppCompatDialog dialog, City.RegionsData regionsData, City.RegionsData.CitiesData citiesData) {
                mCityTextView.setText(regionsData.getProvinceName() + " - " + citiesData.getCityName());
                dialog.dismiss();
            }
        });
    }

    private void sendConfirmRequest() {
        String name = mNameEditText.getText().toString().trim();
        String phone = mPhoneEditText.getText().toString().trim();
        String city = mCityTextView.getText().toString();

        if (StringUtils.isEmpty(name)) {
            PopupUtils.showToast("姓名不能为空");
            mNameEditText.setBackgroundResource(R.drawable.shape_four_card_red_background);
            return;
        } else if (name.length() > 15) {
            PopupUtils.showToast("姓名不能超过15个字");
            mNameEditText.setBackgroundResource(R.drawable.shape_four_card_red_background);
            return;
        }
        if (StringUtils.isEmpty(phone)) {
            PopupUtils.showToast("手机号不能为空");
            mPhoneEditText.setBackgroundResource(R.drawable.shape_four_card_red_background);
            return;
        }
        if (!RegularUtils.isMobileExact(phone)) {
            PopupUtils.showToast("手机号码输入不正确");
            mPhoneEditText.setBackgroundResource(R.drawable.shape_four_card_red_background);
            return;
        }
        if (StringUtils.isEmpty(city)) {
            PopupUtils.showToast("请选择城市");
            return;
        }

        if (mContactJoinPresenter == null) {
            mContactJoinPresenter = new ContactJoinPresenter(this, this);
        }
        mContactJoinPresenter.joinAllpy(name, phone, city, 0);
    }

    @Override
    public void updateContactJoinView() {
        PopupUtils.showToast("提交成功,请等待商务人员与您联系");
        mNameEditText.setText("");
        mPhoneEditText.setText("");
        mCityTextView.setText("");
        finish();
    }
}
