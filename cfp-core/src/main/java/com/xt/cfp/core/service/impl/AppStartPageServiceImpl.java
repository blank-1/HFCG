package com.xt.cfp.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.AppBanner;
import com.xt.cfp.core.pojo.AppStartPage;
import com.xt.cfp.core.service.AppStartPageService;
import com.xt.cfp.core.util.Pagination;

@Service
public class AppStartPageServiceImpl implements AppStartPageService {
	
	@Autowired
	private MyBatisDao myBatisDao;

	@Override
	public AppStartPage updateAppStartPage(AppStartPage appStartPage) {
		myBatisDao.update("APP_START_PAGE.updateByPrimaryKey", appStartPage);
		return appStartPage;
	}

	@Override
	public AppStartPage getAppStartPageById(Long appStartPageId) {
		return myBatisDao.get("APP_START_PAGE.selectByPrimaryKey", appStartPageId);
	}
	
	@Override
	public AppStartPage getByAppType(String appType) {
		return myBatisDao.get("APP_START_PAGE.getByAppType", appType);
	}
	
	@Override
	public Pagination<AppStartPage> findAllByPage(int pageNo, int pageSize, Map<String, Object> params) {
		Pagination<AppStartPage> pagination = new Pagination<AppStartPage>();
        pagination.setCurrentPage(pageNo);
        pagination.setPageSize(pageSize);
        List<AppStartPage> appStartPages = myBatisDao.getListForPaging("findAppStartPageByPage", params, pageNo, pageSize);
        pagination.setRows(appStartPages);
        pagination.setTotal(myBatisDao.count("findAppStartPageByPage", params));
        return pagination;
	}

	@Override
	public AppBanner addAppBanner(AppBanner appBanner) {
		myBatisDao.insert("APP_BANNER.insert", appBanner);
		return appBanner;
	}

	@Override
	public AppBanner updateAppBanner(AppBanner appBanner) {
		myBatisDao.update("APP_BANNER.updateByPrimaryKey", appBanner);
		return appBanner;
	}

	@Override
	public AppBanner getAppBannerById(Long appBannerId) {
		return myBatisDao.get("APP_BANNER.selectByPrimaryKey", appBannerId);
	}

	@Override
	public List<AppBanner> getAppBannerByList(Map<String, Object> params) {
		return myBatisDao.getList("APP_BANNER.getAppBannerByList", params);
	}

	@Override
	public Pagination<AppBanner> findAppBannerByPage(int pageNo, int pageSize, Map<String, Object> params) {
		Pagination<AppBanner> pagination = new Pagination<AppBanner>();
        pagination.setCurrentPage(pageNo);
        pagination.setPageSize(pageSize);
        List<AppBanner> appBanners = myBatisDao.getListForPaging("findAppBannerByPage", params, pageNo, pageSize);
        pagination.setRows(appBanners);
        pagination.setTotal(myBatisDao.count("findAppBannerByPage", params));
        return pagination;
	}

	@Override
	public void deleteAppBannerById(Long appBannerId) {
		myBatisDao.delete("APP_BANNER.deleteByPrimaryKey", appBannerId);
	}

}
