package com.xt.cfp.core.service.impl;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.constants.DisActivityEnums;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.WhiteTabs;
import com.xt.cfp.core.service.WhiteTabsService;

@Service
@Transactional
public class WhiteTabsServiceImpl implements WhiteTabsService {

	@Autowired
	private MyBatisDao myBatisDao;
	
	@Override
	public void insert(WhiteTabs whitetab) {
		myBatisDao.insert("WHITE_TABS.insertSelective", whitetab);
	}

	@Override
	public WhiteTabs findByUserId(Long userId) {
		return myBatisDao.get("WHITE_TABS.selectByUserId", userId);
	}

	@Override
	public int countUserId(Long userId) {
		return myBatisDao.get("WHITE_TABS.countUserId", userId);
	}

	@Override
	public void insertImpotAll(Set<Long> userIdSet) {
		for(Long userId : userIdSet){
			WhiteTabs w = new WhiteTabs();
			int count = countUserId(userId);
			if(count > 0){
				continue; 
			}
			w.setUserId(userId);
			w.setSource(DisActivityEnums.WhiteTabsSourceEnum.SOURCE_IMPORT.getValue());
			insert(w);
		}
	}

	@Override
	public void delete(Long userId) {
		myBatisDao.delete("WHITE_TABS.deleteByPrimaryKey", userId);
	}

	@Override
	public boolean isSaleInvite(Long userId) {
		int count = myBatisDao.get("WHITE_TABS.isSaleInvite", userId);
		if(count>0){
			return true;
		}
		return false;
	}

}
