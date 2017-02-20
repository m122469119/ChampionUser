package com.goodchef.liking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.ListUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.ui.BaseFragment;
import com.aaron.android.framework.utils.ResourceUtils;
import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.ChangeCityAdapter;
import com.goodchef.liking.eventmessages.ChangeCityActivityMessage;
import com.goodchef.liking.eventmessages.ChangeCityFragmentMessage;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.data.ChangeCityHeaderData;
import com.goodchef.liking.http.result.data.City;
import com.goodchef.liking.http.result.data.CityData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.widgets.indexBar.IndexBar;
import com.goodchef.liking.widgets.indexBar.bean.BaseIndexPinyinBean;
import com.goodchef.liking.widgets.indexBar.decoration.DividerItemDecoration;
import com.goodchef.liking.widgets.indexBar.suspension.SuspensionDecoration;
import com.goodchef.liking.widgets.indexBar.util.HeaderRecyclerAndFooterWrapperAdapter;
import com.goodchef.liking.widgets.indexBar.util.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午6:12
 * version 1.0.0
 */

public class ChangeCityFragment extends BaseFragment {

    @BindView(R.id.change_city_RecycleView)
    RecyclerView mChangeCityRecycleView;
    @BindView(R.id.change_city_indexBar)
    IndexBar mChangeCityIndexBar;
    @BindView(R.id.sideBarHint_TextView)
    TextView mSideBarHintTextView;


    private ChangeCityAdapter mChangeCityAdapter;
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private LinearLayoutManager mManager;

    //设置给InexBar、ItemDecoration的完整数据集
    private List<BaseIndexPinyinBean> mSourceDatas;
    //头部数据源
    private List<ChangeCityHeaderData> mHeaderDatas;
    //主体部分数据源（城市数据）
    private List<City.RegionsData.CitiesData> mBodyDatas;

    private SuspensionDecoration mDecoration;

    public static ChangeCityFragment newInstance() {
        Bundle args = new Bundle();
        ChangeCityFragment fragment = new ChangeCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_city, container, false);
        ButterKnife.bind(this, view);
        mManager = new LinearLayoutManager(getActivity());
        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(getActivity())));
        initView();
        return view;
    }

    private void initView() {
        mSourceDatas = new ArrayList<>();
        mHeaderDatas = new ArrayList<>();
        mSourceDatas.addAll(mHeaderDatas);

        mChangeCityAdapter = new ChangeCityAdapter(getActivity());
        mChangeCityAdapter.setData(mBodyDatas);
        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mChangeCityAdapter) {

            @Override
            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o) {

            }
        };

        mChangeCityAdapter.setOnItemClickListener(new ChangeCityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                LogUtils.e(TAG, "-----> %d" + pos);
                ChangeCityActivityMessage message = ChangeCityActivityMessage
                        .obtain(ChangeCityActivityMessage.CITY_ITEM_CLICK);
                message.msg1 = mBodyDatas.get(pos).getCityName();
                postEvent(message);
            }
        });



        mChangeCityRecycleView.setLayoutManager(mManager);
        mChangeCityRecycleView.setAdapter(mHeaderAdapter);
        mChangeCityRecycleView
                .addItemDecoration(mDecoration = new SuspensionDecoration(getActivity(), mSourceDatas)
                .setmTitleHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35,
                        getResources().getDisplayMetrics()))
                .setColorTitleBg(0xfff5f5f5)
                .setTitleFontSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
                        getResources().getDisplayMetrics()))
                .setColorTitleFont(ResourceUtils.getColor(R.color.black))
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - mHeaderDatas.size()));
        mChangeCityRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));

        mChangeCityIndexBar.setmPressedShowTextView(mSideBarHintTextView)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - mHeaderDatas.size());

        initData();
    }

    private void initData() {
        mBodyDatas = LiKingVerifyUtils.getCitiesDataList();
        //先排序
        mChangeCityIndexBar.getDataHelper().sortSourceDatas(mBodyDatas);
        mChangeCityAdapter.setData(mBodyDatas);

        mHeaderAdapter.notifyDataSetChanged();
        mSourceDatas.addAll(mBodyDatas);
        //设置数据
        mChangeCityIndexBar.setmSourceDatas(mSourceDatas).invalidate();
        mDecoration.setmDatas(mSourceDatas);
    }

    protected boolean isEventTarget() {
        return true;
    }


    public void onEvent(ChangeCityFragmentMessage msg){
        switch (msg.what) {
            case ChangeCityFragmentMessage.REFRESH_LIST_DATA:
                initData();
                break;
        }
    }

}
