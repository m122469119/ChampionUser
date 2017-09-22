package com.goodchef.liking.module.train;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.aaron.android.framework.base.ui.BaseActivity;
import com.aaron.android.framework.base.widget.viewpager.TabFragmentPagerAdapter;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.data.SportDataEntity;
import com.goodchef.liking.widgets.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017/09/19
 * desc: Sport训练数据 - 天、 周 、 月
 *
 * @author: chenlei
 * @version:1.0
 */

public class SportDataActivity extends BaseActivity {

    @BindView(R.id.sport_tabLayout)
    TabLayout mSportTabLayout;
    @BindView(R.id.sport_left_btn)
    ImageView mSportLeftBtn;
    @BindView(R.id.sport_right_btn)
    ImageView mSportRightBtn;
    @BindView(R.id.sport_viewpager)
    NoScrollViewPager mSportViewpager;
    private TabFragmentPagerAdapter mTabFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initWidget();
        initTableLayout();
        initViewPage();
    }

    private void initWidget() {
        mSportLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSportRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTabFragmentPagerAdapter != null) {
                    Fragment fragment = mTabFragmentPagerAdapter.getItem(mSportViewpager.getCurrentItem());
                    if(fragment != null) {
                        if(fragment instanceof SportDataDayFragment) {
                            ((SportDataDayFragment)fragment).sportShare();
                        } else if(fragment instanceof SportDataWeekOrMonthFragment) {
                            ((SportDataWeekOrMonthFragment)fragment).sportShare();
                        }
                    }
                }
            }
        });
    }

    private void initTableLayout() {
        mSportTabLayout.addTab(mSportTabLayout.newTab().setText(R.string.day));
        mSportTabLayout.addTab(mSportTabLayout.newTab().setText(R.string.week));
        mSportTabLayout.addTab(mSportTabLayout.newTab().setText(R.string.month));
    }

    private void initViewPage() {
        mTabFragmentPagerAdapter = new TabFragmentPagerAdapter(this, getSupportFragmentManager(), getFragmentList());
        mSportViewpager.setAdapter(mTabFragmentPagerAdapter);
        mSportViewpager.setOffscreenPageLimit(2);
        mSportTabLayout.setupWithViewPager(mSportViewpager);//设置table和viewPage联动
        mSportViewpager.setCurrentItem(0);
    }

    private List<TabFragmentPagerAdapter.FragmentBinder> getFragmentList() {
        List<TabFragmentPagerAdapter.FragmentBinder> fragmentBinders = new ArrayList<>();
        fragmentBinders.add(buildFragmentSportDay(MyTab.TAB_SPORT_DAY));
        fragmentBinders.add(buildFragmentSportWeek(MyTab.TAB_SPORT_WEEK));
        fragmentBinders.add(buildFragmentSportMonth(MyTab.TAB_SPORT_MONTH));
        return fragmentBinders;
    }

    private TabFragmentPagerAdapter.FragmentBinder buildFragmentSportDay(MyTab tab) {
        return new TabFragmentPagerAdapter.FragmentBinder(tab.getIndex(), getString(tab.getTextRestId()),
                0, SportDataDayFragment.newInstance());
    }

    private TabFragmentPagerAdapter.FragmentBinder buildFragmentSportWeek(MyTab tab) {
        return new TabFragmentPagerAdapter.FragmentBinder(tab.getIndex(), getString(tab.getTextRestId()),
                0, SportDataWeekOrMonthFragment.newInstance(SportDataEntity.TYPE_TIME_WEEK));
    }

    private TabFragmentPagerAdapter.FragmentBinder buildFragmentSportMonth(MyTab tab) {
        return new TabFragmentPagerAdapter.FragmentBinder(tab.getIndex(), getString(tab.getTextRestId()),
                0, SportDataWeekOrMonthFragment.newInstance(SportDataEntity.TYPE_TIME_MONTH));
    }

    private enum MyTab {
        TAB_SPORT_DAY(SportDataEntity.TYPE_TIME_DAY, R.string.day),
        TAB_SPORT_WEEK(SportDataEntity.TYPE_TIME_WEEK, R.string.week),
        TAB_SPORT_MONTH(SportDataEntity.TYPE_TIME_MONTH, R.string.month);

        private int mTextRestId;
        private int mIndex;

        MyTab(int mIndex, int mTextRestId) {
            this.mIndex = mIndex;
            this.mTextRestId = mTextRestId;
        }

        public int getTextRestId() {
            return mTextRestId;
        }

        public int getIndex() {
            return mIndex;
        }
    }

}
