package com.aaron.http.statistics;


import com.aaron.http.code.RequestError;

/**
 * Created on 16/7/21.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public interface NetworkStatistics {
    void post(RequestError error);
}
