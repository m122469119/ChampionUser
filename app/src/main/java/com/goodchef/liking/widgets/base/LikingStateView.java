package com.goodchef.liking.widgets.base;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.LoginActivity;


/**
 * Created on 16/2/22.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingStateView extends StateView {
    public LikingStateView(Context context) {
        this(context, null);
    }

    public LikingStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LikingStateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initFailView();
        initNoLoginView();
        setState(State.LOADING);
    }

    public View buildStateCommonSubView(Context context, int resId, String noDataText, String refreshButtonText, View.OnClickListener refreshClickListener) {
        View commonSubView = View.inflate(context, R.layout.view_common_no_data, null);
        TextView noDataTextView = (TextView) commonSubView.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) commonSubView.findViewById(R.id.textview_refresh);
        ImageView noDataImageView = (ImageView) commonSubView.findViewById(R.id.imageview_no_data);
        if (resId > 0) {
            noDataImageView.setImageResource(resId);
        }
        noDataTextView.setText(noDataText);
        refreshView.setText(refreshButtonText);
        refreshView.setOnClickListener(refreshClickListener);
        return commonSubView;
    }

    private void initFailView() {
        setFailedView(buildStateCommonSubView(getContext(), R.drawable.network_anomaly,
                ResourceUtils.getString(R.string.network_anomaly_text),
                ResourceUtils.getString(R.string.refresh_btn_text), mClickListener));
    }

    private void initNoLoginView() {
        setNoLoginView(buildStateCommonSubView(getContext(), R.drawable.icon_head_default_image,
                ResourceUtils.getString(R.string.state_view_no_login_text),
                ResourceUtils.getString(R.string.login_btn_text), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                    }
                }));
    }

    public void initNoDataView(int resId, String text, String refreshText, View.OnClickListener clickListener) {
        setNodataView(buildStateCommonSubView(getContext(), resId, text, refreshText, clickListener));
    }

    public void initNoDataView(int resId, String text, String refreshText) {
        setNodataView(buildStateCommonSubView(getContext(), resId, text, refreshText, mClickListener));
    }
}
