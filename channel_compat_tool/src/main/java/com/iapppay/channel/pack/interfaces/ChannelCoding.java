package com.iapppay.channel.pack.interfaces;

/**
 * 渠道编码接口
 * 可拓展加密解密
 */
public interface ChannelCoding {

    /**
     * 对渠道名称进行编码
     * @param channelName 渠道名称
     */
    String encode(String channelName);

    /**
     * 对渠道名称进行解码
     * @param channelName 渠道名称
     */
    String decode(String channelName);
}
