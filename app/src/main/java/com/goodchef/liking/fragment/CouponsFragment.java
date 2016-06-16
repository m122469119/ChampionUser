package com.goodchef.liking.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkPagerLoaderRecyclerViewFragment;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.CouponsActivity;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.mvp.presenter.CouponPresenter;
import com.goodchef.liking.mvp.view.CouponView;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 下午2:28
 */
public class CouponsFragment extends NetworkPagerLoaderRecyclerViewFragment implements CouponView {

    private CouponPresenter mCouponPresenter;
    private String courseId = "";
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
        initRecycleView();
    }

    private void initRecycleView() {
        mCouponsAdapter = new CouponsAdapter(getActivity());
        setRecyclerAdapter(mCouponsAdapter);
        mCouponsAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CouponsResult.CouponData.Coupon coupon = mCouponsAdapter.getDataList().get(position);
                if (coupon != null) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(CouponsActivity.INTENT_KEY_COUPONS_DATA, coupon);
                    intent.putExtras(bundle);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
    }


    private void sendRequest(int page) {
        setPullType(PullMode.PULL_BOTH);
        mCouponPresenter = new CouponPresenter(getActivity(), this);
        mCouponPresenter.getCoupons(courseId, page, CouponsFragment.this);
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

        protected CouponsAdapter(Context context) {
            super(context);
            this.mContext = context;
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
            TextView mTitleTextView;
            TextView mAmountTextView;

            public CouponsViewHolder(View itemView) {
                super(itemView);
                mTitleTextView = (TextView) itemView.findViewById(R.id.coupon_title);
                mAmountTextView = (TextView) itemView.findViewById(R.id.coupon_amount);
            }

            @Override
            public void bindViews(CouponsResult.CouponData.Coupon object) {
                mTitleTextView.setText(object.getTitle());
                mAmountTextView.setText(object.getAmount());
            }
        }
    }

}
