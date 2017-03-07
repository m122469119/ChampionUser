package com.goodchef.liking.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.swipeback.app.SwipeBackActivity;
import com.aaron.android.framework.utils.DisplayUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.ChangeGymCityAdapter;
import com.goodchef.liking.eventmessages.RefreshChangeCityMessage;
import com.goodchef.liking.fragment.ChangeGymFragment;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.data.CityData;
import com.goodchef.liking.mvp.ChangeGymContract;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.NavigationBarUtil;
import com.goodchef.liking.utils.UMengCountUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明: 切换场馆
 * Author shaozucheng
 * Time:16/9/14 下午3:24
 */
public class ChangeGymActivity extends SwipeBackActivity implements ChangeGymContract.ChangeGymView{
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.right_drawer)
    ListView mListView;
    @BindView(R.id.change_gym_toolbar_right_title)
    TextView mRightTitleTextView;
    @BindView(R.id.change_gym_toolbar_right_icon)
    ImageView mRightIconArrow;
    @BindView(R.id.change_gym_toolbar_title)
    TextView mTitleTextView;
    @BindView(R.id.change_gym_toolbar_left_icon)
    ImageView mLeftIcon;

    private View mCityHeadView;
    private View mCityFootView;
    private TextView mCityHeadText;
    private RelativeLayout mCurrentCityLayout;
    private RelativeLayout mLayoutCityFootView;

    private String gymId;//场馆id
    private int tabIndex;//从哪个位置切换过来的标志位，首页或者是买卡

    ChangeGymContract.ChangeGymPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chang_gym);
        ButterKnife.bind(this);
        mPresenter = new ChangeGymContract.ChangeGymPresenter(this, this);
        initView();
        initData();
        mPresenter.initTitleLocation();
    }

    private void initView() {
        initCityHeadView();
        initCityFootView();
    }

    private void initCityHeadView() {
        mCityHeadView = LayoutInflater.from(this).inflate(R.layout.item_city_head_view, mListView, false);
        mCityHeadText = (TextView) mCityHeadView.findViewById(R.id.city_head_test);
        mCurrentCityLayout = (RelativeLayout) mCityHeadView.findViewById(R.id.layout_current_city);
        mCurrentCityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter.isLocation()) {
                    setDrawerLayout();
                    mRightTitleTextView.setText(mPresenter.getCurrentCityName());
                    if (!StringUtils.isEmpty(mPresenter.doLocationCity())) {//如果当前城市在开通范围城市范围之内
                        mPresenter.compareSelectCity(mPresenter.getCurrentCityName());
                        postEvent(new RefreshChangeCityMessage(mPresenter.doLocationCity(), mPresenter.getLongitude(), mPresenter.getLatitude()));
                    }
                } else {
                    mPresenter.setIsSecondLocation(true);
                    mPresenter.initTitleLocation();
                }
            }
        });
    }

    private void initCityFootView() {
        mCityFootView = LayoutInflater.from(this).inflate(R.layout.item_city_foot_view, mListView, false);
        mLayoutCityFootView = (RelativeLayout) mCityFootView.findViewById(R.id.layout_city_foot_view);
    }

    private void initData() {
        mPresenter.setCityId(getIntent().getStringExtra(LikingHomeActivity.KEY_SELECT_CITY_ID));
        mPresenter.setIsLoaction(getIntent().getBooleanExtra(LikingHomeActivity.KEY_WHETHER_LOCATION, false));
        gymId = getIntent().getStringExtra(LikingLessonFragment.KEY_GYM_ID);
        tabIndex = getIntent().getIntExtra(LikingHomeActivity.KEY_TAB_INDEX, 0);

        mTitleTextView.setText(getString(R.string.title_change_gym));

        setGymFragment();
        setCityListData();
    }

    private void setGymFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(LikingHomeActivity.KEY_SELECT_CITY_ID, mPresenter.getCityId());
        bundle.putInt(LikingHomeActivity.KEY_TAB_INDEX, tabIndex);
        bundle.putString(LikingLessonFragment.KEY_GYM_ID, gymId);
        bundle.putBoolean(LikingHomeActivity.KEY_WHETHER_LOCATION, mPresenter.isLocation());
        fragmentTransaction.add(R.id.gym_content_frame, ChangeGymFragment.newInstance(bundle));
        fragmentTransaction.commit();
    }

    private void setCityListData() {
        mPresenter.setCityListData();
        setCityOnItemClickListener();
        setCityFootView();
    }

    private void setCityOnItemClickListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.city_name);
                if (textView != null) {
                    CityData cityData = (CityData) textView.getTag();
                    if (cityData != null) {
                        mPresenter.setSelectCityName(cityData.getCityName());
                        mPresenter.compareSelectCity(mPresenter.getSelectCityName());
                        mPresenter.compareCurrentCity(mPresenter.getSelectCityName());
                        setDrawerLayout();
                        mRightTitleTextView.setText(mPresenter.getSelectCityName());
                        postEvent(new RefreshChangeCityMessage(String.valueOf(cityData.getCityId()), mPresenter.getLongitude(), mPresenter.getLatitude()));
                        UMengCountUtil.UmengCount(ChangeGymActivity.this, UmengEventId.CHANGE_CITY, mPresenter.getSelectCityName());
                    }
                }
            }
        });
    }

    @OnClick({R.id.change_gym_toolbar_right_title,
            R.id.change_gym_toolbar_right_icon,
            R.id.change_gym_toolbar_left_icon,
    })
    public void onClick(View v) {
        if (v == mRightTitleTextView || v == mRightIconArrow) {
            UMengCountUtil.UmengCount(this, UmengEventId.RIGHT_ICON_ARROW_BTN);
            setDrawerLayout();
        } else if (v == mLeftIcon) {
            finish();
        }
    }

    private void setDrawerLayout() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.END);
        }
    }

    private void setCityFootView() {
        if (mPresenter.getChangeGymCityAdapter() != null) {
            if (mCityFootView != null) {
                setFootViewHeight();
                mListView.addFooterView(mCityFootView);
            }
        }
    }

    private void setFootViewHeight() {
        int heightPixels = DisplayUtils.getHeightPixels();
        int ActionBarHeight = DisplayUtils.getActionBarSize(this);
        int footViewContentHeight = DisplayUtils.dp2px(100);
        int cityListHeight = 0;
        if (mPresenter.getCityDataList() != null && mPresenter.getCityDataList().size() > 0) {
            cityListHeight = (mPresenter.getCityDataList().size() + 1) * DisplayUtils.dp2px(40);
        }
        int blankHeight;
        WindowManager wmManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        boolean hasSoft = NavigationBarUtil.hasSoftKeys(wmManager);
        if (hasSoft) {
            int navigationBarHeight = NavigationBarUtil.getNavigationBarHeight(this);
            blankHeight = heightPixels - ActionBarHeight - cityListHeight - navigationBarHeight - DisplayUtils.dp2px(30);
        } else {
            blankHeight = heightPixels - ActionBarHeight - cityListHeight - DisplayUtils.dp2px(30);
        }
        AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) mLayoutCityFootView.getLayoutParams();
        if (blankHeight > 0 && blankHeight > footViewContentHeight) {
            layoutParams.height = blankHeight;
        } else {
            layoutParams.height = layoutParams.WRAP_CONTENT;
        }
        mLayoutCityFootView.setLayoutParams(layoutParams);
    }

    @Override
    public void setCityHeadView() {
        if (mPresenter.getChangeGymCityAdapter() != null) {
            if (mCityHeadView != null) {
                mListView.removeHeaderView(mCityHeadView);
                if (mPresenter.isLocation()) {
                    mCityHeadText.setText(getString(R.string.current_city) + mPresenter.getCurrentCityName());
                } else {
                    mCityHeadText.setText(getString(R.string.location_fail_repeat_location));
                }
                mListView.addHeaderView(mCityHeadView);
            }
        }
        if (!StringUtils.isEmpty(mPresenter.getSelectCityName())) {
            mPresenter.compareCurrentCity(mPresenter.getSelectCityName());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void setRightTitleTextViewText(String text) {
        mRightTitleTextView.setText(text);
    }

    @Override
    public void setListViewAdapter(ChangeGymCityAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    @Override
    public void setCityHeadTextTextColor(int color) {
        mCityHeadText.setTextColor(color);
    }

}
