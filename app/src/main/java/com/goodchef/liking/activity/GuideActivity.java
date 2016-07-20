package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.GuideFragment;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.widgets.autoviewpager.indicator.IconPageIndicator;
import com.goodchef.liking.widgets.autoviewpager.indicator.IconPagerAdapter;

/**
 * 说明:引导页
 * Author shaozucheng
 * Time:16/7/20 下午5:27
 */
public class GuideActivity extends AppBarActivity {

    private ViewPager mViewPager;
    private FragmentPageAdapter mAdapter;
    private IconPageIndicator mIconPageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        hideAppBar();
        Preference.setAppVersion(EnvironmentUtils.Config.getAppVersionName());
        initView();
        initData();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.guide_viewpager);
        mIconPageIndicator = (IconPageIndicator) findViewById(R.id.guide_indicator);
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

    public class FragmentPageAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        public FragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getIconResId(int index) {
            return R.drawable.banner_indicator;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return GuideFragment.newInstance(position);
                case 1:
                    return GuideFragment.newInstance(position);
                case 2:
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

}
