package com.goodchef.liking.module.card.my;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.aaron.android.framework.base.ui.actionbar.AppBarSwipeBackActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.BuyCardSuccessMessage;
import com.goodchef.liking.module.card.buy.LikingBuyCardFragment;

/**
 * 说明:升级卡或者续卡
 * Author shaozucheng
 * Time:16/6/30 上午10:02
 */
public class UpgradeAndContinueCardActivity extends AppBarSwipeBackActivity{


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_upgrade_and_continue);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.upgrade_contains, LikingBuyCardFragment.newInstance())
                .commit();
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(BuyCardSuccessMessage message) {
        if (message != null) {
            finish();
        }
    }
}
