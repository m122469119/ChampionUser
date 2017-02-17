package com.aaron.android.framework.base.eventbus;

import java.io.Serializable;

/**
 * Created on 16/1/28.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public abstract class BaseMessage implements Serializable {
    public int what;
    public int arg1;
    public int arg2;
    public String msg1;
    public String msg2;

    public Object obj1;
    public Object obj2;

    public BaseMessage(){}

    public BaseMessage(int what){
        this.what = what;
    }
}
