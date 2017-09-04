package com.goodchef.liking.module.train;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BaseRecyclerAdapter;
import com.goodchef.liking.adapter.SportDataAdapter;
import com.goodchef.liking.data.remote.retrofit.result.SportListResult;
import com.goodchef.liking.data.remote.retrofit.result.SportWeekResult;
import com.goodchef.liking.data.remote.retrofit.result.data.SportDataEntity;
import com.goodchef.liking.module.smartspot.SmartSpotDetailActivity;
import com.goodchef.liking.utils.TypefaseUtil;
import com.goodchef.liking.widgets.HistogramView;

import java.util.List;


public class SportDataActivity extends AppBarMVPSwipeBackActivity<SportDataContract.Presenter> implements SportDataContract.View {
    public static final String SPORT_MINS = "sport_mins";
    public static final String SPORT_DAYS = "sport_days";
    public static final String SPORT_TIMES = "sport_times";
    public static final String SHOW_ACTION = "show_action";

    private String mSportMin = "--";
    private String mSportDay = "--";
    private String mSportTimes = "--";

    private RecyclerView mRecyclerView;
    private SportDataAdapter mAdapter;

    StateView mStateView;

    @BindView(R.id.bg_image)
    ImageView mHeaderBgImage;

    @BindView(R.id.person_body_title)
    TextView mHeaderTitle;

    @BindView(R.id.text_point_1)
    TextView mHeaderMin;

    @BindView(R.id.text_point_unit_1)
    TextView mHeaderMinUnit;

    @BindView(R.id.text_point_2)
    TextView mHeaderDay;

    @BindView(R.id.text_point_unit_2)
    TextView mHeaderDayUnit;

    @BindView(R.id.text_point_3)
    TextView mHeaderTime;

    @BindView(R.id.text_point_unit_3)
    TextView mHeaderTimeUnit;

    @BindView(R.id.tablayout_sport)
    TabLayout mTabLayout;

    ImageView mRightImage;
    private View mFooterView;

    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_contains);
        mTypeface = TypefaseUtil.getImpactTypeface(this);
        initIntent();
        initView();
    }

    private void initView() {
        mStateView = (StateView) findViewById(R.id.state_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_sport);
        mAdapter = new SportDataAdapter(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View headView = inflater.inflate(R.layout.layout_sport_data_header, null);
        ImageView imageView = (ImageView) headView.findViewById(R.id.body_data_arrow);
        imageView.setVisibility(View.INVISIBLE);
        initHeadView(headView);
        mFooterView = inflater.inflate(R.layout.item_sport_data_empty, null);
        mAdapter.setHeaderView(headView);
        mAdapter.setFooterView(mFooterView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<SportListResult.DataBean.ListBean>() {

            @Override
            public void onItemClick(int position, SportListResult.DataBean.ListBean data) {
                if (data.getType() == SportListResult.DataBean.ListBean.TYPE_SMARTSPOT) {
                    SmartSpotDetailActivity.launch(SportDataActivity.this, String.valueOf(data.getRecordId()));
                }
            }
        });
        setRightIcon(R.mipmap.sport_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getSportShare(SportDataActivity.this);
            }
        });
    }

    private void initHeadView(View headView) {
        ButterKnife.bind(this, headView);

        mHeaderTitle.setText(R.string.today_train_data);
        mHeaderMinUnit.setText(getString(R.string.Mins));
        mHeaderDayUnit.setText(getString(R.string.Days));
        mHeaderTimeUnit.setText(getString(R.string.Times));

        mHeaderMin.setText(mSportMin);
        mHeaderDay.setText(mSportDay);
        mHeaderTime.setText(mSportTimes);

        mHeaderMin.setTypeface(mTypeface);
        mHeaderDay.setTypeface(mTypeface);
        mHeaderTime.setTypeface(mTypeface);
        mHeaderMinUnit.setTypeface(mTypeface);
        mHeaderDayUnit.setTypeface(mTypeface);
        mHeaderTimeUnit.setTypeface(mTypeface);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //mTabLayout.addTab();

        bindTabLayout();
        requestData();
    }

    public void requestData() {
        mPresenter.getSportStats();
    }


    private void bindTabLayout() {
        for (int i = 0; i < 7; i++) {
            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setCustomView(getTabView(i));
            mTabLayout.addTab(tab);
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                view.findViewById(R.id.image_triangle).setVisibility(View.VISIBLE);
                HistogramView histogramView = (HistogramView) view.findViewById(R.id.histogramview);
                histogramView.setColor(ContextCompat.getColor(SportDataActivity.this, R.color.his_bg_green));
                mPresenter.getSportList(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                view.findViewById(R.id.image_triangle).setVisibility(View.INVISIBLE);
                HistogramView histogramView = (HistogramView) view.findViewById(R.id.histogramview);
                histogramView.setColor(ContextCompat.getColor(SportDataActivity.this, R.color.his_bg));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                view.findViewById(R.id.image_triangle).setVisibility(View.VISIBLE);
                HistogramView histogramView = (HistogramView) view.findViewById(R.id.histogramview);
                histogramView.setColor(ContextCompat.getColor(SportDataActivity.this, R.color.his_bg_green));
            }
        });
    }

    private View getTabView(int position) {
        return LayoutInflater.from(this).inflate(R.layout.item_sport_tab, null);
    }

    private void setTabView() {
        for (int i = 0; i < 7; i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            View customView = tab.getCustomView();
            SportDataEntity date4Index = mPresenter.getDate4Index(i);
            TextView title = (TextView) customView.findViewById(R.id.text_date);
            TextView content = (TextView) customView.findViewById(R.id.text_week);
            HistogramView histogramView = (HistogramView) customView.findViewById(R.id.histogramview);
            histogramView.setPercentageText(date4Index.getPercentageText());
            histogramView.setPercentage(Float.parseFloat(date4Index.getPercentage()));
            title.setText(date4Index.getTitle());
            content.setText(date4Index.getContent());
        }
        mTabLayout.getTabAt(6).select();
    }


    private void initIntent() {
        Intent intent = getIntent();
        if (intent == null || !SHOW_ACTION.equals(intent.getAction()) || intent.getExtras() == null) {
            finish();
        }
        Bundle bundle = intent.getExtras();
        mSportMin = bundle.getString(SPORT_MINS);
        mSportDay = bundle.getString(SPORT_DAYS);
        mSportTimes = bundle.getString(SPORT_TIMES);
    }

    @Override
    public void setPresenter() {
        mPresenter = new SportDataContract.Presenter();
    }

    @Override
    public void updateSportStatsView(SportWeekResult value) {
        if (value != null) {
            setTabView();
            setTitle(value.getData().getTitle());
            mStateView.setState(StateView.State.SUCCESS);
        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }
    }

    @Override
    public void updateSportListView(SportListResult value) {
        List<SportListResult.DataBean.ListBean> data = value.getData().getList();
        mAdapter.setDatas(data);
        mAdapter.notifyDataSetChanged();

        ViewGroup.LayoutParams params = mFooterView.getLayoutParams();
        if(null == data || data.size() == 0){
            mFooterView.setVisibility(View.VISIBLE);
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            mFooterView.setVisibility(View.GONE);
            params.height = 0;
        }
    }

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }
}
