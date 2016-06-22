package com.goodchef.liking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.thirdparty.widget.pullrefresh.PullToRefreshBase;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.ShoppingCartAdapter;
import com.goodchef.liking.http.result.FoodListResult;
import com.goodchef.liking.widgets.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/21 下午8:15
 */
public class ShoppingCartActivity extends AppBarActivity implements View.OnClickListener{
    private PullToRefreshRecyclerView mRecyclerView;
    private ShoppingCartAdapter mShoppingCartAdapter;
    private TextView mMoneyTextView;
    private TextView mImmediatelyBuyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        setTitle("购物车");
        setRightMenu();
        initView();
        initData();
    }

    private void setRightMenu() {
        showRightMenu("清空购物车", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initView() {
        mRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.shopping_card_RecyclerView);
        mMoneyTextView = (TextView) findViewById(R.id.food_money);
        mImmediatelyBuyBtn = (TextView) findViewById(R.id.immediately_buy_btn);

        mImmediatelyBuyBtn.setOnClickListener(this);
    }

    private void initData() {
        List<FoodListResult.FoodData.Food> list = new ArrayList<>();
        FoodListResult.FoodData.Food food = new FoodListResult.FoodData.Food();
        food.setGoodsName("猜猜");
        food.setPrice("30");
        food.setAllEat("33");
        food.setLeftNum(55);
        food.setCoverImg("http://image.tianjimedia.com/uploadImages/2015/129/56/J63MI042Z4P8.jpg");
        list.add(food);

        mShoppingCartAdapter = new ShoppingCartAdapter(this);
        mShoppingCartAdapter.setData(list);
        mRecyclerView.setAdapter(mShoppingCartAdapter);
        mShoppingCartAdapter.setAddListener(addListener);
        mShoppingCartAdapter.setReduceListener(reduceListener);
        mRecyclerView.setMode(PullToRefreshBase.Mode.DISABLED);
    }


    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PopupUtils.showToast("佳佳");
        }
    };

    private View.OnClickListener reduceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PopupUtils.showToast("减减");
        }
    };

    @Override
    public void onClick(View v) {
        if (v == mImmediatelyBuyBtn){

        }
    }
}
