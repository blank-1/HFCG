package com.xt.cfp.core.service;

import java.util.List;
import java.util.Map;

import com.xt.cfp.core.constants.EnterpriseConstants;
import com.xt.cfp.core.pojo.CoLtd;
import com.xt.cfp.core.util.Pagination;

public interface CoLtdService {

	List<CoLtd> getAllCoLtd(Long enterpriseId);

	/**
	 * 分页获取合作公司信息
	 * @param pageNo
	 * @param pageSize
	 * @param coltd
	 * @param customParams
	 * @return
	 */
	Pagination<CoLtd> getCoLtdPaging(int pageNo, int pageSize, CoLtd coltd, Map<String, Object> customParams);

	/**
	 * 添加合作公司
	 * @param coLtd
	 */
	void addCoLtd(CoLtd coLtd);

	/**
	 * 修改合作公司
	 * @param coLtd
	 */
	void updateCoLtd(CoLtd coLtd);

	/**
	 * 通过id查询
	 * @param colId
	 * @return
	 */
	CoLtd getCoLtdById(Long colId);

	/**
	 * 修改状态
	 * @param colId
	 * @param unUsage
	 */
	void changeStatus(Long colId, EnterpriseConstants.CoLtdStatusEnum unUsage);
}
