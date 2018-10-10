package com.iapppay.channel.v1;

/**
 * Created by Administrator on 2018/10/10.
 */

public interface Config {
    /**
     * META_INF 文件夹名称
     */
    String META_INF_DIR = "META-INF/";
    /**
     * 渠道文件前缀开头，用于判断文件是否是渠道文件
     */
    String CHANNEL_FILE_INITIALLY = "iu1";
    /**
     * 完整的渠道文件前缀 = (META-INF/iu1)
     */
    String COMPLETE_CHANNEL_FILE_INITIALLY = META_INF_DIR + CHANNEL_FILE_INITIALLY;
}
