package com.goodchef.liking.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.goodchef.liking.R;

/**
 * 说明:成为私教
 * Author shaozucheng
 * Time:16/5/26 下午2:28
 */
public class BecomeTeacherActivity extends AppBarActivity {

   private EditText mNameEditText;
   private EditText mPhoneEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_teacher);
        setTitle("成为教练");
        initView();
    }

    private void initView() {
        mNameEditText = (EditText) findViewById(R.id.name_editText);
        mPhoneEditText = (EditText) findViewById(R.id.phone_editText);
    }
}
