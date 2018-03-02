package com.xt.cfp.core.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.Commision;
import com.xt.cfp.core.service.CommisionService;
import com.xt.cfp.core.util.Pagination;

@Service
@Transactional
public class CommisionServiceImpl implements CommisionService {

	@Autowired
	private MyBatisDao myBatisDao;
	
	@Override
	public BigDecimal calUserLevelProfit(Long userId, String level) {
		Map map=new HashMap();
		map.put("userId", userId);
		map.put("level", level);
		return myBatisDao.get("COMMISION.accountUserLevelProfit", map);
	}

	@Override
	public Pagination<Commision> findAllByPage(int pageNo, int pageSize,Map<String, Object> params) {
		Pagination<Commision> re = new Pagination<Commision>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);


        int totalCount = this.myBatisDao.count("findAllCommisionByPage", params);
        List<Commision> friends = this.myBatisDao.getListForPaging("findAllCommisionByPage", params, pageNo, pageSize);
        
        re.setTotal(totalCount);
        re.setRows(friends);
        return re;
	}
	
	
	@Override
	public void insert(Commision commision) {
		myBatisDao.insert("COMMISION.insert", commision);
	}

}
