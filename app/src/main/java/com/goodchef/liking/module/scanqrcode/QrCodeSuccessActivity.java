package com.goodchef.liking.module.scanqrcode;

import android.os.Bundle;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.goodchef.liking.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by aaa on 17/9/2.
 */

public class QrCodeSuccessActivity extends AppBarActivity {

    @BindView(R.id.ok_button)
    TextView okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_success);
        ButterKnife.bind(this);
        showHomeUpIcon(0);
        setTitle("成功开启");
    }

    @OnClick({R.id.ok_button})
    public void onClick(android.view.View view) {
        switch (view.getId()) {
            case R.id.ok_button:
                finish();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.silde_bottom_in, R.anim.silde_bottom_out);
    }
}
