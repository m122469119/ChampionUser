package com.chushi007.android.liking;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;

import com.aaron.android.framework.base.actionbar.AppBarActivity;

public class MainActivity extends AppBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showHomeUpIcon(0);
        setTitle(R.string.activity_liking_main);
        FragmentTabHost
    }
}
