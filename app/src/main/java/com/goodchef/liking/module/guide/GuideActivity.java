package com.goodchef.liking.module.guide;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.MediaController;

import com.aaron.android.framework.base.ui.BaseActivity;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.module.home.LikingHomeActivity;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.utils.NavigationBarUtil;
import com.goodchef.liking.utils.NumberConstantUtil;
import com.goodchef.liking.widgets.CustomVideoView;
import com.goodchef.liking.widgets.autoviewpager.indicator.IconPageIndicator;
import com.goodchef.liking.widgets.autoviewpager.indicator.IconPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:引导页
 * Author shaozucheng
 * Time:16/7/20 下午5:27
 */
public class GuideActivity extends BaseActivity {

    @BindView(R.id.video_view)
    CustomVideoView mVideoView;
    @BindView(R.id.guide_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.guide_indicator)
    IconPageIndicator mIconPageIndicator;

    private FragmentPageAdapter mAdapter;
    private MediaController mediaco;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        LikingPreference.setAppVersion(EnvironmentUtils.Config.getAppVersionName());
        setVideoView();
        setIconPageIndicatorView();
        initData();
    }

    /**
     * 设置播放视频
     */
    private void setVideoView() {
        mediaco = new MediaController(this);
        mUri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.guide_video);
        mVideoView.setVideoURI(mUri);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
                mediaco.hide();
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.setVideoURI(mUri);
                mVideoView.start();
                mediaco.hide();
            }
        });
    }

    private void setIconPageIndicatorView() {
        WindowManager wmManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        boolean hasSoft = NavigationBarUtil.hasSoftKeys(wmManager);//判断是否有虚拟键盘
        if (hasSoft) {
            int navigationBarHeight = NavigationBarUtil.getNavigationBarHeight(this);//获取虚拟键盘的高度
            //这一行很重要，将dialog对话框设置在虚拟键盘上面
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mIconPageIndicator.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, (navigationBarHeight + 30));
        } else {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mIconPageIndicator.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 30);
        }
    }



    private void initData() {
        //这里因为是3.0一下版本，所以需继承FragmentActivity，通过getSupportFragmentManager()获取FragmentManager
        //3.0及其以上版本，只需继承Activity，通过getFragmentManager获取事物
        FragmentManager fm = getSupportFragmentManager();
        //初始化自定义适配器
        mAdapter = new FragmentPageAdapter(fm);
        //绑定自定义适配器
        mViewPager.setAdapter(mAdapter);
        mIconPageIndicator.setViewPager(mViewPager);
        mIconPageIndicator.notifyDataSetChanged();
    }

    private class FragmentPageAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        private FragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getIconResId(int index) {
            return R.drawable.banner_indicator;
        }

        @Override
        public int getCount() {
            return NumberConstantUtil.THREE;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case NumberConstantUtil.ZERO:
                    return GuideFragment.newInstance(position);
                case NumberConstantUtil.ONE:
                    return GuideFragment.newInstance(position);
                case NumberConstantUtil.TWO:
                    return GuideFragment.newInstance(position);
                default:
                    return null;
            }
        }
    }

    @Override
    public void onBackPressed() {
        //在引导页按back键进入主页
        startMainActivity();
    }

    private void startMainActivity() {
        startActivity(new Intent(GuideActivity.this, LikingHomeActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.stopPlayback();
    }
}
