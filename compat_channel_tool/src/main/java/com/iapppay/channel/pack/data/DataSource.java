package com.iapppay.channel.pack.data;

import java.io.File;
import java.util.List;

/**
 * 数据资源类
 * 主要记录用户界面输入的信息
 */

public class DataSource {
    private static DataSource INSTANCE = null;

    //源文件
    private File fromFile;
    //多渠道标识符，默认为 false 表示单渠道
    private boolean isMoreChannel;
    //渠道名称  一个或者多个
    private List<String> channelNames;
    //渠道标识符
    private String channelFlag = "ia1";

    public static final DataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (DataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataSource();
                }
            }
        }
        return INSTANCE;
    }

    public File getFromFile() {
        return fromFile;
    }

    public void setFromFile(File fromFile) {
        this.fromFile = fromFile;
    }

    public boolean isMoreChannel() {
        return isMoreChannel;
    }

    public void setMoreChannel(boolean moreChannel) {
        isMoreChannel = moreChannel;
    }

    public List<String> getChannelNames() {
        return channelNames;
    }

    public void setChannelNames(List<String> channelNames) {
        this.channelNames = channelNames;
    }

    public String getChannelFlag() {
        return channelFlag;
    }

    public void setChannelFlag(String channelFlag) {
        this.channelFlag = channelFlag;
    }

}
