package com.xt.cfp.core.service.impl;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.constants.DisActivityEnums;
import com.xt.cfp.core.constants.DisActivityEnums.DistributionInviteEnum;
import com.xt.cfp.core.constants.DisActivityEnums.InviteWhiteTabsTypeEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.InviteWhiteTabs;
import com.xt.cfp.core.service.InviteWhiteTabsService;

@Service
@Transactional
public class InviteWhiteTabsServiceImpl implements InviteWhiteTabsService {

	@Autowired
	private MyBatisDao myBatisDao;
	
	@Override
	public void insert(InviteWhiteTabs whitetab) {
		myBatisDao.insert("INVITE_WHITE_TABS.insertSelective", whitetab);
	}
	
	@Override
	public void update(InviteWhiteTabs whitetab) {
		myBatisDao.insert("INVITE_WHITE_TABS.updateByPrimaryKey", whitetab);
	}
	
	@Override
	public InviteWhiteTabs findByUserId(Long userId) {
		return myBatisDao.get("INVITE_WHITE_TABS.selectByUserId", userId);
	}

	@Override
	public int countUserId(Long userId) {
		return myBatisDao.get("INVITE_WHITE_TABS.countUserId", userId);
	}

	@Override
	public void insertImpotAll(Set<Map<String,Object>> userIdSet) {
		for(Map<String,Object> userMap : userIdSet){
			InviteWhiteTabs w = new InviteWhiteTabs();
			Long userId = Long.valueOf(userMap.get("userId").toString());
			String userType = (String)userMap.get("type");
			int count = countUserId(userId);
			if(count > 0){
				continue; 
			}
			w.setUserId(userId);
			w.setType(userType);
			w.setSource(DisActivityEnums.WhiteTabsSourceEnum.SOURCE_IMPORT.getValue());
			insert(w);
		}
	}
	@Override
	public void insertImpotAllByUserId(Set<Long> userIdSet) {
		for(Long userId : userIdSet){
			InviteWhiteTabs w = new InviteWhiteTabs();
			String userType = InviteWhiteTabsTypeEnum.TYPE_IMPORT.getValue();
			int count = countUserId(userId);
			if(count > 0){
				continue; 
			}
			w.setUserId(userId);
			w.setType(userType);
			w.setSource(DisActivityEnums.WhiteTabsSourceEnum.SOURCE_IMPORT.getValue());
			insert(w);
		}
	}

	@Override
	public InviteWhiteTabs findById(Long userId) {
		return myBatisDao.get("INVITE_WHITE_TABS.selectByUserId", userId);
	}
}
