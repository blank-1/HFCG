/**
 * 
 */
package com.xt.cfp.core.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.RateErrorCode;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.RateEnum;
import com.xt.cfp.core.constants.TemplateType;
import com.xt.cfp.core.constants.RateEnum.RateUsageHisStateEnum;
import com.xt.cfp.core.constants.RateEnum.RateUserStatusEnum;
import com.xt.cfp.core.constants.UserType;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.RateProduct;
import com.xt.cfp.core.pojo.RateUser;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.pojo.UserOpenId;
import com.xt.cfp.core.pojo.ext.RateProductVO;
import com.xt.cfp.core.pojo.ext.RateUserVO;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.service.RateProductService;
import com.xt.cfp.core.service.RateUsageHistoryService;
import com.xt.cfp.core.service.RateUserService;
import com.xt.cfp.core.service.SmsService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.UserOpenIdService;
import com.xt.cfp.core.service.message.WechatMessageBody;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.JsonView;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.ResponseUtil;
import com.xt.cfp.core.util.Sign;
import com.xt.cfp.core.util.StringUtils;
import com.xt.cfp.core.util.TemplateUtil;

/**
 * @author Lianghuan
 *
 */
@Service
@Transactional
public class RateUserServiceImpl implements RateUserService {

	@Autowired
	private RateUsageHistoryService rateUsageHistoryService;
	@Autowired
	private MyBatisDao myBatisDao;
	@Autowired
	private RateProductService rateProductService;
	@Autowired
	private UserOpenIdService userOpenIdService;
	@Autowired
	private ConstantDefineService constantDefineService;
	@Autowired
    private SmsService smsService;
	@Autowired
	private UserInfoExtService userInfoExtService;

	@Override
	public RateUser findByRateUserId(Long rateUserId) {
		return findByRateUserId(rateUserId, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xt.cfp.core.service.RateUserService#findByRateUserId(java.lang.Long)
	 */
	@Override
	public RateUser findByRateUserId(Long rateUserId, boolean lock) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("rateUserId", rateUserId);
		if (lock) {
			param.put("lock", lock);
		}
		return myBatisDao.get("RATE_USER.selectByPrimaryKey", param);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xt.cfp.core.service.RateUserService#updateRateUser(com.xt.cfp.core
	 * .pojo.RateUser)
	 */
	@Override
	public void updateRateUser(RateUser rateUser) {
		myBatisDao.update("RATE_USER.updateByPrimaryKeySelective", rateUser);
	}

	@Override
	public RateUser addRateUser(RateUser rateUser) {
		myBatisDao.insert("RATE_USER.insert", rateUser);
		return rateUser;
	}

	@Override
	public RateUser getRateUserById(Long rateUserId) {
		return myBatisDao.get("RATE_USER.getRateUserById", rateUserId);
	}

	@Override
	public Pagination<RateUserVO> getRateUserPaging(int pageNo, int pageSize, RateUser rateUser, Map<String, Object> customParams) {
		Pagination<RateUserVO> page = new Pagination<RateUserVO>();
		page.setCurrentPage(pageNo);
		page.setPageSize(pageSize);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rateUser", rateUser);
		params.put("customParams", customParams);
		int totalCount = this.myBatisDao.count("getRateUserPaging", params);
		List<RateUserVO> list = this.myBatisDao.getListForPaging("getRateUserPaging", params, pageNo, pageSize);
		page.setTotal(totalCount);
		page.setRows(list);
		return page;
	}

	@Override
	public RateUser subtractRateUser(RateUser rateUser) {
		rateUser.setUsedTimes((rateUser.getUsedTimes() == null ? 1 : rateUser.getUsedTimes()) + 1);
		rateUser.setSurplusTimes(rateUser.getSurplusTimes() - 1);
		return rateUser;
	}

	@Override
	public void updateRateUserandHis(RateUser rateUser, Long userId, Long lendOrderId, Long loanApplicationId, RateUsageHisStateEnum state) {
		updateRateUser(rateUser);

	}

	@Override
	public void exportExcel(HttpServletResponse response, RateUser rateUser, Map<String, Object> customParams) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rateUser", rateUser);
		params.put("customParams", customParams);

		List<RateUserVO> ruvs = myBatisDao.getList("RATE_USER.getRateUserPaging", params);
		List<LinkedHashMap<String, Object>> dataMap = new ArrayList<LinkedHashMap<String, Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		for (RateUserVO ruv : ruvs) {
			LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
			linkedHashMap.put("ID", ruv.getRateUserId());
			linkedHashMap.put("用户名", ruv.getLoginName());
			linkedHashMap.put("姓名", ruv.getRealName());
			linkedHashMap.put("手机号", ruv.getMobileNo());
			linkedHashMap.put("加息券名称", ruv.getRateProductName());
			linkedHashMap.put("加息利率(%)", ruv.getRateValue());
			linkedHashMap.put("使用场景", ruv.getUsageScenarioStr());
			linkedHashMap.put("使用条件", ruv.getCondition());
			linkedHashMap.put("次数限制", ruv.getUsedTimes() > 0 ? ruv.getUsedTimes() : "无限制");
			linkedHashMap.put("发放时间", ruv.getStartDate() != null ? sdf.format(ruv.getStartDate()) : "");
			linkedHashMap.put("失效时间", ruv.getEndDate() != null ? sdf.format(ruv.getEndDate()) : "");
			linkedHashMap.put("状态", ruv.getStatusStr());

			dataMap.add(linkedHashMap);
		}
		ResponseUtil.sendExcel(response, dataMap, "加息券发放列表");
	}

	/**
	 * 初始化实体
	 * 
	 * @param rateProductId
	 *            加息券产品ID
	 * @param userId
	 *            发放用户ID
	 * @param source
	 *            加息券来源
	 * @param adminId
	 *            管理员ID
	 * @return
	 */
	private RateUser initRateUser(Long rateProductId, Long userId, String source, Long adminId) {
		RateUser rateUser = new RateUser();
		RateProduct rateProduct = rateProductService.getRateProductById(rateProductId);
		rateUser.setRateProductId(rateProductId);
		rateUser.setUserId(userId);
		rateUser.setSource(source);//加息券来源
		rateUser.setTotalTimes(rateProduct.getUsageTimes());//总次数
		rateUser.setUsedTimes(0);//已用次数
		rateUser.setSurplusTimes(rateProduct.getUsageTimes());//剩余次数
		rateUser.setStatus(RateEnum.RateUserStatusEnum.UNUSED.getValue());//状态：未使用
		rateUser.setStartDate(new Date());//有效开始时间
		if(rateProduct.getUsageDuration() > 0){
			rateUser.setEndDate(DateUtil.addDate(new Date(), (rateProduct.getUsageDuration()-1)));//有效结束时间=当前时间+有效时长天数(算当天时间，所以减1)
		}else {
			rateUser.setEndDate(rateProduct.getEndDate());//有效结束时间=加息券产品有效时间
		}
		rateUser.setAdminId(adminId);
		return rateUser;
	}

	@Override
	public void handOutOne(Long rateProductId, Long userId, String source, Long adminId) {
		RateUser rateUser = this.initRateUser(rateProductId, userId, source, adminId);
		myBatisDao.insert("RATE_USER.insert", rateUser);
		
		UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(userId);
		RateProduct rateProduct = rateProductService.getRateProductById(rateProductId);
		
		//发送短信信息，加息券获取通知
		this.sendSmsGetRateMsg(userInfoExt.getMobileNo(), rateProduct.getRateValue(), rateUser.getEndDate());
		
		//发送公众号信息，加息券获取通知
		this.sendWechatGetRateMsg(rateProduct.getRateProductName(), userId, rateUser.getEndDate());
	}
	
	@Override
	public void sendSmsGetRateMsg(String mobileNo, BigDecimal rateValue, Date endDate){
        try{
        	VelocityContext context = new VelocityContext();
        	context.put("rateValue", rateValue);
        	context.put("endDate", DateUtil.getDateLongCnD(endDate));
            String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_GETRATEMSG_VM, context);
            smsService.sendMsg(mobileNo, content);
        } catch (Exception e){
        	e.printStackTrace();
        }
    }
	
	@Override
	public void sendWechatGetRateMsg(String rateProductName, Long userId, Date endDate){
		try {
            UserOpenId userOpenId = userOpenIdService.getOpenIdByCondition(userId, null, null);
            if(userOpenId != null && !"".equals(userOpenId.getOpenId())){
            	List<ConstantDefine> tokenSurvivalTimeList = constantDefineService.getConstantDefinesByType("tokenSurvivalTime");
                List<ConstantDefine> accessTokenList = constantDefineService.getConstantDefinesByType("accessToken");
                Date date = new Date();
                if("0".equals(tokenSurvivalTimeList.get(0).getConstantValue().toString()) || (date.getTime()/1000) > Long.valueOf(tokenSurvivalTimeList.get(0).getConstantValue().toString())){
                	//to do重新获取
		        	String access_token = Sign.getAccessToken();
		        	Date resultTime = new Date();
		        	tokenSurvivalTimeList.get(0).setConstantValue(String.valueOf(resultTime.getTime()/1000+3600));
		        	accessTokenList.get(0).setConstantValue(access_token);
		        	constantDefineService.updateConstantDefine(tokenSurvivalTimeList.get(0));
			        constantDefineService.updateConstantDefine(accessTokenList.get(0));
                }
            	WechatMessageBody wechatMessageBody =  new WechatMessageBody();
            	wechatMessageBody.setOpenId(userOpenId.getOpenId());//用户openid(来自腾讯)
            	wechatMessageBody.setAccess_token(accessTokenList.get(0).getConstantValue());//访问令牌（来自腾讯）
            	wechatMessageBody.setEndDate(DateUtil.getDateLongCnD(endDate));//有效截止时间
            	wechatMessageBody.setFlag("7");//模板ID（加息券领取成功通知）
        		wechatMessageBody.setVoucherName(rateProductName);//加息券名称
        		
            	Sign.sendWechatMsg(wechatMessageBody);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handOutSome(Long rateProductId, List<Long> userIds, String source, Long adminId) {
		for (Long userId : userIds) {
			handOutOne(rateProductId, userId, source, adminId);
		}
	}

	@Override
	@Transactional
	public void handOutAll(Long rateProductId, String source, Long adminId) {
		UserInfo user = new UserInfo();
		user.setType(UserType.COMMON.getValue());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userInfo", user);

		// 一批1000条数据
		int pageSize = 1000;
		int totalRow = this.myBatisDao.count("getUserInfoPaging", params);
		int totalPage = (totalRow - 1) / pageSize + 1;
		// 批量发放
		for (int i = 1; i <= totalPage; i++) {
			List<UserInfo> users = this.myBatisDao.getListForPaging("getUserInfoPaging", params, i, pageSize);
			List<RateUser> list = new ArrayList<RateUser>();
			for (UserInfo u : users) {
				RateUser rateUser = this.initRateUser(rateProductId, u.getUserId(), source, adminId);
				Long id = (Long) myBatisDao.get("RATE_USER.getNextPK", null);
				rateUser.setRateUserId(id);
				myBatisDao.getSqlSession().clearCache();
				list.add(rateUser);
			}
			this.addRateUserBatch(list);
		}
	}

	@Override
	@Transactional
	public void addRateUserBatch(List<RateUser> rateUserList) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("list", rateUserList);
		params.put("rateUser", rateUserList.get(0));
		myBatisDao.insert("RATE_USER.addRateUserBatch", params);
	}

	@Override
	public List<RateUserVO> findRateUsersByUserId(Long userId, Integer cycleCount, String loanType, BigDecimal buyBalance, RateUserStatusEnum... statusEnums) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		String[] status = new String[statusEnums.length];
		for (int i = 0; i < statusEnums.length; i++) {
			status[i] = statusEnums[i].getValue();
		}
		param.put("status", status);
		param.put("nowDate", new Date());
		List<RateUserVO> rateUsers = myBatisDao.getList("RATE_USER.findRateUsersByUserId", param);
		List<RateUserVO> returnList = new ArrayList<RateUserVO>();
		// 数据处理[开始]
		if (rateUsers.size() > 0) {
			RateProductVO rateProductVO = new RateProductVO();
			for (RateUserVO v : rateUsers) {
				rateProductVO.getRateProductConditionValue(v.getCondition());
				if (v.getSurplusTimes() == 0)
					continue;
				if (!DateUtil.dateIn(DateUtil.getShortDateWithZeroTime(new Date()), DateUtil.getShortDateWithZeroTime(v.getStartDate()), DateUtil.getShortDateWithZeroTime(v.getEndDate())))
					continue;

				JsonView checkJson = checkRateUserCondition(v.getCondition(), cycleCount, loanType, buyBalance);
				if (!checkJson.isSuccess()) {
					continue;
				}

				v.setConditionStr(rateProductVO.getCustomizeConditionDesc(true, true, false));
				v.setStartAmount(rateProductVO.getRateProductConditionVO().getCon3_start_amount());
				v.setConditionStr(StringUtils.isNull(v.getConditionStr()) ? "无限制" : v.getConditionStr());
				returnList.add(v);
			}
		}
		// 数据处理[结束]

		return returnList;
	}
	
	/**
	 * 加息券条件校验
	 * 
	 * @param condition
	 * @param cycleCount
	 * @param loanType
	 * @param buyBalance
	 * @return
	 */
	private JsonView checkRateUserCondition(String condition, Integer cycleCount, String loanType, BigDecimal buyBalance) {
		RateProductVO rateProductVO = new RateProductVO();
		rateProductVO.getRateProductConditionValue(condition);
		if (rateProductVO.getRateProductConditionVO().isCondition1_term()) {
			if (rateProductVO.getRateProductConditionVO().getCon1_min() > cycleCount || cycleCount > rateProductVO.getRateProductConditionVO().getCon1_max()) {
				return JsonView.JsonViewFactory.create().success(false).info(RateErrorCode.RATE_WRONG_CYCLECOUNT.getDesc());
			}
		}
		boolean flag = true;
		if (rateProductVO.getRateProductConditionVO().isCondition2_type()) {
			switch (loanType) {
			case "0":
				if (!rateProductVO.getRateProductConditionVO().isCon2_0()) {
					flag = false;
				}
				break;
			case "1":
				if (!rateProductVO.getRateProductConditionVO().isCon2_1()) {
					flag = false;
				}
				break;
			case "2":
				if (!rateProductVO.getRateProductConditionVO().isCon2_2()) {
					flag = false;
				}
				break;
			case "3":
				if (!rateProductVO.getRateProductConditionVO().isCon2_3()) {
					flag = false;
				}
				break;
			case "4":
				if (!rateProductVO.getRateProductConditionVO().isCon2_4()) {
					flag = false;
				}
				break;
			case "5":
				if (!rateProductVO.getRateProductConditionVO().isCon2_5()) {
					flag = false;
				}
				break;
			case "6":
				if (!rateProductVO.getRateProductConditionVO().isCon2_6()) {
					flag = false;
				}
				break;
			}
		}
		if (!flag) {
			return JsonView.JsonViewFactory.create().success(false).info(RateErrorCode.RATE_WRONG_LOANTYPE.getDesc());
		}

		if (rateProductVO.getRateProductConditionVO().isCondition3_amount()) {
			if (buyBalance.compareTo(new BigDecimal(rateProductVO.getRateProductConditionVO().getCon3_start_amount().toString())) < 0) {
				return JsonView.JsonViewFactory.create().success(false).info(RateErrorCode.RATE_WRONG_BUYBALANCE.getDesc());
			}
		}

		return JsonView.JsonViewFactory.create().success(true);
	}

	@Override
	public JsonView checkRateUser(RateUser rateUser, RateProduct rateProduct, Long userId, Integer cycleCount, String loanType, BigDecimal buyBalance) {
		if (null == rateUser)
			return JsonView.JsonViewFactory.create().success(false).info(RateErrorCode.RATE_NOT_EXIST.getDesc());
		if (null == rateProduct)
			return JsonView.JsonViewFactory.create().success(false).info(RateErrorCode.RATE_PRODUCT_NOT_EXIST.getDesc());
		if (null == userId)
			return JsonView.JsonViewFactory.create().success(false).info(SystemErrorCode.PARAM_MISS.getDesc());
		if (!userId.equals(rateUser.getUserId()))
			return JsonView.JsonViewFactory.create().success(false).info(RateErrorCode.RATE_NOT_EXIST.getDesc());
		if (rateUser.getStatus().equals(RateUserStatusEnum.USEUP.getValue()) || rateUser.getSurplusTimes() == 0)
			return JsonView.JsonViewFactory.create().success(false).info(RateErrorCode.RATE_USEUP.getDesc()).put("加息券ID", rateUser.getRateUserId()).put("errorCode", RateErrorCode.RATE_USEUP);
		if (rateUser.getStatus().equals(RateUserStatusEnum.TIMEOUT.getValue()))
			return JsonView.JsonViewFactory.create().success(false).info(RateErrorCode.RATE_TIMEOUT.getDesc()).put("加息券ID", rateUser.getRateUserId());
		if (!DateUtil.dateIn(DateUtil.getShortDateWithZeroTime(new Date()), DateUtil.getShortDateWithZeroTime(rateUser.getStartDate()), DateUtil.getShortDateWithZeroTime(rateUser.getEndDate())))
			return JsonView.JsonViewFactory.create().success(false).info(RateErrorCode.RATE_NOT_IN_USE_DATE.getDesc()).put("加息券ID", rateUser.getRateUserId());

		return checkRateUserCondition(rateProduct.getCondition(), cycleCount, loanType, buyBalance);

	}
	 
	
	@Override
	public List<RateUserVO> findRateUsersByUserId(Long userId, RateUserStatusEnum... statusEnums) {
		Map<String,Object> param = new HashMap<>();
		param.put("userId", userId);
		String[] status = new String[statusEnums.length];
		for (int i = 0; i < statusEnums.length; i++) {
			status[i] = statusEnums[i].getValue();
		}
		param.put("status", status);
		param.put("nowDate", new Date());
		param.put("userId", userId);
		List<RateUserVO> rateUsers = myBatisDao.getList("RATE_USER.findRateUsersByUserId", param);
		return rateUsers;
	}
	
	@Override
	public Integer getCountByRateProductId(Long rateProductId) {
		return myBatisDao.get("RATE_USER.getCountByRateProductId", rateProductId);
	}
	
	@Override
	public void changeRateUserStatus(Long rateUserId, RateEnum.RateUserStatusEnum ruse) {
		//判断参数是否为null
		if (rateUserId == null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("rateUserId", "null");

		if (ruse == null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("ruse", "null");

		//修改状态
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rateUserId", rateUserId);
		params.put("status", ruse.getValue());
		
		myBatisDao.update("RATE_USER.changeRateUserStatus", params);
	}
	
	@Override
	public Integer getRateUserExpireCountByUserId(Long userId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("nowDate", new Date());
		return myBatisDao.get("RATE_USER.getRateUserExpireCountByUserId", params);
	}
	
	@Override
	public void sendExpireRateMsgForTimer() {
		UserInfo user = new UserInfo();
		user.setType(UserType.COMMON.getValue());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userInfo", user);

		// 一批1000条数据
		int pageSize = 1000;
		int totalRow = this.myBatisDao.count("getUserInfoPaging", params);
		int totalPage = (totalRow - 1) / pageSize + 1;
		// 批量发放
		for (int i = 1; i <= totalPage; i++) {
			List<UserInfo> users = this.myBatisDao.getListForPaging("getUserInfoPaging", params, i, pageSize);
			for (UserInfo u : users) {
				Integer count = this.getRateUserExpireCountByUserId(u.getUserId());
				
				if(null != count && count > 0){
				
					//发送短信信息，加息券即将到期通知
					this.sendSmsExpireRateMsg(u.getMobileNo(), count);
					
					//发送公众号信息，加息券即将到期通知
					this.sendWechatExpireRateMsg(u.getUserId(), count);
				}
				
			}
		}
	}
	
	@Override
	public void sendSmsExpireRateMsg(String mobileNo, Integer count){
        try{
        	VelocityContext context = new VelocityContext();
        	context.put("count", count);
            String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_EXPIRERATEMSG_VM, context);
            smsService.sendMsg(mobileNo, content);
        } catch (Exception e){
        	e.printStackTrace();
        }
    }
	
	@Override
	public void sendWechatExpireRateMsg(Long userId, Integer count){
		try {
            UserOpenId userOpenId = userOpenIdService.getOpenIdByCondition(userId, null, null);
            if(userOpenId != null && !"".equals(userOpenId.getOpenId())){
            	List<ConstantDefine> tokenSurvivalTimeList = constantDefineService.getConstantDefinesByType("tokenSurvivalTime");
                List<ConstantDefine> accessTokenList = constantDefineService.getConstantDefinesByType("accessToken");
                Date date = new Date();
                if("0".equals(tokenSurvivalTimeList.get(0).getConstantValue().toString()) || (date.getTime()/1000) > Long.valueOf(tokenSurvivalTimeList.get(0).getConstantValue().toString())){
                	//to do重新获取
		        	String access_token = Sign.getAccessToken();
		        	Date resultTime = new Date();
		        	tokenSurvivalTimeList.get(0).setConstantValue(String.valueOf(resultTime.getTime()/1000+3600));
		        	accessTokenList.get(0).setConstantValue(access_token);
		        	constantDefineService.updateConstantDefine(tokenSurvivalTimeList.get(0));
			        constantDefineService.updateConstantDefine(accessTokenList.get(0));
                }
            	WechatMessageBody wechatMessageBody =  new WechatMessageBody();
            	wechatMessageBody.setOpenId(userOpenId.getOpenId());//用户openid(来自腾讯)
            	wechatMessageBody.setAccess_token(accessTokenList.get(0).getConstantValue());//访问令牌（来自腾讯）
            	wechatMessageBody.setFlag("8");//模板ID（加息券到期提示通知）
        		wechatMessageBody.setCount(count);//到期数量
        		
            	Sign.sendWechatMsg(wechatMessageBody);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
