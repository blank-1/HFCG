package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.CapitalFlow;

import java.util.List;
import java.util.Map;

public interface CapitalFlowService {

	List<CapitalFlow> findByCondition(Map map);

	void addCapital(CapitalFlow cap);

	void updateCapital(CapitalFlow cap);
}
