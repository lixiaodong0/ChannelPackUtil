package com.iapppay.channel.pack.v1.ui;

import com.iapppay.channel.pack.v1.config.Config;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * 渠道主页面
 */

public class ChannelMainPage extends JFrame {
    public static ChannelMainPage INSTANCE;

    private ChannelMainPage() {
        initView();
    }

    private void initView() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.add("打渠道", null);
        jTabbedPane.add("读渠道", null);
        jTabbedPane.add("关于", AboutChannelPage.getInstance());
        //设置标题
        setTitle("渠道打包工具");
        //默认布局为BorderLayout，这里让JTabbedPane居中显示
        add(jTabbedPane, BorderLayout.CENTER);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(screenSize.width / 2 - Config.WIDTH / 2, screenSize.height / 2 - Config.HEIGHT / 2, Config.WIDTH, Config.HEIGHT);
        //可见
        setVisible(true);
        //退出关闭程序
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static ChannelMainPage getInstance() {
        if (INSTANCE == null) {
            synchronized (ChannelMainPage.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ChannelMainPage();
                }
            }
        }
        return INSTANCE;
    }
}
