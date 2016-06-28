package com.goodchef.liking.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.widget.refresh.NetworkPagerLoaderRecyclerViewFragment;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.MyDishesOrderAdapter;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.DishesOrderListResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/28 下午2:52
 */
public class MyDishesOrderFragment extends NetworkPagerLoaderRecyclerViewFragment {

    private MyDishesOrderAdapter mMyDishesOrderAdapter;

    @Override
    protected void requestData(int page) {
        sendListRequest(page);
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

        mMyDishesOrderAdapter = new MyDishesOrderAdapter(getActivity());
        setRecyclerAdapter(mMyDishesOrderAdapter);
        getPullToRefreshRecyclerView().setDividerDrawable(null);
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


    private void sendListRequest(int page) {
        LiKingApi.getDishesOrderList(Preference.getToken(), page, new PagerRequestCallback<DishesOrderListResult>(this) {
            @Override
            public void onSuccess(DishesOrderListResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(getActivity(), result)) {
                    mMyDishesOrderAdapter.setData(result.getData().getOrderList());
                    mMyDishesOrderAdapter.notifyDataSetChanged();
                    mMyDishesOrderAdapter.setClickListener(mClickListener);
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }


    /***
     * 适配器中各种事件
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.go_pay:
                    break;
                case R.id.cancel_order:
                    break;
                case R.id.confirm_get_dishes_btn:
                    break;
            }
        }
    };

}
