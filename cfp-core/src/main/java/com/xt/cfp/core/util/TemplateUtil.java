package com.xt.cfp.core.util;

import com.xt.cfp.core.constants.TemplateType;
import com.xt.cfp.core.constants.WebMsgTemplateType;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by luqinglin on 2015/7/2.
 */
public class TemplateUtil {

    /**
     * 根据模版组装文案
     * @param templateType
     * @param context
     * @return
     */
    public static String getStringFromTemplate(TemplateType templateType,VelocityContext context){
        try {
            VelocityEngine velocityEngine = ApplicationContextUtil.getBean("velocityEngine");
            //取得velocity的模版
            String templateName = templateType.getValue();
            String templateSource = "vm/"+templateName+".vm";
            Template t = velocityEngine.getTemplate(templateSource,"utf-8");
            // 输出流
            StringWriter writer = new StringWriter();
            t.merge(context, writer);
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 输出信息
        return null;
    }


    public static void main(String[] args){
        String regex = "^[0-9a-zA-Z]{4,16}+$";
//        String regex = "([a-z]|[A-Z]|[0-9]|[\\u4e00-\\u9fa5])+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher("11f0");
        boolean b = m.matches();
        System.out.print(b);


        System.out.println(StringUtils.getFixLengthUserId("1891171751"));
    }
    
    /**
     * 根据模版组装文案
     * @param templateType
     * @param context
     * @return
     */
    public static String getStringFromTemplate(WebMsgTemplateType templateType,VelocityContext context){
        try {
            VelocityEngine velocityEngine = ApplicationContextUtil.getBean("velocityEngine");
            //取得velocity的模版
            String templateName = templateType.getValue();
            String templateSource = "vm/"+templateName+".vm";
            Template t = velocityEngine.getTemplate(templateSource,"utf-8");
            // 输出流
            StringWriter writer = new StringWriter();
            t.merge(context, writer);
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 输出信息
        return null;
    }

}
