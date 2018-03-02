package com.xt.cfp.core.service.impl.crm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CrmFunction;
import com.xt.cfp.core.pojo.ext.crm.MenusVO;
import com.xt.cfp.core.service.crm.FunctionService;
import com.xt.cfp.core.util.StringUtils;

@Service
public class FunctionServiceImpl implements FunctionService{

	private static Logger logger = Logger.getLogger(FunctionServiceImpl.class);
	
	@Autowired
	private MyBatisDao myBatisDao;
	@Autowired
	private MyBatisDao myBatisDaoRead;
	
	@Override
	public List<MenusVO> findAllMainMenus(Long staffId) {
		List<MenusVO> menus=new ArrayList<MenusVO>();
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("staffId",staffId);
		List<CrmFunction> main=myBatisDaoRead.getList("selectAllMainMenu",param);
		List<CrmFunction> all=myBatisDaoRead.getList("selectStaffMenu",param);
		if(main!=null&&main.size()>0){
			for (CrmFunction crmFunction : main) {
				MenusVO vo=new MenusVO();
				vo.setFunc(crmFunction);
				List<CrmFunction> list=findChildMenus(crmFunction.getFunId());
				List<MenusVO> childs=new ArrayList<MenusVO>();
				if(list!=null&&list.size()>0){
					for (CrmFunction cf : list) {
						//判断员工是否拥有菜单
						if(checkMenu(cf, all)){
							MenusVO child=new MenusVO();
							child.setFunc(cf);
							childs.add(child);
						}
					}
					vo.setFuns(childs);
				}
				menus.add(vo);
			}
		}
		
		return menus;
	}
	
	private boolean checkMenu(CrmFunction cf,List<CrmFunction> all){
		for (CrmFunction func : all) {
			if(func.getFunId().compareTo(cf.getFunId())==0){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<CrmFunction> findChildMenus(Long parentId) {
		List<CrmFunction> menus=myBatisDaoRead.getList("findChildMenus", parentId);
		return menus;
	}
	
	@Override
	public List<CrmFunction> findChildFuncs(Long parentId) {
		List<CrmFunction> menus=myBatisDaoRead.getList("findChildFuncs", parentId);
		return menus;
	}

	@Override
	public CrmFunction findById(Long funId) {
		CrmFunction menu=null;
		if(funId!=null){
			menu=myBatisDaoRead.get("CRM_FUNCTION.selectByPrimaryKey", funId);
		}
		return menu;
	}

	@Transactional
	@Override
	public String saveFuntion(Long funId, Long pid, String code, String name, String type, String url) {
		String result="操作成功";
		CrmFunction func=null;
		if(funId==null||funId==-1){
			CrmFunction codeUrl=myBatisDao.get("CRM_FUNCTION.findCode", code);
			if(codeUrl!=null){
				result="不能有相同的code";
				return result;
			}
		}
		if(funId!=null&&funId!=-1){
			func=myBatisDao.get("CRM_FUNCTION.selectByPrimaryKey", funId);
		}else{
			func=new CrmFunction();
		}
		if(pid!=null&&pid!=0){
			func.setpFunId(pid);
		}
		func.setFunCode(code);
		func.setFunName(name);
		func.setFunType(type);
		func.setUrl(url);
		try {
			if(funId!=null&&funId!=-1){
				myBatisDao.update("CRM_FUNCTION.updateByPrimaryKeySelective", func);
			}else{
				myBatisDao.insert("CRM_FUNCTION.insert", func);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result="添加失败";
		}
		return result;
	}

	@Transactional
	@Override
	public String delFunction(Long funId) {
		String result="操作成功";
		if(funId==null||funId<1){
			result="非法的参数";
			return result;
		}
		List<CrmFunction> childs=findChildFuncs(funId);
		try {
			StringBuffer sb=new StringBuffer("FUN_ID='"+funId+"' or");
			if(childs!=null&&childs.size()>0){
				sb=findChilds(childs, sb);
			}
			String ids=sb.toString();
			ids=ids.substring(0,ids.length()-2);
			Map<String, String> map=new HashMap<String, String>();
			map.put("ids", ids);
			myBatisDao.delete("CRM_FUNCTION.deleteMenus", map);
		} catch (Exception e) {
			e.printStackTrace();
			result="删除失败";
		}
		return result;
	}
	
	private StringBuffer findChilds(List<CrmFunction> list,StringBuffer sb){
		for (CrmFunction fun : list) {
			sb.append(" FUN_ID='"+fun.getFunId()+"' or");
			List<CrmFunction> childs=findChildFuncs(fun.getFunId());
			if(childs!=null&&childs.size()>0){
				findChilds(childs, sb);
			}
		}
		return sb;
	}
	
	@Override
	public List<MenusVO> loadMenus(String funId) {
		List<MenusVO> menus=new ArrayList<MenusVO>();
		List<CrmFunction> main=null;
		if(StringUtils.isNull(funId)){
			main=myBatisDaoRead.getList("selectAllMainMenu");
		}else{
			main=findChildFuncs(Long.parseLong(funId));
		}
		if(main!=null&&main.size()>0){
			for (CrmFunction crmFunction : main) {
				MenusVO vo=new MenusVO();
				vo.setFunc(crmFunction);
				List<CrmFunction> list=findChildFuncs(crmFunction.getFunId());
				List<MenusVO> childs=new ArrayList<MenusVO>();
				if(list!=null&&list.size()>0){
					for (CrmFunction cf : list) {
						MenusVO child=new MenusVO();
						child.setFunc(cf);
						childs.add(child);
					}
					vo.setFuns(childs);
				}
				menus.add(vo);
			}
		}
		return menus;
	}
	
	@Override
	public List<MenusVO> loadFuncs(String funId) {
		List<MenusVO> menus=new ArrayList<MenusVO>();
		List<CrmFunction> main=null;
		if(StringUtils.isNull(funId)){
			main=myBatisDaoRead.getList("selectAllFuncs");
		}else{
			main=findChildFuncs(Long.parseLong(funId));
		}
		if(main!=null&&main.size()>0){
			for (CrmFunction crmFunction : main) {
				MenusVO vo=new MenusVO();
				vo.setFunc(crmFunction);
				List<CrmFunction> list=findChildFuncs(crmFunction.getFunId());
				List<MenusVO> childs=new ArrayList<MenusVO>();
				if(list!=null&&list.size()>0){
					for (CrmFunction cf : list) {
						MenusVO child=new MenusVO();
						child.setFunc(cf);
						childs.add(child);
					}
					vo.setFuns(childs);
				}
				menus.add(vo);
			}
		}
		return menus;
	}
	
	@Override
	public List<CrmFunction> findFuncByRoleId(Long roleId) {
		List<CrmFunction> list=myBatisDaoRead.getList("selectAllFuncByRoleId",roleId);
		/*if(list!=null&&list.size()>0){
			StringBuffer sb=new StringBuffer();
			sb=findChilds(list, sb);
			String ids=sb.toString();
			ids=ids.substring(0,ids.length()-2);
			Map<String, String> map=new HashMap<String, String>();
			map.put("ids", ids);
			list=myBatisDao.getList("CRM_FUNCTION.findMenusByIds", map); 
		}*/
		return list;
	}

	@Override
	public List<CrmFunction> findFuncAndParentFunc(List<CrmFunction> funcs) {
		List<CrmFunction> all=new ArrayList<CrmFunction>();
		for (CrmFunction func : funcs) {
			all.add(func);
			findParents(func, all);
		}
		return all;
	}

	private List<CrmFunction> findParents(CrmFunction func,List<CrmFunction> all){
			CrmFunction parent=findById(func.getpFunId()!=null?func.getpFunId():-1);
			if(parent!=null){
				boolean result=false;
				for (CrmFunction f : all) {
					if(f.getFunId().compareTo(parent.getFunId())==0){
						result=true;
						break;
					}
				}
				if(!result){
					all.add(parent);
				}
				findParents(parent, all);
			}
		return all;
	}

	@Override
	public List<CrmFunction> findAllFuncs() {
		List<CrmFunction> menus=myBatisDaoRead.getList("selectAllFuncs");
		return menus;
	}

}
