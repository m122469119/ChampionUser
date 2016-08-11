package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.aaron.android.framework.base.ui.BaseActivity;
import com.aaron.android.framework.base.adapter.TabFragmentPagerAdapter;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.MyGroupLessonFragment;
import com.goodchef.liking.fragment.MyPrivateCoursesFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:课程
 * Author shaozucheng
 * Time:16/5/31 下午4:32
 */
public class MyLessonActivity extends BaseActivity {
    private static final int INDEX_GROUP_LESSON = 0;//团体课
    private static final int INDEX_PRIVATE_LESSON = 1;//私教课
    public static final String KEY_CURRENT_ITEM = "key_current_item";
    private TabLayout mTableLayout;
    private ViewPager mViewPage;
    private TabFragmentPagerAdapter mTabFragmentPagerAdapter;
    private ImageView mLeftImageView;
    private int currentInt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        initData();
        initView();
    }

    private void initData(){
        currentInt = getIntent().getIntExtra(KEY_CURRENT_ITEM,0);
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
        mViewPage.setCurrentItem(currentInt);
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
