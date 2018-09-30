package com.iapppay.channel.pack.v1.util;

/**
 * Created by Administrator on 2018/9/30.
 */

public class Log {

    public static final void v(String tag, String msg) {
        System.out.println(tag + "_" + msg);
    }

    public static final void v(String msg) {
        v("", msg);
    }
}
