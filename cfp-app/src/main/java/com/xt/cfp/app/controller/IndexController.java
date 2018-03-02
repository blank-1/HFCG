package com.xt.cfp.app.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.app.annotation.DoNotNeedLogin;
import com.xt.cfp.app.vo.AppBannerVO;
import com.xt.cfp.app.vo.CreditorRightsExtVO;
import com.xt.cfp.app.vo.ShareInfo;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.constants.ChannelTypeEnum;
import com.xt.cfp.core.constants.CreditorRightsFromWhereEnum;
import com.xt.cfp.core.constants.LendOrderBidStatusEnum;
import com.xt.cfp.core.constants.LoanApplicationStateEnum;
import com.xt.cfp.core.constants.UserSource;
import com.xt.cfp.core.pojo.AppBanner;
import com.xt.cfp.core.pojo.AppFeedBack;
import com.xt.cfp.core.pojo.AppStartPage;
import com.xt.cfp.core.pojo.RightsRepaymentDetail;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.ext.CreditorRightsExtVo;
import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;
import com.xt.cfp.core.service.AppFeedBackService;
import com.xt.cfp.core.service.AppStartPageService;
import com.xt.cfp.core.service.CreditorRightsService;
import com.xt.cfp.core.service.LendOrderBidDetailService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.StringUtils;

@Controller
public class IndexController extends BaseController {
	private static Logger logger = Logger.getLogger(IndexController.class);
	@Autowired
	LoanApplicationService loanApplicationService;
	
	@Autowired
	LendOrderService lendOrderService;
	
	@Autowired
	AppFeedBackService appFeedBackService;
	
	@Autowired
	LendOrderBidDetailService lendOrderBidDetailService;
	
	@Autowired
	private AppStartPageService appStartPageService;
	
	@Autowired
	private CreditorRightsService creditorRightsService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	/**
	 * 跳转到：首页
	 */
	@DoNotNeedLogin
    @RequestMapping(value = "/")
    public ModelAndView index(HttpServletRequest request) {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }
	
	/**
	 * 【APP接口】
	 * 首页推荐标信息
	 * @param request
	 * @return
	 */
	@DoNotNeedLogin
    @RequestMapping(value = "/indexLoanApplication", method = RequestMethod.POST)//[APP]
	@ResponseBody
    public Object indexLoanApplication(HttpServletRequest request) {
		try {
			//规则：首页推荐标，应推荐可购买标中已认购人数最多的标；如果没有可购买标，则推荐全部列表中的第一个标.
			
			Integer searchCount = 10;//默认条数
			//读取config json文件
			String path = request.getSession().getServletContext().getRealPath("/") + "resources" + "/json/config.json";
			JSONObject jsonObject = this.getJSONObjectByJson(path);
			if(null != jsonObject && jsonObject.getIntValue("searchCount") > 0){
				searchCount = jsonObject.getIntValue("searchCount");
			}
			
			//变量设置
			DecimalFormat df = new DecimalFormat("#,##0.00");
			LoanApplicationListVO loanVO = null;//最终返回数据
			Integer lendCount = 0;//最大借款人数
			
			//获取数据
	    	Pagination<LoanApplicationListVO> loanApplicationList = loanApplicationService.getLoanApplicationPaging(1, searchCount, null, null);
	    	if(null != loanApplicationList){
	    		List<LoanApplicationListVO> loanApplicationListVOs = loanApplicationList.getRows();
	    		if(null != loanApplicationListVOs && loanApplicationListVOs.size() > 0){
	    			for (LoanApplicationListVO vo : loanApplicationListVOs) {
	    				
	    				// 如果类型为投标中的
	    				if(LoanApplicationStateEnum.BIDING.getValue().equals(vo.getApplicationState())){
	    					Integer count = lendOrderBidDetailService.findLendOrderDetailCount(vo.getLoanApplicationId(), LendOrderBidStatusEnum.BIDING);
	    	    			if(count > lendCount){
	    	    				lendCount = count;
	    	    				loanVO = vo;
	    	    			}
	    				}
	    				
					}
	    			// 如果没有类型为投标中的，获取第一个标
	    			if(null == loanVO){
	    				loanVO = loanApplicationListVOs.get(0);
	    			}
	    		}
	    	}
	    	
	    	//最终返回的数据
	    	Map<String,Object> resultMap = new HashMap<String,Object>();
	    	if(null != loanVO){
	    		resultMap.put("loanApplicationId", loanVO.getLoanApplicationId());//ID
				resultMap.put("loanApplicationTitle", loanVO.getLoanApplicationTitle());//借款标题
				resultMap.put("confirmBalance", df.format(loanVO.getConfirmBalance()));//借款金额
				resultMap.put("annualRate", loanVO.getAnnualRate());//年化利率
				resultMap.put("rewardsPercent", loanVO.getRewardsPercent());//奖励利率
				resultMap.put("cycleCount", loanVO.getCycleCount());//借款期限
				resultMap.put("startAmount", "100");//起投金额
				resultMap.put("creditLevel", loanVO.getCreditLevel());//信用等级
				resultMap.put("ratePercent", loanVO.getRatePercent());//借款进度
				resultMap.put("remain", df.format(loanVO.getRemain()));//剩余金额
				resultMap.put("loanType", loanVO.getLoanType());//借款类型
	    	}
	    	
			return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }
	
	/**
	 * 【APP接口】
	 * 首页出借、收益金额信息
	 * @param request
	 * @return
	 */
	@DoNotNeedLogin
    @RequestMapping(value = "/indexInfo", method = RequestMethod.POST)//[APP]
	@ResponseBody
    public Object indexInfo(HttpServletRequest request) {
		try {
			//获取当前用户登录
			UserInfo currentUser = getCurrentUser(request);
			DecimalFormat df = new DecimalFormat("#,##0.00");
			BigDecimal allBuyBalance = BigDecimal.ZERO;// 累计出借金额
			BigDecimal allProfit = BigDecimal.ZERO;// 累计收益
			if (currentUser != null) {
				allBuyBalance = lendOrderService.getAllBuyBalance(currentUser.getUserId());
				allProfit = lendOrderService.getAllProfit(currentUser.getUserId());
				BigDecimal totalAward = userAccountService.getUserTotalAward(currentUser.getUserId());//用户累计奖励
				allProfit = allProfit.add(totalAward);//将累计奖励，添加到累计收益中
			} else {
				allBuyBalance = lendOrderService.getAllBuyBalance(null);
				allProfit = lendOrderService.getAllProfit(null);
			}
			
			//返回结果
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("isLogined", currentUser == null ? "false" : "true");
			resultMap.put("allBuyBalance", df.format(allBuyBalance));
			resultMap.put("allProfit", df.format(allProfit));
			return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }
	
	/**
	 * 【APP接口】
	 * 首页banner图
	 * @param request
	 * @return
	 */
	@DoNotNeedLogin
    @RequestMapping(value = "/banner", method = RequestMethod.POST)//[APP]
	@ResponseBody
    public Object banner(HttpServletRequest request) {
		try {
			//读取banner json文件
			String path = request.getSession().getServletContext().getRealPath("/") + "resources" + "/json/banner.json";
			JSONObject jsonObject = this.getJSONObjectByJson(path);
	        
			//返回结果
			return returnResultMap(true, jsonObject, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }
	
	/**
	 * 【APP接口】
	 * 首页banner图，查询数据库
	 * @param request
	 * @return
	 */
	@DoNotNeedLogin
    @RequestMapping(value = "/indexBanner", method = RequestMethod.POST)//[APP]
	@ResponseBody
    public Object indexBanner(HttpServletRequest request,
    		@RequestParam(value = "source", required = false) String source) {
		try {
			// 合法性验证
			if(null == source || "".equals(source)){
				source = "2";//为了兼容旧版本，添加默认值
				//return returnResultMap(false, null, "check", "请求来源不能为空");
			}else if (!UserSource.ISO.getValue().equals(source) && !UserSource.ANDROID.getValue().equals(source)) {
				return returnResultMap(false, null, "check", "请求来源参数异常");
			}
			
			// 查询数据
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("appType", source);//2ios;3android
			params.put("state", "1");//0禁用；1启用
			List<AppBanner> appBannerList  = appStartPageService.getAppBannerByList(params);
			
			// 精简处理
			List<AppBannerVO> appBannerVOList = new ArrayList<AppBannerVO>();
			AppBannerVO appBannerVO = null;
			ShareInfo shareInfo = null;
			if(null != appBannerList && appBannerList.size() > 0){
				for (AppBanner banner : appBannerList) {
					appBannerVO = new AppBannerVO();
					
					appBannerVO.setShow("true");//兼容旧版本，固定值“true”
					appBannerVO.setImageSrc(this.getWWW_BASEPATH(request) + banner.getImageSrc());//banner图片
					appBannerVO.setHttpUrl(banner.getHttpUrl());//banner跳转地
					
					shareInfo = new ShareInfo();
					shareInfo.setOrderBy(banner.getOrderBy());//banner顺序
					shareInfo.setBannerName(banner.getBannerName());//活动名
					shareInfo.setImgUrl(this.getWWW_BASEPATH(request) + banner.getImgUrl());//分享小图
					shareInfo.setHttpMethod(banner.getHttpMethod());//请求方
					shareInfo.setHttpIsToken(banner.getHttpIsToken());//是否传UserToke
					shareInfo.setTitle(banner.getTitle());//分享标题
					shareInfo.setDesc(banner.getDesc());//分享文案
					shareInfo.setLink(banner.getLink());//分享链接
					shareInfo.setShareCloseUrl(banner.getShareCloseUrl());//监控分享链接
					shareInfo.setCloseUrl(banner.getCloseUrl());//监控活动关闭链接
					shareInfo.setShareBackUrl(banner.getShareBackUrl());//分享结果回调地址
					appBannerVO.setShareInfo(shareInfo);
					
					appBannerVOList.add(appBannerVO);
					
				}
			}else {
				appBannerVO = new AppBannerVO();
				appBannerVO.setShow("false");//兼容旧版本，固定值“false”
				appBannerVOList.add(appBannerVO);
			}
			
			//返回结果
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("rows", appBannerVOList);
			return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }
	
	/**
	 * 【APP接口】
	 * 版本检测(ios用)
	 * @param request
	 * @return
	 */
	@DoNotNeedLogin
    @RequestMapping(value = "/version", method = RequestMethod.POST)//[APP]
	@ResponseBody
    public Object version(HttpServletRequest request) {
		try {
			//读取version json文件
			String path = request.getSession().getServletContext().getRealPath("/") + "resources" + "/json/iosVersion.json";
			JSONObject jsonObject = this.getJSONObjectByJson(path);
	        
			//返回结果
			return returnResultMap(true, jsonObject, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }
	
	/**
	 * 【APP接口】
	 * 版本检测(android用)
	 * @param request
	 * @return
	 */
	@DoNotNeedLogin
    @RequestMapping(value = "/androidVersion", method = RequestMethod.POST)//[APP]
	@ResponseBody
    public Object androidVersion(HttpServletRequest request) {
		try {
			//读取version json文件
			String path = request.getSession().getServletContext().getRealPath("/") + "resources" + "/json/androidVersion.json";
			JSONObject jsonObject = this.getJSONObjectByJson(path);
	        
			//返回结果
			return returnResultMap(true, jsonObject, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }
	
	/**
	 * 【APP接口】
	 * 发送反馈意见
	 * @param request
	 * @param contactInfo 联系方式
	 * @param feedbackInfo 反馈内容
	 * @param feedbackSource 反馈来源
	 * @param systemVersion 系统版本
	 * @param appVersion APP版本
	 * @return
	 */
	@RequestMapping(value = "/feedback", method = RequestMethod.POST)//[APP]
    @ResponseBody
    public Object feedback(HttpServletRequest request,
    		@RequestParam(value = "contactInfo", required = false) String contactInfo,
    		@RequestParam(value = "feedbackInfo", required = false) String feedbackInfo,
    		@RequestParam(value = "feedbackSource", required = false) String feedbackSource,
    		@RequestParam(value = "systemVersion", required = false) String systemVersion,
    		@RequestParam(value = "appVersion", required = false) String appVersion) {
    	try {
    		//登录验证
    		UserInfo currentUser = getCurrentUser(request);
    		if(null == currentUser){
    			return returnResultMap(false, null, "needlogin", "请先登录");
    		}
    		
//    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		
    		// 该用户请求次数验证
//    		AppFeedBack appFeedBackParam = new AppFeedBack();
//    		appFeedBackParam.setUserId(currentUser.getUserId());
//    		appFeedBackParam.setCreateTime(sdf.parse(sdf.format(new Date())));
//    		List<AppFeedBack> appFeedBackList = appFeedBackService.getAppFeedBackByUserIdAndCreateTime(appFeedBackParam);
//    		if(null != appFeedBackList && appFeedBackList.size() > 0){
//    			System.out.println("appFeedBackList.size():" + appFeedBackList.size());
//    		}
    		
    		//参数验证
    		if(null == contactInfo || "".equals(contactInfo)){
    			return returnResultMap(false, null, "check", "请填写联系方式");
    		}else if(!StringUtils.isPattern("(^1[3|4|5|7|8][0-9]{9}$)", contactInfo) 
    				&& !StringUtils.isPattern("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", contactInfo) ){
				return returnResultMap(false, null, "check", "请正确填写联系方式，手机或邮件");
			}
    		
    		if(null == feedbackInfo || "".equals(feedbackInfo)){
    			return returnResultMap(false, null, "check", "请填写反馈信息");
    		}else if(feedbackInfo.length() > 400){
    			return returnResultMap(false, null, "check", "反馈信息过长");
			}else if(isSpecialCharacters(feedbackInfo)){
				return returnResultMap(false, null, "check", "反馈信息含有非法字符，请修改");
			}
    		
    		if(null == feedbackSource || "".equals(feedbackSource)){
    			return returnResultMap(false, null, "check", "参数不合法");
    		}else if(!UserSource.ISO.getValue().equals(feedbackSource) && !UserSource.ANDROID.getValue().equals(feedbackSource)){
    			return returnResultMap(false, null, "check", "参数不合法");
			}
    		
    		if(null == systemVersion || "".equals(systemVersion)){
    			return returnResultMap(false, null, "check", "缺少参数");
    		}else if(systemVersion.length() > 50){
    			return returnResultMap(false, null, "check", "系统版本信息过长");
			}else if(isSpecialCharacters(systemVersion)){
				return returnResultMap(false, null, "check", "版本信息含有非法字符，请修改");
			}
    		
    		if(null == appVersion || "".equals(appVersion)){
    			return returnResultMap(false, null, "check", "缺少参数");
    		}else if(appVersion.length() > 50){
    			return returnResultMap(false, null, "check", "App版本信息过长");
			}else if(isSpecialCharacters(appVersion)){
				return returnResultMap(false, null, "check", "版本信息含有非法字符，请修改");
			}
    		
    		//参数填充
    		AppFeedBack appFeedBack = new AppFeedBack();
    		appFeedBack.setUserId(currentUser.getUserId());
    		appFeedBack.setContactInfo(contactInfo);
    		appFeedBack.setFeedbackInfo(feedbackInfo);
    		appFeedBack.setFeedbackSource(feedbackSource);
    		appFeedBack.setSystemVersion(systemVersion);
    		appFeedBack.setAppVersion(appVersion);
    		appFeedBack.setCreateTime(new Date());
    		
    		//保存操作
    		appFeedBackService.addAppFeedBack(appFeedBack);
    		
            return returnResultMap(true, null, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e) {
 			e.printStackTrace();
 			logger.error(e.getMessage(),e);
 			return returnResultMap(false, null, "exception", e.getMessage());
 		}
    }
	
	/**
	 * 【APP接口】
	 * 启动页接口
	 * @param request
	 * @return
	 */
	@DoNotNeedLogin
    @RequestMapping(value = "/appStartPage", method = RequestMethod.POST)//[APP]
	@ResponseBody
    public Object appStartPage(HttpServletRequest request,
    		@RequestParam(value = "appType", required = false) String appType) {
		try {
    		
    		if(null == appType || "".equals(appType)){
    			return returnResultMap(false, null, "check", "缺少参数");
    		}else if(!UserSource.ISO.getValue().equals(appType) && !UserSource.ANDROID.getValue().equals(appType)){
    			return returnResultMap(false, null, "check", "参数无效");
    		}
			
    		//获取数据
			AppStartPage appStartPage = appStartPageService.getByAppType(appType);
			
			//返回结果
			Map<String,Object> resultMap = new HashMap<String,Object>();
			if(null != appStartPage){
				resultMap.put("appType", appStartPage.getAppType());//APP类型
				resultMap.put("pageTitle", appStartPage.getPageTitle());//标题
				resultMap.put("picUrl", this.getWWW_BASEPATH(request) + appStartPage.getPicUrl());//图片请求路径
				resultMap.put("status", appStartPage.getStatus());//状态
			}else {
				resultMap.put("appType", "");
				resultMap.put("pageTitle", "");
				resultMap.put("picUrl", "");
				resultMap.put("status", "");
			}
			return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }
	
	/**
	 * 【APP接口】
	 * 首页汇总，已投债权信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/indexCreditRight", method = RequestMethod.POST)//[APP]
	@ResponseBody
	public Object indexCreditRight(HttpServletRequest request) {
		
		try {
			
			Integer searchCount = 10;//默认条数
			//读取config json文件
			String path = request.getSession().getServletContext().getRealPath("/") + "resources" + "/json/config.json";
			JSONObject jsonObject = this.getJSONObjectByJson(path);
			if(null != jsonObject && jsonObject.getIntValue("searchCount") > 0){
				searchCount = jsonObject.getIntValue("searchCount");
			}
			
			//变量设置
			DecimalFormat df = new DecimalFormat("#,##0.00");
			BigDecimal minSumBuyPrice = BigDecimal.ZERO;//回款中总金额
            int minCount = 0;//回款中个数
            int minDaysBetween = -1;//保存最小的距到期天数
            String title = "";//一个标的时候，保存标名称
			
			//登录验证
			UserInfo currentUser = getCurrentUser(request);
			if(null == currentUser){
//				return returnResultMap(false, null, "needlogin", "请先登录");
			}else{
				CreditorRightsExtVo vo = new CreditorRightsExtVo();
				vo.setLendUserId(currentUser.getUserId());
				vo.setFromWhere(CreditorRightsFromWhereEnum.BUY.value2Char());//购买来源
				vo.setChannelType(ChannelTypeEnum.ONLINE.value2Long());//线上

				//封装参数
				Map<String, Object> customParams = new HashMap<String, Object>();

				String[] queryStatus = getQueryStatus("1");//默认值1;债权状态:如果值为 0已生效 或 1还款中，则统一显示 回款中
				if(queryStatus != null){
					customParams.put("queryStatus",queryStatus);
				}
				customParams.put("orderBy",getOrderBy("", ""));//默认空值
				Pagination<CreditorRightsExtVo> pagination = creditorRightsService.getCreditorRightsPaging(1, searchCount, vo, customParams);
				
				//数据处理
	            List<CreditorRightsExtVO> creVOs = new ArrayList<>();
	            CreditorRightsExtVO creVO = null;
	            
	            List<CreditorRightsExtVo> crevList = pagination.getRows();
	            if(null != crevList && crevList.size() > 0){
	            	for (CreditorRightsExtVo v : crevList) {
	            		creVO = new CreditorRightsExtVO();
	            		
	            		creVO.setBuyPrice(v.getBuyPrice().toString());//出借金额（元）
	            		creVO.setCreditorRightsId(String.valueOf(v.getCreditorRightsId()));//债权ID
	            		creVO.setCreditorRightsName(v.getCreditorRightsName());//标名称
	            		// 距离到期多少天
	            		List<RightsRepaymentDetail> details = v.getRightsRepaymentDetailList();
	            		if(null != details && details.size() > 0){
	            			int daysBetween = DateUtil.daysBetween(new Date(), details.get((details.size()-1)).getRepaymentDayPlanned());
	            			creVO.setExpireDays(String.valueOf(daysBetween));
	            			
	            			if(daysBetween >= 0 && minDaysBetween == -1){
	            				minDaysBetween = daysBetween;
	            			}else if (daysBetween >= 0 && minDaysBetween >= daysBetween) {
	            				minDaysBetween = daysBetween;
							}
	            			
	            		}
	            		
	            		creVOs.add(creVO);
					}
	            }
	            
	            //根据最小距到期天数，提取该天数的债权信息
	            if(null != creVOs && creVOs.size() > 0){
	            	for (CreditorRightsExtVO cvo : creVOs) {
						
	            		// 首页已投统计接口，回款中个数，回款中总金额，最近到期天数.
	            		if(minDaysBetween == Integer.valueOf(cvo.getExpireDays())){
	            			minSumBuyPrice = minSumBuyPrice.add(new BigDecimal(cvo.getBuyPrice()));//累计金额
	            			minCount += 1;//累加数量
	            			title = cvo.getCreditorRightsName();//标名称
	            		}
	            		
					}
	            }
			}
            
            //用户或平台收益信息
			BigDecimal allBuyBalance = BigDecimal.ZERO;// 累计出借金额
			BigDecimal allProfit = BigDecimal.ZERO;// 累计收益
			if (currentUser != null) {
				allBuyBalance = lendOrderService.getAllBuyBalance(currentUser.getUserId());
				allProfit = lendOrderService.getAllProfit(currentUser.getUserId());
				BigDecimal totalAward = userAccountService.getUserTotalAward(currentUser.getUserId());//用户累计奖励
				allProfit = allProfit.add(totalAward);//将累计奖励，添加到累计收益中
			} else {
				allBuyBalance = lendOrderService.getAllBuyBalance(null);
				allProfit = lendOrderService.getAllProfit(null);
			}
			
            //返回数据
            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("isLogined", currentUser == null ? "false" : "true");
			resultMap.put("allBuyBalance", df.format(allBuyBalance));
			resultMap.put("allProfit", df.format(allProfit));
			
            resultMap.put("minCount", minCount);
            resultMap.put("minSumBuyPrice", df.format(minSumBuyPrice));
            resultMap.put("minDaysBetween", minDaysBetween);
            resultMap.put("title", title);
			
            return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
	}
	
	/**
     * 【APP接口工具】
     * @param backDate
     * @param buyDate
     * @return
     */
	public String getOrderBy(String backDate,String buyDate){

		String orderBy = "";
		String str1 = org.apache.commons.lang.StringUtils.isEmpty(backDate)?null:(backDate.equals("D")?"CURRENT_PAY_DATE desc":"CURRENT_PAY_DATE asc");
		String str2 = org.apache.commons.lang.StringUtils.isEmpty(buyDate)?null:(buyDate.equals("D")?" LOBD.BUY_DATE  desc":" LOBD.BUY_DATE  asc");


		orderBy += str1==null?"":str1;
		orderBy += str2==null?"":(org.apache.commons.lang.StringUtils.isEmpty(orderBy)?str2:","+str2);
		return orderBy;
	}

	/**
	 * 【APP接口工具】
	 * @param status
	 * @return
	 */
	private String[] getQueryStatus(String status){
		if(org.apache.commons.lang.StringUtils.isNotEmpty(status)){
			if(status.indexOf("0")!=-1){
				return null;
			}
			if(status.indexOf("1-2-3")!=-1){
				return null;
			}
			if(status.indexOf("1")!=-1){
				status = status.replace("1","0-1");
			}
			if (status.indexOf("2")!=-1){
				status = status.replace("2","8");
			}

			if (status.indexOf("3")!=-1){
				status = status.replace("3","2-3-7");
			}
			return status.split("-");
		}
		return null;
	}
	
}
