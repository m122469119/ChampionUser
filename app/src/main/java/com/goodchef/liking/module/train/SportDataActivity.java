package com.goodchef.liking.module.train;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.aaron.android.framework.base.mvp.BaseMVPSwipeBackActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.base.widget.viewpager.TabFragmentPagerAdapter;
import com.goodchef.liking.R;
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

public class SportDataActivity extends BaseMVPSwipeBackActivity<SportContract.Presenter> implements SportContract.View {

    private static final int INDEX_SPORT_DAY = 1;//天
    private static final int INDEX_SPORT_WEEK = 2;//周
    private static final int INDEX_SPORT_MONTH = 3;//月

    @BindView(R.id.sport_tabLayout)
    TabLayout mSportTabLayout;
    @BindView(R.id.sport_left_btn)
    ImageView mSportLeftBtn;
    @BindView(R.id.sport_right_btn)
    ImageView mSportRightBtn;
    @BindView(R.id.sport_viewpager)
    ViewPager mSportViewpager;
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
                mPresenter.getSportShare(SportDataActivity.this);
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
                0, SportDataWeekOrMonthFragment.newInstance(INDEX_SPORT_WEEK));
    }

    private TabFragmentPagerAdapter.FragmentBinder buildFragmentSportMonth(MyTab tab) {
        return new TabFragmentPagerAdapter.FragmentBinder(tab.getIndex(), getString(tab.getTextRestId()),
                0, SportDataWeekOrMonthFragment.newInstance(INDEX_SPORT_MONTH));
    }

    @Override
    public void setPresenter() {
        mPresenter = new SportContract.Presenter();
    }

    @Override
    public void changeStateView(StateView.State state) {

    }

    private enum MyTab {
        TAB_SPORT_DAY(INDEX_SPORT_DAY, R.string.day),
        TAB_SPORT_WEEK(INDEX_SPORT_WEEK, R.string.week),
        TAB_SPORT_MONTH(INDEX_SPORT_MONTH, R.string.month);

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
