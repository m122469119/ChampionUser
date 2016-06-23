package com.goodchef.liking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BannerPagerAdapter;
import com.goodchef.liking.fragment.LikingNearbyFragment;
import com.goodchef.liking.http.result.BannerResult;
import com.goodchef.liking.http.result.FoodDetailsResult;
import com.goodchef.liking.mvp.presenter.FoodDetailsPresenter;
import com.goodchef.liking.mvp.view.FoodDetailsView;
import com.goodchef.liking.widgets.autoviewpager.InfiniteViewPager;
import com.goodchef.liking.widgets.autoviewpager.indicator.IconPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/26 下午3:00
 */
public class DishesDetailsActivity extends AppBarActivity implements FoodDetailsView, View.OnClickListener {
    private TextView mDishesDetailsNameTextView;//菜品名称
    private TextView mSurplusNumberTextView;//剩余份数
    private TextView mDishesMoneyTextView;//多少钱
    private TextView mDishesDescribeTextView;//菜品介绍
    private TextView mDishesTagsTextView;//tag
    private TextView mHowNumberBuyTextView;//多少人买过

    private TextView mDishesKcalTextView;//卡路里
    private TextView mDishesProteinTextView;//蛋白质
    private TextView mCarbonAndWaterTextView;//碳水化合物
    private TextView mDishesFatTextView;//脂肪

    private ImageView mReduceImageBtn;//减少
    private ImageView mAddImageBtn;//添加
    private TextView mFoodBuyNumberTextView;
    private TextView mImmediatelyBuyBtn;//立即购买


    public static final int IMAGE_SLIDER_SWITCH_DURATION = 4000;
    private InfiniteViewPager mImageViewPager;
    private IconPageIndicator mIconPageIndicator;
    private BannerPagerAdapter mBannerPagerAdapter;

    private String mUserCityId;
    private String mGoodId;
    private FoodDetailsPresenter mFoodDetailsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_details);
        setTitle("XXX菜");

        initView();
        initData();
        requestBanner();
        sendFoodDetailsRequest();
        setViewOnClickListener();
    }

    //发送详情请求
    private void sendFoodDetailsRequest() {
        mFoodDetailsPresenter = new FoodDetailsPresenter(this, this);
        mFoodDetailsPresenter.getFoodDetails(mUserCityId, mGoodId);
    }

    private void initData() {
        mUserCityId = getIntent().getStringExtra(LikingNearbyFragment.INTENT_KEY_USER_CITY_ID);
        mGoodId = getIntent().getStringExtra(LikingNearbyFragment.INTENT_KEY_GOOD_ID);

        mBannerPagerAdapter = new BannerPagerAdapter(this);
        mImageViewPager.setAdapter(mBannerPagerAdapter);
        mImageViewPager.setAutoScrollTime(IMAGE_SLIDER_SWITCH_DURATION);
        mIconPageIndicator.setViewPager(mImageViewPager);
    }


    private void initView() {
        mImageViewPager = (InfiniteViewPager) findViewById(R.id.liking_home_head_viewpager);
        mIconPageIndicator = (IconPageIndicator) findViewById(R.id.liking_home_head_indicator);

        mDishesDetailsNameTextView = (TextView) findViewById(R.id.dishes_details_name);
        mSurplusNumberTextView = (TextView) findViewById(R.id.surplus_number);
        mDishesMoneyTextView = (TextView) findViewById(R.id.dishes_money);
        mDishesDescribeTextView = (TextView) findViewById(R.id.dishes_describe);
        mDishesTagsTextView = (TextView) findViewById(R.id.dishes_tags);
        mHowNumberBuyTextView = (TextView) findViewById(R.id.how_number_buy);
        mDishesKcalTextView = (TextView) findViewById(R.id.dishes_kcal);
        mDishesProteinTextView = (TextView) findViewById(R.id.dishes_protein);
        mCarbonAndWaterTextView = (TextView) findViewById(R.id.carbon_and_water);
        mDishesFatTextView = (TextView) findViewById(R.id.dishes_fat);
        mReduceImageBtn = (ImageView) findViewById(R.id.reduce_image);
        mAddImageBtn = (ImageView) findViewById(R.id.add_image);
        mFoodBuyNumberTextView = (TextView) findViewById(R.id.food_buy_number);
        mImmediatelyBuyBtn = (TextView) findViewById(R.id.immediately_buy_btn);
    }

    private void setViewOnClickListener() {
        mImmediatelyBuyBtn.setOnClickListener(this);
        mAddImageBtn.setOnClickListener(this);
        mReduceImageBtn.setOnClickListener(this);
    }

    private void requestBanner() {
        List<BannerResult.BannerData.Banner> banners = new ArrayList<>();
        BannerResult.BannerData.Banner banner = new BannerResult.BannerData.Banner();
        banner.setImgUrl("http://bizhi.33lc.com/uploadfile/2014/0911/20140911092615146.jpg");
        banner.setType("2");
        banners.add(banner);

        BannerResult.BannerData.Banner banner1 = new BannerResult.BannerData.Banner();
        banner1.setImgUrl("http://thumbs.dreamstime.com/z/%BD%A1%C9%ED-34080752.jpg");
        banner1.setType("2");
        banners.add(banner1);

        if (mBannerPagerAdapter != null) {
            mBannerPagerAdapter.setData(banners);
            mBannerPagerAdapter.notifyDataSetChanged();
            mIconPageIndicator.notifyDataSetChanged();
        }
        mImageViewPager.setCurrentItem(0);
        mImageViewPager.startAutoScroll();
    }

    @Override
    public void updateFoodDetailsView(FoodDetailsResult.FoodDetailsData foodDetailsData) {
        mDishesDetailsNameTextView.setText(foodDetailsData.getGoodsName());
        mSurplusNumberTextView.setText("还剩"+foodDetailsData.getLeftNum()+"份");
        mDishesMoneyTextView.setText("¥"+foodDetailsData.getPrice());
        mDishesDescribeTextView.setText(foodDetailsData.getGoodsDesc());
        List<String> tagList =foodDetailsData.getTags();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i=0;i<tagList.size();i++){
            stringBuffer.append("#"+tagList.get(0)+" ");
        }
        mDishesTagsTextView.setText(stringBuffer.toString());
        mHowNumberBuyTextView.setText(foodDetailsData.getAllEat()+"人购买过");
        mDishesKcalTextView.setText(foodDetailsData.getCalorie()+"");
        mDishesProteinTextView.setText(foodDetailsData.getProteide()+"");
        mCarbonAndWaterTextView.setText(foodDetailsData.getCarbohydrate()+"");
        mDishesFatTextView.setText(foodDetailsData.getAxunge()+"");
    }


    @Override
    public void onClick(View v) {
        if (v == mAddImageBtn) {

        } else if (v == mReduceImageBtn) {

        } else if (v == mImmediatelyBuyBtn) {

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mImageViewPager != null && mImageViewPager.getChildCount() != 0) {
            mImageViewPager.startAutoScroll();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mImageViewPager != null && mImageViewPager.getChildCount() != 0) {
            mImageViewPager.stopAutoScroll();
        }
    }
}
