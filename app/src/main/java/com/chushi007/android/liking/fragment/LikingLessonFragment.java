package com.chushi007.android.liking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.aaron.android.framework.base.BaseFragment;
import com.aaron.android.framework.utils.DisplayUtils;
import com.chushi007.android.liking.R;
import com.chushi007.android.liking.adapter.BannerPagerAdapter;
import com.chushi007.android.liking.adapter.LikingLessonAdapter;
import com.chushi007.android.liking.http.result.BannerResult;
import com.chushi007.android.liking.widgets.autoviewpager.InfiniteViewPager;
import com.chushi007.android.liking.widgets.autoviewpager.indicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingLessonFragment extends BaseFragment {
    public static final int IMAGE_SLIDER_SWITCH_DURATION = 4000;
    private View headView;
    private InfiniteViewPager mImageViewPager;
    private CirclePageIndicator mCirclePageIndicator;
    private BannerPagerAdapter mBannerPagerAdapter;
    private View mSliderParentLayout;
    private ListView mListView;
    private LikingLessonAdapter mLikingLessonAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view= inflater.inflate(R.layout.fragment_liking_lesson,null,false);
        mListView = (ListView) view.findViewById(R.id.listview);
        initData();
        initView();
        return view;
    }

    private void initData(){
        List<String> list = new ArrayList<>();
        for (int i=0;i<30;i++){
            list.add(""+i);
        }
        mLikingLessonAdapter = new LikingLessonAdapter(getActivity());
        mLikingLessonAdapter.setData(list);
        mListView.setAdapter(mLikingLessonAdapter);
    }

    private void initView() {
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_liking_home_head, mListView, false);
        mSliderParentLayout = headView.findViewById(R.id.layout_liking_home_head);
        mImageViewPager = (InfiniteViewPager) headView.findViewById(R.id.liking_home_head_viewpager);
        mCirclePageIndicator = (CirclePageIndicator) headView.findViewById(R.id.liking_home_head_indicator);
       // mListView.getRefreshableView().addHeaderView(headView);
        mListView.addHeaderView(headView);
        initImageSliderLayout();
        requestBanner();
      //  setNoDataView();
    }

    private void setNoDataView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_liking_no_data, null, false);
        TextView textView = (TextView) view.findViewById(R.id.no_data_text);
       // getStateView().setNodataView(view);
    }

    private void initImageSliderLayout() {
        resizeImageSliderLayout();
        mBannerPagerAdapter = new BannerPagerAdapter(getActivity());
        mImageViewPager.setAdapter(mBannerPagerAdapter);
        mImageViewPager.setAutoScrollTime(IMAGE_SLIDER_SWITCH_DURATION);
        mCirclePageIndicator.setViewPager(mImageViewPager);
    }

    private void resizeImageSliderLayout() {
        AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) mSliderParentLayout.getLayoutParams();
        layoutParams.height = (int) (DisplayUtils.getWidthPixels() * 0.4);
    }

    private void requestBanner() {
        List<BannerResult.BannerData.Banner> banners = new ArrayList<>();
        BannerResult.BannerData.Banner banner = new BannerResult.BannerData.Banner();
        banner.setImgUrl("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=壁纸&pn=1&spn=0&di=0&pi=&rn=1&tn=baiduimagedetail&istype=&ie=utf-8&oe=utf-8&in=3354&cl=2&lm=-1&st=&cs=86613884%2C3968355442&os=3054249283%2C3914181455&simid=&adpicid=0&ln=1000&fmq=1378374347070_R&fm=&ic=0&s=0&se=&sme=&tab=&face=&ist=&jit=&statnum=wallpaper&cg=&bdtype=-1&oriquery=&objurl=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2012%2F012%2F2YXG0J416V69.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Ft4w2j_z%26e3Bkwt17_z%26e3Bv54AzdH3Ffjw6viAzdH3F1jpwts%3Fvp%3Dcann8m9ba%26z%3Da%26trg%3Duwsfj%26o561%3D%25Ec%25An%25b8%25E0%25BA%25Bb%26rg%3D8bm%26frg%3Da%261t%3D88aaaa8bdaca%26rt%3Da%266g%3D8%26pg%3Dkwt17t4w2j1jpwts%26tfpyrj%3D%26tj%3D7pu-b%265j%3D7pu-b%26tg%3Dnnc9%26vs%3Dd%26s4%3D-8%26fp%3D%26vf%3Dnlmbncc99d%25dCbmm8nbb9%265f%3Dnl898b89cc%25dCnac9d9ldbn%26ft4t1%3D9dmcbnc8mb%25dCmlcccmmn0%26w1rtvt1%3Da%26sg%3D8aaa%26u4q%3D8n0bn09n90a0a_R%26u4%3D%26tv%3Da%26f%3Da%26fj%3D%26f4j%3D%26pwk%3D%26uwvj%3D%26tfp%3D%263tp%3D%26fpwpg74%3Dowssrwrj6%26v2%3Dowssrwrj6%26k1pyrj%3Da%2656tq7j6y%3D%265k376s%3Dippr%25nA%25dF%25dFt4w2j_z%26e3Bptwg3t4j1tw_z%26e3Bv54%25dF7rs5w1I4w2jf%25dFda8d%25dFa8d%25dFdYXGaJ98mVml_z%26e3B3r2%26u65476s%3Dippr%25nA%25dF%25dFrtv_z%26e3Byjfhy_z%26e3Bv54%25dFlc%25dFnalbaclc_m_z%26e3Bfip4s%262f4%3Dcaaaaaak9&gsm=0");
        banner.setType("2");
        banners.add(banner);

        if (mBannerPagerAdapter != null) {
            mBannerPagerAdapter.setData(banners);
            mBannerPagerAdapter.notifyDataSetChanged();
        }
        mImageViewPager.setCurrentItem(0);
        mImageViewPager.startAutoScroll();
    }




    @Override
    public void onResume() {
        super.onResume();
        if (mImageViewPager != null && mImageViewPager.getChildCount() != 0) {
            mImageViewPager.startAutoScroll();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mImageViewPager != null && mImageViewPager.getChildCount() != 0) {
            mImageViewPager.stopAutoScroll();
        }
    }
}
