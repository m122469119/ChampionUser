package com.goodchef.liking.module.message;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.aaron.android.framework.base.ui.BaseSwipeBackActivity;
import com.aaron.android.framework.base.widget.viewpager.TabFragmentPagerAdapter;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.CoursesResult;
import com.goodchef.liking.eventmessages.RefshReadMessage;
import com.goodchef.liking.module.home.LikingHomeActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:消息和公告
 * Author : shaozucheng
 * Time: 下午3:37
 * version 1.0.0
 */

public class MessageActivity extends BaseSwipeBackActivity {

    public static final String NOTICE_DATA = "notice_data";
    public static final String CURRENT_TAB = "current_tab";
    public static final String MSG_ID = "msg_id";
    public static final String ENTER = "enter";

    private static final int INDEX_ANNOUNCEMENT = 0;//公告
    private static final int INDEX_MESSAGE = 1;//消息

    @BindView(R.id.message_tabLayout)
    TabLayout mMessageTabLayout;
    @BindView(R.id.message_left_btn)
    ImageView mMessageLeftBtn;
    @BindView(R.id.message_viewpager)
    ViewPager mMessageViewpager;
    private TabFragmentPagerAdapter mTabFragmentPagerAdapter;
    private int currentInt = 0;
    CoursesResult.Courses.Gym mNoticeGym;
    private String msgId = "";
    private String enter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        currentInt = getIntent().getIntExtra(CURRENT_TAB, 0);
        msgId = getIntent().getStringExtra(MSG_ID);
        enter = getIntent().getStringExtra(ENTER);
        initData();
        initView();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        mNoticeGym = (CoursesResult.Courses.Gym) bundle.getSerializable(NOTICE_DATA);
    }

    private void initView() {
        initWidget();
        initTableLayout();
        initViewPage();
    }

    private void initWidget() {
        mMessageLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFinishActivity();
            }
        });
    }

    private void doFinishActivity() {
        if (!StringUtils.isEmpty(enter)) {
            startActivity(LikingHomeActivity.class);
            finish();
        } else {
            postEvent(new RefshReadMessage());
            finish();
        }
    }

    private void initTableLayout() {
        mMessageTabLayout.addTab(mMessageTabLayout.newTab().setText(R.string.tab_title_announcement));
        mMessageTabLayout.addTab(mMessageTabLayout.newTab().setText(R.string.tab_title_message));
    }

    private void initViewPage() {
        mTabFragmentPagerAdapter = new TabFragmentPagerAdapter(this, getSupportFragmentManager(), getFragmentList());
        mMessageViewpager.setAdapter(mTabFragmentPagerAdapter);
        mMessageViewpager.setOffscreenPageLimit(2);
        mMessageTabLayout.setupWithViewPager(mMessageViewpager);//设置table和viewPage联动
        mMessageViewpager.setCurrentItem(currentInt);
    }

    private List<TabFragmentPagerAdapter.FragmentBinder> getFragmentList() {
        List<TabFragmentPagerAdapter.FragmentBinder> fragmentBinders = new ArrayList<>();
        fragmentBinders.add(buildFragmentBinder(MyTab.TAB_ANNOUNCEMENT));
        fragmentBinders.add(buildMessageFragmentBinder(MyTab.TAB_MESSAGE));
        return fragmentBinders;
    }

    private TabFragmentPagerAdapter.FragmentBinder buildFragmentBinder(MyTab tab) {
        return new TabFragmentPagerAdapter.FragmentBinder(tab.getIndex(), getString(tab.getTextRestId()),
                0, AnnouncementFragment.newInstance(mNoticeGym));
    }

    private TabFragmentPagerAdapter.FragmentBinder buildMessageFragmentBinder(MyTab tab) {
        return new TabFragmentPagerAdapter.FragmentBinder(tab.getIndex(), getString(tab.getTextRestId()),
                0, MessageFragment.newInstance(msgId));
    }


    private enum MyTab {
        TAB_ANNOUNCEMENT(INDEX_ANNOUNCEMENT, R.string.tab_title_announcement),
        TAB_MESSAGE(INDEX_MESSAGE, R.string.tab_title_message);

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doFinishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
