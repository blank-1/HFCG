package com.xt.cfp.core.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.constants.DisActivityEnums;
import com.xt.cfp.core.constants.LendOrderBidStatusEnum;
import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CommiProfit;
import com.xt.cfp.core.pojo.DisActivityRules;
import com.xt.cfp.core.pojo.DistributionInvite;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.LendOrderBidDetail;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.LoanProduct;
import com.xt.cfp.core.pojo.ext.CommitProfitVO;
import com.xt.cfp.core.pojo.ext.DisActivityRulesExt;
import com.xt.cfp.core.service.CommiProfitService;
import com.xt.cfp.core.service.DisActivityRulesService;
import com.xt.cfp.core.service.DisActivityService;
import com.xt.cfp.core.service.DistributionInviteService;
import com.xt.cfp.core.service.LendOrderBidDetailService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.LoanProductService;
import com.xt.cfp.core.service.WhiteTabsService;
import com.xt.cfp.core.service.crm.StaffService;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.InterestCalculation;

/**
 * 佣金收益统计 处理类
 */
@Service
public class CommiProfitServiceImpl implements CommiProfitService {
	
	private static Logger logger = Logger.getLogger(CommiProfitServiceImpl.class);
	
	@Autowired
	private MyBatisDao myBatisDao;
	
	@Autowired
	private LendOrderService lendOrderService;
	
	@Autowired
	private DisActivityRulesService disActivityRulesService;//规则表
	
	@Autowired
	private DisActivityService disActivityService;//分销活动表
	
	@Autowired
	private DistributionInviteService distributionInviteService;//分销邀请关系表
	
	@Autowired
	private StaffService staffService;//crm
	

	@Autowired
	private LoanApplicationService loanApplicationService;
	
	@Autowired
	private LoanProductService loanProductService;
	
	@Autowired
	private WhiteTabsService whiteTabsService ;

	@Autowired
	private LendOrderBidDetailService lendOrderBidDetailService;
	
	/**
	 * 创建一条佣金收益统计
	 * @param commiProfit
	 * @return
	 */
	@Override
	public CommiProfit addCommiProfit(CommiProfit commiProfit) {
		myBatisDao.insert("COMMI_PROFIT.insert", commiProfit);
		return commiProfit;
	}

	/**
	 * 修改一条佣金收益统计
	 * @param commiProfit
	 * @return
	 */
	@Override
	public CommiProfit updateCommiProfit(CommiProfit commiProfit) {
		myBatisDao.update("COMMI_PROFIT.updateByPrimaryKeySelective", commiProfit);
		return commiProfit;
	}
	
	/**
	 * 投标创建佣金收益统计（一笔最多三条）
	 * @param lendOrderId 出借产品订单ID
	 * @throws Exception 
	 */
	@Override
	@Transactional
	public void createCommiProfit(Long lendOrderId) throws Exception {
		//规则：
		//1.根据出借产品订单ID，加载一条出借产品订单信息，如果出借产品类型是投标类，继续。
		//2.根据出借产品ID，查询【规则表】，最多一条，查询【分销活动表】验证是否在有效期内，和状态是否是已经发布中。
		//3.根据出借人ID，查询【分销邀请关系表】（最多查出3条），（规则：select * from 关系 where 被邀请人 = 投标人）。
		//4.校验白名单WHITE_TABS，当邀请人在白名单内时，进行佣金统计记录。
		//5.插入【佣金收益统计】(最多3条）。
		
		logger.info("【佣金收益统计】出借产品订单ID：" + lendOrderId);
		//1.根据出借产品订单ID，加载一条出借产品订单信息，如果出借产品类型是投标类，继续。
		LendOrder lendOrder = lendOrderService.findById(lendOrderId);
		//【20170203新需求：需要校验购买人在白名单内，才能参与三级分销活动】
		boolean flag = validateCurrentLendUser(lendOrder) ;
		if(!flag){
			logger.info("【佣金收益统计】购买人白名单验证不通过！");
			return  ;
		}
		logger.info("【佣金收益统计】购买人白名单验证通过！");	
		LendOrder pOrder = null;
		if(lendOrder.getLendOrderPId()!= null){
			pOrder =lendOrderService.findById(lendOrder.getLendOrderPId()); 
		}
		if(LendProductTypeEnum.RIGHTING.getValue().equals(lendOrder.getProductType()) || pOrder == null &&
				(pOrder !=null && !pOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))){
			
			//2.根据出借产品ID，查询【规则表】，最多一条，查询【分销活动表】验证是否在有效期内，和状态是否是已经发布中。
			List<DisActivityRulesExt> disActivityRuleList = disActivityRulesService.getDisActivityRulesByLendProductId(lendOrder.getLendProductId());
			if (null != disActivityRuleList && disActivityRuleList.size() > 0) {
				for (DisActivityRulesExt disActivityRule : disActivityRuleList) {
//					DisActivityRules disActivityRule = disActivityRuleList
//							.get(0);// 理论上只有一条有效
					logger.info("【佣金收益统计】规则ID：" + disActivityRule.getRulesId());

					// 3.根据出借人ID，查询【分销邀请关系表】（最多查出3条），（规则：select * from 关系 where
					// 被邀请人 = 投标人）。
					List<DistributionInvite> distributionInviteList = distributionInviteService
							.getDistributionInviteByUserId(lendOrder
									.getLendUserId(),true);
					if (null != distributionInviteList
							&& distributionInviteList.size() > 0) {
						for (DistributionInvite di : distributionInviteList) {
							logger.info("【佣金收益统计】邀请人：" + di.getUserPid());

							//【新需求】判断当前的邀请人 他的三级内邀请人是否有销售 无销售【1】 有销售【2】
							boolean isSaleInvite = whiteTabsService.isSaleInvite(di.getUserPid()) ;
							String type ;
							if(isSaleInvite){
								type = DisActivityEnums.DisAcivityUserTypeEnum.SALE_INVITE_USRE.getValue();
							}else{
								type = DisActivityEnums.DisAcivityUserTypeEnum.PLAT_USER.getValue();
							}
							if(StringUtils.isNotBlank(disActivityRule.getTargetUser()) && 
									(disActivityRule.getTargetUser().equals(DisActivityEnums.DisAcivityUserTypeEnum.ALL_USER.getValue())
											|| disActivityRule.getTargetUser().equals(type))) {
								// 4.校验白名单WHITE_TABS，当邀请人在白名单内时，进行佣金统计记录。
								Integer count = whiteTabsService.countUserId(di
										.getUserPid());
								if (count <= 0) {
									continue;
								}
								logger.info("【佣金收益统计】邀请人白名单验证通过！");

								// 全部本金(佣金基数)
								BigDecimal buyBalance = lendOrder
										.getBuyBalance();
								// 期数值
								Integer timeLimit = lendOrder.getTimeLimit();
								// 佣金比例
								BigDecimal annualRate = BigDecimal.ZERO;
								if ("1".equals(di.getDisLevel())) {
									annualRate = disActivityRule.getFirstRate();
									logger.info("【佣金收益统计】邀请等级【1】, 佣金比例："
											+ disActivityRule.getFirstRate());
								} else if ("2".equals(di.getDisLevel())) {
									annualRate = disActivityRule
											.getSecondRate();
									logger.info("【佣金收益统计】邀请等级【2】, 佣金比例："
											+ disActivityRule.getSecondRate());
								} else if ("3".equals(di.getDisLevel())) {
									annualRate = disActivityRule.getThirdRate();
									logger.info("【佣金收益统计】邀请等级【3】, 佣金比例："
											+ disActivityRule.getThirdRate());
								}

								// 应得收益
								BigDecimal shoulProfit = BigDecimalUtil.down(
										this.getShoulProfit(buyBalance,
												annualRate, lendOrder), 2);
								logger.info("【佣金收益统计】全部本金(佣金基数)："
										+ lendOrder.getBuyBalance() + ", 期数值："
										+ timeLimit + ", 邀请等级："
										+ di.getDisLevel() + ", 应得收益："
										+ shoulProfit);
								//【20170405-佣金校验：对一个用户，一笔订单的收益只有一条】  使用userId,lendOrderId查询只能有一条
								Map<String,Object> paramMap = new HashMap<>();
								paramMap.put("lendOrderId", lendOrderId);
								paramMap.put("userId", di.getUserPid());
								List<CommiProfit> oldCommiProfitList = this.getCommiProfitByParams(paramMap);
								if(oldCommiProfitList != null && oldCommiProfitList.size() > 0){
									logger.info("订单对应的邀请人已经生成过佣金记录，不能再次生成！");
									continue; 
								}
								
								// 5.插入【佣金收益统计】。
								CommiProfit commiProfit = new CommiProfit();
								commiProfit.setRulesId(disActivityRule
										.getRulesId());// 规则ID
								commiProfit.setUserId(di.getUserPid());// 推荐人用户ID
								commiProfit.setLendOrderId(lendOrderId);// 出借产品订单ID
								commiProfit.setFactProfit(BigDecimal.ZERO);// 已获收益
								commiProfit.setShoulProfit(shoulProfit);// 应得收益
								commiProfit.setOriginProfit(shoulProfit);// 原始总收益
								this.addCommiProfit(commiProfit);
								logger.info("【佣金收益统计】添加一条成功！, 收益统计ID："
										+ commiProfit.getComiProId());
							}else{
								logger.info("【佣金统计收益】该规则不符合用户类型，规则id：" + disActivityRule.getRulesId() + "，用户ID：" + di.getUserPid());
							}
						}
					}
				}
			}
		}
	}
	private boolean validateCurrentLendUser(LendOrder lendOrder) {
		Integer count = whiteTabsService.countUserId(lendOrder.getLendUserId()) ;
		if (count <= 0) {
			return false;
		}
		return true ;
	}

	/**
	 * 投标创建佣金收益统计（一笔最多三条）
	 * @param lendOrderId 出借产品订单ID
	 * @throws Exception 
	 */
	@Override
	@Transactional
	public void createCommiProfit(Long lendOrderId,Date startTime,Date endTime) throws Exception {
		//规则：
		//1.根据出借产品订单ID，加载一条出借产品订单信息，如果出借产品类型是投标类，继续。
		//2.根据出借产品ID，查询【规则表】，最多一条，查询【分销活动表】验证是否在有效期内，和状态是否是已经发布中。
		//3.根据出借人ID，查询【分销邀请关系表】（最多查出3条），（规则：select * from 关系 where 被邀请人 = 投标人）。
		//4.校验白名单WHITE_TABS，当邀请人在白名单内时，进行佣金统计记录。
		//5.插入【佣金收益统计】(最多3条）。
		
		logger.info("【佣金收益统计】出借产品订单ID：" + lendOrderId);
		//1.根据出借产品订单ID，加载一条出借产品订单信息，如果出借产品类型是投标类，继续。
		LendOrder lendOrder = lendOrderService.findById(lendOrderId);
		//【20170203新需求：需要校验购买人在白名单内，才能参与三级分销活动】
			boolean flag = validateCurrentLendUser(lendOrder) ;
			if(!flag){
				logger.info("【佣金收益统计】购买人白名单验证不通过！");
				return  ;
			}
		if(LendProductTypeEnum.RIGHTING.getValue().equals(lendOrder.getProductType())){
			
			//2.根据出借产品ID，查询【规则表】，最多一条，查询【分销活动表】验证是否在有效期内，和状态是否是已经发布中。
			List<DisActivityRules> disActivityRuleList = disActivityRulesService.getDisActivityRulesByLendProductIdAndTime(lendOrder.getLendProductId(), startTime,endTime);
			if(null != disActivityRuleList && disActivityRuleList.size() > 0){
				DisActivityRules disActivityRule = disActivityRuleList.get(0);//理论上只有一条有效
				logger.info("【佣金收益统计】规则ID：" + disActivityRule.getRulesId());
				
				//3.根据出借人ID，查询【分销邀请关系表】（最多查出3条），（规则：select * from 关系 where 被邀请人 = 投标人）。
				List<DistributionInvite> distributionInviteList = distributionInviteService.getDistributionInviteByUserId(lendOrder.getLendUserId(),true);
				if(null != distributionInviteList && distributionInviteList.size() > 0){
					for (DistributionInvite di : distributionInviteList) {
						logger.info("【佣金收益统计】邀请人：" + di.getUserPid());
						
						//4.校验白名单WHITE_TABS，当邀请人在白名单内时，进行佣金统计记录。
						Integer count = whiteTabsService.countUserId(di.getUserPid());
						if(count <= 0){
							continue;
						}
						logger.info("【佣金收益统计】邀请人白名单验证通过！");
						
						//全部本金(佣金基数)
						BigDecimal buyBalance = lendOrder.getBuyBalance();
						//期数值
						Integer timeLimit = lendOrder.getTimeLimit();
						//佣金比例
						BigDecimal annualRate = BigDecimal.ZERO;
						if("1".equals(di.getDisLevel())){
							annualRate = disActivityRule.getFirstRate();
							logger.info("【佣金收益统计】邀请等级【1】, 佣金比例：" + disActivityRule.getFirstRate());
						}else if("2".equals(di.getDisLevel())) {
							annualRate = disActivityRule.getSecondRate();
							logger.info("【佣金收益统计】邀请等级【2】, 佣金比例：" + disActivityRule.getSecondRate());
						}else if ("3".equals(di.getDisLevel())) {
							annualRate = disActivityRule.getThirdRate();
							logger.info("【佣金收益统计】邀请等级【3】, 佣金比例：" + disActivityRule.getThirdRate());
						}
						
						//应得收益
						BigDecimal shoulProfit = BigDecimalUtil.down(this.getShoulProfit(buyBalance, annualRate, lendOrder), 2);
						logger.info("【佣金收益统计】全部本金(佣金基数)：" + lendOrder.getBuyBalance() + ", 期数值：" + timeLimit + ", 邀请等级：" + di.getDisLevel() + ", 应得收益：" + shoulProfit);
						
						//【20170405-佣金校验：对一个用户，一笔订单的收益只有一条】  使用userId,lendOrderId查询只能有一条
						Map<String,Object> paramMap = new HashMap<>();
						paramMap.put("lendOrderId", lendOrderId);
						paramMap.put("userId", di.getUserPid());
						List<CommiProfit> oldCommiProfitList = this.getCommiProfitByParams(paramMap);
						if(oldCommiProfitList != null && oldCommiProfitList.size() > 0){
							logger.info("订单对应的邀请人已经生成过佣金记录，不能再次生成！");
							continue; 
						}
						
						//5.插入【佣金收益统计】。
						CommiProfit commiProfit = new CommiProfit();
						commiProfit.setRulesId(disActivityRule.getRulesId());//规则ID
						commiProfit.setUserId(di.getUserPid());//推荐人用户ID
						commiProfit.setLendOrderId(lendOrderId);//出借产品订单ID
						commiProfit.setFactProfit(BigDecimal.ZERO);//已获收益
						commiProfit.setShoulProfit(shoulProfit);//应得收益
						commiProfit.setOriginProfit(shoulProfit);//原始总收益
						this.addCommiProfit(commiProfit);
						logger.info("【佣金收益统计】添加一条成功！, 收益统计ID：" + commiProfit.getComiProId());
						
					}
				}
			}
		}
	}
	
	/**
	 * 计算应得收益
	 * @param buyBalance 金额
	 * @param annualRate 年化利率
	 * @param lendOrder 出借订单实体
	 * @return
	 * @throws Exception
	 */
	private BigDecimal getShoulProfit(BigDecimal buyBalance, BigDecimal annualRate, LendOrder lendOrder) throws Exception {
		List<LendOrderBidDetail> bidDetailList = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId(), LendOrderBidStatusEnum.BIDSUCCESS,LendOrderBidStatusEnum.BIDING);
		if(null != bidDetailList && bidDetailList.size() > 0){
			LendOrderBidDetail bidDetail = bidDetailList.get(0);
			LoanApplication loanApplication = loanApplicationService.findById(bidDetail.getLoanApplicationId());
			LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
			//计算收益
			//balance, annualRate, dueTimeType, repaymentMethod, repaymentType, repaymentCycle, dueTime, cycleValue
			//[金额],   [利率],       [期数值类型],    [还款方法],         [还款方式],        [还款周期类型],     [期数值],   [周期值]
			return InterestCalculation.getAllInterest(buyBalance, annualRate, loanProduct.getDueTimeType(), loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), lendOrder.getTimeLimit(), loanProduct.getCycleValue());
		}else {
			return BigDecimal.ZERO;
		}
	}
	
	@Override 
	public CommiProfit selectByPrimaryKey(Long primaryId){
		return myBatisDao.get("COMMI_PROFIT.selectByPrimaryKey", primaryId);
	}
	
	@Override
	public List<CommitProfitVO> getCommiProfitByLendOrderId(Long lendOrderId, Long lendUserId) {
		Map param = new HashMap();
		param.put("lendOrderId", lendOrderId);
		param.put("lendUserId",lendUserId);
		return myBatisDao.getList("COMMI_PROFIT.getCommiProfitByLendOrderId",param);
	}
	
	@Override
	public List<CommiProfit> getCommiProfitByParams(Map<String,Object> paramMap) {
		return myBatisDao.getList("COMMI_PROFIT.getCommiProfitByParams",paramMap);
	}
	
	
	@Override
	public BigDecimal calUserAccountProfit(Long userId) {
		return myBatisDao.get("COMMI_PROFIT.accountProfitByUserId", userId);
	}

	@Override
	public BigDecimal calUserAccountShouldProfit(Long userId) {
		return myBatisDao.get("COMMI_PROFIT.calUserAccountShouldProfit", userId);
	}
}
