package com.goodchef.liking.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PullMode;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.BuyCardConfirmActivity;
import com.goodchef.liking.activity.CouponsActivity;
import com.goodchef.liking.activity.CouponsDetailsActivity;
import com.goodchef.liking.activity.ShoppingCartActivity;
import com.goodchef.liking.adapter.CouponsAdapter;
import com.goodchef.liking.eventmessages.ExchangeCouponsMessage;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.data.Food;
import com.goodchef.liking.mvp.presenter.CouponPresenter;
import com.goodchef.liking.mvp.view.CouponView;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 下午2:28
 */
public class CouponsFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment implements CouponView {

    private CouponPresenter mCouponPresenter;

    private String intentType;
    private String courseId = "";
    private String selectTimes;
    private ArrayList<Food> confirmBuyList = new ArrayList<>();
    private String cardId;
    private String type;
    private String couponId;
    private String scheduleId;
    private String gymId;

    private CouponsAdapter mCouponsAdapter;

    public static CouponsFragment newInstance(Bundle args) {
        CouponsFragment fragment = new CouponsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CouponsFragment() {
    }

    @Override
    protected void requestData(int page) {
        sendRequest(page);
    }


    @Override
    protected void initViews() {
        setNoDataView();
        getIntentData();
        initRecycleView();
    }

    /**
     * 设置没有优惠券数据
     */
    private void setNoDataView() {
        View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
        ImageView noDataImageView = (ImageView) noDataView.findViewById(R.id.imageview_no_data);
        TextView noDataText = (TextView) noDataView.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) noDataView.findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.icon_no_coupon);
        noDataText.setText(R.string.no_coupons_data);
        refreshView.setVisibility(View.INVISIBLE);
        getStateView().setNodataView(noDataView);
    }

    /**
     * 获取bundle对象传递的数据
     */
    private void getIntentData() {
        courseId = getArguments().getString(CouponsActivity.KEY_COURSE_ID);
        selectTimes = getArguments().getString(CouponsActivity.KEY_SELECT_TIMES);
        intentType = getArguments().getString(CouponsActivity.TYPE_MY_COUPONS);
        confirmBuyList = getArguments().getParcelableArrayList(ShoppingCartActivity.INTENT_KEY_CONFIRM_BUY_LIST);
        cardId = getArguments().getString(BuyCardConfirmActivity.KEY_CARD_ID);
        type = getArguments().getString(LikingBuyCardFragment.KEY_BUY_TYPE);
        couponId = getArguments().getString(CouponsActivity.KEY_COUPON_ID);
        scheduleId = getArguments().getString(CouponsActivity.KEY_SCHEDULE_ID);
        gymId = getArguments().getString(LikingLessonFragment.KEY_GYM_ID);
//        if (intentType.equals(CouponsActivity.TYPE_MY_COUPONS)) {
//            setPullMode(PullMode.PULL_BOTH);
//        } else {
//            setPullMode(PullMode.PULL_UP);
//        }
        setPullMode(PullMode.PULL_BOTH);
    }

    private String createDishesJson() {
        if (confirmBuyList != null && confirmBuyList.size() > 0) {
            StringBuilder builder = new StringBuilder("{");
            for (Food food : confirmBuyList) {
                builder.append("\"").append(food.getGoodsId()).append("\":");
                builder.append(food.getSelectedOrderNum()).append(",");
            }
            builder.replace(builder.length() - 1, builder.length(), "}");
            return builder.toString();
        } else {
            return null;
        }
    }


    private void initRecycleView() {
        mCouponsAdapter = new CouponsAdapter(getActivity());
        setRecyclerAdapter(mCouponsAdapter);
        if (intentType.equals(CouponsActivity.TYPE_MY_COUPONS)) {
            mCouponsAdapter.setOnItemClickListener(new CouponsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, CouponsResult.CouponData.Coupon coupon) {
                    //在购票界面跳转
                    Intent intent = new Intent(getActivity(), CouponsDetailsActivity.class);
                    getActivity().startActivity(intent);
                }
            });
        } else {
            mCouponsAdapter.setOnItemClickListener(new CouponsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, CouponsResult.CouponData.Coupon coupon) {
                    //在购票界面跳转
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(CouponsActivity.INTENT_KEY_COUPONS_DATA, coupon);
                    intent.putExtras(bundle);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            });
        }
    }


    private void sendRequest(int page) {
        mCouponPresenter = new CouponPresenter(getActivity(), this);
        if (intentType.equals(CouponsActivity.TYPE_MY_COUPONS)) {
            mCouponPresenter.getCoupons(null, null, null, null, null, null, page, gymId, CouponsFragment.this);
        } else {
            mCouponPresenter.getCoupons(courseId, selectTimes, createDishesJson(), cardId, type, scheduleId, page, gymId, CouponsFragment.this);
        }
    }


    @Override
    public void updateCouponData(CouponsResult.CouponData couponData) {
        List<CouponsResult.CouponData.Coupon> list = couponData.getCouponList();
        if (list != null) {
            if (list.size() > 0) {
                if (!StringUtils.isEmpty(couponId)) {
                    for (CouponsResult.CouponData.Coupon coupon : list) {
                        if (coupon.getCouponCode().equals(couponId)) {
                            coupon.setSelect(true);
                        } else {
                            coupon.setSelect(false);
                        }
                    }
                }
            }
            updateListView(list);
        }
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(ExchangeCouponsMessage message) {
        loadHomePage();
    }

    public void onEvent(LoginOutFialureMessage message) {
        getActivity().finish();
    }


}
