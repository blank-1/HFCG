package com.xt.cfp.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xt.cfp.core.service.task.LoanReportTask;
import com.xt.cfp.core.service.task.TradeReportTask;
import com.xt.cfp.core.service.task.UserReportTask;
import com.xt.cfp.core.util.ReportResult;

/**
 * 文件报备补报controller
 * 
 * @author HuYongkui
 *
 */
@Controller
@RequestMapping("/manualReport")
public class ManualReportController extends BaseController {

	@Autowired
	private UserReportTask userReportTask;
	@Autowired
	private LoanReportTask loanReportTask;
	@Autowired
	private TradeReportTask tradeReportTask;

	/**
	 * 个人开户补报
	 */
	@RequestMapping("/userReport")
	public ModelAndView userReport() {
		ModelAndView mv = new ModelAndView();
		ReportResult result = userReportTask.userReportText();
		mv.setViewName("jsp/reportUp/user_report");
		mv.addObject("result", result);
		return mv;
	}

	/**
	 * 个人开户补报上传
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/userReportUp")
	@ResponseBody
	private ReportResult userReportUp(@RequestParam(value = "text", defaultValue = "") String text) throws UnsupportedEncodingException {
		ReportResult result = userReportTask.userReportUp(URLDecoder.decode(text,"UTF-8"));
		return result;
	}

	/**
	 * 法人开户补报
	 * 
	 * @return
	 */
	@RequestMapping("/corpReport")
	public ModelAndView corpReport() {
		ModelAndView mv = new ModelAndView();
		ReportResult result = userReportTask.corpReportText();
		mv.setViewName("jsp/reportUp/corp_report");
		mv.addObject("result", result);
		return mv;
	}

	/**
	 * 法人开户补报上传
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/corpReportUp")
	@ResponseBody
	private ReportResult corpReportUp(@RequestParam(value = "text", defaultValue = "") String text) throws UnsupportedEncodingException {
		ReportResult result = userReportTask.corpReportUp(URLDecoder.decode(text,"UTF-8"));
		return result;
	}

	/**
	 * 项目新增补报
	 * 
	 * @return
	 */
	@RequestMapping("/loanReport")
	public ModelAndView loanReport() {
		ModelAndView mv = new ModelAndView();
		ReportResult result = loanReportTask.loanReportTxt();
		mv.addObject("result", result);
		mv.setViewName("jsp/reportUp/loan_report");
		return mv;
	}

	/**
	 * 项目补报上传
	 * 
	 * @param text
	 * @return
	 */
	@RequestMapping("/loanReportUp")
	@ResponseBody
	private ReportResult loanReportUp(@RequestParam(value = "text", defaultValue = "") String text) throws UnsupportedEncodingException {
		ReportResult result = loanReportTask.loanReportUp(URLDecoder.decode(text,"UTF-8"));
		return result;
	}

	/**
	 * 交易补报
	 * 
	 * @return
	 */
	@RequestMapping("/tradeReport")
	public ModelAndView tradeReport(@RequestParam(value = "tradeType", defaultValue = "2") String tradeType) {
		ModelAndView mv = new ModelAndView();
		ReportResult result = tradeReportTask.tradeReportTxt(tradeType);
		mv.addObject("result", result);
		mv.addObject("tradeType", tradeType);
		mv.setViewName("jsp/reportUp/trade_report");
		return mv;
	}

	/**
	 * 交易补报上传
	 * @param tradeType
	 * @param text
	 * @return
	 */
	@RequestMapping("/tradeReportUp")
	@ResponseBody
	public ReportResult tradeReportUp(@RequestParam(value = "tradeType", defaultValue = "2") String tradeType,
			@RequestParam(value = "text", defaultValue = "2") String text) throws UnsupportedEncodingException {
		ReportResult result = tradeReportTask.tradeReportUp(tradeType, URLDecoder.decode(text,"UTF-8"));
		return result;
	}

}
