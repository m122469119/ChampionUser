package com.chushi007.android.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.codelibrary.utils.ListUtils;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.chushi007.android.liking.R;
import com.chushi007.android.liking.http.result.BannerResult;
import com.chushi007.android.liking.utils.BannerSkipUtils;
import com.chushi007.android.liking.widgets.autoviewpager.InfinitePagerAdapter;
import com.chushi007.android.liking.widgets.autoviewpager.indicator.IconPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 16/2/17.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class BannerPagerAdapter extends InfinitePagerAdapter implements IconPagerAdapter {
    private Context mContext;
    private List<BannerResult.BannerData.Banner> mBannerList = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public BannerPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mBannerList.size();
    }

    public void setData(List<BannerResult.BannerData.Banner> banners) {
        if (ListUtils.isEmpty(banners)) {
            mBannerList = new ArrayList<>();
        } else {
            mBannerList = new ArrayList<>(banners);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        BannerViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new BannerViewHolder();
            convertView = viewHolder.getView();
        } else {
            viewHolder = (BannerViewHolder) convertView.getTag();
        }
        viewHolder.bindBanner(mBannerList.get(position));
        return convertView;
    }

    private View.OnClickListener mBannerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) v.getTag();
            if (bannerViewHolder == null) {
                return;
            }
            BannerResult.BannerData.Banner banner = bannerViewHolder.getBanner();
            if (banner == null) {
                return;
            }
            BannerSkipUtils.skip(mContext, banner);
        }
    };


    private class BannerViewHolder {
        private HImageView mBannerImageView;
        private BannerResult.BannerData.Banner mBanner;

        public BannerViewHolder() {
            mBannerImageView = (HImageView) mLayoutInflater.inflate(R.layout.layout_banner_image, null, false);
            mBannerImageView.setOnClickListener(mBannerClickListener);
            mBannerImageView.setTag(this);
        }

        public void bindBanner(BannerResult.BannerData.Banner banner) {
            if (banner == null) {
                return;
            }
            mBanner = banner;
            HImageLoaderSingleton.getInstance().requestImage(mBannerImageView, banner.getImgUrl());
        }

        public BannerResult.BannerData.Banner getBanner() {
            return mBanner;
        }

        public View getView() {
            return mBannerImageView;
        }
    }

    @Override
    public int getIconResId(int index) {
        return R.drawable.banner_indicator;
    }
}
