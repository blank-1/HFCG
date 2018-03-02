package com.xt.cfp.web.controller;

import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/jsp/system/role")
public class RoleInfoController extends BaseController {
	
	@Autowired
	private SalesRoleInfoService salesRoleInfoService;
	
	@Autowired
	private SalesFunctionInfoService salesFunctionInfoService;

	@Autowired
	private SalesRoleFunctionService salesRoleFunctionService;
	
    /**
     * 跳转到：角色-列表
     */
    @RequestMapping(value = "/to_role_list")
    public ModelAndView to_role_list() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/system/role/role_list");
        return mv;
    }
    
    /**
     * 跳转到：角色-添加
     */
    @RequestMapping(value = "/to_role_add")
    public ModelAndView to_role_add() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/system/role/role_add");
        return mv;
    }
    
    /**
     * 跳转到：角色-编辑
     * @param roleId 角色ID
     */
    @RequestMapping(value = "/to_role_edit")
    public ModelAndView to_role_edit(@RequestParam(value = "roleId", required = false) Long roleId) {
    	ModelAndView mv = new ModelAndView();
		SalesRoleInfo info = salesRoleInfoService.getRoleById(roleId);
    	mv.addObject("info", info);
        mv.setViewName("jsp/system/role/role_edit");
        return mv;
    }
    
    /**
     * 执行：分页列表
     * @param pageSize 页数
     * @param pageNo 页码
     */
	@RequestMapping(value = "/list")
    @ResponseBody
    public Object adminListByPage(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
			@RequestParam(value = "page", defaultValue = "1") int pageNo) throws Exception {
		
		// 填充参数
        Map params = new HashMap();
        
        return salesRoleInfoService.findAllByPage(pageNo, pageSize, params);
    }
    
	/**
	 * 执行：添加
	 * @param roleName 角色名称
	 * @param roleCode 角色编码
	 * @param roleDesc 角色描述
	 */
 	@RequestMapping("/add")
 	@ResponseBody
 	public Object add(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "roleName", required = false) String roleName,
    		@RequestParam(value = "roleCode", required = false) String roleCode,
			@RequestParam(value = "roleDesc", required = false) String roleDesc) {
 		try {
			SalesRoleInfo info = new SalesRoleInfo();
 			info.setRoleName(roleName);
 			info.setRoleCode(roleCode);
 			info.setRoleDesc(roleDesc);
			salesRoleInfoService.addRole(info);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		return "success";
 	}
 	
 	/**
 	 * 执行：编辑
 	 * @param roleId 角色ID
 	 * @param roleName 角色名称
 	 * @param roleCode 角色编码
 	 * @param roleDesc 角色描述
 	 */
 	@RequestMapping("/edit")
 	@ResponseBody
 	public Object edit(HttpServletRequest request, HttpSession session,
 			@RequestParam(value = "roleId", required = false) Long roleId,
    		@RequestParam(value = "roleName", required = false) String roleName,
    		@RequestParam(value = "roleCode", required = false) String roleCode,
			@RequestParam(value = "roleDesc", required = false) String roleDesc) {
 		try {
			SalesRoleInfo info = salesRoleInfoService.getRoleById(roleId);
 			info.setRoleName(roleName);
 			info.setRoleCode(roleCode);
 			info.setRoleDesc(roleDesc);
			salesRoleInfoService.editRole(info);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		return "success";
 	}
 	
 	/**
 	 * 执行：授权保存
 	 * @param functionIdStr 权限ID集合
 	 * @param roleId 角色ID
 	 */
  	@RequestMapping(value="/saveRoleFunction")
 	@ResponseBody
 	public Object saveRoleFunction(HttpSession session, 
 			@ModelAttribute("functionIdStr") String functionIdStr, 
 			@ModelAttribute("roleId") long roleId) {
 			try {
 				SalesRoleFunction roleFunction = new SalesRoleFunction();
 				roleFunction.setRoleId(roleId);
				salesRoleFunctionService.addRoleFunction(roleFunction,functionIdStr);
 			} catch (Exception e) {
 				e.printStackTrace();
 				return "error";
 			}
 			
 			/*RoleInfo sessionRole = super.getCurrentRole();
 			if(null != sessionRole){
 				if(roleId == sessionRole.getRoleId()) {
 	 				//当前用户当前角色对应的权限集
 	 				RoleInfo roleInfo = (RoleInfo)super.getSession().getAttribute(AdminInfo.CURRENTROLE);
 	 				List<SalesFunctionInfo> functionInfos = salesFunctionInfoService.getFunctionByRoleId(roleInfo.getRoleId());
 	 				List<String> adminUrls = new ArrayList<String>();
 	 				if(!functionInfos.isEmpty()) {
 	 					for(FunctionInfo functionInfo : functionInfos) {
 	 						long functionId = functionInfo.getFunctionId();
 	 						List<FunctionUrl> functionUrls = salesFunctionInfoService.getUrlByFunctionId(functionId);
 	 						if(!functionUrls.isEmpty()) {
 	 							for(FunctionUrl functionUrl : functionUrls) {
 	 								String urlInfo = functionUrl.getUrlInfo();
 	 								if(!adminUrls.contains(urlInfo) && !"".equals(urlInfo)) {
 	 									adminUrls.add(urlInfo);
 	 								}
 	 							}
 	 						}
 	 					}
 	 				}
 	 				session.setAttribute(AdminInfo.USERFUNCTIONURL, adminUrls);
 	 			}
 			}*/

 			return "success";
 	}
  	
  	/**
  	 * 执行：展示员工角色列表
  	 * @param adminId 员工ID
  	 */
	@RequestMapping(value="/showAdminRole")
	@ResponseBody
	public Object showAdminRole(@ModelAttribute("adminId") long adminId) {
		List<SalesRoleInfo> roleInfos = salesRoleInfoService.getRoleByAdminId(adminId);
		return roleInfos;
	}
 	
	/**
	 * 执行：获取角色名称列表
	 */
	@RequestMapping(value="/findRoleNames")
	@ResponseBody
	public Object findRolesName() {
		List<SalesRoleInfo> roleInfos = salesRoleInfoService.getAllRole();
		List roleNames = new ArrayList();
		for(int i=0; i<roleInfos.size(); i++) {
			Map map = new HashMap();
			map.put("id", roleInfos.get(i).getRoleId());
			map.put("text", roleInfos.get(i).getRoleName());
			roleNames.add(map);
		}
		return roleNames;
	}
}
