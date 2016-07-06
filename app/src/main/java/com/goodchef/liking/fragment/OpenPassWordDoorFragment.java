package com.goodchef.liking.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.BaseFragment;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/6 下午3:38
 */
public class OpenPassWordDoorFragment extends BaseFragment implements View.OnClickListener{
    private static final int TYPE_NUM_PASSWORD = 0;
    private static final int TYPE_NUM_BRACELET = 1;
    private int mNum; //页号
    private LinearLayout mOpenDoorLayout;
    private ImageView mBraceletImage;
    private TextView mTitleTextView;
    private TextView mGetIntoPasswordBtn;
    private TextView mGetOutPasswordBtn;
    private LinearLayout mShowLayout;
    private String password = "666666";


    public static OpenPassWordDoorFragment newInstance(int num) {
        Bundle args = new Bundle();
        OpenPassWordDoorFragment fragment = new OpenPassWordDoorFragment();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open_the_door, container, false);
        mTitleTextView = (TextView) view.findViewById(R.id.open_text);
        mOpenDoorLayout = (LinearLayout) view.findViewById(R.id.layout_get_open_password);
        mBraceletImage = (ImageView) view.findViewById(R.id.user_bracelet);
        mGetIntoPasswordBtn = (TextView) view.findViewById(R.id.get_into_door_password);
        mGetOutPasswordBtn = (TextView) view.findViewById(R.id.get_out_door_password);
        mShowLayout = (LinearLayout) view.findViewById(R.id.layout_show_password);
        initView();
        initData();
        return view;
    }

    private void initView() {
        if (mNum == TYPE_NUM_PASSWORD) {
            mTitleTextView.setText("获取密码开们");
            mOpenDoorLayout.setVisibility(View.VISIBLE);
            mBraceletImage.setVisibility(View.GONE);
        } else if (mNum == TYPE_NUM_BRACELET) {
            mTitleTextView.setText("使用手环开门");
            mOpenDoorLayout.setVisibility(View.GONE);
            mBraceletImage.setVisibility(View.VISIBLE);
        }
        mGetIntoPasswordBtn.setOnClickListener(this);
        mGetOutPasswordBtn.setOnClickListener(this);
    }

    private void initData() {
        getPasswordChar(password);
    }

    private void getPasswordChar(String password) {
        char[] pwd = password.toCharArray();
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
    public void onClick(View v) {
        if (v == mGetIntoPasswordBtn){
            PopupUtils.showToast("获取进门密码");
        }else if (v == mGetOutPasswordBtn){
            PopupUtils.showToast("获取开门密码");
        }
    }
}
