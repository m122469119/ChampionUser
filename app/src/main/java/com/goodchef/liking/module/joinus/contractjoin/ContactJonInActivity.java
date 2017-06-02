package com.goodchef.liking.module.joinus.contractjoin;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.data.City;
import com.goodchef.liking.dialog.SelectCityDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:联系加盟
 * Author shaozucheng
 * Time:16/5/27 下午2:14
 */
public class ContactJonInActivity extends AppBarMVPSwipeBackActivity<ContactJoinContract.Presenter> implements ContactJoinContract.View {
    @BindView(R.id.name_editText)
    EditText mNameEditText;
    @BindView(R.id.phone_editText)
    EditText mPhoneEditText;
    @BindView(R.id.city_TextView)
    TextView mCityTextView;
    @BindView(R.id.immediately_submit)
    TextView mImmediatelySubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_join);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_activity_contact_join));
        initView();
    }

    private void initView() {
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

    @OnClick({R.id.immediately_submit, R.id.city_TextView})
    public void onClick(android.view.View v) {
        switch (v.getId()) {
            case R.id.immediately_submit:
                mPresenter.sendConfirmRequest(this);
                break;
            case R.id.city_TextView:
                showSelectCityDialog();
                break;
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


    @Override
    public String getUserName() {
        return mNameEditText.getText().toString().trim();
    }

    @Override
    public String getUserPhone() {
        return mPhoneEditText.getText().toString().trim();
    }

    @Override
    public String getUserSelectCity() {
        return mCityTextView.getText().toString();
    }

    @Override
    public void setNameEditText() {
        mNameEditText.setBackgroundResource(R.drawable.shape_four_card_red_background);
    }

    @Override
    public void setPhoneEditText() {
        mPhoneEditText.setBackgroundResource(R.drawable.shape_four_card_red_background);
    }

    @Override
    public void updateContactJoinContractView() {
        showToast(getString(R.string.submit_success_wait_contact));
        mNameEditText.setText("");
        mPhoneEditText.setText("");
        mCityTextView.setText("");
        finish();
    }

    @Override
    public void setPresenter() {
        mPresenter = new ContactJoinContract.Presenter();
    }
}
