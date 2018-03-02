package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.Exception.code.ext.WithDrawErrorCode;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.BondSourceExt;
import com.xt.cfp.core.pojo.ext.BondSourceUser;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.pojo.ext.WithDrawExt;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.Property;
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
 * Created by luqinglin on 2015/6/18.
 */
@Property
@Service
public class BondSourceServiceImpl implements BondSourceService {

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
    private UserInfoExtService userInfoExtService;

    private static String initPassword;

    @Override
    @Transactional
    public BondSource addBondSource(BondSource bondSource) {
        //判断参数是否为null
        if (bondSource == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("bondSource", "null");

        //为渠道创建平台虚拟用户
        UserInfo platFormUser = userInfoService.createPlatFormUser(bondSource.getSourceName(),UserType.CHANNEL);
        //为渠道创建平台账户
        userAccountService.initUserAccounts(platFormUser.getUserId());
        bondSource.setUserId(platFormUser.getUserId());
        bondSource.setCreateTime(new Date());
        //添加渠道
        myBatisDao.insert("BOND_SOURCE.insert",bondSource);
        return bondSource;
    }

    @Override
    @Transactional
    public void changeBondSourceStatus(Long bondSourceId, BondSourceStatus bondSourceStatus) {
        //判断参数是否为null
        if (bondSourceId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("bondSourceId", "null");

        if (bondSourceStatus == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("bondSourceStatus", "null");

        //修改状态
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bondSourceId", bondSourceId);
        params.put("bondSourceStatus", bondSourceStatus.getValue());

        myBatisDao.update("BOND_SOURCE.changeBondSourceStatus", params);
    }

    @Override
    public BondSource getBondSourceByBondSourceId(Long bondSourceId) {
        //判断参数是否为null
        if (bondSourceId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("bondSourceId", "null");
        //根据Id查询
        BondSource bondSource = myBatisDao.get("BOND_SOURCE.selectByPrimaryKey",bondSourceId);

        return bondSource;
    }

    @Override
    public BondSourceExt getBondSourceDetailByBondSourceId(Long bondSourceId) {
        //判断参数是否为null
        if (bondSourceId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("bondSourceId", "null");
        //根据Id查询
        BondSourceExt bondSource = myBatisDao.get("BOND_SOURCE.getBondSourceDetail",bondSourceId);

        return bondSource;
    }

    @Override
    @Transactional
    public void updateBondSource(BondSource bondSource) {
        //判断参数是否为null
        if (bondSource == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("bondSource", "null");

        //判断id属性是否为空
        if (bondSource.getBondSourceId() == null)
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED_FIELD).set("bondSourceId", "null");

        myBatisDao.update("BOND_SOURCE.updateByPrimaryKeySelective", bondSource);
        //修改虚拟用户信息
        UserInfoExt userInfoExt = new UserInfoExt();
        userInfoExt.setRealName(bondSource.getSourceName());
        userInfoExt.setUserId(bondSource.getUserId());
        userInfoService.updateUserInfoExt(userInfoExt);
    }

    @Override
    public List<BondSourceUser> getBondSourceUserBySourceId(Long bondSourceId) {
        //判断参数是否为null
        if (bondSourceId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("bondSourceId", "null");

        List<BondSourceUser> bondSourceUserList = myBatisDao.getList("getBondSourceUserBySourceId", bondSourceId);
        return bondSourceUserList;
    }

    @Override
    @Transactional
    public BondSourceUser addBondSourceUser(BondSourceUser bondSourceUser) {
        //判断参数是否为null
        if (bondSourceUser == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("bondSourceUser", "null");


        String bondSourceUserMobileNo = bondSourceUser.getMobileNo();//[1/3]先将居间人正确的手机号码存储下来
        bondSourceUser.setMobileNo(String.valueOf(new Date().getTime()).substring(2));//[2/3]手机号赋值一个新的随机数字
        
        //系统注册用户（原始债权人）
        UserInfo userInfo = userInfoService.regist(initBondUser(bondSourceUser),UserSource.PLATFORM,null);

        bondSourceUser.setMobileNo(bondSourceUserMobileNo);//[3/3]将正确的手机号码在存储回来
        
        //保存用户认证信息
        UserInfoExt userInfoExt = new UserInfoExt();
        userInfoExt.setRealName(bondSourceUser.getBondName());
        userInfoExt.setIdCard(bondSourceUser.getIdCard());
        userInfoExt.setUserId(userInfo.getUserId());
        userInfoExt.setMobileNo(bondSourceUser.getMobileNo());
        userInfoService.updateUserInfoExt(userInfoExt);

        //保存原始债权人
        bondSourceUser.setUserId(userInfo.getUserId());
        bondSourceUser.setCreateTime(new Date());
        myBatisDao.insert("addBondSourceUser",bondSourceUser);
        return bondSourceUser;
    }

    /**
     * 初始化原始债权人
     * @return
     */
    private UserInfo initBondUser(BondSourceUser bondSourceUser) {
        bondSourceUser.setLoginPass(initPassword);
        bondSourceUser.setType(UserType.MEDIATOR.getValue());
        bondSourceUser.setSource(UserSource.PLATFORM.getValue());
        return bondSourceUser;
    }

    @Override
    public void changeBondSourceUserStatus(Long userId, UserStatus userStatus) {
        //判断参数是否为null
        if (userId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");

        if (userStatus == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userStatus", "null");

        userInfoService.changeUserStatus(userId,userStatus);
    }

    @Override
    @Transactional
    public void updateBondSourceUser(BondSourceUser bondSourceUser) {
        //判断参数是否为null
        if (bondSourceUser == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("bondSourceUser", "null");

        if (bondSourceUser.getUserSourceId() == null)
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED_FIELD).set("userSourceId", "null");

        myBatisDao.update("updateBondSourceUser",bondSourceUser);
        
        //修改用户扩展表
        UserInfoExt userInfoExt = new UserInfoExt();
        userInfoExt.setRealName(bondSourceUser.getBondName());
        userInfoExt.setIdCard(bondSourceUser.getIdCard());
        userInfoExt.setUserId(bondSourceUser.getUserId());
        userInfoExt.setMobileNo(bondSourceUser.getMobileNo());
        userInfoService.updateUserInfoExt(userInfoExt);

        //修改用户表
        bondSourceUser.setMobileNo(String.valueOf(new Date().getTime()).substring(2));//手机号赋值一个新的随机数字
        userInfoService.updateUser(bondSourceUser);
    }


    @Override
    @Transactional
    public RechargeOrder recharge( RechargeOrder rechargeOrder) {
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
        rechargeOrder = rechargeOrderService.recharge(rechargeOrder, AccountConstants.AccountChangedTypeEnum.BOND_SOURCE);
        return rechargeOrder;

    }

    @Override
    public Pagination<BondSourceExt> getBondSourcePaging(int pageNum, int pageSize, BondSource bondSource, Map<String, Object> customParams) {
        Pagination<BondSourceExt> re = new Pagination<BondSourceExt>();
        re.setCurrentPage(pageNum);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bondSource", bondSource);
        params.put("customParams", customParams);

        int totalCount = this.myBatisDao.count("getBondSourcePaging", params);
        List<BondSourceExt> users = this.myBatisDao.getListForPaging("getBondSourcePaging", params, pageNum, pageSize);

        re.setTotal(totalCount);
        re.setRows(users);

        return re;
    }

    @Override
    public Pagination<BondSourceUser> getBondSourceUserPaging(int pageNo, int pageSize, BondSourceUser bondSourceUser, Map<String, Object> customParams) {
        Pagination<BondSourceUser> re = new Pagination<BondSourceUser>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bondSourceUser", bondSourceUser);
        params.put("customParams", customParams);

        int totalCount = this.myBatisDao.count("getBondSourceUserPaging", params);
        List<BondSourceUser> users = this.myBatisDao.getListForPaging("getBondSourceUserPaging", params, pageNo, pageSize);

        re.setTotal(totalCount);
        re.setRows(users);

        return re;
    }

    @Override
    @Transactional
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
        WithDraw _withDraw = withDrawService.withDrawByThirdPart((WithDrawExt) withDraw, AccountConstants.AccountChangedTypeEnum.BOND_SOURCE);
        return _withDraw;
    }

    @Override
    public BondSourceUser getBondSourceUserByUserSourceId(Long userSourceId) {

        //判断参数是否为null
        if (userSourceId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userSourceId", "null");

        BondSourceUser bondSourceUser = myBatisDao.get("getBondSourceUserByUserSourceId",userSourceId);

        return bondSourceUser;
    }

    @Property(name = "system.password")
    public void setInitPassword(String initPassword) {
        this.initPassword = initPassword;
    }

    /**
     * 获取所有渠道信息
     */
	@Override
	public List<BondSource> getAllBondSource() {
		return myBatisDao.getList("BOND_SOURCE.getAllBondSource");
	}

    /**
     * 根据ID，加载一条原始债券人渠道
     * @param userSourceId 原始债权人ID
     */
	@Override
	public BondSourceUser getBondSourceUserById(Long userSourceId) {
		return myBatisDao.get("BOND_SOURCE.getBondSourceUserById", userSourceId);
	}

    /**
     * 根据渠道ID，获取所有原始债权人，条件：原始债权人对应的用户，必须有卡
     * @param bondSourceId 渠道ID
     */
	@Override
	public List<BondSourceUser> getAllBondSourceUserBySourceId(Long bondSourceId) {
		return myBatisDao.getList("BOND_SOURCE.getAllBondSourceUserBySourceId", bondSourceId);
	}
    
	/**
	 * 获取原始债权人列表
	 * @param  pageNo
	 * @param  pageSize
	 * @author wangyadong
	 */
	@Override
	public Pagination<BondSourceUser> getBondSourceUserListPaging(int pageNo, int pageSize, BondSourceUser bondSourceUser, Map<String, Object> customParams) {
		    Pagination<BondSourceUser> re = new Pagination<BondSourceUser>();
	        re.setCurrentPage(pageNo);
	        re.setPageSize(pageSize);

	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("bondSourceUser", bondSourceUser);
	        params.put("customParams", customParams);

	        int totalCount = this.myBatisDao.count("getBondSourceUserPaging", params);
	        List<BondSourceUser> users = this.myBatisDao.getListForPaging("getBondSourceUserPaging", params, pageNo, pageSize);

	        re.setTotal(totalCount);
	        re.setRows(users);

	        return re;
	}

    /**
	 * 获取居间人列表
	 * @param  pageNo
	 * @param  pageSize
	 * @author wangyadong
	 */
	@Override
	public Pagination<BondSourceUser> getBondSourceUserSelectPaging(int pageNo, int pageSize, BondSourceUser bondSourceUser, Map<String, Object> customParams) {
		    Pagination<BondSourceUser> re = new Pagination<BondSourceUser>();
	        re.setCurrentPage(pageNo);
	        re.setPageSize(pageSize);

	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("bondSourceUser", bondSourceUser);
	        params.put("customParams", customParams);

	        int totalCount = this.myBatisDao.count("getBondSourceUserPagingByDiolog", params);
	        List<BondSourceUser> users = this.myBatisDao.getListForPaging("getBondSourceUserPagingByDiolog", params, pageNo, pageSize);

	        re.setTotal(totalCount);
	        re.setRows(users);

	        return re;
	}
	/**
	 * 保存渠道与中间人多对多的关系
	 * @param  userId
	 * @param  bondSourceId 渠道ID
	 * @author wangyadong
	 */
	@Override
	public String addBondSourceUserNewRelation(String userId,Long bondSourceId) {
		// 根据userID查询 所在渠道     
		List<BondSourceUser> listId = myBatisDao.getList("BOND_SOURCE.getAllBondSourceUserByUserId", userId);
		for (int i = 0; i < listId.size(); i++) {
			BondSourceUser bondId = listId.get(i);
			//与当前关系冲突 不能保存
			if(bondSourceId.equals(bondId.getBondSourceId())){
				return "ALEADY";
			}
		}
		//保存新的关系
		try{
			BondSourceUser bondSourceUser = new BondSourceUser();
            UserInfoVO userExt = userInfoService.getUserExtByUserId(new Long(userId));
            bondSourceUser.setCreateTime(new Date());
            bondSourceUser.setBondSourceId(bondSourceId);
            bondSourceUser.setUserId(new Long(userId));
            bondSourceUser.setBondName(userExt.getRealName());
			myBatisDao.insert("addBondSourceUser",bondSourceUser);
			return "SCUESS";
		}catch(Exception e){
			
		}
		  
		return "SCUESS";
	}
}
