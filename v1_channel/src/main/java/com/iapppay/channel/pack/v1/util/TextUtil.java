package com.iapppay.channel.pack.v1.util;

/**
 * Created by Administrator on 2018/9/30.
 */

public class TextUtil {

    public static final boolean isEmpty(String str) {
        return str == null || (str != null && str.length() <= 0);
    }
}
