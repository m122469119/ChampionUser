package com.goodchef.liking.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkPagerLoaderRecyclerViewFragment;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.LikingNearbyAdapter;
import com.goodchef.liking.eventmessages.JumpToDishesDetailsMessage;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.eventmessages.UserCityIdMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.FoodListResult;
import com.goodchef.liking.http.result.data.Food;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;

import java.util.List;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingNearbyFragment extends NetworkPagerLoaderRecyclerViewFragment{
    public static final String INTENT_KEY_USER_CITY_ID = "intent_key_user_city_id";
    public static final String INTENT_KEY_GOOD_ID = "intent_key_good_id";
    private static final int SELECT_MAX = 5;//设置单个菜品最大购买数量
    private LikingNearbyAdapter mAdapter;
    private String mCityId = "310100";
    private double mLongitude = 0;
    private double mLatitude = 0;
    private String mUserCityId;

    public static LikingNearbyFragment newInstance() {
        LikingNearbyFragment fragment = new LikingNearbyFragment();
        return fragment;
    }

    @Override
    protected void requestData(int page) {
        sendRequest(page);
    }

    @Override
    protected void initViews() {
        View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
        ImageView noDataImageView = (ImageView) noDataView.findViewById(R.id.imageview_no_data);
        TextView noDataText = (TextView) noDataView.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) noDataView.findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.no_order);
        noDataText.setText("暂无数据");
        refreshView.setText(R.string.refresh_btn_text);
        refreshView.setOnClickListener(refreshOnClickListener);
        getStateView().setNodataView(noDataView);

        mAdapter = new LikingNearbyAdapter(getActivity());
        setRecyclerAdapter(mAdapter);
        initData();
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
                        postEvent(new UserCityIdMessage(mUserCityId));
                        List<Food> list = foodData.getFoodList();
                        if (list != null && list.size() > 0) {
                            for (Food food : list) {
                                food.setRestStock(SELECT_MAX);
                                food.setSelectedOrderNum(0);
                            }
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
        mAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.dishes_name);
                if (textView != null) {
                    Food foodData = (Food) textView.getTag();
                    if (foodData != null) {
                        postEvent(new JumpToDishesDetailsMessage(foodData,mUserCityId));
                    }
                }

            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
    }



}
