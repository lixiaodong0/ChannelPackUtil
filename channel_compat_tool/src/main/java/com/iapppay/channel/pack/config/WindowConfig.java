package com.iapppay.channel.pack.config;

/**
 * 窗口配置
 */
public interface WindowConfig {
    /**
     * 宽度
     */
    int WIDTH = 400;
    /**
     * 高度
     */
    int HEIGHT = 300;

    /**
     * 窗口标题
     */
    String TITLE = "渠道打包工具(兼容V1_V2签名)";


    interface Dialog {
        /**
         * 宽度
         */
        int WIDTH = 100;
        /**
         * 高度
         */
        int HEIGHT = 60;
    }

}
