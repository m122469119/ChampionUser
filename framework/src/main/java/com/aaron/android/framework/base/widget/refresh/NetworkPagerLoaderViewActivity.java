package com.aaron.android.framework.base.widget.refresh;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.aaron.android.framework.R;
import com.aaron.android.framework.base.actionbar.AppBarActivity;

/**
 * Created on 16/2/23.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public abstract class NetworkPagerLoaderViewActivity<T extends BasePagerLoaderViewFragment> extends AppBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_pagerloader);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = createNetworkPagerLoaderFragment(this);
        if (fragment == null) {
            throw new NullPointerException("NetworkPagerLoader Fragment is null, must be use createNetworkPagerLoaderFragment to create Fragment!");
        }
        fragmentTransaction.add(R.id.layout_network_pager_loader_content, fragment);
        fragmentTransaction.commit();
    }

    public abstract T createNetworkPagerLoaderFragment(Context context);


}
