package com.xt.cfp.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xt.cfp.core.service.CreditorRightsService;

@Controller
@RequestMapping("/jsp/rights/all")
public class CreditorRightsController extends BaseController {
	
	@Autowired
	private CreditorRightsService creditorRightsService;
	
    /**
     * 跳转到：债权-列表
     */
    @RequestMapping(value = "/to_rights_list")
    public ModelAndView to_rights_list(@RequestParam(value = "userId", required=false) String userId) {
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("userId", userId);
        mv.setViewName("jsp/rights/all/rights_list");
        return mv;
    }
    
    /**
     * 执行：分页列表
     * @param pageSize 页数
     * @param pageNo 页码
     * @param searchLoanName 借款标题
     * @param searchLoanUserName 借款人用户名
     * @param searchLeanUserName 出借人用户名
     * @param searchT 时间段
     * @param searchBeginTime 出借开始时间
     * @param searchEndTime 出借结束时间
     * @param searchState 债权状态
     */
	@RequestMapping(value = "/list")
    @ResponseBody
    public Object list(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "searchLoanName", required=false) String searchLoanName,
			@RequestParam(value = "searchLoanUserName", required=false) String searchLoanUserName,
			@RequestParam(value = "searchLeanUserName", required=false) String searchLeanUserName,
			@RequestParam(value = "searchT", required=false) String searchT,
			@RequestParam(value = "searchBeginTime", required=false) String searchBeginTime,
			@RequestParam(value = "searchEndTime", required=false) String searchEndTime,
			@RequestParam(value = "userId", required=false) String userId,
			@RequestParam(value = "searchState", required=false) String searchState) throws Exception {

		// 填充参数
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("searchLoanName", searchLoanName);
        params.put("searchLoanUserName", searchLoanUserName);
        params.put("searchLeanUserName", searchLeanUserName);
        params.put("searchT", searchT);
        params.put("searchBeginTime", searchBeginTime);
        params.put("searchEndTime", searchEndTime);
        params.put("searchState", searchState);
        params.put("userId", userId);
        
        return creditorRightsService.findAllByPage(pageNo, pageSize, params);
    }
	
}
