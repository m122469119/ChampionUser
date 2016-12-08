package com.goodchef.liking.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.swipeback.app.SwipeBackActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BodyAnalyzeAdapter;
import com.goodchef.liking.adapter.FatAnalyzeAdapter;
import com.goodchef.liking.http.result.BodyTestResult;
import com.goodchef.liking.mvp.presenter.BodyTestPresenter;
import com.goodchef.liking.mvp.view.BodyTestView;
import com.goodchef.liking.utils.TypefaseUtil;
import com.goodchef.liking.widgets.CustomRadarView;
import com.goodchef.liking.widgets.MyCircleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午10:21
 * version 1.0.0
 */

public class BodyTestDataActivity extends SwipeBackActivity implements View.OnClickListener, BodyTestView {

    public static final String BODY_ID = "bodyId";
    public static final String SOURCE = "source";

    private HImageView mTopBackgroundHImageView;
    private HImageView mHeadHImageView;
    private TextView mUserNameTextView;
    private ImageView mUserGenderImageView;
    private TextView mUserAgeTextView;
    private TextView mUserWeightTextView;
    private TextView mUserWeightUnit;
    private CardView mHeadCardView;

    //评分
    private CardView mGradeCardView;
    private TextView mBodyGradeTitle;//标题
    private TextView mBodyTestTimeTextView;//测试时间
    private MyCircleView mMyCircleView;//体测评分圆环
    private TextView mBodyGradeHistoryTextView;//体测评分历史记录

    //成分分析
    private CardView mBodyRadarCardView;
    private CustomRadarView mBodyIngredientRadarChart;//身体分析雷达图
    private TextView mBodyRadarAnalyzeResultTextView;//身体成分分析结果
    private TextView mBodyElementHistoryTextView;//身体成分历史记录
    private ImageView mBodyRadarHelpImageView;//查看所有明细按钮
    private TextView mBodyRadarTitle;//标题
    //肥胖分析
    private CardView mFatCardView;
    private CustomRadarView mFatAnalyzeRadarChart;//肥胖分析雷达
    private TextView mFatAnalyzeResultTextView;//肥胖分析结论
    private TextView mFatAnalyzeHistoryTextView;//肥胖分析历史记录
    private TextView mFatAnalyzeTitle;//标题
    private ImageView mFatAnalyzeHelpImageView;//查看所有明细

    private RelativeLayout mMuscleLayout;
    private RelativeLayout mFatLayout;

    //--节段肌肉和节段脂肪---
    private CardView mMuscleCardView;
    private TextView mMuscleAnalyzeTitle;
    private ImageView mMuscleAnalyzeImageView;

    private TextView mLeftMuscleTextView;
    private TextView mLeftMuscleUnitTextView;
    private TextView mLeftMuscleEvaluateTextView;

    private TextView mRightMuscleTextView;
    private TextView mRightMuscleUnitTextView;
    private TextView mRightMuscleEvaluateTextView;

    private TextView mRightDownMuscleTextView;
    private TextView mRightDownMuscleUnitTextView;
    private TextView mRightDownMuscleEvaluateTextView;

    private TextView mLeftDownMuscleTextView;
    private TextView mLeftDownMuscleUnitTextView;
    private TextView mLeftDownMuscleEvaluateTextView;

    private TextView mMuscleAnalyzeResultTextView;
    private TextView mMuscleResultHistoryTextView;
    //---end------

    //footer建议
    private CardView mFootCardView;
    private TextView mEveryDayKcalTitleTextView;
    private TextView mEveryDayCalTextView;
    private TextView mEveryDayCalUnitTextView;

    private TextView mMuscleControlTitleTextView;
    private TextView mMuscleControlTextView;
    private TextView mMuscleControlUnitTextView;

    private TextView mFatControlTitleTextView;
    private TextView mFatControlTextView;
    private TextView mFatControlUnitTextView;
    private TextView mAdviceHistoryTextView;
    //---end-

    private TextView mBodyTestHistoryTextView;//体测历史

    private BodyTestPresenter mBodyTestPresenter;
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
        initView();
        //透明状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        bodyId = getIntent().getStringExtra(BODY_ID);
        sourse = getIntent().getStringExtra(SOURCE);
        setSourceView();
        initViewOnClickListener();
        initToolbar();
        sendRequest();
        mTypeface = TypefaseUtil.getImpactTypeface(this);
    }


    private void setSourceView() {
        if (sourse.equals("history")) {
            mBodyTestHistoryTextView.setVisibility(View.GONE);
        } else {
            mBodyTestHistoryTextView.setVisibility(View.VISIBLE);
        }
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_app_bar_style);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("体测数据");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    private void initView() {
        mTopBackgroundHImageView = (HImageView) findViewById(R.id.head_image_background);
        mHeadHImageView = (HImageView) findViewById(R.id.body_head_image);
        mUserNameTextView = (TextView) findViewById(R.id.person_user_name_TextView);
        mUserGenderImageView = (ImageView) findViewById(R.id.person_user_gender_ImageView);
        mUserAgeTextView = (TextView) findViewById(R.id.person_user_age_TextView);
        mUserWeightTextView = (TextView) findViewById(R.id.user_weight_TextView);
        mUserWeightUnit = (TextView) findViewById(R.id.user_weight_unit);
        mHeadCardView = (CardView) findViewById(R.id.body_test_CardView);

        mGradeCardView = (CardView) findViewById(R.id.body_grade_CardView);
        mBodyGradeTitle = (TextView) findViewById(R.id.body_grade_title);
        mBodyTestTimeTextView = (TextView) findViewById(R.id.body_test_time_TextView);
        mBodyGradeHistoryTextView = (TextView) findViewById(R.id.body_grade_history_TextView);
        mMyCircleView = (MyCircleView) findViewById(R.id.body_grade_MyCircleView);

        mBodyRadarCardView = (CardView) findViewById(R.id.body_radar_CardView);
        mBodyRadarTitle = (TextView) findViewById(R.id.body_radar_title);
        mBodyIngredientRadarChart = (CustomRadarView) findViewById(R.id.body_ingredient_RadarChart);
        mBodyRadarAnalyzeResultTextView = (TextView) findViewById(R.id.body_radar_analyze_result_TextView);
        mBodyElementHistoryTextView = (TextView) findViewById(R.id.body_element_history_TextView);
        mBodyRadarHelpImageView = (ImageView) findViewById(R.id.body_radar_help_ImageView);

        mFatCardView = (CardView) findViewById(R.id.fat_analyze_CardView);
        mFatAnalyzeTitle = (TextView) findViewById(R.id.fat_analyze_title_TextView);
        mFatAnalyzeRadarChart = (CustomRadarView) findViewById(R.id.body_fat_RadarChart);
        mFatAnalyzeResultTextView = (TextView) findViewById(R.id.fat_analyze_result_TextView);
        mFatAnalyzeHistoryTextView = (TextView) findViewById(R.id.fat_analyze_history_TextView);
        mFatAnalyzeHelpImageView = (ImageView) findViewById(R.id.fat_analyze_help_ImageView);

        mMuscleLayout = (RelativeLayout) findViewById(R.id.layout_muscle_view);
        mFatLayout = (RelativeLayout) findViewById(R.id.layout_fat_view);

        mFootCardView = (CardView) findViewById(R.id.muscle_fat_advice_CardView);
        mEveryDayKcalTitleTextView = (TextView) findViewById(R.id.every_day_kcal_title_TextView);
        mEveryDayCalTextView = (TextView) findViewById(R.id.every_day_cal_TextView);
        mEveryDayCalUnitTextView = (TextView) findViewById(R.id.every_day_cal_unit_TextView);
        mMuscleControlTitleTextView = (TextView) findViewById(R.id.muscle_control_title_TextView);
        mMuscleControlTextView = (TextView) findViewById(R.id.muscle_control_TextView);
        mMuscleControlUnitTextView = (TextView) findViewById(R.id.muscle_control_unit_TextView);
        mFatControlTitleTextView = (TextView) findViewById(R.id.fat_control_title_TextView);
        mFatControlTextView = (TextView) findViewById(R.id.fat_control_TextView);
        mFatControlUnitTextView = (TextView) findViewById(R.id.fat_control_unit_TextView);
        mAdviceHistoryTextView = (TextView) findViewById(R.id.muscle_fat_history_TextView);

        mBodyTestHistoryTextView = (TextView) findViewById(R.id.body_test_history_TextView);

        if (Build.VERSION.SDK_INT < 21) {
            mHeadCardView.setCardElevation(0f);
            mGradeCardView.setCardElevation(0f);
            mBodyRadarCardView.setCardElevation(0f);
            mFatCardView.setCardElevation(0f);
            mFootCardView.setCardElevation(0f);
        } else {
            mHeadCardView.setCardElevation(10f);
            mGradeCardView.setCardElevation(10f);
            mBodyRadarCardView.setCardElevation(10f);
            mFatCardView.setCardElevation(10f);
            mFootCardView.setCardElevation(10f);
        }
    }

    private void initViewOnClickListener() {
        mBodyElementHistoryTextView.setOnClickListener(this);
        mBodyTestHistoryTextView.setOnClickListener(this);
        mBodyGradeHistoryTextView.setOnClickListener(this);
        mFatAnalyzeHistoryTextView.setOnClickListener(this);
        mBodyRadarHelpImageView.setOnClickListener(this);
        mFatAnalyzeHelpImageView.setOnClickListener(this);
        mAdviceHistoryTextView.setOnClickListener(this);
    }

    private void sendRequest() {
        if (mBodyTestPresenter == null) {
            mBodyTestPresenter = new BodyTestPresenter(this, this);
        }
        mBodyTestPresenter.getBodyData(bodyId);
    }


    @Override
    public void onClick(View v) {
        if (v == mBodyGradeHistoryTextView) {//体侧评分历史记录
            startBodyAnalyzeChartActivity(gradeData.getTitle(), gradeData.getType());
        } else if (v == mBodyElementHistoryTextView) {//身体成分历史记录
            startBodyAnalyzeChartActivity(bodyAnalysisData.getTitle(), bodyAnalysisData.getType());
        } else if (v == mFatAnalyzeHistoryTextView) {//肥胖分析历史记录
            startBodyAnalyzeChartActivity(fatAnalysisData.getTitle(), fatAnalysisData.getType());
        } else if (v == mAdviceHistoryTextView) {
            startBodyAnalyzeChartActivity(adviceData.getTitle(), adviceData.getType());
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
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_body_ingredient_view, null, false);
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
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_body_ingredient_view, null, false);
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
    private void setFatAnalyzeDialogView(View view) {
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
    private void setBodyAnalyzeView(View view) {
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
            HImageLoaderSingleton.getInstance().loadImage(mTopBackgroundHImageView, imageUrl);
            HImageLoaderSingleton.getInstance().loadImage(mHeadHImageView, imageUrl);
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
            mBodyTestTimeTextView.setText(gradeData.getBodyTime());
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
                chineseNameList.add(bodyDataList.get(i).getChineseName());
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

        List<BodyTestResult.BodyTestData.MuscleData.BodyDataData> bodyListData = muscleData.getBodyData();

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
        mMuscleResultHistoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBodyAnalyzeChartActivity(muscleData.getTitle(), muscleData.getType());
            }
        });
    }

    private void setMuscleView(View view) {
        mMuscleCardView = (CardView) view.findViewById(R.id.body_muscle_CardView);
        mMuscleAnalyzeTitle = (TextView) view.findViewById(R.id.muscle_analyze_title);
        mMuscleAnalyzeImageView = (ImageView) view.findViewById(R.id.muscle_analyze_imageView);

        mLeftMuscleTextView = (TextView) view.findViewById(R.id.left_muscle_TextView);
        mLeftMuscleUnitTextView = (TextView) view.findViewById(R.id.left_muscle_unit_TextView);
        mLeftMuscleEvaluateTextView = (TextView) view.findViewById(R.id.left_muscle_evaluate_TextView);

        mRightMuscleTextView = (TextView) view.findViewById(R.id.right_muscle_TextView);
        mRightMuscleUnitTextView = (TextView) view.findViewById(R.id.right_muscle_unit_TextView);
        mRightMuscleEvaluateTextView = (TextView) view.findViewById(R.id.right_muscle_evaluate_TextView);

        mRightDownMuscleTextView = (TextView) view.findViewById(R.id.right_down_muscle_TextView);
        mRightDownMuscleUnitTextView = (TextView) view.findViewById(R.id.right_down_muscle_unit_TextView);
        mRightDownMuscleEvaluateTextView = (TextView) view.findViewById(R.id.right_down_muscle_evaluate_TextView);

        mLeftDownMuscleTextView = (TextView) view.findViewById(R.id.lef_down_muscle_TextView);
        mLeftDownMuscleUnitTextView = (TextView) view.findViewById(R.id.lef_down_muscle_unit_TextView);
        mLeftDownMuscleEvaluateTextView = (TextView) view.findViewById(R.id.lef_down_muscle_evaluate_TextView);

        mMuscleAnalyzeResultTextView = (TextView) view.findViewById(R.id.muscle_analyze_result_TextView);
        mMuscleResultHistoryTextView = (TextView) view.findViewById(R.id.muscle_result_history_TextView);

        if (Build.VERSION.SDK_INT < 21) {
            mMuscleCardView.setCardElevation(0);
        } else {
            mMuscleCardView.setCardElevation(10);
        }
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
    }

    /**
     * 设置节段脂肪数据
     */
    private void setBodyFatData() {
        setMuscleView(mFatLayout);

        mMuscleAnalyzeTitle.setText(bodyFatData.getTitle());
        mMuscleAnalyzeImageView.setImageResource(R.mipmap.icon_axunge);

        List<BodyTestResult.BodyTestData.BodyFatData.BodyDataData> bodyListData = bodyFatData.getBodyData();

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

        mMuscleAnalyzeResultTextView.setText(bodyFatData.getAdvise());
        mMuscleResultHistoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBodyAnalyzeChartActivity(bodyFatData.getTitle(), bodyFatData.getType());
            }
        });
    }


    /**
     * 设置底部建议数据
     */
    private void setFootAdviceData() {
        List<BodyTestResult.BodyTestData.FooterData.BodyDataData> bodyData = adviceData.getBodyData();
        mEveryDayKcalTitleTextView.setText(bodyData.get(1).getChineseName());
        mEveryDayCalTextView.setText(bodyData.get(1).getValue() + " ");
        mEveryDayCalUnitTextView.setText(bodyData.get(1).getUnit());

        mMuscleControlTitleTextView.setText(bodyData.get(0).getChineseName());
        mMuscleControlTextView.setText(bodyData.get(0).getValue() + " ");
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

    }
}
