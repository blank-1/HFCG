package com.xt.cfp.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.util.JsonUtil;
import com.xt.cfp.core.util.MD5Util;

@Controller
@RequestMapping("/jsp/system/admin")
public class SalesAdminInfoController extends BaseController {

	@Autowired
	private SalesAdminInfoService salesAdminInfoService;

	@Autowired
	private SalesAdminRoleService salesAdminRoleService;
	@Autowired
	private SalesOrganizeInfoService salesOrganizeInfoService;

	@Autowired
	private UserInfoService userInfoService;

	@RequestMapping(value = "/initSystemUser")
	public ModelAndView initSystemUser() {
		ModelAndView mv = new ModelAndView();
		userInfoService.createSystemUser();

		return mv;
	}
	
    /**
     * 跳转到：员工-列表
     */
    @RequestMapping(value = "/to_admin_list")
    public ModelAndView to_admin_list() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/system/admin/admin_list");
        return mv;
    }
    
    /**
     * 跳转到：员工-添加
     */
    @RequestMapping(value = "/to_admin_add")
    public ModelAndView to_admin_add() {
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("add", true);
    	mv.addObject("adminId", -1);
        mv.setViewName("jsp/system/admin/admin_add");
        return mv;
    }
    
    /**
     * 跳转到：员工-编辑
     * @param adminId 员工ID
     */
	@RequestMapping(value="/to_admin_edit")
	public ModelAndView to_admin_edit(@ModelAttribute("adminId") long adminId) {
		ModelAndView mv = new ModelAndView();
		SalesAdminInfo adminInfo = salesAdminInfoService.getAdminById(adminId);
		mv.addObject("add", false);
		mv.addObject("adminId", adminId);
		mv.addObject("adminInfo", adminInfo);

		SalesAdminOrganizetion adminOrganizetion = salesOrganizeInfoService.getOrganizetionInfobyAdminId(adminId);
		mv.addObject("adminOrganizetion", adminOrganizetion);

		mv.setViewName("jsp/system/admin/admin_add");
		return mv;
	}
    
    /**
     * 跳转到：员工-详情
     * @param adminId 员工ID
     */
    @RequestMapping(value = "/to_admin_show")
    public ModelAndView to_admin_show(@RequestParam(value = "adminId") Long adminId) {
    	ModelAndView mv = new ModelAndView();
		SalesAdminInfo adminInfo = salesAdminInfoService.getAdminById(adminId);
		mv.addObject("adminInfo",adminInfo);
        mv.setViewName("jsp/system/admin/admin_show");
        return mv;
    }
    
    /**
     * 执行：分页列表
     * @param pageSize 页数
     * @param pageNo 页码
     */
	@RequestMapping(value = "/list")
    @ResponseBody
    public Object list(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
			@RequestParam(value = "page", defaultValue = "1") int pageNo) throws Exception {
		
		// 填充参数
        Map params = new HashMap();
        return salesAdminInfoService.findAllByPage(pageNo, pageSize, params);
    }
	
	// 执行添加（测试用）
	@RequestMapping("/add")
	@ResponseBody
	public Object add() {
		try {
			SalesAdminInfo info = new SalesAdminInfo();
			info.setDisplayName("员工011");
			info.setAdminCode("88011");
			info.setLoginName("haoyuangong011");
			info.setLoginPwd("123456");
			info.setCreateTime(new Date());
			info.setAdminState(SalesAdminInfo.STATE_ENABLE);
			info.setTelephone("13901236011");
			info.setEmail("011@gmail.com");
			info.setAdminIdCode("110102198201024011");
			info.setUpdateTime(new Date());
			salesAdminInfoService.addAdmin(info);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	/**
	 * 执行：保存（添加+编辑）
	 * @param addFlag true添加；false编辑
	 * @param adminInfo 员工对象
	 * @param departmentInfo 角色信息
	 */
	@RequestMapping(value="/saveAdmin")
	@ResponseBody
	public Object saveAdmin(@ModelAttribute("addFlag") Boolean addFlag,
			@ModelAttribute("adminInfo") SalesAdminInfo adminInfo,
			@ModelAttribute("organizeId") Long organizeId,
			@ModelAttribute("departmentInfo") String departmentInfo) {
		
		List<JSONObject> departmentJsonObjects = JsonUtil.getListFromPageJSON(departmentInfo);

		// 角色去重复
		if(!departmentJsonObjects.isEmpty()) {
			for (int i = 0; i < departmentJsonObjects.size() - 1; i++) {
		         for (int j = departmentJsonObjects.size() - 1; j > i; j--) {
	        		  if(departmentJsonObjects.get(j).equals(departmentJsonObjects.get(i))) {
	        			  departmentJsonObjects.remove(j);
	        		  }
		        }
		    }
		}
		
		if(addFlag) {// add
			try {
				String md5Encode = MD5Util.MD5Encode(AdminInfo.DEFAULT_PASSWORD, "utf-8");// 将默认密码MD5处理。
				//自动生成工号
				inputAdminCode(adminInfo, organizeId);

				adminInfo.setLoginPwd(md5Encode);
		        if(!departmentJsonObjects.isEmpty()) {
		        	for (JSONObject jsonObject : departmentJsonObjects) {
						salesAdminInfoService.addAdminInfo(adminInfo, jsonObject.getLong("roleId"),organizeId);
		        	}
		        }else {
					salesAdminInfoService.addAdminInfo(adminInfo, null,organizeId);
		        }
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
		}else {// update
			long adminId = adminInfo.getAdminId();
			SalesAdminOrganizetion adminOrganizetion = salesOrganizeInfoService.getOrganizetionInfobyAdminId(adminId);
			if (adminOrganizetion!=null){
				if (!adminOrganizetion.getOrganizeId().toString().equals(organizeId+"")){
					adminOrganizetion.setOrganizeId(organizeId);
					inputAdminCode(adminInfo, organizeId);
					salesOrganizeInfoService.updateAdminOrganizetion(adminOrganizetion);
				}
			}
			// 根据员工ID加载一条数据
			SalesAdminInfo info = salesAdminInfoService.getAdminById(adminId);

			//adminInfo.setLoginName(info.getLoginName());
			adminInfo.setCreateTime(info.getCreateTime());
			adminInfo.setUpdateTime(new Date());
			
			try {
				salesAdminInfoService.updateAdminInfo(adminInfo);
				
				List<SalesAdminRole> adminRoles = salesAdminRoleService.getAdminRoleByAdminId(adminInfo.getAdminId());
				
				if(!departmentJsonObjects.isEmpty()) {
					if(adminRoles.size() != 0) {
						//判断departmentJsonObjects中的数据只是修改，还是有增删操作
						List<Long> formUserRoleIds = new ArrayList();	//表单提交过来数据的userRoleId
						for(JSONObject jsonObject : departmentJsonObjects) {
							formUserRoleIds.add(jsonObject.getLong("userRoleId"));
						}
						for(SalesAdminRole adminRole : adminRoles) {
							long saveUserRoleId = adminRole.getAdminRoleId();
							if(!formUserRoleIds.contains(saveUserRoleId)) {
								salesAdminRoleService.deleteAdminRole(adminRole);
							}
						}
					}
					
		        	for (JSONObject jsonObject : departmentJsonObjects) {
			            
			            Long userRoleId = jsonObject.getLong("userRoleId");
			            if(userRoleId == null) {	//新加的数据
			            	adminInfo.setAdminId(adminId);
							salesAdminInfoService.addAdminInfo(adminInfo, jsonObject.getLong("roleId"),null);
			            }else {
							salesAdminInfoService.updateRoleInfo(adminInfo,userRoleId,jsonObject.getLong("roleId"));
			            }
			        }
		        }else {	//表单中没有选择数据
		        	if(adminRoles.size() != 0) {	//之前保存了数据，所以执行删除
		        		for(SalesAdminRole adminRole : adminRoles) {
							salesAdminRoleService.deleteAdminRole(adminRole);
		        		}
		        	}
		        	//之前无数据不做任何操作
		        }

			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
		}
		return "success";
	}

	private void inputAdminCode(@ModelAttribute("adminInfo") SalesAdminInfo adminInfo, @ModelAttribute("organizeId") Long organizeId) {
		SalesOrganizationInfo organize = salesOrganizeInfoService.getOrganizeById(organizeId);
		int userCount = salesOrganizeInfoService.getUserCountByOrganizeId(organizeId);
		adminInfo.setAdminCode(organize.getOrganizeCode()+(userCount+1));
	}

	/**
     * 跳转到：员工-修改密码
     */
    @RequestMapping(value = "/to_edit_pass")
    public ModelAndView to_edit_pass() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/system/admin/edit_pass");
        return mv;
    }
    
    /**
     * 执行：保存密码
     * @param login_old 原密码
     * @param login_new 新密码
     */
    @RequestMapping("/editPass")
    @ResponseBody
    public Object editPass(HttpServletRequest request, HttpSession session,
                           @RequestParam(value = "login_old", required = false) String login_old,
                           @RequestParam(value = "login_new", required = false) String login_new) {
        try {
            // 合法性验证
        	if (null == login_old || "".equals(login_old)) {
                return returnResultMap(false, null, "check", "原密码不能为空");
            }
            if (null == login_new || "".equals(login_new)) {
                return returnResultMap(false, null, "check", "新密码不能为空");
            }
            
            // 获取当前登录用户
            SalesAdminInfo sessionAdminInfo = (SalesAdminInfo) session.getAttribute(Constants.USER_ID_IN_SESSION);
            // 根据用户ID，加载一条数据
			SalesAdminInfo adminInfo = salesAdminInfoService.getAdminById(sessionAdminInfo.getAdminId());
            
            // 赋值
            String md5_login_old = MD5Util.MD5Encode(login_old.trim(), "utf-8");
            if (!adminInfo.getLoginPwd().equals(md5_login_old)) {
                return returnResultMap(false, null, "check", "原密码不正确");
            }
            String md5_login_new = MD5Util.MD5Encode(login_new.trim(), "utf-8");
            adminInfo.setLoginPwd(md5_login_new);
            
            // 更改操作
			salesAdminInfoService.updateAdminInfo(adminInfo);
            
            // 覆盖用户登录信息
            session.setAttribute(Constants.USER_ID_IN_SESSION, adminInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }
    
    /**
     * 执行：重置员工密码
     * @param adminId 员工ID
     */
    @RequestMapping("/changePwd")
    @ResponseBody
    public Object changePwd(HttpServletRequest request, HttpSession session,
                           @RequestParam(value = "adminId", required = false) String adminId) {
        try {
            // 合法性验证
            if (null == adminId || "".equals(adminId)) {
                return returnResultMap(false, null, "check", "员工信息不能为空");
            }
            
            // 根据用户ID，加载一条数据
            SalesAdminInfo adminInfo = salesAdminInfoService.getAdminById(Long.valueOf(adminId));
            if(null == adminInfo){
            	return returnResultMap(false, null, "check", "员工信息不存在");
            }
            
            // 赋值
            String md5_login_pwd = MD5Util.MD5Encode(AdminInfo.DEFAULT_PASSWORD, "utf-8");
            adminInfo.setLoginPwd(md5_login_pwd);
            
            // 更改操作
			salesAdminInfoService.updateAdminInfo(adminInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }
    
}
