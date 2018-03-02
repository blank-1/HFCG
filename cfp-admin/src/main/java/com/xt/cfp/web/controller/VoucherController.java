package com.xt.cfp.web.controller;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.constants.UserType;
import com.xt.cfp.core.constants.VoucherConstants;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.pojo.ext.VoucherProductVO;
import com.xt.cfp.core.pojo.ext.VoucherVO;
import com.xt.cfp.core.service.PayService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.VoucherService;
import com.xt.cfp.core.util.*;
import com.xt.cfp.core.util.StringUtils;

import jodd.io.FileUtil;

import org.apache.commons.lang.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by lenovo on 2015/8/20.
 */
@Controller
@RequestMapping(value = "/voucher")
public class VoucherController extends BaseController {

    @Autowired
    private VoucherService voucherService;
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private PayService payService;
    /**
     * 跳转至创建财富券产品列表页面
     * @return
     */
    @RequestMapping(value = "showVoucherProduct")
    public String showVoucherProduct(){
        return "jsp/voucher/showVoucherProductList";
    }
    /**
     * 跳转至创建财富券页面
     * @return
     */
    @RequestMapping(value = "add")
    public String add(){
        return "jsp/voucher/addVoucherProduct";
    }

    @RequestMapping(value = "voucherProductList")
    @ResponseBody
    public Object voucherProductList(  @ModelAttribute VoucherProduct voucherProduct,
                                       @RequestParam(value = "startDateStr", defaultValue = "", required = false) String startDate,
                                      @RequestParam(value = "endDateStr" ,defaultValue = "", required = false) String endDate,
                                      @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                                      @RequestParam(value = "page", defaultValue = "1") int pageNo){
        //封装参数
        TimeInterval timeInterval = new TimeInterval(// 日期间隔
                "".equals(startDate) ? null : DateUtil.parseStrToDate(
                        startDate, "yyyy-MM-dd"),
                "".equals(endDate) ? null : DateUtil.parseStrToDate(
                        endDate, "yyyy-MM-dd"));


        Map<String, Object> customParams = new HashMap<String, Object>();
        customParams.put("timeInterval", timeInterval);


        Pagination<VoucherProductVO> voucherProductPaging = voucherService.getVoucherProductPaging(pageNo, pageSize, voucherProduct, customParams);
        return voucherProductPaging;
    }

    /**
     * 保存财富券产品
     * @param voucherProduct
     * @return
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public String save(VoucherProduct voucherProduct){
        AdminInfo currentUser = getCurrentUser();
        if (currentUser==null)
            return "session已过期";
        try {
            if (voucherProduct.getVoucherProductId()==null){
                //默认有效
                voucherProduct.setStatus(VoucherConstants.VoucherProductStatus.USAGE.getValue());
                voucherProduct.setAdminId(currentUser.getAdminId());
                voucherProduct.setIsExperience(voucherProduct.getIsExperience()==null?"0":"1");
                voucherService.addVoucherProduct(voucherProduct);
            }
            else{}
//                voucherService.updateBondSource(bondSource);
            return "success";
        }catch (SystemException se){
            logger.error(se.getDetailDesc(),se);
            se.printStackTrace();
            return se.getMessage();
        }
    }


    /**
     * 详情页面
     * @return
     */
    @RequestMapping(value = "detail")
    public String detail(HttpServletRequest request,Long voucherProductId) {
        //组织回显参数
        VoucherProductVO voucherProductVO = voucherService.getVoucherProductById(voucherProductId);

        request.setAttribute("voucherProduct",voucherProductVO);

        return "jsp/voucher/voucherProductDetail";
    }
    /**
     * 详情页面
     * @return
     */
    @RequestMapping(value = "detailVoucher")
    public String detailVoucher(HttpServletRequest request,Long voucherId) {
        //组织回显参数
        VoucherVO voucherVO = voucherService.getVoucherById(voucherId);
        request.setAttribute("voucher",voucherVO);
        //获得使用记录
        List<VoucherPayOrderDetail> voucherPayOrderDetail = voucherService.getVoucherPayOrderDetail(voucherId);
        if (voucherPayOrderDetail!=null&&voucherPayOrderDetail.size()>0){
            String orderNames = "";
            for (VoucherPayOrderDetail detail:voucherPayOrderDetail){
                if (detail.getDetailId()!=null){
                    if (voucherVO.getUsageScenario().equals(VoucherConstants.UsageScenario.WITHDRAW.getValue())){
                        //提现券
                        orderNames += voucherVO.getDetailId() +"、";
                    }else{
                        LendOrder lendOrder = payService.getLendOrderByPayOrderDetailId(detail.getDetailId());
                        if (lendOrder!=null)
                            orderNames +=lendOrder.getLendOrderName()+"、";
                    }
                }
            }
            if (org.apache.commons.lang.StringUtils.isNotEmpty(orderNames))
                voucherVO.setOrderName(orderNames.substring(0,orderNames.length()-1));
        }
        return "jsp/voucher/voucherDetail";
    }

    /**
     * 详情页面
     * @return
     */
    @RequestMapping(value = "detailProduct")
    @ResponseBody
    public Object detailProduct(HttpServletRequest request,Long voucherProductId) {
        //组织回显参数
        VoucherProductVO voucherProductVO = voucherService.getVoucherProductById(voucherProductId);
        return voucherProductVO;
    }



    /**
     * 停用
     * @param request
     * @param voucherProductId
     * @return
     */
    @RequestMapping(value = "stopUse")
    @ResponseBody
    public String delBondSource(HttpServletRequest request,Long voucherProductId){
        //修改状态为禁用
        voucherService.changeVoucherProductStatus(voucherProductId, VoucherConstants.VoucherProductStatus.UN_USAGE);
        return "success";
    }

    /**
     * 启用
     * @param request
     * @param voucherProductId
     * @return
     */
    @RequestMapping(value = "startUse")
    @ResponseBody
    public String startUse(HttpServletRequest request,Long voucherProductId){
        //修改状态为正常
        voucherService.changeVoucherProductStatus(voucherProductId, VoucherConstants.VoucherProductStatus.USAGE);
        return "success";
    }


    /**
     * 跳转财富券列表页面
     * @return
     */
    @RequestMapping(value = "showVoucher")
    public String showVoucher(){
        return "jsp/voucher/showVoucherList";
    }


    @RequestMapping(value = "voucherList")
    @ResponseBody
    public Object voucherList( @ModelAttribute VoucherVO voucherVO,
                               @RequestParam(value = "startDateStr", defaultValue = "", required = false) String startDate,
                               @RequestParam(value = "endDateStr" ,defaultValue = "", required = false) String endDate,
                               @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                               @RequestParam(value = "page", defaultValue = "1") int pageNo){

        //封装参数
        TimeInterval timeInterval = new TimeInterval(// 日期间隔
                "".equals(startDate) ? null : DateUtil.parseStrToDate(
                        startDate, "yyyy-MM-dd"),
                "".equals(endDate) ? null : DateUtil.parseStrToDate(
                        endDate, "yyyy-MM-dd"));

        Map<String, Object> customParams = new HashMap<String, Object>();
        customParams.put("timeInterval", timeInterval);
        voucherVO.setVoucherCouponType("1");//非加息券
        Pagination<VoucherVO> voucherPaging = voucherService.getVoucherPaging(pageNo, pageSize, voucherVO, customParams);
        return voucherPaging;
    }


    /**
     * 导出代金券
     */
    @RequestMapping(value = "exportExcel")
    public void exportExcel(@ModelAttribute VoucherVO voucherVO,
                            @RequestParam(value = "startDateStr", defaultValue = "", required = false) String startDate,
                            @RequestParam(value = "endDateStr" ,defaultValue = "", required = false) String endDate,HttpServletResponse response){

        //封装参数
        TimeInterval timeInterval = new TimeInterval(// 日期间隔
                "".equals(startDate) ? null : DateUtil.parseStrToDate(
                        startDate, "yyyy-MM-dd"),
                "".equals(endDate) ? null : DateUtil.parseStrToDate(
                        endDate, "yyyy-MM-dd"));

        Map<String, Object> customParams = new HashMap<String, Object>();
        customParams.put("timeInterval", timeInterval);

        voucherService.exportExcel(response, voucherVO,customParams);
    }

    /**
     * 跳转至发放财富券页面
     * @return
     */
    @RequestMapping(value = "addVoucher")
    public String addVoucher(HttpServletRequest request){
        //财富券列表
        List<VoucherProduct> voucherList = voucherService.getAvalibleHandOutVoucherProductList();
        request.setAttribute("voucherList",voucherList);
        return "jsp/voucher/addVoucher";
    }

    /**
     * 模糊查询用户名
     * @return
     */
    @RequestMapping(value = "getUser")
    @ResponseBody
    public String getUser(HttpServletRequest request,String userName){
        if (StringUtils.isNull(userName)){
            return null;
        }
        //用户列表
        List<UserInfo> userList = userInfoService.getUserListByLoginName(userName, UserType.COMMON);
        if (userList==null||userList.size()==0)
            return null;
        String result = "[{\"id\":\"\",\"text\":\"请选择\"},";
        for (UserInfo user:userList){
            result +="{\"id\":"+user.getUserId()+",\"text\":\""+user.getLoginName()+"\"},";
        }
        result = result.substring(0,result.length()-1);
        result += "]";
        return result;
    }
    /**
     * 查询所有用户个数
     * @return
     */
    @RequestMapping(value = "getAllUser")
    @ResponseBody
    public Integer getAllUser(HttpServletRequest request){
       return userInfoService.countAllUser(UserType.COMMON);
    }

    /**
     * 保存财富券产品
     * @param voucher
     * @return
     */
    @RequestMapping(value = "saveVoucher")
    @ResponseBody
    public String saveVoucher(@ModelAttribute Voucher voucher,String targetUser,String userId,String userIdList){
        AdminInfo currentUser = getCurrentUser();
        if (currentUser==null)
            return "session已过期";
        try {

            switch (targetUser){
                case "0":
                    voucherService.handOut(voucher.getVoucherProductId(),Long.valueOf(userId),voucher.getSourceType(),voucher.getSourceDesc());
                    break;
                case "1":
                    List<Long> userIds = new ArrayList<Long>();
                    for (String user_id:userIdList.split(",")){
                        userIds.add(Long.valueOf(user_id));
                    }
                    voucherService.handOut(voucher.getVoucherProductId(),userIds,voucher.getSourceType(),voucher.getSourceDesc());
                    break;
                case "2":
                    voucherService.handOutToEveryOne(voucher.getVoucherProductId(),voucher.getSourceType(),voucher.getSourceDesc());
                    break;
            }
            return "success";
        }catch (SystemException se){
            logger.error(se.getDetailDesc(),se);
            se.printStackTrace();
            return se.getMessage();
        }
    }

    /**
     * 导入用户文件
     * @param request
     * @param response
     */
    @RequestMapping(value = "importExcel")
    @ResponseBody
    public String  importExcel(HttpServletRequest request,HttpServletResponse response,MultipartFile importFile){
        List<String> loginName = importExcel(importFile);
        String userIdStr = "";
        String error = "";
        for (String name:loginName){
            UserInfo user = userInfoService.getUserByLoginName(name);
            if (user==null){
                error = "导入用户中有系统不存在的用户";
                break;
            }
            if(UserType.COMMON.getValue().equals(user.getType())){
                userIdStr+=user.getUserId()+",";
            }else{
                error = "导入用户存在非普通用户，导入失败";
                break;
            }
        }
        userIdStr = userIdStr.substring(0,userIdStr.length()-1);
        return "<script>parent.inputUserIds('"+userIdStr+"',"+loginName.size()+",'"+error+"');</script>";
    }

    private List<String> importExcel(MultipartFile importFile) {
        try {
        	String type="1";
        	if(importFile.getName().indexOf(".xlsx")>=0){
        		type="2";
        	}
            File tempFile = FileUtil.createTempFile();
            FileUtil.appendBytes(tempFile,importFile.getBytes());

            List<List<Map<String, Object>>> lists = ExcelUtil.analysisExcel(tempFile,type);
            List<Map<String,Object>> result = lists.get(0);//sheet-0
            List<String> loginNameList = new ArrayList<String>();
            for (Map<String,Object> map:result){
                Collection<Object> values = map.values();
                for (Object o:values){
                    if (o==null)
                        continue;
                    loginNameList.add(o.toString());
                }
            }
            return loginNameList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
