package com.xt.cfp.core.service;

import com.xt.cfp.core.constants.AgreementEnum.AgreementStatusEnum;
import com.xt.cfp.core.pojo.AgreementInfo;

import java.util.List;

/**
 * Created by ren yulin on 15-8-1.
 */
public interface AgreementInfoService {

    public void insert(AgreementInfo agreementInfo);

    public void update(AgreementInfo agreementInfo);

    public List<AgreementInfo> findByCreditorRightsId(long creditorRightsId);
    /**
     * 根据债权id和合同类型返回合同版本
     * */
	public AgreementInfo findVersionByCreditorRightsId(long creditorRightsId);

	public List<AgreementInfo> findAgreeListByCreditorRightsId(long creditorRightsId, AgreementStatusEnum statuEnum);
}
