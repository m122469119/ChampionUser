package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.OpenPassWordDoorFragment;
import com.goodchef.liking.widgets.autoviewpager.indicator.IconPageIndicator;
import com.goodchef.liking.widgets.autoviewpager.indicator.IconPagerAdapter;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/6 下午3:26
 */
public class OpenTheDoorActivity extends AppBarActivity {

    private ViewPager mViewPager;
    private MyFragmentPageAdapter mAdapter;
    private IconPageIndicator mIconPageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_the_door);
        setTitle(getString(R.string.title_open_the_door));
        initView();
        initData();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.open_viewpager);
        mIconPageIndicator = (IconPageIndicator)findViewById(R.id.open_indicator);
    }

    private void initData() {
        //这里因为是3.0一下版本，所以需继承FragmentActivity，通过getSupportFragmentManager()获取FragmentManager
        //3.0及其以上版本，只需继承Activity，通过getFragmentManager获取事物
        FragmentManager fm = getSupportFragmentManager();
        //初始化自定义适配器
        mAdapter = new MyFragmentPageAdapter(fm);
        //绑定自定义适配器
        mViewPager.setAdapter(mAdapter);
        mIconPageIndicator.setViewPager(mViewPager);
        mIconPageIndicator.notifyDataSetChanged();
    }


    public class MyFragmentPageAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        public MyFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getIconResId(int index) {
            return R.drawable.open_the_door_indicator;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return OpenPassWordDoorFragment.newInstance(position);
                case 1:
                    return OpenPassWordDoorFragment.newInstance(position);
                default:
                    return null;
            }
        }
    }
}
