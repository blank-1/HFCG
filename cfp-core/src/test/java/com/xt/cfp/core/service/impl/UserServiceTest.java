/*
package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.service.UserInfoService;
import junit.framework.TestCase;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.StringWriter;
import java.util.List;

*/
/**
 * Created by luqinglin on 2015/6/12.
 *//*

public class UserServiceTest extends TestCase {

    public static void main(String [] arg){
        System.out.print(System.currentTimeMillis()+","+Math.round(Math.random()*100));
    }

    public UserInfoService userService;

    public VelocityEngine ve;
    @Before
    public void setUp() throws Exception {
        ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");
        userService = (UserInfoService)applicationContext.getBean("userInfoServiceImpl");
        ve =  (VelocityEngine)applicationContext.getBean("velocityEngine");
    }

    @Test
    public void testRegister() throws Exception {

        //测试模版
        //取得velocity的模版
        Template t = ve.getTemplate("vm/registerMessage.vm","utf-8");

        // 取得velocity的上下文context
        VelocityContext context = new VelocityContext();

        // 把数据填入上下文
        context.put("code", "Liang");

        // 输出流
        StringWriter writer = new StringWriter();
        // 转换输出
        t.merge(context, writer);
        // 输出信息
        System.out.println(writer.toString());

        //注册用例

*/
/*        System.out.println("0000000000000");
        UserInfo user = new UserInfo();
        user.setCreateTime(new Date());
        user.setLoginName("test");
  *//*
*/
/*      user.setEmail("luqinglin2010@163.com");
        user.setIdCard("6214830104846372");*//*
*/
/*
        String md5Encode = MD5Util.MD5Encode("111111", "utf-8");
        user.setLoginPass(md5Encode);
        user.setMobileNo("18911710750");
        user.setSource("1");
        user.setType("1");
        user.setStatus("1");
//        UserInfo usered = userService.regist(user);
        UserInfo usered = userService.regist(user);*//*

    }

    @Test
    public void testMobileExist() throws Exception{
        System.out.print(userService.isMobileExist("1891171071"));
    }

    @Test
    public void testIsLoginNameExist() throws Exception{
        System.out.print(userService.isLoginNameExist("luqinglin1"));
    }
    @Test
    public void testUpdatePassword() throws Exception{
        UserInfo user = userService.updatePassword("111111","123456",new Long(1));
    }
    @Test
    public void testGetUserByCondition() throws Exception{
        UserInfo user = new UserInfo();
       */
/* user.setCreateTime(new Date());*//*

        user.setLoginName("luqinglin");
     */
/*   user.setEmail("luqinglin2010@163.com");
        user.setIdCard("6214830104846372");
        user.setIsVerified("1");
        String md5Encode = MD5Util.MD5Encode("111111", "utf-8");
        user.setLoginPass(md5Encode);
        user.setMobileNo("18911710751");
        user.setRecUserId(new Long(11111));
        user.setSource("1");
        user.setType("1");
        user.setStatus("1");*//*

        List<UserInfo> usered = userService.getUserByCondition(user, null);
//        user.setEmail("luqinglin2010@163.com1");
        usered = userService.getUserByCondition(user, null);
//        user.setIdCard("6214830104846372");
        usered = userService.getUserByCondition(user, null);
    }

    @Test
    public void testGetUserPaging() throws Exception{
        UserInfo user = new UserInfo();
       */
/* user.setCreateTime(new Date());*//*

        user.setLoginName("luqinglin");
     */
/*   user.setEmail("luqinglin2010@163.com");
        user.setIdCard("6214830104846372");
        user.setIsVerified("1");
        String md5Encode = MD5Util.MD5Encode("111111", "utf-8");
        user.setLoginPass(md5Encode);
        user.setMobileNo("18911710751");
        user.setRecUserId(new Long(11111));
        user.setSource("1");
        user.setType("1");
        user.setStatus("1");*//*

//        PagingObj<UserInfo> usered = userService.getUserPaging(1,2,user, null);
//        user.setEmail("luqinglin2010@163.com1");
//        usered = userService.getUserPaging(1, 2, user, null);
//        user.setIdCard("6214830104846372");
    }

}
*/
