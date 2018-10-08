package com.iapppay.channel.pack.v1.ui;

import com.iapppay.channel.pack.v1.config.Config;

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
        jLabel.setText(Config.COMPANY_INFO);
        jLabel.setFont(new Font(null, Font.PLAIN, 25));
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
