package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.IDCardCheckEnum;
import com.xt.cfp.core.constants.UserIsVerifiedEnum;
import com.xt.cfp.core.constants.UserSource;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.VoucherService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.SecurityUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserInfoExtServiceImpl implements UserInfoExtService {

    @Autowired
    private MyBatisDao myBatisDao;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private IDCardVerifiedService idCardVerifiedService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private CustomerCardService customerCardService;


    @Override
    public boolean checkIDCard(String idCode, String trueName) {

        IDCardVerified verified = idCardVerifiedService.findByNameIdCode(trueName, idCode);
        boolean checked = true;
        if (verified == null) {
            checked = authenticationService.verifyService(idCode, trueName);
            //todoed 调ID5接口返回验证结果
            if (checked) {
                idCardVerifiedService.insert(trueName, idCode, IDCardCheckEnum.SUCCESS.value2Char());
            } else {
                idCardVerifiedService.insert(trueName, idCode, IDCardCheckEnum.FAILURE.value2Char());
            }
        } else {
            checked = IDCardCheckEnum.SUCCESS.value2Char() == verified.getVerifiedResult();
        }

        return checked;
    }

    /**
     * 添加用户扩展
     * 注：需自行添加主键值
     */
    @Override
    public UserInfoExt addUserInfoExt(UserInfoExt userInfoExt) {
        myBatisDao.insert("USER_INFO_EXT.insert", userInfoExt);
        return userInfoExt;
    }

    /**
     * 修改用户扩展
     */
    @Override
    public UserInfoExt updateUserInfoExt(UserInfoExt userInfoExt) {
        myBatisDao.update("USER_INFO_EXT.updateByPrimaryKeySelective", userInfoExt);
        return userInfoExt;
    }

    /**
     * 根据ID加载一条用户扩展
     *
     * @param userId 用户ID
     */
    @Override
    public UserInfoExt getUserInfoExtById(Long userId) {
        return myBatisDao.get("USER_INFO_EXT.selectByPrimaryKey", userId);
    }

    @Override
    public UserInfoExt getUserInfoExtById(Long userId, boolean isLock) {
        if (!isLock)
            return getUserInfoExtById(userId);
        else
            return myBatisDao.get("USER_INFO_EXT.getUserInfoExtByIdAndLock", userId);
    }

    @Override
    public UserInfoExt getUserExtByMobileAanIndentInfo(String mobile,
                                                       String before, String after) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mobile", mobile);
        params.put("before", before);
        params.put("after", after);
        return myBatisDao.get("USER_INFO_EXT.getUserExtByMobileAanIndentInfo", params);
    }

    @Override
    public InvitationCode getUserInfoByInviteId(InvitationCode param) {
        return myBatisDao.get("INVITATION_CODE.selectBy", param);
    }

    @Override
    public Pagination<UserInfoVO> getUserInviteFriends(int pageNo, int pageSize, Long userId) {
        Pagination<UserInfoVO> pagination = new Pagination<UserInfoVO>();
        pagination.setCurrentPage(pageNo);
        pagination.setPageSize(pageSize);
        List<UserInfoVO> loanProducts = myBatisDao.getListForPaging("USER_INFO_EXT.getUserInviteFriends", userId, pageNo, pageSize);
        pagination.setRows(loanProducts);
        pagination.setTotal(myBatisDao.count("getUserInviteFriends", userId));
        return pagination;
    }

    @Override
    public Boolean identityAuthentication(String idCard, String trueName, Long userId) {
        boolean result = false;
        if (idCard == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("idCard", "null");
        if (trueName == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("trueName", "null");

        String birth = idCard.substring(6, 14);
        String sex = idCard.substring(16, 17);
        if (Integer.parseInt(sex) % 2 == 0) {
            sex = "0";
        } else {
            sex = "1";
        }
        if (checkIDCard(idCard, trueName)) {
            UserInfoExt userInfoExt = getUserInfoExtById(userId);
            userInfoExt.setSex(sex);
            userInfoExt.setRealName(trueName);
            userInfoExt.setIdCard(idCard);
            userInfoExt.setBirthday(DateUtil.parseStrToDate(birth, "yyyyMMdd"));
            //证明身份验证通过
            userInfoExt.setIsVerified(UserIsVerifiedEnum.YES.getValue());
            updateUserInfoExt(userInfoExt);
            result = true;
            //完成身份认证再奖励一百元财富卷
            voucherService.AuditRelease(userId);
        }
        return result;
    }

    /**
     * 添加存管开户后的认证信息
     *
     * @param idCard   证件号
     * @param trueName 实名
     * @param userId   用户id
     */
    @Override
    public void updateIdentityInfo(String idCard, String trueName, Long userId, String cardNo) {
        if (idCard == null) {
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("idCard", "null");
        }
        if (trueName == null) {
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("trueName", "null");
        }

        String birth = idCard.substring(6, 14);
        String sex = idCard.substring(16, 17);
        if (Integer.parseInt(sex) % 2 == 0) {
            sex = "0";
        } else {
            sex = "1";
        }
        UserInfoExt userInfoExt = getUserInfoExtById(userId);
        boolean isUpdate = true;
        if (userInfoExt == null) {
            isUpdate = false;
            userInfoExt = new UserInfoExt();
        }
        userInfoExt.setSex(sex);
        userInfoExt.setRealName(trueName);
        userInfoExt.setIdCard(idCard);
        userInfoExt.setBirthday(DateUtil.parseStrToDate(birth, "yyyyMMdd"));
        //证明身份验证通过
        userInfoExt.setIsVerified(UserIsVerifiedEnum.BIND.getValue());
        if (isUpdate) {
            updateUserInfoExt(userInfoExt);
        } else {
            userInfoExt.setUserId(userId);
            addUserInfoExt(userInfoExt);
        }

    }

    @Override
    @Transactional
    public InvitationCode getInviteCodeByUserId(Map<String, Object> param) {

        //判断有无邀请码
        InvitationCode invitationCode = myBatisDao.get("INVITATION_CODE.selectByUserId", param);
        //非正常注册用户查询结果可能为空
        if (invitationCode != null) {
            if (StringUtils.isEmpty(invitationCode.getInvitationCode())) {
                //历史用户没有邀请码，需要生成一个邀请码
                UserInfo user = userInfoService.getUserByUserId(invitationCode.getUserId());

                invitationCode.setInvitationCode(userInfoService.createInviteCode(user.getMobileNo()));

                myBatisDao.insert("INVITATION_CODE.updateByPrimaryKeySelective", invitationCode);
            }
        }
        return invitationCode;
    }


    @Override
    public BigDecimal getUserFuZhai(Long userId) {
        return myBatisDao.get("USER_INFO_EXT.getUserFuZhai", userId);
    }

    @Override
    public BigDecimal getUserZiChan(Long userId) {
        return myBatisDao.get("USER_INFO_EXT.getUserZichan", userId);
    }

    /**
     * 根据身份证号，查询一条用户扩展信息
     *
     * @param idCard 身份证号
     */
    @Override
    public List<UserInfoExt> getUserInfoExtByIdCardAndSource(String idCard) {
        return myBatisDao.getList("USER_INFO_EXT.getUserInfoExtByIdCardAndSource", idCard);
    }

    @Override
    public int identityBindingExist(String idCard, String trueName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("idCard", idCard);
        params.put("trueName", trueName);
        return myBatisDao.get("USER_INFO_EXT.identityBindingExist", params);
    }

    @Override
    public List<UserInfoExt> getCommonUserExtByIdCard(String idCard) {
        //判断参数是否为null
        if (StringUtils.isEmpty(idCard))
            throw new SystemException(ValidationErrorCode.ERROR_STRING_CAN_NOT_BE_EMPTY).set("idCard", idCard);
        return myBatisDao.getList("USER_INFO_EXT.getUserExtByIdCard", idCard);
    }

    @Override
    public void addMobileHistory(Long userId, String beforeNo, String afterNo, UserSource updateSource) {
        MobileHistory mobileHistory = new MobileHistory();
        mobileHistory.setUserId(userId);
        mobileHistory.setBeforeNo(beforeNo);
        mobileHistory.setAfterNo(afterNo);
        mobileHistory.setUpdateTime(new Date());
        mobileHistory.setUpdateSource(updateSource.getValue());
        myBatisDao.insert("MOBILE_HISTORY.insert", mobileHistory);
    }

    @Override
    public List<UserInfoExt> getUserInfoExtByRecUserId(Long recUserId) {
        return myBatisDao.getList("USER_INFO_EXT.getUserInfoExtByRecUserId", recUserId);
    }

    @Override
    public UserInfoExt getUserInfoExt4currentUser() {
        return getUserInfoExtById(SecurityUtil.getCurrentUser(true).getUserId());
    }

    @Override
    public List<UserInfoVO> getUserAllInvite(Long userId) {
        return myBatisDao.getList("USER_INFO_EXT.getUserInviteFriends", userId);
    }

    @Override
    public Pagination<UserInfoExt> getLLUser(UserInfoExt params, int pageNum, int pageSize) {
        Pagination<UserInfoExt> pagination = new Pagination<>();
        pagination.setCurrentPage(pageNum);
        pagination.setPageSize(pageSize);
        List<UserInfoExt> loanProducts = myBatisDao.getListForPaging("USER_INFO_EXT.getLLUser", params, pageNum, pageSize);
        pagination.setRows(loanProducts);
        pagination.setTotal(myBatisDao.count("USER_INFO_EXT.getLLUser", params));
        return pagination;
    }
}
