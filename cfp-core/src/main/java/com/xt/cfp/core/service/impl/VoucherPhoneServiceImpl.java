package com.xt.cfp.core.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.TemplateType;
import com.xt.cfp.core.constants.VoucherConstants;
import com.xt.cfp.core.constants.WechatActConstants;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.games.LotteryDraw;
import com.xt.cfp.core.pojo.ActivityBinding;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.PrizePool;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.pojo.UserShare;
import com.xt.cfp.core.pojo.VoucherPhone;
import com.xt.cfp.core.pojo.ext.VoucherPhoneVO;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.SmsService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.VoucherPhoneService;
import com.xt.cfp.core.service.VoucherService;
import com.xt.cfp.core.util.TemplateUtil;

@Service
public class VoucherPhoneServiceImpl implements VoucherPhoneService {

	@Autowired
	private MyBatisDao myBatisDao;
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private ConstantDefineService constantDefineService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private UserInfoExtService userInfoExtService;
	@Autowired
	private LendOrderService lendOrderService;
	@Autowired
    private SmsService smsService;
	@Value(value = "${VOUCHER_FLAG}")
	private String voucherFlag;

	@Value(value = "${VOUCHER_FIVE_ID}")
	private Long voucherFive;
	@Value(value = "${VOUCHER_TEN_ID}")
	private Long voucherTen;
	@Value(value = "${VOUCHER_TWENTY_ID}")
	private Long voucherTwenty;
	@Value(value = "${VOUCHER_FIFTY_ID}")
	private Long voucherFifty;
	@Value(value = "${VOUCHER_HUNDRED_ID}")
	private Long voucherHundred;
	@Value(value = "${VOUCHER_WITHDRAW_ID}")
	private Long voucherWithDraw;
	@Value(value = "${ACTIVITY_NUMBER}")
	private String activityNumber;
	
	@Override
	public VoucherPhone addVoucherPhone(VoucherPhone voucherPhone) {
		myBatisDao.insert("VOUCHER_PHONE.insert", voucherPhone);
		return voucherPhone;
	}

	@Override
	public VoucherPhone getVoucherPhone(String phone) {
		return myBatisDao.get("VOUCHER_PHONE.getVoucherPhoneByPhone", phone);
	}
	
	@Override
	public void updateVoucherPhone(VoucherPhone voucherPhone) {
		myBatisDao.get("VOUCHER_PHONE.updateByPrimaryKey", voucherPhone);
	}
	@Override
	public VoucherPhone getVoucherPhone(String openId, String activityId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("openId", openId);
		params.put("activityId", activityId);
		return myBatisDao.get("", params);
	}

	@Override
	public List<VoucherPhone> getVoucherPhoneByPhone(String phone, String activityId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("phone", phone);
		params.put("activityId", activityId);
		return myBatisDao.getList("VOUCHER_PHONE.getVoucherPhoneByPhoneAndAct", params);
	}

	@Override
	public ActivityBinding getActivityBindingByOpenId(String openId) {
		
		return myBatisDao.get("ACTIVITY_BINDING.getActivityBindingByOpenId", openId);
	}

	@Override
	public ActivityBinding addActivityBindingAndVoucherPhone(String openId, String phone,String activityId) {
		ActivityBinding ab = new ActivityBinding();
		ab.setCreateTime(new Date());
		ab.setMobileNo(phone);
		ab.setOpenId(openId);
		ab.setUsageFrequency(Long.valueOf(0));
		myBatisDao.insert("ACTIVITY_BINDING.insert", ab);
		return ab;
	}

	@Override
	public List<VoucherPhone> getVoucherPhoneBindingPhone(String phone, String activityId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("phone", phone);
		params.put("activityId", activityId);
		return myBatisDao.getList("VOUCHER_PHONE.getVoucherPhoneBindingPhone", params);
	}
	@Override
	@Transactional
	public void updateVoucherPhoneAndPrize(int prizeNum, Long userId, String activityId, Long bindingId){
		if(!isPublishVoucher()){
			return;
		}
		Long voucherProductId = null;
		BigDecimal publishValue = new BigDecimal(0);
		if(prizeNum == 5){
			voucherProductId = voucherFive;
			publishValue = new BigDecimal(5);
		}else if(prizeNum == 10){
			voucherProductId = voucherTen;
			publishValue = new BigDecimal(10);
		}else if(prizeNum == 20){
			voucherProductId = voucherTwenty;
			publishValue = new BigDecimal(20);
		}else if(prizeNum == 50){
			voucherProductId = voucherFifty;
			publishValue = new BigDecimal(50);
		}else if(prizeNum == 100){
			voucherProductId = voucherHundred;
			publishValue = new BigDecimal(100);
		}
		if(voucherProductId != null){
			VoucherPhone voucherPhone = new VoucherPhone();
			voucherPhone.setPrizeId(voucherProductId);
			voucherPhone.setActivityNumber(activityId);
			voucherPhone.setBindingId(bindingId);
			voucherPhone.setCreateTime(new Date());
			voucherPhone.setUsageFrequency(Long.valueOf(0));
			addVoucherPhone(voucherPhone);
			List<ConstantDefine> wechatVoucherTotalList = constantDefineService.getConstantDefinesByType("wechatVoucherTotal");
			BigDecimal wechatVoucherTotal = new BigDecimal(wechatVoucherTotalList.get(0).getConstantValue());
			ConstantDefine constantDefine = wechatVoucherTotalList.get(0);
			constantDefine.setConstantValue(wechatVoucherTotal.add(publishValue).toString());
    		constantDefineService.updateConstantDefine(constantDefine);
			voucherService.handOut(voucherProductId,userId,VoucherConstants.SourceType.OTHER.getValue(),"活动奖励");
		}
	}
	@Override
	@Transactional
	public void sendVoucherWithDraw(Long userId, String activityId, ActivityBinding ab, Long bindingId){
		if(!isPublishVoucher()){
			return;
		}
		if(ab != null){
			ab = addActivityBindingAndVoucherPhone(ab.getOpenId(), ab.getMobileNo(), activityId);
		}
		if(bindingId == null){
			bindingId = ab.getBindingId();
		}
		Long voucherProductId = voucherWithDraw;
		BigDecimal publishValue = new BigDecimal("1");
		if(voucherProductId != null){
			VoucherPhone voucherPhone = new VoucherPhone();
			voucherPhone.setPrizeId(voucherProductId);
			voucherPhone.setActivityNumber(activityId);
			voucherPhone.setBindingId(bindingId);
			voucherPhone.setCreateTime(new Date());
			voucherPhone.setUsageFrequency(Long.valueOf(0));
			addVoucherPhone(voucherPhone);
			List<ConstantDefine> wechatVoucherTotalList = constantDefineService.getConstantDefinesByType("withdrawVoucherSize");
			BigDecimal wechatVoucherTotal = new BigDecimal(wechatVoucherTotalList.get(0).getConstantValue());
			ConstantDefine constantDefine = wechatVoucherTotalList.get(0);
			constantDefine.setConstantValue(wechatVoucherTotal.add(publishValue).toString());
    		constantDefineService.updateConstantDefine(constantDefine);
			voucherService.handOut(voucherProductId,userId,VoucherConstants.SourceType.OTHER.getValue(),"活动奖励");
		}
	}
	
	@Override
	@Transactional
	public void AddVoucherPhoneAndPrize(int prizeNum, Long userId, String activityId, Long bindingId){
		Long voucherProductId = null;
		if(prizeNum == 5){
			voucherProductId = voucherFive;
		}else if(prizeNum == 10){
			voucherProductId = voucherTen;
		}else if(prizeNum == 20){
			voucherProductId = voucherTwenty;
		}else if(prizeNum == 50){
			voucherProductId = voucherFifty;
		}else if(prizeNum == 100){
			voucherProductId = voucherHundred;
		}
		if(voucherProductId != null){
			VoucherPhone voucherPhone = new VoucherPhone();
			voucherPhone.setPrizeId(voucherProductId);
			voucherPhone.setActivityNumber(activityId);
			voucherPhone.setBindingId(bindingId);
			voucherPhone.setCreateTime(new Date());
			voucherPhone.setUsageFrequency(Long.valueOf(0));
			addVoucherPhone(voucherPhone);
			voucherService.handOut(voucherProductId,userId,VoucherConstants.SourceType.OTHER.getValue(),"活动奖励");
		}
	}
	
	@Override
	public void updateActivityBinding(ActivityBinding ab) {
		myBatisDao.insert("ACTIVITY_BINDING.updateByPrimaryKey", ab);
	}

	@Override
	public ActivityBinding getActivityBindingByPhone(String phone) {
		return myBatisDao.get("ACTIVITY_BINDING.getActivityBindingByPhone", phone);
	}

	public String getVoucherFlag() {
		return voucherFlag;
	}

	public void setVoucherFlag(String voucherFlag) {
		this.voucherFlag = voucherFlag;
	}

	public Long getVoucherFive() {
		return voucherFive;
	}

	public void setVoucherFive(Long voucherFive) {
		this.voucherFive = voucherFive;
	}

	public Long getVoucherTen() {
		return voucherTen;
	}

	public void setVoucherTen(Long voucherTen) {
		this.voucherTen = voucherTen;
	}

	public Long getVoucherTwenty() {
		return voucherTwenty;
	}

	public void setVoucherTwenty(Long voucherTwenty) {
		this.voucherTwenty = voucherTwenty;
	}

	public Long getVoucherFifty() {
		return voucherFifty;
	}

	public void setVoucherFifty(Long voucherFifty) {
		this.voucherFifty = voucherFifty;
	}

	public Long getVoucherHundred() {
		return voucherHundred;
	}

	public void setVoucherHundred(Long voucherHundred) {
		this.voucherHundred = voucherHundred;
	}
	public Long getVoucherWithDraw() {
		return voucherWithDraw;
	}

	public void setVoucherWithDraw(Long voucherWithDraw) {
		this.voucherWithDraw = voucherWithDraw;
	}
	
	public String getActivityNumber() {
		return activityNumber;
	}

	public void setActivityNumber(String activityNumber) {
		this.activityNumber = activityNumber;
	}

	/**
	 * 触发节点开关
	 * @return
	 */
	public boolean isPublishVoucher(){
		return this.getVoucherFlag().equals("1")?true:false;
	}

	@Override
	public UserShare addUserShare(UserShare userShare) {
		myBatisDao.insert("USER_SHARE.insert", userShare);
		return userShare;
	}

	@Override
	public UserShare getUserSHareByUserId(Long userId, String activityId) {
		Map<String,Object> params = new HashMap<String,Object>();
		if(userId == null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", userId);
		if(activityId == null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("activityId", activityId);
		params.put("userId", userId);
		params.put("activityId", activityId);
		return myBatisDao.get("USER_SHARE.getUserSHareByUserId", params);
	}

	@Override
	public void updateUserShare(UserShare userShare) {
		myBatisDao.update("USER_SHARE.updateByPrimaryKeySelective", userShare);
	}

//	@Override
//	@Transactional
//	public UserShare shareAddNum(Long userId, String activityId) {
//		UserShare userShare = getUserSHareByUserId(userId, activityId);
//		if(userShare != null){
//			userShare.setLuckDrawNum(userShare.getLuckDrawNum()+1);
//			userShare.setIsShare(WechatActConstants.IsShareEnum.IS_SHARE.getValue());
//			myBatisDao.update("USER_SHARE.updateByPrimaryKeySelective", userShare);
//			return userShare;
//		}else{
//			UserShare userShareNew = new UserShare();
//			userShareNew.setUserId(userId);
//			userShareNew.setActivityNumber("franchiseeV07");
//			userShareNew.setCreateTime(new Date());
//			userShareNew.setIsShare(WechatActConstants.IsShareEnum.IS_SHARE.getValue());
//			userShareNew.setLuckDrawNum(1);
//			userShareNew.setUsedLuckDrawNum(0);
//			myBatisDao.insert("USER_SHARE.insert", userShareNew);
//			return userShareNew;
//		}
//	}
	@Override
	public List<PrizePool> getPrizePoolList(Boolean lock){
		Map map = new HashMap();
        map.put("activityNumber", activityNumber);
        if(lock){
        	map.put("lock", lock);
        }
		return myBatisDao.getList("PRIZE_POOL.selectByActivityNum",map);
	}
	@Override
	public PrizePool getPrizePoolById(Long prizePoolId, Boolean lock){
		Map map = new HashMap();
        map.put("prizePoolId", prizePoolId);
        if(lock){
        	map.put("lock", lock);
        }
		return myBatisDao.get("PRIZE_POOL.selectByPrimaryKey",map);
	}
	@Override
	@Transactional
	public int turntableDraw(UserInfo userInfo, UserShare userShare, BigDecimal allBuyBalance){
		List<PrizePool> prizePoolList = getPrizePoolList(false);
		if(prizePoolList.size()<=0)
			return -2;
		int gameWinningLevel = getGameWinningLevel(prizePoolList, allBuyBalance);
		PrizePool prizePool = prizePoolList.get(prizePoolList.size()-1);
		if(gameWinningLevel != -1){
			prizePool = prizePoolList.get(gameWinningLevel-1);
		}
		prizePool = getPrizePoolById(prizePool.getPrizePoolId(), true);
		if(prizePool.getPrizeNum()>0){
			prizePool.setPrizeNum(prizePool.getPrizeNum()-1);
		}else{
			prizePool = prizePoolList.get(prizePoolList.size()-1);
			prizePool.setPrizeNum(prizePool.getPrizeNum()-1);
			gameWinningLevel=-1;
		}
		VoucherPhone vp = new VoucherPhone();
		vp.setActivityNumber(activityNumber);
		vp.setCreateTime(new Date());
		vp.setMobileNo(userInfo.getMobileNo());
		vp.setRealName(userInfoService.getUserExtByUserId(userInfo.getUserId()).getRealName());
		vp.setPrizeId(prizePool.getPrizePoolId());
		userShare.setUsedLuckDrawNum(userShare.getUsedLuckDrawNum()+1);
		addVoucherPhone(vp);
		updateUserShare(userShare);
		myBatisDao.update("PRIZE_POOL.updateByPrimaryKeySelective", prizePool);
		//虚拟奖品直接发放
		if(prizePool.getPrizeType().equals(WechatActConstants.PrizeTypeEnum.VIRTUAL.getValue())){
			voucherService.handOut(prizePool.getPrizeProductId(),userInfo.getUserId(),VoucherConstants.SourceType.OTHER.getValue(),"活动奖励");
		}
		return gameWinningLevel;
	}
	public int getGameWinningLevel(List<PrizePool> list, BigDecimal allallBuyBalance){ 
		//获得奖池奖品和中奖率
		//List<PrizePool> list = myBatisDao.getList("PRIZE_POOL.selectByActivityNum",activityNumber);
		getWinningPro(list, allallBuyBalance);
	    // 中奖等级：未中奖   
	    int winningLevel = -1;   
	  
	    if (list == null || list.size() <= 0) {   
	        return winningLevel;   
	    }   
	  
	    // 中奖随机号   
	    int randomWinningNo = 0;   
	    int args[] = new int[list.size() * 2];
	    int temp = (int) Math.round(Math.random() * 1000000000) % 1000000;   
	    int j = 0;   
	  
	    for (int i = 0; i < list.size(); i++) {   
	  
	        double tmpWinningPro = Double.valueOf(list.get(i).getWinningPro());   
	  
	        if (j == 0) {   
	            args[j] = randomWinningNo;   
	        } else {   
	            args[j] = args[j - 1] + 1;   
	        }   
	        args[j + 1] = args[j] + (int) Math.round(tmpWinningPro * 10000) - 1;   
	  
	        if (temp >= args[j] && temp <= args[j + 1]) {   
	            winningLevel = i + 1;   
	            return winningLevel; 
	        }   
	        j += 2;   
	    }   
	    return winningLevel; 
	}
	public void getWinningPro(List<PrizePool> poolList,BigDecimal allAcountBlance){
//		 1档	 100≤x<5000	 50%	 0	 0	 0	 0	 5%	 15%	 25%	 5%	 100%
//		 2档	 5000≤x<10000	 50%	 0	 0	 0	 5%	 20%	 15%	 5%	 5%	 100%
//		 3档	 1万≤x<5万	 30%	 0	 0	 5%	 20%	 15%	 15%	 15%	 0	 100%
//		 4档	 5万≤x<10万	 30%	 0	 0	 10%	 25%	 20%	 10%	 5%	 0	 100%
//		 5档	 10万≤x<20万	 10%	 0%	 10%	 20%	 30%	 10%	 15%	 5%	 0	 100%
//		 6档	 20万≤x<50万	 10%	 10%	 30%	 10%	 0	 10%	 20%	 10%	 0	 100%
//		 7档	 50万≤x<100万	 5%	 20%	 40%	 0%	 0	 5%	 25%	 5%	 0	 100%
//		 8档	 100万及以上	 5%	 30%	 0%	 30%	 0	 5%	 20%	 10%	 0	 100%
		Double[] winRate = new Double[]{0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.0};
		if(allAcountBlance.compareTo(new BigDecimal(100)) >=0 && allAcountBlance.compareTo(new BigDecimal(5000)) < 0){
			winRate = new Double[]{0.00,0.00,0.00,0.00,5.00,15.00,25.00,5.00,50.00};
		}else if(allAcountBlance.compareTo(new BigDecimal(5000)) >=0 && allAcountBlance.compareTo(new BigDecimal(10000)) < 0){
			winRate = new Double[]{0.00,0.00,0.00,5.00,20.00,15.00,5.00,5.00,50.00};
		}else if(allAcountBlance.compareTo(new BigDecimal(10000)) >=0 && allAcountBlance.compareTo(new BigDecimal(50000)) < 0){
			winRate = new Double[]{0.00,0.00,5.00,20.00,15.00,15.00,15.00,0.00,30.00};
		}else if(allAcountBlance.compareTo(new BigDecimal(50000)) >=0 && allAcountBlance.compareTo(new BigDecimal(100000)) < 0){
			winRate = new Double[]{0.00,0.00,10.00,25.00,20.00,10.00,5.00,0.00,30.00};
		}else if(allAcountBlance.compareTo(new BigDecimal(100000)) >=0 && allAcountBlance.compareTo(new BigDecimal(200000)) < 0){
			winRate = new Double[]{0.00,10.00,20.00,30.00,10.00,15.00,5.00,0.00,10.00};
		}else if(allAcountBlance.compareTo(new BigDecimal(200000)) >=0 && allAcountBlance.compareTo(new BigDecimal(500000)) < 0){
			winRate = new Double[]{10.00,30.00,10.00,0.00,10.00,20.00,10.00,0.00,10.00};
		}else if(allAcountBlance.compareTo(new BigDecimal(500000)) >=0 && allAcountBlance.compareTo(new BigDecimal(1000000)) < 0){
			winRate = new Double[]{20.00,40.00,0.00,0.00,5.00,25.00,5.00,0.00,5.00};
		}else{
			winRate = new Double[]{30.00,0.00,30.00,0.00,5.00,20.00,10.00,0.00,5.00};
		}
		matchingWinningRate(poolList, winRate);
	}
	public void matchingWinningRate(List<PrizePool> poolList,Double[] winRate){
		for(int i=0;i<poolList.size();i++){
			if(i==0){
				if(WechatActConstants.CoLtdStatusEnum.UN_USAGE.getValue()
						.equals(poolList.get(i).getDisable()) || 0 >= poolList.get(i).getPrizeNum()){
					poolList.get(i).setWinningPro(0.00);
				}else{
					poolList.get(i).setWinningPro(winRate[i]);
				}
			}else if(i==1){
				if(WechatActConstants.CoLtdStatusEnum.UN_USAGE.getValue()
						.equals(poolList.get(i).getDisable()) || 0 >= poolList.get(i).getPrizeNum()){
					poolList.get(i).setWinningPro(0.00);
				}else{
					poolList.get(i).setWinningPro(winRate[i]);
				}
			}else if(i==2){
				if(WechatActConstants.CoLtdStatusEnum.UN_USAGE.getValue()
						.equals(poolList.get(i).getDisable()) || 0 >= poolList.get(i).getPrizeNum()){
					poolList.get(i).setWinningPro(0.00);
				}else{
					poolList.get(i).setWinningPro(winRate[i]);
				}
			}else if(i==3){
				if(WechatActConstants.CoLtdStatusEnum.UN_USAGE.getValue()
						.equals(poolList.get(i).getDisable()) || 0 >= poolList.get(i).getPrizeNum()){
					poolList.get(i).setWinningPro(0.00);
				}else{
					poolList.get(i).setWinningPro(winRate[i]);
				}
			}else if(i==4){
				if(WechatActConstants.CoLtdStatusEnum.UN_USAGE.getValue()
						.equals(poolList.get(i).getDisable()) || 0 >= poolList.get(i).getPrizeNum()){
					poolList.get(i).setWinningPro(0.00);
				}else{
					poolList.get(i).setWinningPro(winRate[i]);
				}
			}else if(i==5){
				if(WechatActConstants.CoLtdStatusEnum.UN_USAGE.getValue()
						.equals(poolList.get(i).getDisable()) || 0 >= poolList.get(i).getPrizeNum()){
					poolList.get(i).setWinningPro(0.00);
				}else{
					poolList.get(i).setWinningPro(winRate[i]);
				}
			}else if(i==6){
				if(WechatActConstants.CoLtdStatusEnum.UN_USAGE.getValue()
						.equals(poolList.get(i).getDisable()) || 0 >= poolList.get(i).getPrizeNum()){
					poolList.get(i).setWinningPro(0.00);
				}else{
					poolList.get(i).setWinningPro(winRate[i]);
				}
			}else if(i==7){
				if(WechatActConstants.CoLtdStatusEnum.UN_USAGE.getValue()
						.equals(poolList.get(i).getDisable()) || 0 >= poolList.get(i).getPrizeNum()){
					poolList.get(i).setWinningPro(0.00);
				}else{
					poolList.get(i).setWinningPro(winRate[i]);
				}
			}else if(i==8){
				if(WechatActConstants.CoLtdStatusEnum.UN_USAGE.getValue()
						.equals(poolList.get(i).getDisable()) || 0 >= poolList.get(i).getPrizeNum()){
					poolList.get(i).setWinningPro(0.00);
				}else{
					poolList.get(i).setWinningPro(winRate[i]);
				}
			}
		}
	}

	@Override
	public int getUserDrawNum(Map map) {
		return myBatisDao.get("PRIZE_POOL.selectUserDrawNum",map);
	}

	@Override
	public List<VoucherPhoneVO> getHasLotteryDrawUsers(Map map) {
		return myBatisDao.getList("VOUCHER_PHONE.selectHasLotteryDrawUsers",map);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	@Override
	public Map startLotteryDraw(UserInfo user) {
		Map result=new HashMap();
		result.put("result", "1");
		result.put("msg", "ok");
		Map map=new HashMap();
		map.put("userId", user.getUserId());
		map.put("date", "2016-08-25");
		map.put("account", 10000);
		map.put("limitType", 2);
		map.put("limit", 3);
		int chance=lendOrderService.getUserOrderNumByCondition(map);
		if(chance==0){
			result.put("result", "0");
			result.put("msg", "非法操作");
			return result;
		}
		map.clear();
		map.put("mobile", user.getMobileNo());
		map.put("name", "increaseRate");
		int num=getUserDrawNum(map);
		chance=chance-num>=0?chance-num:0;
		if(chance==0){
			result.put("chance", chance);
			result.put("result", "0");
			result.put("msg", "您当前的抽奖次数已用完");
			return result;
		}
		map.clear();
		map.put("userId", user.getUserId());
		map.put("date", "2016-08-25");
		BigDecimal sum=lendOrderService.getUserAllBuyBalanceByCondition(map);
		LotteryDraw ld=new LotteryDraw(LotteryDraw.calLevel(sum));
		map.clear();
		map.put("productId", ld.getLotteryValue().getValue());
		map.put("name", "increaseRate");
		PrizePool pp=myBatisDao.get("PRIZE_POOL.selectByProductId",map);
		if(!ld.getLotteryResult().getValue().equals(LotteryDraw.LotteryResult.TYPE_ONE.getValue())&&!ld.getLotteryResult().getValue().equals(LotteryDraw.LotteryResult.TYPE_TWO.getValue())){
			if(pp.getPrizeNum()!=0){
				pp.setPrizeNum(pp.getPrizeNum()-1);
			}
		}
		if(pp.getPrizeNum()<=0){
			if(pp.getPrizeProductId().compareTo(Long.parseLong(LotteryDraw.Lottery.TYPE_ONE.getValue()))!=0){
				ld.changeLottertType();
				map.clear();
				map.put("productId", ld.getLotteryValue().getValue());
				map.put("name", "increaseRate");
				pp=myBatisDao.get("PRIZE_POOL.selectByProductId",map);
			}
		}
		myBatisDao.update("PRIZE_POOL.updateByPrimaryKeySelective", pp);
		UserInfoExt ue=userInfoExtService.getUserInfoExtById(user.getUserId());
		VoucherPhone vp=new VoucherPhone();
		vp.setPrizeId(pp.getPrizePoolId());
		vp.setMobileNo(user.getMobileNo());
		vp.setRealName(ue.getRealName());
		vp.setCreateTime(new Date());
		vp.setActivityNumber("increaseRate");
		myBatisDao.insert("VOUCHER_PHONE.insert", vp);
		if(!ld.getLotteryResult().getValue().equals(LotteryDraw.LotteryResult.TYPE_ONE.getValue())){
			VelocityContext context = new VelocityContext();
			context.put("value", ld.getLotteryValue().getDesc());
			String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_LOTTERY_DRAW_VM, context);
			smsService.sendMsg(user.getMobileNo(), content);
		}
		result.put("lottery", ld.getLotteryResult().getValue());
		return result;
	}
}
