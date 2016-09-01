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

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.base.widget.refresh.PullMode;
import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.BuyCardConfirmActivity;
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
public class CouponsFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment implements CouponView {
    private static final String COUPON_TYPE_YINGYANGCANG = "1";//1营养餐
    private static final String COUPON_TYPE_PRIVATE_COURSES = "2";//2 私教课
    private static final String COUPON_TYPE_BUY_CARD = "3";// 3 购卡
    private static final String COUPON_TYPE_GRUOP_COURSES = "4";//团体课优惠券

    private static final String COUPON_STATUS_NOT_USED = "0";//0未使用
    private static final String COUPON_STATUS_USED = "1";//1已使用
    private static final String COUPON_STATUS_OVERDUE = "2";// 2已过期
    private static final String COUPON_STATUS_NO_SUBJECT = "3";//不符合该项目


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
        if (intentType.equals(CouponsActivity.TYPE_MY_COUPONS)) {
            setPullMode(PullMode.PULL_BOTH);
        } else {
            setPullMode(PullMode.PULL_NONE);
        }
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
            mCouponsAdapter.setRootViewOnClickListener(null);
        } else {
            mCouponsAdapter.setRootViewOnClickListener(mCouponsClickListener);
        }
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
            mCouponPresenter.getCoupons(null, null, null, null, null, null, page,gymId, CouponsFragment.this);
        } else {
            mCouponPresenter.getCoupons(courseId, selectTimes, createDishesJson(), cardId, type, scheduleId, page,gymId, CouponsFragment.this);
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
            LinearLayout mSelectCouponLayout;
            TextView mSelectCouponTextView;


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
                mSelectCouponLayout = (LinearLayout) itemView.findViewById(R.id.layout_select_coupon);
                mSelectCouponTextView = (TextView) itemView.findViewById(R.id.select_coupon);
            }

            @Override
            public void bindViews(CouponsResult.CouponData.Coupon object) {
                String couponsStatus = object.getCouponStatus();
                String couponType = object.getCouponType();
                String minAmount = object.getMinAmount();
                if (couponsStatus.equals(COUPON_STATUS_NOT_USED)) {//没有使用
                    boolean isSelect = object.isSelect();
                    setNotUsedBackGround(couponType, minAmount);
                    mOverdueImageView.setVisibility(View.GONE);
                    if (mClickListener != null) {
                        mRootCouponsLayout.setEnabled(true);
                        mRootCouponsLayout.setOnClickListener(mClickListener);
                    } else {
                        mRootCouponsLayout.setEnabled(false);
                    }
                    setSelectCouponView(isSelect, object.getAmount());
                } else if (couponsStatus.equals(COUPON_STATUS_USED)) {//使用过
                    setUsedBackGround(couponType, minAmount);
                    mOverdueImageView.setVisibility(View.VISIBLE);
                    mOverdueImageView.setImageDrawable(ResourceUtils.getDrawable(R.drawable.coupons_icon_used));
                    mRootCouponsLayout.setEnabled(false);
                    setSelectCouponView(false, "0");
                } else if (couponsStatus.equals(COUPON_STATUS_OVERDUE)) {//过期
                    mOverdueImageView.setVisibility(View.VISIBLE);
                    setUsedBackGround(couponType, minAmount);
                    mOverdueImageView.setImageDrawable(ResourceUtils.getDrawable(R.drawable.coupons_icon_overdue));
                    mRootCouponsLayout.setEnabled(false);
                    setSelectCouponView(false, "0");
                } else if (couponsStatus.equals(COUPON_STATUS_NO_SUBJECT)) {//不符合该项目
                    setNotProjectBackGround(couponType, minAmount);
                    mRootCouponsLayout.setEnabled(false);
                    setSelectCouponView(false, "0");
                }
                mTitleTextView.setText(object.getTitle());
                mAmountTextView.setText(object.getAmount());
                mEndTimeTextView.setText(object.getEndTime() + " 到期");
                mRootCouponsLayout.setTag(object);
            }

            //选择过的优惠券
            private void setSelectCouponView(boolean isSelect, String amount) {
                if (isSelect) {
                    mSelectCouponLayout.setVisibility(View.VISIBLE);
                    mSelectCouponTextView.setVisibility(View.VISIBLE);
                    mSelectCouponTextView.setText("已选择" + amount + "元优惠券");
                } else {
                    mSelectCouponLayout.setVisibility(View.GONE);
                    mSelectCouponTextView.setVisibility(View.GONE);
                }
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
                    mRightLayout.setBackgroundResource(R.drawable.coupons_right_orange_backround);
                    mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_orange));
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("营养餐专用满" + minAmount + "可用");
                    } else {
                        mTypeTextView.setText("营养餐专用,无门槛使用");
                    }
                } else if (couponType.equals(COUPON_TYPE_PRIVATE_COURSES)) {//私教课
                    mRightLayout.setBackgroundResource(R.drawable.coupons_right_blue_background);
                    mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_blue));
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("私教课专用满" + minAmount + "可用");
                    } else {
                        mTypeTextView.setText("私教课专用,无门槛使用");
                    }
                } else if (couponType.equals(COUPON_TYPE_BUY_CARD)) {//购卡
                    mRightLayout.setBackgroundResource(R.drawable.coupons_right_green_background);
                    mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_green));
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("购卡专用满" + minAmount + "可用");
                    } else {
                        mTypeTextView.setText("购卡专用,无门槛使用");
                    }
                } else if (couponType.equals(COUPON_TYPE_GRUOP_COURSES)) {//团体课
                    mRightLayout.setBackgroundResource(R.drawable.coupons_right_blue_background);
                    mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_blue));
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("团体课专用满" + minAmount + "可用");
                    } else {
                        mTypeTextView.setText("团体课专用,无门槛使用");
                    }
                }
            }

            //设置已经使用过的优惠券
            private void setUsedBackGround(String couponType, String minAmount) {
                mRightLayout.setBackgroundResource(R.drawable.coupons_right_gray_background);
                mTitleTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray));
                mEndTimeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray));
                mCouponsLogon.setImageDrawable(ResourceUtils.getDrawable(R.drawable.coupons_logo_gray));
                mAmountTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray_text));
                mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray));
                mAmountYuanTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray_text));
                double minAmountDouble = Double.parseDouble(minAmount);

                if (couponType.equals(COUPON_TYPE_YINGYANGCANG)) {//营养餐
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("营养餐专用满" + minAmount + "可用");
                    } else {
                        mTypeTextView.setText("营养餐专用,无门槛使用");
                    }
                } else if (couponType.equals(COUPON_TYPE_PRIVATE_COURSES)) {//私教课
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("购买私教课可用满" + minAmount + "可用");
                    } else {
                        mTypeTextView.setText("私教课专用,无门槛使用");
                    }
                } else if (couponType.equals(COUPON_TYPE_BUY_CARD)) {//购卡
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("购卡专用满" + minAmount + "可用");
                    } else {
                        mTypeTextView.setText("购卡专用,无门槛使用");
                    }
                } else if (couponType.equals(COUPON_TYPE_GRUOP_COURSES)) {//团体课
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("团体课专用满" + minAmount + "可用");
                    } else {
                        mTypeTextView.setText("团体课专用,无门槛使用");
                    }
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
                        mTypeTextView.setText("营养餐专用满" + minAmount + "可用");
                    } else {
                        mTypeTextView.setText("营养餐专用,无门槛使用");
                    }
                } else if (couponType.equals(COUPON_TYPE_PRIVATE_COURSES)) {//私教课
                    mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_blue));
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("购买私教课可用满" + minAmount + "可用");
                    } else {
                        mTypeTextView.setText("私教课专用,无门槛使用");
                    }
                } else if (couponType.equals(COUPON_TYPE_BUY_CARD)) {//购卡
                    mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_orange));
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("购卡专用满" + minAmount + "可用");
                    } else {
                        mTypeTextView.setText("购卡专用,无门槛使用");
                    }
                } else if (couponType.equals(COUPON_TYPE_GRUOP_COURSES)) {//团体课
                    mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_blue));
                    if (minAmountDouble > 0.00) {
                        mTypeTextView.setText("团体课专用满" + minAmount + "可用");
                    } else {
                        mTypeTextView.setText("团体课专用,无门槛使用");
                    }
                }
            }


        }

    }

}
