package com.xt.cfp.web.controller;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.WithDrawExt;
import com.xt.cfp.core.service.CustomerCardService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.WithDrawService;
import com.xt.cfp.core.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luqinglin on 2015/6/19.
 */
@Controller
@RequestMapping(value = "/withdraw")
public class WithDrawController extends BaseController {

    private static Logger logger = Logger.getLogger(WithDrawController.class);

    @Autowired
    private WithDrawService withDrawService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CustomerCardService customerCardService;
    @Autowired
    private UserAccountService userAccountService;



    /**
     * 手动生成提现单
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping(value = "mannalCreatWithDraw")
    @ResponseBody
    public String mannalCreatWithDraw(HttpServletRequest request, Long userId,BigDecimal amount){

        if (getCurrentUser()==null){
            return "session已过期";
        }
        if(userId==null)
            return "请输入用户id";
        if (amount==null)
            return "请输入提现金额";
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(userId, PayConstants.PayChannel.LL);
        if (customerCard==null)
            return "此用户没有可用的银行卡";

        //生成提现单
        if (amount.compareTo(new BigDecimal(100))<0){
            return "请输入大于100的提现金额";
        }
        UserAccount cashAccount = userAccountService.getCashAccount(userId);
        if (amount.compareTo(cashAccount.getAvailValue2())>0)
            return "金额不足";
        WithDraw withDraw = new WithDraw();
        withDraw.setWithdrawAmount(amount);
        withDraw.setOperatorId(getCurrentUser().getAdminId());
        withDraw.setHappenType(WithDrawSource.SYSTEM_OPERT_WITHDRAW.getValue());
        withDraw.setUserId(userId);
        withDraw.setCustomerCardId(customerCard.getCustomerCardId());
        withDraw.setCreateTime(new Date());
        withDrawService.withDraw(false, withDraw, AccountConstants.AccountChangedTypeEnum.PLATFORM_USER, ClientEnum.WEB_CLIENT);

        return "success";
    }
   /**
     * 跳转至提现列表页面
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping(value = "showWithDraw")
    public String showWithDrawList(HttpServletRequest request,
                                   @RequestParam(required = false) Long userId){
        request.setAttribute("userId", userId);
        return "jsp/bondSource/withDrawList";
    }


    /**
     * 跳转至渠道审核页面
     * @return
     */
    @RequestMapping(value = "to_withDraw_list")
    public String toBondSourceList() {
        return "jsp/financial/withDrawList";
    }

    /**
     * 跳转至导入文件页面
     * @return
     */
    @RequestMapping(value = "toImportExcel")
    public String toImportExcel() {
        return "jsp/financial/toImportExcel";
    }

    /**
     * 提现申请列表
     * @param request
     * @param withDraw
     * @param startDate
     * @param endDate
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "showWithDrawList")
    @ResponseBody
    public Object showWithDrawList(HttpServletRequest request,
                                 @ModelAttribute WithDraw withDraw,
                                 @RequestParam(value = "startDate", defaultValue = "", required = false) String startDate,
                                 @RequestParam(value = "endDate" ,defaultValue = "", required = false) String endDate,
                                 @RequestParam(value = "operateName" ,defaultValue = "", required = false) String operateName,
                                 @RequestParam(value = "belongChannel", required = false) String belongChannel,
                                 @RequestParam(value = "orderResource", required = false) String orderResource,
                                 @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                                 @RequestParam(value = "page", defaultValue = "1") int pageNo) {
        //封装参数
        TimeInterval timeInterval = new TimeInterval(// 日期间隔
                "".equals(startDate) ? null : DateUtil.parseStrToDate(
                        startDate, "yyyy-MM-dd"),
                "".equals(endDate) ? null : DateUtil.parseStrToDate(
                        endDate, "yyyy-MM-dd"));

        //开户名，去除空格
        boolean isLegal = true;
        if(null != operateName && !"".equals(operateName)){
        	operateName = operateName.replaceAll(" ", "");
        	if(FilterUtil.isSpecialCharacters(operateName)){
        		isLegal = false;
            }
        }

        Map<String, Object> customParams = new HashMap<String, Object>();
        customParams.put("timeInterval", timeInterval);
        customParams.put("operateName", operateName);
        customParams.put("belongChannel", belongChannel);
        if(!StringUtils.isNull(orderResource)){
        	customParams.put("resource", orderResource);
        }

        if(!isLegal){
        	return new Pagination<WithDrawExt>();
        }
        Pagination<WithDrawExt> withDrawPaging = withDrawService.getWithDrawPaging(pageNo, pageSize, withDraw, customParams);
        return withDrawPaging;
    }

    /**
     * 审核页面
     * @return
     */
    @RequestMapping(value = "verify")
    public String verify(HttpServletRequest request,Long withdrawId) {
        //组织回显参数
        WithDraw withDraw = withDrawService.detail(withdrawId);
        request.setAttribute("withDraw",withDraw);
        request.setAttribute("withdrawId",withdrawId);
        return "jsp/financial/verify";
    }

    /**
     * 审核操作
     * @return
     */
    @RequestMapping(value = "doVerify")
    @ResponseBody
    public String doVerify(HttpServletRequest request,@ModelAttribute WithDrawExt withDraw) {

        try{
            AdminInfo currentUser = getCurrentUser();
            if (currentUser==null)
                return "session已过期";
            withDrawService.verify(withDraw);
            return "success";
        }catch (SystemException se){
            return se.getMessage();
        }


    }

    /**
     * 重新放款提现
     * @return
     */
    @RequestMapping(value = "reWithDraw")
    @ResponseBody
    public String reWithDraw(HttpServletRequest request,Long withDrawId) {

        if (getCurrentUser()==null){
            return "session已过期";
        }
        WithDraw withDraw = withDrawService.getWithDrawByWithDrawId(Long.valueOf(withDrawId));
        if(withDraw==null){
            return "提现单号不存在！";
        }
        if (withDraw.getHappenType().equals(WithDrawSource.USER_WITHDRAW.getValue())){
            return "非放款产生的提现单！";
        }
        if (withDraw.getVerifyStatus().equals(VerifyStatus.UN_PAST.getValue())){
            return "不处理驳回而失败的提现单！";
        }
        try{
            withDrawService.reWithDraw(withDrawId,getCurrentUser().getAdminId());
        }catch (SystemException se){
            return se.getErrorCode().getDesc();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 导出付款提现单
     * @param request
     * @param response
     */
    @RequestMapping(value = "exportExcel")
    public void exportExcel(HttpServletRequest request,HttpServletResponse response){
        withDrawService.exportExcel(response);
    }
    /**
     * 导入付款提现单
     * @param request
     * @param response
     */
    @RequestMapping(value = "importExcel")
    @ResponseBody
    public String  importExcel(HttpServletRequest request,HttpServletResponse response,MultipartFile importFile){
        withDrawService.importExcel(importFile);
        return "<script>parent.closeDialog();</script>";
    }

    /**
     * 确认打款（申请打款）
     * @param request
     * @param withDrawId
     * @return
     */
    @RequestMapping(value = "doWithDraw")
    @ResponseBody
    public String withDraw111(HttpServletRequest request,String withDrawId){
        AdminInfo currentUser = getCurrentUser();
        if (currentUser==null)
            return "session已过期";
        //判断卡是否已经绑定
        WithDraw withDraw = withDrawService.getWithDrawByWithDrawId(Long.valueOf(withDrawId));
        if(withDraw==null){
            return "提现单号不存在";
        }

        UserInfo user = userInfoService.getUserByUserId(withDraw.getUserId());
        if (!user.getStatus().equals(UserStatus.NORMAL.getValue())){
            return "提现用户已冻结或已禁用，无法提现";
        }

        CustomerCard card = customerCardService.findById(withDraw.getCustomerCardId());
        if (card==null){
            return "未绑定银行卡";
        }
        if(!CustomerCardStatus.NORMAL.getValue().equals(card.getStatus())
				|| !CustomerCardBindStatus.BINDED.getValue().equals(card.getBindStatus())){
            return "银行卡未绑定";
        }
        String result = withDrawService.doWithDrawNew(withDrawId);
        return result;
    }

    /**
     * 确认打款（刷新打款）
     * @param request
     * @param withDrawId
     * @return
     */
    @RequestMapping(value = "refresh")
    @ResponseBody
    public String refresh(HttpServletRequest request,String withDrawId){
        try {
            AdminInfo currentUser = getCurrentUser();
            if (currentUser==null)
                return "session已过期";
            String result = withDrawService.refreshNew(withDrawId);
            if("SUCCESS".equals(result)){
                return "提现成功！";
            }else if ("DOING".equals(result)){
                return "提现处理中，请稍后刷新！";
            }else{
                return "提现失败!<span style='display:none;'>"+result+"</span>";
            }
        }catch (SystemException systemException){
        	systemException.printStackTrace();
            return "提示：提现失败!<span style='display:none;'>"+systemException.getErrorCode().getDesc()+"</span>";
        }catch (Exception e) {
        	e.printStackTrace();
        	return "提示：系统错误!<span style='display:none;'>"+e.getMessage()+"</span>";
		}
    }

    public static void main(String[] args) {
        System.out.println( sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high"));
    }
}
