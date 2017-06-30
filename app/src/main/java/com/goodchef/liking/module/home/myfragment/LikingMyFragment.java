package com.goodchef.liking.module.home.myfragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.BaseMVPFragment;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.CoursesResult;
import com.goodchef.liking.data.remote.retrofit.result.MyUserOtherInfoResult;
import com.goodchef.liking.data.remote.retrofit.result.UserExerciseResult;
import com.goodchef.liking.eventmessages.GymNoticeMessage;
import com.goodchef.liking.eventmessages.InitApiFinishedMessage;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.eventmessages.LoginOutMessage;
import com.goodchef.liking.module.brace.mybracelet.MyBraceletActivity;
import com.goodchef.liking.module.card.my.MyCardActivity;
import com.goodchef.liking.module.card.order.MyOrderActivity;
import com.goodchef.liking.module.coupons.CouponsActivity;
import com.goodchef.liking.module.course.MyLessonActivity;
import com.goodchef.liking.module.course.selfhelp.SelfHelpGroupActivity;
import com.goodchef.liking.module.home.LikingHomeActivity;
import com.goodchef.liking.module.home.myfragment.water.WaterRateActivity;
import com.goodchef.liking.module.joinus.becomecoach.BecomeTeacherActivity;
import com.goodchef.liking.module.joinus.contractjoin.ContactJonInActivity;
import com.goodchef.liking.module.login.LoginActivity;
import com.goodchef.liking.module.more.MoreActivity;
import com.goodchef.liking.module.train.MyTrainDataActivity;
import com.goodchef.liking.module.userinfo.MyInfoActivity;
import com.goodchef.liking.umeng.UmengEventId;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.goodchef.liking.utils.TypefaseUtil;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/5/20.
 * 我的界面
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingMyFragment extends BaseMVPFragment<LikingMyContract.Presenter> implements android.view.View.OnClickListener, LikingMyContract.View {
    public static final String KEY_MY_BRACELET_MAC = "key_my_bracelet_mac";
    public static final String KEY_UUID = "key_UUID";
    @BindView(R.id.my_state_view)
    LikingStateView mStateView;
    @BindView(R.id.head_image_background)
    HImageView mHImageViewBackground;//头像背景;
    @BindView(R.id.head_image)
    HImageView mHeadHImageView;//头像
    @BindView(R.id.person_name)
    TextView mPersonNameTextView;//用户名称;
    @BindView(R.id.is_vip)
    TextView mIsVip;
    @BindView(R.id.layout_head_info)
    RelativeLayout mHeadInfoLayout;//头像布局;
    @BindView(R.id.person_phone)
    TextView mPersonPhoneTextView;//用户手机;
    @BindView(R.id.login_text)
    TextView mLoginBtn;//登录按钮;
    @BindView(R.id.my_head_person_data_TextView)
    TextView mHeadPersonDataTextView;
    @BindView(R.id.layout_my_course)
    LinearLayout mMyCourseLayout;//我的课程;
    @BindView(R.id.layout_my_order)
    LinearLayout mMyOrderLayout;//我的订单;
    @BindView(R.id.layout_member_card)
    LinearLayout mMemberCardLayout;//会员卡;
    @BindView(R.id.layout_coupons)
    LinearLayout mCouponsLayout;//我的优惠券;
    @BindView(R.id.layout_more)
    RelativeLayout mMoreLayout;//更多
    @BindView(R.id.more_ImageView)
    ImageView mUpdateAppImageView;//更新小红点

    private LinearLayout mContactJoinLayout;//联系加盟
    private LinearLayout mBecomeTeacherLayout;//称为教练
    private LinearLayout mSelfHelpGroupLayout;//自助团体课
    private LinearLayout mBindBraceletLinearLayout;//绑定手环
    private LinearLayout mWaterRateLinearLayout;   //水费充值
    private LinearLayout mBodyScoreLayout;//体测评分
    private LinearLayout mEverydaySportLayout;//每日运动
    private LinearLayout mTrainLayout;//今日训练
    private TextView mBodyScoreData;//个人训练数据
    private TextView mEveryDataSportData;//每日运动
    private TextView mTrainTimeData;//训练时间


    private TextView mWaterSurplus; //剩余水费时间


    private boolean isRetryRequest = true;
    private Typeface mTypeface;//字体
    private String mBraceletMac;//手环mac地址
    private String UUID;//蓝牙UUID


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liking_my, container, false);
        ButterKnife.bind(this, view);
        mTypeface = TypefaseUtil.getImpactTypeface(getActivity());
        initView(view);
        initViewIconAndText();
        setHeadPersonData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (EnvironmentUtils.Network.isNetWorkAvailable()) {
            mStateView.setState(StateView.State.SUCCESS);
            showUpdate();
            setLogonView();
            getMyUserInfoOther();
            getUserExerciseData();
        } else {
            mStateView.setState(StateView.State.FAILED);
        }
    }

    private void showUpdate() {
        int update = LikingPreference.getUpdateApp();
        if (update == 0) {//不更新
            mUpdateAppImageView.setVisibility(android.view.View.GONE);
        } else if (update == 1 || update == 2) {//有更新
            mUpdateAppImageView.setVisibility(android.view.View.VISIBLE);
        }
    }

    /**
     * 获个人信息
     */
    private void getMyUserInfoOther() {
        if (LikingPreference.isLogin()) {
            if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
                clearExerciseData();
            } else {
                if (isRetryRequest) {
                    mStateView.setState(StateView.State.LOADING);
                    isRetryRequest = false;
                }
                mPresenter.getUserData();
            }
        } else {
            if (EnvironmentUtils.Network.isNetWorkAvailable()) {
                mStateView.setState(StateView.State.SUCCESS);
            }
            clearExerciseData();
        }
    }


    /**
     * 获取我的锻炼数据
     */
    private void getUserExerciseData() {
        if (LikingPreference.isLogin()) {
            mPresenter.getUserExerciseData();
        } else {
            clearExerciseData();
        }
    }

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(StateView.State.FAILED);
        isRetryRequest = true;
        clearExerciseData();
    }

    @Override
    public void updateInfoData(MyUserOtherInfoResult.UserOtherInfoData userOtherInfoData) {
        mStateView.setState(StateView.State.SUCCESS);
        LikingPreference.setIsBind(userOtherInfoData.getIsBind());
        LikingPreference.setIsVip(Integer.parseInt(userOtherInfoData.getIsVip()));
        mBraceletMac = userOtherInfoData.getBraceletMac();
        UUID = userOtherInfoData.getUuid();
        if (LikingPreference.isVIP()) {
            mIsVip.setVisibility(android.view.View.VISIBLE);
        } else {
            mIsVip.setVisibility(android.view.View.GONE);
        }
        if (LikingPreference.isBind()) {
            setMySettingCard(mBindBraceletLinearLayout, R.string.layout_bing_bracelet_my, true);
            mEveryDataSportData.setText(userOtherInfoData.getAllDistance());
        } else {
            setMySettingCard(mBindBraceletLinearLayout, R.string.layout_bing_bracelet, true);
        }

        if (userOtherInfoData.getWaterData() != null) {
            if (userOtherInfoData.getWaterData().getWater_status() == MyUserOtherInfoResult.UserOtherInfoData.WaterData.CHARGE_WATER) {
                mWaterRateLinearLayout.setVisibility(View.VISIBLE);
                mWaterSurplus.setVisibility(View.VISIBLE);
                mWaterSurplus.setText( getString(R.string.time_remaining)+userOtherInfoData.getWaterData().getWater_time() + getString(R.string.min));
            } else  {
                mWaterRateLinearLayout.setVisibility(View.GONE);
                mWaterSurplus.setVisibility(View.GONE);
            }
        } else  {
            mWaterRateLinearLayout.setVisibility(View.GONE);
            mWaterSurplus.setVisibility(View.GONE);
        }

        setHeadPersonData();
    }

    @Override
    public void updateExerciseData(UserExerciseResult.ExerciseData exerciseData) {
        mStateView.setState(StateView.State.SUCCESS);
        doExerciseData(exerciseData);
    }


    /**
     * 处理
     *
     * @param exerciseData
     */
    private void doExerciseData(UserExerciseResult.ExerciseData exerciseData) {
        if (exerciseData != null) {
            mTrainTimeData.setText(exerciseData.getTodayMin());
            if (LikingPreference.isBind()) {
                mBodyScoreData.setText(exerciseData.getScore());
            } else {
                mEveryDataSportData.setText(exerciseData.getScore());
            }
        }
    }

    /**
     * 清除训练数据
     */
    private void clearExerciseData() {
        if (mBodyScoreData != null) {
            mBodyScoreData.setText("--");
        }
        mTrainTimeData.setText("--");
        mEveryDataSportData.setText("--");
    }


    private void setLogonView() {
        if (LikingPreference.isLogin()) {
            mLoginBtn.setVisibility(android.view.View.GONE);
            mPersonNameTextView.setVisibility(android.view.View.VISIBLE);
            mPersonPhoneTextView.setVisibility(android.view.View.VISIBLE);
            mPersonNameTextView.setText(LikingPreference.getNickName());
            mPersonPhoneTextView.setText(LikingPreference.getUserPhone());
            if (!StringUtils.isEmpty(LikingPreference.getUserIconUrl())) {
                HImageLoaderSingleton.loadImage(mHeadHImageView, LikingPreference.getUserIconUrl(), getActivity());
                HImageLoaderSingleton.loadImage(mHImageViewBackground, LikingPreference.getUserIconUrl(), getActivity());
            }
            if (LikingPreference.isVIP()) {
                mIsVip.setVisibility(android.view.View.VISIBLE);
            } else {
                mIsVip.setVisibility(android.view.View.GONE);
            }
        } else {
            mLoginBtn.setVisibility(android.view.View.VISIBLE);
            mPersonNameTextView.setVisibility(android.view.View.GONE);
            mPersonPhoneTextView.setVisibility(android.view.View.GONE);
            mIsVip.setVisibility(android.view.View.GONE);
            mHeadHImageView.setImageDrawable(ResourceUtils.getDrawable(R.drawable.icon_head_default_image));
            HImageLoaderSingleton.loadImage(mHImageViewBackground, "", getActivity());
        }
    }

    private void initView(android.view.View view) {
        mContactJoinLayout = (LinearLayout) view.findViewById(R.id.layout_contact_join);
        mBecomeTeacherLayout = (LinearLayout) view.findViewById(R.id.layout_become_teacher);
        mSelfHelpGroupLayout = (LinearLayout) view.findViewById(R.id.layout_self_help_group_gym);
        mBindBraceletLinearLayout = (LinearLayout) view.findViewById(R.id.layout_bind_bracelet);

        mWaterRateLinearLayout = (LinearLayout) view.findViewById(R.id.layout_water_rate);
        mWaterSurplus = (TextView) mWaterRateLinearLayout.findViewById(R.id.standard_my_label);

        mBodyScoreLayout = (LinearLayout) view.findViewById(R.id.layout_body_score);
        mEverydaySportLayout = (LinearLayout) view.findViewById(R.id.layout_everyday_sport);
        mTrainLayout = (LinearLayout) view.findViewById(R.id.layout_today_data);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                setLogonView();
                isRetryRequest = true;
                getMyUserInfoOther();
                getUserExerciseData();
            }
        });
        showSelfHelpGroupLayout(((LikingHomeActivity) getActivity()).mCanSchedule);
    }


    private void initViewIconAndText() {
        setMySettingCard(mSelfHelpGroupLayout, R.string.layout_self_help_group, true);
        if (LikingPreference.isBind()) {
            setMySettingCard(mBindBraceletLinearLayout, R.string.layout_bing_bracelet_my, true);
        } else {
            setMySettingCard(mBindBraceletLinearLayout, R.string.layout_bing_bracelet, true);
        }
        setMySettingCard(mWaterRateLinearLayout, R.string.layout_water_rate, true);
        mWaterRateLinearLayout.setVisibility(View.GONE);
        mWaterSurplus.setVisibility(View.GONE);
        setMySettingCard(mContactJoinLayout, R.string.layout_contact_join, true);
        setMySettingCard(mBecomeTeacherLayout, R.string.layout_become_teacher, false);
    }

    private void setMySettingCard(android.view.View view, int text, boolean isShowLine) {
        TextView textView = (TextView) view.findViewById(R.id.standard_my_text);
        android.view.View line = view.findViewById(R.id.standard_view_line);
        textView.setText(text);
        if (isShowLine) {
            line.setVisibility(android.view.View.VISIBLE);
        } else {
            line.setVisibility(android.view.View.GONE);
        }
    }

    private void setHeadPersonData() {
        if (LikingPreference.isBind()) {//已绑定
            mBodyScoreLayout.setVisibility(android.view.View.VISIBLE);
            mHeadPersonDataTextView.setVisibility(android.view.View.GONE);
            setHeadPersonDataView(mBodyScoreLayout, R.string.body_test_grade, R.string.body_test_grade_unit);
            setHeadPersonDataView(mEverydaySportLayout, R.string.everyday_sport_title, R.string.everyday_sport_unit);
            setHeadPersonDataView(mTrainLayout, R.string.today_train_data, R.string.today_train_data_unit);
        } else {//没有绑定
            mBodyScoreLayout.setVisibility(android.view.View.GONE);
            mHeadPersonDataTextView.setVisibility(android.view.View.VISIBLE);
            setHeadPersonDataView(mEverydaySportLayout, R.string.body_test_grade, R.string.body_test_grade_unit);
            setHeadPersonDataView(mTrainLayout, R.string.today_train_data, R.string.today_train_data_unit);
        }
    }

    /**
     * 设置头部个人数据
     *
     * @param view
     * @param title
     * @param unitText
     */
    private void setHeadPersonDataView(android.view.View view, int title, int unitText) {
        TextView titleTextView = (TextView) view.findViewById(R.id.person_body_title);
        TextView contentTextView = (TextView) view.findViewById(R.id.person_content_data);
        TextView unitTextView = (TextView) view.findViewById(R.id.person_content_data_unit);
        titleTextView.setText(title);
        unitTextView.setText(unitText);
        unitTextView.setTypeface(mTypeface);
        contentTextView.setTypeface(mTypeface);
        switch (view.getId()) {
            case R.id.layout_body_score:
                mBodyScoreData = contentTextView;
                break;
            case R.id.layout_everyday_sport:
                mEveryDataSportData = contentTextView;
                break;
            case R.id.layout_today_data:
                mTrainTimeData = contentTextView;
                break;
        }
    }

    @OnClick({R.id.layout_today_data, R.id.login_text,
            R.id.layout_head_info, R.id.head_image,
            R.id.layout_my_course, R.id.layout_my_order,
            R.id.layout_member_card, R.id.layout_coupons,
            R.id.layout_contact_join, R.id.layout_become_teacher,
            R.id.layout_more, R.id.layout_self_help_group_gym,
            R.id.layout_body_score, R.id.layout_bind_bracelet,
            R.id.layout_water_rate, R.id.layout_everyday_sport})
    public void onClick(android.view.View view) {
        switch (view.getId()) {
            case R.id.layout_today_data://我的训练数据
                if (LikingPreference.isLogin()) {
                    UMengCountUtil.UmengCount(getActivity(), UmengEventId.MYTRAINDATAACTIVITY);
                    Intent intent = new Intent(getActivity(), MyTrainDataActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.login_text://登录
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_head_info://个人信息
            case R.id.head_image:
                if (!LikingPreference.isLogin()) {
                    startActivity(LoginActivity.class);
                } else {
                    UMengCountUtil.UmengCount(getActivity(), UmengEventId.MYINFOACTIVITY);
                    Intent myInfoIntent = new Intent(getActivity(), MyInfoActivity.class);
                    myInfoIntent.putExtra(LoginActivity.KEY_TITLE_SET_USER_INFO, getString(R.string.change_person_info));
                    startActivity(myInfoIntent);
                }
                break;
            case R.id.layout_my_course://我的课程
                if (LikingPreference.isLogin()) {
                    UMengCountUtil.UmengCount(getActivity(), UmengEventId.MYLESSONACTIVITY);
                    Intent myCourseIntent = new Intent(getActivity(), MyLessonActivity.class);
                    startActivity(myCourseIntent);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.layout_my_order://我的订单
                if (LikingPreference.isLogin()) {
                    Intent myOrderIntent = new Intent(getActivity(), MyOrderActivity.class);
                    startActivity(myOrderIntent);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.layout_member_card://会员卡
                if (LikingPreference.isLogin()) {
                    UMengCountUtil.UmengCount(getActivity(), UmengEventId.MYCARDACTIVITY);
                    Intent memberCardIntent = new Intent(getActivity(), MyCardActivity.class);
                    startActivity(memberCardIntent);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.layout_coupons://我的优惠券
                if (LikingPreference.isLogin()) {
                    UMengCountUtil.UmengCount(getActivity(), UmengEventId.COUPONSACTIVITY);
                    Intent couponsIntent = new Intent(getActivity(), CouponsActivity.class);
                    couponsIntent.putExtra(CouponsActivity.TYPE_MY_COUPONS, CouponsActivity.TYPE_MY_COUPONS);
                    startActivity(couponsIntent);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.layout_contact_join://联系加盟
                UMengCountUtil.UmengCount(getActivity(), UmengEventId.CONTACTJONINACTIVITY);
                startActivity(ContactJonInActivity.class);
                break;
            case R.id.layout_become_teacher://成为教练
                UMengCountUtil.UmengCount(getActivity(), UmengEventId.BECOMETEACHERACTIVITY);
                startActivity(BecomeTeacherActivity.class);
                break;
            case R.id.layout_more://更多
                startActivity(MoreActivity.class);
                break;
            case R.id.layout_self_help_group_gym://自助团体课
                UMengCountUtil.UmengCount(getActivity(), UmengEventId.SELFHELPGROUPACTIVITY);
                startActivity(SelfHelpGroupActivity.class);
                break;
            case R.id.layout_body_score:
                if (LikingPreference.isLogin()) {
                    mPresenter.jumpBodyTestActivity(getActivity());
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.layout_bind_bracelet://绑定手环
                mPresenter.jumpBraceletActivity(getActivity(), this,  UUID, mBraceletMac);
                break;
            case R.id.layout_water_rate: //水费充值
                startActivity(WaterRateActivity.class);
                break;
            case R.id.layout_everyday_sport://手环数据
                mPresenter.jumpBracelet(getActivity(), UUID, mBraceletMac);
                break;
        }
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }


    public void onEvent(LoginOutFialureMessage message) {
        setLogonView();
        clearExerciseData();
    }

    public void onEvent(InitApiFinishedMessage message) {
        if (message.isSuccess()) {
            mStateView.setState(StateView.State.SUCCESS);
            setLogonView();
            getUserExerciseData();
        }
    }

    public void onEvent(GymNoticeMessage message) {
        CoursesResult.Courses.Gym mNoticeGym = message.getGym();
        showSelfHelpGroupLayout(mNoticeGym.getCanSchedule());
    }

    public void onEvent(LoginOutMessage loginOutMessage) {
        if (loginOutMessage != null) {
            setMySettingCard(mBindBraceletLinearLayout, R.string.layout_bing_bracelet, true);
            setHeadPersonData();
            setLogonView();
            clearExerciseData();
        }
    }

    /**
     * 是否显示自助团体课
     *
     * @param canschedule
     */
    private void showSelfHelpGroupLayout(int canschedule) {
        if (1 == canschedule) {
            mSelfHelpGroupLayout.setVisibility(android.view.View.VISIBLE);
        } else {
            mSelfHelpGroupLayout.setVisibility(android.view.View.GONE);
        }
    }

    /**
     * 打开蓝牙的回调
     * @param requestCode 1
     * @param resultCode 0 为失败  -1 为成功
     * @param data null
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            switch (resultCode) {
                case 0:
                    break;
                case -1:
                    jumpMyBraceletActivity();
                    break;
            }
        }
    }


    @Override
    public void jumpMyBraceletActivity(){
        UMengCountUtil.UmengCount(getActivity(), UmengEventId.MYBRACELETACTIVITY);
        Intent intent = new Intent(getActivity(), MyBraceletActivity.class);
        if (!StringUtils.isEmpty(mBraceletMac)) {
            intent.putExtra(LikingMyFragment.KEY_MY_BRACELET_MAC, mBraceletMac.toUpperCase());
        }
        intent.putExtra(LikingMyFragment.KEY_UUID, UUID);
        intent.putExtra(MyBraceletActivity.KEY_BRACELET_NAME, "");
        intent.putExtra(MyBraceletActivity.KEY_BRACELET_ADDRESS, "");
        intent.putExtra(MyBraceletActivity.KEY_BRACELET_SOURCE, "LikingMyFragment");
        getActivity().startActivity(intent);
    }


    public void setPresenter() {
        mPresenter = new LikingMyContract.Presenter();
    }
}
