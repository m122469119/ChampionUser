package com.goodchef.liking.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.goodchef.liking.R;
import com.goodchef.liking.widgets.RoundImageView;
import com.goodchef.liking.widgets.WhewView;

/**
 * 说明:绑定手环
 * Author : shaozucheng
 * Time: 下午3:28
 * version 1.0.0
 */

public class BingBraceletActivity extends AppBarActivity {

    private WhewView mWhewView;
    private RoundImageView mRoundImageView;

    private static final int Nou = 1;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == Nou) {
                // 每隔10s响一次
                handler.sendEmptyMessageDelayed(Nou, 5000);
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_bracelet);
        setTitle(getString(R.string.title_bing_bracelet));
        showPromptDialog();
        initView();
    }


    private void initView() {
        mWhewView = (WhewView) findViewById(R.id.blue_tooth_WhewView);
        mRoundImageView = (RoundImageView) findViewById(R.id.blue_tooth_RoundImageView);
        mRoundImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWhewView.isStarting()) {
                    //如果动画正在运行就停止，否则就继续执行
                    mWhewView.stop();
                    //结束进程
                    handler.removeMessages(Nou);
                } else {
                    // 执行动画
                    mWhewView.start();
                    handler.sendEmptyMessage(Nou);
                }
            }
        });
    }


    private void showPromptDialog() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_one_content, null, false);
        TextView titleTextView = (TextView) view.findViewById(R.id.one_dialog_title);
        TextView contentTextView = (TextView) view.findViewById(R.id.one_dialog_content);
        builder.setCustomView(view);
        titleTextView.setText("提示");
        contentTextView.setText("只有二代手环才能绑定哟！");
        builder.setPositiveButton(R.string.dialog_know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(Nou);
        }
    }
}
