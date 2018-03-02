package com.xt.cfp.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author leixiongfei
 * @date 2013-7-11 下午3:59:57
 */
public class BeanConfig {
	
	//对象包含属性
	private List<String> includeMethod = new ArrayList<String>(); 
	
	//对象不包含的属性
	private List<String> excludeMethod = new ArrayList<String>(); 
	
	/**
	 * 添加包含的属性队列
	 * @param properties 属性队列
	 * @return
	 */
	public BeanConfig includeProperty(String... properties) {
		if (properties == null || properties.length < 1) return this;
		for (String property : properties) {
			includeMethod.add(property);
		}
		return this;
	}
	
	/**
	 * 添加不包含的属性队列
	 * @param properties 属性队列
	 * @return
	 */
	public BeanConfig excludeProperty(String... properties) {
		if (properties == null || properties.length < 1) return this;
		for (String property : properties) {
			excludeMethod.add(property);
		}
		return this;
	}

	public List<String> getIncludeMethod() {
		return includeMethod;
	}

	public void setIncludeMethod(List<String> includeMethod) {
		this.includeMethod = includeMethod;
	}

	public List<String> getExcludeMethod() {
		return excludeMethod;
	}

	public void setExcludeMethod(List<String> excludeMethod) {
		this.excludeMethod = excludeMethod;
	}
	
	
}
