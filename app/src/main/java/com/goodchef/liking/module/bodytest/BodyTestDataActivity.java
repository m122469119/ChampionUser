package com.goodchef.liking.module.bodytest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.BaseMVPActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BodyAnalyzeAdapter;
import com.goodchef.liking.adapter.FatAnalyzeAdapter;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.BodyTestResult;
import com.goodchef.liking.data.remote.retrofit.result.data.BodyData;
import com.goodchef.liking.module.body.analyze.BodyAnalyzeChartActivity;
import com.goodchef.liking.module.body.history.BodyTestHistoryActivity;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.goodchef.liking.utils.StatusBarUtils;
import com.goodchef.liking.utils.TypefaseUtil;
import com.goodchef.liking.widgets.AppBarStateChangeListener;
import com.goodchef.liking.widgets.CustomRadarView;
import com.goodchef.liking.widgets.MyCircleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:体测数据
 * Author : shaozucheng
 * Time: 上午10:21
 * version 1.0.0
 */

public class BodyTestDataActivity extends BaseMVPActivity<BodyTestDataContract.Presenter> implements BodyTestDataContract.View {

    public static final String BODY_ID = "bodyId";
    public static final String SOURCE = "source";

    @BindView(R.id.head_image_background)
    HImageView mTopBackgroundHImageView;
    @BindView(R.id.body_head_image)
    HImageView mHeadHImageView;
    @BindView(R.id.person_user_name_TextView)
    TextView mUserNameTextView;
    @BindView(R.id.person_user_gender_ImageView)
    ImageView mUserGenderImageView;
    @BindView(R.id.person_user_age_TextView)
    TextView mUserAgeTextView;
    @BindView(R.id.user_weight_TextView)
    TextView mUserWeightTextView;
    @BindView(R.id.user_weight_unit)
    TextView mUserWeightUnit;

    //评分
    @BindView(R.id.body_grade_title)
    TextView mBodyGradeTitle;//标题
    @BindView(R.id.body_test_time_TextView)
    TextView mBodyTestTimeTextView;//测试时间
    @BindView(R.id.body_grade_MyCircleView)
    MyCircleView mMyCircleView;//体测评分圆环
    @BindView(R.id.body_grade_history_TextView)
    TextView mBodyGradeHistoryTextView;//体测评分历史记录

    //成分分析

    @BindView(R.id.body_ingredient_RadarChart)
    CustomRadarView mBodyIngredientRadarChart;//身体分析雷达图
    @BindView(R.id.body_radar_analyze_result_TextView)
    TextView mBodyRadarAnalyzeResultTextView;//身体成分分析结果
    @BindView(R.id.body_element_history_TextView)
    TextView mBodyElementHistoryTextView;//身体成分历史记录
    @BindView(R.id.body_radar_help_ImageView)
    ImageView mBodyRadarHelpImageView;//查看所有明细按钮
    @BindView(R.id.body_radar_title)
    TextView mBodyRadarTitle;//标题

    //肥胖分析
    @BindView(R.id.body_fat_RadarChart)
    CustomRadarView mFatAnalyzeRadarChart;//肥胖分析雷达
    @BindView(R.id.fat_analyze_result_TextView)
    TextView mFatAnalyzeResultTextView;//肥胖分析结论
    @BindView(R.id.fat_analyze_history_TextView)
    TextView mFatAnalyzeHistoryTextView;//肥胖分析历史记录
    @BindView(R.id.fat_analyze_title_TextView)
    TextView mFatAnalyzeTitle;//标题
    @BindView(R.id.fat_analyze_help_ImageView)
    ImageView mFatAnalyzeHelpImageView;//查看所有明细

    @BindView(R.id.layout_muscle_view)
    RelativeLayout mMuscleLayout;
    @BindView(R.id.layout_fat_view)
    RelativeLayout mFatLayout;

    //--节段肌肉和节段脂肪---
    TextView mMuscleAnalyzeTitle;
    ImageView mMuscleAnalyzeImageView;

    TextView mLeftMusclePercentageTextView;
    TextView mLeftMuscleTextView;
    TextView mLeftMuscleUnitTextView;
    TextView mLeftMuscleEvaluateTextView;

    TextView mRightMusclePercentageTextView;
    TextView mRightMuscleTextView;
    TextView mRightMuscleUnitTextView;
    TextView mRightMuscleEvaluateTextView;

    TextView mRightDownMusclePercentageTextView;
    TextView mRightDownMuscleTextView;
    TextView mRightDownMuscleUnitTextView;
    TextView mRightDownMuscleEvaluateTextView;

    TextView mLeftDownMusclePercentageTextView;
    TextView mLeftDownMuscleTextView;
    TextView mLeftDownMuscleUnitTextView;
    TextView mLeftDownMuscleEvaluateTextView;

    TextView mMuscleAnalyzeResultTextView;

    TextView mMuscleResultHistoryTextView;
    //---end------

    //footer建议
    @BindView(R.id.muscle_fat_suggest_title_TextView)
    TextView mFooterTitleTextView;
    @BindView(R.id.every_day_kcal_title_TextView)
    TextView mEveryDayKcalTitleTextView;
    @BindView(R.id.every_day_cal_TextView)
    TextView mEveryDayCalTextView;
    @BindView(R.id.every_day_cal_unit_TextView)
    TextView mEveryDayCalUnitTextView;

    @BindView(R.id.muscle_control_title_TextView)
    TextView mMuscleControlTitleTextView;
    @BindView(R.id.muscle_control_TextView)
    TextView mMuscleControlTextView;
    @BindView(R.id.muscle_control_unit_TextView)
    TextView mMuscleControlUnitTextView;

    @BindView(R.id.fat_control_title_TextView)
    TextView mFatControlTitleTextView;
    @BindView(R.id.fat_control_TextView)
    TextView mFatControlTextView;
    @BindView(R.id.fat_control_unit_TextView)
    TextView mFatControlUnitTextView;
    @BindView(R.id.muscle_fat_history_TextView)
    TextView mAdviceHistoryTextView;
    //---end-

    @BindView(R.id.body_test_history_TextView)
    TextView mBodyTestHistoryTextView;//体测历史
    @BindView(R.id.body_test_no_data_view)
    LinearLayout mNoDataLayout;
    @BindView(R.id.no_data_bar_back)
    ImageView mNoDataAppBackImageView;
    @BindView(R.id.imageview_no_data)
    ImageView mNoDataImageView;
    @BindView(R.id.textview_refresh)
    TextView mNoDataTextView;
    @BindView(R.id.textview_no_data)
    TextView mNoDataPromptTextView;
    @BindView(R.id.body_test_CardView)
    CardView mHeadCardView;

    //title
    @BindView(R.id.body_test_AppBarLayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar_app_bar_style)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mTooBarTitle;

    private Typeface mTypeface;

    private BodyTestResult.BodyTestData.TopDataData gradeData;//评分数据
    private BodyTestResult.BodyTestData.BodyAnalysisData bodyAnalysisData;//身体成分分析Data
    private BodyTestResult.BodyTestData.FatAnalysisData fatAnalysisData;//肥胖分析Data
    private BodyTestResult.BodyTestData.MuscleData muscleData;//节段肌肉
    private BodyTestResult.BodyTestData.BodyFatData bodyFatData; //节段脂肪
    private BodyTestResult.BodyTestData.FooterData adviceData;//底部数据

    private String bodyId = "";
    private String sourse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_test_data);
        ButterKnife.bind(this);
        bodyId = getIntent().getStringExtra(BODY_ID);
        sourse = getIntent().getStringExtra(SOURCE);
        setSourceView();
        initViewOnClickListener();
        initToolbar(R.string.title_body_test);
        sendRequest();
        mTypeface = TypefaseUtil.getImpactTypeface(this);
        StatusBarUtils.setWindowStatusBarColor(this);
    }


    private void setSourceView() {
        if (sourse.equals("history")) {
            mBodyTestHistoryTextView.setVisibility(android.view.View.GONE);
        } else {
            mBodyTestHistoryTextView.setVisibility(android.view.View.VISIBLE);
        }
    }

    /**
     * 初始化Toolbar
     */
    public void initToolbar(int titleString) {
        mTooBarTitle.setText(titleString);
        setSupportActionBar(mToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示左边返回按钮
            actionBar.setDisplayShowTitleEnabled(false);//设置不显示Toolbar自带的title,用自己自定义的
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initViewOnClickListener() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) { //展开状态
                    mTooBarTitle.setTextColor(ResourceUtils.getColor(R.color.white));
                    mToolbar.setNavigationIcon(R.drawable.app_bar_white_back);
                } else if (state == State.COLLAPSED) {//折叠状态
                    mTooBarTitle.setTextColor(ResourceUtils.getColor(R.color.add_minus_dishes_text));
                    mToolbar.setNavigationIcon(R.drawable.app_bar_back);
                } else {//中间状态
                    mTooBarTitle.setTextColor(ResourceUtils.getColor(R.color.white));
                    mToolbar.setNavigationIcon(R.drawable.app_bar_white_back);
                }
            }
        });
        mNoDataAppBackImageView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                finish();
            }
        });
    }

    private void sendRequest() {
        mPresenter.getBodyData(this, bodyId);
    }


    @OnClick({
            R.id.body_grade_history_TextView,
            R.id.body_element_history_TextView,
            R.id.fat_analyze_history_TextView,
            R.id.muscle_fat_history_TextView,
            R.id.body_radar_help_ImageView,
            R.id.fat_analyze_help_ImageView,
            R.id.body_test_history_TextView,
    })
    public void onClick(android.view.View v) {
        if (v == mBodyGradeHistoryTextView) {//体侧评分历史记录
            startBodyAnalyzeChartActivity(gradeData.getTitle() + getString(R.string.history), gradeData.getType());
        } else if (v == mBodyElementHistoryTextView) {//身体成分历史记录
            startBodyAnalyzeChartActivity(bodyAnalysisData.getTitle() + getString(R.string.history), bodyAnalysisData.getType());
        } else if (v == mFatAnalyzeHistoryTextView) {//肥胖分析历史记录
            startBodyAnalyzeChartActivity(fatAnalysisData.getTitle() + getString(R.string.history), fatAnalysisData.getType());
        } else if (v == mAdviceHistoryTextView) {
            startBodyAnalyzeChartActivity(adviceData.getTitle() + getString(R.string.history), adviceData.getType());
        } else if (v == mBodyRadarHelpImageView) {//身体成分分析雷达图
            showBodyIngredientDialog();
        } else if (v == mFatAnalyzeHelpImageView) {//肥胖分析雷达图
            showFatAnalyzeDialog();
        } else if (v == mBodyTestHistoryTextView) {//历史记录
            startActivity(BodyTestHistoryActivity.class);
        }
    }

    /**
     * 展示肥胖分析dialog
     */
    private void showFatAnalyzeDialog() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        android.view.View view = LayoutInflater.from(this).inflate(R.layout.dialog_body_ingredient_view, null, false);
        setFatAnalyzeDialogView(view);
        builder.setCustomView(view);
        builder.setPositiveButton(R.string.dialog_know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 展示身体成分分析具体数据
     */
    private void showBodyIngredientDialog() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        android.view.View view = LayoutInflater.from(this).inflate(R.layout.dialog_body_ingredient_view, null, false);
        setBodyAnalyzeView(view);
        builder.setCustomView(view);
        builder.setPositiveButton(R.string.dialog_know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 设置肥胖分析view
     *
     * @param view
     */
    private void setFatAnalyzeDialogView(android.view.View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dialog_analyze_RecyclerView);
        TextView titleTextView = (TextView) view.findViewById(R.id.dialog_body_ingredient_title);
        List<BodyTestResult.BodyTestData.FatAnalysisData.BodyDataData> bodyDataList = fatAnalysisData.getBodyData();
        titleTextView.setText(fatAnalysisData.getTitle());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FatAnalyzeAdapter adapter = new FatAnalyzeAdapter(this);
        adapter.setData(bodyDataList);
        recyclerView.setAdapter(adapter);
    }


    /**
     * 设置身体成分分析明细对话框数据
     *
     * @param view
     */
    private void setBodyAnalyzeView(android.view.View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dialog_analyze_RecyclerView);
        TextView titleTextView = (TextView) view.findViewById(R.id.dialog_body_ingredient_title);
        List<BodyTestResult.BodyTestData.BodyAnalysisData.BodyDataData> bodyDataList = bodyAnalysisData.getBodyData();
        titleTextView.setText(bodyAnalysisData.getTitle());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BodyAnalyzeAdapter analyzeAdapter = new BodyAnalyzeAdapter(this);
        analyzeAdapter.setData(bodyDataList);
        recyclerView.setAdapter(analyzeAdapter);
    }


    /**
     * 跳转到历史记录
     *
     * @param title   标题
     * @param modules 类型
     */
    private void startBodyAnalyzeChartActivity(String title, String modules) {
        Intent intent = new Intent(this, BodyAnalyzeChartActivity.class);
        intent.putExtra(BodyAnalyzeChartActivity.KEY_HISTORY_TITLE, title);
        intent.putExtra(BodyAnalyzeChartActivity.KEY_HISTORY_MODULES, modules);
        startActivity(intent);
    }

    @Override
    public void updateBodyTestView(BodyTestResult.BodyTestData bodyTestData) {
        //用户基本信息
        BodyTestResult.BodyTestData.UserDataData bodyUserData = bodyTestData.getUserData();
        //评分数据
        gradeData = bodyTestData.getTopData();
        //身体成分分析
        bodyAnalysisData = bodyTestData.getBodyAnalysis();
        //肥胖分析
        fatAnalysisData = bodyTestData.getFatAnalysis();
        //节段肌肉
        muscleData = bodyTestData.getMuscle();
        //节段脂肪
        bodyFatData = bodyTestData.getBodyFat();
        //底部建议数据
        adviceData = bodyTestData.getFooter();

        if (bodyUserData == null && gradeData == null && bodyAnalysisData == null
                && fatAnalysisData == null && muscleData == null
                && bodyFatData == null && adviceData == null) {
            mNoDataLayout.setVisibility(android.view.View.VISIBLE);
            mHeadCardView.setVisibility(android.view.View.GONE);
            mNoDataImageView.setImageResource(R.drawable.icon_no_data);
            mNoDataTextView.setVisibility(android.view.View.GONE);
            mNoDataPromptTextView.setVisibility(android.view.View.VISIBLE);
            mNoDataPromptTextView.setText(R.string.no_data);
            mAppBarLayout.setVisibility(android.view.View.GONE);
        } else {
            mHeadCardView.setVisibility(android.view.View.VISIBLE);
            mNoDataLayout.setVisibility(android.view.View.GONE);
            mAppBarLayout.setVisibility(android.view.View.VISIBLE);
        }

        if (bodyUserData != null) {
            setUserData(bodyUserData);
        }
        if (gradeData != null) {
            setGradeData();
        }
        if (bodyAnalysisData != null) {
            mBodyRadarTitle.setText(bodyAnalysisData.getTitle());
            setBodyElementAnalyzeData();
        }
        if (fatAnalysisData != null) {
            mFatAnalyzeTitle.setText(fatAnalysisData.getTitle());
            setFatAnalysisData();
        }
        if (muscleData != null) {
            setMuscleData();
        }
        if (bodyFatData != null) {
            setBodyFatData();
        }
        if (adviceData != null) {
            setFootAdviceData();
        }
    }

    /**
     * 设置用户基本信息
     *
     * @param bodyUserData 用户基本数据
     */
    private void setUserData(BodyTestResult.BodyTestData.UserDataData bodyUserData) {
        mUserNameTextView.setText(bodyUserData.getName());
        LikingPreference.setNickName(bodyUserData.getName());
        String gender = bodyUserData.getGender();
        if ("0".equals(gender)) {
            mUserGenderImageView.setImageResource(R.drawable.icon_women);
        } else if ("1".equals(gender)) {
            mUserGenderImageView.setImageResource(R.drawable.icon_man);
        }
        mUserAgeTextView.setText(bodyUserData.getAge());
        mUserWeightTextView.setText(bodyUserData.getHeight());
        mUserWeightUnit.setTypeface(mTypeface);
        mUserWeightTextView.setTypeface(mTypeface);
        mUserAgeTextView.setTypeface(mTypeface);

        String imageUrl = bodyUserData.getAvatar();
        if (!StringUtils.isEmpty(imageUrl)) {
            LikingPreference.setUserIconUrl(imageUrl);
            HImageLoaderSingleton.loadImage(mTopBackgroundHImageView, imageUrl, this);
            HImageLoaderSingleton.loadImage(mHeadHImageView, imageUrl, this);
        }
    }

    /***
     * 设置体测评分数据
     */
    private void setGradeData() {
        if (gradeData != null) {
            mBodyGradeTitle.setText(gradeData.getTitle());
            mMyCircleView.setCurrentCount(100, Integer.parseInt(gradeData.getScore()));
            mMyCircleView.setTextTypeface(TypefaseUtil.getImpactTypeface(this));
            mBodyTestTimeTextView.setText(getString(R.string.test_time) + gradeData.getBodyTime());
        }
    }

    private void setBodyElementAnalyzeData() {
        List<BodyTestResult.BodyTestData.BodyAnalysisData.BodyDataData> bodyDataList = bodyAnalysisData.getBodyData();
        List<String> chineseNameList = new ArrayList<>();
        List<String> unitList = new ArrayList<>();
        List<Float> valueList = new ArrayList<>();
        if (bodyDataList != null && bodyDataList.size() > 0) {//组装显示的中文名称集合
            for (int i = 0; i < bodyDataList.size(); i++) {
                chineseNameList.add(bodyDataList.get(i).getChineseName());
                unitList.add(bodyDataList.get(i).getValue() + bodyDataList.get(i).getUnit());
                float max = Float.parseFloat(bodyDataList.get(i).getCriterionMax());
                float min = Float.parseFloat(bodyDataList.get(i).getCriterionMin());
                float value = Float.parseFloat(bodyDataList.get(i).getValue());
                valueList.add(getRadarValueData(max, min, value));
            }
        }
        setBodyElementRadarChart(chineseNameList, valueList, unitList);
        mBodyRadarAnalyzeResultTextView.setText(bodyAnalysisData.getAdvise());
    }

    /**
     * 计算雷达图比例
     *
     * @param max   最大值
     * @param min   最小值
     * @param value 数据值
     * @return
     */
    private float getRadarValueData(float max, float min, float value) {
        //实际值所在点的百分比 =（标准值上限百分比-标准值下限百分比）/  (标准值上限 - 标准值下限)   *（实际值 -  标准值下限）+  标准值下限百分比
        float radarValue;
        radarValue = ((2f / 3f) - (1f / 3f)) / (max - min) * (value - min) + (1f / 3f);
        if (radarValue > 1) {
            radarValue = 1;
        } else if (radarValue < 0) {
            radarValue = 0;
        }
        Log.d(TAG, "radarValue ==" + radarValue + "");
        return radarValue * 100;
    }

    /**
     * 设置身体分析雷达图
     *
     * @param chineseNameList
     * @param valueList
     */
    private void setBodyElementRadarChart(List<String> chineseNameList, List<Float> valueList, List<String> unitList) {
        List<Float> minList = new ArrayList<>();
        List<Float> maxList = new ArrayList<>();
        for (int i = 0; i < valueList.size(); i++) {
            minList.add((1f / 3f) * 100f);
            maxList.add((2f / 3f) * 100f);
        }
        mBodyIngredientRadarChart.setDataCount(valueList.size());
        mBodyIngredientRadarChart.setTitles(chineseNameList);
        mBodyIngredientRadarChart.setRadarValueList(valueList);
        mBodyIngredientRadarChart.setMinValueList(minList);
        mBodyIngredientRadarChart.setMaxValueList(maxList);
        mBodyIngredientRadarChart.setValueUnitList(unitList);
    }


    /**
     * 设置肥胖分析数据
     */
    private void setFatAnalysisData() {
        List<BodyTestResult.BodyTestData.FatAnalysisData.BodyDataData> bodyDataList = fatAnalysisData.getBodyData();
        List<String> chineseNameList = new ArrayList<>();
        List<String> unitList = new ArrayList<>();
        List<Float> valueList = new ArrayList<>();
        if (bodyDataList != null && bodyDataList.size() > 0) {//组装显示的中文名称集合
            for (int i = 0; i < bodyDataList.size(); i++) {
                String englishName = bodyDataList.get(i).getEnglishName();
                if (!StringUtils.isEmpty(englishName)) {
                    chineseNameList.add(bodyDataList.get(i).getChineseName() + "(" + englishName + ")");
                } else {
                    chineseNameList.add(bodyDataList.get(i).getChineseName());
                }
                unitList.add(bodyDataList.get(i).getValue() + bodyDataList.get(i).getUnit());
                float max = Float.parseFloat(bodyDataList.get(i).getCriterionMax());
                float min = Float.parseFloat(bodyDataList.get(i).getCriterionMin());
                float value = Float.parseFloat(bodyDataList.get(i).getValue());
                valueList.add(getRadarValueData(max, min, value));
            }
        }
        setBodyFatRadarChart(chineseNameList, valueList, unitList);
        mFatAnalyzeResultTextView.setText(fatAnalysisData.getAdvise());
    }

    private void setBodyFatRadarChart(List<String> chineseNameList, List<Float> valueList, List<String> unitList) {
        List<Float> minList = new ArrayList<>();
        List<Float> maxList = new ArrayList<>();
        for (int i = 0; i < valueList.size(); i++) {
            minList.add((1f / 3f) * 100f);
            maxList.add((2f / 3f) * 100f);
        }
        mFatAnalyzeRadarChart.setDataCount(valueList.size());
        mFatAnalyzeRadarChart.setTitles(chineseNameList);
        mFatAnalyzeRadarChart.setRadarValueList(valueList);
        mFatAnalyzeRadarChart.setMinValueList(minList);
        mFatAnalyzeRadarChart.setMaxValueList(maxList);
        mFatAnalyzeRadarChart.setValueUnitList(unitList);
    }

    /**
     * 设置节段肌肉数据
     */
    private void setMuscleData() {
        setMuscleView(mMuscleLayout);

        mMuscleAnalyzeTitle.setText(muscleData.getTitle());
        mMuscleAnalyzeImageView.setImageResource(R.mipmap.icon_muscle);

        List<BodyData> bodyListData = muscleData.getBodyData();

        mLeftMuscleTextView.setText(bodyListData.get(0).getValue() + " ");
        mLeftMuscleUnitTextView.setText(bodyListData.get(0).getUnit());
        mLeftMuscleEvaluateTextView.setText(bodyListData.get(0).getEvaluate());

        mRightMuscleTextView.setText(bodyListData.get(1).getValue() + " ");
        mRightMuscleUnitTextView.setText(bodyListData.get(1).getUnit());
        mRightMuscleEvaluateTextView.setText(bodyListData.get(1).getEvaluate());

        mRightDownMuscleTextView.setText(bodyListData.get(2).getValue() + " ");
        mRightDownMuscleUnitTextView.setText(bodyListData.get(2).getUnit());
        mRightDownMuscleEvaluateTextView.setText(bodyListData.get(2).getEvaluate());

        mLeftDownMuscleTextView.setText(bodyListData.get(3).getValue() + " ");
        mLeftDownMuscleUnitTextView.setText(bodyListData.get(3).getUnit());
        mLeftDownMuscleEvaluateTextView.setText(bodyListData.get(3).getEvaluate());

        mMuscleAnalyzeResultTextView.setText(muscleData.getAdvise());

        mMuscleResultHistoryTextView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                startBodyAnalyzeChartActivity(muscleData.getTitle() + getString(R.string.history), muscleData.getType());
            }
        });
        mLeftMusclePercentageTextView.setVisibility(android.view.View.GONE);
        mRightMusclePercentageTextView.setVisibility(android.view.View.GONE);
        mRightDownMusclePercentageTextView.setVisibility(android.view.View.GONE);
        mLeftDownMusclePercentageTextView.setVisibility(android.view.View.GONE);
    }

    private void setMuscleView(android.view.View view) {
        mMuscleResultHistoryTextView = (TextView) view.findViewById(R.id.muscle_result_history_TextView);
        mMuscleAnalyzeTitle = (TextView) view.findViewById(R.id.muscle_analyze_title);
        mMuscleAnalyzeImageView = (ImageView) view.findViewById(R.id.muscle_analyze_imageView);

        mLeftMusclePercentageTextView = (TextView) view.findViewById(R.id.left_muscle_percentage_TextView);
        mLeftMuscleTextView = (TextView) view.findViewById(R.id.left_muscle_TextView);
        mLeftMuscleUnitTextView = (TextView) view.findViewById(R.id.left_muscle_unit_TextView);
        mLeftMuscleEvaluateTextView = (TextView) view.findViewById(R.id.left_muscle_evaluate_TextView);

        mRightMusclePercentageTextView = (TextView) view.findViewById(R.id.right_muscle_percentage_TextView);
        mRightMuscleTextView = (TextView) view.findViewById(R.id.right_muscle_TextView);
        mRightMuscleUnitTextView = (TextView) view.findViewById(R.id.right_muscle_unit_TextView);
        mRightMuscleEvaluateTextView = (TextView) view.findViewById(R.id.right_muscle_evaluate_TextView);

        mRightDownMusclePercentageTextView = (TextView) view.findViewById(R.id.right_down_muscle_percentage_TextView);
        mRightDownMuscleTextView = (TextView) view.findViewById(R.id.right_down_muscle_TextView);
        mRightDownMuscleUnitTextView = (TextView) view.findViewById(R.id.right_down_muscle_unit_TextView);
        mRightDownMuscleEvaluateTextView = (TextView) view.findViewById(R.id.right_down_muscle_evaluate_TextView);

        mLeftDownMusclePercentageTextView = (TextView) view.findViewById(R.id.left_down_muscle_percentage_TextView);
        mLeftDownMuscleTextView = (TextView) view.findViewById(R.id.lef_down_muscle_TextView);
        mLeftDownMuscleUnitTextView = (TextView) view.findViewById(R.id.lef_down_muscle_unit_TextView);
        mLeftDownMuscleEvaluateTextView = (TextView) view.findViewById(R.id.lef_down_muscle_evaluate_TextView);

        mMuscleAnalyzeResultTextView = (TextView) view.findViewById(R.id.muscle_analyze_result_TextView);

        setBodyFatTypeface();
    }

    private void setBodyFatTypeface() {
        setTxtViewTypeface(mLeftMuscleTextView);
        setTxtViewTypeface(mLeftMuscleUnitTextView);
        setTxtViewTypeface(mLeftMuscleEvaluateTextView);
        setTxtViewTypeface(mRightMuscleTextView);
        setTxtViewTypeface(mRightMuscleUnitTextView);
        setTxtViewTypeface(mRightMuscleEvaluateTextView);
        setTxtViewTypeface(mRightDownMuscleTextView);
        setTxtViewTypeface(mRightDownMuscleUnitTextView);
        setTxtViewTypeface(mRightDownMuscleEvaluateTextView);
        setTxtViewTypeface(mLeftDownMuscleTextView);
        setTxtViewTypeface(mLeftDownMuscleUnitTextView);
        setTxtViewTypeface(mLeftDownMuscleEvaluateTextView);
        setTxtViewTypeface(mLeftMusclePercentageTextView);
        setTxtViewTypeface(mRightMusclePercentageTextView);
        setTxtViewTypeface(mRightDownMusclePercentageTextView);
        setTxtViewTypeface(mLeftDownMusclePercentageTextView);
    }

    /**
     * 设置节段脂肪数据
     */
    private void setBodyFatData() {
        setMuscleView(mFatLayout);

        mMuscleAnalyzeTitle.setText(bodyFatData.getTitle());
        mMuscleAnalyzeImageView.setImageResource(R.mipmap.icon_axunge);

        List<BodyData> bodyListData = bodyFatData.getBodyData();

        mLeftMusclePercentageTextView.setVisibility(android.view.View.VISIBLE);
        mRightMusclePercentageTextView.setVisibility(android.view.View.VISIBLE);
        mRightDownMusclePercentageTextView.setVisibility(android.view.View.VISIBLE);
        mLeftDownMusclePercentageTextView.setVisibility(android.view.View.VISIBLE);

        mLeftMusclePercentageTextView.setText(bodyListData.get(0).getContent());
        mLeftMuscleTextView.setText(bodyListData.get(0).getValue() + " ");
        mLeftMuscleUnitTextView.setText(bodyListData.get(0).getUnit());
        mLeftMuscleEvaluateTextView.setText(bodyListData.get(0).getEvaluate());

        mRightMusclePercentageTextView.setText(bodyListData.get(1).getContent());
        mRightMuscleTextView.setText(bodyListData.get(1).getValue() + " ");
        mRightMuscleUnitTextView.setText(bodyListData.get(1).getUnit());
        mRightMuscleEvaluateTextView.setText(bodyListData.get(1).getEvaluate());

        mRightDownMusclePercentageTextView.setText(bodyListData.get(2).getContent());
        mRightDownMuscleTextView.setText(bodyListData.get(2).getValue() + " ");
        mRightDownMuscleUnitTextView.setText(bodyListData.get(2).getUnit());
        mRightDownMuscleEvaluateTextView.setText(bodyListData.get(2).getEvaluate());

        mLeftDownMusclePercentageTextView.setText(bodyListData.get(3).getContent());
        mLeftDownMuscleTextView.setText(bodyListData.get(3).getValue() + " ");
        mLeftDownMuscleUnitTextView.setText(bodyListData.get(3).getUnit());
        mLeftDownMuscleEvaluateTextView.setText(bodyListData.get(3).getEvaluate());

        mMuscleAnalyzeResultTextView.setText(bodyFatData.getAdvise());
        mMuscleResultHistoryTextView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                startBodyAnalyzeChartActivity(bodyFatData.getTitle() + getString(R.string.history), bodyFatData.getType());
            }
        });
    }


    /**
     * 设置底部建议数据
     */
    private void setFootAdviceData() {
        List<BodyTestResult.BodyTestData.FooterData.BodyDataData> bodyData = adviceData.getBodyData();
        mFooterTitleTextView.setText(adviceData.getTitle());
        mEveryDayKcalTitleTextView.setText(bodyData.get(1).getChineseName());
        mEveryDayCalTextView.setText(bodyData.get(1).getValue() + " ");
        mEveryDayCalUnitTextView.setText(bodyData.get(1).getUnit());

        mMuscleControlTitleTextView.setText(bodyData.get(0).getChineseName());
        String bodyValue = bodyData.get(0).getValue();
        if (Float.parseFloat(bodyValue) >= 0) {
            mMuscleControlTextView.setText("+" + bodyValue + " ");
        } else {
            mMuscleControlTextView.setText(bodyValue + " ");
        }
        mMuscleControlUnitTextView.setText(bodyData.get(0).getUnit());

        mFatControlTitleTextView.setText(bodyData.get(2).getChineseName());
        String fatValue = bodyData.get(2).getValue();
        if (Float.parseFloat(fatValue) >= 0) {
            mFatControlTextView.setText("+" + fatValue + " ");
        } else {
            mFatControlTextView.setText(fatValue + " ");
        }
        mFatControlUnitTextView.setText(bodyData.get(2).getUnit());

        setTxtViewTypeface(mEveryDayCalTextView);
        setTxtViewTypeface(mEveryDayCalUnitTextView);
        setTxtViewTypeface(mMuscleControlTextView);
        setTxtViewTypeface(mMuscleControlUnitTextView);
        setTxtViewTypeface(mFatControlTextView);
        setTxtViewTypeface(mFatControlUnitTextView);
    }

    private void setTxtViewTypeface(TextView mTextView) {
        mTextView.setTypeface(mTypeface);
    }


    @Override
    public void handleNetworkFailure() {
        mNoDataLayout.setVisibility(android.view.View.VISIBLE);
        mHeadCardView.setVisibility(android.view.View.GONE);
        mAppBarLayout.setVisibility(android.view.View.GONE);
        mNoDataPromptTextView.setVisibility(android.view.View.GONE);
        mNoDataImageView.setImageResource(R.drawable.network_anomaly);
        mNoDataTextView.setText(R.string.refresh_btn_text);
        mNoDataTextView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                sendRequest();
            }
        });
    }

    @Override
    public void setPresenter() {
        mPresenter = new BodyTestDataContract.Presenter();
    }
}
