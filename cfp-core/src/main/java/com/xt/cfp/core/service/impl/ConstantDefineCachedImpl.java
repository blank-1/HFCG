package com.xt.cfp.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.pojo.FeesItem;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.service.UserAccountService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.service.ConstantDefineCached;
import com.xt.cfp.core.service.ConstantDefineService;

@Service
public class ConstantDefineCachedImpl implements ConstantDefineCached {
    protected static List<ConstantDefine> constantDefinesAll = null;
    protected static Map<String,Long> systemAccountMap = null;

    @Autowired
    private ConstantDefineService constantDefineService;
    @Autowired
    private UserAccountService userAccountService;

    @Override
    public Map<String,Long> getSystemAccount() {
        if (systemAccountMap==null ||systemAccountMap.isEmpty()) {
            systemAccountMap = new HashMap<String,Long>();
            UserAccount platFormAccount = userAccountService.getUserAccountByUserIdAndAccountTypeCode(Constants.USERINFOID_SYSTEM, AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT.getValue());
                 if(isEmpty(platFormAccount)){
                     platFormAccount=this.userAccountService.getPlatformAccountByType(AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT);
                 }

            systemAccountMap.put(AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT.getValue(),platFormAccount.getAccId());

            UserAccount paymentAccount = userAccountService.getUserAccountByUserIdAndAccountTypeCode(Constants.USERINFOID_SYSTEM, AccountConstants.AccountTypeEnum.PLATFORM_PAYMENT.getValue());
            if(isEmpty(paymentAccount)){
                paymentAccount=this.userAccountService.getPlatformAccountByType(AccountConstants.AccountTypeEnum.PLATFORM_PAYMENT);
            }
            systemAccountMap.put(AccountConstants.AccountTypeEnum.PLATFORM_PAYMENT.getValue(),paymentAccount.getAccId());

            UserAccount riskAccount = userAccountService.getUserAccountByUserIdAndAccountTypeCode(Constants.USERINFOID_SYSTEM, AccountConstants.AccountTypeEnum.PLATFORM_RISK.getValue());
            if(isEmpty(riskAccount)){
                riskAccount=this.userAccountService.getPlatformAccountByType(AccountConstants.AccountTypeEnum.PLATFORM_RISK);
            }
            systemAccountMap.put(AccountConstants.AccountTypeEnum.PLATFORM_RISK.getValue(),riskAccount.getAccId());

            UserAccount operatingAccount = userAccountService.getUserAccountByUserIdAndAccountTypeCode(Constants.USERINFOID_SYSTEM, AccountConstants.AccountTypeEnum.PLATFORM_OPERATING.getValue());
            if(isEmpty(operatingAccount)){
                operatingAccount=this.userAccountService.getPlatformAccountByType(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING);
            }
            systemAccountMap.put(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING.getValue(),operatingAccount.getAccId());
        }

        return systemAccountMap;

    }

    @Override
    public List<ConstantDefine> getAll() {
        if (constantDefinesAll == null) {
            constantDefinesAll = constantDefineService.findAll();
        }
        return constantDefinesAll;
    }

    @Override
    public List<ConstantDefine> getByTypeCode(String typeCode) {
        List<ConstantDefine> defineList = new ArrayList<ConstantDefine>();
        constantDefinesAll = getAll();
        List<String> tc = new ArrayList<>();
        if (typeCode.contains(",")) {
            for (String s : typeCode.split(",")) {
                tc.add(s);
            }
        } else {
            tc.add(typeCode);
        }
        for (ConstantDefine constantDefine : constantDefinesAll) {
            for (String s : tc) {
                if (constantDefine.getConstantTypeCode().equals(s)) {
                    defineList.add(constantDefine);
                }
            }
        }
        return defineList;
    }

    @Override
    public List<ConstantDefine> getByTypeCodeAndParent(String typeCode, long pConstantDefine) {
        List<ConstantDefine> defineList = new ArrayList<ConstantDefine>();
        constantDefinesAll = getAll();
        for (ConstantDefine constantDefine : constantDefinesAll) {
            if (constantDefine.getParentConstant() == pConstantDefine && constantDefine.getConstantTypeCode().equals(typeCode)) {
                defineList.add(constantDefine);
            }
        }
        return defineList;
    }

    @Override
    public ConstantDefine getpConstantByChild(String typeCode, String typeValue) {
        ConstantDefine theConstantDefine = null;
        constantDefinesAll = getAll();
        for (ConstantDefine constantDefine : constantDefinesAll) {
            if (constantDefine.getConstantValue().equals(typeValue) && constantDefine.getConstantTypeCode().equals(typeCode)) {
                theConstantDefine = constantDefine;
                break;
            }
        }
        for (ConstantDefine constantDefine : constantDefinesAll) {
            if (constantDefine.getConstantDefineId() == theConstantDefine.getParentConstant()) {
                return constantDefine;
            }
        }
        return null;
    }

    @Override
    public ConstantDefine getConstantByValue(String typeCode, String typeValue) {
        ConstantDefine theConstantDefine = null;
        constantDefinesAll = getAll();
        for (ConstantDefine constantDefine : constantDefinesAll) {
            //System.out.println(constantDefine.getConstantTypeCode() + "->" + constantDefine.getConstantValue());
            if (StringUtils.isNotBlank(constantDefine.getConstantValue()) && constantDefine.getConstantValue().equals(typeValue) && constantDefine.getConstantTypeCode().equals(typeCode)) {
                theConstantDefine = constantDefine;
                break;
            }
        }

        return theConstantDefine;
    }

    public void reset() {
        constantDefinesAll = null;
        constantDefinesAll = getAll();
    }

	@Override
	public List<ConstantDefine> getFeesItemChildType(char lendType, char itemType) {
		
		ConstantDefine itemTypeVO = null  ;
		if(lendType==FeesItem.ITEMKIND_LEND || lendType==FeesItem.ITEMKIND_LOAN){
			ConstantDefine  itemKind = getConstantByValue("itemKind", String.valueOf(lendType));
			List<ConstantDefine> itemTypeList = getByTypeCodeAndParent("itemType", itemKind.getConstantDefineId());
			for(ConstantDefine c : itemTypeList){
				if(itemType==(FeesItem.PARENTITEMTYPE_FEES)){		//费用
					if("费用".trim().equals(c.getConstantName().trim())){
						itemTypeVO = c ;
						break ;
					}
				}else if(itemType==FeesItem.PARENTITEMTYPE_BREACH){ 		//违约
					if("违约".trim().equals(c.getConstantName().trim())){
						itemTypeVO = c ;
						System.out.println("1");
						break ;
					}
				}else{
					throw new SystemException("未知的费用类别", ValidationErrorCode.ERROR_PARAM_ILLEGAL);
				}
			}
		}else{
			throw new SystemException("未知的费用类别", ValidationErrorCode.ERROR_PARAM_ILLEGAL);
		}
		List<ConstantDefine> itemTypeList = null;
		if(itemTypeVO != null){
			itemTypeList = getByTypeCodeAndParent("itemChildType", itemTypeVO.getConstantDefineId());
		}
		return itemTypeList;
	}

	private  boolean isEmpty(UserAccount userAccount){
        return  (null ==userAccount);
    }

	private UserAccount  getNewSystemUserAccount(AccountConstants.AccountTypeEnum accountTypeEnum){
        return   this.userAccountService.getPlatformAccountByType( accountTypeEnum);
    }
}
