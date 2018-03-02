package com.xt.cfp.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.EnterpriseInfo;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.MainLoanApplication;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.LoanPublishService;
import com.xt.cfp.core.service.MainLoanApplicationService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.Pagination;

@Service
public class MainLoanApplicationServiceImpl implements MainLoanApplicationService {
	
	private static Logger logger = Logger.getLogger(MainLoanApplicationServiceImpl.class);
	
	@Autowired
    private MyBatisDao myBatisDao;
	
	@Autowired
	private LoanApplicationService loanApplicationService;
	
	@Autowired
	private LoanPublishService loanPublishService;
	
	@Override
	public Pagination<EnterpriseInfo> findAllEnterpriseMainLoanByPage(int pageNo, int pageSize, Map<String, Object> params) {
		Pagination<EnterpriseInfo> pagination = new Pagination<EnterpriseInfo>();
        pagination.setCurrentPage(pageNo);
        pagination.setPageSize(pageSize);
        List<EnterpriseInfo> enterpriseInfos = myBatisDao.getListForPaging("findAllEnterpriseMainLoanByPage", params, pageNo, pageSize);
        pagination.setRows(enterpriseInfos);
        pagination.setTotal(myBatisDao.count("findAllEnterpriseMainLoanByPage", params));
        return pagination;
	}

	@Override
	public MainLoanApplication addMainLoanApplication(MainLoanApplication mainLoanApplication) {
		myBatisDao.insert("MAIN_LOAN_APPLICATION.insert", mainLoanApplication);
        return mainLoanApplication;
	}

	@Override
	public MainLoanApplication findById(Long mainLoanApplicationId) {
		return myBatisDao.get("MAIN_LOAN_APPLICATION.selectByPrimaryKey", mainLoanApplicationId);
	}

	@Override
	public MainLoanApplication updateMainLoanApplication(MainLoanApplication mainLoanApplication) {
		myBatisDao.update("MAIN_LOAN_APPLICATION.updateByPrimaryKeySelective", mainLoanApplication);
		return mainLoanApplication;
	}

	@Override
	public void update(Map<String, Object> map) {
		myBatisDao.update("MAIN_LOAN_APPLICATION.updateByMap", map);
	}
	
	@Override
	public List<MainLoanApplication> selectByAuto() {
		return myBatisDao.getList("MAIN_LOAN_APPLICATION.selectByAuto");
	}

	@Override
	@Transactional
	public void autoPublish() throws Exception {
		
//		执行：
//		1.查询主表，自动发标状态为开启的；
//		2.查询子loan表,借款申请状态为投标中;并且子publish表，开标时间小于当前时间的；
//		如果没有执行下面：
//		3.查询子loan表，借款申请状态为投标中；并且子publish表，开标时间大于当前时间的；
//		如果多条，根据开标时间和ID，正序排，取第一条；
//		4.更改子publish表预热时间和开标时间为当前时间；
//		如果是最后一条子标，关闭开关。
		
		logger.info("【自动发标】执行开始！");
		List<MainLoanApplication> mainList = this.selectByAuto();
		
		if(null != mainList && mainList.size() > 0){
			logger.info("【自动发标】【探测】主数量:" + mainList.size());
			
			for (MainLoanApplication main : mainList) {
				logger.info("【自动发标】【探测】主ID: " + main.getMainLoanApplicationId());
				
				List<LoanApplication> loanBidList = loanApplicationService.selectLoanByBid(main.getMainLoanApplicationId());
				if(null != loanBidList && loanBidList.size() > 0){
					logger.info("【自动发标】检测到线上有再投中的标，" + loanBidList.size() + "个，终止探测！");
					continue;
				}
				
				List<LoanApplication> loanList = loanApplicationService.selectLoanByAuto(main.getMainLoanApplicationId());
				if(null != loanList && loanList.size() > 0){
					logger.info("【自动发标】【探测】子数量:" + loanList.size());
					
					LoanApplication loan = loanList.get(0);
					logger.info("【自动发标】【执行】发标子ID: " + loan.getLoanApplicationId() + ",原预热时间：" + DateUtil.getPlusTime(loan.getPreheatTime()) + ",原开标时间：" + DateUtil.getPlusTime(loan.getOpenTime()));
					
					Map<String, Object> l_map = new HashMap<String, Object>();
					l_map.put("preheatTime", new Date());
					l_map.put("openTime", new Date());
					l_map.put("loanApplicationId", loan.getLoanApplicationId());
					loanPublishService.updateByMap(l_map);
					
					if(loanList.size() == 1){
						logger.info("【自动发标】【执行】关闭主ID：" + main.getMainLoanApplicationId());
						Map<String, Object> m_map = new HashMap<String, Object>();
						m_map.put("mainAutoPublish", "0");
						m_map.put("mainLoanApplicationId", main.getMainLoanApplicationId());
						this.update(m_map);
					}
					
				}
				
			}
		}
		logger.info("【自动发标】执行结束！");
		
	}
	
}
