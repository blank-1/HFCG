package com.xt.cfp.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.core.pojo.CreditorRights;
import com.xt.cfp.core.service.CreditorRightsService;
import com.xt.cfp.core.service.RightsRepaymentDetailService;


@Controller
@RequestMapping("/jsp/rights/detail")
public class RightsRepaymentDetailController extends BaseController {

	@Autowired
	private RightsRepaymentDetailService rightsRepaymentDetailService;
	
	@Autowired
	private CreditorRightsService creditorRightsService;
	
    /**
     * 执行：获取列表
     */
	@RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(value = "creditorRightsId", required = false) Long creditorRightsId) throws Exception {
        return rightsRepaymentDetailService.getDetailListByRightsId(creditorRightsId);
    }
	
    /**
     * 执行：获取详细目录
     */
	@RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail(@RequestParam(value = "creditorRightsId", required = false) Long creditorRightsId) throws Exception {
		Map map = new HashMap();
		map.put("result", "success");
		
		CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
		
		map.put("annualRate", creditorRights.getAnnualRate());
        return map;
    }
	
}
