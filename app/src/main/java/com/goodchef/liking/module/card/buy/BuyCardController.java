package com.goodchef.liking.module.card.buy;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ToolBarUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.module.home.LikingHomeActivity;

/**
 * Created by aaa on 17/9/21.
 */

public class BuyCardController {
    private Context context;
    private View view;

    private TextView likingLeftTitleText;
    private TextView likingMiddleTitleText;
    private TextView likingDistanceText;
    private ImageView likingRightImageView;
    private ImageView likingRightRightImageView;
    private RelativeLayout layoutHomeMiddle;
    private Toolbar toolbar;

    public BuyCardController(Context context, View view) {
        this.context = context;
        this.view = view;
        initView();
    }

    private void initView() {
        likingLeftTitleText = (TextView) view.findViewById(R.id.liking_left_title_text);
        likingMiddleTitleText = (TextView) view.findViewById(R.id.liking_middle_title_text);
        likingDistanceText = (TextView) view.findViewById(R.id.liking_distance_text);
        likingRightImageView = (ImageView) view.findViewById(R.id.liking_right_imageView);
        likingRightRightImageView = (ImageView) view.findViewById(R.id.liking_right_right_imageView);
        layoutHomeMiddle = (RelativeLayout) view.findViewById(R.id.layout_home_middle);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        likingRightImageView.setVisibility(View.GONE);
        likingRightRightImageView.setVisibility(View.GONE);
        ToolBarUtils.setToolbarHeight(context, toolbar);
        initDefaultTitle();
    }

    private void initDefaultTitle() {
        if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
            setNotNetWorkMiddleView();
        }
        setLikingLeftTitleText(context.getString(R.string.title_change_gym));
    }

    /**
     * 没有网络时设置默认
     */
    private void setNotNetWorkMiddleView() {
        LikingHomeActivity.isWhetherLocation = false;
        setLikingMiddleTitleText(context.getString(R.string.title_network_contact_fail));
        setLikingDistanceText("");
    }

    public void setLikingLeftTitleText(String text) {
        if (likingLeftTitleText != null) {
            likingLeftTitleText.setText(text);
        }
    }

    public void setLikingMiddleTitleText(String text) {
        if (likingMiddleTitleText != null) {
            likingMiddleTitleText.setText(text);
        }
    }

    public void setLikingDistanceText(String text) {
        if (likingDistanceText == null) {
            return;
        }
        if (!StringUtils.isEmpty(text)) {
            likingDistanceText.setVisibility(View.VISIBLE);
            likingDistanceText.setText(text);
        } else {
            likingDistanceText.setVisibility(View.GONE);
        }
    }

    public void setLikingRightImageView(int resId) {
        if (likingRightImageView != null) {
            likingRightImageView.setVisibility(View.VISIBLE);
            likingRightImageView.setImageResource(resId);
        }
    }

    public void setLikingRightImageView(int resId, View.OnClickListener listener) {
        if (likingRightImageView != null) {
            likingRightImageView.setVisibility(View.VISIBLE);
            likingRightImageView.setImageResource(resId);
            likingRightImageView.setOnClickListener(listener);
        }
    }

    public TextView getLikingLeftTitleText() {
        return likingLeftTitleText;
    }

    public RelativeLayout getLayoutHomeMiddle() {
        return layoutHomeMiddle;
    }


}
