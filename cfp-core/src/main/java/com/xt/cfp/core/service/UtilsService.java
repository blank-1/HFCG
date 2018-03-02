package com.xt.cfp.core.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.xt.cfp.core.pojo.AdminInfo;
import com.xt.cfp.core.pojo.AwardDetail;
import com.xt.cfp.core.pojo.PlatformBill;

/**
 * Created by yulei on 2015/7/27.
 */
public interface UtilsService {

	Map<String,Object> doRecharge(String reason, Long userId, BigDecimal amount, Map<String, Object> params, AdminInfo currentUser, String externalNo, String channelCode);

    void doRechargeForAward(String[] title, List<Map<String,Object>> result, Map<String, Object> params);

    void reMakeWithdrawForApply(Collection<Long> ids);

    void reSendAward(Date sDate, Date eDate);
    /**
     * 修复债权占比方法
     * */
    public void repairRightsRepaymentDetailData();
    /**
     * 补发一次对应债权奖励的数据
     * */
    public void reissuedAwardByTime(String mobilesStr);

	void executeAwardAccountHis(String loginName, String loanPublishTitle,
			Long systemOperationIdAccountId, BigDecimal theAward2Lend,
			AwardDetail awardDetail, Date now, Long customerAccoutId,
			Long userId);
	
	/**
	 * 工具：导入白名单的userId
	 * 格式：Excel文档 title 用户ID
	 * */
	void importToWhiteTabs(MultipartFile importFile);
	
	/**
	 * 资金操作
	 * @param accountId 用户账户id
	 * @param money	
	 * @param type	操作类型 1收入 2支出 4冻结 5解冻
	 * @param desc
	 * @return
	 */
	String capitalOperate(Long accountId ,BigDecimal money,String type ,String desc) throws Exception;

	/**
     * 三级分销手动生成订单对应的佣金
     */
	void createCommiProfitTools(String lendOrderIds,Date ccpStartDate, Date ccpEndDate)  throws Exception;

	
	/**
	 * 工具：导入邀请白名单的userId
	 * 格式：Excel文档 title 用户ID
	 * */
	void importToInviteWhiteTabs(MultipartFile importFile);

	/**
	 * 处理支付成功但处理失败的支付订单
	 */
	void handleUndonePayOrder();

	/**
	 * 临时，处理导入失误
	 */
	void repairAcc();
	
	/**
	 * 参与白名单和邀请白名单通过手机号录入工具
	 * flag:0 -- 只查询 ， 1 -- 查询并导入
	 * */
	Map<String,Object> checkAndImportWhiteTabsByMobiles(String importWhiteTabsMobiles, char flag);

	/**
	 * 批量生成省心计划合同
	 * */
	void exeSXJHCreateAgreement(Date startTime , Date endTime);
	
	/**
	 * 工具：导入多级邀请关系表的userId
	 * 格式：Excel文档 title 用户ID
	 * */
	void importMultilevelInvitationExcel(MultipartFile importFile);
	
	/**
	 * 向平台账单表中添加数据 
	 */
	void addPlatformBill(PlatformBill platformBill);
	
	/**
	 * 根据userid，查询一条平台账单数据
	 * @param userId 用户id
	 */
	PlatformBill getPlatformBillByUserId(Long userId);
	
	/**
	 * 更改一条账单数据
	 */
	void updatePlatformBill(PlatformBill platformBill);

}
