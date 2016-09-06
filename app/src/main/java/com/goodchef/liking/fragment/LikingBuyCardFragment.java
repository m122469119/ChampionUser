package com.goodchef.liking.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PullMode;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.BuyCardConfirmActivity;
import com.goodchef.liking.adapter.BuyCardAdapter;
import com.goodchef.liking.eventmessages.BuyCardListMessage;
import com.goodchef.liking.eventmessages.ChangGymMessage;
import com.goodchef.liking.eventmessages.InitApiFinishedMessage;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.eventmessages.OnCLickBuyCardFragmentMessage;
import com.goodchef.liking.eventmessages.getGymDataMessage;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.CardResult;
import com.goodchef.liking.http.result.CoursesResult;
import com.goodchef.liking.http.result.data.CityData;
import com.goodchef.liking.http.result.data.GymData;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.CardListPresenter;
import com.goodchef.liking.mvp.view.CardListView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.UMengCountUtil;

import java.util.List;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingBuyCardFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment implements CardListView {
    public static final String KEY_CARD_CATEGORY = "key_card_category";
    public static final String KEY_CATEGORY_ID = "key_category_id";
    public static final String KEY_BUY_TYPE = "key_buy_type";

    private BuyCardAdapter mBuyCardAdapter;
    private CardListPresenter mCardListPresenter;
    private View mHeadView;
    private static final int TYPE_BUY = 1;
    private TextView mCityOpenTextView;//当前城市是否开通
    private String gymId = "0";
    private String longitude = "0";
    private String latitude = "0";
    private String cityId = "310100";
    private String districtId = "310104";
    private GymData mGymData;

    @Override
    protected void requestData(int page) {
        sendBuyCardListRequest();
    }

    @Override
    protected void initViews() {
        setPullMode(PullMode.PULL_NONE);
        setNoDataView();
        mBuyCardAdapter = new BuyCardAdapter(getActivity());
        getStateView().setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                LiKingVerifyUtils.initApi(getActivity());
            }
        });
        setRecyclerViewPadding(0, 0, 0, DisplayUtils.dp2px(10));
        setRecyclerAdapter(mBuyCardAdapter);
        initRecycleHeadView();
        setItemClickListener();
    }

    private void getLocationData() {
        LocationData  locationData = Preference.getLocationData();
        if (locationData != null) {
            longitude = locationData.getLongitude() + "";
            latitude = locationData.getLatitude() + "";
            cityId = locationData.getCityId();
            districtId = locationData.getDistrictId();
        }
    }


    private void setItemClickListener() {
        mBuyCardAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CardResult.CardData.Card card = mBuyCardAdapter.getDataList().get(position);
                if (card != null) {
                    if (!StringUtils.isEmpty(mGymData.getGymId())) {
                        UMengCountUtil.UmengCount(getActivity(), UmengEventId.BUYCARDCONFIRMACTIVITY);
                        Intent intent = new Intent(getActivity(), BuyCardConfirmActivity.class);
                        intent.putExtra(KEY_CARD_CATEGORY, card.getCategoryName());
                        intent.putExtra(KEY_CATEGORY_ID, card.getCategoryId());
                        intent.putExtra(KEY_BUY_TYPE, 1);
                        intent.putExtra(LikingLessonFragment.KEY_GYM_ID, mGymData.getGymId());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
    }

    private void setNoDataView() {
        View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
        ImageView noDataImageView = (ImageView) noDataView.findViewById(R.id.imageview_no_data);
        TextView noDataText = (TextView) noDataView.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) noDataView.findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.icon_no_coureses_data);
        noDataText.setText(R.string.no_data);
        refreshView.setText(R.string.refresh_btn_text);
        refreshView.setOnClickListener(refreshOnClickListener);
        getStateView().setNodataView(noDataView);
    }

    /***
     * 刷新事件
     */
    private View.OnClickListener refreshOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendBuyCardListRequest();
        }
    };

    private void initRecycleHeadView() {
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_buy_card_item, getRecyclerView(), false);
        mCityOpenTextView = (TextView) mHeadView.findViewById(R.id.buy_card_head_text);
        mCityOpenTextView.setVisibility(View.VISIBLE);
        mCityOpenTextView.setText(R.string.current_city_no_dredge);
    }

    private void sendBuyCardListRequest() {
        if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
            getStateView().setState(StateView.State.FAILED);
        } else {
            getLocationData();
            mCardListPresenter = new CardListPresenter(getActivity(), this);
            mCardListPresenter.getCardList(longitude, latitude, cityId, districtId, gymId, TYPE_BUY);
        }
    }

    @Override
    public void updateCardListView(CardResult.CardData cardData) {
        mGymData = cardData.getGymData();
        CoursesResult.Courses.Gym gym = new CoursesResult.Courses.Gym();
        gym.setGymId(mGymData.getGymId());
        gym.setDistance(mGymData.getDistance());
        gym.setName(mGymData.getName());
        postEvent(new getGymDataMessage(gym));
        List<CardResult.CardData.Card> list = cardData.getCardList();
        if (list != null && list.size() > 0) {
            LocationData locationData = Preference.getLocationData();
            if (locationData != null) {
                String cityName = locationData.getCityName();
                boolean isLocation = locationData.isPositionSuccess();
                if (!StringUtils.isEmpty(cityName)) {
                    setHeadNoLocationView(cityName);
                } else if (isLocation) {
                    SetHeadView();
                } else {
                    removeHeadView();
                }
            }
            updateListView(list);
        }
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(MainAddressChanged mainAddressChanged) {
        String cityName = mainAddressChanged.getCityName();
        if (!StringUtils.isEmpty(cityName)) {
            setHeadNoLocationView(cityName);
        }
        latitude = mainAddressChanged.getLatitude() + "";
        longitude = mainAddressChanged.getLongitude() + "";
        cityId = mainAddressChanged.getCityId();
        districtId = mainAddressChanged.getDistrictId();
        loadHomePage();
    }

    public void onEvent(InitApiFinishedMessage message) {
        if (message.isSuccess()) {
            loadHomePage();
        }
    }

    public void onEvent(OnCLickBuyCardFragmentMessage message) {
        sendBuyCardListRequest();
    }

    public void onEvent(BuyCardListMessage message) {
        sendBuyCardListRequest();
    }


    public void onEvent(ChangGymMessage message) {
        gymId = message.getGymId();
        int index = message.getIndex();
        if (index == 1) {//从买卡界面切换场馆过来
            sendBuyCardListRequest();
        }
    }

    private void setHeadNoLocationView(String cityName) {
        boolean isContains = false;
        BaseConfigResult baseConfigResult = Preference.getBaseConfig();
        if (baseConfigResult != null) {
            BaseConfigResult.BaseConfigData baseConfigData = baseConfigResult.getBaseConfigData();
            if (baseConfigData != null) {
                List<CityData> cityDataList = baseConfigData.getCityList();
                if (cityDataList != null && cityDataList.size() > 0) {
                    for (CityData data : cityDataList) {
                        if (data.getCityName().contains(cityName)) {
                            isContains = true;
                            break;
                        }
                    }
                    if (isContains) {
                        removeHeadView();
                    } else {
                        SetHeadView();
                    }
                }
            }
        }
    }

    private void SetHeadView() {
        if (mBuyCardAdapter != null) {
            if (mHeadView != null) {
                getRecyclerView().removeView(mHeadView);
                mBuyCardAdapter.setHeaderView(mHeadView);
                mBuyCardAdapter.notifyDataSetChanged();
            }
        }
    }

    private void removeHeadView() {
        if (mBuyCardAdapter != null) {
            if (mHeadView != null) {
                getRecyclerView().removeView(mHeadView);
                mBuyCardAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void handleNetworkFailure() {
    }

}
