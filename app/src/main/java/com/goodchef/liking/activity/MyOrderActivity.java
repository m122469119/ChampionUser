package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.RefreshBuyCardMessage;
import com.goodchef.liking.fragment.MyCardOrderFragment;

/**
 * 说明:我的订单
 * Author shaozucheng
 * Time:16/6/28 下午2:41
 */
public class MyOrderActivity extends AppBarActivity {
    public static String KEY_CURRENT_INDEX = "key_current_index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        setTitle(getString(R.string.title_activity_myorder));
        showHomeUpIcon(R.drawable.app_bar_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postEvent(new RefreshBuyCardMessage());
                finish();
            }
        });
        setCouponsFragment();
    }

    private void setCouponsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.my_card_fragment, MyCardOrderFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            postEvent(new RefreshBuyCardMessage());
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
