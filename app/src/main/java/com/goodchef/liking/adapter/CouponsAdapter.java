package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.CouponsResult;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017/3/10
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsAdapter extends BaseRecycleViewAdapter {

    private static final String COUPON_TYPE_YINGYANGCANG = "1";//1营养餐
    private static final String COUPON_TYPE_PRIVATE_COURSES = "2";//2 私教课
    private static final String COUPON_TYPE_BUY_CARD = "3";// 3 购卡
    private static final String COUPON_TYPE_GRUOP_COURSES = "4";//团体课优惠券

    private static final String COUPON_STATUS_NOT_USED = "0";//0未使用
    private static final String COUPON_STATUS_USED = "1";//1已使用
    private static final String COUPON_STATUS_OVERDUE = "2";// 2已过期
    private static final String COUPON_STATUS_NO_SUBJECT = "3";//不符合该项目

    private Context mContext;
    private OnItemClickListener mClickListener;

    public CouponsAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, CouponsResult.CouponData.Coupon coupon);
    }

    public void setOnItemClickListener(OnItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    @Override
    protected CouponsViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_coupons, parent, false);
        return new CouponsViewHolder(view);
    }

    final class CouponsViewHolder extends BaseRecycleViewHolder<CouponsResult.CouponData.Coupon> {
//        LinearLayout mRootCouponsLayout;
//        LinearLayout mLeftLayout;//左边布局
//        TextView mTitleTextView;//title
//        TextView mTypeTextView;//优惠券类型
//        TextView mEndTimeTextView;//到期时间
//        ImageView mCouponsLogon;//logo
//        LinearLayout mRightLayout;//右边的布局
//        TextView mAmountTextView;//金额
//        TextView mAmountYuanTextView;//提示
//        ImageView mOverdueImageView;
//        LinearLayout mSelectCouponLayout;
//        TextView mSelectCouponTextView;

        @BindView(R.id.rl_item_coupons)
        View mItemView;
        @BindView(R.id.txt_item_coupons_title)
        TextView mTitle;
        @BindView(R.id.txt_item_coupons_price)
        TextView mPrice;
        @BindView(R.id.txt_item_coupons_content_top)
        TextView mContentTop;
        @BindView(R.id.txt_item_coupons_content_bottom)
        TextView mContentBottom;
        @BindView(R.id.img_item_coupons_draw)
        ImageView mDraw;
        @BindView(R.id.img_item_coupons_next)
        ImageView mNext;
        @BindView(R.id.rl_item_coupons_hide)
        View mHideView;
        @BindView(R.id.txt_item_coupons_hide)
        TextView mHideText;

        public CouponsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindViews(final CouponsResult.CouponData.Coupon object) {
            String couponsStatus = object.getCouponStatus();
            String couponType = object.getCouponType();
            String minAmount = object.getMinAmount();

            if (couponsStatus.equals(COUPON_STATUS_NOT_USED)) {//没有使用
                boolean isSelect = object.isSelect();
               // setNotUsedBackGround(couponType, minAmount);
                mHideView.setVisibility(View.GONE);
                if (mClickListener != null) {
                    mItemView.setEnabled(true);
                    mItemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mClickListener.onItemClick(v, object);
                        }
                    });
                } else {
                    mHideView.setEnabled(false);
                }
               // setSelectCouponView(isSelect, object.getAmount());
            } else if (couponsStatus.equals(COUPON_STATUS_USED)) {//使用过
                //setUsedBackGround(couponType, minAmount);
                mHideView.setVisibility(View.VISIBLE);
                mItemView.setEnabled(false);
                mHideText.setText(mContext.getString(R.string.already_used));
            } else if (couponsStatus.equals(COUPON_STATUS_OVERDUE)) {//过期
                mHideView.setVisibility(View.VISIBLE);
              //  setUsedBackGround(couponType, minAmount);
                mItemView.setEnabled(false);
                mHideText.setText(mContext.getString(R.string.already_past));
              //  setSelectCouponView(false, "0");
            } else if (couponsStatus.equals(COUPON_STATUS_NO_SUBJECT)) {//不符合该项目
                mHideView.setVisibility(View.GONE);
              //  setNotProjectBackGround(couponType, minAmount);
                mItemView.setEnabled(false);
             //   setSelectCouponView(false, "0");


            }
            mTitle.setText(object.getTitle());
            setContentTop(object);
            setContentBottom(object);

        }


        /**
         * 固定日期（领取后已生效-当前时间在有效期内）：截止至2017.08.20前使用有效
           固定日期（领取后未生效-优惠券开始时间晚于当前时间）：2017.08.20后可用
           指定日期（领取后当天生效）：截止至2017.08.20前使用有效
           指定日期（领取后非当天生效）：2017.08.20后可用
          * @param object  : CouponsResult.CouponData.Coupon
         */
        private void setContentTop(CouponsResult.CouponData.Coupon object){
            mContentTop.setText(object.getAmount());
        }

        /**
         *   使用条件-设定了最低消费额：满100元可用，
             使用条件-未设定最低消费额：此条目不显示
             适用商品-全部商品适用：此条目不显示
             适用商品-指定会员卡（购买类型为全部、所有卡种）：仅限会员卡可用
             适用商品-指定会员卡（购买类型为购买续卡、所有卡种）：仅限购买（续）会员卡可用
             适用商品-指定会员卡（购买类型为升级、所有卡种）：仅限升级会员卡
             适用商品-指定会员卡（购买类型为购买续卡、指定卡种）：仅限购买（续）部分会员卡
             适用商品-指定私教课：仅限私教课
             适用商品-指定团体课：仅买团体课
         * @param object : CouponsResult.CouponData.Coupon
         */
        private void setContentBottom(CouponsResult.CouponData.Coupon object) {
            mContentBottom.setText(mContext.getString(R.string.the_period_of_validity)
                    + object.getEndTime()
                    + mContext.getString(R.string.expire));
        }



//
//        //选择过的优惠券
//        private void setSelectCouponView(boolean isSelect, String amount) {
//            if (isSelect) {
//                mSelectCouponLayout.setVisibility(View.VISIBLE);
//                mSelectCouponTextView.setVisibility(View.VISIBLE);
//                mSelectCouponTextView.setText(getString(R.string.selected) + amount + getString(R.string.yuan_coupons));
//            } else {
//                mSelectCouponLayout.setVisibility(View.GONE);
//                mSelectCouponTextView.setVisibility(View.GONE);
//            }
//        }
//
//        //设置没有使用过是的背景
//        private void setNotUsedBackGround(String couponType, String minAmount) {
//            mTitleTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_dark_back));
//            mEndTimeTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
//            mCouponsLogon.setImageDrawable(ResourceUtils.getDrawable(R.drawable.coupons_logo));
//            mAmountTextView.setTextColor(ResourceUtils.getColor(R.color.white));
//            mAmountYuanTextView.setTextColor(ResourceUtils.getColor(R.color.white));
//            double minAmountDouble = Double.parseDouble(minAmount);
//            if (couponType.equals(COUPON_TYPE_YINGYANGCANG)) {//营养餐
//                mRightLayout.setBackgroundResource(R.drawable.coupons_right_orange_backround);
//                mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_orange));
//                if (minAmountDouble > 0.00) {
//                    mTypeTextView.setText(getString(R.string.nutritious_food_dedicated) + minAmount + getString(R.string.can_uses));
//                } else {
//                    mTypeTextView.setText(R.string.utritious_food_no_doorsill);
//                }
//            } else if (couponType.equals(COUPON_TYPE_PRIVATE_COURSES)) {//私教课
//                mRightLayout.setBackgroundResource(R.drawable.coupons_right_blue_background);
//                mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_blue));
//                if (minAmountDouble > 0.00) {
//                    mTypeTextView.setText(getString(R.string.private_courses_dedicated) + minAmount + getString(R.string.can_uses));
//                } else {
//                    mTypeTextView.setText(R.string.private_courses_no_doorsill);
//                }
//            } else if (couponType.equals(COUPON_TYPE_BUY_CARD)) {//购卡
//                mRightLayout.setBackgroundResource(R.drawable.coupons_right_green_background);
//                mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_green));
//                if (minAmountDouble > 0.00) {
//                    mTypeTextView.setText(getString(R.string.buy_card_dedicated) + minAmount + getString(R.string.can_uses));
//                } else {
//                    mTypeTextView.setText(R.string.buy_card_no_doorsill);
//                }
//            } else if (couponType.equals(COUPON_TYPE_GRUOP_COURSES)) {//团体课
//                mRightLayout.setBackgroundResource(R.drawable.coupons_right_blue_background);
//                mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_blue));
//                if (minAmountDouble > 0.00) {
//                    mTypeTextView.setText(getString(R.string.group_courses_dedicated) + minAmount + getString(R.string.can_uses));
//                } else {
//                    mTypeTextView.setText(R.string.group_courses_no_doorsill);
//                }
//            }
//        }
//
//        //设置已经使用过的优惠券
//        private void setUsedBackGround(String couponType, String minAmount) {
//            mRightLayout.setBackgroundResource(R.drawable.coupons_right_gray_background);
//            mTitleTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray));
//            mEndTimeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray));
//            mCouponsLogon.setImageDrawable(ResourceUtils.getDrawable(R.drawable.coupons_logo_gray));
//            mAmountTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray_text));
//            mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray));
//            mAmountYuanTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray_text));
//            double minAmountDouble = Double.parseDouble(minAmount);
//
//            if (couponType.equals(COUPON_TYPE_YINGYANGCANG)) {//营养餐
//                if (minAmountDouble > 0.00) {
//                    mTypeTextView.setText(getString(R.string.nutritious_food_dedicated) + minAmount + getString(R.string.can_uses));
//                } else {
//                    mTypeTextView.setText(R.string.utritious_food_no_doorsill);
//                }
//            } else if (couponType.equals(COUPON_TYPE_PRIVATE_COURSES)) {//私教课
//                if (minAmountDouble > 0.00) {
//                    mTypeTextView.setText(getString(R.string.private_courses_dedicated) + minAmount + getString(R.string.can_uses));
//                } else {
//                    mTypeTextView.setText(R.string.private_courses_no_doorsill);
//                }
//            } else if (couponType.equals(COUPON_TYPE_BUY_CARD)) {//购卡
//                if (minAmountDouble > 0.00) {
//                    mTypeTextView.setText(getString(R.string.buy_card_dedicated) + minAmount + getString(R.string.can_uses));
//                } else {
//                    mTypeTextView.setText(R.string.buy_card_no_doorsill);
//                }
//            } else if (couponType.equals(COUPON_TYPE_GRUOP_COURSES)) {//团体课
//                if (minAmountDouble > 0.00) {
//                    mTypeTextView.setText(getString(R.string.group_courses_dedicated) + minAmount + getString(R.string.can_uses));
//                } else {
//                    mTypeTextView.setText(R.string.group_courses_no_doorsill);
//                }
//            }
//        }

//        //设不符合该项目的优惠券
//        private void setNotProjectBackGround(String couponType, String minAmount) {
//            mRightLayout.setBackgroundResource(R.drawable.coupons_right_gray_background);
//            mTitleTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray));
//            mEndTimeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray));
//            mCouponsLogon.setImageDrawable(ResourceUtils.getDrawable(R.drawable.coupons_logo_gray));
//            mAmountTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray_text));
//            mAmountYuanTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_gray_text));
//            double minAmountDouble = Double.parseDouble(minAmount);
//            if (couponType.equals(COUPON_TYPE_YINGYANGCANG)) {//营养餐
//                mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_green));
//                if (minAmountDouble > 0.00) {
//                    mTypeTextView.setText(getString(R.string.nutritious_food_dedicated) + minAmount + getString(R.string.can_uses));
//                } else {
//                    mTypeTextView.setText(R.string.utritious_food_no_doorsill);
//                }
//            } else if (couponType.equals(COUPON_TYPE_PRIVATE_COURSES)) {//私教课
//                mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_blue));
//                if (minAmountDouble > 0.00) {
//                    mTypeTextView.setText(getString(R.string.private_courses_dedicated) + minAmount + getString(R.string.can_uses));
//                } else {
//                    mTypeTextView.setText(R.string.private_courses_no_doorsill);
//                }
//            } else if (couponType.equals(COUPON_TYPE_BUY_CARD)) {//购卡
//                mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_green));
//                if (minAmountDouble > 0.00) {
//                    mTypeTextView.setText(getString(R.string.buy_card_dedicated) + minAmount + getString(R.string.can_uses));
//                } else {
//                    mTypeTextView.setText(R.string.buy_card_no_doorsill);
//                }
//            } else if (couponType.equals(COUPON_TYPE_GRUOP_COURSES)) {//团体课
//                mTypeTextView.setTextColor(ResourceUtils.getColor(R.color.coupons_blue));
//                if (minAmountDouble > 0.00) {
//                    mTypeTextView.setText(getString(R.string.group_courses_dedicated)+ minAmount + getString(R.string.can_uses));
//                } else {
//                    mTypeTextView.setText(R.string.group_courses_no_doorsill);
//                }
//            }
//        }


    }
}
