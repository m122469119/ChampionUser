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
import com.goodchef.liking.http.result.CouponsPersonResult;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017/3/17
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsPersonAdapter extends BaseRecycleViewAdapter<CouponsPersonAdapter.ViewHolder, CouponsPersonResult.DataBean.CouponListBean> {

    private static final String COUPON_STATUS_NOT_USED = "0";//0未使用
    private static final String COUPON_STATUS_USED = "1";//1已使用
    private static final String COUPON_STATUS_OVERDUE = "2";// 2已过期

    private Context mContext;
    private OnItemClickListener mClickListener;

    public CouponsPersonAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, CouponsPersonResult.DataBean.CouponListBean coupon);
    }

    public void setOnItemClickListener(OnItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }


    @Override
    protected ViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_coupons, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseRecycleViewHolder<CouponsPersonResult.DataBean.CouponListBean>{
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindViews(final CouponsPersonResult.DataBean.CouponListBean object) {
            String couponsStatus = object.getCoupon_status();

            if (couponsStatus.equals(COUPON_STATUS_NOT_USED)) {//没有使用
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
            } else if (couponsStatus.equals(COUPON_STATUS_USED)) {//使用过

                mHideView.setVisibility(View.VISIBLE);
                mItemView.setEnabled(false);
                mHideText.setText(mContext.getString(R.string.already_used));
            } else if (couponsStatus.equals(COUPON_STATUS_OVERDUE)) {//过期
                mHideView.setVisibility(View.VISIBLE);

                mItemView.setEnabled(false);
                mHideText.setText(mContext.getString(R.string.already_past));

            }
            mDraw.setVisibility(View.GONE);
            mNext.setVisibility(View.VISIBLE);
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
        private void setContentTop(CouponsPersonResult.DataBean.CouponListBean object){
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
        private void setContentBottom(CouponsPersonResult.DataBean.CouponListBean object) {
            mContentBottom.setText(mContext.getString(R.string.the_period_of_validity) + object.getValid_date());
        }
    }
}
