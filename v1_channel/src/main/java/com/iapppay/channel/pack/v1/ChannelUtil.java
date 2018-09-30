package com.iapppay.channel.pack.v1;

import com.iapppay.channel.pack.v1.config.Config;
import com.iapppay.channel.pack.v1.data.DataSource;
import com.iapppay.channel.pack.v1.interfaces.ChannelCoding;
import com.iapppay.channel.pack.v1.interfaces.HexChannelCodeing;
import com.iapppay.channel.pack.v1.util.Log;
import com.iapppay.channel.pack.v1.util.TextUtil;
import com.iapppay.channel.pack.v1.util.ZipUtil;

import java.io.File;
import java.util.List;

/**
 * 渠道工具
 */

public class ChannelUtil implements ChannelCoding {

    @Override
    public String encode(String channelName) {
        return HexChannelCodeing.INSTANCE.encode(channelName);
    }

    @Override
    public String decode(String channelName) {
        return HexChannelCodeing.INSTANCE.decode(channelName);
    }

    /**
     * 渠道打包
     */
    public void packTask() {
        boolean moreChannel = DataSource.getInstance().isMoreChannel();
        String validity = checkParams();
        if (!TextUtil.isEmpty(validity)) {
            //校验数据失败，中断打包流程
            return;
        }

        if (moreChannel) {
            moreChannel();
        } else {
            singleChannel();
        }
    }

    /**
     * 检查数据的合法性
     *
     * @return 返回空字符串检查通过, 反之失败
     */
    private String checkParams() {
        String failInfo = "";
        DataSource dataSource = DataSource.getInstance();
        File fromFile = dataSource.getFromFile();
        if (!(fromFile != null && fromFile.exists())) {
            return "源文件不存在或是空的";
        }
        List<String> channelName = dataSource.getChannelNames();
        if (DataSource.getInstance().isMoreChannel()) {
            for (String str : channelName) {
                if (TextUtil.isEmpty(str)) {
                    return "渠道标识符不能为空";
                }
            }
        } else {
            if (TextUtil.isEmpty(channelName.get(0))) {
                return "渠道标识符不能为空";
            }
        }
        return failInfo;
    }


    /**
     * 单渠道
     */
    private void singleChannel() {
        String isFail = pack(DataSource.getInstance().getChannelNames().get(0));
        if (!TextUtil.isEmpty(isFail)) {
            //打包成功
        } else {
            //打包失败
        }
    }

    /**
     * 多渠道
     */
    private void moreChannel() {
        DataSource dataSource = DataSource.getInstance();
        List<String> channelNames = dataSource.getChannelNames();
        for (String name : channelNames) {
            String isFail = pack(name);
            if (!TextUtil.isEmpty(isFail)) {
                //name + "_渠道打包失败，已经终止打包流程，请联系技术人员"
            }
        }
        //打包成功
    }


    private String pack(String channelName) {
        DataSource dataSource = DataSource.getInstance();
        //源apk
        File fromFile = dataSource.getFromFile();
        //构建复制的apk文件名称 = (渠道名称)+(_)+(源Apk名称)+(.apk)
        StringBuilder toFileNameBuilder = new StringBuilder();
        toFileNameBuilder.append(channelName)
                .append("_")
                .append(fromFile.getName())
                .append(Config.APK_SUF);
        //渠道apk
        File toFile = new File(fromFile.getParent(), toFileNameBuilder.toString());
        //渠道文件名称 = (META_INF/)+ encode(渠道标识符+渠道名称)
        String channelFileName = Config.META_INF + encode(dataSource.getChannelFlag() + channelName);
        String isFail = ZipUtil.getInstance().markZip(fromFile, toFile, channelFileName);
        if (TextUtil.isEmpty(isFail)) {
            //成功
            Log.v("channel pack success,  path = " + toFile.getAbsolutePath());
        } else {
            //失败
            Log.v("channel pack fail, " + isFail);
        }
        return isFail;
    }
}
