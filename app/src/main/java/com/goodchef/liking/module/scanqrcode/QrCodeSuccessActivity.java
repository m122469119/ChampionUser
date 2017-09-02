package com.goodchef.liking.module.scanqrcode;

import android.os.Bundle;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.goodchef.liking.R;

/**
 * Created by aaa on 17/9/2.
 */

public class QrCodeSuccessActivity extends AppBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_success);
        showHomeUpIcon(R.drawable.app_bar_left_quit);
        setTitle("扫描成功");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.silde_bottom_in, R.anim.silde_bottom_out);
    }
}
