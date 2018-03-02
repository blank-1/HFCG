package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.constants.AgreementEnum.AgreementStatusEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.AgreementInfo;
import com.xt.cfp.core.service.AgreementInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ren yulin on 15-8-1.
 */
@Service
public class AgreementInfoServiceImpl implements AgreementInfoService {
    @Autowired
    private MyBatisDao myBatisDao;
    @Override
    public void insert(AgreementInfo agreementInfo) {
        myBatisDao.insert("AGREEMENT_INFO.insert",agreementInfo);
    }

    @Override
    public void update(AgreementInfo agreementInfo) {
        myBatisDao.update("AGREEMENT_INFO.updateByPrimaryKeySelective",agreementInfo);
    }

    @Override
    public List<AgreementInfo> findByCreditorRightsId(long creditorRightsId) {
        return myBatisDao.getList("AGREEMENT_INFO.findByCreditorRightsId",creditorRightsId);
    }
    
    @Override
    public AgreementInfo  findVersionByCreditorRightsId(long creditorRightsId) {
    	return myBatisDao.get("AGREEMENT_INFO.findVersionByCreditorRightsIdAgreeType",creditorRightsId);
    }

	@Override
	public List<AgreementInfo> findAgreeListByCreditorRightsId(long creditorRightsId, AgreementStatusEnum statuEnum) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("creditorRightsId", creditorRightsId);
		param.put("status", statuEnum.getValue());
		return myBatisDao.getList("AGREEMENT_INFO.findAgreeListByCreditorRightsId", param);
	}

}
