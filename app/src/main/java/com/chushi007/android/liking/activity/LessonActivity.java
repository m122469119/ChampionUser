package com.chushi007.android.liking.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.aaron.android.framework.base.BaseActivity;
import com.aaron.android.framework.base.adapter.TabFragmentPagerAdapter;
import com.chushi007.android.liking.R;
import com.chushi007.android.liking.fragment.GroupLessonFragment;
import com.chushi007.android.liking.fragment.PrivateLessonFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:课程
 * Author shaozucheng
 * Time:16/5/31 下午4:32
 */
public class LessonActivity extends BaseActivity {
    private static final int INDEX_GROUP_LESSON = 0;//团体课
    private static final int INDEX_PRIVATE_LESSON = 1;//私教课
    private TabLayout mTableLayout;
    private ViewPager mViewPage;
    private TabFragmentPagerAdapter mTabFragmentPagerAdapter;
    private ImageView mLeftImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        initView();
    }

    private void initView() {
        initWidget();
        initTableLayout();
        initViewPage();
    }

    private void initWidget() {
        mTableLayout = (TabLayout) findViewById(R.id.my_order_tablayout);
        mViewPage = (ViewPager) findViewById(R.id.my_order_viewpager);
        mLeftImageView = (ImageView) findViewById(R.id.lesson_left_btn);

        mLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initTableLayout() {
        mTableLayout.addTab(mTableLayout.newTab().setText(R.string.tab_title_group_lesson));
        mTableLayout.addTab(mTableLayout.newTab().setText(R.string.tab_title_private_lesson));
    }

    private void initViewPage() {
        mTabFragmentPagerAdapter = new TabFragmentPagerAdapter(this, getSupportFragmentManager(), myOrderFragmentList());
        mViewPage.setAdapter(mTabFragmentPagerAdapter);
        mViewPage.setOffscreenPageLimit(2);
        mTableLayout.setTabsFromPagerAdapter(mTabFragmentPagerAdapter);//给table设置适配器
        mTableLayout.setupWithViewPager(mViewPage);//设置table和viewPage联动
    }

    private List<TabFragmentPagerAdapter.FragmentBinder> myOrderFragmentList() {
        List<TabFragmentPagerAdapter.FragmentBinder> fragmentBinders = new ArrayList<>();
        fragmentBinders.add(buildFragmentBinder(MyOrderTab.TAB_GROUP_LESSON));
        fragmentBinders.add(buildCommentFragmentBinder(MyOrderTab.TAB_PRIVATE_LESSON));
        return fragmentBinders;
    }

    private TabFragmentPagerAdapter.FragmentBinder buildFragmentBinder(MyOrderTab tab) {
        return new TabFragmentPagerAdapter.FragmentBinder(tab.getIndex(), getString(tab.getTextRestId()),
                0, new GroupLessonFragment());
    }

    private TabFragmentPagerAdapter.FragmentBinder buildCommentFragmentBinder(MyOrderTab tab) {
        return new TabFragmentPagerAdapter.FragmentBinder(tab.getIndex(), getString(tab.getTextRestId()),
                0, new PrivateLessonFragment());
    }


    enum MyOrderTab {
        TAB_GROUP_LESSON(INDEX_GROUP_LESSON,R.string.tab_title_group_lesson),
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
