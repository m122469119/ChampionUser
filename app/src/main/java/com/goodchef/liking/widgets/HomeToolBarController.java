package com.goodchef.liking.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.framework.utils.ToolBarUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.CoursesResult;
import com.goodchef.liking.module.gym.list.ChangeGymActivity;
import com.goodchef.liking.module.home.LikingHomeActivity;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;
import com.goodchef.liking.umeng.UmengEventId;
import com.goodchef.liking.utils.ChangeGymUtil;
import com.goodchef.liking.utils.UMengCountUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aaa on 17/9/20.
 */

public class HomeToolBarController {


    @BindView(R.id.liking_left_title_text)
    TextView likingLeftTitleText;
    @BindView(R.id.liking_middle_title_text)
    TextView likingMiddleTitleText;
    @BindView(R.id.liking_distance_text)
    TextView likingDistanceText;
    @BindView(R.id.layout_home_middle)
    RelativeLayout layoutHomeMiddle;
    @BindView(R.id.liking_right_imageView)
    ImageView likingRightImageView;
    @BindView(R.id.liking_right_right_imageView)
    ImageView likingRightRightImageView;
    @BindView(R.id.home_notice_prompt)
    TextView homeNoticePrompt;//红色点点
    @BindView(R.id.tv_shopping_cart_num)
    TextView tvShoppingCartNum;
    @BindView(R.id.liking_right_title_text)
    TextView likingRightTitleText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.liking_home_appBar)
    AppBarLayout likingHomeAppBar;

    public Context context;
    private View mRootView;

    public HomeToolBarController(Context context) {
        this.context = context;
    }

    public View createToolbarLayout() {
        mRootView = LayoutInflater.from(context).inflate(R.layout.layout_liking_lesson_title_bar, null, false);
        ButterKnife.bind(this, mRootView);
        ToolBarUtils.setToolbarHeight(context,toolbar);
        initDefaultTitle();
        return mRootView;
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

    public void setHomeNoticePrompt(boolean isShow) {
        if (homeNoticePrompt != null) {
            if (isShow) {
                homeNoticePrompt.setVisibility(View.VISIBLE);
            } else {
                homeNoticePrompt.setVisibility(View.GONE);
            }
        }
    }

    public TextView getLikingLeftTitleText() {
        return likingLeftTitleText;
    }

    public RelativeLayout getLayoutHomeMiddle() {
        return layoutHomeMiddle;
    }

}
