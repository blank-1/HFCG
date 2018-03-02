package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.SalesUserInfo;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.pojo.ext.UserValidCode;
import com.xt.cfp.core.security.util.Base64Utils;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.service.crm.StaffService;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by luqinglin on 2015/6/12.
 */
@Property
@Service
public class UserInfoServiceImpl implements UserInfoService {

    private static Logger logger = Logger.getLogger(UserInfoServiceImpl.class);

    @Autowired
    private MyBatisDao myBatisDao;
    @Autowired
    private UserAccountService userAccountService;

    private static String initPassword;
    @Autowired
    private SmsService smsService;
    @Autowired
    private RedisCacheManger redisCacheManger;
    @Autowired
    private UserInfoExtService userInfoExtService;

    private static Integer smsCodeTime;

    @Autowired
    private AddressService addressService;
    @Autowired
    private UserAccountOperateService userAccountOperateService;
    @Autowired
    private SalesOrganizeInfoService salesOrganizeInfoService;
    @Autowired
    private SalesAdminInfoService salesAdminInfoService;
    @Autowired
    private UserOpenIdService userOpenIdService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private LendOrderService lendOrderService;

    //注册路径
    private static String registerPath;
    @Autowired
    private DistributionInviteService distributionInviteService;
    @Autowired
    private VoucherService voucherService;

    @Override
    @Transactional
    public UserInfo regist(UserInfo userInfo, UserSource userSource, String inviteUserId) throws SystemException {
        //判断参数是否为null
        if (userInfo == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userInfo", "null");

        //判断用户名是否存在
        if (isLoginNameExist(userInfo.getLoginName()))
            throw new SystemException(UserErrorCode.LOGINNAME_EXIST).set("loginName", userInfo.getLoginName());

        //判断手机号是否存在
        if (isMobileExist(userInfo.getMobileNo()))
            throw new SystemException(UserErrorCode.MOBILENO_EXIST).set("moblieNo", userInfo.getMobileNo());
        userInfo.setSource(userSource.getValue());
        String password = MD5Util.MD5Encode(userInfo.getLoginPass(), "utf-8");
        //加密登陆密码
        userInfo.setLoginPass(password);
        //默认初始化交易密码和登陆密码一致
        userInfo.setBidPass(password);
        if (userInfo.getStatus() == null)
            userInfo.setStatus(UserStatus.NORMAL.getValue());
        if (userInfo.getType() == null)
            userInfo.setType(UserType.COMMON.getValue());
        userInfo.setCreateTime(new Date());
        //保存
        myBatisDao.insert("USER_INFO.insert", userInfo);
        //初始化用户账户信息
        userAccountService.initUserAccounts(userInfo.getUserId());
        //记录用户登陆信息（统计表）
        myBatisDao.insert("USER_INFO.recordUser", userInfo);
        //把(认证)手机号放到ext表
        UserInfoExt userInfoExt = new UserInfoExt();
        userInfoExt.setMobileNo(userInfo.getMobileNo());
        userInfoExt.setUserId(userInfo.getUserId());
        userInfoExt.setIsVerified(UserIsVerifiedEnum.NO.getValue());
        userInfoExt.setRecUserId(StringUtils.isEmpty(inviteUserId) ? null : Long.parseLong(inviteUserId));
        myBatisDao.insert("USER_INFO_EXT.insert", userInfoExt);
        //维护员工客户关系
        if (!StringUtils.isEmpty(inviteUserId)) {
            staffService.saveCustomerAfterRegister(Long.parseLong(inviteUserId), userInfo.getUserId());
        }
        //插入用户邀请码
        InvitationCode invite = new InvitationCode();
        invite.setUserId(userInfo.getUserId());
        invite.setInvitationCode(createInviteCode(userInfo.getMobileNo()));
        invite.setType(InviteFriendsType.LINK_INVITE.getValue());
        myBatisDao.insert("INVITATION_CODE.insert", invite);
        userInfo.setLastLoginTime(userInfo.getCreateTime());
        distributionInviteService.settingDistribtionRelation(userInfoExt.getRecUserId(), userInfoExt.getUserId());
        //注册发放财富券
        voucherService.registerRelease(userInfo.getUserId());
        //记录多级邀请关系
        this.recordMultilevelInvitation(userInfo.getUserId());
        return userInfo;
    }


    /**
     * 检查并获取邀请码
     *
     * @param mobileNo
     * @return
     */
    public String createInviteCode(String mobileNo) {
        String code = com.xt.cfp.core.util.StringUtils.getLastStr(mobileNo, 6);
        InvitationCode invitationCode = this.getInvitationCodeByCode(code);
        if (invitationCode == null) {
            return code;
        } else {
            String radomStr = com.xt.cfp.core.util.StringUtils.generateVerifyCode();
            return createInviteCode(radomStr);
        }
    }

    @Override
    public void exportExcel(HttpServletResponse response, UserInfoVO userInfoVO) {
        List<LinkedHashMap<String, Object>> list = myBatisDao.getList("USER_INFO.exportExcel", userInfoVO);
        ResponseUtil.sendExcel(response, list, "用户信息表");
    }

    @Override
    public int countAllUser(UserType userType) {
        UserInfo user = new UserInfo();
        user.setType(userType.getValue());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userInfo", user);
        int totalRow = this.myBatisDao.count("getUserInfoPaging", params);
        return totalRow;
    }

    @Override
    public UserInfo regist(UserInfo userInfo, UserSource userSource) throws SystemException {
        return this.regist(userInfo, userSource, null);
    }

    @Override
    @Transactional
    public UserInfo registBinding(UserInfo userInfo, UserSource userSource, String openId, InvitationCode invitationCode) {
        if (invitationCode == null) {
            userInfo = regist(userInfo, UserSource.WECHAT);
        } else {
            userInfo = regist(userInfo, UserSource.WECHAT, invitationCode.getUserId() + "");

        }
        if (openId != null && !"".equals(openId)) {
            UserOpenId userOpenId = new UserOpenId();
            userOpenId.setOpenId(openId);
            userOpenId.setUserId(userInfo.getUserId());
            userOpenId.setType("1");
            userOpenId.setGetTime(new Date());
            UserOpenId seachUserOpenId = userOpenIdService.getOpenIdByCondition(null, openId, "1");
            if (seachUserOpenId != null) {
                userOpenIdService.updateUserOpenId(seachUserOpenId, userInfo, userOpenId);
            } else {
                userOpenIdService.addUserOpenId(userInfo, userOpenId);
            }
        }

        return userInfo;
    }

    @Override
    @Transactional
    public UserInfo regist(String mobileNo) {
        //判断参数是否为null
        if (StringUtils.isEmpty(mobileNo))
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("mobileNo", "null");

        String loginName = "tel_" + mobileNo;

        //判断用户名是否存在
        boolean isExist = isLoginNameExist(loginName);
        while (isExist) {
            loginName = "tel_" + mobileNo + com.xt.cfp.core.util.StringUtils.generateRandomNumber(3);
            isExist = isLoginNameExist(loginName);
        }

        //判断手机号是否存在
        if (isMobileExist(mobileNo))
            throw new SystemException(UserErrorCode.MOBILENO_EXIST).set("moblieNo", mobileNo);

        //用户名
        UserInfo userInfo = new UserInfo();
        userInfo.setLoginName(loginName);
        userInfo.setMobileNo(mobileNo);
        String radomStrLower = com.xt.cfp.core.util.StringUtils.getRadomStrLower(6);
        userInfo.setLoginPass(radomStrLower);
        //注册
        UserInfo registUser = this.regist(userInfo, UserSource.SALES_PLANTFORM, null);

        //保存用户与客户的对应关系
        SalesAdminUserInfo saui = new SalesAdminUserInfo();
        SalesAdminInfo currentUser = (SalesAdminInfo) SecurityUtil.getCurrentUser();
        saui.setCreateTime(new Date());
        saui.setAdminId(currentUser.getAdminId());
        saui.setStatus(UserStatus.NORMAL.getValue());
        saui.setUserId(registUser.getUserId());
        myBatisDao.insert("SL_ADMIN_USER_INFO.insertSelective", saui);

        //发短信
        VelocityContext context = new VelocityContext();
        context.put("name", loginName);
        context.put("pwd", radomStrLower);
        String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_SALESREGIST_VM, context);
        smsService.sendMsgByWXTL(mobileNo, content);
        return registUser;

    }

    @Override
    @Transactional
    public UserInfo createPlatFormUser(String userName, UserType userType) {
        UserInfo user = new UserInfo();
        user.setType(userType.getValue());
        user.setCreateTime(new Date());
        user.setLoginName(createPlatFormUserName());
        user.setMobileNo(getRandom());
        String password = MD5Util.MD5Encode(initPassword, "utf-8");
        user.setLoginPass(password);
        user.setStatus(UserStatus.NORMAL.getValue());
        user.setSource(UserSource.PLATFORM.getValue());
        myBatisDao.insert("USER_INFO.insert", user);
        UserInfoExt userInfoExt = new UserInfoExt();
        userInfoExt.setUserId(user.getUserId());
        userInfoExt.setRealName(userName);
        userInfoExt.setMobileNo(user.getMobileNo());
        this.updateUserInfoExt(userInfoExt);
        return user;
    }

    @Override
    @Transactional
    public UserInfo createPlatFormUserForEnterprise(String userName, UserType userType,String mobleNo) {
        UserInfo user = new UserInfo();
        user.setType(userType.getValue());
        user.setCreateTime(new Date());
        user.setLoginName(createPlatFormUserName());
        user.setMobileNo(mobleNo);
        String password = MD5Util.MD5Encode(initPassword, "utf-8");
        user.setLoginPass(password);
        user.setStatus(UserStatus.NORMAL.getValue());
        user.setSource(UserSource.PLATFORM.getValue());
        myBatisDao.insert("USER_INFO.insert", user);
        UserInfoExt userInfoExt = new UserInfoExt();
        userInfoExt.setUserId(user.getUserId());
        userInfoExt.setRealName(userName);
        userInfoExt.setMobileNo(user.getMobileNo());
        this.updateUserInfoExt(userInfoExt);
        return user;
    }

    /**
     * 生成平台用户登陆名
     *
     * @return
     */
    private String createPlatFormUserName() {
        return "cfp" + getRandom();
    }

    private String getRandom() {
        long time = System.currentTimeMillis();
        return StringUtils.substring(time + "", 7) + "" + Math.round(Math.random() * 100);
    }

    @Override
    public boolean isLoginNameExist(String loginName) {
        //判断参数是否为null
        if (StringUtils.isEmpty(loginName))
            throw new SystemException(ValidationErrorCode.ERROR_STRING_CAN_NOT_BE_EMPTY).set("loginName", loginName);

        //用登陆名查询
        UserInfo user = getUserByLoginName(loginName);
        return user != null;
    }

    @Override
    public boolean isMobileExist(String mobileNo) {
        //判断参数是否为null
        if (StringUtils.isEmpty(mobileNo))
            throw new SystemException(ValidationErrorCode.ERROR_STRING_CAN_NOT_BE_EMPTY).set("mobileNo", mobileNo);

        //用手机号查询
        UserInfo user = getUserByMobileNo(mobileNo);
        return user != null;
    }

    @Override
    public UserInfo getUserByUserId(Long userId) {
        //判断参数是否为null
        if (null == userId)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");

        //根据userId查询
        UserInfo user = myBatisDao.get("USER_INFO.selectByPrimaryKey", userId);
        Date userLastLoginTime = getUserLastLoginTime(user.getUserId());
        if(null!=userLastLoginTime)
        user.setLastLoginTime(getUserLastLoginTime(user.getUserId()));
        return user;
    }

    @Override
    public UserInfo getUserByLoginName(String loginName) {
        //判断参数是否为null
        if (StringUtils.isEmpty(loginName))
            throw new SystemException(ValidationErrorCode.ERROR_STRING_CAN_NOT_BE_EMPTY).set("loginName", loginName);

        //根据用户名查询
        UserInfo userInfo = myBatisDao.get("USER_INFO.getUserByLoginName", loginName);

        return userInfo;
    }

    @Override
    public List<UserInfo> getUserListByLoginName(String loginName, UserType userType) {
        //判断参数是否为null
        if (StringUtils.isEmpty(loginName))
            throw new SystemException(ValidationErrorCode.ERROR_STRING_CAN_NOT_BE_EMPTY).set("loginName", loginName);
        Map param = new HashMap<String, String>();
        param.put("loginName", loginName);
        param.put("userType", userType.getValue());
        //根据用户名查询
        List<UserInfo> userInfo = myBatisDao.getList("USER_INFO.getUserListByLoginName", param);

        return userInfo;
    }

    @Override
    public UserInfo getUserByMobileNo(String mobileNo) {
        //判断参数是否为null
        if (StringUtils.isEmpty(mobileNo))
            throw new SystemException(ValidationErrorCode.ERROR_STRING_CAN_NOT_BE_EMPTY).set("mobileNo", mobileNo);

        //根据手机号查询
        UserInfo userInfo = myBatisDao.get("USER_INFO.getUserByMobile", mobileNo);

        return userInfo;
    }

    @Override
    public void changeUserStatus(Long userId, UserStatus userstatus) {
        //判断参数是否为null
        if (userId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");
        if (userstatus == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userstatus", "null");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("userstatus", userstatus.getValue());

        myBatisDao.update("USER_INFO.changeUserStatus", params);
    }

    @Override
    public void updateUser(UserInfo userInfo) {
        //判断参数是否为null
        if (userInfo == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userInfo", "null");

        //判断id属性是否为空
        if (userInfo.getUserId() == null)
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED_FIELD).set("userId", "null");

        UserInfo user = this.getUserByUserId(userInfo.getUserId());

        //判断用户名是否存在

        if (StringUtils.isNotEmpty(userInfo.getLoginName()) && !user.getLoginName().equals(userInfo.getLoginName()) && isLoginNameExist(userInfo.getLoginName()))
            throw new SystemException(UserErrorCode.LOGINNAME_EXIST).set("loginName", userInfo.getLoginName());

        //判断手机号是否存在
        if (StringUtils.isNotEmpty(userInfo.getMobileNo()) && !user.getMobileNo().equals(userInfo.getMobileNo()) && isMobileExist(userInfo.getMobileNo()))
            throw new SystemException(UserErrorCode.MOBILENO_EXIST).set("moblieNo", userInfo.getMobileNo());

        myBatisDao.update("USER_INFO.updateByPrimaryKeySelective", userInfo);
    }

    @Override
    @Transactional
    public UserInfo updatePassword(String oldPassword, String newPassword, Long userId) {
        //判断参数是否为null
        if (userId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");

        //判断用户是否存在
        UserInfo user = getUserByUserId(userId);
        if (user == null)
            throw new SystemException(UserErrorCode.USER_NOT_EXIST).set("userId", userId);

        //验证原密码
        String md5oldEncode = MD5Util.MD5Encode(oldPassword.trim(), "utf-8");
        if (!user.getLoginPass().equals(md5oldEncode))
            throw new SystemException(UserErrorCode.ERROR_OLD_PASSWORD).set("oldPassword", oldPassword);

        //设置新密码
        String md5newEncode = MD5Util.MD5Encode(newPassword.trim(), "utf-8");
        user.setLoginPass(md5newEncode);
        myBatisDao.update("USER_INFO.updateByPrimaryKey", user);

        return user;
    }

    @Override
    @Transactional
    public UserInfo updatePayPassword(String oldPassword, String newPassword, Long userId) {
        if (userId == null) {
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");
        }
        UserInfo user = getUserByUserId(userId);
        if (user == null) {
            throw new SystemException(UserErrorCode.USER_NOT_EXIST).set("userId", userId);
        }

        if (!StringUtils.isEmpty(oldPassword)) {
            //验证原密码
            String md5oldEncode = MD5Util.MD5Encode(oldPassword.trim(), "utf-8");
            if (!user.getLoginPass().equals(md5oldEncode)) {
                throw new SystemException(UserErrorCode.ERROR_OLD_PASSWORD).set("oldPassword", oldPassword);
            }
        }

        //设置新密码
        String md5newEncode = MD5Util.MD5Encode(newPassword.trim(), "utf-8");
        user.setBidPass(md5newEncode);
        myBatisDao.update("USER_INFO.updateByPrimaryKey", user);

        return user;
    }

    @Override
    public List<UserInfo> getUserByCondition(UserInfo userInfo, Map<String, Object> customParams) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userInfo", userInfo);
        params.put("customParams", customParams);
        List<UserInfo> userInfos = myBatisDao.getList("USER_INFO.getUserByCondition", params);
        return userInfos;
    }

    @Override
    public Pagination<UserInfo> getUserPaging(int pageNum, int pageSize, UserInfo userInfo, Map<String, Object> customParams) {
        Pagination<UserInfo> re = new Pagination<UserInfo>();
        re.setCurrentPage(pageNum);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userInfo", userInfo);
        params.put("customParams", customParams);

        int totalCount = this.myBatisDao.count("getUserInfoPaging", params);
        List<UserInfo> users = this.myBatisDao.getListForPaging("getUserInfoPaging", params, pageNum, pageSize);

        re.setTotal(totalCount);
        re.setRows(users);

        return re;
    }

    public String getInitPassword() {
        return initPassword;
    }

    @Property(name = "system.password")
    public void setInitPassword(String initPassword) {
        UserInfoServiceImpl.initPassword = initPassword;
    }

    @Override
    @Transactional
    public String updateUserInfo(UserInfoVO userInfo) {
        // 更新userInfo
        UserInfo paramUser = new UserInfo();
        paramUser.setUserId(userInfo.getUserId());
        paramUser.setLoginName(userInfo.getLoginName());
        UserInfo oldUser = getUserByUserId(paramUser.getUserId());
        if (null != oldUser) {
            updateUser(paramUser);
        }

        // 更新address
        Address oldAddress = null;
        Long residentAddress = null;
        UserInfoExt ext = userInfoExtService.getUserInfoExtById(userInfo.getUserId());
        if (ext.getResidentAddress() != null) {
            oldAddress = addressService.getAddressById(ext.getResidentAddress());
            oldAddress.setProvince(userInfo.getProvince());
            oldAddress.setCity(userInfo.getCity());
            oldAddress.setDetail(userInfo.getDetail());
            oldAddress = addressService.updateAddress(oldAddress);
            residentAddress = oldAddress.getAddressId();
        } else {
            Address newAddress = new Address();
            newAddress.setUserId(userInfo.getUserId());
            newAddress.setProvince(userInfo.getProvince());
            newAddress.setCity(userInfo.getCity());
            newAddress.setDetail(userInfo.getDetail());
            newAddress = addressService.addAddress(newAddress);
            residentAddress = newAddress.getAddressId();
        }

        // 更新userInfoExt
        UserInfoExt paramExt = new UserInfoExt();
        paramExt.setUserId(userInfo.getUserId());
        paramExt.setEducationLevel(userInfo.getEducationLevel());
        paramExt.setResidentAddress(residentAddress);
        updateUserInfoExt(paramExt);

        return "success";
    }

    @Override
    public UserInfoExt updateUserInfoExt(UserInfoExt userInfoExt) {
        UserInfoExt oldExt = myBatisDao.get("USER_INFO_EXT.selectByPrimaryKey", userInfoExt.getUserId());
        if (null == oldExt) {
            myBatisDao.insert("USER_INFO_EXT.insert", userInfoExt);
        } else {
            myBatisDao.update("USER_INFO_EXT.updateByPrimaryKeySelective", userInfoExt);
        }
        return userInfoExt;
    }

    @Override
    public UserInfoVO getUserExtByUserId(Long userId) {
        UserInfoVO userExtForm = myBatisDao.get(
                "USER_INFO_EXT.getUserExtByUserId", userId);
        return userExtForm;
    }

    @Override
    public void uploadPhoto(String userId, String fileName, String imgPath, String url) {
        // 保存attachment
        //todo type类型更改
        Attachment att = new Attachment();
        att.setUserId(Long.valueOf(userId));
        att.setType("1");
        att.setUrl(url);
        att.setPhysicalAddress(imgPath);
        att.setFileName(fileName);
        att.setCreateTime(new Date());
        myBatisDao.insert("ATTACHMENT.insert", att);
    }

    /**
     * 客户列表分页查询
     */
    @Override
    public Pagination<UserInfoVO> getUserExtPaging(int pageNo, int pageSize, UserInfoVO userVO) {
        Pagination<UserInfoVO> pagination = new Pagination<UserInfoVO>();
        pagination.setCurrentPage(pageNo);
        pagination.setPageSize(pageSize);
        List<UserInfoVO> customers = myBatisDao.getListForPaging("USER_INFO_EXT.getUserExtPaging", userVO, pageNo, pageSize);
        List<UserInfoVO> resultList = new ArrayList<UserInfoVO>();
        for (int i = 0; i < customers.size(); i++) {
            UserInfoVO result = customers.get(i);
            BigDecimal fuzhai = userInfoExtService.getUserFuZhai(result.getUserId());
            //不带省心计划的资产
            BigDecimal zichan = userInfoExtService.getUserZiChan(result.getUserId());
            //持有全部省心计划
            BigDecimal totalHoldFinancePlan = BigDecimal.ZERO;
            List<UserAccount> financeAccountList = userAccountService.getUserFinanceAccount(result.getUserId());
            for (UserAccount ua : financeAccountList) {
                totalHoldFinancePlan = totalHoldFinancePlan.add(ua.getValue());
            }
            //省心计划带回款利息
            BigDecimal waitInterest = lendOrderService.getFinancialWaitInterestByUserId(result.getUserId());
            totalHoldFinancePlan = totalHoldFinancePlan.add(waitInterest);
            zichan = zichan.add(totalHoldFinancePlan);
            zichan = BigDecimalUtil.down(zichan, 2);
            result.setZichan(zichan);
            result.setBalance(fuzhai);
            result.setValue(zichan.subtract(fuzhai));
            resultList.add(result);
        }

        pagination.setRows(resultList);
        pagination.setTotal((Integer) myBatisDao.get("count_getUserExtPaging", userVO));
        return pagination;
    }

    @Override
    public void sendRegisterMsg(String mobileNo) {
        VelocityContext context = new VelocityContext();
        String verifyCode = com.xt.cfp.core.util.StringUtils.generateVerifyCode();
        context.put("code", verifyCode);
        //将验证码存入redis方便后台查询
        ProcessValidCodeUtil.saveValidCode(ValidCodeEnum.REGISTER_USER, redisCacheManger, verifyCode, mobileNo);
        String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_REGISTER_VM, context);
        //把验证码放到redis里,过期时间为1分钟
        boolean result = redisCacheManger.setRedisCacheInfo(TemplateType.SMS_REGISTER_VM.getPrekey() + mobileNo, verifyCode, smsCodeTime);
        if (result)
            smsService.sendMsg(mobileNo, content);
        //发短信后增加次数
        try {
            this.setRegisterMsgCount(mobileNo, DateUtil.getNowShortDate(), 1, UserRegisterRedisKeyEnum.USER_REGISTER_COUNT_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendValidCode(String mobileNo) {
        VelocityContext context = new VelocityContext();
        String verifyCode = com.xt.cfp.core.util.StringUtils.generateVerifyCode();
        context.put("code", verifyCode);
        //将验证码存入redis方便后台查询
        ProcessValidCodeUtil.saveValidCode(ValidCodeEnum.VALID_MSG, redisCacheManger, verifyCode, mobileNo);
        String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_VALID_VM, context);
        //把验证码放到redis里,过期时间为1分钟
        boolean result = redisCacheManger.setRedisCacheInfo(TemplateType.SMS_VALID_VM.getPrekey() + mobileNo, verifyCode, smsCodeTime);
        if (result)
            smsService.sendMsg(mobileNo, content);
    }

    @Override
    public void sendRetrievePassword(String mobileNo) {
        VelocityContext context = new VelocityContext();
        String verifyCode = com.xt.cfp.core.util.StringUtils.generateVerifyCode();
        //将验证码存入redis方便后台查询
        //ProcessValidCodeUtil.saveValidCode(ValidCodeEnum.FIND_PWD, redisCacheManger, verifyCode, mobileNo);
        context.put("date", DateUtil.getDateLongMD(new Date()));
        context.put("code", verifyCode);
        String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_RETRIEVEPASSWORD_VM, context);
        //把验证码放到redis里,过期时间为1分钟
        boolean result = redisCacheManger.setRedisCacheInfo(TemplateType.SMS_RETRIEVEPASSWORD_VM.getPrekey() + mobileNo, verifyCode, smsCodeTime);
        if (result)
            smsService.sendMsg(mobileNo, content);

    }

    /**
     * 发送重设交易密码的短信
     *
     * @param mobileNo
     */
    @Override
    public void sendResetTradePassMsg(String mobileNo) {
        VelocityContext context = new VelocityContext();
        String verifyCode = com.xt.cfp.core.util.StringUtils.generateVerifyCode();
        context.put("date", DateUtil.getDateLongMD(new Date()));
        context.put("code", verifyCode);
        //将验证码存入redis方便后台查询
        //ProcessValidCodeUtil.saveValidCode(ValidCodeEnum.RESET_TRANSACTION_PWD, redisCacheManger, verifyCode, mobileNo);
        String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_RESETTRADEPASSMSG_VM, context);
        //把验证码放到redis里,过期时间为1分钟
        boolean result = redisCacheManger.setRedisCacheInfo(TemplateType.SMS_RESETTRADEPASSMSG_VM.getPrekey() + mobileNo, verifyCode, smsCodeTime);
        if (result)
            smsService.sendMsg(mobileNo, content);

    }

    @Override
    public InvitationCode getInvitationCodeByCode(String code) {
        return myBatisDao.get("INVITATION_CODE.getInvitationCodeByCode", code);
    }

    @Override
    public void sendmobileAuthenticationMsg(String mobileNo) {
        VelocityContext context = new VelocityContext();
        String verifyCode = com.xt.cfp.core.util.StringUtils.generateVerifyCode();
        context.put("date", DateUtil.getDateLongMD(new Date()));
        context.put("code", verifyCode);
        //将验证码存入redis方便后台查询
        ProcessValidCodeUtil.saveValidCode(ValidCodeEnum.CHANGE_MOBILE, redisCacheManger, verifyCode, mobileNo);
        String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_MOBILEAUTHENTICATION_VM, context);
        //把验证码放到redis里,过期时间为1分钟
        boolean result = redisCacheManger.setRedisCacheInfo(TemplateType.SMS_MOBILEAUTHENTICATION_VM.getPrekey() + mobileNo, verifyCode, smsCodeTime);
        if (result)
            smsService.sendMsg(mobileNo, content);
    }

    @Override
    public UserInfo login(String loginName, String loginPass) {
        if (StringUtils.isEmpty(loginName))
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("loginName", "null");

        if (StringUtils.isEmpty(loginPass))
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("loginPass", "null");
        UserInfo user = null;
        //手机号、登陆名登陆
        if (com.xt.cfp.core.util.StringUtils.mobileNoValidate(loginName)) {
            //todo 引入缓存，防止频繁访问数据库
            user = getUserByMobileNo(loginName.trim());
            if (user == null)
                throw new SystemException(UserErrorCode.MOBILENO_NOT_EXIST).set("loginName", loginName);
        } else {
            //todo 引入缓存，防止频繁访问数据库
            user = getUserByLoginName(loginName.trim());
            if (user == null)
                throw new SystemException(UserErrorCode.LOGINNAME_NOT_EXIST).set("loginName", loginName);
        }
        if (!user.getType().equals(UserType.COMMON.getValue()))
            throw new SystemException(UserErrorCode.CAN_NOT_LOGIN).set("loginName", loginName);
        String md5Encode = MD5Util.MD5Encode(loginPass.trim(), "utf-8");
        if (!user.getLoginPass().equals(md5Encode))
            return null;
        //附加最近登陆时间
        user.setLastLoginTime(getUserLastLoginTime(user.getUserId()));
        //用户邀请码
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", user.getUserId());
        param.put("type", 0);
        InvitationCode invite = userInfoExtService.getInviteCodeByUserId(param);
        if (invite != null) {
            user.setIcCode(invite.getInvitationCode());
        }
        return user;
    }

    private Date getUserLastLoginTime(Long userId) {
        return myBatisDao.get("USER_INFO.getUserLastLoginTime", userId);
    }

    @Override
    public boolean hasIdentityAuthentication(Long userId) {
        UserInfoVO userExt = this.getUserExtByUserId(userId);
        return !(userExt == null || UserIsVerifiedEnum.NO.getValue().equals(userExt.getIsVerified()));

    }

    @Override
    public void recordUser(UserInfo loginUser) {
        //把用户创建时间复用，记录当前用户登陆时间
        loginUser.setCreateTime(new Date());
        myBatisDao.update("USER_INFO.updateRecordUser", loginUser);
    }


    public static Integer getSmsCodeTime() {
        return smsCodeTime;
    }

    @Property(name = "register_smscode_time")
    public static void setSmsCodeTime(Integer smsCodeTime) {
        UserInfoServiceImpl.smsCodeTime = smsCodeTime;
    }

    @Override
    public boolean isBidPassEqualLoginPass(Long userId) {
        UserInfo user = this.getUserByUserId(userId);
        return user.getLoginPass().equals(user.getBidPass());
    }

    @Override
    @Transactional
    public void dd() {
        List<UserList> lists = myBatisDao.getList("USER_LIST.select");

        Map<String, Long> map = new HashMap<String, Long>();
        for (UserList userList : lists) {

            String bidPass = MD5Util.MD5Encode(String.valueOf("111111"), "UTF-8").replace("-", "");
            String loginName = userList.getClearUser();//登录名
            String loginPass = bidPass;//登录密码
            String mobileNo = String.valueOf(userList.getCellph() != 0 ? userList.getCellph() : "");//手机号
            UserSource bydesc = UserSource.getBydesc(userList.getReason());
            String source = bydesc == null ? UserSource.WEB.getValue() : bydesc.getValue();//注册来源
            String createTime = userList.getZcsj();//注册时间
            String tjrId = userList.getTjrId();

            //（1）用户基本数据
            UserInfo userInfo = new UserInfo();
            userInfo.setBidPass(bidPass);
            userInfo.setLoginName(loginName);
            userInfo.setLoginPass(loginPass);
            userInfo.setMobileNo(mobileNo);
            userInfo.setSource(source);
            userInfo.setCreateTime(DateUtil.parseStrToDate(createTime, "yyyy-MM-dd HH:mm:ss"));
            userInfo.setType(UserType.COMMON.getValue());
            userInfo.setStatus(UserStatus.NORMAL.getValue());
            this.myBatisDao.insert("USER_INFO.insert", userInfo);
            map.put(userList.getId(), userInfo.getUserId());
            //（2）记录用户登陆信息（统计表）
            myBatisDao.insert("USER_INFO.recordUser", userInfo);

            String realName = userList.getName();//真实姓名
            String idCard = userList.getId();//身份证号
            String isVerified = UserIsVerifiedEnum.YES.getValue();
            if (StringUtils.isEmpty(idCard) || StringUtils.isEmpty(realName)) {
                isVerified = UserIsVerifiedEnum.NO.getValue();
            }
            Date birthday = null;//出生日期
            String sex = null;//性别
            Long tjId = null;
            //记录邀请好友ID
            if (StringUtils.isNotEmpty(tjrId)) {
                tjId = map.get(tjrId);
            }
            if (isVerified.equals(UserIsVerifiedEnum.YES.getValue())) {
                birthday = DateUtil.parseStrToDate(IdCardUtils.getBirthByIdCard(idCard), "yyyyMMdd");
                sex = IdCardUtils.getGenderByIdCard(idCard).equalsIgnoreCase("M") ? "1" : "0";
            }
            String mobileNo2 = mobileNo;//手机号
            //（3）用户扩展数据
            UserInfoExt userInfoExt = new UserInfoExt();
            userInfoExt.setMobileNo(userInfo.getMobileNo());
            userInfoExt.setIdCard(idCard);
            userInfoExt.setRealName(realName);
            userInfoExt.setIsVerified(isVerified);
            userInfoExt.setBirthday(birthday);
            userInfoExt.setSex(sex);
            userInfoExt.setUserId(userInfo.getUserId());
            userInfoExt.setIsVerified(isVerified);
            userInfoExt.setRecUserId(tjId);
            myBatisDao.insert("USER_INFO_EXT.insert", userInfoExt);

            Long userId = userInfo.getUserId();
            String accTypeCode = AccountConstants.AccountTypeEnum.LENDER_ACCOUNT.getValue();
            BigDecimal availValue = userList.getKyzc();
            String accStatus = AccountConstants.AccountStatusEnum.NORMAL.getValue();
            Date accCreateTime = userInfo.getCreateTime();
            this.userAccountService.initUserAccount(userId, AccountConstants.AccountTypeEnum.BORROW_ACCOUNT);

            //（4）用户账户数据
            UserAccount userAccount = new UserAccount();
            userAccount.setUserId(userId);
            userAccount.setAccTypeCode(accTypeCode);
            userAccount.setValue(BigDecimal.ZERO);
            userAccount.setValue2(BigDecimal.ZERO);
            userAccount.setAvailValue(BigDecimal.ZERO);
            userAccount.setAvailValue2(BigDecimal.ZERO);
            userAccount.setFrozeValue(BigDecimal.ZERO);
            userAccount.setFrozeValue2(BigDecimal.ZERO);
            userAccount.setAccStatus(accStatus);
            userAccount.setCreateTime(accCreateTime);
            this.userAccountService.createUserAccount(userAccount);

            AccountValueChangedQueue queue = new AccountValueChangedQueue();

            AccountValueChanged changed = new AccountValueChanged(userAccount.getAccId(), availValue, availValue,
                    AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_SYS_DATATRANS_INCOME.getValue(),
                    AccountConstants.AccountChangedTypeEnum.DATA_TRANS.getValue(), AccountConstants.VisiableEnum.HIDDEN.getValue(), 0l,
                    AccountConstants.OwnerTypeEnum.USER.getValue(), userInfo.getUserId(), userInfo.getCreateTime(),
                    com.xt.cfp.core.util.StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.DATA_TRANS_INCOME, availValue), false
            );
            queue.addAccountValueChanged(changed);
            this.userAccountOperateService.execute(queue);

            //（5）插入用户邀请码
            InvitationCode invite = new InvitationCode();
            invite.setUserId(userInfo.getUserId());
            invite.setType(InviteFriendsType.LINK_INVITE.getValue());
            myBatisDao.insert("INVITATION_CODE.insert", invite);
        }

    }

    @Override
    @Transactional
    public void createSystemUser() {
        UserInfo userInfo = new UserInfo();
        String password = MD5Util.MD5Encode("cfp", "utf-8");
        userInfo.setBidPass(password);
        userInfo.setLoginPass(password);
        userInfo.setStatus(UserStatus.NORMAL.getValue());
        userInfo.setType(UserType.SYSTEM.getValue());
        userInfo.setCreateTime(new Date());
        userInfo.setEncryptMobileNo("15810000000");
        userInfo.setLastLoginTime(new Date());
        userInfo.setLoginName("system001");
        userInfo.setMobileNo("15810000000");
        userInfo.setSource(UserSource.PLATFORM.getValue());
        userInfo.setUserId(0l);
        //保存
        myBatisDao.insert("USER_INFO.insertWithId", userInfo);
        //初始化用户账户信息
        userAccountService.initUserAccount(0l, AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT);
        userAccountService.initUserAccount(0l, AccountConstants.AccountTypeEnum.PLATFORM_OPERATING);
        userAccountService.initUserAccount(0l, AccountConstants.AccountTypeEnum.PLATFORM_PAYMENT);
        userAccountService.initUserAccount(0l, AccountConstants.AccountTypeEnum.PLATFORM_RISK);
        //记录用户登陆信息（统计表）
        myBatisDao.insert("USER_INFO.recordUser", userInfo);
        //把(认证)手机号放到ext表
        UserInfoExt userInfoExt = new UserInfoExt();
        userInfoExt.setMobileNo(userInfo.getMobileNo());
        userInfoExt.setUserId(userInfo.getUserId());
        myBatisDao.insert("USER_INFO_EXT.insert", userInfoExt);
    }

    @Override
    public String getQrImg(long userId) {
        String fileName = "QR_" + Base64Utils.encode(String.valueOf(userId).getBytes()) + ".jpg";
        File file = new File(PropertiesUtils.getInstance().get("QRCORD_DIR") + fileName);
        if (!file.exists()) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", userId);
            param.put("type", 0);
            InvitationCode invite = userInfoExtService.getInviteCodeByUserId(param);
            String invitecode = invite.getInvitationCode();
            String visit_path = PropertiesUtils.getInstance().get("INVITE_FRIENDS_VISIT_PATH") + "?invite_code=" + invitecode;

            MatrixToImageWriter.encode(visit_path,
                    Integer.parseInt(PropertiesUtils.getInstance().get("QRCORD_HEIGHT")),
                    Integer.parseInt(PropertiesUtils.getInstance().get("QRCORD_WIDTH")),
                    PropertiesUtils.getInstance().get("QRCORD_DIR") + fileName);
        }
        return PropertiesUtils.getInstance().get("QRCORD_HREF") + fileName;

    }


    @Override
    @Transactional
    public UserInfo mobileAuthentValidate(UserInfo user, String phone, UserSource updateSource) {
        user = getUserByUserId(user.getUserId());
        String beforeNo = user.getMobileNo();
        user.setMobileNo(phone);
        updateUser(user);
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(user.getUserId());
        userInfoExt.setMobileNo(phone);
        userInfoExtService.updateUserInfoExt(userInfoExt);
        // 记录手机号变更记录
        userInfoExtService.addMobileHistory(user.getUserId(), beforeNo, phone, updateSource);
        return user;
    }

    @Override
    public Pagination<SalesUserInfo> getSalesUserPaging(int pageNo, int pageSize, SalesUserInfo salesUserInfo, Object customParams) {
        Pagination<SalesUserInfo> re = new Pagination<SalesUserInfo>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("salesUserInfo", salesUserInfo);
        params.put("adminId", salesUserInfo.getAdminId());
        params.put("customParams", customParams);

        SalesAdminOrganizetion organizetionInfobyAdminId = salesOrganizeInfoService.getOrganizetionInfobyAdminId(salesUserInfo.getAdminId());
        List<SalesOrganizationInfo> organizeByPId = salesOrganizeInfoService.getOrganizeByPId(organizetionInfobyAdminId.getOrganizeId());
        if (organizeByPId == null || organizeByPId.size() == 0) {
            params.put("isLeaf", "isLeaf");
        }


        int totalCount = this.myBatisDao.count("getSalesUserInfoPaging", params);
        List<SalesUserInfo> users = this.myBatisDao.getListForPaging("getSalesUserInfoPaging", params, pageNo, pageSize);

        for (SalesUserInfo sui : users) {
            SalesOrganizationInfo org = salesOrganizeInfoService.getSupOrganizetionByAdminId(sui.getAdminId());
            if (org != null) {
                SalesAdminInfo leader = salesAdminInfoService.getLeaderByOrganizeId(org.getOrganizeId());
                if (leader != null)
                    sui.setSuperiorsAdmin(leader.getDisplayName());
            }
        }

        re.setTotal(totalCount);
        re.setRows(users);

        return re;
    }

    @Override
    public int getRegisterMsgCount(String phoneNo, String date,
                                   UserRegisterRedisKeyEnum userRegisterRedisKeyEnum) {
        if (StringUtils.isEmpty(phoneNo))
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set(
                    "mobileNo", "null");
        if (StringUtils.isEmpty(phoneNo))
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set(
                    "date", "null");
        String count = redisCacheManger.getRedisCacheInfo(userRegisterRedisKeyEnum.getValue()
                + date + phoneNo);
        try {
            return StringUtils.isEmpty(count) ? 0 : Integer.valueOf(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void setRegisterMsgCount(String phoneNo, String date, int add,
                                    UserRegisterRedisKeyEnum userRegisterRedisKeyEnum) throws Exception {
        if (StringUtils.isEmpty(phoneNo))
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set(
                    "mobileNo", "null");
        if (StringUtils.isEmpty(date))
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set(
                    "date", "null");
        int count = getRegisterMsgCount(phoneNo, date, userRegisterRedisKeyEnum);
        redisCacheManger.setRedisCacheInfo(userRegisterRedisKeyEnum.getValue() + date
                        + phoneNo, String.valueOf(count + add),
                DateUtil.getLeftTimesToday() > 0 ? DateUtil.getLeftTimesToday() : 1);
    }

    @Override
    public void setRegisterMsgConfirm(String phoneNo) {
        redisCacheManger.setRedisCacheInfo(UserRegisterRedisKeyEnum.USER_REGISTER_MSG_KEY.getValue() + phoneNo, "ok", 180);
    }

    @Override
    public boolean getRegisterMsgConfirm(String phoneNo) {
        String msg = redisCacheManger.getRedisCacheInfo(UserRegisterRedisKeyEnum.USER_REGISTER_MSG_KEY.getValue() + phoneNo);
        redisCacheManger.destroyRedisCacheInfo(UserRegisterRedisKeyEnum.USER_REGISTER_MSG_KEY.getValue() + phoneNo);
        return StringUtils.isBlank(msg);
    }

    @Override
    public Map<String, String> checkImgCode(String imgCode, String id) {
        Map<String, String> map = new HashMap<String, String>();
        String imgCodeRedis = redisCacheManger.getRedisCacheInfo(UserRegisterRedisKeyEnum.USER_REGISTER_IMG_KEY.getValue() + id);

        if (com.xt.cfp.core.util.StringUtils.isNull(imgCodeRedis)) {
            map.put("imgCode", JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.IMGAE_CODE_TIME_OUT.getDesc()).toJson());
            return map;
        }

        if (com.xt.cfp.core.util.StringUtils.isNull(imgCode)) {
            map.put("imgCode", JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.IMGAE_CODE_NOT_EXIST.getDesc()).toJson());
            return map;
        }

        if (!StringUtils.equalsIgnoreCase(imgCode, imgCodeRedis)) {
            map.put("imgCode", JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.IMGAE_CODE_ERROR.getDesc()).toJson());
            return map;
        }

        map.put("imgCode", JsonView.JsonViewFactory.create().success(true).toJson());

        return map;
    }

    @Override
    public void setRegisterImgCode(UserRegisterRedisKeyEnum userRegisterImgKey,
                                   String id, String imgCode, int seconds) {
        if (StringUtils.isEmpty(id))
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set(
                    "id", "null");
        if (StringUtils.isEmpty(imgCode))
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set(
                    "imgCode", "null");
        redisCacheManger.setRedisCacheInfo(userRegisterImgKey.getValue() + id, imgCode, seconds);
    }


    @Override
    public boolean isRegisterMsgValid(String mobileNo) {
        long time = redisCacheManger.getKeysValidTime(TemplateType.SMS_REGISTER_VM.getPrekey() + mobileNo);
        //键值在redis失效,或者不存在
        if (time <= 0) {
            return true;
        }
        //业务上验证码只有60秒存在时间
        if ((smsCodeTime - Long.valueOf(time).intValue()) < 60) {
            return false;
        }
        return true;
    }

    /**
     * 初始化用户注册路径
     *
     * @param registerConfig 0-旧版 , 1-新版
     */
    @Property(name = "registerConfig")
    public void setInitRegisterPath(String registerConfig) {
        if ("1".equals(registerConfig)) {
            registerPath = "/register/registernew";
        } else {
            registerPath = "/register/userRegister";
        }
    }

    @Override
    public String getRegisterPath() {
        return registerPath;
    }


    @Override
    public Pagination<UserValidCode> findAllUserValidCodes(int pageNo, int pageSize, String userMobile, String codeType) {
        Pagination<UserValidCode> re = new Pagination<UserValidCode>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);
        if (!com.xt.cfp.core.util.StringUtils.isNull(codeType)) {
            if (codeType.equals("-1")) {
                codeType = null;
            }
        }
        int totalCount = ProcessValidCodeUtil.getValusCountByConditionAndPaging(userMobile, codeType, redisCacheManger);
        List<String> list = ProcessValidCodeUtil.getValusByConditionAndPaging(userMobile, codeType, pageNo, pageSize, redisCacheManger);
        List<UserValidCode> results = ProcessValidCodeUtil.changeToUserValidCode(list);

        re.setTotal(totalCount);
        re.setRows(results);
        return re;
    }


    @Override
    public UserInfo getUserTypeInfo(String idCard, String trueName, String userType, String verified) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("idCard", idCard);
        params.put("trueName", trueName);
        params.put("userType", userType);
        params.put("verified", verified);
        return myBatisDao.get("USER_INFO.getUserInfoTypeByIdCardAndName", params);
    }

    /**
     * 查询是否为定向用户
     */
    @Override
    public int normalOrOrienta(Long userId, Long applicationId) {
        // TODO Auto-generated method stub
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("userId", userId);
        map.put("applicationId", applicationId);
        return myBatisDao.get("USER_INFO.getUserInfonormalOrOrienta", map);
    }


    @Override
    public int countOtypeByLendOrderId(Long lendOrderId) {
        // TODO Auto-generated method stub
        return myBatisDao.get("USER_INFO.getCountOtypeByLendOrderId", lendOrderId);
    }


    @Override
    public String getOrientPass(Long lendOrderId) {
        // TODO Auto-generated method stub
        return myBatisDao.get("USER_INFO.getOrientPass", lendOrderId);
    }


    @Override
    public String getOrientPassLoan(Long loanApplicationId) {
        // TODO Auto-generated method stub
        return myBatisDao.get("USER_INFO.getOrientPassLoanLoanApplicationId", loanApplicationId);
    }


    @Override
    public LoanOrientation countLoanOrientation(Long loanApplicationId) {
        // TODO Auto-generated method stub
        return myBatisDao.get("LOAN_ORIENTATION.selectByPrimaryKey", loanApplicationId);
    }


    @Override
    public List<LoanOrientation> countLoanOrientationList(Long loanApplicationId) {
        // TODO Auto-generated method stub
        return myBatisDao.getList("LOAN_ORIENTATION.selectByPrimaryKey", loanApplicationId);
    }


    @Override
    public List<LoanOrientation> getUserInfoByLoanApplicationId(Long loanApplicationId) {
        // TODO Auto-generated method stub
        return myBatisDao.getList("LOAN_ORIENTATION.getUserInfoByLoanApplicationId", loanApplicationId);
    }


    @Override
    public int countOrientByLoanApplicationId(Long loanApplicationId) {
        // TODO Auto-generated method stub
        return myBatisDao.get("LOAN_ORIENTATION.selectByPrimaryKeyCount", loanApplicationId);
    }


    @Override
    public UserInfoExt getUserExtByMobileNo(String mobileNo) throws SystemException {
        if (StringUtils.isEmpty(mobileNo))
            throw new SystemException(ValidationErrorCode.ERROR_STRING_CAN_NOT_BE_EMPTY).set("mobileNo", mobileNo);

        List<UserInfoExt> users = myBatisDao.getList("USER_INFO.getUserExtByMobile", mobileNo);
    /*if(users!=null&&users.size()>1){
        throw new SystemException(ValidationErrorCode.ERROR_MORE_RESULTS).set("mobileNo", mobileNo);
	}*/
        return users != null && users.size() > 0 ? users.get(0) : null;
    }


    @Override
    public List<UserInfo> findCRMUserAndCustomers(String code, Long userId) {
        Map map = new HashMap();
        map.put("code", code);
        map.put("userId", userId);
        return myBatisDao.getList("USER_INFO.getCRMUserAndCustomers", map);
    }

    @Override
    public List<UserInfo> getUserInfoByMobileNosAndType(List mobileNos, String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("mobiles", mobileNos);
        map.put("type", type);
        return myBatisDao.getList("USER_INFO.getUserInfoByMobileNosAndType", map);
    }

    @Override
    public void recordMultilevelInvitation(Long userId) {
        try {
            UserInfoExt userInfoExt = myBatisDao.get("USER_INFO_EXT.selectByPrimaryKey", userId);
            if (null != userInfoExt) {
                MultilevelInvitation inv = myBatisDao.get("MULTILEVEL_INVITATION.selectByPrimaryKey", userInfoExt.getRecUserId());
                if (null != inv) {

                    MultilevelInvitation newInv = new MultilevelInvitation();
                    newInv.setUserId(userId);//用户id
                    newInv.setRecommendUserId(inv.getUserId());//推荐用户id
                    newInv.setSaleByUserId(inv.getSaleByUserId());//所属销售的用户id
                    newInv.setHierarchy(inv.getHierarchy() + 1);//层级(从1开始)
                    newInv.setCreateTime(new Date());//创建时间

                    myBatisDao.insert("MULTILEVEL_INVITATION.insert", newInv);

                }
            }
        } catch (Exception e) {
            logger.info("新注册用户：" + userId + "，记录多级邀请关系，失败！" + e.getMessage());
        }
    }


    @Override
    public MultilevelInvitation getMultilevelInvitationByUserId(Long userId) {
        return myBatisDao.get("MULTILEVEL_INVITATION.selectByPrimaryKey", userId);
    }


    @Override
    public void saveUserJsession(String userId, String jsession) {
        if (redisCacheManger.testConn() && !com.xt.cfp.core.util.StringUtils.isNull(jsession)) {
            UserLoginHelper.saveUserJsession(redisCacheManger, jsession, userId);
        }
    }

    @Override
    public boolean validUserJsession(String userId, String jsession) {
        boolean result = false;
        if (redisCacheManger.testConn()) {
            String session = UserLoginHelper.getUserJsession(redisCacheManger, userId);
            if (null != jsession && jsession.equals(session)) {
                result = true;
            }
        } else {
            result = true;
        }

        return result;
    }
}
