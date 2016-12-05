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
import android.widget.TextView;

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
import com.goodchef.liking.mvp.presenter.BodyTestPresenter;
import com.goodchef.liking.mvp.view.BodyTestView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.utils.ChartColorUtil;
import com.goodchef.liking.utils.TypefaseUtil;
import com.goodchef.liking.widgets.MyCircleView;

import java.util.ArrayList;

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
    private TextView mBodyGradeHistoryTextView;//体测评分历史记录
    private MyCircleView mMyCicleView;//体测评分圆环

    //成分分析
    private RadarChart mBodyIngredientRadarChart;//身体分析雷达图
    private TextView mBodyRadarAnalyzeResultTextView;//身体成分分析结果
    private TextView mBodyElementHistoryTextView;//身体成分历史记录
    //肥胖分析
    private RadarChart mFatAnalyzeRadarChart;//肥胖分析雷达
    private TextView mFatAnalyzeResultTextView;//肥胖分析结论
    private TextView mFatAnalyzeHistoryTextView;//肥胖分析历史记录

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
        setBodyIngredientRadarChart();
        initViewOnClickListener();
        initToolbar();
        sendRequest();
        mTypeface = TypefaseUtil.getImpactTypeface(this);
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
        mMyCicleView = (MyCircleView) findViewById(R.id.body_grade_MyCircleView);

        mBodyIngredientRadarChart = (RadarChart) findViewById(R.id.body_ingredient_RadarChart);
        mBodyRadarAnalyzeResultTextView = (TextView) findViewById(R.id.body_radar_analyze_result_TextView);
        mBodyElementHistoryTextView = (TextView) findViewById(R.id.body_element_history_TextView);

        mFatAnalyzeRadarChart = (RadarChart) findViewById(R.id.body_fat_RadarChart);
        mFatAnalyzeResultTextView = (TextView) findViewById(R.id.fat_analyze_result_TextView);
        mFatAnalyzeHistoryTextView = (TextView) findViewById(R.id.fat_analyze_history_TextView);

        mBodyTestHistoryTextView = (TextView) findViewById(R.id.body_test_history_TextView);
    }

    private void initViewOnClickListener() {
        mBodyElementHistoryTextView.setOnClickListener(this);
        mBodyTestHistoryTextView.setOnClickListener(this);
    }

    private void sendRequest() {
        if (mBodyTestPresenter == null) {
            mBodyTestPresenter = new BodyTestPresenter(this, this);
        }
        mBodyTestPresenter.getBodyData("");
    }

    private void setBodyIngredientRadarChart() {
        mBodyIngredientRadarChart.setBackgroundColor(Color.rgb(255, 255, 255));
        mBodyIngredientRadarChart.setWebLineWidth(1f);
        mBodyIngredientRadarChart.setWebColor(ChartColorUtil.CHART_LIGHT_GRAY);
        mBodyIngredientRadarChart.setWebLineWidthInner(1f);
        mBodyIngredientRadarChart.setWebColorInner(ChartColorUtil.CHART_LIGHT_GRAY);
        mBodyIngredientRadarChart.setWebAlpha(255);
        mBodyIngredientRadarChart.setDescription("");//去掉说明
        mBodyIngredientRadarChart.setRotationEnabled(false);//设置旋转
        setData();

        mBodyIngredientRadarChart.animateXY(1400, 1400, Easing.EasingOption.EaseInOutQuad, Easing.EasingOption.EaseInOutQuad);
        XAxis xAxis = mBodyIngredientRadarChart.getXAxis();

        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setAxisMaxValue(80f);

        xAxis.setValueFormatter(new AxisValueFormatter() {

            private String[] mActivities = new String[]{"Burger", "Steak", "Salad", "Pasta", "Pizza"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        xAxis.setTextColor(Color.BLACK);

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

        mBodyIngredientRadarChart.setDrawWeb(true);
    }

    public void setData() {

        float mult = 80;
        float min = 0;
        int cnt = 5;

        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            float val1 = 80 + (float) i;
            entries1.add(new RadarEntry(val1));

            float val2 = 50 + (float) i;
            entries2.add(new RadarEntry(val2));
        }

        RadarDataSet set1 = new RadarDataSet(entries1, "");
        set1.setColor(ChartColorUtil.CHART_LIGHT_GRAY);
        set1.setFillColor(ChartColorUtil.CHART_LIGHT_GRAY);
        set1.setDrawFilled(true);
        set1.setFillAlpha(100);
        set1.setLineWidth(1f);
        set1.setDrawHighlightCircleEnabled(false);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "");
        set2.setColor(ChartColorUtil.CHART_LIGHT_GREEN);
        set2.setFillColor(ChartColorUtil.CHART_LIGHT_GREEN);
        set2.setDrawFilled(true);
        set2.setFillAlpha(50);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(false);
        set2.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(sets);

        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.BLACK);

        mBodyIngredientRadarChart.setData(data);
        mBodyIngredientRadarChart.invalidate();
    }


    @Override
    public void onClick(View v) {
        if (v == mBodyElementHistoryTextView) {//身体成分历史记录
            startActivity(BodyAnalyzeChartActivity.class);
        } else if (v == mBodyTestHistoryTextView) {
            startActivity(BodyTestHistoryActivity.class);
        }
    }

    @Override
    public void updateBodyTestView(BodyTestResult.BodyTestData bodyTestData) {
        BodyTestResult.BodyTestData.UserDataData bodyUserData = bodyTestData.getUserData();
        setUserData(bodyUserData);
        setGradeData();
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

    private void setGradeData() {
        mMyCicleView.setCurrentCount(100, 72);
        mMyCicleView.setTextTypeface(TypefaseUtil.getImpactTypeface(this));
    }

    @Override
    public void handleNetworkFailure() {

    }
}
