package com.aaron.android.framework.base.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.aaron.android.framework.utils.EnvironmentUtils;

/**
 * Created on 15/7/23.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public abstract class AbsPreference {
    private static SharedPreferences sPreferences;
    private static final String TAG = "AbsPreference";

    public static void init(Context context){
        if (sPreferences == null) {
            sPreferences = context.getSharedPreferences(EnvironmentUtils.Config.getAppFlag(), Context.MODE_PRIVATE);
        }
    }

    /**
     * 初始化SharedPreferences
     */
    protected static SharedPreferences getInstance() {
        return sPreferences;
    }

    /**
     * 清空SharedPreferences
     */
    public static void clear() {
        getInstance().edit().clear().commit();
    }

    /**
     * 根据Key移除SharedPreferences
     * @param key key
     */
    public static void remove(String key) {
        getInstance().edit().remove(key).commit();
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key    Key
     * @param object 需要存的引用对象
     * @return 数据是否成功存储
     */
    protected static boolean setObject(String key, Object object) {
        if (object == null) {
            return false;
        }
        String type = object.getClass().getSimpleName();
        SharedPreferences.Editor editor = getInstance().edit();
        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        return editor.commit(); //为保证后面取值的正确性,这里使用同步存储(线程阻塞)commit方法
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key           Key
     * @param defaultObject 默认值
     * @return 取值
     */
    protected static Object getObject(String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        if ("String".equals(type)) {
            return getInstance().getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return getInstance().getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return getInstance().getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return getInstance().getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return getInstance().getLong(key, (Long) defaultObject);
        }

        return null;
    }
}
