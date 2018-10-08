package com.iapppay.channel.pack.v1;

import com.iapppay.channel.pack.v1.interfaces.ChannelCoding;

/**
 * 十六进制渠道编码
 */

public class HexChannelCodeing implements ChannelCoding {
    public static final HexChannelCodeing INSTANCE = new HexChannelCodeing();
    @Override
    public String encode(String channelName) {
        char[] channelByte = new char[channelName.length() * 2];
        char[] org = channelName.toCharArray();

        char[] arr_index = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        for (int i = 0; i < org.length; i++) {
            channelByte[i * 2] = arr_index[org[i] & 0x0f];
            channelByte[i * 2 + 1] = arr_index[(org[i] & 0xf0) >> 4];
        }
        return new String(channelByte);
    }

    @Override
    public String decode(String channelName) {
        char[] channelByte = new char[channelName.length() / 2];
        char[] org = channelName.toCharArray();

        for (int i = 0; i < org.length; i += 2) {
            if (org[i] >= '0' && org[i] <= '9')
                org[i] = (char) (org[i] - '0');
            else if (org[i] >= 'a' && org[i] <= 'f')
                org[i] = (char) (org[i] - 'a' + 10);

            if (org[i + 1] >= '0' && org[i + 1] <= '9')
                org[i + 1] = (char) (org[i + 1] - '0');
            else if (org[i + 1] >= 'a' && org[i + 1] <= 'f')
                org[i + 1] = (char) (org[i + 1] - 'a' + 10);

            channelByte[i / 2] = (char) (org[i] + (org[i + 1] << 4));
        }
        return new String(channelByte);
    }
}
