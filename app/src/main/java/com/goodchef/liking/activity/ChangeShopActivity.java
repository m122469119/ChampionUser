package com.goodchef.liking.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.android.thirdparty.widget.pullrefresh.PullToRefreshBase;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.LikingNutrimealFragment;
import com.goodchef.liking.http.result.GymListResult;
import com.goodchef.liking.http.result.data.Food;
import com.goodchef.liking.mvp.presenter.ChangeShopPresenter;
import com.goodchef.liking.mvp.view.ChangeShopView;
import com.goodchef.liking.widgets.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/26 下午5:36
 */
public class ChangeShopActivity extends AppBarActivity implements ChangeShopView {
    public static final String INTENT_KEY_SHOP_OBJECT = "intent_key_shop_object";
    private PullToRefreshRecyclerView mRecyclerView;

    private ChangeShopPresenter mChangeShopPresenter;
    private String mUserCityId;
    private ArrayList<Food> confirmBuyList;
    private ChangeShopAdapter mChangeShopAdapter;
    private List<GymListResult.GymData.Shop> mShopList;
    private GymListResult.GymData.Shop myShop;//选中后的对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_shop);
        setTitle(getString(R.string.title_change_shop));
        initView();
        initData();
    }

    private void initView() {
        mRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.change_shop_recyclerView);
    }

    private void initData() {
        mUserCityId = getIntent().getStringExtra(LikingNutrimealFragment.INTENT_KEY_USER_CITY_ID);
        Bundle bundle = getIntent().getExtras();
        confirmBuyList = bundle.getParcelableArrayList(ShoppingCartActivity.INTENT_KEY_CONFIRM_BUY_LIST);
        myShop = (GymListResult.GymData.Shop) getIntent().getSerializableExtra(INTENT_KEY_SHOP_OBJECT);
        mChangeShopAdapter = new ChangeShopAdapter(this);
        mRecyclerView.setMode(PullToRefreshBase.Mode.DISABLED);
        sendGetListRequest();
    }

    private void sendGetListRequest() {
        if (confirmBuyList != null && confirmBuyList.size() > 0) {
            mChangeShopPresenter = new ChangeShopPresenter(this, this);
            String foodInfo = createDishesJson();
            mChangeShopPresenter.getShopList(mUserCityId, foodInfo);
        }
    }

    /**
     * 创建所有菜品的json字串
     */
    private String createDishesJson() {
        StringBuilder builder = new StringBuilder("{");
        for (Food food : confirmBuyList) {
            builder.append("\"").append(food.getGoodsId()).append("\":");
            builder.append(food.getSelectedOrderNum()).append(",");
        }
        builder.replace(builder.length() - 1, builder.length(), "}");
        return builder.toString();
    }

    @Override
    public void updateChangeShopView(GymListResult.GymData gymData) {
        mShopList = gymData.getGymList();
        if (mShopList != null && mShopList.size() > 0) {
            for (GymListResult.GymData.Shop shop : mShopList) {
                if (myShop != null && !StringUtils.isEmpty(myShop.getName()) && myShop.getName().equals(shop.getName())) {
                    shop.setSelect(true);
                } else {
                    shop.setSelect(false);
                }
            }
            mChangeShopAdapter.setData(mShopList);
            mChangeShopAdapter.setLayoutOnClickListener(listener);
            mRecyclerView.setAdapter(mChangeShopAdapter);
        }
    }

    /***
     * 点击切换回去
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.layout_change_shop);
            if (layout != null) {
                GymListResult.GymData.Shop object = (GymListResult.GymData.Shop) layout.getTag();
                if (object != null) {
                    for (GymListResult.GymData.Shop shop : mShopList) {
                        if (shop.getName().equals(object.getName())) {
                            shop.setSelect(true);
                        } else {
                            shop.setSelect(false);
                        }
                    }
                    mChangeShopAdapter.notifyDataSetChanged();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(INTENT_KEY_SHOP_OBJECT, object);
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        }
    };


    public class ChangeShopAdapter extends BaseRecycleViewAdapter<ChangeShopAdapter.ChangeShopViewHolder, GymListResult.GymData.Shop> {
        private static final int STOCK_LIMIT_NOT_SELL_OUT = 0;//没有售罄
        private static final int STOCK_LIMIT_SELL_OUT = 1;//售罄
        private Context mContext;
        private View.OnClickListener listener;

        public ChangeShopAdapter(Context context) {
            super(context);
            this.mContext = context;
        }

        public void setLayoutOnClickListener(View.OnClickListener listener) {
            this.listener = listener;
        }

        @Override
        protected ChangeShopViewHolder createViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_change_shop, parent, false);
            return new ChangeShopViewHolder(view);
        }

        public class ChangeShopViewHolder extends BaseRecycleViewHolder<GymListResult.GymData.Shop> {
            TextView mShopNameTextView;
            TextView mShopAddressTextView;
            TextView mPromptTextView;
            CheckBox mCheckBox;
            RelativeLayout mLayout;

            public ChangeShopViewHolder(View itemView) {
                super(itemView);
                mShopNameTextView = (TextView) itemView.findViewById(R.id.change_shop_name);
                mShopAddressTextView = (TextView) itemView.findViewById(R.id.change_shop_address);
                mPromptTextView = (TextView) itemView.findViewById(R.id.change_shop_prompt);
                mCheckBox = (CheckBox) itemView.findViewById(R.id.change_shop_checkBox);
                mLayout = (RelativeLayout) itemView.findViewById(R.id.layout_change_shop);
            }

            @Override
            public void bindViews(GymListResult.GymData.Shop object) {
                int stockLimit = object.getStockLimit();
                if (stockLimit == STOCK_LIMIT_NOT_SELL_OUT) {//没有售罄
                    mPromptTextView.setVisibility(View.GONE);
                    mCheckBox.setVisibility(View.VISIBLE);
                    boolean isSelect = object.isSelect();
                    if (isSelect) {
                        mCheckBox.setChecked(true);
                    } else {
                        mCheckBox.setChecked(false);
                    }
                    mShopNameTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_dark_back));
                    mShopAddressTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
                    mLayout.setEnabled(true);
                    mLayout.setOnClickListener(listener);
                    mLayout.setTag(object);
                } else if (stockLimit == STOCK_LIMIT_SELL_OUT) {//售罄
                    mPromptTextView.setVisibility(View.VISIBLE);
                    mCheckBox.setVisibility(View.GONE);
                    mShopNameTextView.setTextColor(ResourceUtils.getColor(R.color.change_shop_gray_back));
                    mShopAddressTextView.setTextColor(ResourceUtils.getColor(R.color.change_shop_gray_back));
                    mLayout.setEnabled(false);
                    mLayout.setOnClickListener(null);
                }
                mShopNameTextView.setText(object.getName());
                mShopAddressTextView.setText(object.getAddress());
            }
        }
    }
}
