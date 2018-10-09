package com.iapppay.channel.pack.v2;

import com.iapppay.channel.pack.v2.ui.ChannelMainPage;

import java.awt.Font;

import javax.swing.UIManager;



public class MainClass {

    public static void main(String[] args) {
        UIManager.put("Button.font", new Font("黑体", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("黑体", Font.PLAIN, 14));
        UIManager.put("RadioButton.font", new Font("黑体", Font.PLAIN, 14));
        ChannelMainPage.getInstance();
    }
}
