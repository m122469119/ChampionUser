package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.dialog.SelectCityDialog;
import com.goodchef.liking.http.result.data.City;
import com.goodchef.liking.module.joinus.becomecoach.BecomeTeacherContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:成为私教
 * Author shaozucheng
 * Time:16/5/26 下午2:28
 */
public class BecomeTeacherActivity extends AppBarActivity implements BecomeTeacherContract.BecomeTeacherView{
    @BindView(R.id.become_teacher_name_editText)
    EditText mBecomeTeacherNameEditText;
    @BindView(R.id.become_teacher_phone_editText)
    EditText mBecomeTeacherPhoneEditText;
    @BindView(R.id.become_teacher_city_TextView)
    TextView mBecomeTeacherCityTextView;
    @BindView(R.id.become_teacher_immediately_submit)
    TextView mBecomeTeacherImmediatelySubmit;



    private BecomeTeacherContract.BecomeTeacherPresenter mBecomeTeacherPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_teacher);
        mBecomeTeacherPresenter = new BecomeTeacherContract.BecomeTeacherPresenter(this,this);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_become_private_teacher));
        initView();
    }

    private void initView() {
        mBecomeTeacherNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mBecomeTeacherNameEditText.requestFocus()) {
                    mBecomeTeacherNameEditText.setBackgroundResource(R.drawable.shape_four_card_green_background);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBecomeTeacherPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mBecomeTeacherPhoneEditText.requestFocus()) {
                    mBecomeTeacherPhoneEditText.setBackgroundResource(R.drawable.shape_four_card_green_background);
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


    @OnClick({R.id.become_teacher_immediately_submit, R.id.become_teacher_city_TextView})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.become_teacher_immediately_submit://立即提交
                mBecomeTeacherPresenter.sendConfirmRequest();
                break;
            case R.id.become_teacher_city_TextView://选择城市
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
                mBecomeTeacherCityTextView.setText(regionsData.getProvinceName() + " - " + citiesData.getCityName());
                dialog.dismiss();
            }
        });
    }

    @Override
    public String getBecomeTeacherName() {
        return mBecomeTeacherNameEditText.getText().toString().trim();
    }

    @Override
    public String getBecomeTeacherPhone() {
        return mBecomeTeacherPhoneEditText.getText().toString().trim();
    }

    @Override
    public String getBecomeTeacherCity() {
        return mBecomeTeacherCityTextView.getText().toString().trim();
    }

    @Override
    public void setBecomeTeacherNameEditText() {
        mBecomeTeacherNameEditText.setBackgroundResource(R.drawable.shape_four_card_red_background);
    }

    @Override
    public void setBecomeTeacherPhoneEditText() {
        mBecomeTeacherPhoneEditText.setBackgroundResource(R.drawable.shape_four_card_red_background);
    }

    @Override
    public void updateBecomeTeacherView() {
        showToast(getString(R.string.submit_success_and_waiting_we_can_call_you));
        mBecomeTeacherNameEditText.setText("");
        mBecomeTeacherPhoneEditText.setText("");
        mBecomeTeacherCityTextView.setText("");
        finish();
    }


}
