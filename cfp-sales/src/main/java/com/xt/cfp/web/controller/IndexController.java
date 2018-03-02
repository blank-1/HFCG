package com.xt.cfp.web.controller;

import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.util.MD5Util;
import com.xt.cfp.web.annotation.DoNotNeedLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequestMapping("")
public class IndexController extends BaseController {
	
	@Autowired
	private SalesAdminInfoService salesAdminInfoService;
	
	@Autowired
	private SalesRoleInfoService salesRoleInfoService;

	@Autowired
	private SalesFunctionInfoService salesFunctionInfoService;

	
    /**
     * 首页
     */
    @RequestMapping(value = "/index")
    public ModelAndView index() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }
	/**
     * 登陆页
     */
    @RequestMapping(value = "/toLogin")
	@DoNotNeedLogin
    public String  toLogin() {
        return "login";
    }
    
    /**
     * 执行：登录 
     */
	@DoNotNeedLogin
    @RequestMapping(value="/login", method = RequestMethod.POST)
	public Object login(HttpServletRequest request, HttpSession session,
						String loginName,
						String loginPwd) {
		
		ModelAndView mv = new ModelAndView();
		//判断用户名密码是否含有敏感字符
		String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"  
                + "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";  
		Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE); 
		if (sqlPattern.matcher(loginName).find() || sqlPattern.matcher(loginPwd).find()) {
			mv.addObject("loginName", loginName);
			mv.addObject("loginPwd", loginPwd);
			mv.addObject("message", "用户名或密码存在非法字符！");	//该用户状态为无效，不可以登录系统！
			return mv;  
	    }
		
		//获取登录用户信息
		SalesAdminInfo adminInfo = new SalesAdminInfo();
		SalesAdminInfo sessionAdminInfo = (SalesAdminInfo) session.getAttribute(Constants.USER_ID_IN_SESSION);
		List<SalesAdminInfo> adminInfos = salesAdminInfoService.getAdminByAdminCode(loginName);
		if(sessionAdminInfo == null||!sessionAdminInfo.getAdminCode().equals(loginName)) {
			if(adminInfos.size() > 0) {
				adminInfo = adminInfos.get(0);
			}
		}else {
			adminInfo = sessionAdminInfo;
		}
		//////////////////////////////////////
		List<String> permissionStr = new ArrayList<String>();	//默认角色具有的权限集
		if(adminInfo.getAdminCode() != null) {

				if(adminInfo.getAdminState() == adminInfo.STATE_DISABLE) {
					mv.addObject("loginName", loginName);
					mv.addObject("loginPwd", loginPwd);
					mv.addObject("message", "该用户已禁用，不可以登录系统！");	//该用户已禁用，不可以登录系统！
					return mv;
				}else {
					String md5Encode = MD5Util.MD5Encode(loginPwd, "utf-8");// 将密码MD5处理。
					if(md5Encode.equals(adminInfo.getLoginPwd())) {
						//登录成功
						session.setAttribute(Constants.USER_ID_IN_SESSION, adminInfo);	//记录当前登录用户信息
						
						// 获取员工对用的角色集合
						List<SalesRoleInfo> roleInfos = salesRoleInfoService.getRoleByAdminId(adminInfo.getAdminId());

						if(null != roleInfos){
							for (SalesRoleInfo roleInfo : roleInfos) {
								//获取角色对应的权限Code,记录角色具有的权限集
								List<SalesFunctionInfo> functionInfos = salesFunctionInfoService.getFunctionByRoleId(roleInfo.getRoleId());
								if(null != functionInfos){
									for(SalesFunctionInfo functionInfo : functionInfos) {
										permissionStr.add(functionInfo.getFunctionCode());
									}
								}
							}
						}
						
						//默认角色对应的权限Code,记录默认角色具有的权限集
						session.setAttribute(AdminInfo.CURRENTPERMISSION, permissionStr);
						
					}else {
						mv.addObject("loginName", loginName);
						mv.addObject("loginPwd", loginPwd);
						mv.addObject("message", "工号或密码错误！");	//密码错误！
						return mv;
					}
				}
		}else {
			mv.addObject("loginName", loginName);
			mv.addObject("loginPwd", loginPwd);
			mv.addObject("message", "工号或密码错误！");	//用户名错误！
			return mv;
		}
		return "redirect:/index";
	}

    /**
     * 执行：登出
     */
	@DoNotNeedLogin
	@RequestMapping(value="/logout", method = RequestMethod.POST)
	@ResponseBody
	public Object logout(HttpServletRequest request, HttpSession session) {
		session.invalidate();
		return "success";
	}
}
