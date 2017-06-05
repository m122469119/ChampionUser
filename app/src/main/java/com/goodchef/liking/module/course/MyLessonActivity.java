package com.goodchef.liking.module.course;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.aaron.android.framework.base.widget.viewpager.TabFragmentPagerAdapter;
import com.aaron.android.framework.base.ui.BaseSwipeBackActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.module.course.personal.MyPrivateCoursesFragment;
import com.goodchef.liking.module.course.group.MyGroupLessonFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:我的 - 课程列表
 * Author shaozucheng
 * Time:16/5/31 下午4:32
 */
public class MyLessonActivity extends BaseSwipeBackActivity {
    private static final int INDEX_GROUP_LESSON = 0;//团体课
    private static final int INDEX_PRIVATE_LESSON = 1;//私教课
    public static final String KEY_CURRENT_ITEM = "key_current_item";
    @BindView(R.id.my_order_tablayout)
    TabLayout mMyOrderTablayout;
    @BindView(R.id.lesson_left_btn)
    ImageView mLessonLeftBtn;
    @BindView(R.id.my_order_viewpager)
    ViewPager mMyOrderViewpager;
    private TabFragmentPagerAdapter mTabFragmentPagerAdapter;
    private int currentInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        currentInt = getIntent().getIntExtra(KEY_CURRENT_ITEM, 0);
    }

    private void initView() {
        initWidget();
        initTableLayout();
        initViewPage();
    }

    private void initWidget() {
        mLessonLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initTableLayout() {
        mMyOrderTablayout.addTab(mMyOrderTablayout.newTab().setText(R.string.tab_title_group_lesson));
        mMyOrderTablayout.addTab(mMyOrderTablayout.newTab().setText(R.string.tab_title_private_lesson));
    }

    private void initViewPage() {
        mTabFragmentPagerAdapter = new TabFragmentPagerAdapter(this, getSupportFragmentManager(), myOrderFragmentList());
        mMyOrderViewpager.setAdapter(mTabFragmentPagerAdapter);
        mMyOrderViewpager.setOffscreenPageLimit(2);
        mMyOrderTablayout.setTabsFromPagerAdapter(mTabFragmentPagerAdapter);//给table设置适配器
        mMyOrderTablayout.setupWithViewPager(mMyOrderViewpager);//设置table和viewPage联动
        mMyOrderViewpager.setCurrentItem(currentInt);
    }

    private List<TabFragmentPagerAdapter.FragmentBinder> myOrderFragmentList() {
        List<TabFragmentPagerAdapter.FragmentBinder> fragmentBinders = new ArrayList<>();
        fragmentBinders.add(buildFragmentBinder(MyOrderTab.TAB_GROUP_LESSON));
        fragmentBinders.add(buildCommentFragmentBinder(MyOrderTab.TAB_PRIVATE_LESSON));
        return fragmentBinders;
    }

    private TabFragmentPagerAdapter.FragmentBinder buildFragmentBinder(MyOrderTab tab) {
        return new TabFragmentPagerAdapter.FragmentBinder(tab.getIndex(), getString(tab.getTextRestId()),
                0, new MyGroupLessonFragment());
    }

    private TabFragmentPagerAdapter.FragmentBinder buildCommentFragmentBinder(MyOrderTab tab) {
        return new TabFragmentPagerAdapter.FragmentBinder(tab.getIndex(), getString(tab.getTextRestId()),
                0, new MyPrivateCoursesFragment());
    }


    private enum MyOrderTab {
        TAB_GROUP_LESSON(INDEX_GROUP_LESSON, R.string.tab_title_group_lesson),
        TAB_PRIVATE_LESSON(INDEX_PRIVATE_LESSON, R.string.tab_title_private_lesson);

        private int mTextRestId;
        private int mIndex;

        MyOrderTab(int mIndex, int mTextRestId) {
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
