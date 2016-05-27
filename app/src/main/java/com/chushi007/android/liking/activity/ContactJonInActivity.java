package com.chushi007.android.liking.activity;

import android.os.Bundle;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.chushi007.android.liking.R;

/**
 * 说明:联系加盟
 * Author shaozucheng
 * Time:16/5/27 下午2:14
 */
public class ContactJonInActivity extends AppBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_join);
        setTitle(getString(R.string.title_activity_contact_join));
        initView();
    }

    private void initView() {
    }
}
