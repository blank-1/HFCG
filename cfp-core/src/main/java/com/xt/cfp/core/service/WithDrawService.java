package com.xt.cfp.core.service;

import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.ClientEnum;
import com.xt.cfp.core.context.ParaContext;
import com.xt.cfp.core.pojo.Voucher;
import com.xt.cfp.core.pojo.WithDraw;
import com.xt.cfp.core.pojo.ext.WithDrawExt;
import com.xt.cfp.core.pojo.ext.crm.CRMWithdrawVO;
import com.xt.cfp.core.pojo.ext.phonesell.WithdrawVO;
import com.xt.cfp.core.util.Pagination;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luqinglin on 2015/6/25.
 */
public interface WithDrawService {

    /**
     * 第三方提现
     * @param withDrawExt
     * @param accountChangedType
     * @return
     */
    public WithDraw withDrawByThirdPart(WithDrawExt withDrawExt, AccountConstants.AccountChangedTypeEnum accountChangedType);
    /**
     * 生成提现单（会冻结账户）
     * @param withDraw
     * @param accountChangedType
     * @return
     */
    public WithDraw withDraw(boolean voucher,WithDraw withDraw, AccountConstants.AccountChangedTypeEnum accountChangedType, ClientEnum client);

    /**
     * 创建提现申请（只建提现申请，不冻账户余额）
     * @param paraContext
     * @return
     */
    WithDraw withDraw(ParaContext paraContext) throws Exception;

    /**
     * 提现记录列表
     * @param pageNo
     * @param pageSize
     * @param withDraw
     * @param customParams
     * @return
     */
    Pagination<WithDrawExt> getWithDrawPaging(int pageNo, int pageSize, WithDraw withDraw, Map<String, Object> customParams);
    
    /**
     * 获取所有提现记录数据（导出excel报表专用）
     * @param withDraw
     * @param customParams
     * @return
     */
    List<LinkedHashMap<String, Object>> getWithDrawAllList(WithDraw withDraw, Map<String, Object> customParams);
    
    /**
     * 电销提现记录列表
     * @param pageNo
     * @param pageSize
     * @param withDraw
     * @param customParams
     * @return
     */
    Pagination<WithdrawVO> phonesellWithDrawPaging(int pageNo, int pageSize, Map<String, Object> customParams);
    
    /**
     * 电销统计数据
     * @param customParams
     * @return
     */
    List<String> getAllWithDrawAccounts(Map<String, Object> customParams);
    
    /**
     * 审核
     * @param withDraw
     */
    void verify(WithDraw withDraw);

    /**
     * 提现申请详细信息
     * @param withdrawId
     */
    WithDraw detail(Long withdrawId);

    /**
     * 导出提现单
     * @param response
     */
    void exportExcel(HttpServletResponse response);

    /**
     * 导入付款状态
     * @param importFile
     */
    void importExcel(MultipartFile importFile);

    /**
     * 根据卡id查询提现记录
     * @param customerCardId
     * @return
     */
    List<WithDraw> getWithdrawListByCardId(Long customerCardId);

    /**
     * 提现申请时发送短信
     * @param mobileNo
     */
    void sendWithDrawApplyMsg(String mobileNo);

    /**
     * 向第三方发出提现请求 连连
     * @param withDrawId
     */
    String doWithDrawNew(String withDrawId);
    /**
     * 根据用户卡ID，获得提现总额
     * @param customerCardId 用户卡ID
     */
    BigDecimal getWithdrawAmountSumByCardId(Long customerCardId);

    /**
     * 刷新提现状态
     * @param withDrawId
     */
    String refreshNew(String withDrawId);
    /**
     * 提现回调处理
     * @param withDrawId
     */
    String withDrawCallback(Map<String, String> resultMap);
    /**
     * 获取用户当日提现请求次数
     * @param userId
     * @return
     */
    int getWithDrawTimesByUserId(Long userId);

    /**
     * 获取当日用户提现总金额
     * @param userId
     * @return
     */
    BigDecimal getWithDrawAmountByUserId(Long userId);

    int getWithDrawTimesDue();

    /**
     * 根据id查询
     * @param withDrawId
     * @return
     */
    WithDraw getWithDrawByWithDrawId(Long withDrawId);
    /**
     * 根据id查询
     * @param withDrawId
     * @return
     */
    WithDraw getWithDrawByWithDrawId(Long withDrawId,boolean lock);

    /**
     * 重新放款提现
     * @param withDrawId
     */
    void reWithDraw(Long withDrawId,Long admimId) throws Exception;
    
    /**
     * 查询WithDraw List
     * @param withDraw
     * @return
     */
    List<WithDraw> findBy(WithDraw withDraw);

    /**
     * 提现总额
     * @param userId
     * @return
     */
    BigDecimal getAllWithDrawAmountByUserId(Long userId);

    /**
     * 提现总额
     * @param userId
     * @return
     */
    BigDecimal getAllWithDrawAmountByUserId(Long userId,String month);

    /**
     * 线下手动生成提现单（无法绑卡情况下使用）
     * @param sysMobileNo
     * @param cardNo
     * @param mobileNo
     */
    void outLineWithDraw(String sysMobileNo,Long bankid ,String cardNo,String mobileNo,BigDecimal amount);

    /**
     * 获取可用提现券个数
     * @param userId
     * @return
     */
    int getVoucherWithDrawCount(Long userId);

    /**
     * 快过期的提现券
     * @param userId
     * @return
     */
    Voucher getVoucherWithDraw(Long userId);
    
    /**
     * crm提现记录列表
     * @param pageNo
     * @param pageSize
     * @param customParams
     * @return
     */
    Pagination<CRMWithdrawVO> getCRMWithdrawListPaging(int pageNo, int pageSize, Map<String, Object> customParams);
    
    /**
     * 导出crm提现记录列表
     * @param response
     */
    void exportCRMWithdrawList(HttpServletResponse response, Map<String, Object> customParams);
}
