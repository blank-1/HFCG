package com.xt.cfp.core.service;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.constants.UserRegisterRedisKeyEnum;
import com.xt.cfp.core.constants.UserSource;
import com.xt.cfp.core.constants.UserStatus;
import com.xt.cfp.core.constants.UserType;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.SalesUserInfo;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.pojo.ext.UserValidCode;
import com.xt.cfp.core.util.Pagination;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by luqinglin on 2015/6/11.
 */
public interface UserInfoService {

    /**
     * 用户注册
     *
     * @param userInfo
     * @return
     */
    UserInfo regist(UserInfo userInfo, UserSource userSource, String inviteUserId) throws SystemException;

    /**
     * 用户注册
     *
     * @param userInfo
     * @return
     */
    UserInfo regist(UserInfo userInfo, UserSource userSource) throws SystemException;

    /**
     * 在微信端注册＋绑定
     *
     * @param userInfo
     * @param userSource
     * @param openId
     * @param invitationCode
     * @return
     */
    UserInfo registBinding(UserInfo userInfo, UserSource userSource, String openId, InvitationCode invitationCode);

    /**
     * 电销注册
     *
     * @param mobileNo
     * @return
     */
    UserInfo regist(String mobileNo);

    /**
     * 系统创建平台虚拟账户
     *
     * @param userName
     * @return
     */
    UserInfo createPlatFormUser(String userName, UserType userType);

    /**
     * 系统创建平台虚拟账户
     *
     * @param userName
     * @return
     */
    UserInfo createPlatFormUserForEnterprise(String userName, UserType userType,String mobileNo);

    /**
     * 登陆名是否已经存在
     *
     * @param loginName
     * @return
     */
    boolean isLoginNameExist(String loginName);

    /**
     * 手机号是否已存在
     *
     * @param mobileNo
     * @return
     */
    boolean isMobileExist(String mobileNo);

    /**
     * 根据UserId获取用户信息
     *
     * @param userId
     * @return
     */
    UserInfo getUserByUserId(Long userId);

    /**
     * 根据用户名获取用户信息
     *
     * @param loginName
     * @return
     */
    UserInfo getUserByLoginName(String loginName) throws SystemException;

    /**
     * 根据用户名获取用户信息(模糊查询)
     *
     * @param loginName
     * @return
     */
    List<UserInfo> getUserListByLoginName(String loginName, UserType userType) throws SystemException;

    /**
     * 根据手机号获取用户信息
     *
     * @param mobileNo
     * @return
     */
    UserInfo getUserByMobileNo(String mobileNo) throws SystemException;

    UserInfoExt getUserExtByMobileNo(String mobileNo) throws SystemException;

    /**
     * 修改用户的状态
     *
     * @param userId
     * @param userstatus
     */
    void changeUserStatus(Long userId, UserStatus userstatus);

    /**
     * 修改用户信息
     *
     * @param userInfo
     */
    void updateUser(UserInfo userInfo);


    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param userId
     * @return
     */
    UserInfo updatePassword(String oldPassword, String newPassword, Long userId);

    /**
     * 修改支付密码
     */
    UserInfo updatePayPassword(String oldPassword, String newPassword, Long userId);

    /**
     * 根据条件获取用户信息
     *
     * @return
     */
    List<UserInfo> getUserByCondition(UserInfo userInfo, Map<String, Object> customParams);

    /**
     * 获取分页用户信息
     *
     * @param pageNum
     * @param pageSize
     * @param userInfo
     * @param customParams
     * @return
     */
    Pagination<UserInfo> getUserPaging(int pageNum, int pageSize, UserInfo userInfo, Map<String, Object> customParams);

    /**
     * update 用户扩展信息
     *
     * @param userInfoExt
     * @return
     */
    UserInfoExt updateUserInfoExt(UserInfoExt userInfoExt);

    /**
     * update 用户个人信息
     *
     * @param userInfo
     * @return
     */
    String updateUserInfo(UserInfoVO userInfo);

    /**
     * get userinfoext
     *
     * @param userId
     * @return
     */
    UserInfoVO getUserExtByUserId(Long userId);

    /**
     * 头像上传
     *
     * @param userId
     * @param fileName
     * @param imgPath
     * @param url
     */
    void uploadPhoto(String userId, String fileName, String imgPath, String url);

    /**
     * 客户列表查询
     *
     * @param pageNo
     * @param pageSize
     * @param userVO
     * @return
     */
    Pagination<UserInfoVO> getUserExtPaging(int pageNo, int pageSize, UserInfoVO userVO);

    /**
     * 发送注册短信
     *
     * @param mobileNo
     */
    void sendRegisterMsg(String mobileNo);

    /**
     * 发送找回密码的短信
     *
     * @param mobileNo
     */
    void sendRetrievePassword(String mobileNo);

    /**
     * 发送修改手机验证的短信
     *
     * @param mobileNo
     */
    void sendmobileAuthenticationMsg(String mobileNo);


    /**
     * 登陆
     *
     * @param loginName
     * @param loginPass
     */
    UserInfo login(String loginName, String loginPass);

    /**
     * 判断一个用户是否做过身份认证
     *
     * @param userId
     * @return
     */
    boolean hasIdentityAuthentication(Long userId);

    /**
     * 记录用户登陆信息
     *
     * @param loginUser
     */
    void recordUser(UserInfo loginUser);

    /**
     * 判断用户的交易密码是否等于用户的登录密码
     *
     * @param userId
     * @return
     */
    boolean isBidPassEqualLoginPass(Long userId);

    void dd();

    void createSystemUser();

    String getQrImg(long userId);

    /**
     * 发送重设交易密码的短信
     *
     * @param mobileNo
     */
    void sendResetTradePassMsg(String mobileNo);

    /**
     * 根据邀请码查询
     *
     * @param code
     * @return
     */
    InvitationCode getInvitationCodeByCode(String code);

    /**
     * 检查并获取邀请码
     *
     * @param mobileNo
     * @return
     */
    String createInviteCode(String mobileNo);

    /**
     * 导出用户
     *
     * @param response
     */
    void exportExcel(HttpServletResponse response, UserInfoVO userInfoVO);

    /**
     * 获取指定类型用户个数
     *
     * @param userType
     * @return
     */
    int countAllUser(UserType userType);

    /**
     * 修改手机验证
     *
     * @param user
     */
    UserInfo mobileAuthentValidate(UserInfo user, String phone, UserSource userSource);

    /**
     * 电销用户列表
     *
     * @param pageNo
     * @param pageSize
     * @param salesUserInfo
     * @param o
     * @return
     */
    Pagination<SalesUserInfo> getSalesUserPaging(int pageNo, int pageSize, SalesUserInfo salesUserInfo, Object o);

    /**
     * 返回手机号当天发送注册验证码次数
     *
     * @param phoneNo
     * @param date
     * @return count
     */
    int getRegisterMsgCount(String phoneNo, String date, UserRegisterRedisKeyEnum userRegisterRedisKeyEnum);

    /**
     * 设置手机号当天发送注册验证码次数
     *
     * @param phoneNo
     * @param date
     * @return void
     */
    void setRegisterMsgCount(String phoneNo, String date, int add, UserRegisterRedisKeyEnum userRegisterRedisKeyEnum) throws Exception;

    /**
     * 发短信前设置手机号确认发送短信
     *
     * @param phoneNo
     */
    void setRegisterMsgConfirm(String phoneNo);

    /**
     * 发短信前获取手机号确认发送短信，获取后立即失效
     *
     * @param phoneNo
     * @return
     */
    boolean getRegisterMsgConfirm(String phoneNo);

    /**
     * 校验图片验证码
     *
     * @param imgCode
     * @return map
     */
    Map<String, String> checkImgCode(String imgCode, String auth);

    /**
     * 保存图片验证码到redis
     *
     * @param userRegisterImgKey
     * @param id
     * @param imgCode
     * @param seconds
     */
    void setRegisterImgCode(UserRegisterRedisKeyEnum userRegisterImgKey,
                            String id, String imgCode, int seconds);

    /**
     * 校验手机验证码是否失效
     *
     * @param mobileNo
     */
    boolean isRegisterMsgValid(String mobileNo);


    /**
     * 获取用户注册路径（新或旧版）
     */
    String getRegisterPath();

    Pagination<UserValidCode> findAllUserValidCodes(int pageNo, int pageSize, String userMobile, String codeType);

    /**
     * 查询普通用户
     *
     * @param idCard
     * @param trueName
     * @param userType
     * @param verified
     * @return
     */
    UserInfo getUserTypeInfo(String idCard, String trueName, String userType, String verified);

    /***
     * 查询是否为定向用户
     * @param userId
     * @return
     */
    int normalOrOrienta(Long userId, Long applicaionId);

    /***
     * 查询是否为定向用户
     * 根据订单表
     * @param lendOrderId
     * @return
     */
    int countOtypeByLendOrderId(Long lendOrderId);

    /***
     * 查询是否为定向密码
     * 根据订单表
     * @param lendOrderId
     * @return
     */
    String getOrientPass(Long lendOrderId);

    /***
     * 查询是否为定向密码
     * 根据loan_application_id
     * @return
     */
    String getOrientPassLoan(Long loanApplicationId);

    LoanOrientation countLoanOrientation(Long loanApplicationId);

    List<LoanOrientation> countLoanOrientationList(Long loanApplicationId);

    /**
     * 查询投标用户
     *
     * @param loanApplicationId
     * @return
     */
    List<LoanOrientation> getUserInfoByLoanApplicationId(Long loanApplicationId);

    /**
     * 查询是否有定向设置
     *
     * @param loanApplicationId
     * @return
     */
    int countOrientByLoanApplicationId(Long loanApplicationId);

    /**
     * 查询crm中的员工级3级下的客户
     *
     * @param code
     * @param userId
     * @return
     */
    List<UserInfo> findCRMUserAndCustomers(String code, Long userId);

    /**
     * 根据多个手机号和用户类型查询用户信息
     */
    List<UserInfo> getUserInfoByMobileNosAndType(List mobileNos, String type);

    /**
     * 新注册用户，记录多级邀请关系
     *
     * @param userId 新注册用户ID
     */
    void recordMultilevelInvitation(Long userId);

    /**
     * 根据userId，获取一条邀请信息
     *
     * @param userId
     * @return
     */
    MultilevelInvitation getMultilevelInvitationByUserId(Long userId);

    /**
     * 发送验证码模拟登录
     *
     * @param mobileNo
     */
    void sendValidCode(String mobileNo);

    void saveUserJsession(String userId, String jsession);

    boolean validUserJsession(String userId, String jsession);
}
