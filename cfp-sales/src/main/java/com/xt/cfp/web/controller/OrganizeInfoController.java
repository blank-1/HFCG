package com.xt.cfp.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.pojo.SalesFunctionInfo;
import com.xt.cfp.core.pojo.SalesOrganizationInfo;
import com.xt.cfp.core.pojo.SalesRoleFunction;
import com.xt.cfp.core.service.SalesFunctionInfoService;
import com.xt.cfp.core.service.SalesOrganizeInfoService;
import com.xt.cfp.core.service.SalesRoleFunctionService;
import com.xt.cfp.core.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping("/jsp/system/organize")
public class OrganizeInfoController extends BaseController {
	
	@Autowired
	private SalesOrganizeInfoService salesOrganizeInfoService;

	@Autowired
	private SalesRoleFunctionService salesRoleFunctionService;
	
    /**
     * 跳转到：权限-列表
     */
    @RequestMapping(value = "/to_organize_list")
    public ModelAndView to_function_list() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/system/organize/organize_list");
        return mv;
    }
    
    /**
     * 执行：树列表
     */
	@RequestMapping("/tree")
	@ResponseBody
	public Object tree() {
		List<SalesOrganizationInfo> salesOrganizationInfos = salesOrganizeInfoService.findAllSalesOrganizetionInfo();
		Map pMap = new HashMap();
		List list = new ArrayList();
		for(SalesOrganizationInfo salesOrganizationInfo : salesOrganizationInfos) {
			String id = salesOrganizationInfo.getOrganizeId() + "";
			String text = salesOrganizationInfo.getOrganizeName();
			String parentId = salesOrganizationInfo.getParentId() + "";
				Map map = new HashMap();
				map.put("id", id);
				map.put("name", text);
				if(salesOrganizationInfo.getParentId() != 0) {
					map.put("_parentId", parentId);
				}
				list.add(map);
		}
		pMap.put("rows",list);
		return pMap;
	}
	
	/**
	 * 跳转到：权限-添加
	 * @param editingId 操作节点ID
	 * @param editingPid 操作节点父ID
	 * @param flag 1添加同级；2添加子级
	 */
	@RequestMapping(value="/to_organize_add")
	public ModelAndView to_function_add(
		@RequestParam("editingId") Long editingId,
		@RequestParam("editingPid") Long editingPid,
		@RequestParam("flag") String flag) {

		SalesOrganizationInfo info = salesOrganizeInfoService.getOrganizeById(editingId);
		SalesOrganizationInfo pInfo = salesOrganizeInfoService.getOrganizeById(editingPid);
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("add", true);
		mv.addObject("flag", flag);
		mv.addObject("functionId", editingId);
		mv.addObject("pFunctionId", editingPid);
		mv.addObject("functionCode", info.getOrganizeCode());
		
		//显示节点信息用
		String childName = "";
		String parentName = "";
		if(editingId != 0) {
			childName = info.getOrganizeName();
			if(editingPid != 0) {
				parentName = pInfo.getOrganizeName();
			}
		}
		mv.addObject("childName", childName);
		mv.addObject("parentName", parentName);

		long functionId = editingId;
		long pFunctionId = editingPid;

		if("".equals(pFunctionId)) {	//添加第一个节点时
			pFunctionId = 0L;
		}

		long pId = 0;
		if("1".equals(flag)) {
			pId = pFunctionId;
		}else if("2".equals(flag)) {
			pId = functionId;
		}
		List<SalesOrganizationInfo> functionInfos = new ArrayList();
		//根据当前id，判断是否有子节点，并构造新的子节点code
		//if(pId != 0) {
		functionInfos = salesOrganizeInfoService.getOrganizeByPId(pId);
		String newCode;
		if(functionInfos.size() > 0) {
			List<Long> childCodes = new ArrayList();
			for(int i=0; i<=functionInfos.size()-1; i++) {
				childCodes.add(Long.parseLong(functionInfos.get(i).getOrganizeCode()));
			}
			Collections.sort(childCodes);

			long lastChildCode = childCodes.get(childCodes.size()-1);	//得到子节点code最后一位的code
			long newCodeLong = lastChildCode+1;
			if (newCodeLong>=10)
				newCode = info.getOrganizeCode() + newCodeLong;
			else
				newCode = info.getOrganizeCode()+"0" + newCodeLong;
		}else {
			newCode = info.getOrganizeCode() + "01";
		}

		mv.addObject("code", newCode);
		mv.setViewName("jsp/system/organize/organize_add");
		return mv;
	}
	
	/**
	 * 执行：保存操作
	 * @param addFlag true添加；false编辑
	 * @param flag 1同级操作
	 * @param Url  2子级操作
	 * @param functionInfo 权限对象
	 */
	@RequestMapping(value="/saveOrganize", method=RequestMethod.POST)
	@ResponseBody
	public Object saveFunction(@ModelAttribute("addFlag") Boolean addFlag,
			@ModelAttribute("flag") String flag,
			@ModelAttribute("functionInfo") SalesOrganizationInfo functionInfo) {
		
		Map map = new HashMap();
		
		if("".equals(functionInfo.getOrganizeName())) {
			map.put("name", "null");
			return map;
		}
		

		String functionCode = functionInfo.getOrganizeCode();
		long functionId = functionInfo.getOrganizeId();
		long pFunctionId = functionInfo.getParentId();
		
		if("".equals(pFunctionId)) {	//添加第一个节点时
			pFunctionId = 0L;
		}
		
		long pId = 0;
		if("1".equals(flag)) {
			pId = pFunctionId;
		}else if("2".equals(flag)) {
			pId = functionId;
		}
		
		if(addFlag) {
			List<SalesOrganizationInfo> functionInfos = new ArrayList();
			//根据当前id，判断是否有子节点，并构造新的子节点code
			//if(pId != 0) {
			functionInfos = salesOrganizeInfoService.getOrganizeByPId(pId);
			//}
			functionInfo.setParentId(pId);
			try {
				salesOrganizeInfoService.addFunctionInfo(functionInfo);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("error", true);
				return map;
			}
			map.put("success", true);
			map.put("newId", salesOrganizeInfoService.getOrganizeByCode(functionInfo.getOrganizeCode()).getOrganizeId());
		}else {
			//更新权限信息
			try {
				salesOrganizeInfoService.updateOrganizeInfo(functionInfo);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("error", true);
				return map;
			}
			map.put("success", true);
		}
		return map;
	}
	
	/**
	 * 跳转到：权限-详细
	 * @param functionId 权限ID
	 */
	@RequestMapping(value="/to_organize_show")
	public ModelAndView showOrganize(@RequestParam("functionId") long functionId) {
		ModelAndView modelAndView = new ModelAndView();
		SalesOrganizationInfo functionInfo = salesOrganizeInfoService.getOrganizeById(functionId);
		SalesOrganizationInfo pInfo = salesOrganizeInfoService.getOrganizeById(functionInfo.getParentId());

		//显示节点信息用
		String childName = "";
		String parentName = "";
		if(functionId != 0) {
			childName = functionInfo.getOrganizeName();
			if(functionInfo.getParentId()!= 0) {
				parentName = pInfo.getOrganizeName();
			}
		}

		modelAndView.addObject("childName", childName);
		modelAndView.addObject("parentName", parentName);
		
		modelAndView.addObject("functionInfo", functionInfo);
		modelAndView.setViewName("jsp/system/organize/organize_show");
		return modelAndView;
	}

	
	/**
	 * 执行：权限-编辑
	 * @param editingId 权限ID
	 */
	@RequestMapping(value="/editOrganize")
	public Object editOrganize(@RequestParam("editingId") long editingId) {
		ModelAndView mv = new ModelAndView();
		SalesOrganizationInfo functionInfo = salesOrganizeInfoService.getOrganizeById(editingId);
		//显示节点信息用
		String childName = "";
		String parentName = "";
		if(editingId != 0) {
			childName = functionInfo.getOrganizeName();
			if(functionInfo.getParentId() != 0) {
				parentName = salesOrganizeInfoService.getOrganizeById(functionInfo.getParentId()).getOrganizeName();
			}
		}
		mv.addObject("childName", childName);
		mv.addObject("parentName", parentName);
		
		mv.addObject(functionInfo);
		mv.addObject("add", false);
		mv.addObject("flag", "");
		mv.addObject("functionId", functionInfo.getOrganizeId());
		mv.addObject("pFunctionId", functionInfo.getParentId());
		mv.addObject("code", functionInfo.getOrganizeCode());
		mv.addObject("functionInfo", functionInfo);
		mv.setViewName("jsp/system/organize/organize_add");
		return mv;
	}
	
	/**
	 * 执行：权限-编辑
	 * @param roleId 角色ID
	 * @param roleName 角色名称
	 */
	@RequestMapping("/showFunctionTree")
	@ResponseBody
	public Object showFunctionTree(@ModelAttribute("roleId") long roleId,
			@ModelAttribute("roleName") String roleName){
		ModelAndView mv = new ModelAndView();
		mv.addObject("roleId", roleId);
		mv.addObject("roleName", roleName);
		
		//将角色已有的权限传到页面选中
		List<SalesRoleFunction> roleFunctions = salesRoleFunctionService.getRoleFunctionByRoleId(roleId);
		StringBuffer sBuffer = new StringBuffer();
		for(int i=0; i<roleFunctions.size(); i++) {
			sBuffer.append(roleFunctions.get(i).getFunctionId());
			if(i != roleFunctions.size() - 1) {
				sBuffer.append("-");
			}
		}
		String functionIds = sBuffer.toString();
		mv.addObject("functionIds", functionIds);
		mv.setViewName("jsp/system/function/function_tree");
		return mv;
	}
	
	/**
	 * 执行：角色授权页(显示权限树，添加默认选中)
	 * @param roleId 角色ID
	 */
	@RequestMapping("/functionTree")
	@ResponseBody
	public Object functionTree(@ModelAttribute("roleId") long roleId) {
		List<SalesOrganizationInfo> functionInfos = salesOrganizeInfoService.findAllSalesOrganizetionInfo();
		
		//封装该角色对应的functionId
		List<SalesRoleFunction> roleFunctions = salesRoleFunctionService.getRoleFunctionByRoleId(roleId);
		List<Long> functionIds = new ArrayList<Long>();
		for(SalesRoleFunction roleFunction : roleFunctions) {
			functionIds.add(roleFunction.getFunctionId());
		}
		
		Map pMap = new HashMap();
		List list = new ArrayList();
		for(SalesOrganizationInfo functionInfo : functionInfos) {
			String id = functionInfo.getOrganizeId() + "";
			String text = functionInfo.getOrganizeName();
			String parentId = functionInfo.getParentId() + "";
				Map map = new HashMap();
				map.put("id", id);
				map.put("name", text);
				if(functionInfo.getOrganizeId() != 0) {
					map.put("_parentId", parentId);
				}
				/*if(functionIds.contains(id)) {	//默认情况下需要关闭的节点
					map.put("state", "closed");
				}*/
				list.add(map);
		}
		pMap.put("rows",list);
		return pMap;
	}
	
}
