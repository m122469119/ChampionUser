package com.goodchef.liking.module.body.analyze;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BodyAnalyzeTitleAdapter;
import com.goodchef.liking.eventmessages.BodyAnalyzeHistoryMessage;
import com.goodchef.liking.http.result.BodyModelNavigationResult;
import com.goodchef.liking.http.result.data.BodyHistoryData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午7:05
 * version 1.0.0
 */

public class BodyAnalyzeChartActivity extends AppBarActivity implements BodyAnalyzeChartContract.BodyModelNavigationView {

    public static final String KEY_HISTORY_MODULES = "key_history_modules";
    public static final String KEY_HISTORY_TITLE = "key_history_title";

    @BindView(R.id.analyze_title_RecyclerView)
    RecyclerView mTitleRecyclerView;

    private BodyAnalyzeChartContract.BodyModeNavigationPresenter mBodyModeNavigationPresenter;
    private String modules;
    private String title;
    private BodyAnalyzeTitleAdapter mBodyAnalyzeTitleAdapter;
    private List<BodyModelNavigationResult.HistoryTitleData.NavData> navDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_chart);
        ButterKnife.bind(this);
        getInitData();
        sendRequest(modules);
    }

    private void getInitData() {
        modules = getIntent().getStringExtra(KEY_HISTORY_MODULES);
        title = getIntent().getStringExtra(KEY_HISTORY_TITLE);
        setTitle(title);
    }

    private void sendRequest(String modules) {
        if (mBodyModeNavigationPresenter == null) {
            mBodyModeNavigationPresenter = new BodyAnalyzeChartContract.BodyModeNavigationPresenter(this, this);
        }
        mBodyModeNavigationPresenter.getBodyModeNavigation(modules);
    }

    private void setAnalyzeFragment(ArrayList<BodyHistoryData> firstList, String unit) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(BodyAnalyzeChartFragment.KEY_HISTORY_LIST, firstList);
        bundle.putString(BodyAnalyzeChartFragment.KEY_HISTORY_UNIT, unit);
        bundle.putString(BodyAnalyzeChartFragment.KEY_HISTORY_MODULES, modules);
        fragmentTransaction.add(R.id.analyze_FrameLayout, BodyAnalyzeChartFragment.newInstance(bundle));
        fragmentTransaction.commit();
    }

    @Override
    public void updateBodyModelNavigationView(BodyModelNavigationResult.HistoryTitleData data) {
        navDataList = data.getNavData();
        ArrayList<BodyHistoryData> firstList = (ArrayList<BodyHistoryData>) data.getHistoryData();
        setTopTitleRecycleView();
        if (navDataList != null && navDataList.size() > 0) {
            setAnalyzeFragment(firstList, navDataList.get(0).getUnit());
        } else {
            setAnalyzeFragment(firstList, "");
        }
    }

    private void setTopTitleRecycleView() {
        if (navDataList != null && navDataList.size() > 0) {
            mTitleRecyclerView.setVisibility(View.VISIBLE);
            for (int i = 0; i < navDataList.size(); i++) {
                if (i == 0) {
                    navDataList.get(i).setSelect(true);
                } else {
                    navDataList.get(i).setSelect(false);
                }
                navDataList.get(i).setId(i);
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mTitleRecyclerView.setLayoutManager(linearLayoutManager);
            mBodyAnalyzeTitleAdapter = new BodyAnalyzeTitleAdapter(this);
            mBodyAnalyzeTitleAdapter.setData(navDataList);
            mTitleRecyclerView.setAdapter(mBodyAnalyzeTitleAdapter);
            mBodyAnalyzeTitleAdapter.setClickListener(mItemClickListener);
        } else {
            mTitleRecyclerView.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener mItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView mBodyAnalyzeTextView = (TextView) v.findViewById(R.id.body_analyze_title_TextView);
            if (mBodyAnalyzeTextView != null) {
                BodyModelNavigationResult.HistoryTitleData.NavData object = (BodyModelNavigationResult.HistoryTitleData.NavData) mBodyAnalyzeTextView.getTag();
                if (object != null) {
                    for (int i = 0; i < navDataList.size(); i++) {
                        if (object.getId() == navDataList.get(i).getId()) {
                            navDataList.get(i).setSelect(true);
                        } else {
                            navDataList.get(i).setSelect(false);
                        }
                    }
                    mBodyAnalyzeTitleAdapter.notifyDataSetChanged();
                    postEvent(new BodyAnalyzeHistoryMessage(object.getColumn(), object.getUnit(), modules));
                }
            }
        }
    };

    @Override
    public void changeStateView(StateView.State state) {

    }
}
