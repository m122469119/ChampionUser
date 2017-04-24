package com.aaron.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2014/11/17 19:44.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class ListUtils {
    /**
     * 判断List是否为null或者size为0
     * @param list List
     * @return true：empty
     */
    public static boolean isEmpty(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    public static <T> void wipeNullElement(List<T> list) {
        List<T> newList = new ArrayList();
        for (T obj : list) {
            if (obj != null) {
                newList.add(obj);
            }
        }
        list.clear();
        list.addAll(newList);
    }
}
