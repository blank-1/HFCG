package com.xt.cfp.core.service;

import com.xt.cfp.core.constants.UserSource;
import com.xt.cfp.core.pojo.InvitationCode;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.util.Pagination;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface UserInfoExtService {

    boolean checkIDCard(String idCode, String trueName);

    /**
     * 添加用户扩展
     * 注：需自行添加主键值
     */
    UserInfoExt addUserInfoExt(UserInfoExt userInfoExt);

    /**
     * 修改用户扩展
     */
    UserInfoExt updateUserInfoExt(UserInfoExt userInfoExt);

    /**
     * 根据ID加载一条用户扩展
     *
     * @param userId 用户ID
     */
    UserInfoExt getUserInfoExtById(Long userId);

    /**
     * 根据ID加载一条用户扩展
     *
     * @param userId 用户ID
     */
    UserInfoExt getUserInfoExtById(Long userId, boolean isLock);

    /**
     * 根据手机号 和身份证前4 后4 查找
     *
     * @param userId 用户ID
     */
    UserInfoExt getUserExtByMobileAanIndentInfo(String mobile, String before, String after);

    /**
     * 根据邀请码ID查询用户ID
     *
     * @param param
     * @return
     */
    InvitationCode getUserInfoByInviteId(InvitationCode param);

    /**
     * 查询用户邀请的好友
     *
     * @param userId
     * @return
     */
    Pagination<UserInfoVO> getUserInviteFriends(int pageNo, int pageSize, Long userId);

    /**
     * 添加身份验证
     *
     * @param id
     * @param trueName
     * @return
     */
    Boolean identityAuthentication(String idCard, String trueName, Long userId);

    /**
     * 添加存管开户后的认证信息
     *
     * @param idCard   证件号
     * @param realName 实名
     * @param userId   用户id
     */
    void updateIdentityInfo(String idCard, String realName, Long userId, String cardNo);

    /**
     * 根据用户id及类型查找用户的邀请码
     *
     * @param param
     * @return
     */
    InvitationCode getInviteCodeByUserId(Map<String, Object> param);

    /**
     * 获取当前用户的资产
     *
     * @param userId
     * @return
     */
    BigDecimal getUserZiChan(Long userId);

    /**
     * 获取当前用户的负债
     *
     * @param userId
     * @return
     */
    BigDecimal getUserFuZhai(Long userId);

    /**
     * 根据身份证号，查询一条用户扩展信息
     *
     * @param idCard 身份证号
     */
    List<UserInfoExt> getUserInfoExtByIdCardAndSource(String idCard);

    /**
     * 根据身份证号和名字查询身份证是否已经绑定过了
     */
    int identityBindingExist(String idCard, String trueName);


    /**
     * 通过身份证获取平台用户扩展信息
     *
     * @param mobileNo
     * @return UserInfoExt
     */
    List<UserInfoExt> getCommonUserExtByIdCard(String idCard);

    /**
     * 添加手机号变更记录
     *
     * @param userId       用户id
     * @param beforeNo     变更前手机号
     * @param afterNo      变更后手机号
     * @param updateSource 客户端来源
     * @return
     */
    void addMobileHistory(Long userId, String beforeNo, String afterNo, UserSource updateSource);

    /**
     * 根据邀请用户Id，查询用户列表
     *
     * @param recUserId 邀请用户Id
     * @return
     */
    List<UserInfoExt> getUserInfoExtByRecUserId(Long recUserId);

    /**
     * 根据邀请用户Id，查询用户列表
     *
     * @see #getUserInfoExtById(Long)
     */
    UserInfoExt getUserInfoExt4currentUser();

    List<UserInfoVO> getUserAllInvite(Long userId);

    /**
     * 查询连连用户
     */
    Pagination<UserInfoExt> getLLUser(UserInfoExt params, int pageNum, int pageSize);
}
