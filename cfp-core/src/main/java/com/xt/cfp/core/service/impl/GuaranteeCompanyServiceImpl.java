package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.Exception.code.ext.WithDrawErrorCode;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.GuaranteeCompanyStatus;
import com.xt.cfp.core.constants.UserType;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.GuaranteeCompanyExt;
import com.xt.cfp.core.pojo.ext.WithDrawExt;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luqinglin on 2015/7/1.
 */
@Service
public class GuaranteeCompanyServiceImpl implements GuaranteeCompanyService {

    @Autowired
    private MyBatisDao myBatisDao;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private WithDrawService withDrawService;
    @Autowired
    private CustomerCardService customerCardService;


    @Override
    public Pagination<GuaranteeCompanyExt> getGuaranteeCompanyPaging(int pageNum, int pageSize, GuaranteeCompany guaranteeCompany, Map<String, Object> customParams) {
        Pagination<GuaranteeCompanyExt> re = new Pagination<GuaranteeCompanyExt>();
        re.setCurrentPage(pageNum);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("guaranteeCompany", guaranteeCompany);
        params.put("customParams", customParams);

        int totalCount = this.myBatisDao.count("getGuaranteeCompanyPaging", params);
        List<GuaranteeCompanyExt> guaranteeCompanyList = this.myBatisDao.getListForPaging("getGuaranteeCompanyPaging", params, pageNum, pageSize);

        re.setTotal(totalCount);
        re.setRows(guaranteeCompanyList);

        return re;
    }

    @Override
    public GuaranteeCompany addGuaranteeCompany(GuaranteeCompany guaranteeCompany) {
        //判断参数是否为null
        if (guaranteeCompany == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("guaranteeCompany", "null");

        //为渠道创建平台虚拟用户
        UserInfo platFormUser = userInfoService.createPlatFormUser(guaranteeCompany.getCompanyName(), UserType.GUARANTEE_COMPANY);
        //为渠道创建平台账户
        userAccountService.initUserAccounts(platFormUser.getUserId());
        guaranteeCompany.setUserId(platFormUser.getUserId());
        guaranteeCompany.setCreateTime(new Date());
        //添加渠道
        myBatisDao.insert("GUARANTEE_COMPANY.insertSelective",guaranteeCompany);
        return guaranteeCompany;
    }

    @Override
    public void changeGuaranteeCompanyStatus(Long companyId, GuaranteeCompanyStatus guaranteeCompanyStatus) {
        //判断参数是否为null
        if (companyId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("companyId", "null");

        if (guaranteeCompanyStatus == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("guaranteeCompanyStatus", "null");

        //修改状态
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("companyId", companyId);
        params.put("guaranteeCompanyStatus", guaranteeCompanyStatus.getValue());

        myBatisDao.update("GUARANTEE_COMPANY.changeGuaranteeCompanyStatus", params);
    }

    @Override
    public GuaranteeCompany getGuaranteeCompanyByCompanyId(Long companyId) {
        //判断参数是否为null
        if (companyId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("companyId", "null");
        //根据Id查询
        GuaranteeCompany guaranteeCompany = myBatisDao.get("GUARANTEE_COMPANY.selectByPrimaryKey",companyId);

        return guaranteeCompany;
    }

    @Override
    public GuaranteeCompanyExt getGuaranteeCompanyDetailByCompanyId(Long companyId) {
        //判断参数是否为null
        if (companyId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("companyId", "null");
        //根据Id查询
        GuaranteeCompanyExt companyExt = myBatisDao.get("GUARANTEE_COMPANY.getGuaranteeCompanyDetail",companyId);

        return companyExt;
    }

    @Override
    @Transactional
    public void updateBondSource(GuaranteeCompany guaranteeCompany) {
        //判断参数是否为null
        if (guaranteeCompany == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("guaranteeCompany", "null");

        //判断id属性是否为空
        if (guaranteeCompany.getCompanyId() == null)
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED_FIELD).set("companyId", "null");

        myBatisDao.update("GUARANTEE_COMPANY.updateByPrimaryKeySelective", guaranteeCompany);
        //修改虚拟用户信息
        UserInfoExt userInfoExt = new UserInfoExt();
        userInfoExt.setRealName(guaranteeCompany.getCompanyName());
        userInfoExt.setUserId(guaranteeCompany.getUserId());
        userInfoService.updateUserInfoExt(userInfoExt);
    }

    @Override
    public RechargeOrder recharge(RechargeOrder rechargeOrder) {
        //判断参数是否为null
        if (rechargeOrder == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("sourceRecharge", "null");

        HttpSession session = WebUtil.getHttpServletRequest().getSession();
        AdminInfo sessionAdminInfo = (AdminInfo) session.getAttribute(AdminInfo.LOGINUSER);
        if(sessionAdminInfo==null){
            throw new SystemException(UserErrorCode.LONGIN_EXIST);
        }
        rechargeOrder.setAdminId(sessionAdminInfo.getAdminId());
        //渠道充值
        rechargeOrder = rechargeOrderService.recharge(rechargeOrder, AccountConstants.AccountChangedTypeEnum.GUARANTEE_COMPANY);
        return rechargeOrder;
    }

    @Override
    public WithDraw withDraw(WithDraw withDraw) {
        //判断参数是否为null
        if (withDraw == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("withDraw", "null");
        if (withDraw.getWithdrawAmount() == null)
            throw new SystemException(WithDrawErrorCode.WITHDRAW_AMOUNT).set("withDrawAmount", "null");
        //提现金额的合法性判断
        UserAccount userAccount = userAccountService.getCashAccount(withDraw.getUserId());
        if (userAccount.getAvailValue().compareTo(withDraw.getWithdrawAmount())<0){
            throw new SystemException(WithDrawErrorCode.WITHDRAW_OWER_FLOW).set("availValue", userAccount.getAvailValue()).set("withDrawAmout",withDraw.getWithdrawAmount());
        }

        HttpSession session = WebUtil.getHttpServletRequest().getSession();
        AdminInfo sessionAdminInfo = (AdminInfo) session.getAttribute(AdminInfo.LOGINUSER);
        if(sessionAdminInfo==null){
            throw new SystemException(UserErrorCode.LONGIN_EXIST);
        }
        withDraw.setOperatorId(sessionAdminInfo.getAdminId());
        //第三方提现
        WithDraw _withDraw = withDrawService.withDrawByThirdPart((WithDrawExt) withDraw, AccountConstants.AccountChangedTypeEnum.GUARANTEE_COMPANY);
        return _withDraw;
    }

	@Override
	public List<GuaranteeCompany> findAll(GuaranteeCompany guaranteeCompany) {
		return this.myBatisDao.getList("GUARANTEE_COMPANY.findAll", guaranteeCompany);
	}
}
