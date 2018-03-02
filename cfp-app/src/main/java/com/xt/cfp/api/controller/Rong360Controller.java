package com.xt.cfp.api.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.api.vo.WdzjBorrowListVO;
import com.xt.cfp.api.vo.WdzjSubscribesVO;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum;
import com.xt.cfp.core.constants.DisplayEnum;
import com.xt.cfp.core.constants.LendOrderBidStatusEnum;
import com.xt.cfp.core.pojo.ext.LenderRecordVO;
import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;
import com.xt.cfp.core.service.ApiService;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.Pagination;

/**
 * 融360，开放接口
 */
@Controller
@RequestMapping(value = "/r360")
public class Rong360Controller extends BaseController{
	
	private static Logger logger = Logger.getLogger(Rong360Controller.class);
	
	@Autowired
    private RedisCacheManger redisCacheManger;
	
	@Autowired
	private ApiService apiService;
	
	@RequestMapping(value = "/login")
    @ResponseBody
    public Object login(HttpServletRequest request,
    		@RequestParam(value = "userName", required = false) String username,
    		@RequestParam(value = "password", required = false) String password) {
        try {
        	if(null == username || "".equals(username)){
        		return returnR360ResultMap(false, null, "check", "用户名不能为空");
        	}
        	if(null == password || "".equals(password)){
        		return returnR360ResultMap(false, null, "check", "密码不能为空");
        	}
        	
        	//读取api json文件
			String path = request.getSession().getServletContext().getRealPath("/") + "resources" + "/json/api.json";
			JSONObject jsonObject = this.getJSONObjectByJson(path);
			String r360login = "";
			String r360pass = "";
			Integer r360TokenTime = 3600;//默认时间（秒）
			if(null != jsonObject){
				r360login = jsonObject.getString("r360login");
				r360pass = jsonObject.getString("r360pass");
			}
			if(null != jsonObject && jsonObject.getIntValue("r360TokenTime") > 0){
				r360TokenTime = jsonObject.getIntValue("r360TokenTime");
			}
			
			//验证
			if(!r360login.equals(username) || !r360pass.equals(password)){
				return returnR360ResultMap(false, null, "check", "用户名或密码错误");
			}
			logger.info("登录r360Name:" + username);
			
        	//存储redis
			String r360Token = UUID.randomUUID().toString();
        	logger.info("登录r360Token:" + r360Token);
        	redisCacheManger.setRedisCacheInfo("r360Token_" + r360login, r360Token, r360TokenTime);
            
        	//返回数据
        	Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("token", r360Token);
            return returnR360ResultMap(true, resultMap, null, null);
		} catch (SystemException se){
            logger.error(se.getMessage(),se);
            return returnR360ResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            return returnR360ResultMap(false, null, "exception", e.getMessage());
        }
    }
	
	/**
	 * 出借列表
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(HttpServletRequest request,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize, 
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "date", defaultValue = "") String date,
			@RequestParam(value = "token", defaultValue = "") String token) {
		try {
			//token验证【开始】
			String path = request.getSession().getServletContext().getRealPath("/") + "resources" + "/json/api.json";
			JSONObject jsonObject = this.getJSONObjectByJson(path);
			String r360login = "";
			String r360IsLogin = "false";
			if(null != jsonObject){
				r360login = jsonObject.getString("r360login");
				r360IsLogin = jsonObject.getString("r360IsLogin");
			}
			if("true".equals(r360IsLogin)){//true验证
				String r360Token = redisCacheManger.getRedisCacheInfo("r360Token_" + r360login);
				if(null == r360Token){
					return returnResultMap(false, null, "check", "token已失效，请重新登录");
				}else {
					if(!r360Token.equals(token)){
						return returnResultMap(false, null, "check", "token值失效，请重新登录");
					}
				}
			}
			//token验证【结束】
			
			LoanApplicationListVO loanApplicationListVO = new LoanApplicationListVO();
			if(null != date && !"".equals(date)){
				loanApplicationListVO.setFullTime(DateUtil.parseStrToDate(date,"yyyy-MM-dd"));//查询条件，满标时间
			}
			Map<String, Object> customParams = new HashMap<String, Object>();
			customParams.put("isNewUserLoan", true);//是否加载新手标
			
			Pagination<LoanApplicationListVO> pagination = apiService.getLoanApplicationPaging(pageNo, pageSize, loanApplicationListVO, customParams);
			List<LoanApplicationListVO> loanList = pagination.getRows();

			List<WdzjBorrowListVO> datas = new ArrayList<WdzjBorrowListVO>();
			BigDecimal totalAmount = new BigDecimal(0);//借款总额
			
			if (null != loanList && loanList.size() > 0) {
				WdzjBorrowListVO borrowVO = null;
				for (LoanApplicationListVO loan : loanList) {
					borrowVO = new WdzjBorrowListVO();

					//项目主键(唯一)【必】
					borrowVO.setProjectId(String.valueOf(loan.getLoanApplicationId()));
					//借款标题【必】
					borrowVO.setTitle(loan.getLoanApplicationTitle());
					//借款金额【必】
					borrowVO.setAmount(loan.getConfirmBalance());
					totalAmount = totalAmount.add(loan.getConfirmBalance());//累加借款总额
					//进度【必】
					borrowVO.setSchedule(loan.getRatePercent());
					//利率【必】
					borrowVO.setInterestRate(String.valueOf(loan.getAnnualRate())+"%");
					//借款期限【必】
					borrowVO.setDeadline(loan.getCycleCount());
					//期限单位【必】
					borrowVO.setDeadlineUnit("月");
					//奖励【必】
					borrowVO.setReward(loan.getRewardsPercent());
					//标类型【必】
					if("0".equals(loan.getLoanType())){
						borrowVO.setType("信用标");
					}else {
						borrowVO.setType("抵押标");
					}
					//还款方式【必】【1等额本金，2等额本息，3周期付息到期还款】
					if(null != loan.getRepayMethod()){
						if("1".equals(loan.getRepayMethod())){
							borrowVO.setRepaymentType(6);
						}else if ("2".equals(loan.getRepayMethod())) {
							borrowVO.setRepaymentType(2);
						}else if ("3".equals(loan.getRepayMethod())) {
							borrowVO.setRepaymentType(5);
						}
					}
					//标所属平台频道板块【不必】
					borrowVO.setPlateType("我要理财");
					//保障担保机构名称【不必】
					borrowVO.setGuarantorsType("");
					//借款人所在省份【不必】
					borrowVO.setProvince("");
					//借款人所在城市【不必】
					borrowVO.setCity("");
					//借款人ID【必】
					borrowVO.setUserName(String.valueOf(loan.getUserId()));
					//发标人头像的URL【不必】
					borrowVO.setUserAvatarUrl("");
					//借款用途【不必】
					borrowVO.setAmountUsedDesc(loan.getLoanUseageDesc());
					//营收【不必】
					borrowVO.setRevenue(new BigDecimal("0"));
					//标的详细页面地址链接【必】
					borrowVO.setLoanUrl("https://www.caifupad.com/finance/bidding?loanApplicationNo=" + loan.getLoanApplicationId());
					//满标时间【必】
					borrowVO.setSuccessTime(DateUtil.getPlusTime(loan.getFullTime()));
					//发标时间【不必】
					borrowVO.setPublishTime(DateUtil.getPlusTime(loan.getPublishTime()));
					//-----------------------------------
					
					// 投资列表【开始】
					Pagination<LenderRecordVO> lenderPagination = null;
					if ("345".indexOf(loan.getApplicationState()) != -1) {
						// 发标中、放款审核中、待放款
						// 查询出借订单明细表中 投标中 的状态数据
						lenderPagination = apiService.findLendOrderDetailPaging(1, 9999, loan.getLoanApplicationId(), LendOrderBidStatusEnum.BIDING, DisplayEnum.DISPLAY);
					} else {
						// 6、7、8 还款中、已结清、已结清(提前还贷)
						// 到债权表查询 已生效，还款中 状态的数据
//						lenderPagination = lendOrderBidDetailService.findLendOrderDetailPaging(1, 9999, loan.getLoanApplicationId(), LendOrderBidStatusEnum.BIDSUCCESS);
						lenderPagination = apiService.findLendOrderDetailPaging(1, 9999, 1, 9999, loan.getLoanApplicationId(),
								CreditorRightsStateEnum.EFFECTIVE, CreditorRightsStateEnum.COMPLETE, CreditorRightsStateEnum.EARLYCOMPLETE);
					}
					List<LenderRecordVO> lendList = lenderPagination.getRows();
					List<WdzjSubscribesVO> lendDatas = new ArrayList<WdzjSubscribesVO>();
					if (null != lendList && lendList.size() > 0) {
						WdzjSubscribesVO subVO = null;
						for (LenderRecordVO lend : lendList) {
							subVO = new WdzjSubscribesVO();
							
							//投标人ID【必】
							subVO.setSubscribeUserName(String.valueOf(lend.getUserId()));
							//投标金额【必】
							subVO.setAmount(lend.getLendAmount());
							//有效金额【必】
							subVO.setValidAmount(lend.getLendAmount());
							//投标时间【必】
							subVO.setAddDate(DateUtil.getPlusTime(lend.getLendTime()));
							//投标状态【必】
							subVO.setStatus(1);
							//标识手动或自动投标【必】
							subVO.setType(0);
							//投标来源【不必】
							subVO.setSourceType(0);
							
							lendDatas.add(subVO);
						}
					}
					borrowVO.setSubscribes(lendDatas);
					//投资列表【结束】
					datas.add(borrowVO);
				}
			}

			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("totalPage", pagination.getTotalPage());//总页数
			resultMap.put("currentPage", pagination.getCurrentPage());//当前页数
			resultMap.put("totalCount", pagination.getTotal());//总标数
			resultMap.put("totalAmount", totalAmount);//当天借款标总额
			resultMap.put("borrowList", datas);//借款标信息
			return returnSuccessMap(resultMap);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
	}
}
