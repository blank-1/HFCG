package com.xt.cfp.wechat.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.core.constants.WechatNoticeConstants;
import com.xt.cfp.core.pojo.WechatNotice;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.WechatNoticeService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.wechat.annotation.DoNotNeedLogin;

@Controller 
@RequestMapping("/notice")
public class NoticeController extends BaseController {
	
	private static Logger logger = Logger.getLogger(NoticeController.class);
	
	@Autowired
	public LendOrderService lendOrderService;

	@Autowired
	private WechatNoticeService wechatNoticeService;
	
	@DoNotNeedLogin
	@RequestMapping(value = "/list")
	public String toNoticeList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "sign", defaultValue = "notice") String sign) {
		BigDecimal allBuyBalance = lendOrderService.getAllBuyBalance(null);// 累计出借金额
		BigDecimal allProfit = lendOrderService.getAllProfit(null);// 累计收益
		request.setAttribute("allBuyBalance", allBuyBalance);
		request.setAttribute("allProfit", allProfit);
		request.setAttribute("sign", sign);
		try {
	    	int days = DateUtil.daysBetween(new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-01"), new Date());
	    	request.setAttribute("days", days);
		} catch (Exception e) {
			logger.error("计算平台运营时间错误：" + e.getMessage());
		}
		return "/notice/noticeList";
	}
	
	@DoNotNeedLogin
    @RequestMapping(value = "/noticeList")
    @ResponseBody
    public Pagination<WechatNotice> noticeList(HttpServletRequest request,
    		@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
    		@RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
        
        WechatNotice wechatNotice = new WechatNotice();
        wechatNotice.setNoticeState(WechatNoticeConstants.WechatNoticeStateEnum.VALID.getValue());
        
        return wechatNoticeService.getWechatNoticePaging(pageNo, pageSize, wechatNotice, null);
    }
	
	@DoNotNeedLogin
	@RequestMapping(value = "/detail")
	public String toNoticeDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "noticeId", required = false) Long noticeId) {
		WechatNotice wechatNotice = wechatNoticeService.getWechatNoticeById(noticeId);
		request.setAttribute("wechatNotice", wechatNotice);
		return "/notice/noticeDetail";
	}
	
}
