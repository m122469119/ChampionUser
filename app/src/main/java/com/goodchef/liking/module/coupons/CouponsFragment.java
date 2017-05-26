package com.goodchef.liking.module.coupons;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PullMode;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.CouponsAdapter;
import com.goodchef.liking.adapter.CouponsPersonAdapter;
import com.goodchef.liking.eventmessages.ExchangeCouponsMessage;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.data.remote.retrofit.result.CouponsPersonResult;
import com.goodchef.liking.data.remote.retrofit.result.CouponsResult;
import com.goodchef.liking.module.card.buy.LikingBuyCardFragment;
import com.goodchef.liking.module.card.buy.confirm.BuyCardConfirmActivity;
import com.goodchef.liking.module.coupons.details.CouponsDetailsActivity;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 下午2:28
 */
public class CouponsFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment implements CouponContract.CouponView {

    private CouponContract.CouponPresenter mCouponPresenter;

    private String intentType;
    private String courseId = "";
    private String selectTimes;
    private String cardId;
    private String type;
    private String couponId;
    private String scheduleId;
    private String gymId;


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
        cardId = getArguments().getString(BuyCardConfirmActivity.KEY_CARD_ID);
        type = getArguments().getString(LikingBuyCardFragment.KEY_BUY_TYPE);
        couponId = getArguments().getString(CouponsActivity.KEY_COUPON_ID);
        scheduleId = getArguments().getString(CouponsActivity.KEY_SCHEDULE_ID);
        gymId = getArguments().getString(LikingLessonFragment.KEY_GYM_ID);
        setPullMode(PullMode.PULL_BOTH);
    }
//
//    private String createDishesJson() {
//        if (confirmBuyList != null && confirmBuyList.size() > 0) {
//            StringBuilder builder = new StringBuilder("{");
//            for (Food food : confirmBuyList) {
//                builder.append("\"").append(food.getGoodsId()).append("\":");
//                builder.append(food.getSelectedOrderNum()).append(",");
//            }
//            builder.replace(builder.length() - 1, builder.length(), "}");
//            return builder.toString();
//        } else {
//            return null;
//        }
//    }


    private void initRecycleView() {
        setRecyclerViewPadding(DisplayUtils.dp2px(10), 0, DisplayUtils.dp2px(10), 0);
        if (intentType.equals(CouponsActivity.TYPE_MY_COUPONS)) {
            CouponsPersonAdapter couponsPersonAdapter = new CouponsPersonAdapter(getActivity());
            setRecyclerAdapter(couponsPersonAdapter);
            couponsPersonAdapter.setOnItemClickListener(new CouponsPersonAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, CouponsPersonResult.DataBean.CouponListBean coupon) {
                    //在购票界面跳转
                    Intent intent = new Intent(getActivity(), CouponsDetailsActivity.class);
                    intent.setAction(CouponsDetailsActivity.ACTION_SHOW_DETAILS);
                    intent.putExtra(CouponsDetailsActivity.COUPONS, coupon);
                    getActivity().startActivity(intent);
                }
            });
        } else {
            CouponsAdapter mCouponsAdapter = new CouponsAdapter(getActivity());
            setRecyclerAdapter(mCouponsAdapter);
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
        if (mCouponPresenter == null) {
            mCouponPresenter = new CouponContract.CouponPresenter(getActivity(), this);
        }
        if (intentType.equals(CouponsActivity.TYPE_MY_COUPONS)) {
            mCouponPresenter.getMyCoupons(page);
        } else {
            mCouponPresenter.getCoupons(courseId, selectTimes, null, cardId, type, scheduleId, page, gymId);
        }
    }


    @Override
    public void updateCouponData(CouponsResult.CouponData couponData) {
        List<CouponsResult.CouponData.Coupon> list = couponData.getCoupon_list();
        if (list != null) {
            if (list.size() > 0) {
                if (!StringUtils.isEmpty(couponId)) {
                    for (CouponsResult.CouponData.Coupon coupon : list) {
                        if (coupon.getCoupon_code().equals(couponId)) {
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
    public void updateExchangeCode() {

    }

    @Override
    public void updateMyCouponData(CouponsPersonResult.DataBean dataBean) {
        updateListView(dataBean.getCouponList());
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
