package com.goodchef.liking.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkPagerLoaderRecyclerViewFragment;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.DishesDetailsActivity;
import com.goodchef.liking.adapter.LikingNearbyAdapter;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.FoodListResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;

import java.util.List;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingNearbyFragment extends NetworkPagerLoaderRecyclerViewFragment {
    public static final String INTENT_KEY_USER_CITY_ID = "intent_key_user_city_id";
    public static final String INTENT_KEY_GOOD_ID = "intent_key_good_id";
    private LikingNearbyAdapter mAdapter;
    private String mCityId = "310100";
    private double mLongitude = 0;
    private double mLatitude = 0;
    private String mUserCityId;

    @Override
    protected void requestData(int page) {
        sendRequest(page);
    }

    @Override
    protected void initViews() {
        initData();
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(MainAddressChanged mainAddressChanged) {
        mLatitude = mainAddressChanged.getLatitude();
        mLongitude = mainAddressChanged.getLongitude();
    }

    private void sendRequest(int page) {
        LiKingApi.getFoodList(mLongitude, mLatitude, mCityId, page, new PagerRequestCallback<FoodListResult>(LikingNearbyFragment.this) {
            @Override
            public void onSuccess(FoodListResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(getActivity(), result)) {
                    FoodListResult.FoodData foodData = result.getFoodData();
                    if (foodData != null) {
                        mUserCityId = foodData.getUserCityId();
                        List<FoodListResult.FoodData.Food> list = foodData.getFoodList();
                        if (list != null && list.size() > 0) {
                            updateListView(list);
                        }
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
        setPullType(PullMode.PULL_BOTH);
        mAdapter = new LikingNearbyAdapter(getActivity());
        setRecyclerAdapter(mAdapter);
        mAdapter.setAddListener(addListener);
        mAdapter.setReduceListener(reduceListener);
        mAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.dishes_name);
                if (textView != null) {
                    FoodListResult.FoodData.Food foodData = (FoodListResult.FoodData.Food) textView.getTag();
                    if (foodData != null) {
                        Intent intent = new Intent(getActivity(), DishesDetailsActivity.class);
                        intent.putExtra(INTENT_KEY_USER_CITY_ID, mUserCityId);
                        intent.putExtra(INTENT_KEY_GOOD_ID, foodData.getGoodsId());
                        startActivity(intent);
                    }
                }

            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
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
}
