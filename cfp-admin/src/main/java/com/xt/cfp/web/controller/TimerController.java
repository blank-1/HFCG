package com.xt.cfp.web.controller;

import com.xt.cfp.core.service.BidTaskForTimerService;
import com.xt.cfp.core.service.CreditorRightsService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.financePlan.FinancePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by ren yulin on 15-7-16.
 */
@Controller
@RequestMapping("/timer")
public class TimerController extends BaseController {

    @Autowired
    private FinancePlanService financePlanService;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private BidTaskForTimerService bidTaskForTimerService;

    @RequestMapping("/crAutoMatch")
    @ResponseBody
    public Object testCrAutoMatch() {
        try {
            financePlanService.creditorRightsAutoMatch();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "faulure";
        }
    }

    @RequestMapping(value = "/bidFail")
    @ResponseBody
    public String bidFail() {
        bidTaskForTimerService.failBidTask();
        return "success";
    }

    @RequestMapping("/createAgreement")
    @ResponseBody
    public Object createAgreement() {
        try {
            loanApplicationService.createAgreement();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "faulure";
        }
    }

    @RequestMapping("/refreshOrderReceive")
    @ResponseBody
    public Object refreshOrderReceive() {
        try {
            lendOrderService.refreshLendOrderReceive();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "faulure";
        }
    }

    @RequestMapping("/to_timers")
    public String toTimes() {
        return "jsp/timer/timerList";
    }
    
    @RequestMapping("/testTimers")
    public void executeTimers(){
    	try {
    		bidTaskForTimerService.failBidTask();
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	try {
    		bidTaskForTimerService.contrastRechangeTask();
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	try {
    		bidTaskForTimerService.refreshUnRechargeOrderTask();
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	try {
    		bidTaskForTimerService.overDuePenltyTask();
		} catch (Exception e) {
			// TODO: handle exception
		}
    	try {
    		bidTaskForTimerService.frontCreditorTransOverdue();
    	} catch (Exception e) {
    		// TODO: handle exception
    	}
    	try {
    		bidTaskForTimerService.financeQuit();
    	} catch (Exception e) {
    		// TODO: handle exception
    	}
    	
    	
    }
    
    

    @RequestMapping("/quitFinance")
    @ResponseBody
    public Object financeQuit() {
    	try {
    		bidTaskForTimerService.financeQuit();
    	} catch (Exception e) {
    		// TODO: handle exception
    	}
    	return null;
    }
    
    
    @RequestMapping("/jiesuan")
    @ResponseBody
    public Object financeJiesuan() {
    	try {
    	} catch (Exception e) {
    		// TODO: handle exception
    	}
    	return null;
    }
    
    

    
}
