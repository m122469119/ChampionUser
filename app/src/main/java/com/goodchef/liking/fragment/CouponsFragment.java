package com.goodchef.liking.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.base.widget.refresh.NetworkPagerLoaderRecyclerViewFragment;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.CouponsActivity;
import com.goodchef.liking.activity.ShoppingCartActivity;
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
public class CouponsFragment extends NetworkPagerLoaderRecyclerViewFragment implements CouponView {
    private static final String COUPON_TYPE_YINGYANGCANG = "1";//1营养餐
    private static final String COUPON_TYPE_PRIVATE_COURSES = "2";//2 私教课
    private static final String COUPON_TYPE_BUY_CARD = "3";// 3 购卡
    private static final String COUPON_STATUS_NOT_USED = "0";//0未使用
    private static final String COUPON_STATUS_USED = "1";//1已使用
    private static final String COUPON_STATUS_OVERDUE = "2";// 2已过期
    private static final String COUPON_STATUS_NO_SUBJECT = "3";//不符合该项目

    private CouponPresenter mCouponPresenter;

    private String intentType;
    private String courseId = "";
    private ArrayList<Food> confirmBuyList = new ArrayList<>();

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
        courseId = getArguments().getString(CouponsActivity.KEY_COURSE_ID);
        intentType = getArguments().getString(CouponsActivity.TYPE_MY_COUPONS);
        confirmBuyList = getArguments().getParcelableArrayList(ShoppingCartActivity.INTENT_KEY_CONFIRM_BUY_LIST);

        if (intentType.equals(CouponsActivity.TYPE_MY_COUPONS)) {
            setPullType(PullMode.PULL_BOTH);
        } else {
            setPullType(PullMode.PULL_NONE);
        }
        initRecycleView();
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
        mCouponsAdapter.setRootViewOnClickListener(mCouponsClickListener);
    }


    private View.OnClickListener mCouponsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LinearLayout mRootCouponsLayout = (LinearLayout) v.findViewById(R.id.layout_root_coupons);
            if (mRootCouponsLayout != null) {
                CouponsResult.CouponData.Coupon coupon = (CouponsResult.CouponData.Coupon) mRootCouponsLayout.getTag();
                if (coupon != null) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(CouponsActivity.INTENT_KEY_COUPONS_DATA, coupon);
                    intent.putExtras(bundle);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            }
        }
    };


    private void sendRequest(int page) {
        mCouponPresenter = new CouponPresenter(getActivity(), this);
        if (intentType.equals(CouponsActivity.TYPE_MY_COUPONS)) {
            mCouponPresenter.getCoupons(null, null, page, CouponsFragment.this);
        } else {
            mCouponPresenter.getCoupons(courseId, createDishesJson(), page, CouponsFragment.this);
        }
    }


    @Override
    public void updateCouponData(CouponsResult.CouponData couponData) {
        List<CouponsResult.CouponData.Coupon> list = couponData.getCouponList();
        if (list != null && list.size() > 0) {
            updateListView(list);
        }
    }


    class CouponsAdapter extends BaseRecycleViewAdapter<CouponsAdapter.CouponsViewHolder, CouponsResult.CouponData.Coupon> {

        private Context mContext;
        private View.OnClickListener mClickListener;

        protected CouponsAdapter(Context context) {
            super(context);
            this.mContext = context;
        }

        public void setRootViewOnClickListener(View.OnClickListener listener) {
            this.mClickListener = listener;
        }

        @Override
        protected CouponsViewHolder createHeaderViewHolder() {
            return null;
        }

        @Override
        protected CouponsViewHolder createViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_coupons, parent, false);
            return new CouponsViewHolder(view);
        }

        final class CouponsViewHolder extends BaseRecycleViewHolder<CouponsResult.CouponData.Coupon> {
            LinearLayout mRootCouponsLayout;
            LinearLayout mLeftLayout;//左边布局
            TextView mTitleTextView;//title
            TextView mTypeTextView;//优惠券类型
            TextView mEndTimeTextView;//到期时间
            ImageView mCouponsLogon;//logo
            LinearLayout mRightLayout;//右边的布局
            TextView mAmountTextView;//金额
            TextView mAmountYuanTextView;//提示
            ImageView mOverdueImageView;


            public CouponsViewHolder(View itemView) {
                super(itemView);
                mRootCouponsLayout = (LinearLayout) itemView.findViewById(R.id.layout_root_coupons);
                mLeftLayout = (LinearLayout) itemView.findViewById(R.id.layout_coupons_left);
                mTypeTextView = (TextView) itemView.findViewById(R.id.coupon_tye);
                mTitleTextView = (TextView) itemView.findViewById(R.id.coupon_title);
                mEndTimeTextView = (TextView) itemView.findViewById(R.id.coupon_end_time);
                mCouponsLogon = (ImageView) itemView.findViewById(R.id.coupon_logon);
                mRightLayout = (LinearLayout) itemView.findViewById(R.id.layout_coupons_right);
                mAmountTextView = (TextView) itemView.findViewById(R.id.coupon_amount);
                mAmountYuanTextView = (TextView) itemView.findViewById(R.id.coupon_amount_yuan);
                mOverdueImageView = (ImageView) itemView.findViewById(R.id.coupons_overdue_image);
            }

            @Override
            public void bindViews(CouponsResult.CouponData.Coupon object) {
                String couponsStatus = object.getCouponStatus();
                String couponType = object.getCouponType();
                String minAmount = object.getMinAmount();
                if (couponsStatus.equals(COUPON_STATUS_NOT_USED)) {//没有使用
                    setNotUsedBackGround(couponType, minAmount);
                    mOverdueImageView.setVisibility(View.GONE);
                    mRootCouponsLayout.setEnabled(true);
                    mRootCouponsLayout.setOnClickListener(mClickListener);
                } else if (couponsStatus.equals(COUPON_STATUS_USED)) {//使用过
                    setUsedBackGround(minAmount);
                    mOverdueImageView.setVisibility(View.VISIBLE);
                    mOverdueImageView.setImageDrawable(ResourceUtils.getDrawable(R.drawable.coupons_icon_used));
                    mRootCouponsLayout.setEnabled(false);
                } else if (couponsStatus.equals(COUPON_STATUS_OVERDUE)) {//过期
                    mOverdueImageView.setVisibility(View.VISIBLE);
                    setUsedBackGround(minAmount);
                    mOverdueImageView.setImageDrawable(ResourceUtils.getDrawable(R.drawable.coupons_icon_overdue));
                    mRootCouponsLayout.setEnabled(false);
                } else if (couponsStatus.equals(COUPON_STATUS_NO_SUBJECT)) {//不符合该项目
                    setNotProjectBackGround(couponType, minAmount);
                    mRootCouponsLayout.setEnabled(false);
                }
                mTitleTextView.setText(object.getTitle());
                mAmountTextView.setText(object.getAmount());
                mEndTimeTextView.setText(object.getEndTime() + " 到期");
                mRootCouponsLayout.setTag(object);
            }

            //设置没有使用过是的背景
            private void setNotUsedBackGround(String couponType, String minAmount) {
                mTitleTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_dark_back));
                mEndTimeTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
                mCouponsLogon.setImageDrawable(ResourceUtils.getDrawable(R.drawable.coupons_logo));
                mAmountTextView.setTextColor(ResourceUtils.getColor(R.color.white));
                mAmountYuanTextView.setTextColor(ResourceUtils.getColor(R.color.white));
                double minAmountDouble = Double.parseDouble(minAmount);
                if (couponType.equals(COUPON_TYPE_YINGYANGCANG)) {//营养餐
                    mRightLayout.setBackgroundResource(R.drawable.coupons_right_green_background);
                    mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_green));
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("购买营养餐可用,满" + minAmount + "可使用");
                    } else {
                        mTypeTextView.setText("购买营养餐可用,无门槛使用");
                    }
                } else if (couponType.equals(COUPON_TYPE_PRIVATE_COURSES)) {//私教课
                    mRightLayout.setBackgroundResource(R.drawable.coupons_right_blue_background);
                    mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_blue));
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("购买私教课可用,满" + minAmount + "可使用");
                    } else {
                        mTypeTextView.setText("购买私教课可用,无门槛使用");
                    }
                } else if (couponType.equals(COUPON_TYPE_BUY_CARD)) {//购卡
                    mRightLayout.setBackgroundResource(R.drawable.coupons_right_orange_backround);
                    mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_orange));
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("购买卡可用,满" + minAmount + "可使用");
                    } else {
                        mTypeTextView.setText("购买卡可用,无门槛使用");
                    }
                }
            }

            //设置已经使用过的优惠券
            private void setUsedBackGround(String minAmount) {
                mRightLayout.setBackgroundResource(R.drawable.coupons_right_gray_background);
                mTitleTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray));
                mEndTimeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray));
                mCouponsLogon.setImageDrawable(ResourceUtils.getDrawable(R.drawable.coupons_logo_gray));
                mAmountTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray_text));
                mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray));
                mAmountYuanTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray_text));
                double minAmountDouble = Double.parseDouble(minAmount);
                if (minAmountDouble > 0.00) {
                    mTypeTextView.setText("购买营养餐可用,满" + minAmount + "可使用");
                } else {
                    mTypeTextView.setText("购买营养餐可用,无门槛使用");
                }
                if (minAmountDouble > 0.00) {
                    mTypeTextView.setText("购买私教课可用,满" + minAmount + "可使用");
                } else {
                    mTypeTextView.setText("购买私教课可用,无门槛使用");
                }
                if (minAmountDouble > 0.00) {
                    mTypeTextView.setText("购买卡可用,满" + minAmount + "可使用");
                } else {
                    mTypeTextView.setText("购买卡可用,无门槛使用");
                }
            }

            //设不符合该项目的优惠券
            private void setNotProjectBackGround(String couponType, String minAmount) {
                mRightLayout.setBackgroundResource(R.drawable.coupons_right_gray_background);
                mTitleTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray));
                mEndTimeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray));
                mCouponsLogon.setImageDrawable(ResourceUtils.getDrawable(R.drawable.coupons_logo_gray));
                mAmountTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray_text));
                mAmountYuanTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray_text));
                double minAmountDouble = Double.parseDouble(minAmount);
                if (couponType.equals(COUPON_TYPE_YINGYANGCANG)) {//营养餐
                    mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_green));
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("购买营养餐可用,满" + minAmount + "可使用");
                    } else {
                        mTypeTextView.setText("购买营养餐可用,无门槛使用");
                    }
                } else if (couponType.equals(COUPON_TYPE_PRIVATE_COURSES)) {//私教课
                    mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_blue));
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("购买私教课可用,满" + minAmount + "可使用");
                    } else {
                        mTypeTextView.setText("购买私教课可用,无门槛使用");
                    }
                } else if (couponType.equals(COUPON_TYPE_BUY_CARD)) {//购卡
                    mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_orange));
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("购买卡可用,满" + minAmount + "可使用");
                    } else {
                        mTypeTextView.setText("购买卡可用,无门槛使用");
                    }
                }
            }


        }

    }

}
