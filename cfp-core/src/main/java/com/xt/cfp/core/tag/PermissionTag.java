package com.xt.cfp.core.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.xt.cfp.core.pojo.AdminInfo;

public class PermissionTag extends SimpleTagSupport {
	private String code;

	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public void doTag() throws JspException, IOException {
		//得到域对象
		PageContext pageContext = (PageContext) this.getJspContext();
		//获取登录用户当前角色对应的权限
		List<String> permissionList= (List<String>) pageContext.getSession().getAttribute(AdminInfo.CURRENTPERMISSION);
		/*String[] permissions = permissionString.split("-");
		List<String> permissionList = Arrays.asList(permissions);*/
		if(permissionList != null && permissionList.size() > 0) {
			//获取标签属性值，判断当前用户的权限是否包含该值
			if(permissionList.contains(code)) {
				this.getJspBody().invoke(null);
			}
		}
		//super.doTag();
	}
}
