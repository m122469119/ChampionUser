package com.goodchef.liking.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.ListUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.thirdparty.widget.pullrefresh.PullToRefreshBase;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.ShoppingCartAdapter;
import com.goodchef.liking.eventmessages.ClearCartMessage;
import com.goodchef.liking.eventmessages.DishesAliPayMessage;
import com.goodchef.liking.eventmessages.DishesPayFalse;
import com.goodchef.liking.eventmessages.DishesWechatPayMessage;
import com.goodchef.liking.eventmessages.FreePayMessage;
import com.goodchef.liking.fragment.LikingNutrimealFragment;
import com.goodchef.liking.http.result.data.Food;
import com.goodchef.liking.module.login.LoginActivity;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.widgets.PullToRefreshRecyclerView;

import java.util.ArrayList;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/21 下午8:15
 */
public class ShoppingCartActivity extends AppBarActivity implements View.OnClickListener, ShoppingCartAdapter.ShoppingDishChangedListener {
    public static final String INTENT_KEY_CONFIRM_BUY_LIST = "intent_key_confirm_buy_List";
    public static final String KEY_CLEAR_CART = "key_clear_cart";
    private PullToRefreshRecyclerView mRecyclerView;
    private ShoppingCartAdapter mShoppingCartAdapter;
    private TextView mTotalPriceTextView;
    private TextView mImmediatelyBuyBtn;
    private ArrayList<Food> buyList;
    private String mUserCityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        setTitle(getString(R.string.title_shopping_cart));
        setRightMenu();
        initView();
        initData();
        showHomeUpIcon(R.drawable.app_bar_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(LikingHomeActivity.INTENT_KEY_BUY_LIST, buyList);
                intent.putExtra(KEY_CLEAR_CART, false);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setRightMenu() {
        showRightMenu("清空购物车", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClearShoppingCartDialog();
            }
        });
    }

    private void initView() {
        mRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.shopping_card_RecyclerView);
        mTotalPriceTextView = (TextView) findViewById(R.id.food_money);
        mImmediatelyBuyBtn = (TextView) findViewById(R.id.immediately_buy_btn);

        mImmediatelyBuyBtn.setOnClickListener(this);
    }

    private void initData() {
        mUserCityId = getIntent().getStringExtra(LikingNutrimealFragment.INTENT_KEY_USER_CITY_ID);
        Bundle bundle = getIntent().getExtras();
        buyList = bundle.getParcelableArrayList(LikingHomeActivity.INTENT_KEY_BUY_LIST);
        mShoppingCartAdapter = new ShoppingCartAdapter(this);
        mShoppingCartAdapter.setData(buyList);
        setNumAndPrice();
        mRecyclerView.setAdapter(mShoppingCartAdapter);
        mRecyclerView.setMode(PullToRefreshBase.Mode.DISABLED);
    }


    @Override
    public void onClick(View v) {
        if (v == mImmediatelyBuyBtn) {
            if (Preference.isLogin()) {
                Intent intent = new Intent(this, DishesConfirmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(INTENT_KEY_CONFIRM_BUY_LIST, buyList);
                intent.putExtra(LikingNutrimealFragment.INTENT_KEY_USER_CITY_ID, mUserCityId);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onShoppingDishAdded(Food foodData) {
        int riceNum = foodData.getSelectedOrderNum();
        if (riceNum >= foodData.getRestStock()) {
            PopupUtils.showToast("单个最多只能购买" + foodData.getRestStock() + "份");
        }
        if (!buyList.contains(foodData)) {
            buyList.add(foodData);
        }
        setNumAndPrice();
        refreshChangeData();
    }


    @Override
    public void onShoppingDishRemove(Food foodData) {
        if (foodData.getSelectedOrderNum() == 0) {//当某个菜品数量减少为0时，去掉他在这个集合中的数量
            buyList.remove(foodData);
        }
        setNumAndPrice();
        refreshChangeData();
    }


    /**
     * 当从购物车回来时对数据刷新
     */
    private void refreshChangeData() {
        if (buyList != null && buyList.size() > 0) {
            for (Food mFood : buyList) {
                if (mFood.getGoodsId().equals(mFood.getGoodsId())) {
                    mFood.setSelectedOrderNum(mFood.getSelectedOrderNum());
                }
            }
        }
    }


    /**
     * 计算并显示选择的菜品份数和总价格
     */
    private void setNumAndPrice() {
        int num = 0;
        float dishPrice = 0;
        if (!ListUtils.isEmpty(buyList)) {
            for (Food data : buyList) {
                int n = data.getSelectedOrderNum();
                float p = Float.parseFloat(data.getPrice()) * n;
                num += n;
                dishPrice += p;
            }
        }
        mTotalPriceTextView.setText("¥ " + dishPrice);
    }


    //清空购物车对话框
    private void showClearShoppingCartDialog() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(R.string.dialog_message_clear_shopping_cart);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearShoppingCart();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 清空购物车
     */
    private void clearShoppingCart() {
//        for (Food data : buyList) {
//            data.setSelectedOrderNum(0);
//        }
        buyList.clear();
        postEvent(new ClearCartMessage());
        setNumAndPrice();
        mShoppingCartAdapter.notifyDataSetChanged();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(LikingHomeActivity.INTENT_KEY_BUY_LIST, buyList);
        intent.putExtra(KEY_CLEAR_CART, true);//清空购物车
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(LikingHomeActivity.INTENT_KEY_BUY_LIST, buyList);
            intent.putExtra(KEY_CLEAR_CART, false);
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(DishesWechatPayMessage wechatMessage) {
        clearShoppingCart();
    }

    public void onEvent(DishesAliPayMessage dishesAliPayMessage) {
        clearShoppingCart();
    }

    public void onEvent(DishesPayFalse dishesWechatPayFalse) {
        clearShoppingCart();
    }

    public void onEvent(FreePayMessage message){
        clearShoppingCart();
    }

}
