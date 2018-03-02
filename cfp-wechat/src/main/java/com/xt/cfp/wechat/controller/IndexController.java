package com.xt.cfp.wechat.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.constants.LoanApplicationListVOComparator;
import com.xt.cfp.core.pojo.AppBanner;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserOpenId;
import com.xt.cfp.core.pojo.WechatNotice;
import com.xt.cfp.core.pojo.ext.LendProductPublishVO;
import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;
import com.xt.cfp.core.pojo.ext.LproductWithBalanceStatus;
import com.xt.cfp.core.service.AppStartPageService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LendProductService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.UserOpenIdService;
import com.xt.cfp.core.service.WechatNoticeService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.Sign;
import com.xt.cfp.core.util.StringUtils;
import com.xt.cfp.wechat.annotation.DoNotNeedLogin;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController extends BaseController {
	private static Logger logger = Logger.getLogger(IndexController.class);
	@Autowired
	LoanApplicationService loanApplicationService;
	
	@Autowired
	LendOrderService lendOrderService;
	
	@Autowired
	UserOpenIdService userOpenIdService;
	
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	private LendProductService lendProductService;
 
	@Autowired
    private UserAccountService userAccountService;
	
	@Autowired
    private AppStartPageService appBannerService;

	@Autowired
	private WechatNoticeService wechatNoticeService;
	
	/**
	 * 跳转到：首页
	 */
	@DoNotNeedLogin
    @RequestMapping(value = "/")
    public ModelAndView index(HttpServletRequest request,String flag) {
    	ModelAndView mv = new ModelAndView();

    	if(!StringUtils.isNull(flag)){
			mv.addObject("regFlag", flag);
		}

//    	Map<String, Object> customParams = new HashMap<String, Object>();
		//customParams.put("isNewUserLoan", true);//是否加载新手标
    	
    	Pagination<LoanApplicationListVO> loanApplicationList = loanApplicationService.getLoanApplicationPaging(1, 7, null, null);
    	//获取微信绑定的当前用户登录
    	String code = request.getParameter("code");
    	logger.info("code="+code);
    	if(code != null){
    		String openId = Sign.getOpenId(code);
    		logger.info("opendId="+openId);
        	UserOpenId userOpenId = userOpenIdService.getOpenIdByCondition(null, openId, "1");
        	if(userOpenId != null && "1".equals(userOpenId.getType())){
        		request.getSession().setAttribute(Constants.USER_ID_IN_SESSION,userInfoService.getUserByUserId(userOpenId.getUserId()));   		
        	}else{
        		request.getSession().setAttribute("openId", openId);
        	}
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Map bannerMap = new HashMap();
    	bannerMap.put("appType", 4);
    	bannerMap.put("state", "1");
    	bannerMap.put("endPublishDate", sdf.format(new Date()));
    	bannerMap.put("rowNum", 1);
    	List<AppBanner> bannerList = appBannerService.getAppBannerByList(bannerMap);
    	mv.addObject("bannerList", bannerList);
    	//查询最近的公告信息
    	WechatNotice wechatNotice = wechatNoticeService.getTopNewWechatNotice();
    	String noticeTitle = "";
    	if(null != wechatNotice){
    		noticeTitle = wechatNotice.getNoticeTitle();
    	}
    	
		//获取当前用户登录
//		UserInfo currentUser = SecurityUtil.getCurrentUser(false);
//		BigDecimal allBuyBalance = BigDecimal.ZERO;// 累计出借金额
//		BigDecimal allProfit = BigDecimal.ZERO;// 累计收益
//		 java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00"); 
//		if (currentUser != null) {
//			allBuyBalance = lendOrderService.getAllBuyBalance(currentUser.getUserId());
//			allProfit = lendOrderService.getAllProfit(currentUser.getUserId());
//			BigDecimal totalAward = userAccountService.getUserTotalAward(currentUser.getUserId());
//			allProfit=allProfit.add(totalAward);
//			userInfoService.recordUser(currentUser);
//			//redis中存入用户标识
//			userInfoService.saveUserJsession(currentUser.getUserId().toString(),request.getRequestedSessionId());
//		} else {
//			allBuyBalance = lendOrderService.getAllBuyBalance(null);
//			allProfit = lendOrderService.getAllProfit(null);
//		}
		
		// 测试代码-开始
//		List<LoanApplicationListVO> list = loanApplicationList.getRows();
//    	for (LoanApplicationListVO loanVO : list) {
//			System.out.println("1.借款标题:" + loanVO.getLoanApplicationTitle());
//			System.out.println("2.年化利率:" + loanVO.getAnnualRate() + " + " + loanVO.getRewardsPercent());
//			System.out.println("3.借款期限:" + loanVO.getCycleCount());
//			System.out.println("4.借款金额:" + loanVO.getConfirmBalance());
//			System.out.println("5.信用等级 :" + loanVO.getCreditLevel());
//			System.out.println("6.借款进度:" + loanVO.getRatePercent());
//			System.out.println("===========================================");
//		}
    	// 测试代码-结束
    	
    	// 返回值说明：
		//1.借款标题 === 借款发标表 -- 借款标题 *****【LOAN_PUBLISH 。 LOAN_TITLE】
		//2.年化利率 == 借款产品表  -- 年利率 + 借款发标表 -- 奖励利率 *****【LOAN_PRODUCT 。ANNUAL_RATE + LOAN_PUBLISH 。AWARD_RATE】 ***注：LOAN_PUBLISH 。AWARD_RATE = LOAN_APPLICATION 。 ANNUAL_RATE
		//3.借款期限 == 借款产品表 -- 期限时长(单位：期限类型) *****【LOAN_PRODUCT 。DUE_TIME（DUE_TIME_TYPE）】 ***注：现在用 周期数 【CYCLE_COUNTS】
		//4.借款金额 == 借款申请表 -- 批复金额 *****【LOAN_APPLICATION 。CONFIRM_BALANCE】
		//5.信用等级 == 借款发标表 -- 信用等级 *****【LOAN_PUBLISH  。 CREDIT_LEVEL】
		//6.借款进度 == 出借订单表 -- sum(购买总金额) <除以>（借款申请表 -- 批复金额）*****【LEND_ORDER 。sum(BUY_BALANCE) 除以 LOAN_APPLICATION 。CONFIRM_BALANCE 】

    	if(null != loanApplicationList){
    		//calFirstLoan(loanApplicationList);
    		mv.addObject("loanApplicationList", loanApplicationList.getRows());
    	}
    	
    	Pagination<LendProductPublishVO> lendProductPublishVOList = lendProductService.findFinanceProductListForWebCondition(1, 1, new LendProductPublishVO());
    	if(null != lendProductPublishVOList && !lendProductPublishVOList.getRows().isEmpty()){
    			LproductWithBalanceStatus financeDetail = this.lendProductService.findFinanceProductDetailForWeb(lendProductPublishVOList.getRows().get(0).getLendProductPublishId());
    			mv.addObject("financePlanRows",lendProductPublishVOList.getRows().get(0) );
    			mv.addObject("availableBalance",financeDetail.getAvailableBalance() );
    	}
	
//		mv.addObject("isLogined", currentUser == null ? false : true);
//		mv.addObject("allBuyBalance", df.format(allBuyBalance));
//		mv.addObject("allProfit", df.format(allProfit));
    	mv.addObject("noticeTitle", noticeTitle);
        mv.setViewName("newIndex");
        return mv;
    }
	
	//计算出推荐的标的
	private void calFirstLoan(Pagination<LoanApplicationListVO> loanApplicationList){
		List<LoanApplicationListVO> list=loanApplicationList.getRows();
		if(list!=null&&list.size()>0){
			Collections.sort(list,new LoanApplicationListVOComparator());
			excludeLoan(list);
		}
	}
	
	//排除定向标为推荐
	private void excludeLoan(List<LoanApplicationListVO> list){
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getoType()==null){
				LoanApplicationListVO vo=list.get(i);
				list.remove(i);
				list.add(0, vo);
				return;
			}else{
				if(list.get(i).getoType().equals("0")){
					LoanApplicationListVO vo=list.get(i);
					list.remove(i);
					list.add(0, vo);
					return;
				}
			}
				
		}
	}
	
	@DoNotNeedLogin
    @RequestMapping(value = "/storesJoin")
    public ModelAndView storesJoin() {
    	ModelAndView mv = new ModelAndView();
    	mv.setViewName("storesJoin");
    	return mv;
	}
	@DoNotNeedLogin
    @RequestMapping(value = "/about")
    public ModelAndView about() {
    	ModelAndView mv = new ModelAndView();
    	mv.setViewName("about");
    	return mv;
	}
	@DoNotNeedLogin
    @RequestMapping(value = "/helpIndex")
    public ModelAndView help(String flag) {
    	ModelAndView mv = new ModelAndView();
    	if("0".equals(flag)){
    		mv.setViewName("/help/help_jsq");
    	}else if("1".equals(flag)){
    		mv.setViewName("/help/help1");
    	}else if("2".equals(flag)){
    		mv.setViewName("/help/help2");
    	}else if("3".equals(flag)){
    		mv.setViewName("/help/help3");
    	}else if("4".equals(flag)){
    		mv.setViewName("/help/help4");
    	}else if("5".equals(flag)){
    		mv.setViewName("/help/help5");
    	}else if("6".equals(flag)){
    		mv.setViewName("/help/help6");
    	}else if("7".equals(flag)){
    		mv.setViewName("/help/help7");
    	}else{
    		mv.setViewName("/help/help_index");
    	}
    	return mv;
	}
	@DoNotNeedLogin
    @RequestMapping(value = "/calculator")
    public ModelAndView calculator() {
    	ModelAndView mv = new ModelAndView();
    	mv.setViewName("calculator");
    	return mv;
	}

	@DoNotNeedLogin
    @RequestMapping(value = "/calculator/lendDetail")
    public ModelAndView lendDetail() {
    	ModelAndView mv = new ModelAndView();
		mv.addObject("tab",1);
    	mv.setViewName("calculator");
    	return mv;
	}

	@DoNotNeedLogin
    @RequestMapping(value = "/calculator/loanDetail")
    public ModelAndView loanDetail() {
    	ModelAndView mv = new ModelAndView();
		mv.addObject("tab",2);
    	mv.setViewName("calculator");
    	return mv;
	}
	/**
	 * 跳转到：安卓下载
	 */
	@DoNotNeedLogin
    @RequestMapping(value = "/androidDownload")
    public ModelAndView androidDownload(HttpServletRequest request) {
    	ModelAndView mv = new ModelAndView();
    	mv.setViewName("Android_download");
    	return mv;
	}
	
	@DoNotNeedLogin
    @RequestMapping(value = "/bankList")
    public ModelAndView bankList(HttpServletRequest request) {
    	return new ModelAndView("common/bankList");
	}
}
