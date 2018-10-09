package com.iapppay.channel.pack.v2.ui;


import com.iapppay.channel.pack.v2.config.PageConfig;
import com.iapppay.channel.pack.v2.config.WindowConfig;
import com.iapppay.channel.pack.v2.util.LoadingDialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * 渠道主页面
 */

public class ChannelMainPage extends JFrame {
    public static ChannelMainPage INSTANCE;

    private ChannelMainPage() {
        LoadingDialog.getInstance().init(this);
        initView();
    }

    private void initView() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.add(PageConfig.WritePage.TITLE, WriteChannelPage.getInstance());
        jTabbedPane.add(PageConfig.ReadPage.TITLE, ReadChannelPage.getInstance());
        jTabbedPane.add(PageConfig.AboutPage.TITLE, AboutChannelPage.getInstance());
        jTabbedPane.setFont(new Font("黑体", Font.PLAIN, 16));
        //设置标题
        setTitle(WindowConfig.TITLE);
        //默认布局为BorderLayout，这里让JTabbedPane居中显示
        add(jTabbedPane, BorderLayout.CENTER);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(screenSize.width / 2 - WindowConfig.WIDTH / 2, screenSize.height / 2 - WindowConfig.HEIGHT / 2, WindowConfig.WIDTH, WindowConfig.HEIGHT);
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
