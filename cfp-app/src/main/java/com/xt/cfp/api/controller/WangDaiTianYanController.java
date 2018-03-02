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
import com.xt.cfp.api.vo.WdtyBorrowListVO;
import com.xt.cfp.api.vo.WdtySubscribesVO;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum;
import com.xt.cfp.core.constants.DisplayEnum;
import com.xt.cfp.core.constants.LendOrderBidStatusEnum;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.ext.LenderRecordVO;
import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;
import com.xt.cfp.core.service.ApiService;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.Pagination;

/**
 * 网贷天眼，开放接口
 */
@Controller
@RequestMapping(value = "/wdty")
public class WangDaiTianYanController extends BaseController{
	
	private static Logger logger = Logger.getLogger(WangDaiTianYanController.class);
	
	@Autowired
    private RedisCacheManger redisCacheManger;
	
	@Autowired
	private ApiService apiService;
	
	/**
	 * 登录
	 * @param request
	 * @param username 用户名
	 * @param password 密码
	 * @return 密钥token值
	 */
	@RequestMapping(value = "/login")
    @ResponseBody
    public Object login(HttpServletRequest request,
    		@RequestParam(value = "username", required = false) String username,
    		@RequestParam(value = "password", required = false) String password) {
        try {
        	if(null == username || "".equals(username)){
        		return returnWdtyLoginResult(false, "用户名不能为空");
        	}
        	if(null == password || "".equals(password)){
        		return returnWdtyLoginResult(false, "密码不能为空");
        	}
        	
        	//读取api json文件
			String path = request.getSession().getServletContext().getRealPath("/") + "resources" + "/json/api.json";
			JSONObject jsonObject = this.getJSONObjectByJson(path);
			String wdtylogin = "";
			String wdtypass = "";
			Integer wdtyTokenTime = 3600;//默认时间（秒）
			if(null != jsonObject){
				wdtylogin = jsonObject.getString("wdtylogin");
				wdtypass = jsonObject.getString("wdtypass");
			}
			if(null != jsonObject && jsonObject.getIntValue("wdtyTokenTime") > 0){
				wdtyTokenTime = jsonObject.getIntValue("wdtyTokenTime");
			}
			
			//验证
			if(!wdtylogin.equals(username) || !wdtypass.equals(password)){
				return returnWdtyLoginResult(false, "用户名或密码错误");
			}
			logger.info("登录wdtyName:" + username);
			
        	//存储redis
			String wdtyToken = UUID.randomUUID().toString();
        	logger.info("登录wdtyToken:" + wdtyToken);
        	redisCacheManger.setRedisCacheInfo("wdtyToken_" + wdtylogin, wdtyToken, wdtyTokenTime);
            
        	//返回数据
        	Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("token", wdtyToken);
            return returnWdtyLoginResult(true, resultMap);
		} catch (SystemException se){
            logger.error(se.getMessage(),se);
            return returnWdtyLoginResult(false, se.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            return returnWdtyLoginResult(false, e.getMessage());
        }
    }
	
	/**
	 * 借款数据
	 * @param request
	 * @param status 标的状态:0.正在投标中的借款标;1.已完成(包括还款中和已完成的借款标).
	 * @param time_from 起始时间如:2014-05-09 06:10:00,状态为1是对应平台满标字段的值检索,状态为0就以平台发标时间字段检索.
	 * @param time_to 截止时间如:2014-05-09 06:10:00,状态为1是对应平台满标字段的值检索,状态为0就以平台发标时间字段检索.
	 * @param page_size 每页记录条数.
	 * @param page_index 请求的页码.
	 * @param token 请求 token 链接平台返回的秘钥或签名.
	 * @return json数据
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(HttpServletRequest request,
			@RequestParam(value = "status", defaultValue = "0") int status,
			@RequestParam(value = "time_from", defaultValue = "") String time_from,
			@RequestParam(value = "time_to", defaultValue = "") String time_to,
			@RequestParam(value = "page_size", defaultValue = "20") int page_size, 
			@RequestParam(value = "page_index", defaultValue = "1") int page_index,
			@RequestParam(value = "token", defaultValue = "") String token) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//token验证【开始】
			String path = request.getSession().getServletContext().getRealPath("/") + "resources" + "/json/api.json";
			JSONObject jsonObject = this.getJSONObjectByJson(path);
			String wdzjlogin = "";
			String wdzjIsLogin = "false";
			if(null != jsonObject){
				wdzjlogin = jsonObject.getString("wdtylogin");
				wdzjIsLogin = jsonObject.getString("wdtyIsLogin");
			}
			if("true".equals(wdzjIsLogin)){//true验证
				String wdzjToken = redisCacheManger.getRedisCacheInfo("wdtyToken_" + wdzjlogin);
				if(null == wdzjToken){
					resultMap.put("result_code", "-1");//返回码,数字1标识成功,否则是请求失败.
					resultMap.put("result_msg", "token已失效，请重新登录");//返回消息,返回的错误或成功的文本消息.
					return returnSuccessMap(resultMap);
				}else {
					if(!wdzjToken.equals(token)){
						resultMap.put("result_code", "-1");//返回码,数字1标识成功,否则是请求失败.
						resultMap.put("result_msg", "token值失效，请重新登录");//返回消息,返回的错误或成功的文本消息.
						return returnSuccessMap(resultMap);
					}
				}
			}
			//token验证【结束】
			
			LoanApplicationListVO loanApplicationListVO = new LoanApplicationListVO();
			loanApplicationListVO.setTimeStatus(String.valueOf(status));
			if(null != time_from && !"".equals(time_from)){
				loanApplicationListVO.setTimeFrom(DateUtil.parseStrToDate(time_from,"yyyy-MM-dd HH:mm:ss"));//起始时间
			}
			if(null != time_to && !"".equals(time_to)){
				loanApplicationListVO.setTimeTo(DateUtil.parseStrToDate(time_to,"yyyy-MM-dd HH:mm:ss"));//截止时间
			}
			Map<String, Object> customParams = new HashMap<String, Object>();
			customParams.put("isNewUserLoan", true);//是否加载新手标
			
			Pagination<LoanApplicationListVO> pagination = apiService.getLoanApplicationPaging(page_index, page_size, loanApplicationListVO, customParams);
			List<LoanApplicationListVO> loanList = pagination.getRows();

			List<WdtyBorrowListVO> datas = new ArrayList<WdtyBorrowListVO>();
			
			if (null != loanList && loanList.size() > 0) {
				WdtyBorrowListVO borrowVO = null;
				for (LoanApplicationListVO loan : loanList) {
					borrowVO = new WdtyBorrowListVO();
					
					//标编号,标的唯一编号(不为空,很重要).
					borrowVO.setId(String.valueOf(loan.getLoanApplicationId()));
					
					//平台名称,平台中文名称.
					borrowVO.setPlatform_name("财富派");
					
					//标的链接,借款标的URL链接.
					borrowVO.setUrl("https://www.caifupad.com/finance/bidding?loanApplicationNo=" + loan.getLoanApplicationId());
					
					//标题,标的标题信息.
					borrowVO.setTitle(loan.getLoanApplicationTitle());
					
					//用户名,借款人(发标人)的用户名称,如果没有借款人用户名,一定要返回下面的 userid,尽量不为空.
					borrowVO.setUsername(String.valueOf(loan.getUserId()));
					
					//用户编号,发标人的用户编号/ID.
					borrowVO.setUserid(String.valueOf(loan.getUserId()));
					
					//标的状态,0,正在投标中的借款标;1,已完成(包括还款中和已完成的借款标).
					if ("345".indexOf(loan.getApplicationState()) != -1) {
						// 发标中、放款审核中、待放款
						borrowVO.setStatus("0");
					} else {
						// 6、7、8 还款中、已结清、已结清(提前还贷)
						borrowVO.setStatus("1");
					}
					
					//借款类型
					//0 代表信用标,1 担保标;2 抵押,质押标, 3 秒标;
					//4 债权转让标(流转标,二级市场标的);5 理财计划(宝类业务_活期);
					//6 其它;7 净值标;8 活动标(体验标).9 理财计划(宝类业务_定期).
					//3，4，5标类型不参与贷款余额计算；请注意5【理财计划(宝类业务_活期)】和【9理财计划(宝类业务_定期)】的区分；4债权转让标指的是不会产生新待还的转让，如果会产生新待还，请返回其他标类型.
					if("0".equals(loan.getLoanType())){
						borrowVO.setC_type("0");
					}else {
						borrowVO.setC_type("2");
					}
					
					//借款金额,以元为单位,精度2位(1000.00),如万元请转换为元,请过滤掉借款金额小于50块的标.
					borrowVO.setAmount(String.valueOf(loan.getConfirmBalance().divide(new BigDecimal(1),2,BigDecimal.ROUND_HALF_UP)));
					
					//借款年利率,如果为月利率或天利率,统一转换为年利率并使用小数表示;精度4位,如:0.0910.
					borrowVO.setRate(String.valueOf(loan.getAnnualRate().divide(new BigDecimal(100),4,BigDecimal.ROUND_HALF_UP)));
					
					//借款期限,借款期限的数字。如3月这里只返回3若借款标的为流转标,对应的要有流转期限.
					borrowVO.setPeriod(String.valueOf(loan.getCycleCount()));
					
					//期限类型,0 代表天,1 代表月.
					borrowVO.setP_type("1");
					
					//还款方式
					//0 代表其他;1 按月等额本息还款;2按月付息,到期还本;
					//3 按天计息,一次性还本付息;4,按月计息,一次性还本付息;
					//5 按季分期还款;6 为等额本金,按月还本金;7 先息期本.
//					if(null != loan.getRepayMethod()){
//						if("1".equals(loan.getRepayMethod())){//【财富派：1等额本金，2等额本息，3周期付息到期还款】
//							borrowVO.setPay_way("6");
//						}else if ("2".equals(loan.getRepayMethod())) {
//							borrowVO.setPay_way("1");
//						}else if ("3".equals(loan.getRepayMethod())) {
//							borrowVO.setPay_way("2");
//						}
//					}
					
					//以借款产品表LOAN_PRODUCT枚举为准
					if("1".equals(loan.getRepayMethod())){//1等额本息
						borrowVO.setPay_way("1");
					}else if ("2".equals(loan.getRepayMethod())) {//2为周期还利息,到期还本金
						borrowVO.setPay_way("2");
					}else if ("3".equals(loan.getRepayMethod())) {//3为周期还本金,到期还利息
						borrowVO.setPay_way("0");
					}
					
					//完成百分比,转换成小数表示.
					borrowVO.setProcess(new BigDecimal(loan.getRatePercent()).divide(new BigDecimal(100))+"");
					
					//投标奖励,如奖励为百分比,使用小数表示.
					if(null == loan.getRewardsPercent()){
						borrowVO.setReward("0");
					}else {
						borrowVO.setReward(String.valueOf(loan.getRewardsPercent().divide(new BigDecimal(100))));
					}
					
					//担保奖励,如奖励为百分比,使用小数表示.
					borrowVO.setGuarantee("0");
					
					//标的创建时间,格式如:2013-08-10 14:24:01(24小时制).
					borrowVO.setStart_time(DateUtil.getPlusTime(loan.getPublishTime()));
					
					//满标时间(最后一笔投标时间),格式如:2013-08-10 13:10:00我们要的投资记录最后一笔的时间,请不要理解为标最后的的还款完成日期.
					if(null != loan.getFullTime()){
						borrowVO.setEnd_time(DateUtil.getPlusTime(loan.getFullTime()));
					}else {
						borrowVO.setEnd_time("");
					}
					
					//投资次数,这笔借款标有多少个投标记录.
					Integer invest_num = 0;
					if ("345".indexOf(loan.getApplicationState()) != -1) {
						// 发标中、放款审核中、待放款
						// 查询出借订单明细表中 投标中 的状态数据
						invest_num = apiService.findLendOrderDetailCount(loan.getLoanApplicationId(), LendOrderBidStatusEnum.BIDING);
					} else {
						// 6、7、8 还款中、已结清、已结清(提前还贷)
						// 到债权表查询 已生效，还款中 状态的数据
//						invest_num = lendOrderBidDetailService.findLendOrderDetailCount(loan.getLoanApplicationId(), LendOrderBidStatusEnum.BIDSUCCESS);
						invest_num = apiService.findLendOrderDetailCount(loan.getLoanApplicationId(),
								CreditorRightsStateEnum.EFFECTIVE, CreditorRightsStateEnum.COMPLETE, CreditorRightsStateEnum.EARLYCOMPLETE);

					}
					borrowVO.setInvest_num(String.valueOf(invest_num));
					
					//续投奖励,继续投标的奖励.
					borrowVO.setC_reward("0");

					datas.add(borrowVO);
				}
			}

			resultMap.put("result_code", "1");//返回码,数字1标识成功,否则是请求失败.
			resultMap.put("result_msg", "获取数据成功");//返回消息,返回的错误或成功的文本消息.
			resultMap.put("page_count", pagination.getTotalPage());//总页数
			resultMap.put("page_index", pagination.getCurrentPage());//当前请求的页数.
			resultMap.put("loans", datas);//数据
			return returnSuccessMap(resultMap);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
        	resultMap.put("result_code", "-1");//返回码,数字1标识成功,否则是请求失败.
			resultMap.put("result_msg", se.getMessage());//返回消息,返回的错误或成功的文本消息.
			return returnSuccessMap(resultMap);
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
        	resultMap.put("result_code", "-1");//返回码,数字1标识成功,否则是请求失败.
			resultMap.put("result_msg", e.getMessage());//返回消息,返回的错误或成功的文本消息.
			return returnSuccessMap(resultMap);
        }
	}
	
	/**
	 * 投资数据
	 * @param request
	 * @param id 标的编号.
	 * @param page_size 每页记录条数.
	 * @param page_index 请求的页码.
	 * @param token 请求 token 链接平台返回的秘钥或签名.
	 * @return
	 */
	@RequestMapping(value = "/sublist")
	@ResponseBody
	public Object sublist(HttpServletRequest request,
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "page_size", defaultValue = "20") int page_size,
			@RequestParam(value = "page_index", defaultValue = "1") int page_index,
			@RequestParam(value = "token", defaultValue = "") String token) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//token验证【开始】
			String path = request.getSession().getServletContext().getRealPath("/") + "resources" + "/json/api.json";
			JSONObject jsonObject = this.getJSONObjectByJson(path);
			String wdzjlogin = "";
			String wdzjIsLogin = "false";
			if(null != jsonObject){
				wdzjlogin = jsonObject.getString("wdtylogin");
				wdzjIsLogin = jsonObject.getString("wdtyIsLogin");
			}
			if("true".equals(wdzjIsLogin)){//true验证
				String wdzjToken = redisCacheManger.getRedisCacheInfo("wdtyToken_" + wdzjlogin);
				if(null == wdzjToken){
					resultMap.put("result_code", "-1");//返回码,数字1标识成功,否则是请求失败.
					resultMap.put("result_msg", "token已失效，请重新登录");//返回消息,返回的错误或成功的文本消息.
					return returnSuccessMap(resultMap);
				}else {
					if(!wdzjToken.equals(token)){
						resultMap.put("result_code", "-1");//返回码,数字1标识成功,否则是请求失败.
						resultMap.put("result_msg", "token值失效，请重新登录");//返回消息,返回的错误或成功的文本消息.
						return returnSuccessMap(resultMap);
					}
				}
			}
			//token验证【结束】
			
			LoanApplication loan = apiService.findById(Long.valueOf(id));
			if(null == loan){
				resultMap.put("result_code", "-1");//返回码,数字1标识成功,否则是请求失败.
				resultMap.put("result_msg", "请求数据不存在");//返回消息,返回的错误或成功的文本消息.
				return returnSuccessMap(resultMap);
			}
			
			// 投资列表【开始】
			Pagination<LenderRecordVO> lenderPagination = null;
			if ("345".indexOf(loan.getApplicationState()) != -1) {
				// 发标中、放款审核中、待放款
				// 查询出借订单明细表中 投标中 的状态数据
				lenderPagination = apiService.findLendOrderDetailPaging(page_index, page_size, loan.getLoanApplicationId(), LendOrderBidStatusEnum.BIDING, DisplayEnum.DISPLAY);
			} else {
				// 6、7、8 还款中、已结清、已结清(提前还贷)
				// 到债权表查询 已生效，还款中 状态的数据
//				lenderPagination = lendOrderBidDetailService.findLendOrderDetailPaging(page_index, page_size, loan.getLoanApplicationId(), LendOrderBidStatusEnum.BIDSUCCESS);
				lenderPagination = apiService.findLendOrderDetailPaging(page_index, page_size, page_index, page_size, loan.getLoanApplicationId(),
						CreditorRightsStateEnum.EFFECTIVE, CreditorRightsStateEnum.COMPLETE, CreditorRightsStateEnum.EARLYCOMPLETE);
				
			}
			List<LenderRecordVO> lendList = lenderPagination.getRows();
			List<WdtySubscribesVO> lendDatas = new ArrayList<WdtySubscribesVO>();
			if (null != lendList && lendList.size() > 0) {
				WdtySubscribesVO subVO = null;
				for (LenderRecordVO lend : lendList) {
					subVO = new WdtySubscribesVO();
					
					//标编号,标的唯一编号(不为空,很重要)
					subVO.setId(String.valueOf(loan.getLoanApplicationId()));
					
					//标的链接,URL链接.
					subVO.setLink("https://www.caifupad.com/finance/bidding?loanApplicationNo=" + loan.getLoanApplicationId());
					
					//用户所在地,投标人所在城市.
					subVO.setUseraddress("");
					
					//用户名,投标人的用户名称,登录账号,可辨识区分,可支持加密数据.
					subVO.setUsername(lend.getLenderName());
					
					//用户编号,投标人的用户编号/ID.
					subVO.setUserid(String.valueOf(lend.getUserId()));
					
					//投标方式,例如:手动、自动.
					subVO.setType("手动");
					
					//投标金额,投标金额实际生效部分(保留两位小数).
					subVO.setMoney(String.valueOf(lend.getLendAmount().divide(new BigDecimal(1),2,BigDecimal.ROUND_HALF_UP)));
					
					//有效金额,投标金额实际生效部分(保留两位小数),请过滤掉投资金额小于10块的记录. 
					subVO.setAccount(String.valueOf(lend.getLendAmount().divide(new BigDecimal(1),2,BigDecimal.ROUND_HALF_UP)));
					
					//投标状态,例如:成功、部分成功、失败.
					subVO.setStatus("成功");
					
					//投标时间,格式如:2014-03-13 16:44:26.
					subVO.setAdd_time(DateUtil.getPlusTime(lend.getLendTime()));
					
					lendDatas.add(subVO);
				}
			}
			//投资列表【结束】
			
			resultMap.put("result_code", "1");//返回码,数字1标识成功,否则是请求失败.
			resultMap.put("result_msg", "获取数据成功");//返回消息,返回的错误或成功的文本消息.
			resultMap.put("page_count", lenderPagination.getTotalPage());//总页数
			resultMap.put("page_index", lenderPagination.getCurrentPage());//当前请求的页数.
			resultMap.put("loans", lendDatas);//数据
			return returnSuccessMap(resultMap);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
        	resultMap.put("result_code", "-1");//返回码,数字1标识成功,否则是请求失败.
			resultMap.put("result_msg", se.getMessage());//返回消息,返回的错误或成功的文本消息.
			return returnSuccessMap(resultMap);
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
        	resultMap.put("result_code", "-1");//返回码,数字1标识成功,否则是请求失败.
			resultMap.put("result_msg", e.getMessage());//返回消息,返回的错误或成功的文本消息.
			return returnSuccessMap(resultMap);
        }
	}
	
}
