package com.xt.cfp.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.DisActivityRules;
import com.xt.cfp.core.pojo.ext.CommitProfitVO;
import com.xt.cfp.core.pojo.ext.DisActivityRulesExt;
import com.xt.cfp.core.service.DisActivityRulesService;
import com.xt.cfp.core.util.Pagination;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
public class DisActivityRulesServiceImpl implements DisActivityRulesService {
	@Autowired
	private MyBatisDao myBatisDao;

	@Override
	public void addDisActivityRules(DisActivityRules disActivityRules) {
		myBatisDao.insert("DIS_ACTIVITY_RULES.insertSelective", disActivityRules);
	}

	/**
	 * 根据出借产品ID，查询规则列表
	 * 
	 * @param lendProductId
	 *            出借产品ID
	 * @return 规则列表
	 */
	@Override
	public List<DisActivityRulesExt> getDisActivityRulesByLendProductId(Long lendProductId) {
		return myBatisDao.getList("DIS_ACTIVITY_RULES.getDisActivityRulesByLendProductId", lendProductId);
	}
	/**
	 * 根据出借产品ID，查询规则列表
	 * 
	 * @param lendProductId
	 *            出借产品ID
	 * @return 规则列表
	 */
	@Override
	public List<DisActivityRules> getDisActivityRulesByLendProductIdAndTime(Long lendProductId,Date startTime, Date endTime) {
		Map param = new HashMap();
		param.put("lendProductId", lendProductId);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		return myBatisDao.getList("DIS_ACTIVITY_RULES.getDisActivityRulesByLendProductIdAndTime", param);
	}
/**
 * 
 */
	@Override
	public Pagination<CommitProfitVO> getDisActivityRulesByProductIds(int pageNo, int pageSize, Map<String, Object> customParams) {
		Pagination<CommitProfitVO> re = new Pagination<CommitProfitVO>();
		re.setCurrentPage(pageNo);
		re.setPageSize(pageSize);
		   RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		  
		   customParams.put("pageNo",   pageSize*pageNo);
		   customParams.put("pageSize",  rowBounds.getOffset());
		   int total= this.myBatisDao.get("COMMI_PROFIT.selectByPrimaryKeyCountById", customParams);
		   List <CommitProfitVO> commiProfitList = this.myBatisDao.getList("COMMI_PROFIT.selectByPrimaryKeyById", customParams);
		   re.setTotal(total);
		   re.setRows(commiProfitList);
		return re;
	}

	/**
	 * 根据分销活动ID，查询规则列表
	 * 
	 * @param disId
	 *            分销活动ID
	 * @return 规则列表
	 */
	@Override
	public List<DisActivityRulesExt> getDisActivityRulesByDisId(Long disId) {
		return myBatisDao.getList("DIS_ACTIVITY_RULES.getDisActivityRulesByDisId", disId);
	}


	@Override
	public List<DisActivityRules> checkLendProIdCountByDisId(Long disId) {
		return myBatisDao.getList("DIS_ACTIVITY_RULES.checkLendProIdCountByDisId", disId);
	}


}
