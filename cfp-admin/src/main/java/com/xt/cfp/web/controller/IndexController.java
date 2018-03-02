package com.xt.cfp.web.controller;

import com.xt.cfp.core.pojo.AdminInfo;
import com.xt.cfp.core.pojo.FunctionInfo;
import com.xt.cfp.core.pojo.RoleInfo;
import com.xt.cfp.core.service.AdminInfoService;
import com.xt.cfp.core.service.FunctionInfoService;
import com.xt.cfp.core.service.RoleInfoService;
import com.xt.cfp.core.util.AccessLogUtil;
import com.xt.cfp.core.util.MD5Util;
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
	private AdminInfoService adminInfoService;
	
	@Autowired
	private RoleInfoService roleInfoService;
	
	@Autowired
	private FunctionInfoService functionInfoService;
	
    /**
     * 首页
     */
    @RequestMapping(value = "index")
    public ModelAndView index() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }
	/**
     * 登陆页
     */
    @RequestMapping(value = "toLogin")
    public String  toLogin() {
        return "login";
    }
    
    /**
     * 执行：登录 
     */
    @RequestMapping(value="/login", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request, HttpSession session,
						@ModelAttribute("loginName") String loginName,
						@ModelAttribute("loginPwd") String loginPwd) {
		
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
		AdminInfo adminInfo = new AdminInfo();
		AdminInfo sessionAdminInfo = (AdminInfo) session.getAttribute(AdminInfo.LOGINUSER);
		List<AdminInfo> adminInfos = adminInfoService.getAdminByLoginName(loginName);
		if(sessionAdminInfo == null) {
			if(adminInfos.size() > 0) {
				adminInfo = adminInfos.get(0);
			}
		}else {
			adminInfo = sessionAdminInfo;
		}
		
		List<String> permissionStr = new ArrayList<String>();	//默认角色具有的权限集
		if(adminInfo.getAdminCode() != null) {

				if(adminInfo.getAdminState() == adminInfo.STATE_DISABLE) {
					mv.addObject("loginName", loginName);
					mv.addObject("loginPwd", loginPwd);
					mv.addObject("message", "用户名或密码错误！");	//该用户已禁用，不可以登录系统！
					return mv;
				}else {
					String md5Encode = MD5Util.MD5Encode(loginPwd, "utf-8");// 将密码MD5处理。
					if(md5Encode.equals(adminInfo.getLoginPwd())) {
						//登录成功
						session.setAttribute(AdminInfo.LOGINUSER, adminInfo);	//记录当前登录用户信息
						request.setAttribute(AccessLogUtil.SESSION_USER_ID_KEY,adminInfo.getAdminId());
						
						// 获取员工对用的角色集合
						List<RoleInfo> roleInfos = roleInfoService.getRoleByAdminId(adminInfo.getAdminId());

						if(null != roleInfos){
							for (RoleInfo roleInfo : roleInfos) {
								//获取角色对应的权限Code,记录角色具有的权限集
								List<FunctionInfo> functionInfos = functionInfoService.getFunctionByRoleId(roleInfo.getRoleId());
								if(null != functionInfos){
									for(FunctionInfo functionInfo : functionInfos) {
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
						mv.addObject("message", "用户名或密码错误！");	//密码错误！
						return mv;
					}
				}
		}else {
			mv.addObject("loginName", loginName);
			mv.addObject("loginPwd", loginPwd);
			mv.addObject("message", "用户名或密码错误！");	//用户名错误！
			return mv;
		}
		
		mv.setViewName("index");
		return mv;
	}
    
    /**
     * 执行：登出
     */
	@RequestMapping(value="/logout", method = RequestMethod.POST)
	@ResponseBody
	public Object logout(HttpServletRequest request, HttpSession session) {
		session.invalidate();
		return "success";
	}
}
