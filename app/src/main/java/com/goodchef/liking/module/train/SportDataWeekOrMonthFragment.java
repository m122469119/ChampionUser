package com.goodchef.liking.module.train;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aaron.android.framework.base.mvp.BaseMVPFragment;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.LogUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BaseRecyclerAdapter;
import com.goodchef.liking.adapter.SportHistogramAdapter;
import com.goodchef.liking.data.remote.retrofit.result.SportListResult;
import com.goodchef.liking.data.remote.retrofit.result.SportStatsResult;
import com.goodchef.liking.data.remote.retrofit.result.SportUserStatResult;
import com.goodchef.liking.data.remote.retrofit.result.data.SportDataEntity;
import com.goodchef.liking.utils.TypefaseUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的训练数据记录 - 周或月
 */
public class SportDataWeekOrMonthFragment extends BaseMVPFragment<SportDataContract.Presenter> implements SportDataContract.View {

    public static final String SPORTDATA_TIME_TYPE_KEY = "timetype";

    private ScrollView rootView;

    @BindView(R.id.state_view)
    StateView mStateView;

    @BindView(R.id.recyclerView_sport)
    RecyclerView mSportRecyclerView;

    @BindView(R.id.sport_data_header_layout)
    LinearLayout mSportDataHeaderLayout;

    TextView mSportRecordDate; //选中日期

    TextView mSportTotalSeconds; //运动时间

    TextView mSportTotalDays; //运动天数

    TextView mSportTotalKcal; //燃烧卡路里

    TextView mSportTotalExercise; //运动次数

    TextView mSportRunKilometre; //跑步-公里

    TextView mSportRunMin; //跑步-分钟

    TextView mSportTrainingTimes; //力量训练-次数

    TextView mSportTrainingMin; //力量训练-分钟

    TextView mSportGroupLessonSection; //团体课-节

    TextView mSportGroupLessonMin; //团体课-min

    TextView mSportPrivateTeachingSection; //私教课-节

    TextView mSportPrivateTeachingMin; //私教课-min

    private SportHistogramAdapter mHistogramAdapter = null;

    private boolean isLoadMore = false;

    private int typeTime = -1;

    private Toast mToastLoadMore = null;

    public static SportDataWeekOrMonthFragment newInstance(int type) {
        SportDataWeekOrMonthFragment fragment = new SportDataWeekOrMonthFragment();
        Bundle args = new Bundle();
        args.putInt(SPORTDATA_TIME_TYPE_KEY, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_sport_week_day_contains, container, false);
        rootView = new ScrollView(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.setLayoutParams(layoutParams);
        rootView.removeAllViews();
        rootView.addView(viewRoot);
        ButterKnife.bind(this, rootView);
        initData();
        initView();
        return rootView;
    }

    private void initView() {
        initHeadView();
        initRecordView();
    }

    private void initRecordView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_sport_record_stat, null);
        mSportDataHeaderLayout.addView(view);

        mSportRecordDate = (TextView) view.findViewById(R.id.sport_record_stat_date);
        mSportTotalSeconds = (TextView) view.findViewById(R.id.sport_total_seconds);
        setImpactTypeface(mSportTotalSeconds);
        mSportTotalDays = (TextView) view.findViewById(R.id.sport_total_days);
        setImpactTypeface(mSportTotalDays);
        mSportTotalKcal = (TextView) view.findViewById(R.id.sport_total_kcal);
        setImpactTypeface(mSportTotalKcal);
        mSportTotalExercise = (TextView) view.findViewById(R.id.sport_total_exercise);
        setImpactTypeface(mSportTotalExercise);
        mSportRunKilometre = (TextView) view.findViewById(R.id.sport_run_kilometre);
        setImpactTypeface(mSportRunKilometre);
        mSportRunMin = (TextView) view.findViewById(R.id.sport_run_min);
        setImpactTypeface(mSportRunMin);
        mSportTrainingTimes = (TextView) view.findViewById(R.id.sport_strength_training_times);
        setImpactTypeface(mSportTrainingTimes);
        mSportTrainingMin = (TextView) view.findViewById(R.id.sport_strength_training_min);
        setImpactTypeface(mSportTrainingMin);
        mSportGroupLessonSection = (TextView) view.findViewById(R.id.sport_group_lesson_section);
        setImpactTypeface(mSportGroupLessonSection);
        mSportGroupLessonMin = (TextView) view.findViewById(R.id.sport_group_lesson_section_min);
        setImpactTypeface(mSportGroupLessonMin);
        mSportPrivateTeachingSection = (TextView) view.findViewById(R.id.sport_private_teaching_section);
        setImpactTypeface(mSportPrivateTeachingSection);
        mSportPrivateTeachingMin = (TextView) view.findViewById(R.id.sport_private_teaching_min);
        setImpactTypeface(mSportPrivateTeachingMin);
    }

    public void setImpactTypeface(TextView textView) {
        if (textView != null) {
            textView.setTypeface(TypefaseUtil.getImpactTypeface(getContext()));
        }
    }

    private void initData() {

    }

    private void initHeadView() {
        bindSportRecyclerLayout();
        requestData();
    }

    public void requestData() {
        mPresenter.getSportStats();
    }

    private void bindSportRecyclerLayout() {
        mHistogramAdapter = new SportHistogramAdapter(getContext());
        mSportRecyclerView.setAdapter(mHistogramAdapter);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        mSportRecyclerView.setLayoutManager(manager);
        mSportRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LogUtils.i(TAG, "-----------onScrollStateChanged-----------");
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !recyclerView.canScrollHorizontally(-1)) {
                    mPresenter.getSportStats();
                    isLoadMore = true;

                    if(mToastLoadMore == null) {
                        mToastLoadMore =  Toast.makeText(getContext().getApplicationContext(),
                                ResourceUtils.getString(R.string.sport_load_more), Toast.LENGTH_SHORT);
                    } else {
                        mToastLoadMore.setText(R.string.sport_load_more);
                    }
                    mToastLoadMore.show();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LogUtils.e(TAG, "onScrolled: DX:" + dx + ";DY:" + dy);
                LogUtils.e(TAG, "CHECK_SCROLL_LEFT:" + recyclerView.canScrollHorizontally(-1));
                LogUtils.e(TAG, "CHECK_SCROLL_RIGHT:" + recyclerView.canScrollHorizontally(1));
            }
        });
        mHistogramAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                int currPoi = mHistogramAdapter.getSelectCurrPosition();
                if (currPoi < mHistogramAdapter.getDatas().size()) {
                    mHistogramAdapter.getDatas().get(currPoi).setChecked(false);
                }
                SportDataEntity entity = mHistogramAdapter.getDatas().get(position);
                entity.setChecked(true);
                setSportRecordDateText(entity);
                mHistogramAdapter.setSelectCurrPosition(position);
                mPresenter.getSportUserStatsResult(position);
            }
        });
    }

    private void setSportRecordDateText(SportDataEntity entity){
        if(entity == null) return;
        String recordDateText = "";
        if (typeTime == SportDataEntity.TYPE_TIME_WEEK) {
            recordDateText = mPresenter.isThisWeek(entity.getStartTime())
                    ? ResourceUtils.getString(R.string.this_week) : entity.getTitle();

        } else if (typeTime == SportDataEntity.TYPE_TIME_MONTH) {
            recordDateText = mPresenter.isThisMonth(entity.getStartTime())
                    ? ResourceUtils.getString(R.string.this_month) : entity.getTitle();
        }
        mSportRecordDate.setText(recordDateText);
    }

    private void setSportRecyclerView() {
        mHistogramAdapter.setDatas(mPresenter.getSportDatas(), isLoadMore);
        mHistogramAdapter.notifyDataSetChanged();
        if (mHistogramAdapter.getSelectCurrPosition() != -1 && !isLoadMore) {
            int pos = mHistogramAdapter.getSelectCurrPosition();
            mPresenter.getSportUserStatsResult(pos);
            setSportRecordDateText(mHistogramAdapter.getDatas().get(pos));
        }
    }

    @Override
    public void setPresenter() {
        typeTime = getArguments().getInt(SPORTDATA_TIME_TYPE_KEY, -1);
        mPresenter = new SportDataContract.Presenter(typeTime);
    }

    @Override
    public void updateSportStatsView(SportStatsResult value) {
        if (value != null) {
            mStateView.setState(StateView.State.SUCCESS);
            setSportRecyclerView();
        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }
    }

    @Override
    public void updateSportListView(SportListResult value) {
    }

    @Override
    public void updateSportUserStatView(SportUserStatResult value) {
        if (value != null && value.getData() != null) {
            SportUserStatResult.DataBean bean = value.getData();
            mSportTotalSeconds.setText(bean.getTotalSeconds());
            mSportTotalDays.setText(bean.getTotalDay());
            mSportTotalKcal.setText(bean.getTotalCal());
            mSportTotalExercise.setText(bean.getTotalTime());
            mSportRunKilometre.setText(bean.getRunDistance());
            mSportRunMin.setText(bean.getRunTime());
            mSportTrainingTimes.setText(bean.getSmartspotExercise());
            mSportTrainingMin.setText(bean.getSmartspotTime());
            mSportGroupLessonSection.setText(bean.getCourse());
            mSportGroupLessonMin.setText(bean.getCourseTime());
            mSportPrivateTeachingSection.setText(bean.getPersonal());
            mSportPrivateTeachingMin.setText(bean.getPersonalTime());
        }
    }

    public void sportShare() {
        mPresenter.getSportShare(getContext(), mHistogramAdapter.getSelectCurrPosition());
    }

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }
}
