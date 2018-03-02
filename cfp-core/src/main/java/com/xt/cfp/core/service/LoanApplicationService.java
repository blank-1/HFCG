package com.xt.cfp.core.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.xt.cfp.core.pojo.*;
import org.springframework.web.multipart.MultipartFile;

import com.xt.cfp.core.constants.LoanApplicationStateEnum;
import com.xt.cfp.core.context.ParaContext;
import com.xt.cfp.core.pojo.ext.CustomerUploadSnapshotVO;
import com.xt.cfp.core.pojo.ext.LoanApplicationExtOne;
import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;
import com.xt.cfp.core.pojo.ext.LoanApplicationVO;
import com.xt.cfp.core.pojo.ext.RepaymentPlansAndDetailVO;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.service.matchrules.MatchCreditorVO;
import com.xt.cfp.core.util.Pagination;

/**
 * Created by renyulin on 15-6-23.
 */
public interface LoanApplicationService {

    LoanApplication findById(long loanApplicationId);


    List<LoanApplication> findByStates(LoanApplicationStateEnum... enums);

    void updateLoanAndAddVerify(LoanApplication loanApplication, long adminId, String fullContent);

    /**
	 * 借款列表
	 * @param pageNum 页
	 * @param pageSize 条
	 * @param loanApplicationCode 标的编号
	 * @param loanApplicationName 标的名称
	 * @param channel 标的来源
	 * @param loanType 类型
	 * @param realName 用户姓名
	 * @param idCard 身份证
	 * @param mobileNo 手机号
	 * @param applicationState 标的状态
	 * @return
	 */
	Pagination<LoanApplicationExtOne> getAllLoanApplicationList(int pageNum, int pageSize, String loanApplicationCode, String loanApplicationName,String channel, String loanType, String realName, String idCard, String mobileNo, String applicationState);
	
	/**
	 * 导出借款列表
	 * @param response
	 * @param loanApplicationCode
	 * @param loanApplicationName
	 * @param channel
	 * @param loanType
	 * @param realName
	 * @param idCard
	 * @param mobileNo
	 * @param applicationState
	 */
	void exportExcel(HttpServletResponse response,String loanApplicationCode, String loanApplicationName,String channel, String loanType, String realName, String idCard, String mobileNo, String applicationState);
	/**
	 * 借款列表
	 * @param pageNum
	 * @param pageSize
	 * @param loanApplication
	 * @return
	 */
	Pagination<LoanApplicationExtOne> getAllLoanApplicationList(int pageNum, int pageSize, LoanApplication loanApplication, String startTime,
			String endTime);
	
	/**
	 * 发标描述编辑
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	Object getAllLoanAppPublishDescEditList(int pageNum, int pageSize, LoanApplicationExtOne loanApplicationExtOne);
	
	/**
	 * 发标描述编辑，待发标列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	Object getLoanAppPagingByMainId(int pageNum, int pageSize, LoanApplicationExtOne loanApplicationExtOne);
	
	// get count by mainid
	Integer getLoanAppCountByMainId(Long mainLoanApplicationId);
	
	/**
	 * 待放款列表
	 * @param pageNum
	 * @param pageSize
	 * @param loanApplicationCode
	 * @param loanApplicationName
	 * @param channel
	 * @param loanType
	 * @param realName
	 * @param idCard
	 * @param mobileNo
	 * @return
	 */
    Pagination<LoanApplicationExtOne> getWaitLoanList(int pageNum,
                                                      int pageSize, String loanApplicationCode,
                                                      String loanApplicationName, String channel, String loanType,
                                                      String realName, String idCard, String mobileNo);


    void notice2FullBid(LoanApplication loanApplication, Date date) throws Exception;

    /**
     * 执行某个借款申请的流标动作
     * @param temp
     * @param date
     * @param timerExecLog
     * @throws Exception
     */
    void noticeFailLoan(LoanApplication temp, Date date, TimerExecLog timerExecLog) throws Exception;

    /**
	 * 上传图片
	 * @param loanApplicationId
	 * @param file
	 * @param type
	 * @param msgName
	 * @param typeList
	 * @return
	 * @throws IOException
	 */
	Map<String,Object> saveUploadSnapshot(String loanApplicationId, MultipartFile file, String type, String msgName, String typeList, String rootPath, String isCode) throws IOException;
	
	/**
	 * 图片入库
	 * @param userId
	 * @param fileName
	 * @param imgPath
	 * @param url
	 * @param type
	 * @param status
	 * @param seqNum
	 * @param loanApplicationId
	 * @return
	 */
	CustomerUploadSnapshot saveRelatedAccessories (Long userId, String fileName, String imgPath, String url, String type, String status, Long seqNum, Long loanApplicationId, String thumbnailUrl, String isCode);
	
	CustomerUploadSnapshot insertCustomerUploadSnapshot(CustomerUploadSnapshot customerUploadSnapshot);
	
	/**
	 * 获取大图地址
	 * @param cusId
	 * @return
	 */
	Attachment getAttachmentBycusId(Long cusId);
	/**
	 * 获得快照列表
	 * @param loanApplicationId
	 * @param type
	 * @return
	 */
	List<CustomerUploadSnapshot> getcustomerUploadSnapshotList(Long loanApplicationId, String type);
	
	//by main
	List<CustomerUploadSnapshot> getCustomerUploadSnapshotListByMainId(Long mainLoanApplicationId, String type);
	/**
	 * 得到快照信息
	 * @param cusId
	 * @return
	 */
	CustomerUploadSnapshot getcustomerUploadSnapshotDetails(Long cusId);
	/**
	 * 得到最大的图片顺序
	 * @param loanApplicationId
	 * @param type
	 * @return
	 */
	int getCustomerSeqNum (Long loanApplicationId, String type);
	
	//by mainid
	int getCustomerSeqNumByMainId (Long mainLoanApplicationId, String type);
	/**
	 * 删除图片  (逻辑删)
	 * @param cusId
	 */
	void delImg(Long cusId,String status, Attachment atta, String rootPath) throws IOException;
	void imgToFormal(Long mainLoanApplicationId, String rootPath) throws IOException;

    void submitMakeLoan(long loanApplicationId, long adminId, String desc, char channel, Date now, Date financialLendDate) throws Exception;


    void createAgreement();

    void createAgreement(long loanApplicationId);
    /**
     * 债权转让合同
     * */
    void createAgreementTurnRights(long loanApplicationId,List<CreditorRights> newCreditorRights);

    /**
     * 创建提现单，账户冻结
     * @param paraContext
     *  loanApplicationId:借款申请ID
     *  accountId:借款账户ID
     *  adminId：管理员ID
     *  customerCard:客户卡信息
     *  balance:提现金额
     *  businessType：费用类型
     *  display:是否显示
     *  ownerType：流水所有者类型
     *  ownerId：流水所有者ID
     *  desc:描述信息
     *  needCheck：是否需要判断余额情况
     *  accountValueChangedQueue：AccountValueChangedQueue
     * @throws Exception
     */
    WithDraw commitCash2Card(ParaContext paraContext) throws Exception;
    
    /**
     * 添加借款申请
     */
    LoanApplication addLoanApplication(LoanApplication loanApplication);
    
    /**
     * 修改借款申请
     */
    LoanApplication updateLoanApplication(LoanApplication loanApplication);

    LoanApplication findLockById(long loanApplicationId);

    void update(Map<String,Object> map);
    
    /**
     * 根据ID加载一条借款申请
     * @param loanApplicationId 借款申请ID
     */
    LoanApplication getLoanApplicationById(Long loanApplicationId);

    /**
     * 查询在指定借款产品下，指定条件的借款申请
     *  loanAppParaMap
     *      createTimeForOrder:创建时间排序
     *      forLoanBalance：剩余可投金额大于0
     *      forLoanBalanceForOrder：剩余可投金额排序
     * @param loanProductId
     * @param loanAppParaMap
     * @return
     */
    List<LoanApplicationVO> findByProductIdOrderBy(long loanProductId, Map<String, Object> loanAppParaMap);
    
    /**
     * 添加借款第一页保存【main】
     * @param loan 借款信息
     * @param basic 基础信息
     * @param user 用户信息
     * @param ext 用户扩展
     * @param existsUser 是否存在该用户
     */
    MainLoanApplication saveLoanPart1(MainLoanApplication mainLoan, CustomerBasicSnapshot basic, UserInfo user, UserInfoExt ext, Boolean existsUser);


    
    /**
     * 添加借款第二页保存
     * @param loan 借款信息
     * @param bornAddr 籍贯地址
     * @param registAddr 户口所在地
     * @param residenceAddr 现住址
     * @param workingAddr 单位地址
     * @param basic 基础信息
     * @param ext 用户扩展
     * @param work 工作信息
     * @param contactsList 联系人列表
     * @param card 银行卡信息
     * @param feesItems 借款申请费用表集合
     */
    MainLoanApplication saveLoanPart2(MainLoanApplication mainLoan, Address bornAddr, Address registAddr, 
    		Address residenceAddr, Address workingAddr, CustomerBasicSnapshot basic, UserInfoExt ext,
    		CustomerWorkSnapshot work, List<CustomerContactsSnapshot> contactsList, CustomerCard card,
    		List<LoanApplicationFeesItem> feesItems);
    
    /**
     * 添加借款第三页保存
     * @param houseAddr 房屋地址
     * @param house 抵押信息
     */
    CustomerHouseSnapshot saveLoanPart3(Address houseAddr, CustomerHouseSnapshot house);
    
    /**
     * 提交初审
     * @param loan 借款信息
     * @param lendOrder 出借订单
     * @param lendOrderBidDetail 出借订单投标明细
     * @param payOrder 支付订单
     * @param payOrderDetail 支付订单明细
     * @param creditorRights 债权信息
     */
    @Deprecated
    LoanApplication saveLoanSubmit(LoanApplication loan, LendOrder lendOrder, LendOrderBidDetail lendOrderBidDetail, 
    		PayOrder payOrder, PayOrderDetail payOrderDetail, CreditorRights creditorRights, 
    		List<RepaymentPlansAndDetailVO> repaymentPlansAndDetailVOs,String source);

    MainLoanApplication submitLoanAppcalication(MainLoanApplication mainLoanApplication,String rootPath) throws Exception;

    /**
     * 借款列表（前端使用）
     * @param pageNo
     * @param pageSize
     * @param loanVo
     * @param customParams
     * @return
     */
    Pagination<LoanApplicationListVO> getLoanApplicationPaging(int pageNo, int pageSize,  LoanApplicationListVO loanVo, Map<String, Object> customParams);
    /**
     * 债券市场列表
     * @param pageNo
     * @param pageSize
     * @param loanVo
     * @param customParams
     * @return
     */
    Pagination<LoanApplicationListVO> getTurnCreditRightPaging(int pageNo, int pageSize, LoanApplicationListVO loanVo, Map<String, Object> customParams);
    /**
     * 借款预期收益列表
     * @return
     */
    List<BigDecimal> getLoanRateTypes(LoanApplicationStateEnum ... loanApplicationStateEnum);

    /**
     * 借款期限列表
     * @param loanApplicationStateEnum
     * @return
     */
    List<Integer> getDurationTypes(LoanApplicationStateEnum ... loanApplicationStateEnum);

    /**
     * 获取借款标详情
     * @param loanApplicationNo
     * @return
     */
    LoanApplicationListVO getLoanApplicationVoById(Long loanApplicationNo);
    
    // by mainid
    LoanApplicationListVO getLoanApplicationVoByMainId(Long loanApplicationNo);
    
	/**
	 * 累计借款金额
	 * @param userId
	 * @return
	 */
	BigDecimal getTotalLoanAmount(Long userId);

    /**
     * 获取认证信息
     * @param loanApplicationNo
     * @return
     */
    List<Integer> getAuthInfo(Long loanApplicationNo);

    /**
     * 获取用户上传图片
     * @param loanApplicationId
     * @return
     */
    List<CustomerUploadSnapshotVO> getcustomerUploadAttachment(Long loanApplicationId, String isCode);
    
    List<CustomerUploadSnapshotVO> getCustomerUploadAttachmentByMainId(Long mainLoanApplicationId, String isCode);

    /**
     * 投标中的标的已投总额
     * */
    BigDecimal getBidedBalance(long loanApplicationId);

	Pagination getRepaymentList(int pageNum, int pageSize, String loanApplicationCode, String loanApplicationName, String channel, String loanType,
			String realName, String idCard, String mobileNo, String planState, String beginRepaymentDay, String endRepaymentDay);
	
	/**
	 * 获取所有数据（导出excel报表专用）
	 */
	List getRepaymentAllList(String loanApplicationCode, String loanApplicationName, String channel, String loanType,
			String realName, String idCard, String mobileNo, String planState, String beginRepaymentDay, String endRepaymentDay);

    /**
     * 通过借款申请id查询 出借产品发布明细
     * @param loanApplicationId
     * @return
     */
    LendProductPublish getLendProductPublishByLoanApplicationId(Long loanApplicationId);

    /**
     * 根据借款申请id查找发标信息
     * @param loanApplicationId
     * @return
     */
    LoanPublish getLoanPublishByAppId(Long loanApplicationId);

    List<Map> makeLoan(long loanApplicationId, long adminId, String theDesc, char channel, Date now, Date financialLendDate) throws Exception;


    /**
     * 重新放款提现
     * @param loanApplicationId
     */
    void reMakeLoan(Long loanApplicationId, Long adminId) throws Exception;

    /**
     * 根据用户Id查找所有借款
     * @param userId
     * @return
     */
	List<LoanApplication> getLoanAppListByUserId(Long userId);
	
	/**
	 * 企业借款申请分页列表
	 * @param pageNo 页码
	 * @param pageSize 页数
	 * @param params 查询条件
	 */
	Pagination<EnterpriseInfo> findAllEnterpriseLoanByPage(int pageNo, int pageSize, Map<String, Object> params);
	
	/**
	 * 导出企业借款申请列表
	 * @param response
	 * @param params
	 */
	void exportExcel(HttpServletResponse response, Map<String, Object> params);

    /**
     * 分页 借款列表
     * @param pageNo
     * @param pageSize
     * @param loanApplicationVO
     * @param customParams
     * @return
     */
    Pagination<LoanApplicationVO> getLoanApplicationByUserId(int pageNo, int pageSize, LoanApplicationVO loanApplicationVO, Map<String, Object> customParams);
    /**
     * 分页 借款列表(企业)
     * @param pageNo
     * @param pageSize
     * @param loanApplicationVO
     * @param customParams
     * @return
     */
    Pagination<LoanApplicationVO> getLoanApplicationByEnterpriseId(int pageNo, int pageSize, LoanApplicationVO loanApplicationVO,  Map<String, Object> customParams);

    /**
     * 财富券变现  处理，执行资金流即可
     * @param lendOrder
     * @param queue
     * @return
     */
    BigDecimal changeVoucherToAmount(LendOrder lendOrder,AccountValueChangedQueue queue);

    /**
     * 省心计划投标
     * @param lendOrder
     * @param now
     * @param matchCreditorVO
     * @throws Exception
     */
    void newLendBidForFinanceLendOrder(LendOrder lendOrder,  MatchCreditorVO matchCreditorVO) throws Exception;

    /***
     * @author wangyadong
     * @param opassword  定向密码
     * @param loanApplicationId  发标ID
     * @param excel  定向用户id
     * @param obj 操作类型 是新增还是修改
     * @return
     * 定向密码设置
     */
	String insertOrUpdateOLoanPublish(String opassword, Long loanApplicationId, String excel, String object);

	/**
	 * 查询是否为定向标以及它的类型
	 * @param loanApplicationNo
	 * @return
	 * 王亚东
	 */
	int getLoanApplicationType(Long loanApplicationNo);

	/**
	 * 查询定向密码
	 * @param loanApplicationId
	 * @return
	 * 王亚东
	 */
	String getLoanApplicationPass(Long loanApplicationId);


	int getLoanApplicationOrientById(Long loanApplicationId);
//	/**
//	 * 查询定向属性
//	 * @param loanApplicationId
//	 * @return
//	 * 王亚东
//	 */
//
//	LoanOrientation getLoanOrientationById(Long loanApplicationId);
	/**
//	 * 查询借款期限
//	 * @param null
//	 * @return List<BigDecimal>
//	 * 王亚东 
//	 */

	List<BigDecimal> getLendRateTypes();

	/**
	 * 根据条件，查询所有满标的借款申请
	 * @param params
	 * @return
	 */
	List<LoanApplication> findAllFullLoanApplication(Map<String, Object> params);

	/**
	 * 创建省心计划的合同
	 * */
	void createFinanceAgreement(List<LendOrder> lendOrderList);

	/**
	 * 创建省心计划的合同
	 * */
	void createFinanceAgreementAndFile(LendOrder lendOrder, Date createTime)
			throws Exception;

	
	/**
	 * 根据主借款申请ID，查询预热中的子借款申请数据(自动发标专用)
	 * @param mainLoanApplicationId 主借款申请ID
	 * @return
	 */
	List<LoanApplication> selectLoanByAuto(Long mainLoanApplicationId);
	
	/**
	 * 根据主借款申请ID，查询投标中的子借款申请数据(自动发标专用)
	 * @param mainLoanApplicationId 主借款申请ID
	 * @return
	 */
	List<LoanApplication> selectLoanByBid(Long mainLoanApplicationId);

	/**
	 * 重新生成合同（借款合同、债权转让合同）
	 * @param loanApplicationId
	 */
	void createAllAgreement(long loanApplicationId);
	   /**
     * 创建还款计划数据
     *
     * @param product
     * @param loanApp
     * @param channelType
     * @return
     * @throws Exception
     */
    public List<RepaymentPlan> getRepaymentPLanData(LoanProduct product, LoanApplication loanApp) throws Exception ;

    /**
     * 手动执行某个借款申请的流标动作
     * @param temp
     * @param date
     * @param timerExecLog
     * @throws Exception
     */
	void doNoticeFailLoan(LoanApplication temp, Date date,String logDesc) throws Exception;
	
	/***
	 * 查询是否为定向密码
	 * 根据loan_application_id
	 * @param loan_application_id
	 * @return
	 */
	String countOtypeByLoanApplicationId(Long loanApplicationId);

    /**
     * 新手标
     * @param pageNo
     * @param pageSize
     * @param loanApplicationListVO
     * @param object
     * @return
     */
	Pagination<LoanApplicationListVO> getLoanSpecialApplicationPaging(
			int pageNo, int pageSize,
			LoanApplicationListVO loanApplicationListVO, Map<String, Object> params);


	List<BigDecimal> getLoanRateTypesByNewUser(LoanApplicationStateEnum ... loanApplicationStateEnum);

	/**
	 * 批量生成省心计划合同
	 * */
	void exeSXJHCreateAgreementAll(Date startTime , Date endTime);

	/**
	 * 个人信用车贷
	 * @param mainLoan
	 * @param residenceAddr
	 * @param basic
	 * @param ext
	 * @param card
	 * @param feesItems
	 * @return
	 */
    MainLoanApplication saveLoanForCarPart2(MainLoanApplication mainLoan, Address residenceAddr, CustomerBasicSnapshot basic,
                                            UserInfoExt ext, CustomerCard card, List<LoanApplicationFeesItem> feesItems,CustomerCarSnapshot customerCarSnapshot);

    /**
     *新手标预期收益列表
     * @return
     */
//	List<BigDecimal> getLoanRateTypesByNewUser(LoanApplicationStateEnum... loanApplicationStateEnum, String flag);
	
}
