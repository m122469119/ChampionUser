package com.goodchef.liking.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.data.PlacesData;

import java.util.List;

/**
 * 说明:切换上课地点
 * Author shaozucheng
 * Time:16/8/23 上午11:49
 */
public class ChangeAddressActivity extends AppBarActivity {

    public static final String KEY_ADDRESS_DATA = "key_address_data";
    private RecyclerView mRecyclerView;

    private ChangeAddressAdapter mAddressAdapter;
    private List<PlacesData> placesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);
        setTitle(getString(R.string.activity_title_change_address));
        initView();
        getInItData();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.change_address_recyclerView);

    }

    private void getInItData() {
        Bundle bundle = getIntent().getExtras();
        placesList = bundle.getParcelableArrayList(OrderPrivateCoursesConfirmActivity.KEY_CHANGE_ADDRESS);
        if (placesList !=null && placesList.size()>0){
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAddressAdapter =new ChangeAddressAdapter(this);
            mAddressAdapter.setData(placesList);
            mAddressAdapter.setLayoutOnClickListener(listener);
            mRecyclerView.setAdapter(mAddressAdapter);
        }
    }



    /***
     * 点击切换回去
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.layout_change_address);
            if (layout != null) {
               PlacesData object = (PlacesData) layout.getTag();
                if (object != null) {
                    for (PlacesData dto : placesList) {
                        if (dto.getGymId().equals(object.getGymId())) {
                            dto.setSelect(true);
                        } else {
                            dto.setSelect(false);
                        }
                    }
                    mAddressAdapter.notifyDataSetChanged();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(KEY_ADDRESS_DATA, object);
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        }
    };


    public class ChangeAddressAdapter extends BaseRecycleViewAdapter<ChangeAddressAdapter.ChangeAddressViewHolder, PlacesData> {
        private Context mContext;
        private View.OnClickListener listener;

        public ChangeAddressAdapter(Context context) {
            super(context);
            this.mContext = context;
        }

        public void setLayoutOnClickListener(View.OnClickListener listener) {
            this.listener = listener;
        }

        @Override
        protected ChangeAddressViewHolder createViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_change_address, parent, false);
            return new ChangeAddressViewHolder(view);
        }

        public class ChangeAddressViewHolder extends BaseRecycleViewHolder<PlacesData> {
            TextView mAddressTextView;
            CheckBox mCheckBox;
            RelativeLayout mLayout;

            public ChangeAddressViewHolder(View itemView) {
                super(itemView);
                mAddressTextView = (TextView) itemView.findViewById(R.id.change_address_name);
                mCheckBox = (CheckBox) itemView.findViewById(R.id.change_address_checkBox);
                mLayout = (RelativeLayout) itemView.findViewById(R.id.layout_change_address);
            }

            @Override
            public void bindViews(PlacesData object) {
                boolean isSelect = object.isSelect();
                if (isSelect) {
                    mCheckBox.setChecked(true);
                } else {
                    mCheckBox.setChecked(false);
                }
                if (listener !=null){
                    mLayout.setOnClickListener(listener);
                }
                mLayout.setTag(object);
                mAddressTextView.setText(object.getAddress());
            }
        }
    }


}
