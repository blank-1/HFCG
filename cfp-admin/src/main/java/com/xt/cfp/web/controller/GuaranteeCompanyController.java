package com.xt.cfp.web.controller;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.BondSourceExt;
import com.xt.cfp.core.pojo.ext.BondSourceUser;
import com.xt.cfp.core.pojo.ext.GuaranteeCompanyExt;
import com.xt.cfp.core.pojo.ext.WithDrawExt;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.TimeInterval;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luqinglin on 2015/6/19.
 */
@Controller
@RequestMapping(value = "/guaranteeCompany")
public class GuaranteeCompanyController extends BaseController {

    private static Logger logger = Logger.getLogger(GuaranteeCompanyController.class);

    @Autowired
    private BondSourceService bondSourceService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private ConstantDefineService constantDefineService;
    @Autowired
    private CustomerCardService customerCardService;
    @Autowired
    private GuaranteeCompanyService guaranteeCompanyService;


    /**
     * 跳转至担保公司页面
     * @return
     */
    @RequestMapping(value = "to_guaranteeCompany_list")
    public String toGuaranteeCompanyList() {
        return "jsp/guarantee/guaranteeCompanyList";
    }

    /**
     * 担保公司列表
     * @param request
     * @param guaranteeCompany
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "list")
    @ResponseBody
    public Object list(HttpServletRequest request,
                       @ModelAttribute GuaranteeCompany guaranteeCompany,
                                 @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                                 @RequestParam(value = "page", defaultValue = "1") int pageNo) {
        Pagination<GuaranteeCompanyExt> guaranteeCompanyPaging = guaranteeCompanyService.getGuaranteeCompanyPaging(pageNo, pageSize, guaranteeCompany, null);

        return guaranteeCompanyPaging;
    }

    /**
     * 跳至添加页面
     * @param request
     * @param companyId
     * @return
     */
    @RequestMapping(value = "add")
    public String add(HttpServletRequest request,Long companyId){
        if (companyId!=null){
            //修改操作
            GuaranteeCompany guaranteeCompany = guaranteeCompanyService.getGuaranteeCompanyByCompanyId(companyId);
            request.setAttribute("guaranteeCompany", guaranteeCompany);
        }
        return "jsp/guarantee/guaranteeCompanyAdd";
    }

    /**
     * 保存担保公司
     * @param guaranteeCompany
     * @return
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public String save(@ModelAttribute GuaranteeCompany guaranteeCompany){
        try {
            if (guaranteeCompany.getCompanyId() == null)
                guaranteeCompanyService.addGuaranteeCompany(guaranteeCompany);
            else
                guaranteeCompanyService.updateBondSource(guaranteeCompany);
            return "success";
        }catch (SystemException se){
            logger.error(se.getDetailDesc(),se);
            se.printStackTrace();
            return se.getMessage();
        }

    }

    /**
     * 禁用担保公司
     * @param request
     * @param companyId
     * @return
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delGuaranteeCompany(HttpServletRequest request,Long companyId){
        //修改状态为禁用
        guaranteeCompanyService.changeGuaranteeCompanyStatus(companyId, GuaranteeCompanyStatus.DISABLED);
        return "success";
    }

    /**
     * 解禁担保公司
     * @param request
     * @param companyId
     * @return
     */
    @RequestMapping(value = "startUse")
    @ResponseBody
    public String startUse(HttpServletRequest request,Long companyId){
        //修改状态为正常
        guaranteeCompanyService.changeGuaranteeCompanyStatus(companyId, GuaranteeCompanyStatus.NORMAL);
        return "success";
    }

    /**
     * 担保公司详情
     * @param request
     * @param companyId
     * @return
     */
    @RequestMapping(value = "detail")
    public String detail(HttpServletRequest request,Long companyId){

        GuaranteeCompany guaranteeCompany = guaranteeCompanyService.getGuaranteeCompanyDetailByCompanyId(companyId);
        request.setAttribute("guaranteeCompany", guaranteeCompany);
        return "jsp/guarantee/guaranteeCompanyDetail";
    }

    /**
     * 跳至充值页面
     * @param request
     * @param companyId
     * @return
     */
    @RequestMapping(value = "toIncome")
    public String toIncome(HttpServletRequest request,@RequestParam(required = false) Long companyId){
        GuaranteeCompany guaranteeCompany = guaranteeCompanyService.getGuaranteeCompanyDetailByCompanyId(companyId);
        request.setAttribute("guaranteeCompany", guaranteeCompany);
        return "jsp/guarantee/guaranteeCompanyIncome";
    }
    /**
     * 跳至提现页面
     * @param request
     * @param companyId
     * @return
     */
    @RequestMapping(value = "toWithDraw")
    public String toWithDraw(HttpServletRequest request,@RequestParam(required = false) Long companyId){
        GuaranteeCompany guaranteeCompany = guaranteeCompanyService.getGuaranteeCompanyDetailByCompanyId(companyId);
        request.setAttribute("guaranteeCompany", guaranteeCompany);
        //准备银行信息
        List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType(ConstantDefineCode.BANK.getValue());
        //查询渠道最近使用的客户卡
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(guaranteeCompany.getUserId(), PayConstants.PayChannel.LL);
        request.setAttribute("customerCard", customerCard);
        request.setAttribute("constantDefines", constantDefines);
        return "jsp/guarantee/guaranteeCompanyWithDraw";
    }

    @RequestMapping(value = "income")
    @ResponseBody
    public String income(@ModelAttribute RechargeOrder rechargeOrder,@RequestParam(required = true) Long bondSourceId){
        try {
            guaranteeCompanyService.recharge(rechargeOrder);
            return "success";
        }catch (SystemException se){
            se.printStackTrace();
            return se.getMessage();
        }
    }

    @RequestMapping(value = "withDraw")
    @ResponseBody
    public String withDraw(@ModelAttribute WithDrawExt withDraw,@RequestParam(required = false) Long bondSourceId){
        try {
            withDraw.setHappenType(WithDrawSource.USER_WITHDRAW.getValue());
            guaranteeCompanyService.withDraw(withDraw);
            return "success";
        }catch (SystemException se){
            se.printStackTrace();
            return se.getMessage();
        }
    }


}
