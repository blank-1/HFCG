package com.xt.cfp.web.controller;

import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.ext.LendProductPublishVO;
import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LendProductService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.SecurityUtil;
import com.xt.cfp.web.annotation.DoNotNeedLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Controller
public class IndexController extends BaseController {
	
	@Autowired
	LoanApplicationService loanApplicationService;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	LendOrderService lendOrderService;
	@Autowired
	private LendProductService lendProductService;
	
	/**
	 * 跳转到：首页（新）
	 */
	@DoNotNeedLogin
    @RequestMapping(value = "/", method=RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) {
    	ModelAndView mv = new ModelAndView();
    	
    	//新手标推荐
    	Pagination<LoanApplicationListVO> loanSpecialApplicationList = loanApplicationService.getLoanSpecialApplicationPaging(1, 1, null, null);
    	
    	//省心计划推荐
    	Pagination<LendProductPublishVO> lendProductPublishVOList = lendProductService.findFinanceProductListForWebCondition(2, 1, new LendProductPublishVO());
    	
    	//普通散标推荐
    	Pagination<LoanApplicationListVO> loanApplicationList = loanApplicationService.getLoanApplicationPaging(1, 5, null, null);
    	
		//获取当前用户登录
		UserInfo currentUser = SecurityUtil.getCurrentUser(false);
		BigDecimal allBuyBalance = BigDecimal.ZERO;// 累计出借金额
		BigDecimal allProfit = BigDecimal.ZERO;// 累计收益
		BigDecimal totalAward = BigDecimal.ZERO;// 已获奖励
		if (currentUser != null) {
			allBuyBalance = lendOrderService.getAllBuyBalance(currentUser.getUserId());
			allProfit = lendOrderService.getAllProfit(currentUser.getUserId());
			totalAward = userAccountService.getUserTotalAward(currentUser.getUserId());

			//获取用户减少的奖励（如：取消）
			BigDecimal totalReduceAward = userAccountService.getUserTotalReduceAward(currentUser.getUserId());
			totalAward = totalAward.subtract(totalReduceAward);//实际得奖=励总的奖励-减少的奖励
			
			UserAccount cashAccount = userAccountService.getCashAccount(currentUser.getUserId());
			request.setAttribute("cashAccount", cashAccount);
		} else {
			allBuyBalance = lendOrderService.getAllBuyBalance(null);
			allProfit = lendOrderService.getAllProfit(null);
		}

		if(null != loanSpecialApplicationList&&loanSpecialApplicationList.getRows()!=null&&loanSpecialApplicationList.getRows().size()>0){
			mv.addObject("loanSpecialApplication", loanSpecialApplicationList.getRows().get(0));
		}
    	if(null != lendProductPublishVOList&&lendProductPublishVOList.getRows()!=null&&lendProductPublishVOList.getRows().size()>0){
    		mv.addObject("shengxinLe", lendProductPublishVOList.getRows().get(0));
    		mv.addObject("shengxinRi", lendProductPublishVOList.getRows().get(1));
    	}
    	if(null != loanApplicationList&&loanApplicationList.getRows()!=null&&loanApplicationList.getRows().size()>0){
    		mv.addObject("loanApplicationList", loanApplicationList.getRows());
    	}
		mv.addObject("isLogined", currentUser != null);
		mv.addObject("allBuyBalance", allBuyBalance);
		mv.addObject("allProfit", allProfit.add(totalAward));
        mv.setViewName("index_201612");
        return mv;
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
}
