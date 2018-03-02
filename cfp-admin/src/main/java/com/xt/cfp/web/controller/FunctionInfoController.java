package com.xt.cfp.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.pojo.FunctionInfo;
import com.xt.cfp.core.pojo.FunctionUrl;
import com.xt.cfp.core.pojo.RoleFunction;
import com.xt.cfp.core.pojo.RoleInfo;
import com.xt.cfp.core.service.FunctionInfoService;
import com.xt.cfp.core.service.RoleFunctionService;
import com.xt.cfp.core.service.RoleInfoService;
import com.xt.cfp.core.util.JsonUtil;

@Controller
@RequestMapping("/jsp/system/function")
public class FunctionInfoController extends BaseController {
	
	@Autowired
	private FunctionInfoService functionInfoService;

	@Autowired
	private RoleFunctionService roleFunctionService;
	
	@Autowired 
	private RoleInfoService roleInfoService ;
	
    /**
     * 跳转到：权限-列表
     */
    @RequestMapping(value = "/to_function_list")
    public ModelAndView to_function_list() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/system/function/function_list");
        return mv;
    }
    
    /**
     * 执行：树列表
     */
	@RequestMapping("/tree")
	@ResponseBody
	public Object tree() {
		List<FunctionInfo> functionInfos = functionInfoService.findAllFunction();
		Map pMap = new HashMap();
		List list = new ArrayList();
		for(FunctionInfo functionInfo : functionInfos) {
			String id = functionInfo.getFunctionId() + "";
			String text = functionInfo.getFunctionName();
			String parentId = functionInfo.getpFunctionId() + "";
				Map map = new HashMap();
				map.put("id", id);
				map.put("name", text);
				if(functionInfo.getpFunctionId() != 0) {
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
	@RequestMapping(value="/to_function_add")
	public ModelAndView to_function_add(
		@RequestParam("editingId") Long editingId,
		@RequestParam("editingPid") Long editingPid,
		@RequestParam("flag") String flag) {
		
		FunctionInfo info = functionInfoService.getFunctionById(editingId);
		FunctionInfo pInfo = functionInfoService.getFunctionById(editingPid);
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("add", true);
		mv.addObject("flag", flag);
		mv.addObject("functionId", editingId);
		mv.addObject("pFunctionId", editingPid);
		mv.addObject("functionCode", info.getFunctionCode());
		
		//显示节点信息用
		String childName = "";
		String parentName = "";
		if(editingId != 0) {
			childName = info.getFunctionName();
			if(editingPid != 0) {
				parentName = pInfo.getFunctionName();
			}
		}
		mv.addObject("childName", childName);
		mv.addObject("parentName", parentName);
		
		mv.setViewName("jsp/system/function/function_add");
		return mv;
	}
	
	/**
	 * 执行：保存操作
	 * @param addFlag true添加；false编辑
	 * @param flag 1同级操作
	 * @param Url  2子级操作
	 * @param functionInfo 权限对象
	 */
	@RequestMapping(value="/saveFunction", method=RequestMethod.POST)
	@ResponseBody
	public Object saveFunction(@ModelAttribute("addFlag") Boolean addFlag,
			@ModelAttribute("flag") String flag,
			@ModelAttribute("Url") String Url,
			@ModelAttribute("functionInfo") FunctionInfo functionInfo) {
		
		Map map = new HashMap();
		
		if("".equals(functionInfo.getFunctionName())) {
			map.put("name", "null");
			return map;
		}
		
		//权限Url
		List<JSONObject> urlJsonObjects = JsonUtil.getListFromPageJSON(Url);
		if(!urlJsonObjects.isEmpty()) {
			//Url是否重复
			List urlInfos = new ArrayList();
			for (JSONObject jsonObject : urlJsonObjects) {
				urlInfos.add(jsonObject.getString("urlInfo"));
			}
			
			for (int i = 0; i < urlInfos.size() - 1; i++) {
		         for (int j = urlInfos.size() - 1; j > i; j--) {
		              if (urlInfos.get(j).equals(urlInfos.get(i))) {
		            	  map.put("equal", true);
		            	  return map;
		              }
		        }
		    }
		}
		
		String functionCode = functionInfo.getFunctionCode();
		long functionId = functionInfo.getFunctionId();
		long pFunctionId = functionInfo.getpFunctionId();
		
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
			List<FunctionInfo> functionInfos = new ArrayList();
			//根据当前id，判断是否有子节点，并构造新的子节点code
			//if(pId != 0) {
			functionInfos = functionInfoService.getFunctionByPId(pId);
			//}
			String newCode;
			if(functionInfos.size() > 0) {
				List<Long> childCodes = new ArrayList();
				for(int i=0; i<=functionInfos.size()-1; i++) {
					childCodes.add(Long.parseLong(functionInfos.get(i).getFunctionCode()));
				}
				Collections.sort(childCodes);
				
				long lastChildCode = childCodes.get(childCodes.size()-1);	//得到子节点code最后一位的code
				long newCodeLong = lastChildCode+1;
				newCode = "0" + newCodeLong;
			}else {
				newCode = functionCode + "01";
			}
			
			functionInfo.setFunctionCode(newCode);
			functionInfo.setpFunctionId(pId);
			try {
				functionInfoService.addFunctionInfo(functionInfo,urlJsonObjects);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("error", true);
				return map;
			}
			map.put("success", true);
			map.put("newId", functionInfoService.getFunctionByCode(functionInfo.getFunctionCode()).getFunctionId());
		}else {
			//更新权限信息
			try {
				functionInfoService.updateFunctionInfo(functionInfo,urlJsonObjects);
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
	@RequestMapping(value="/to_function_show")
	public ModelAndView showFunction(@RequestParam("functionId") long functionId) {
		ModelAndView modelAndView = new ModelAndView();
		FunctionInfo functionInfo = functionInfoService.getFunctionById(functionId);
		
		//显示节点信息用
		String childName = "";
		String parentName = "";
		if(functionId != 0) {
			childName = functionInfo.getFunctionName();
			if(functionInfo.getpFunctionId() != 0) {
				parentName = functionInfoService.getFunctionById(functionInfo.getpFunctionId()).getFunctionName();
			}
		}
		modelAndView.addObject("childName", childName);
		modelAndView.addObject("parentName", parentName);
		
		modelAndView.addObject(functionInfo);
		modelAndView.setViewName("jsp/system/function/function_show");
		return modelAndView;
	}
	
	/**
	 * 执行：根据权限ID获取URL列表
	 * @param functionId 权限ID
	 */
	@RequestMapping(value="/showUrls")
	@ResponseBody
	public Object showUrls(@ModelAttribute("functionId") long functionId) {
		List<FunctionUrl> functionUrls = functionInfoService.getUrlByFunctionId(functionId);
		return functionUrls;
	}
	
	/**
	 * 执行：权限-编辑
	 * @param editingId 权限ID
	 */
	@RequestMapping(value="/editFunction")
	public Object editfunction(@RequestParam("editingId") long editingId) {
		ModelAndView mv = new ModelAndView();
		FunctionInfo functionInfo = functionInfoService.getFunctionById(editingId);
		//显示节点信息用
		String childName = "";
		String parentName = "";
		if(editingId != 0) {
			childName = functionInfo.getFunctionName();
			if(functionInfo.getpFunctionId() != 0) {
				parentName = functionInfoService.getFunctionById(functionInfo.getpFunctionId()).getFunctionName();
			}
		}
		mv.addObject("childName", childName);
		mv.addObject("parentName", parentName);
		
		mv.addObject(functionInfo);
		mv.addObject("add", false);
		mv.addObject("flag", "");
		mv.addObject("functionCode", functionInfo.getFunctionCode());
		mv.addObject("functionId", functionInfo.getFunctionId());
		mv.addObject("pFunctionId", functionInfo.getpFunctionId());
		mv.setViewName("jsp/system/function/function_add");
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
		RoleInfo role = roleInfoService.getRoleById(roleId);
		mv.addObject("roleName", role.getRoleName());
		//将角色已有的权限传到页面选中
		List<RoleFunction> roleFunctions = roleFunctionService.getRoleFunctionByRoleId(roleId);
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
		List<FunctionInfo> functionInfos = functionInfoService.findAllFunction();
		
		//封装该角色对应的functionId
		List<RoleFunction> roleFunctions = roleFunctionService.getRoleFunctionByRoleId(roleId);
		List<Long> functionIds = new ArrayList<Long>();
		for(RoleFunction roleFunction : roleFunctions) {
			functionIds.add(roleFunction.getFunctionId());
		}
		
		Map pMap = new HashMap();
		List list = new ArrayList();
		for(FunctionInfo functionInfo : functionInfos) {
			String id = functionInfo.getFunctionId() + "";
			String text = functionInfo.getFunctionName();
			String parentId = functionInfo.getpFunctionId() + "";
				Map map = new HashMap();
				map.put("id", id);
				map.put("name", text);
				if(functionInfo.getpFunctionId() != 0) {
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
