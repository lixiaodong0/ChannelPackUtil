package com.iapppay.channel.pack.v2.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * 输入框字符串过滤器
 * 只能输入 字母 + 数字 组合
 */

public class LetterNumberDocument extends PlainDocument {

    public static final String REG = "^[A-Za-z0-9]+$";

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str.matches(REG)) {
            super.insertString(offs, str, a);
        }
    }
}
