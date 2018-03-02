package com.xt.cfp.test.controller;

import jodd.util.StringUtil;


/**
 * Created by yulei on 2015/10/30.
 */
public class Test {



    public static void main(String[] args) {
        String s = "�Ƹ���Ͷ��";
        s = StringUtil.convertCharset(s, "utf8", "iso-8859-1");
        System.out.println(s);
        s = StringUtil.convertCharset(s, "iso-8859-1", "gbk");
        System.out.println(s);
        s = StringUtil.convertCharset(s, "GBK", "UTF-8");
        System.out.println(s);
    }
}
