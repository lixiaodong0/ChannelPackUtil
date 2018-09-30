package com.iapppay.channel.pack.v1.ui;

import com.iapppay.channel.pack.v1.config.Config;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 关于页面
 */

public class AboutChannelPage extends JPanel {

    public static AboutChannelPage INSTANCE = null;

    private AboutChannelPage() {
        initView();
    }

    private void initView() {
        setBounds(0, 0, Config.WIDTH, Config.HEIGHT);
        JLabel jLabel = new JLabel();
        jLabel.setText("Github: https://github.com/lixiaodong0");
        jLabel.setFont(new Font(null, Font.PLAIN, 25));
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
