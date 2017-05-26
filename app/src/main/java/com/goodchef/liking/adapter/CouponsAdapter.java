package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.CouponsResult;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017/3/10
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsAdapter extends BaseRecycleViewAdapter {

    private static final String COUPON_NOT_CAN_USE = "0";//不能使用
    private static final String COUPON_CAN_USE = "1";//能使用

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
            String couponsStatus = object.getCan_use();

            if (couponsStatus.equals(COUPON_NOT_CAN_USE)) {//不可用
                mHideView.setVisibility(View.VISIBLE);
                mItemView.setEnabled(false);
                if (StringUtils.isEmpty(object.getNot_use_desc())){
                    mHideText.setText("不可用");
                } else {
                    mHideText.setText(object.getNot_use_desc());
                }
            } else if (couponsStatus.equals(COUPON_CAN_USE)) {//可用
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
            }

            mNext.setVisibility(View.GONE);

            if (object.isSelect()) {
                mDraw.setVisibility(View.VISIBLE);
            } else {
                mDraw.setVisibility(View.GONE);
            }

            mTitle.setText(object.getTitle());
            mPrice.setText(object.getAmount());
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
            mContentTop.setText(object.getUse_desc());
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
                    + object.getValid_date());
        }

    }
}
