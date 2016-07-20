package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.base.adapter.TabFragmentPagerAdapter;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.MyCardOrderFragment;
import com.goodchef.liking.fragment.MyDishesOrderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/28 下午2:41
 */
public class MyOrderActivity extends AppBarActivity {
    private static final int INDEX_DISHES = 0;//营养餐
    private static final int INDEX_MY_CARD = 1;//会员卡
    public static String KEY_CURRENT_INDEX = "key_current_index";
    private TabLayout mTableLayout;
    private ViewPager mViewPage;
    private TabFragmentPagerAdapter mTabFragmentPagerAdapter;
    private ImageView mLeftImageView;
    private int mCurrentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        setTitle("会员卡");
        //   initWidget();
        //  initData();
        setCouponsFragment();
    }


    private void setCouponsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.my_card_fragment, MyCardOrderFragment.newInstance());
        fragmentTransaction.commit();
    }

    private void initData() {
        mCurrentItem = getIntent().getIntExtra(KEY_CURRENT_INDEX, 0);
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
        mTableLayout.addTab(mTableLayout.newTab().setText(R.string.tab_title_dishes));
        mTableLayout.addTab(mTableLayout.newTab().setText(R.string.tab_title_my_card));
    }

    private void initViewPage() {
        mTabFragmentPagerAdapter = new TabFragmentPagerAdapter(this, getSupportFragmentManager(), myOrderFragmentList());
        mViewPage.setAdapter(mTabFragmentPagerAdapter);
        mViewPage.setOffscreenPageLimit(2);
        mTableLayout.setTabsFromPagerAdapter(mTabFragmentPagerAdapter);//给table设置适配器
        mTableLayout.setupWithViewPager(mViewPage);//设置table和viewPage联动
        mViewPage.setCurrentItem(mCurrentItem);
    }

    private List<TabFragmentPagerAdapter.FragmentBinder> myOrderFragmentList() {
        List<TabFragmentPagerAdapter.FragmentBinder> fragmentBinders = new ArrayList<>();
        fragmentBinders.add(buildFragmentBinder(MyOrderTab.TAB_DISHES));
        fragmentBinders.add(buildCommentFragmentBinder(MyOrderTab.TAB_MY_CARD));
        return fragmentBinders;
    }

    private TabFragmentPagerAdapter.FragmentBinder buildFragmentBinder(MyOrderTab tab) {
        return new TabFragmentPagerAdapter.FragmentBinder(tab.getIndex(), getString(tab.getTextRestId()),
                0, new MyDishesOrderFragment());
    }

    private TabFragmentPagerAdapter.FragmentBinder buildCommentFragmentBinder(MyOrderTab tab) {
        return new TabFragmentPagerAdapter.FragmentBinder(tab.getIndex(), getString(tab.getTextRestId()),
                0, new MyCardOrderFragment());
    }


    enum MyOrderTab {
        TAB_DISHES(INDEX_DISHES, R.string.tab_title_dishes),
        TAB_MY_CARD(INDEX_MY_CARD, R.string.tab_title_my_card);

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
