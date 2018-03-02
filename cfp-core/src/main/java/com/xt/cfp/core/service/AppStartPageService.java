package com.xt.cfp.core.service;

import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.AppBanner;
import com.xt.cfp.core.pojo.AppStartPage;
import com.xt.cfp.core.util.Pagination;

public interface AppStartPageService {
	
	/**
	 * 修改启动页信息
	 */
	AppStartPage updateAppStartPage(AppStartPage appStartPage);
	
	/**
	 * 根据ID加载一条信息
	 * @param appStartPageId 启动页ID
	 */
	AppStartPage getAppStartPageById(Long appStartPageId);
	
	/**
	 * 根据app类型获取启动页数据
	 * @param appType app类型
	 * @return
	 */
	AppStartPage getByAppType(String appType);
	
	/**
	 * 分页列表
	 * @param pageNo 页码
	 * @param pageSize 页数
	 * @param params 查询条件
	 */
    Pagination<AppStartPage> findAllByPage(int pageNo, int pageSize, Map<String, Object> params);
    
    
    /**
     * 添加 APP Banner
     */
    AppBanner addAppBanner(AppBanner appBanner);
    
    /**
     * 修改 APP Banner
     */
    AppBanner updateAppBanner(AppBanner appBanner);
    
    /**
     * 根据ID记载一条信息
     * @param appBannerId 
     */
    AppBanner getAppBannerById(Long appBannerId);
    
    /**
     * 根据APP类型获取列表
     * @param params 查询条件
     */
    List<AppBanner> getAppBannerByList(Map<String, Object> params);
    
    /**
     * 获取APP Banner 分页列表
     * @param pageNo 页码
     * @param pageSize 页数
     * @param params 查询条件
     */
    Pagination<AppBanner> findAppBannerByPage(int pageNo, int pageSize, Map<String, Object> params);
    
    /**
     * 根据Id删除一条数据
     * @param appBannerId
     */
    void deleteAppBannerById(Long appBannerId);

}
