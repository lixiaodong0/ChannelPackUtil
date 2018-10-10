package com.iapppay.channel.pack.ui;

import com.iapppay.channel.pack.config.PageConfig;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * 关于页面
 */

public class AboutChannelPage extends JPanel {

    public static AboutChannelPage INSTANCE = null;

    private AboutChannelPage() {
        initView();
    }

    private void initView() {
        setLayout(new BorderLayout());
        JLabel jLabel = new JLabel();
        jLabel.setText(PageConfig.AboutPage.COMPANY_INFO);
        jLabel.setFont(new Font("黑体", Font.PLAIN, 20));
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(jLabel, BorderLayout.CENTER);
    }

    public static AboutChannelPage getInstance() {
        if (INSTANCE == null) {
            synchronized (AboutChannelPage.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AboutChannelPage();
                }
            }
        }
        return INSTANCE;
    }


}
