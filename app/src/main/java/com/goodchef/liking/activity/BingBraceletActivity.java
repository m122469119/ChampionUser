package com.goodchef.liking.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.goodchef.liking.R;
import com.goodchef.liking.widgets.RoundImageView;
import com.goodchef.liking.widgets.WhewView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:绑定手环
 * Author : shaozucheng
 * Time: 下午3:28
 * version 1.0.0
 */

public class BingBraceletActivity extends AppBarActivity implements View.OnClickListener {

    @BindView(R.id.blue_tooth_WhewView)
    WhewView mBlueToothWhewView;
    @BindView(R.id.blue_tooth_RoundImageView)
    RoundImageView mBlueToothRoundImageView;
    @BindView(R.id.click_search_TextView)
    TextView mClickSearchTextView;
    @BindView(R.id.layout_blue_tooth_bracelet)
    LinearLayout mLayoutBlueToothBracelet;
    @BindView(R.id.blue_booth_RecyclerView)
    RecyclerView mBlueBoothRecyclerView;
    @BindView(R.id.open_blue_tooth_TextView)
    TextView mOpenBlueToothTextView;


    private static final int Nou = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
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
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_bing_bracelet));
        showPromptDialog();
        setRightIcon(R.drawable.icon_blue_tooth_help, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BlueToothHelpActivity.class);
            }
        });
        setViewOnClickListener();
    }


    private void setViewOnClickListener() {
        mBlueToothRoundImageView.setOnClickListener(this);
        mOpenBlueToothTextView.setOnClickListener(this);
    }

    /**
     * 展示蓝牙提示
     */
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

    @Override
    public void onClick(View v) {
        if (v == mBlueToothRoundImageView) {
            if (mBlueToothWhewView.isStarting()) {
                mClickSearchTextView.setVisibility(View.VISIBLE);
                //如果动画正在运行就停止，否则就继续执行
                mBlueToothWhewView.stop();
                //结束进程
                handler.removeMessages(Nou);
            } else {
                // 执行动画
                mBlueToothWhewView.start();
                mClickSearchTextView.setVisibility(View.GONE);
                handler.sendEmptyMessage(Nou);
            }
        } else if (v == mOpenBlueToothTextView) {
        }
    }
}
