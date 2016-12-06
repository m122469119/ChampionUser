package com.goodchef.liking.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.swipeback.app.SwipeBackActivity;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.ResourceUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.BodyTestResult;
import com.goodchef.liking.http.result.data.BodyChartValueFormatter;
import com.goodchef.liking.mvp.presenter.BodyTestPresenter;
import com.goodchef.liking.mvp.view.BodyTestView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.utils.ChartColorUtil;
import com.goodchef.liking.utils.TypefaseUtil;
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

    private HImageView mTopBackgroundHImageView;
    private HImageView mHeadHImageView;
    private TextView mUserNameTextView;
    private ImageView mUserGenderImageView;
    private TextView mUserAgeTextView;
    private TextView mUserWeightTextView;
    private TextView mUserWeightUnit;
    private CardView mHeadCardView;

    //评分
    private TextView mBodyTestTimeTextView;//测试时间
    private MyCircleView mMyCircleView;//体测评分圆环
    private TextView mBodyGradeHistoryTextView;//体测评分历史记录

    //成分分析
    private RadarChart mBodyIngredientRadarChart;//身体分析雷达图
    private TextView mBodyRadarAnalyzeResultTextView;//身体成分分析结果
    private TextView mBodyElementHistoryTextView;//身体成分历史记录
    //肥胖分析
    private RadarChart mFatAnalyzeRadarChart;//肥胖分析雷达
    private TextView mFatAnalyzeResultTextView;//肥胖分析结论
    private TextView mFatAnalyzeHistoryTextView;//

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
    private TextView mEveryDayCalTextView;
    private TextView mEveryDayCalUnitTextView;
    private TextView mMuscleControlTextView;
    private TextView mMuscleControlUnitTextView;
    private TextView mFatControlTextView;
    private TextView mFatControlUnitTextView;

    private TextView mBodyTestHistoryTextView;//体测历史

    private BodyTestPresenter mBodyTestPresenter;
    private Typeface mTypeface;

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
        //透明导航栏

        initViewOnClickListener();
        initToolbar();
        sendRequest();
        mTypeface = TypefaseUtil.getImpactTypeface(this);
        initRadarChart(mBodyIngredientRadarChart);
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

        mBodyTestTimeTextView = (TextView) findViewById(R.id.body_test_time_TextView);
        mBodyGradeHistoryTextView = (TextView) findViewById(R.id.body_grade_history_TextView);
        mMyCircleView = (MyCircleView) findViewById(R.id.body_grade_MyCircleView);

        mBodyIngredientRadarChart = (RadarChart) findViewById(R.id.body_ingredient_RadarChart);
        mBodyRadarAnalyzeResultTextView = (TextView) findViewById(R.id.body_radar_analyze_result_TextView);
        mBodyElementHistoryTextView = (TextView) findViewById(R.id.body_element_history_TextView);

        mFatAnalyzeRadarChart = (RadarChart) findViewById(R.id.body_fat_RadarChart);
        mFatAnalyzeResultTextView = (TextView) findViewById(R.id.fat_analyze_result_TextView);
        mFatAnalyzeHistoryTextView = (TextView) findViewById(R.id.fat_analyze_history_TextView);

        mMuscleLayout = (RelativeLayout) findViewById(R.id.layout_muscle_view);
        mFatLayout = (RelativeLayout) findViewById(R.id.layout_fat_view);

        mEveryDayCalTextView = (TextView) findViewById(R.id.every_day_cal_TextView);
        mEveryDayCalUnitTextView = (TextView) findViewById(R.id.every_day_cal_unit_TextView);
        mMuscleControlTextView = (TextView) findViewById(R.id.muscle_control_TextView);
        mMuscleControlUnitTextView = (TextView) findViewById(R.id.muscle_control_unit_TextView);
        mFatControlTextView = (TextView) findViewById(R.id.fat_control_TextView);
        mFatControlUnitTextView = (TextView) findViewById(R.id.fat_control_unit_TextView);


        mBodyTestHistoryTextView = (TextView) findViewById(R.id.body_test_history_TextView);
    }

    private void initViewOnClickListener() {
        mBodyElementHistoryTextView.setOnClickListener(this);
        mBodyTestHistoryTextView.setOnClickListener(this);
        mBodyGradeHistoryTextView.setOnClickListener(this);

    }

    private void sendRequest() {
        if (mBodyTestPresenter == null) {
            mBodyTestPresenter = new BodyTestPresenter(this, this);
        }
        mBodyTestPresenter.getBodyData("");
    }

    private void initRadarChart(RadarChart radarChart) {
        radarChart.setBackgroundColor(Color.rgb(255, 255, 255));
        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(ChartColorUtil.CHART_LIGHT_GRAY);
        radarChart.setWebLineWidthInner(1f);
        radarChart.setWebColorInner(ChartColorUtil.CHART_LIGHT_GRAY);
        radarChart.setWebAlpha(255);
        radarChart.setDescription("");//去掉说明
        radarChart.setRotationEnabled(false);//设置旋转
        radarChart.animateXY(1400, 1400, Easing.EasingOption.EaseInOutQuad, Easing.EasingOption.EaseInOutQuad);
    }


    @Override
    public void onClick(View v) {
        if (v == mBodyGradeHistoryTextView) {//体侧评分历史记录

        } else if (v == mBodyElementHistoryTextView) {//身体成分历史记录
            startActivity(BodyAnalyzeChartActivity.class);
        } else if (v == mBodyTestHistoryTextView) {
            startActivity(BodyTestHistoryActivity.class);
        } else if (v == mMuscleResultHistoryTextView) {//节段肌肉历史记录

        }
    }

    @Override
    public void updateBodyTestView(BodyTestResult.BodyTestData bodyTestData) {
        //用户基本信息
        BodyTestResult.BodyTestData.UserDataData bodyUserData = bodyTestData.getUserData();
        //评分数据
        BodyTestResult.BodyTestData.TopDataData gradeData = bodyTestData.getTopData();
        //身体成分分析
        BodyTestResult.BodyTestData.BodyAnalysisData bodyAnalysisData = bodyTestData.getBodyAnalysis();
        //肥胖分析
        BodyTestResult.BodyTestData.FatAnalysisData fatAnalysisData = bodyTestData.getFatAnalysis();
        //节段肌肉
        BodyTestResult.BodyTestData.MuscleData muscleData = bodyTestData.getMuscle();
        //节段脂肪
        BodyTestResult.BodyTestData.BodyFatData bodyFatData = bodyTestData.getBodyFat();
        //底部建议数据
        BodyTestResult.BodyTestData.FooterData adviceData = bodyTestData.getFooter();

        setUserData(bodyUserData);
        setGradeData(gradeData);
        setBodyElementAnalyzeData(bodyAnalysisData);
        setFatAnalysisData(fatAnalysisData);
        setMuscleData(muscleData);
        setBodyFatData(bodyFatData);
        setFootAdviceData(adviceData);
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
     *
     * @param gradeData 体测评分数据
     */
    private void setGradeData(BodyTestResult.BodyTestData.TopDataData gradeData) {
        if (gradeData != null) {
            mMyCircleView.setCurrentCount(100, Integer.parseInt(gradeData.getScore()));
            mMyCircleView.setTextTypeface(TypefaseUtil.getImpactTypeface(this));
            mBodyTestTimeTextView.setText(gradeData.getBodyTime());
        }
    }

    private void setBodyElementAnalyzeData(BodyTestResult.BodyTestData.BodyAnalysisData bodyAnalysisData) {
        if (bodyAnalysisData != null) {
            List<BodyTestResult.BodyTestData.BodyAnalysisData.BodyDataData> bodyDataList = bodyAnalysisData.getBodyData();
            List<String> chineseNameList = new ArrayList<>();
            List<Float> valueList = new ArrayList<>();
            if (bodyDataList != null && bodyDataList.size() > 0) {//组装显示的中文名称集合
                for (int i = 0; i < bodyDataList.size(); i++) {
                    chineseNameList.add(bodyDataList.get(i).getChineseName());
                    float max = Float.parseFloat(bodyDataList.get(i).getCriterionMax());
                    float min = Float.parseFloat(bodyDataList.get(i).getCriterionMin());
                    float value = Float.parseFloat(bodyDataList.get(i).getValue());
                    valueList.add(getRadarValueData(max, min, value));
                }
            }
            setBodyElementRadarChart(chineseNameList, valueList);
        }
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
        LogUtils.d(TAG, "radarValue ==" + radarValue + "");
        return radarValue;
    }

    /**
     * 设置身体分析雷达图
     *
     * @param chineseNameList
     * @param valueList
     */
    private void setBodyElementRadarChart(final List<String> chineseNameList, List<Float> valueList) {
        setBodyElementRadarData(valueList);
        XAxis xAxis = mBodyIngredientRadarChart.getXAxis();
        xAxis.setTextSize(8f);
        xAxis.setXOffset(0f);
        xAxis.setYOffset(0f);
        AxisValueFormatter formatter = new BodyChartValueFormatter(chineseNameList);

        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return chineseNameList.get((int) value % chineseNameList.size());
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        xAxis.setTextColor(ResourceUtils.getColor(R.color.lesson_details_dark_back));

        YAxis yAxis = mBodyIngredientRadarChart.getYAxis();
        yAxis.setLabelCount(2, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinValue(0f);
        yAxis.setAxisMaxValue(80f);
        yAxis.setDrawLabels(false); // no axis labels
        yAxis.setDrawAxisLine(false); // no axis line
        yAxis.setDrawGridLines(false); // no grid lines
        yAxis.setDrawZeroLine(false); // draw a zero line
        yAxis.setEnabled(false);
        yAxis.setLabelCount(2);
    }

    public void setBodyElementRadarData(List<Float> valueList) {

        ArrayList<RadarEntry> entries1 = new ArrayList<>();
        ArrayList<RadarEntry> entries2 = new ArrayList<>();
        ArrayList<RadarEntry> entries3 = new ArrayList<>();

        for (int i = 0; i < valueList.size(); i++) {
            entries1.add(new RadarEntry((1f / 3f) * 100f));
        }

        for (int i = 0; i < valueList.size(); i++) {
            entries2.add(new RadarEntry((2f / 3f) * 100f));
        }

        for (int i = 0; i < valueList.size(); i++) {
            entries3.add(new RadarEntry(valueList.get(i) * 100f));
        }

        RadarDataSet set1 = new RadarDataSet(entries1, "");
        set1.setColor(ChartColorUtil.CHART_WHITE);
        set1.setFillColor(ChartColorUtil.CHART_WHITE);
        set1.setDrawFilled(true);
        set1.setFillAlpha(100);
        set1.setLineWidth(1f);
        set1.setDrawHighlightCircleEnabled(false);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "");
        set2.setColor(ChartColorUtil.CHART_LIGHT_GRAY);
        set2.setFillColor(ChartColorUtil.CHART_LIGHT_GRAY);
        set2.setDrawFilled(true);
        set2.setFillAlpha(100);
        set2.setLineWidth(1f);
        set2.setDrawHighlightCircleEnabled(false);
        set2.setDrawHighlightIndicators(false);

        RadarDataSet set3 = new RadarDataSet(entries3, "");
        set3.setColor(ChartColorUtil.CHART_LIGHT_GREEN);
        set3.setFillColor(ChartColorUtil.CHART_LIGHT_GREEN);
        set3.setDrawFilled(true);
        set3.setFillAlpha(50);
        set3.setLineWidth(2f);
        set3.setDrawHighlightCircleEnabled(false);
        set3.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);
        sets.add(set2);
        sets.add(set3);

        RadarData data = new RadarData(sets);
        data.setDrawValues(false);

        mBodyIngredientRadarChart.setData(data);
        mBodyIngredientRadarChart.invalidate();
    }


    /**
     * 设置肥胖分析数据
     *
     * @param fatAnalysisData
     */
    private void setFatAnalysisData(BodyTestResult.BodyTestData.FatAnalysisData fatAnalysisData) {

    }

    /**
     * 设置节段肌肉数据
     *
     * @param muscleData
     */
    private void setMuscleData(BodyTestResult.BodyTestData.MuscleData muscleData) {
        setMuscleView(mMuscleLayout);

        mMuscleAnalyzeTitle.setText("节段肌肉");
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
        setBodyFatTypeface();
    }

    private void setBodyFatTypeface(){
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
     *
     * @param bodyFatData
     */
    private void setBodyFatData(BodyTestResult.BodyTestData.BodyFatData bodyFatData) {
        setMuscleView(mFatLayout);

        mMuscleAnalyzeTitle.setText("节段脂肪");
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

    }




    /**
     * 设置底部建议数据
     *
     * @param adviceData
     */
    private void setFootAdviceData(BodyTestResult.BodyTestData.FooterData adviceData) {
        List<BodyTestResult.BodyTestData.FooterData.BodyDataData> bodyData = adviceData.getBodyData();
        mEveryDayCalTextView.setText(bodyData.get(1).getValue() + " ");
        mEveryDayCalUnitTextView.setText(bodyData.get(1).getUnit());
        mMuscleControlTextView.setText(bodyData.get(0).getValue() + " ");
        mMuscleControlUnitTextView.setText(bodyData.get(0).getUnit());
        mFatControlTextView.setText(bodyData.get(2).getValue() + " ");
        mFatControlUnitTextView.setText(bodyData.get(2).getUnit());

        setTxtViewTypeface(mEveryDayCalTextView);
        setTxtViewTypeface(mEveryDayCalUnitTextView);
        setTxtViewTypeface(mMuscleControlTextView);
        setTxtViewTypeface(mMuscleControlUnitTextView);
        setTxtViewTypeface(mFatControlTextView);
        setTxtViewTypeface(mFatControlUnitTextView);
    }

    private void setTxtViewTypeface(TextView mTextView){
        mTextView.setTypeface(mTypeface);
    }


    @Override
    public void handleNetworkFailure() {

    }
}
