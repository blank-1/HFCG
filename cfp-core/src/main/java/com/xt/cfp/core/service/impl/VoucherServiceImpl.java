package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.Exception.code.ext.VoucherErrorCode;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.RateProductVO;
import com.xt.cfp.core.pojo.ext.VoucherProductVO;
import com.xt.cfp.core.pojo.ext.VoucherVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.message.WechatMessageBody;
import com.xt.cfp.core.util.*;
import jodd.util.StringUtil;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VoucherServiceImpl implements VoucherService {


	private static final Logger LOGGER = Logger.getLogger(VoucherServiceImpl.class);

	@Autowired
	private MyBatisDao myBatisDao;
	@Autowired
	private UserOpenIdService userOpenIdService;
	@Autowired
	private ConstantDefineService constantDefineService;
	@Autowired
	private PayService payService;
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
	@Autowired
	private SmsService smsService;
	@Autowired
	private UserInfoExtService userInfoExtService;
	
	@Value(value = "${REGISTER_RELEASE_VOUCHER}")
	private String registerReleaseVoucher;//注册发放财富券，产品ID集合
	@Value(value = "${REGISTER_RELEASE_STARTTIME}")
	private String registerReleaseStarttime;//注册发放财富券，注册开始时间
	@Value(value = "${REGISTER_RELEASE_ENDTIME}")
	private String registerReleaseEndtime;//注册发放财富券，注册结束时间

	@Value(value = "${REGISTER_VOUCHER}")
	private String registerVoucher;//活动注册发放财富券，产品ID集合
	@Value(value = "${REGISTER_STARTTIME}")
	private String registerStarttime;//活动注册发放财富券，注册开始时间
	@Value(value = "${REGISTER_ENDTIME}")
	private String registerEndtime;//活动注册发放财富券，注册结束时间
	
	@Value(value = "${AUDIT_RELEASE_VOUCHER}")
	private String auditVoucher;//活动实名发放财富券，产品ID集合
	@Value(value = "${AUDIT_RELEASE_STARTTIME}")
	private String auditStarttime;//活动实名发放财富券，注册开始时间
	@Value(value = "${AUDIT_RELEASE_ENDTIME}")
	private String auditEndtime;//活动实名发放财富券，注册结束时间

	@Override
	public Pagination<VoucherProductVO> getVoucherProductPaging(int pageNo, int pageSize, VoucherProduct voucherProduct, Map<String, Object> customParams) {
		Pagination<VoucherProductVO> re = new Pagination<VoucherProductVO>();
		re.setCurrentPage(pageNo);
		re.setPageSize(pageSize);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("voucherProduct", voucherProduct);
		params.put("customParams", customParams);

		int totalCount = this.myBatisDao.count("getVoucherProductPaging", params);
		List<VoucherProductVO> users = this.myBatisDao.getListForPaging("getVoucherProductPaging", params, pageNo, pageSize);

		re.setTotal(totalCount);
		re.setRows(users);

		return re;
	}

	@Override
	public VoucherProduct addVoucherProduct(VoucherProduct voucherProduct) {
		myBatisDao.insert("VOUCHER_PRODUCT.insertSelective",voucherProduct);
		return voucherProduct;
	}

	@Override
	public VoucherProductVO getVoucherProductById(Long voucherProductId) {
		VoucherProductVO voucherProductVO = myBatisDao.get("VOUCHER_PRODUCT.detail", voucherProductId);
		return voucherProductVO;
	}

	@Override
	public void changeVoucherProductStatus(Long voucherProductId, VoucherConstants.VoucherProductStatus usage) {
		//判断参数是否为null
		if (voucherProductId == null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("voucherProductId", "null");

		if (usage == null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("usage", "null");

		//修改状态
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("voucherProductId", voucherProductId);
		params.put("status", usage.getValue());

		myBatisDao.update("VOUCHER_PRODUCT.changeBondSourceStatus", params);
	}

	@Override
	public Pagination<VoucherVO> getVoucherPaging(int pageNo, int pageSize, VoucherVO voucherVO, Map<String, Object> customParams) {
		Pagination<VoucherVO> re = new Pagination<VoucherVO>();
		re.setCurrentPage(pageNo);
		re.setPageSize(pageSize);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("voucher", voucherVO);
		params.put("customParams", customParams);

		int totalCount = this.myBatisDao.count("getVoucherPaging", params);
		List<VoucherVO> vos = this.myBatisDao.getListForPaging("getVoucherPaging", params, pageNo, pageSize);
		RateProductVO rateProductVO = new RateProductVO();
		//添加不可用财富券最近历史描述
		for (VoucherVO vo : vos){
			if (!VoucherConstants.VoucherStatus.UN_USAGE.getValue().equals(vo.getStatus())){
				VoucherPayOrderDetail recentVoucherPayOrderDetail = this.getRecentVoucherPayOrderDetail(vo.getVoucherId());
				if (recentVoucherPayOrderDetail!=null)
					vo.setDetailRemark(recentVoucherPayOrderDetail.getDetailRemark());
			}
			if(vo.getVoucherCouponType().equals("2")){//处理加息券条件
				rateProductVO.getRateProductConditionValue(vo.getCondition());
				vo.setConditionStr(rateProductVO.getCustomizeConditionDesc(true, true, false));
				vo.setAmountStartStr(rateProductVO.getRateProductConditionVO().getCon3_start_amount().toString());
			}
		}
		re.setTotal(totalCount);
		re.setRows(vos);

		return re;
	}

	@Override
	public List<VoucherProduct> getAvalibleHandOutVoucherProductList() {
		Date date = new Date();
		//刷新过期财富券产品
		this.refreshProductStatus(date);
		List<VoucherProduct> list = myBatisDao.getList("VOUCHER_PRODUCT.getAvalibleHandOutVoucherProductList", date);

		return list;
	}

	@Override
	@Transactional
	public void refreshProductStatus(Date date) {
		myBatisDao.update("VOUCHER_PRODUCT.refreshProductStatus", date);
	}

	@Override
	@Transactional
	public void refreshStatus(Date date) {
		List<Voucher> voucherLisgt = myBatisDao.getList("VOUCHER.refreshStatusList", date);
		for (Voucher v:voucherLisgt){
			//记录财富券记录
			VoucherPayOrderDetail vpd = new VoucherPayOrderDetail();
			vpd.setVoucherId(v.getVoucherId());
			vpd.setDetailId(v.getDetailId());
			vpd.setCreateTime(new Date());
			vpd.setStatus(VoucherConstants.VoucherUseStatus.OVER_TIME.getValue());
			vpd.setDetailRemark("财富券已过期");
			recordVoucherPayOrderDetail(vpd);
		}
		myBatisDao.update("VOUCHER.refreshStatus", date);

	}

	@Override
	public void sendGetVoucherSms(Long userId, Voucher voucher){
        try{
        	VelocityContext context = new VelocityContext();
        	context.put("value", voucher.getVoucherValue());
        	context.put("date",  DateUtil.getDateLongCnD(voucher.getEndDate()));
            String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_GETVOUCHERMSG_VM, context);
            UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(userId);
            smsService.sendMsg(userInfoExt.getMobileNo(), content);
        } catch (Exception e){
        	e.printStackTrace();
        }
    }

	private void sendVoucherOnOct(Long userId, String content){
		try{
			UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(userId);
			smsService.sendMsg(userInfoExt.getMobileNo(), content);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public Voucher handOut(Long voucherProductId,Long userId,String sourceType,String sourceDesc) {
		Voucher voucher = initVoucher(voucherProductId,userId,sourceType,sourceDesc,null);
		myBatisDao.insert("VOUCHER.insertSelective",voucher);
		
    	VoucherProductVO voucherProductVO = getVoucherProductById(voucher.getVoucherProductId());
    	//如果不是提现券才发放通知
    	if(!VoucherConstants.UsageScenario.WITHDRAW.getValue().equals(voucherProductVO.getUsageScenario())){
    		//公众号推送，获取通知
    		sendVoucherMessage(voucherProductId, userId, voucher.getEndDate());
    		
    		//短信发送，获取通知
    		this.sendGetVoucherSms(userId, voucher);
    	}
		
		return voucher;
	}

	@Override
	public Voucher sendVourcherOnOct(Long voucherProductId,Long userId,String sourceType,String sourceDesc) {
		Voucher voucher = initVoucher(voucherProductId,userId,sourceType,sourceDesc,null);
		myBatisDao.insert("VOUCHER.insertSelective",voucher);

		VoucherProductVO voucherProductVO = getVoucherProductById(voucher.getVoucherProductId());

		return voucher;
	}

	@Override
	public void handOut(Long voucherProductId,List<Long> userIds,String sourceType,String sourceDesc) {
		for (Long userId:userIds){
			handOut(voucherProductId,userId,sourceType,sourceDesc);
		}
	}

	@Override
	@Transactional
	public Voucher addVoucherBatch(List<Voucher> voucherList) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("list", voucherList);
		params.put("voucher", voucherList.get(0));

		myBatisDao.insert("VOUCHER.addVoucherBatch",params);
		return null;
	}


	/**
	 * 初始化财富券
	 * @return
	 */
	private Voucher initVoucher(Long voucherProductId,Long userId,String sourceType,String sourceDesc,BigDecimal cardinalValue){
		Voucher voucher = new Voucher();
		voucher.setStatus(VoucherConstants.VoucherStatus.UN_USAGE.getValue());
		voucher.setCreateDate(new Date());
		voucher.setVoucherProductId(voucherProductId);
		voucher.setUserId(userId);
		voucher.setSourceType(sourceType);
		voucher.setSourceDesc(sourceDesc);
		VoucherProductVO voucherProduct = this.getVoucherProductById(voucherProductId);
		if (voucherProduct.getEffectiveCount()!=null&&voucherProduct.getEffectiveCount()>0){
			voucher.setEndDate(DateUtils.addDays(voucher.getCreateDate(),voucherProduct.getEffectiveCount()));
		}
		if (voucherProduct.getVoucherType().equals(VoucherConstants.VoucherTypeEnum.FIXED.getValue())){
			voucher.setVoucherValue(voucherProduct.getAmount());
		}else{
			voucher.setVoucherValue(cardinalValue.multiply(voucherProduct.getRate()).divide(new BigDecimal("100")));
		}
		return voucher;
	}

	@Override
	@Transactional
	public void handOutToEveryOne(Long voucherProductId,String sourceType,String sourceDesc) {
		UserInfo user = new UserInfo();
		user.setType(UserType.COMMON.getValue());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userInfo", user);

		//一批500条数据
		int pageSize = 1000;
		int totalRow = this.myBatisDao.count("getUserInfoPaging", params);
		int totalPage = (totalRow - 1) / pageSize + 1;
		//批量发放财富券
		for (int i=1;i<=totalPage;i++){
			List<UserInfo> users = this.myBatisDao.getListForPaging("getUserInfoPaging", params, i, pageSize);
			List<Voucher> list = new ArrayList<Voucher>();
			for (UserInfo u:users){
				Voucher voucher = initVoucher(voucherProductId, u.getUserId(), sourceType, sourceDesc,null);
				Long id = (Long) myBatisDao.get("VOUCHER.getNextPK", null);
				voucher.setVoucherId(id);
				myBatisDao.getSqlSession().clearCache();
				list.add(voucher);
			}
			this.addVoucherBatch(list);
		}

	}

	@Override
	public VoucherVO getVoucherById(Long voucherId) {
		VoucherVO voucherVO = myBatisDao.get("VOUCHER.detail", voucherId);
		return voucherVO;
	}

	@Override
	public BigDecimal getAllVoucherValue(Long userId) {
		return  myBatisDao.get("VOUCHER.getAllVoucherValue", userId);
	}

	@Override
	public BigDecimal getAllVoucherValue(Long userId, VoucherConstants.VoucherStatus voucherStatus) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("voucherStatus", voucherStatus.getValue());

		return  myBatisDao.get("VOUCHER.getAllVoucherValueByStatus", params);
	}

	@Override
	public List<VoucherVO> getAllVoucherList(Long userId,BigDecimal buyBalance,VoucherConstants.UsageScenario ... usageScenarios) {
		List<String> statusList = new ArrayList<String>();
		if (usageScenarios == null || usageScenarios.length == 0) {
			statusList = null;
		} else {
			for (VoucherConstants.UsageScenario usageScenario : usageScenarios) {
				statusList.add(usageScenario.getValue());
			}
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("usageScenario", statusList);
		params.put("buyBalance", buyBalance);
		return myBatisDao.getList("VOUCHER.getAllVoucherList", params);
	}

	@Override
	@Transactional
	public void unFreezeVoucher(Long voucherId) {
		Voucher voucherLocked = this.myBatisDao.get("VOUCHER.selectByPrimaryKeyLock", voucherId);

		if (!voucherLocked.getStatus().equals(VoucherConstants.VoucherStatus.FREEZE.getValue()))
			throw new SystemException(VoucherErrorCode.VOUCHER_NOT_FREEZE_STATUS).set("id", voucherId).set("status", voucherLocked.getStatus()).set("detailId", voucherLocked.getDetailId());

		backVoucher(voucherLocked.getDetailId(), "返还", voucherId);
	}

	@Override
	@Transactional
	public void rollbackVoucherUse(Long voucherId) {
		Voucher voucherLocked = this.myBatisDao.get("VOUCHER.selectByPrimaryKeyLock", voucherId);

		if (!voucherLocked.getStatus().equals(VoucherConstants.VoucherStatus.USAGE.getValue()))
			throw new SystemException(VoucherErrorCode.VOUCHER_NOT_USEAGE_STATUS).set("id", voucherId).set("status", voucherLocked.getStatus()).set("detailId", voucherLocked.getDetailId());

		LendOrder lendOrder = payService.getLendOrderByPayOrderDetailId(voucherLocked.getDetailId());
		if (StringUtil.equalsOne(lendOrder.getOrderState(), new String[] {
				LendOrderConstants.LoanOrderStatusEnum.CLEAR.getValue(), LendOrderConstants.LoanOrderStatusEnum.REPAYMENTING.getValue()
		}) != -1)
			throw new SystemException(VoucherErrorCode.VOUCHER_CANNOT_CANCEL).set("id", voucherId).set("status", voucherLocked.getStatus()).set("detailId", voucherLocked.getDetailId()).set("reason", "订单所属借款已被放款").set("lendOrderId", lendOrder.getLendOrderId());

		backVoucher(voucherLocked.getDetailId(), "返还", voucherId);
	}

	@Override
	@Transactional
	public BigDecimal frozeVoucher(PayOrder payOrder) {
		BigDecimal voucherValue = BigDecimal.ZERO;
		PayOrderDetail payOrderDetail = payService.getPayOrderDetailByPayIdAndAmountType(payOrder.getPayId(), PayConstants.AmountType.VOUCHERS.getValue());
		if (payOrderDetail!=null){
			List<Voucher> voucherVOs = this.getVoucherList(payOrderDetail.getDetailId(),VoucherConstants.UsageScenario.WHOLE, VoucherConstants.UsageScenario.FINANCE, VoucherConstants.UsageScenario.LOAN);
			//如果没有找到对应的财富劵，就抛出异常
			if (voucherVOs==null||voucherVOs.size()==0)
				throw new SystemException(VoucherErrorCode.VOUCHER_NOT_FOUND).set("detailId", payOrderDetail.getDetailId());

			for (Voucher vo:voucherVOs){
				voucherValue = voucherValue.add(vo.getVoucherValue());
				//锁定财富券
				Voucher voucherLocked = myBatisDao.get("VOUCHER.selectByPrimaryKeyLock",vo.getVoucherId());
				if (!VoucherConstants.VoucherStatus.UN_USAGE.getValue().equals(voucherLocked.getStatus())){
					throw new SystemException(VoucherErrorCode.VOUCHER_USED).set("status",voucherLocked.getStatus());
				}
				//冻结财富券
				voucherLocked.setDetailId(payOrderDetail.getDetailId());//如果支付成功记录财富券使用的支付单
				voucherLocked.setStatus(VoucherConstants.VoucherStatus.FREEZE.getValue());
				myBatisDao.update("VOUCHER.updateByPrimaryKeySelective", voucherLocked);
				//记录财富券记录
				VoucherPayOrderDetail vpd = new VoucherPayOrderDetail();
				vpd.setVoucherId(voucherLocked.getVoucherId());
				vpd.setDetailId(vo.getDetailId());
				vpd.setCreateTime(new Date());
				vpd.setStatus(VoucherConstants.VoucherUseStatus.FREEZE.getValue());
				//获得订单信息
				LendOrder lendOrder = payService.getLendOrderByPayOrderDetailId(vo.getDetailId());
				vpd.setDetailRemark("订单：【"+lendOrder.getLendOrderName()+"】中已冻结");
				recordVoucherPayOrderDetail(vpd);
			}
		}
		return voucherValue;
	}

	@Override
	@Transactional
	public void frozeVoucherWithDraw(Long voucherId,WithDraw withDraw){
		Voucher voucher = new Voucher();
		voucher.setVoucherId(voucherId);
		//冻结财富券
		voucher.setDetailId(withDraw.getWithdrawId());
		voucher.setStatus(VoucherConstants.VoucherStatus.FREEZE.getValue());
		myBatisDao.update("VOUCHER.updateByPrimaryKeySelective", voucher);
		//记录财富券记录
		VoucherPayOrderDetail vpd = new VoucherPayOrderDetail();
		vpd.setVoucherId(voucher.getVoucherId());
		vpd.setCreateTime(new Date());
		vpd.setStatus(VoucherConstants.VoucherUseStatus.FREEZE.getValue());
		vpd.setDetailRemark("提现单：【"+withDraw.getWithdrawId()+"】中已冻结");
		recordVoucherPayOrderDetail(vpd);
	}

	@Override
	public List<VoucherProductVO> getVoucherStatistics(Long userId,VoucherConstants.UsageScenario... usageScenarios) {
		List<String> statusList = new ArrayList<String>();
		if (usageScenarios == null || usageScenarios.length == 0) {
			statusList = null;
		} else {
			for (VoucherConstants.UsageScenario usageScenario : usageScenarios) {
				statusList.add(usageScenario.getValue());
			}
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("usageScenario", statusList);
		return myBatisDao.getList("VOUCHER_PRODUCT.getVoucherStatistics", params);
	}

	@Override
	public BigDecimal calcVoucherValue(PayOrder payOrder,VoucherConstants.VoucherStatus status) {
		BigDecimal voucherValue = BigDecimal.ZERO;
		PayOrderDetail payOrderDetail = payService.getPayOrderDetailByPayIdAndAmountType(payOrder.getPayId(), PayConstants.AmountType.VOUCHERS.getValue());
		if (payOrderDetail!=null){
			List<Voucher> voucherVOs = this.getVoucherList(payOrderDetail.getDetailId(),VoucherConstants.UsageScenario.WHOLE, VoucherConstants.UsageScenario.FINANCE, VoucherConstants.UsageScenario.LOAN);
			//保存每个财富券使用的支付订单明细
			if (voucherVOs==null||voucherVOs.size()==0)
				throw new SystemException(VoucherErrorCode.VOUCHER_NOT_FOUND).set("detailId", payOrderDetail.getDetailId());

			for (Voucher vo:voucherVOs){
				if (!vo.getStatus().equals(status.getValue())){
					return BigDecimal.ZERO;
				}
				voucherValue = voucherValue.add(vo.getVoucherValue());
			}
		}
		return voucherValue;
	}

	@Override
	public BigDecimal calcVoucherValue(PayOrder payOrder) {
		BigDecimal result = BigDecimal.ZERO;
		PayOrderDetail detail = payService.getPayOrderDetailByPayIdAndAmountType(payOrder.getPayId(), PayConstants.AmountType.VOUCHERS.getValue());
		if (detail != null)
			return detail.getAmount() ;

		return result;
	}

	@Override
	public boolean isVoucherAvaliable(PayOrder payOrder) {
		PayOrderDetail payOrderDetail = payService.getPayOrderDetailByPayIdAndAmountType(payOrder.getPayId(), PayConstants.AmountType.VOUCHERS.getValue());
		if (payOrderDetail!=null){
			List<Voucher> voucherVOs = this.getVoucherList(payOrderDetail.getDetailId(),VoucherConstants.UsageScenario.WHOLE, VoucherConstants.UsageScenario.FINANCE, VoucherConstants.UsageScenario.LOAN);
			//保存每个财富券使用的支付订单明细
			if (voucherVOs==null||voucherVOs.size()==0) {
				//return false;
				return false;
			}
			//每个财富券状态是否为未使用
			for (Voucher v :voucherVOs){
				if (!v.getStatus().equals(VoucherConstants.VoucherStatus.UN_USAGE.getValue())){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 获取支付单和财富券关系
	 * @param detailId
	 * @return
	 */
	private List<VoucherPayOrderDetail> getVoucherPayOrderDetails(Long detailId,VoucherConstants.UsageScenario ... usageScenarios) {
		List<String> statusList = new ArrayList<String>();
		if (usageScenarios == null || usageScenarios.length == 0) {
			statusList = null;
		} else {
			for (VoucherConstants.UsageScenario usageScenario : usageScenarios) {
				statusList.add(usageScenario.getValue());
			}
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("detailId", detailId);
		params.put("usageScenario", statusList);
		return myBatisDao.getList("VOUCHER.getVoucherBindingHis",params);
	}

	@Override
	@Transactional
	public void linkVoucher(PayOrder payOrder, List<VoucherVO> voucherVOs) {
		//保存每个财富券使用的支付订单明细
		if (voucherVOs==null||voucherVOs.size()==0){
			return;
		}
		for (PayOrderDetail payOrderDetail:payOrder.getPayOrderDetails()){
			if (PayConstants.AmountType.VOUCHERS.getValue().equals(payOrderDetail.getAmountType())){
				for (VoucherVO vo:voucherVOs){
					//财富券和支付搭建关系
					Voucher voucher = new Voucher();
					voucher.setVoucherId(vo.getVoucherId());
					voucher.setDetailId(payOrderDetail.getDetailId());
					myBatisDao.update("VOUCHER.updateByPrimaryKeySelective", voucher);
					//记录财富券记录
					VoucherPayOrderDetail vpd = new VoucherPayOrderDetail();
					vpd.setVoucherId(voucher.getVoucherId());
					vpd.setDetailId(voucher.getDetailId());
					vpd.setCreateTime(new Date());
					vpd.setStatus(VoucherConstants.VoucherUseStatus.BING.getValue());
					LendOrder lendOrder = payService.getLendOrderByPayOrderDetailId(payOrderDetail.getDetailId());
					vpd.setDetailRemark("订单：【" + lendOrder.getLendOrderName() + "】中已绑定");
					recordVoucherPayOrderDetail(vpd);
				}
			}
		}
	}

	@Override
	public List<VoucherPayOrderDetail> getVoucherPayOrderDetail(Long voucherId) {
		return myBatisDao.getList("VOUCHER.getVoucherPayOrderDetail", voucherId);
	}

	@Override
	public VoucherPayOrderDetail getRecentVoucherPayOrderDetail(Long voucherId) {
		return myBatisDao.get("VOUCHER.getRecentVoucherPayOrderDetail", voucherId);
	}

	@Override
	public VoucherPayOrderDetail recordVoucherPayOrderDetail(VoucherPayOrderDetail voucherPayOrderDetail) {
		myBatisDao.insert("VOUCHER.insertDetailSelective",voucherPayOrderDetail);
		return voucherPayOrderDetail;
	}

	@Override
	public List<Voucher> getVoucherList(Long detailId,VoucherConstants.UsageScenario ... usageScenarios) {
		List<String> statusList = new ArrayList<String>();
		if (usageScenarios == null || usageScenarios.length == 0) {
			statusList = null;
		} else {
			for (VoucherConstants.UsageScenario usageScenario : usageScenarios) {
				statusList.add(usageScenario.getValue());
			}
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("detailId", detailId);//提现单id/支付明细id
		params.put("usageScenario", statusList);
		List<Voucher> voucherList = myBatisDao.getList("VOUCHER.getVoucherList", params);

		//支付时才回用到，财富券冻结后则此块代码失效
		if (voucherList == null||voucherList.size()==0){
			List<Voucher> list = new ArrayList<Voucher>();

			List<VoucherPayOrderDetail> bindLinks = getVoucherPayOrderDetails(detailId,usageScenarios);
			if (bindLinks==null)
				return null;
			for (VoucherPayOrderDetail vpd:bindLinks){
				Voucher v = this.getVoucherById(vpd.getVoucherId());
				//财富券冻结后则此块代码失效
				if (!v.getStatus().equals(VoucherConstants.VoucherStatus.UN_USAGE.getValue())){
					return null;
				}
				list.add(v);
			}
			return list;
		}

		return voucherList;
	}

	/**
	 * 财富券校验
	 * @return
	 */
	@Override
	public JsonView voucherValidate(UserInfo currentUser,LendOrder lendOrder,List<VoucherVO> voucherList){
		Date now = new Date();
		BigDecimal voucherValue = BigDecimal.ZERO;
		for (VoucherVO vo:voucherList){
			//财富券是否为当前用户所有
			if (!vo.getUserId().equals(currentUser.getUserId())){
				return JsonView.JsonViewFactory.create().success(false).info("请用自己的财富券进行支付！").put("id", "redirect");
			}
			//状态校验
			if (!vo.getStatus().equals(VoucherConstants.VoucherStatus.UN_USAGE.getValue())){
				return JsonView.JsonViewFactory.create().success(false).info("你选用的财富券不可用！").put("id", "redirect");
			}
			//时间校验
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				if (vo.getEndDate()!=null){
					Date _date = DateUtils.addDays(vo.getEndDate(), 1);
					Date date = sdf.parse(sdf.format(_date));
					if (date.getTime()<now.getTime()){
						this.refreshStatus(new Date());
						return JsonView.JsonViewFactory.create().success(false).info("你选用的财富券已过期！").put("id", "redirect");
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//使用条件
			if (vo.getConditionAmount().compareTo(lendOrder.getBuyBalance())>0){
				return JsonView.JsonViewFactory.create().success(false).info("你选用的财富券不符合使用条件！").put("id", "redirect");
			}

			if (vo.getVoucherValue().compareTo(lendOrder.getBuyBalance())>0){//财富券金额大于订单金额
				return JsonView.JsonViewFactory.create().success(false).info("你选用的财富券不符合使用条件！").put("id", "redirect");
			}

			//使用场景
			if (!vo.getUsageScenario().equals(VoucherConstants.UsageScenario.WHOLE.getValue())){
				if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())){
					//投标订单
					if (!vo.getUsageScenario().equals(VoucherConstants.UsageScenario.LOAN.getValue())){
						return JsonView.JsonViewFactory.create().success(false).info("你选用的财富券不符合使用条件！").put("id", "redirect");
					}

				}
				if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())){
					//理财
					if (!vo.getUsageScenario().equals(VoucherConstants.UsageScenario.FINANCE.getValue())){
						return JsonView.JsonViewFactory.create().success(false).info("你选用的财富券不符合使用条件！").put("id", "redirect");
					}
				}
			}

			voucherValue = voucherValue.add(vo.getVoucherValue());
		}

		if (lendOrder.getBuyBalance().compareTo(voucherValue)<0){
			//理财
			return JsonView.JsonViewFactory.create().success(false).info("财富券总额不能大于订单金额").put("id", "redirect");
		}
		return JsonView.JsonViewFactory.create().success(true);
	}

	@Override
	public void changeVoucherStatus(Long voucherId, VoucherConstants.VoucherStatus voucherStatus) {
		Voucher voucher = new Voucher();
		voucher.setVoucherId(voucherId);
		voucher.setStatus(voucherStatus.getValue());
		if (voucherStatus.getValue().equals(VoucherConstants.VoucherStatus.USAGE.getValue()))
			voucher.setUsageDate(new Date());
		myBatisDao.update("VOUCHER.updateByPrimaryKeySelective", voucher);
	}

	@Override
	@Transactional
	public void useVoucher(List<Voucher> voucherList) {
		if (voucherList!=null){
			for (Voucher voucher:voucherList){
				VoucherVO voucherVO = getVoucherById(voucher.getVoucherId());
				changeVoucherStatus(voucher.getVoucherId(), VoucherConstants.VoucherStatus.USAGE);
				//记录财富券记录
				VoucherPayOrderDetail vpd = new VoucherPayOrderDetail();
				vpd.setVoucherId(voucher.getVoucherId());
				vpd.setDetailId(voucher.getDetailId());
				vpd.setCreateTime(new Date());
				vpd.setStatus(VoucherConstants.VoucherUseStatus.USAGE.getValue());
				if (voucherVO.getUsageScenario().equals(VoucherConstants.UsageScenario.WITHDRAW.getValue())){
					vpd.setDetailRemark("提现单：【"+voucherVO.getDetailId()+"】中已使用");
				}else {
					LendOrder lendOrder = payService.getLendOrderByPayOrderDetailId(voucher.getDetailId());
					vpd.setDetailRemark("订单：【"+lendOrder.getLendOrderName()+"】中已使用");
				}
				//获得订单信息
				recordVoucherPayOrderDetail(vpd);
			}
		}
	}

	@Override
	@Transactional
	public void publishVoucher(PayOrder payOrder) {
		//发放代金券开关
		if (!isPublishVoucher())
			return ;

		//计算支付单金额（去除财富券部分）
		BigDecimal voucherValue = BigDecimal.ZERO;
		PayOrderDetail payOrderDetail = payService.getPayOrderDetailByPayIdAndAmountType(payOrder.getPayId(), PayConstants.AmountType.VOUCHERS.getValue());
		if (payOrderDetail!=null){
			List<Voucher> voucherVOs = this.getVoucherList(payOrderDetail.getDetailId(),VoucherConstants.UsageScenario.WHOLE, VoucherConstants.UsageScenario.FINANCE, VoucherConstants.UsageScenario.LOAN);
			//保存每个财富券使用的支付订单明细
			if (voucherVOs!=null&&voucherVOs.size()>0) {
				for (Voucher vo:voucherVOs){
					if (!vo.getStatus().equals(VoucherConstants.VoucherStatus.USAGE.getValue())){
						voucherValue = BigDecimal.ZERO;
						break;
					}
					voucherValue = voucherValue.add(vo.getVoucherValue());
				}
			}
		}
		BigDecimal buyAmount = payOrder.getAmount().subtract(voucherValue);
		//根据实际支付金额选择发放财富券
		BigDecimal value = buyAmount.divideToIntegralValue(new BigDecimal(5000));
		Long voucherProductId = null;
		BigDecimal publishValue = BigDecimal.ZERO;
		if (value.compareTo(new BigDecimal(1))==0){
			//5元
			voucherProductId = voucherFive;
			publishValue = new BigDecimal(5);
		}else if (value.compareTo(new BigDecimal(2))>=0&&value.compareTo(new BigDecimal(4))<0){
			//10元
			voucherProductId = voucherTen;
			publishValue = new BigDecimal(10);
		}else if (value.compareTo(new BigDecimal(4))>=0&&value.compareTo(new BigDecimal(10))<0){
			//20元
			voucherProductId = voucherTwenty;
			publishValue = new BigDecimal(20);
		}else if (value.compareTo(new BigDecimal(10))>=0&&value.compareTo(new BigDecimal(20))<0){
			//50元
			voucherProductId = voucherFifty;
			publishValue = new BigDecimal(50);
		}else if (value.compareTo(new BigDecimal(20))>=0){
			//100元
			voucherProductId = voucherHundred;
			publishValue = new BigDecimal(100);
		}
		if (voucherProductId!=null){
			String desc = null;
			//购买理财
			if (payOrder.getBusType().equals(PayConstants.BusTypeEnum.BUY_FINANCE.getValue())) {
				desc = "购买理财奖励";
				//投标借款
			} else if (payOrder.getBusType().equals(PayConstants.BusTypeEnum.BID_LOAN.getValue())){
				desc = "投标奖励";
			}
			LOGGER.info(LogUtils.createSimpleLog("投资奖励", "发放财富券：" + publishValue + "元"));
			handOut(voucherProductId,payOrder.getUserId(),VoucherConstants.SourceType.OTHER.getValue(),desc);
		}
	}

	@Override
	public void exportExcel(HttpServletResponse response, VoucherVO voucherVO, Map<String, Object> customParams) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("voucher", voucherVO);
		params.put("customParams", customParams);

		List<VoucherVO> vos  = myBatisDao.getList("VOUCHER.exportExcel",params);
		List<LinkedHashMap<String, Object>> dataMap = new ArrayList<LinkedHashMap<String, Object>>();
		for (VoucherVO vvo : vos){
			LinkedHashMap<String,Object> linkedHashMap = new LinkedHashMap<String,Object>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			linkedHashMap.put("名称",vvo.getVoucherName());
			linkedHashMap.put("持有人",vvo.getUserName());
			linkedHashMap.put("真实姓名",vvo.getRealName());
			linkedHashMap.put("金额",vvo.getAmountStr());
			linkedHashMap.put("有效期",vvo.getEffictiveDate());
			linkedHashMap.put("使用条件",vvo.getConditionAmountStr());
			linkedHashMap.put("使用场景",vvo.getUsageScenarioStr());
			linkedHashMap.put("叠加使用",vvo.getIsOverlyStr());
			linkedHashMap.put("来源",vvo.getSourceStr());
			linkedHashMap.put("获得时间",vvo.getCreateDate()!=null?sdf.format(vvo.getCreateDate()):"");
			linkedHashMap.put("使用时间", vvo.getUsageDate()!=null?sdf.format(vvo.getUsageDate()):"");
			linkedHashMap.put("订单金额",vvo.getBuyBalance());
			linkedHashMap.put("标的名称",vvo.getLoanTitle());
			linkedHashMap.put("状态",vvo.getStatusStr());
			dataMap.add(linkedHashMap);
		}
		ResponseUtil.sendExcel(response, dataMap, "财富券信息表");
	}
	@Override
	public void sendVoucherMessage(Long voucherProductId, Long userId, Date endDate){
		try {	
            UserOpenId userOpenId = userOpenIdService.getOpenIdByCondition(userId, null, null);
            if(userOpenId != null && !"".equals(userOpenId.getOpenId())){
            	VoucherProductVO voucherProductVO = getVoucherProductById(voucherProductId);
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
            	wechatMessageBody.setOpenId(userOpenId.getOpenId());
            	wechatMessageBody.setAccess_token(accessTokenList.get(0).getConstantValue());
            	wechatMessageBody.setEndDate(DateUtil.getDateLongCnD(endDate));
            	if(VoucherConstants.UsageScenario.WITHDRAW.getValue().equals(voucherProductVO.getUsageScenario())){
            		wechatMessageBody.setFlag("3");
            		wechatMessageBody.setVoucherName("提现劵");
            	}
            	else{
            		wechatMessageBody.setFlag("2");
            		wechatMessageBody.setVoucherName(voucherProductVO.getVoucherName());
            	}
            	Sign.sendWechatMsg(wechatMessageBody);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	@Transactional
	public void backVoucher(Long detailId,String remark,Long... voucherId) {
		if (voucherId==null)
			return;
		for (Long id : voucherId){
			Voucher voucher = new Voucher();
			voucher.setVoucherId(id);
			voucher.setStatus(VoucherConstants.VoucherStatus.UN_USAGE.getValue());
			myBatisDao.update("VOUCHER.updateByPrimaryKeySelective", voucher);
			//记录历史
			VoucherPayOrderDetail voucherPayOrderDetail = new VoucherPayOrderDetail();
			voucherPayOrderDetail.setCreateTime(new Date());
			voucherPayOrderDetail.setDetailId(detailId);
			voucherPayOrderDetail.setStatus(VoucherConstants.VoucherUseStatus.BACK.getValue());//返还
			voucherPayOrderDetail.setDetailRemark(remark);//"因流标已返还"
			myBatisDao.update("VOUCHER.updateDetailByPrimaryKeySelective", voucherPayOrderDetail);
			//刷新财富券状态
			this.refreshStatus(new Date());
		}
	}
	@Override
	@Transactional
	public void wechatVoucherExpireMsg(Date nowDate){
		List<Voucher> voucherList = myBatisDao.getList("VOUCHER.wechatVoucherExpireMsg", nowDate);
		for (Voucher v:voucherList){
			try {	
	            UserOpenId userOpenId = userOpenIdService.getOpenIdByCondition(v.getUserId(), null, null);
	            if(userOpenId != null && !"".equals(userOpenId.getOpenId())){
	            	VoucherProductVO voucherProductVO = getVoucherProductById(v.getVoucherProductId());
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
	            	wechatMessageBody.setOpenId(userOpenId.getOpenId());
	            	wechatMessageBody.setAccess_token(accessTokenList.get(0).getConstantValue());
	            	if(VoucherConstants.UsageScenario.WITHDRAW.getValue().equals(voucherProductVO.getUsageScenario())){
	            		wechatMessageBody.setFlag("5");
	            	}
	            	else{
	            		wechatMessageBody.setFlag("4");
	            		wechatMessageBody.setBalance(voucherProductVO.getAmount());
	            	}
	            	Sign.sendWechatMsg(wechatMessageBody);
	            }
			} catch (Exception e) {
				System.out.println("到期提醒推送接收失败！");
				e.printStackTrace();
			}
			
		}
	}
	
	@Override
	public List<Voucher> getVoucherExpireByUserId(Long userId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("nowDate", new Date());
		return myBatisDao.getList("VOUCHER.getVoucherExpireByUserId", params);
	}
	
	@Override
	public void sendExpireVoucherMsgForTimer() {
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
				List<Voucher> vs = this.getVoucherExpireByUserId(u.getUserId());
//				myBatisDao.getSqlSession().clearCache();
				
				if(null != vs && vs.size() > 0){
					BigDecimal sum = BigDecimal.ZERO;
					for (Voucher v : vs) {
						sum = sum.add(v.getVoucherValue());
					}
					//发送短信信息，财富券即将到期通知
					this.sendSmsExpireVoucherMsg(u.getMobileNo(), vs.size(), sum);
					
				}
				
			}
		}
	}

	@Override
	public Voucher getVoucherByPayId(Long payId) {
		PayOrderDetail orderDetail = this.payService.getPayOrderDetailByPayIdAndAmountType(payId, PayConstants.AmountType.VOUCHERS.getValue());
		return this.myBatisDao.get("VOUCHER.getVoucherByDetailId", orderDetail.getDetailId());
	}

	@Override
	public void sendSmsExpireVoucherMsg(String mobileNo, Integer count, BigDecimal value){
        try{
        	VelocityContext context = new VelocityContext();
        	context.put("count", count);
        	context.put("value", value);
            String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_EXPIREVOUCHERMSG_VM, context);
            smsService.sendMsg(mobileNo, content);
        } catch (Exception e){
        	e.printStackTrace();
        }
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

	public String getVoucherFlag() {
		return voucherFlag;
	}

	public void setVoucherFlag(String voucherFlag) {
		this.voucherFlag = voucherFlag;
	}

	/**
	 * 触发节点开关
	 * @return
	 */
	public boolean isPublishVoucher(){
		return this.getVoucherFlag().equals("1")?true:false;
	}

	@Override
	public void registerRelease(Long userId) {
		
		LOGGER.info("注册发放财富券【开始】，userId:" + userId);
		
		try {
			
			LOGGER.info("注册发放财富券，读取配置信息: startTime：" + registerReleaseStarttime + ", endTime:" + registerReleaseEndtime + ", vids:" + registerReleaseVoucher);
			
			if(!StringUtils.isNull(registerReleaseStarttime) 
					&& !StringUtils.isNull(registerReleaseEndtime)
						&& !StringUtils.isNull(registerReleaseVoucher)){
				
				Date startTime = DateUtil.parseStrToDate(registerReleaseStarttime, "yyyy-MM-dd HH:mm:ss");
				Date endTime = DateUtil.parseStrToDate(registerReleaseEndtime, "yyyy-MM-dd HH:mm:ss");
				Date start = DateUtil.parseStrToDate(registerStarttime, "yyyy-MM-dd HH:mm:ss");
				Date end = DateUtil.parseStrToDate(registerEndtime, "yyyy-MM-dd HH:mm:ss");
				Date nowTime = new Date();


				if(start.before(nowTime) && end.after(nowTime)){
					String[] voucherIds = registerVoucher.split(",");
					for (String vid : voucherIds) {
						VoucherProductVO voucherProductVO = this.getVoucherProductById(Long.valueOf(vid));
						if(null != voucherProductVO){
							LOGGER.info("注册发放财富券，执行发放：productId：" + voucherProductVO.getVoucherProductId() + ", userId:" + userId);
							this.sendVourcherOnOct(voucherProductVO.getVoucherProductId(), userId, VoucherConstants.SourceType.PLATFORM_AWARD.getValue(), VoucherConstants.SourceType.PLATFORM_AWARD.getDesc());
						}
					}
					this.sendVoucherOnOct(userId, "【财富派】亲爱的财富派用户：" +"恭喜您获得88元财富券大礼包，可于账户中心-优惠券中查看，记得在有效期内使用哦。详情请登录官网或APP。");
					LOGGER.info("注册发放财富券【结束】，userId:" + userId);
					return;
				}

				if(startTime.before(nowTime) && endTime.after(nowTime)){
					
					LOGGER.info("注册发放财富券，时间验证通过，准备发放");
					
					String[] voucherIds = registerReleaseVoucher.split(",");
					for (String vid : voucherIds) {
						VoucherProductVO voucherProductVO = this.getVoucherProductById(Long.valueOf(vid));
						if(null != voucherProductVO){
							LOGGER.info("注册发放财富券，执行发放：productId：" + voucherProductVO.getVoucherProductId() + ", userId:" + userId);
							this.handOut(voucherProductVO.getVoucherProductId(), userId, VoucherConstants.SourceType.PLATFORM_AWARD.getValue(), VoucherConstants.SourceType.PLATFORM_AWARD.getDesc());
						}
					}
					LOGGER.info("注册发放财富券【结束】，userId:" + userId);
					return;
				}

			}
			

			
		} catch (Exception e) {
			LOGGER.error("注册发放财富券出现错误：" + e.getMessage());
		}
		
	}
	
	@Override
	public void AuditRelease(Long userId) {
		
		LOGGER.info("实名发放财富券【开始】，userId:" + userId);
		
		try {
			
			LOGGER.info("实名发放财富券，读取配置信息: startTime：" + auditStarttime + ", auditEndtime:" + registerReleaseEndtime + ", vids:" + auditVoucher);
			
			if(!StringUtils.isNull(auditStarttime) 
					&& !StringUtils.isNull(auditEndtime)
						&& !StringUtils.isNull(auditVoucher)){
				
				Date startTime = DateUtil.parseStrToDate(auditStarttime, "yyyy-MM-dd HH:mm:ss");
				Date endTime = DateUtil.parseStrToDate(auditEndtime, "yyyy-MM-dd HH:mm:ss");
				Date nowTime = new Date();

				if(startTime.before(nowTime) && endTime.after(nowTime)){
					
					LOGGER.info("实名发放财富券，时间验证通过，准备发放");
					
					String[] voucherIds = auditVoucher.split(",");
					for (String vid : voucherIds) {
						VoucherProductVO voucherProductVO = this.getVoucherProductById(Long.valueOf(vid));
						if(null != voucherProductVO){
							LOGGER.info("实名发放财富券，执行发放：productId：" + voucherProductVO.getVoucherProductId() + ", userId:" + userId);
							this.handOut(voucherProductVO.getVoucherProductId(), userId, VoucherConstants.SourceType.PLATFORM_AWARD.getValue(), VoucherConstants.SourceType.PLATFORM_AWARD.getDesc());
						}
					}
					LOGGER.info("实名发放财富券【结束】，userId:" + userId);
					return;
				}

			}
			

			
		} catch (Exception e) {
			LOGGER.error("实名发放财富券出现错误：" + e.getMessage());
		}
		
	}
}
