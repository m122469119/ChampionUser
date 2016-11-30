package com.goodchef.liking.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PagerRequestCallback;
import com.aaron.android.framework.base.widget.refresh.PullMode;
import com.aaron.android.framework.utils.DisplayUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.LikingNearbyAdapter;
import com.goodchef.liking.eventmessages.ClearCartMessage;
import com.goodchef.liking.eventmessages.JumpToDishesDetailsMessage;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.eventmessages.RefreshChangeDataMessage;
import com.goodchef.liking.eventmessages.UserCityIdMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.FoodListResult;
import com.goodchef.liking.http.result.data.Food;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 *          营养餐Fragment
 */
public class LikingNutrimealFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment {
    public static final String INTENT_KEY_USER_CITY_ID = "intent_key_user_city_id";
    private static final int SELECT_MAX = 5;//设置单个菜品最大购买数量
    private LikingNearbyAdapter mAdapter;
    private String mCityId = "310100";
    private String mLongitude = "0";
    private String mLatitude = "0";
    private String mUserCityId;
    private ArrayList<Food> buyLit;
    private List<Food> mFoodList;

    public static LikingNutrimealFragment newInstance() {
        LikingNutrimealFragment fragment = new LikingNutrimealFragment();
        return fragment;
    }

    @Override
    protected void requestData(int page) {
        sendRequest(page);
    }

    @Override
    protected void initViews() {
        setNoDataView();
        setRecyclerViewPadding(0, 0, 0, DisplayUtils.dp2px(10));
        mAdapter = new LikingNearbyAdapter(getActivity());
        setRecyclerAdapter(mAdapter);
        initData();
    }

    private void setNoDataView() {
        View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
        ImageView noDataImageView = (ImageView) noDataView.findViewById(R.id.imageview_no_data);
        TextView noDataText = (TextView) noDataView.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) noDataView.findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.icon_no_data);
        noDataText.setText(R.string.no_data);
        refreshView.setText(R.string.refresh_btn_text);
        refreshView.setOnClickListener(refreshOnClickListener);
        getStateView().setNodataView(noDataView);
    }

    /***
     * 刷新事件
     */
    private View.OnClickListener refreshOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadHomePage();
        }
    };

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(MainAddressChanged mainAddressChanged) {
        mLatitude = mainAddressChanged.getLatitude();
        mLongitude = mainAddressChanged.getLongitude();
        mCityId = mainAddressChanged.getCityId();
    }

    private void sendRequest(int page) {
        LiKingApi.getFoodList(mLongitude, mLatitude, mCityId, page, new PagerRequestCallback<FoodListResult>(LikingNutrimealFragment.this) {
            @Override
            public void onSuccess(FoodListResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(getActivity(), result)) {
                    FoodListResult.FoodData foodData = result.getFoodData();
                    if (foodData != null) {
                        mUserCityId = foodData.getUserCityId();
                        postEvent(new UserCityIdMessage(mUserCityId));
                        mFoodList = foodData.getFoodList();
                        if (mFoodList != null && mFoodList.size() > 0) {
                            for (Food food : mFoodList) {
                                food.setRestStock(SELECT_MAX);
                                food.setSelectedOrderNum(0);
                            }
                            refreshChangeData(false);
                        }
                        updateListView(mFoodList);
                    }

                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }

    private void initData() {
        setPullMode(PullMode.PULL_BOTH);
        mAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.dishes_name);
                if (textView != null) {
                    Food foodData = (Food) textView.getTag();
                    if (foodData != null) {
                        postEvent(new JumpToDishesDetailsMessage(foodData, mUserCityId));
                    }
                }

            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
    }


    public void onEvent(RefreshChangeDataMessage refreshChangeDataMessage) {
        buyLit = refreshChangeDataMessage.getBuyList();
        boolean isClearCart = refreshChangeDataMessage.isClearCart();
        refreshChangeData(isClearCart);
    }

    /**
     * 当从购物车回来时对数据刷新
     */
    private void refreshChangeData(boolean isClearCart) {
        if (isClearCart) {//如果是清空购物车
            buyLit.clear();
            for (Food food : mFoodList) {
                food.setSelectedOrderNum(0);
            }
        } else {//没有清空购物车
            if (mFoodList != null && mFoodList.size() > 0 && buyLit != null && buyLit.size() > 0) {
                for (Food mFood : mFoodList) {
                    mFood.setSelectedOrderNum(0);
                    for (Food buyFood : buyLit) {
                        if (mFood.getGoodsId().equals(buyFood.getGoodsId())) {
                            mFood.setSelectedOrderNum(buyFood.getSelectedOrderNum());
                        }
                    }
                }
            } else if (mFoodList != null && mFoodList.size() > 0) {
                for (Food food : mFoodList) {
                    food.setSelectedOrderNum(0);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    public void onEvent(ClearCartMessage message) {
        buyLit.clear();
        for (Food food : mFoodList) {
            food.setSelectedOrderNum(0);
        }
        mAdapter.notifyDataSetChanged();
    }
}
