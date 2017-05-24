package com.aaron.http.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Volley RequestQueue单例
 * <p>
 * Created by AaronHuang on 15/6/8.
 *
 * @version 3.0.1
 * @auchor ran.huang
 */
public class VolleyRequestSingleton {
    private static VolleyRequestSingleton sVolleyRequestSingleton;
    private RequestQueue mRequestQueue;
    private static Context sContext;

    public static void init(Context context) {
        if (sVolleyRequestSingleton == null) {
            sVolleyRequestSingleton = new VolleyRequestSingleton(context);
        }
    }

    public static synchronized VolleyRequestSingleton getInstance() {
        return sVolleyRequestSingleton;
    }

    private VolleyRequestSingleton(Context context) {
        sContext = context;
        mRequestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(sContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * 添加一个request到Volley的RequestQueue
     *
     * @param req request
     * @param <T> 请求数据泛型
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}
