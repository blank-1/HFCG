package com.external.yongyou.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.external.yongyou.dao.MyBatisYongYouDao;
import com.external.yongyou.entity.UserAccountHistory;
import com.external.yongyou.entity.http.YongYouBean;
import com.external.yongyou.service.UserAccountHistoryYongYouService;
import com.external.yongyou.util.YongYouUtil;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.util.ApplicationContextUtil;

@Transactional
@Service
public class UserAccountHistoryYongYouServiceImpl implements
		UserAccountHistoryYongYouService {

	@Autowired
	private MyBatisYongYouDao myBatisYongYouDao;

	private Logger logger = Logger.getLogger(UserAccountHistoryYongYouServiceImpl.class) ;
	
	public void insert(UserAccountHistory his) {
		myBatisYongYouDao.insert("insert", his);
	}

	@Override
	public Map<Integer, Integer> queryAllHisId(boolean lock) {
		List<Integer> idList = null;
		if (lock) {
			idList = myBatisYongYouDao.getList("queryAllIdsLock");
		} else {
			idList = myBatisYongYouDao.getList("queryAllIds");
		}
		Map<Integer, Integer> resMap = new HashMap<Integer, Integer>();
		for (Integer i : idList) {
			if (i != null)
				resMap.put(i, i);
		}
		return resMap;
	}

	@Transactional
	@Override
	public void saveAll(List<UserAccountHistory> list) throws Exception {
		Map<Integer, Integer> idMap = queryAllHisId(true);
		if (list.size() > 0) {
			for (UserAccountHistory uah : list) {
				if (uah.getUserid() == null) {
					continue;
				}
				if (idMap.get(uah.getLsid().intValue()) == null) {
					// 校验姓名是否为空，如空则赋值userId
					if (StringUtils.isBlank(uah.getUname())) {
						uah.setUname(uah.getUserid().toString());
					}
					// 校验身份证是否为空，如空则赋值userId
					if (StringUtils.isBlank(uah.getUid())) {
						uah.setUid(uah.getUserid().toString());
					}
					// 校验交易日期是否为空，如空则赋值now
					if (uah.getDdate() == null) {
						uah.setDdate(new Date());
					}
					// 校验交易类型是否空，如空默认未知
					if (StringUtils.isBlank(uah.getCtype())) {
						uah.setCtype("未知类型");
					}
					// 校验交易金额是否空，如空默认0
					if (uah.getJe() == null) {
						uah.setJe(BigDecimal.ZERO);
					}
					// 校验u8id是否空，如空默认-1
					if (uah.getU8id() == null) {
						uah.setJe(new BigDecimal(-1));
					}

					if (uah.getFromtype().equals(
							AccountConstants.AccountChangedTypeEnum.RECHARGE
									.getValue())) {
						switch (uah.getPaychannel()) {
							case "YBTZT":
							case "YBEBK": {
								uah.setJszh("易宝");
								break;
							}
							case "LL_AUTHPAY":
							case "LL_GATEPAY": {
								uah.setJszh("连连");
								break;
							}
							case "FUIOU_POS": {
								uah.setJszh("富友");
								break;
							}
							default: {
								uah.setJsfs("平台内充值");
							}
						}

					} else if (uah.getFromtype().equals(
							AccountConstants.AccountChangedTypeEnum.WITHDRAW
									.getValue())) {
						if (uah.getPaychannel() != null
								&& uah.getPaychannel().equals("0")) {
							uah.setJszh("易宝");
						} else if (uah.getPaychannel() != null
								&& uah.getPaychannel().equals("1")) {
							uah.setJszh("连连");
						}
					}

					if (StringUtils.isBlank(uah.getJsfs())) {
						uah.setJsfs("其他");
					}
					//15810000000 == 平台账户15810000000
					if(uah.getMphone().equals("15810000000")){
						uah.setUname("平台账户");
						uah.setUid("----------");
					}
					insert(uah);
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void requestAndSaveAccountHis(String startTime,String endTime) throws Exception {
		YongYouBean respBean = YongYouUtil.requestForYongYouData(startTime, endTime);
		try {
			if(respBean == null){
				throw new Exception("接口数据返回异常！");
			}
			List<UserAccountHistory> list = (List<UserAccountHistory>) respBean.getResponseList() ;
			UserAccountHistoryYongYouService bean = ApplicationContextUtil.getBean(UserAccountHistoryYongYouServiceImpl.class);
			bean.saveAll(list);
		} catch (Exception e) {
			logger.error("保存流水异常，异常原因：" , e);
			throw e ;
		}
	}
	
}
