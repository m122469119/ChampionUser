package com.chushi007.android.liking.activity;

import android.os.Bundle;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.chushi007.android.liking.R;

public class MainActivity extends AppBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showHomeUpIcon(0);
        setTitle(R.string.activity_liking_main);
    }
}
