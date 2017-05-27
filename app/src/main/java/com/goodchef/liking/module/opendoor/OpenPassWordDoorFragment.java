package com.goodchef.liking.module.opendoor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.BaseFragment;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.UserAuthCodeResult;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.module.login.LoginActivity;
import com.goodchef.liking.umeng.UmengEventId;
import com.goodchef.liking.utils.NumberConstantUtil;
import com.goodchef.liking.utils.UMengCountUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/6 下午3:38
 */
public class OpenPassWordDoorFragment extends BaseFragment implements View.OnClickListener, OpenDoorContract.OpenDoorView {
    private static final int TYPE_NUM_PASSWORD = 0;
    private static final int TYPE_NUM_BRACELET = 1;
    public static final String NUM = "num";
    @BindView(R.id.open_text)
    TextView mTitleTextView;
    @BindView(R.id.layout_show_password)
    LinearLayout mShowLayout;
    @BindView(R.id.get_into_door_password)
    TextView mGetIntoPasswordBtn;
    @BindView(R.id.get_out_door_password)
    TextView mGetOutPasswordBtn;
    @BindView(R.id.user_code_fail_message)
    TextView mFailMessageTextView;
    @BindView(R.id.layout_get_open_password)
    LinearLayout mOpenDoorLayout;
    @BindView(R.id.user_bracelet)
    ImageView mBraceletImage;

    private int mNum; //页号
    private OpenDoorContract.OpenDoorPresenter mOpenDoorPresenter;

    public static OpenPassWordDoorFragment newInstance(int num) {
        Bundle args = new Bundle();
        OpenPassWordDoorFragment fragment = new OpenPassWordDoorFragment();
        args.putInt(NUM, num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt(NUM) : 1;
        mOpenDoorPresenter = new OpenDoorContract.OpenDoorPresenter(getActivity(), this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open_the_door, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        mFailMessageTextView.setText("");
        return view;
    }

    private void initData() {
        String password = "aaaaaa";
        char[] pwd = password.toCharArray();
        mShowLayout.removeAllViews();
        for (int i = 0; i < pwd.length; i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_item_open_password, mShowLayout, false);
            TextView mTextView = (TextView) view.findViewById(R.id.open_num);
            mTextView.setText(" ");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            mShowLayout.addView(view, params);
        }
    }

    private void initView() {
        if (mNum == TYPE_NUM_PASSWORD) {
            mTitleTextView.setText(R.string.get_open_door_pwd);
            mOpenDoorLayout.setVisibility(View.VISIBLE);
            mBraceletImage.setVisibility(View.GONE);
        } else if (mNum == TYPE_NUM_BRACELET) {
            mTitleTextView.setText(R.string.user_bracelet_open_door);
            mOpenDoorLayout.setVisibility(View.GONE);
            mBraceletImage.setVisibility(View.VISIBLE);
        }
        mGetIntoPasswordBtn.setOnClickListener(this);
        mGetOutPasswordBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mGetIntoPasswordBtn) {
            UMengCountUtil.UmengBtnCount(getActivity(), UmengEventId.GET_INTO_PASSWORD_BUTTON);
            if (LikingPreference.isLogin()) {
                mOpenDoorPresenter.getOpenPwd(NumberConstantUtil.ONE);
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        } else if (v == mGetOutPasswordBtn) {
            UMengCountUtil.UmengBtnCount(getActivity(), UmengEventId.GET_OUT_PASSWORD_BUTTON);
            if (LikingPreference.isLogin()) {
                mOpenDoorPresenter.getOpenPwd(NumberConstantUtil.TWO);
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void updateUserAuthView(UserAuthCodeResult.UserAuthCodeData userAuthCodeData) {
        String password = userAuthCodeData.getAuthCode();
        if (!StringUtils.isEmpty(password)) {
            setPasswordChar(password);
            mFailMessageTextView.setText(userAuthCodeData.getTips().trim());
        }
    }

    @Override
    public void updateFailCodeView(String message) {
        if (mShowLayout != null) {
            initData();
            mFailMessageTextView.setText(message.trim());
        }
    }

    private void setPasswordChar(String password) {
        char[] pwd = password.toCharArray();
        mShowLayout.removeAllViews();
        mFailMessageTextView.setText("");
        for (int i = 0; i < pwd.length; i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_item_open_password, mShowLayout, false);
            TextView mTextView = (TextView) view.findViewById(R.id.open_num);
            mTextView.setText(pwd[i] + "");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            mShowLayout.addView(view, params);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
