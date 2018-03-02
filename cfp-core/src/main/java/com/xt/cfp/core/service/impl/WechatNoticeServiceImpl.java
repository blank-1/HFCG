package com.xt.cfp.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.WechatNoticeConstants.WechatNoticeStateEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.WechatNotice;
import com.xt.cfp.core.service.WechatNoticeService;
import com.xt.cfp.core.util.Pagination;

@Service
public class WechatNoticeServiceImpl implements WechatNoticeService{
	
	private static final Logger logger = Logger.getLogger(WechatNoticeServiceImpl.class);

	@Autowired
	private MyBatisDao myBatisDao;

	@Override
	public void addWechatNotice(WechatNotice wechatNotice) {
		logger.info(wechatNotice.getAdminId()+"发布微信公告："+wechatNotice.getNoticeTitle());
		myBatisDao.insert("WECHAT_NOTICE.insert", wechatNotice);
	}

	@Override
	public void updateWechatNotice(WechatNotice wechatNotice) {
		logger.info(wechatNotice.getAdminId()+"修改微信公告："+wechatNotice.getNoticeTitle());
		myBatisDao.update("WECHAT_NOTICE.updateByPrimaryKeySelective", wechatNotice);
	}

	@Override
	public Pagination<WechatNotice> getWechatNoticePaging(int pageNo, int pageSize, WechatNotice wechatNotice, Map<String, Object> customParams) {
		Pagination<WechatNotice> page = new Pagination<WechatNotice>();
		page.setCurrentPage(pageNo);
		page.setPageSize(pageSize);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("wechatNotice", wechatNotice);
		params.put("customParams", customParams);
		int totalCount = this.myBatisDao.count("getWechatNoticePaging", params);
		List<WechatNotice> list = this.myBatisDao.getListForPaging("getWechatNoticePaging", params, pageNo, pageSize);
		page.setTotal(totalCount);
		page.setRows(list);
		return page;
	}

	@Override
	public WechatNotice getWechatNoticeById(Long noticeId) {
		return myBatisDao.get("WECHAT_NOTICE.selectByPrimaryKey", noticeId);
	}

	@Override
	public void changeWechatNoticeState(Long noticeId, WechatNoticeStateEnum wnse) {
		//判断参数是否为null
		if (noticeId == null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("noticeId", "null");

		if (wnse == null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("wnse", "null");
		
		WechatNotice wechatNotice = this.getWechatNoticeById(noticeId);
		if(wechatNotice == null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("wechatNotice", "null");

		//修改状态
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("noticeId", noticeId);
		params.put("noticeState", wnse.getValue());
		logger.info(wechatNotice.getNoticeId()+"微信公告状态被修改为："+wnse.getDesc());
		myBatisDao.update("WECHAT_NOTICE.changeWechatNoticeState", params);
	}

	@Override
	public WechatNotice getTopNewWechatNotice() {
		return myBatisDao.get("WECHAT_NOTICE.getTopNewWechatNotice", null);
	}
	
}
