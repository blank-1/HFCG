package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.constants.CreditorRightsConstants;
import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.constants.TemplateType;
import com.xt.cfp.core.constants.UserMessage;
import com.xt.cfp.core.constants.WebMsgTemplateType;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.LendOrderReceiveService;
import com.xt.cfp.core.service.LoanProductService;
import com.xt.cfp.core.service.LoanPublishService;
import com.xt.cfp.core.service.SmsService;
import com.xt.cfp.core.service.UserMessageService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.Pair;
import com.xt.cfp.core.util.TemplateUtil;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ren yulin on 15-7-8.
 */
@Service
public class LendOrderReceiveServiceImpl implements LendOrderReceiveService {
    private static Logger logger = Logger.getLogger(LendOrderReceiveServiceImpl.class);
    @Autowired
    private MyBatisDao myBatisDao;
    @Autowired
    private LoanPublishService loanPublishService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private UserMessageService userMessageService ;
    @Override
    public LendOrderReceive getByOrderAndDate(long lendOrderId, String today) {
        Map map = new HashMap();
        map.put("lendOrderId", lendOrderId);
        map.put("today", today);
        return myBatisDao.get("LENDORDERRECEIVE.getByOrderAndDate",map);
    }

    @Override
    public LendOrderReceive getByOrderAndSectionCode(long lendOrderId, int sectionCode) {
        Map map = new HashMap();
        map.put("lendOrderId", lendOrderId);
        map.put("sectionCode", sectionCode);
        return myBatisDao.get("LENDORDERRECEIVE.getByOrderAndSectionCode",map);
    }

    @Override
    public LendOrderReceive getLastByOrder(Long lendOrderId) {
        Map map = new HashMap();
        map.put("lendOrderId", lendOrderId);
        map.put("lastSectionCode",1);
        return myBatisDao.get("LENDORDERRECEIVE.findLendOrderReceiveBy",map);
    }

    @Override
    public List<LendOrderReceive> getByLendOrderId(Long lendOrderId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("lendOrderId",lendOrderId);
        return myBatisDao.getList("LENDORDERRECEIVE.findLendOrderReceiveDetailBy",map);
    }

    @Override
    public void update(Map map) {
        myBatisDao.update("LENDORDERRECEIVE.updateByMap",map);
    }

    @Override
    public BigDecimal getCapitalReciveByUserId(Long lendOrderId,Long userId,LendProductTypeEnum ... productType) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userId",userId);
        params.put("lendOrderId",lendOrderId);
        List<String> productTypeList = new ArrayList<String>();
        if (productType == null || productType.length == 0) {
            productTypeList = null;
        } else {
            for (LendProductTypeEnum type : productType) {
                productTypeList.add(type.getValue());
            }
        }
        params.put("productTypeList", productTypeList);
        Map<String,BigDecimal> map = myBatisDao.get("LENDORDERRECEIVE.getCapitalReciveByUserId", params);
        BigDecimal subtract = map.get("SCAPITAL").subtract(map.get("FCAPITAL"));
        return subtract;
    }

    @Override
    public BigDecimal getInterestReciveByUserId(Long lendOrderId,Long userId,LendProductTypeEnum ... productType) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("lendOrderId",lendOrderId);
        params.put("userId",userId);
        List<String> productTypeList = new ArrayList<String>();
        if (productType == null || productType.length == 0) {
            productTypeList = null;
        } else {
            for (LendProductTypeEnum type : productType) {
                productTypeList.add(type.getValue());
            }
        }
        params.put("productTypeList", productTypeList);
        Map<String,BigDecimal> map = myBatisDao.get("LENDORDERRECEIVE.getInterestReciveByUserId",params);
        BigDecimal subtract = map.get("INTEREST").subtract(map.get("FINTEREST"));
        return subtract;
    }

    @Override
    public BigDecimal getExceptProfitByUserId(Long lendOrderId,Long userId,LendProductTypeEnum ... productType) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("lendOrderId",lendOrderId);
        params.put("userId",userId);
        List<String> productTypeList = new ArrayList<String>();
        if (productType == null || productType.length == 0) {
            productTypeList = null;
        } else {
            for (LendProductTypeEnum type : productType) {
                productTypeList.add(type.getValue());
            }
        }
        params.put("productTypeList", productTypeList);
        Map<String,BigDecimal> map = myBatisDao.get("LENDORDERRECEIVE.getExceptProfitByUserId",params);
        BigDecimal subtract = map.get("INTEREST");
        return subtract;
    }

    /**
     * 收益
     * @param userId
     * @param productType
     * @return
     */
    @Override
    public Map<String, Pair<BigDecimal, BigDecimal>> getTrendByUserId(Long userId,String productType) {
        TreeMap<String,Pair<BigDecimal,BigDecimal>> map = new TreeMap<String,Pair<BigDecimal,BigDecimal>>();
        //获取6个月内出借回款明细,(投标、理财)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        List<Map<String, Object>> interestSixMonth = getInterestSixMonth(userId, productType);
        List<Map<String, Object>> feeSixMonth = getFeeSixMonth(userId, productType);
        for(int i=6;i>0;i--){
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH,-i+1);
            Date date = c.getTime();
            String  key = sdf.format(date);
            BigDecimal interest = BigDecimal.ZERO;
            BigDecimal fee = BigDecimal.ZERO;
            if (interestSixMonth!=null){
                for (Map<String, Object> interestMap : interestSixMonth){
                    String month = (String)interestMap.get("MONTHFMT");
                    if (key.equals(month)){
                        interest = (BigDecimal)interestMap.get("INTEREST");
                    }
                }
                for(Map<String, Object> feeMap:feeSixMonth){
                    String fee_month = (String)feeMap.get("MONTHFMT");
                    if (key.equals(fee_month)){
                        fee = (BigDecimal)feeMap.get("INTEREST");
                    }
                }

            }
            map.put(key,new Pair<BigDecimal, BigDecimal>(interest,fee));
        }
        return map;
    }




    /**
     * 出借金额
     * @param userId
     * @param productType
     * @return
     */
    @Override
    public Map<String, Pair<BigDecimal, BigDecimal>> getTrendByUserIdNew(Long userId,String productType) {
        TreeMap<String,Pair<BigDecimal,BigDecimal>> map = new TreeMap<String,Pair<BigDecimal,BigDecimal>>();
        //获取6个月内出借回款明细,(投标、理财)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        List<Map<String, Object>> interestSixMonth = getLendSixMonth(userId, productType);

        for(int i=6;i>0;i--){
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH,-i+1);
            Date date = c.getTime();
            String  key = sdf.format(date);
            BigDecimal interest = BigDecimal.ZERO;
            BigDecimal fee = BigDecimal.ZERO;
            if (interestSixMonth!=null){
                for (Map<String, Object> interestMap : interestSixMonth){
                    String month = (String)interestMap.get("MONTHFMT");
                    if (key.equals(month)){
                        interest = (BigDecimal)interestMap.get("INTEREST");
                    }
                }

            }
            map.put(key,new Pair<BigDecimal, BigDecimal>(interest, BigDecimal.ZERO));
        }
        return map;
    }

    private List<Map<String, Object>> getLendSixMonth(Long userId, String productType) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userId",userId);
        params.put("productType",productType);
        params.put("date",new Date());
        return myBatisDao.getList("LENDORDERRECEIVE.getLendSixMonth", params);
    }


    @Override
    public void insert(LendOrderReceive lendOrderReceiveDetail) {
        myBatisDao.insert("LENDORDERRECEIVE.insert",lendOrderReceiveDetail);
    }

    @Override
    public void delete(long lendOrderReceiveDetailId) {
        myBatisDao.delete("LENDORDERRECEIVE.delete",lendOrderReceiveDetailId);
    }

    @Override
    public void sendToLenderRepaymentSuccessMsg(int sectionCode, LoanApplication loanApplication, Date repaymentDate, BigDecimal balance,String mobileNo) {

        VelocityContext context = new VelocityContext();
        try{
            try {
                context.put("date", DateUtil.getDateLongMD(repaymentDate));
            } catch (Exception e) {
                logger.error("生成还款成功短信失败",e);
            }
            context.put("amount",balance);

            LoanPublish loanPublish = loanPublishService.findById(loanApplication.getLoanApplicationId());
            context.put("name",loanPublish.getLoanTitle());
//            context.put("sectionCode",sectionCode);
//            context.put("count",loanProductService.findById(loanApplication.getLoanProductId()).getCycleCounts());
            //生成还款短信，发送还款短信
            String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_REPAYMENT_SUCCESS_VM, context);
            smsService.sendMsg(mobileNo,content);
        }catch (Exception e){
            logger.error("发送还款成功短信失败", e);
        }
    }

    /**
     * 根据出借产品类型获得当月总收益
     * @param userId
     * @param productType
     * @return
     */
    private List<Map<String,Object>> getInterestSixMonth(Long userId,String productType){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userId",userId);
        params.put("productType",productType);
        return myBatisDao.getList("LENDORDERRECEIVE.findLendOrderReceiveSixMonth", params);
    }

    /**
     * 根据出借产品类型获得当月总费用
     * @param userId
     * @param productType
     * @return
     */
    private List<Map<String,Object>> getFeeSixMonth(Long userId,String productType){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userId",userId);
        params.put("productType",productType);
        return myBatisDao.getList("LENDORDERRECEIVE.getFeeSixMonth", params);
    }

    @Override
    public LendOrderReceive getFirstByOrder(Long lendOrderId) {
            Map map = new HashMap();
            map.put("lendOrderId", lendOrderId);
            map.put("sectionCode", 1);
        return myBatisDao.get("LENDORDERRECEIVE.findLendOrderReceiveDetailBy", map);
    }

	@Override
	public LendOrderReceive getByIdLock(Long receiveId, boolean isLock) {
		Map<Object, Object> param = new HashMap<>();
		param.put("receiveId", receiveId);
		param.put("isLock", isLock);
		return myBatisDao.get("LENDORDERRECEIVE.getByIdLock", param);
	}
	
	@Override
    public void sendToLenderAheadRepaymentSuccessMsg(LendOrder lendOrder,LoanPublish loanPublish, String mobileNo) {

        VelocityContext context = new VelocityContext();
        try {
            try {
                context.put("date", DateUtil.getDateLongMD(lendOrder.getPayTime()));
                context.put("name", loanPublish.getLoanTitle());
            } catch (Exception e) {
                logger.error("生成提前还款还款成功短信失败", e);
            }

            //生成还款短信，发送还款短信
            String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_AHEAD_REPAYMENT_VM, context);
            smsService.sendMsg(mobileNo, content);
        } catch (Exception e) {
            logger.error("发送提前还款还款成功短信失败", e);
        }
    }

	@Override
	public void sendToLenderAheadRepaymentSuccessWebMsg(Date payTime,
			LoanPublish loanPublish, List<Long> userIds) {
		VelocityContext context = new VelocityContext();
		try {
			if(userIds == null || userIds.size() < 1){
				logger.info("【站内信】用户ID为空，不需要给用户发送站内信");
				return ;
			}
			List<UserRecive> reciverIdList = new ArrayList<>();
			for(Long userId : userIds){
				UserRecive userRecieve  = new UserRecive();
				userRecieve.setReciverId(userId);
				reciverIdList.add(userRecieve);
			}
			try {
				context.put("date", DateUtil.getDateLongMD(payTime));
				context.put("name", loanPublish.getLoanTitle());
			} catch (Exception e) {
				logger.error("生成提前还款还款成功站内信失败", e);
			}
			
			//生成还款站内信，发送还款站内信
			String content = TemplateUtil.getStringFromTemplate(WebMsgTemplateType.WEB_MSG_AHEAD_REPAYMENT_VM, context);
			userMessageService.sendStationMessage(reciverIdList, "提前还款通知", content, UserMessage.MessageReciverTypeEnum.LEND.getValue(), "财富派团队", (long)1, null);
			
		} catch (Exception e) {
			logger.error("发送提前还款还款成功站内信失败", e);
		}
		
	}

	@Override
	public BigDecimal getChildCapitalReciveByUserId(Long lendOrderId,
			Long userId) {
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("userId",userId);
        params.put("lendOrderId",lendOrderId);
        Map<String,BigDecimal> map = myBatisDao.get("LENDORDERRECEIVE.getChildCapitalReciveByUserId", params);
        BigDecimal subtract = map.get("SCAPITAL").subtract(map.get("FCAPITAL"));
        return subtract;
	}

	@Override
	public BigDecimal getChildInterestReciveByUserId(Long lendOrderId,
			Long userId) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("lendOrderId",lendOrderId);
        params.put("userId",userId);
        Map<String,BigDecimal> map = myBatisDao.get("LENDORDERRECEIVE.getChildInterestReciveByUserId",params);
        BigDecimal subtract = map.get("INTEREST").subtract(map.get("FINTEREST"));
        return subtract;
	}


}
