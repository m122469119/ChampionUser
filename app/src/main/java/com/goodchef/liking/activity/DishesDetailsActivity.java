package com.goodchef.liking.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BannerPagerAdapter;
import com.goodchef.liking.eventmessages.ClearCartMessage;
import com.goodchef.liking.fragment.LikingNearbyFragment;
import com.goodchef.liking.http.result.BannerResult;
import com.goodchef.liking.http.result.FoodDetailsResult;
import com.goodchef.liking.http.result.data.Food;
import com.goodchef.liking.mvp.presenter.FoodDetailsPresenter;
import com.goodchef.liking.mvp.view.FoodDetailsView;
import com.goodchef.liking.widgets.autoviewpager.InfiniteViewPager;
import com.goodchef.liking.widgets.autoviewpager.indicator.IconPageIndicator;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/26 下午3:00
 */
public class DishesDetailsActivity extends AppBarActivity implements FoodDetailsView, View.OnClickListener {
    private static final int INTENT_REQUEST_CODE_DEISH_DETAILS_CART = 114;
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

    private RelativeLayout mAddReduceLayout;
    private ImageView mReduceImageBtn;//减少
    private ImageView mAddImageBtn;//添加
    private TextView mFoodBuyNumberTextView;
    private TextView mImmediatelyBuyBtn;//立即购买


    public static final int IMAGE_SLIDER_SWITCH_DURATION = 4000;
    private InfiniteViewPager mImageViewPager;
    private IconPageIndicator mIconPageIndicator;
    private BannerPagerAdapter mBannerPagerAdapter;

    private String mUserCityId;
    private Food mFood;
    private ArrayList<Food> buyList;
    private FoodDetailsPresenter mFoodDetailsPresenter;
    private int mSelectNum;
    private LikingStateView mStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_details);
        setTitle("商品详情");
        initView();
        initData();
        sendFoodDetailsRequest();
        setViewOnClickListener();
        showHomeUpIcon(R.drawable.app_bar_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(LikingHomeActivity.INTENT_KEY_BUY_LIST, buyList);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }


    private void initData() {
        mUserCityId = getIntent().getStringExtra(LikingNearbyFragment.INTENT_KEY_USER_CITY_ID);
        mFood = (Food) getIntent().getSerializableExtra(LikingHomeActivity.INTENT_KEY_FOOD_OBJECT);
        Bundle bundle = getIntent().getExtras();
        buyList = bundle.getParcelableArrayList(LikingHomeActivity.INTENT_KEY_BUY_LIST);
        mSelectNum = mFood.getSelectedOrderNum();
        mFoodBuyNumberTextView.setText(String.valueOf(mSelectNum));
        initBanner();
    }


    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.dishes_details_state_view);
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

        mAddReduceLayout = (RelativeLayout) findViewById(R.id.layout_add_reduce);
        mReduceImageBtn = (ImageView) findViewById(R.id.reduce_image);
        mAddImageBtn = (ImageView) findViewById(R.id.add_image);
        mFoodBuyNumberTextView = (TextView) findViewById(R.id.food_buy_number);
        mImmediatelyBuyBtn = (TextView) findViewById(R.id.dishes_immediately_buy_btn);

        mStateView.setState(StateView.State.LOADING);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                sendFoodDetailsRequest();
            }
        });
    }

    private void setViewOnClickListener() {
        mImmediatelyBuyBtn.setOnClickListener(this);
        mAddImageBtn.setOnClickListener(this);
        mReduceImageBtn.setOnClickListener(this);
    }

    private void initBanner() {
        mBannerPagerAdapter = new BannerPagerAdapter(this);
        mImageViewPager.setAdapter(mBannerPagerAdapter);
        mImageViewPager.setAutoScrollTime(IMAGE_SLIDER_SWITCH_DURATION);
        mIconPageIndicator.setViewPager(mImageViewPager);
    }

    //发送详情请求
    private void sendFoodDetailsRequest() {
        mFoodDetailsPresenter = new FoodDetailsPresenter(this, this);
        mFoodDetailsPresenter.getFoodDetails(mUserCityId, mFood.getGoodsId());
    }

    @Override
    public void updateFoodDetailsView(FoodDetailsResult.FoodDetailsData foodDetailsData) {
        if (foodDetailsData != null) {
            mStateView.setState(StateView.State.SUCCESS);
            mDishesDetailsNameTextView.setText(foodDetailsData.getGoodsName());
            mDishesMoneyTextView.setText("¥" + foodDetailsData.getPrice());
            mDishesDescribeTextView.setText(foodDetailsData.getGoodsDesc());
            List<String> tagList = foodDetailsData.getTags();
            if (tagList != null && tagList.size() > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < tagList.size(); i++) {
                    stringBuffer.append("#" + tagList.get(0) + " ");
                }
                mDishesTagsTextView.setText(stringBuffer.toString());
            }
            mHowNumberBuyTextView.setText(foodDetailsData.getAllEat() + "人购买过");
            mDishesKcalTextView.setText(foodDetailsData.getCalorie() + "");
            mDishesProteinTextView.setText(foodDetailsData.getProteide() + "");
            mCarbonAndWaterTextView.setText(foodDetailsData.getCarbohydrate() + "");
            mDishesFatTextView.setText(foodDetailsData.getAxunge() + "");

            int leftNmu = foodDetailsData.getLeftNum();
            setDishesLefNumView(leftNmu);

            List<String> imgList = foodDetailsData.getImgs();
            if (imgList != null && imgList.size() > 0) {
                setBannerData(imgList);
            }
        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }
    }

    /**
     * 设置库存状态
     *
     * @param leftNmu 库存
     */
    private void setDishesLefNumView(int leftNmu) {
        if (leftNmu > 0) {
            mSurplusNumberTextView.setText("还剩" + leftNmu + "份");
            mSurplusNumberTextView.setTextColor(ResourceUtils.getColor(R.color.add_minus_dishes_text));
            mAddReduceLayout.setVisibility(View.VISIBLE);
            mImmediatelyBuyBtn.setBackgroundResource(R.color.add_minus_dishes_text);
            mImmediatelyBuyBtn.setTextColor(ResourceUtils.getColor(R.color.white));
            mImmediatelyBuyBtn.setEnabled(true);
            mImmediatelyBuyBtn.setText(R.string.immediately_buy_btn);
        } else {
            mSurplusNumberTextView.setText(R.string.sold_out);
            mSurplusNumberTextView.setTextColor(ResourceUtils.getColor(R.color.bg_gray_text));
            mAddReduceLayout.setVisibility(View.GONE);
            mImmediatelyBuyBtn.setBackgroundResource(R.color.dishes_buy_btn_gray_background);
            mImmediatelyBuyBtn.setTextColor(ResourceUtils.getColor(R.color.dishes_buy_gray_text));
            mImmediatelyBuyBtn.setText(R.string.sold_out);
            mImmediatelyBuyBtn.setEnabled(false);
        }
    }

    private void setBannerData(List<String> imgList) {
        List<BannerResult.BannerData.Banner> banners = new ArrayList<>();
        for (int i = 0; i < imgList.size(); i++) {
            BannerResult.BannerData.Banner banner = new BannerResult.BannerData.Banner();
            banner.setImgUrl(imgList.get(i));
            banner.setType("2");
            banners.add(banner);
        }
        if (mBannerPagerAdapter != null) {
            mBannerPagerAdapter.setData(banners);
            mBannerPagerAdapter.notifyDataSetChanged();
            mIconPageIndicator.notifyDataSetChanged();
        }
        mImageViewPager.setCurrentItem(0);
        mImageViewPager.startAutoScroll();
    }


    @Override
    public void onClick(View v) {
        if (v == mAddImageBtn) {
            if (mSelectNum == mFood.getRestStock()){
                PopupUtils.showToast("单个最多只能购买" + mFood.getRestStock() + "份");
                return;
            }
            mFood.setSelectedOrderNum(++mSelectNum);
            mFoodBuyNumberTextView.setText(String.valueOf(mFood.getSelectedOrderNum()));
            setAddBuyList();
        } else if (v == mReduceImageBtn) {
            --mSelectNum;
            if (mSelectNum < 0) {
                mSelectNum = 0;
            }
            mFood.setSelectedOrderNum(mSelectNum);
            mFoodBuyNumberTextView.setText(String.valueOf(mFood.getSelectedOrderNum()));
            setReduceBuyList();
        } else if (v == mImmediatelyBuyBtn) {
            int selectNum = mFood.getSelectedOrderNum();
            if (selectNum > 0) {
                if (buyList != null && buyList.size() > 0) {
                    Intent intent = new Intent(this, ShoppingCartActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(LikingHomeActivity.INTENT_KEY_BUY_LIST, buyList);
                    intent.putExtra(LikingNearbyFragment.INTENT_KEY_USER_CITY_ID, mUserCityId);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, INTENT_REQUEST_CODE_DEISH_DETAILS_CART);
                }
            } else {
                PopupUtils.showToast("您还没有添加任何营养餐");
            }
        }
    }

    /**
     * 添加数据
     */
    private void setAddBuyList() {
        if (buyList != null && buyList.size() > 0) {
            boolean isBuyListExits = false;
            for (int i = 0; i < buyList.size(); i++) {
                if (buyList.get(i).getGoodsId().equals(mFood.getGoodsId())) {
                    buyList.get(i).setSelectedOrderNum(mSelectNum);
                    isBuyListExits = true;
                    break;
                }
            }
            if (!isBuyListExits) {
                buyList.add(mFood);
            }

        } else {
            buyList.add(mFood);
        }
    }

    /**
     * 减少数据
     */
    private void setReduceBuyList() {
        if (buyList != null && buyList.size() > 0) {
            for (int i = 0; i < buyList.size(); i++) {
                if (buyList.get(i).getGoodsId().equals(mFood.getGoodsId())) {
                    if (mFood.getSelectedOrderNum() == 0) {//当某个菜品数量减少为0时，去掉他在这个集合中的数量
                        buyList.remove(buyList.get(i));
                    }else {
                        buyList.get(i).setSelectedOrderNum(mSelectNum);
                    }
                }
            }
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(LikingHomeActivity.INTENT_KEY_BUY_LIST, buyList);
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_REQUEST_CODE_DEISH_DETAILS_CART) {//从购物车回来时，带回购物车数据，从新计算购物车数量
                Bundle bundle = data.getExtras();
                buyList = bundle.getParcelableArrayList(LikingHomeActivity.INTENT_KEY_BUY_LIST);
                refreshChangeData();
            }
        }
    }


    private void refreshChangeData() {
        if (buyList != null && buyList.size() > 0) {
            for (Food food : buyList) {
                if (food.getGoodsId().equals(mFood.getGoodsId())) {
                    mSelectNum = food.getSelectedOrderNum();
                    food.setSelectedOrderNum(mSelectNum);
                    mFood.setSelectedOrderNum(mSelectNum);
                    mFoodBuyNumberTextView.setText(String.valueOf(food.getSelectedOrderNum()));
                }
            }
        }else {
            mFood.setSelectedOrderNum(0);
            mFoodBuyNumberTextView.setText("0");
        }
    }

    @Override
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(ClearCartMessage message){
        buyList.clear();
        mFood.setSelectedOrderNum(0);
        mFoodBuyNumberTextView.setText("0");
    }
}
