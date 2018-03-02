package com.xt.cfp.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.constants.DisActivityEnums.DisActivityStateEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.DisActivity;
import com.xt.cfp.core.pojo.DisActivityRules;
import com.xt.cfp.core.pojo.ext.DisActivityRulesExt;
import com.xt.cfp.core.pojo.ext.DisActivityVO;
import com.xt.cfp.core.service.DisActivityRulesService;
import com.xt.cfp.core.service.DisActivityService;
import com.xt.cfp.core.util.Pagination;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
public class DisActivityServiceImpl implements DisActivityService {

	private static final Logger logger = Logger.getLogger(DisActivityServiceImpl.class);

	@Autowired
	private DisActivityRulesService disActivityRulesService;
	@Autowired
	private MyBatisDao myBatisDao;

	@Override
	public Pagination<DisActivityVO> getDisActivityListPaging(int pageNo, int pageSize, DisActivityVO disActivityVO, Map<String, Object> customParams) {
		Pagination<DisActivityVO> re = new Pagination<DisActivityVO>();
		re.setCurrentPage(pageNo);
		re.setPageSize(pageSize);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("disActivity", disActivityVO);
		params.put("customParams", customParams);

		int totalCount = this.myBatisDao.count("getDisActivityListVOPaging", params);
		List<DisActivityVO> disActivitys = this.myBatisDao.getListForPaging("DIS_ACTIVITY.getDisActivityListVOPaging", params, pageNo, pageSize);

		re.setTotal(totalCount);
		re.setRows(disActivitys);

		return re;
	}

	@Override
	public void addDisActivity(DisActivity disActivity) {
		myBatisDao.insert("DIS_ACTIVITY.insertSelective", disActivity);

	}

	@Override
	public void addDisActivityInfo(DisActivity disActivity, List<DisActivityRules> rulesList) {
		addDisActivity(disActivity);
		for (int i = 0; i < rulesList.size(); i++) {
			DisActivityRules rule = rulesList.get(i);
			rule.setDisId(disActivity.getDisId());
			disActivityRulesService.addDisActivityRules(rule);
		}
	}

	/**
	 * 根据分销活动ID，加载一条信息
	 * 
	 * @param disId
	 *            分销活动ID
	 * @return 一条分销活动
	 */
	@Override
	public DisActivity getDisActivityById(Long disId) {
		return myBatisDao.get("DIS_ACTIVITY.selectByPrimaryKey", disId);
	}

	@Override
	public DisActivityVO getDisActivityVoById(Long disId) {
		return myBatisDao.get("DIS_ACTIVITY.getDisActivityVoById", disId);
	}

	@Override
	public void updateDisActivityInfo(DisActivity disActivity, List<DisActivityRules> newRules) {
		myBatisDao.update("DIS_ACTIVITY.updateByPrimaryKeySelective", disActivity);
		List<DisActivityRulesExt> oldRules = disActivityRulesService.getDisActivityRulesByDisId(disActivity.getDisId());
		for (DisActivityRulesExt oldRule : oldRules) {
			myBatisDao.delete("DIS_ACTIVITY_RULES.deleteByPrimaryKey", oldRule.getRulesId());
		}
		for (DisActivityRules newRule : newRules) {
			newRule.setDisId(disActivity.getDisId());
			disActivityRulesService.addDisActivityRules(newRule);
		}

	}

	@Override
	public List<DisActivityVO> getDisActByStateAndLendProId(Long lendProductId, DisActivityStateEnum statePublish,List<String> targetUser) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("lendProductId", lendProductId);
		param.put("state", statePublish.getValue());
		if(targetUser!=null && !targetUser.isEmpty()){
			param.put("targetUser", targetUser);
		}
		return myBatisDao.getList("DIS_ACTIVITY.getDisActByStateAndLendProId", param);
	}
	
	@Override
	public List<DisActivityVO> getAllDisActivityProducts() {
		return myBatisDao.getList("DIS_ACTIVITY.getAllDisActivityProducts");
	}

	@Override
	public void updateDisActivityState(DisActivity disActivity) {
		myBatisDao.update("DIS_ACTIVITY.updateByPrimaryKeySelective", disActivity);
	}

}
