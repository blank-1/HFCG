package com.xt.cfp.core.service;

import com.xt.cfp.core.constants.BondSourceStatus;
import com.xt.cfp.core.constants.UserStatus;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.BondSourceUser;
import com.xt.cfp.core.pojo.ext.BondSourceExt;
import com.xt.cfp.core.util.Pagination;

import java.util.List;
import java.util.Map;

/**
 * Created by luqinglin on 2015/6/18.
 */
public interface BondSourceService {

    /**
     * 添加债券渠道
     * @param bondSource
     * @return
     */
    public BondSource addBondSource(BondSource bondSource);

    /**
     * 修改债券渠道状态
     * @param bondSourceId
     * @param bondSourceStatus
     */
    public void changeBondSourceStatus(Long bondSourceId,BondSourceStatus bondSourceStatus);

    /**
     * 根据id查询
     * @param bondSourceId
     * @return
     */
    public BondSource getBondSourceByBondSourceId(Long bondSourceId);
    /**
     * 根据id查询详情
     * @param bondSourceId
     * @return
     */
    public BondSourceExt getBondSourceDetailByBondSourceId(Long bondSourceId);

    /**
     * 修改渠道信息
     * @param bondSource
     */
    public void updateBondSource(BondSource bondSource);

    /**
     * 获取该渠道下的所有原始债权人
     * @param bondSourceId
     * @return
     */
    public List<BondSourceUser> getBondSourceUserBySourceId(Long bondSourceId);

    /**
     * 获取对应的原始债券人
     * @param userSourceId
     * @return
     */
    public BondSourceUser getBondSourceUserByUserSourceId(Long userSourceId);

    /**
     * 添加原始债权人
     * @return
     */
    public BondSourceUser addBondSourceUser(BondSourceUser bondSourceUser);

    /**
     * 修改原始债权人状态
     * @param userId
     * @param userStatus
     */
    public void changeBondSourceUserStatus(Long userId,UserStatus userStatus);

    /**
     * 修改原始债权人信息
     * @param bondSourceUser
     */
    public void updateBondSourceUser(BondSourceUser bondSourceUser);

    /**
     * 渠道充值
     * @param rechargeOrder
     * @return
     */
    public RechargeOrder recharge(RechargeOrder rechargeOrder);

    /**
     * 获取分页渠道信息
     *
     * @param pageNum
     * @param pageSize
     * @param bondSource
     * @param customParams
     * @return
     */
    Pagination<BondSourceExt> getBondSourcePaging(int pageNum, int pageSize, BondSource bondSource, Map<String, Object> customParams);

    /**
     * 获取分页原始债券人信息
     * @param pageNo
     * @param pageSize
     * @param bondSourceUser
     * @param customParams
     * @return
     */
    Pagination<BondSourceUser> getBondSourceUserPaging(int pageNo, int pageSize, BondSourceUser bondSourceUser, Map<String, Object> customParams);



    /**
     * 提现申请
     * @param withDraw
     */
    WithDraw withDraw(WithDraw withDraw);
    
    /**
     * 获取所有渠道信息
     */
    List<BondSource> getAllBondSource();

    /**
     * 根据ID，加载一条原始债券人渠道
     * @param userSourceId 原始债权人ID
     */
    BondSourceUser getBondSourceUserById(Long userSourceId);
    
    /**
     * 根据渠道ID，获取所有原始债权人，条件：原始债权人对应的用户，必须有卡
     * @param bondSourceId 渠道ID
     */
    List<BondSourceUser> getAllBondSourceUserBySourceId(Long bondSourceId);
    /**
     * 原始债权人列表
     * @param bondSourceUser 
     * @param
     * @date 2015年10月10日 下午5:44:10 
     * @auhthor wangyadong
     */
	public Pagination<BondSourceUser> getBondSourceUserListPaging(int pageNo, int pageSize, BondSourceUser bondSourceUser, Map<String, Object> customParams);
    /**
     * 原始债权人列表
     * @param bondSourceUser
     * @param
     * @date 2015年10月10日 下午5:44:10
     * @auhthor wangyadong
     */
	public Pagination<BondSourceUser> getBondSourceUserSelectPaging(int pageNo, int pageSize, BondSourceUser bondSourceUser, Map<String, Object> customParams);
   /**
    * @param userId
    * @param bondSourceId
    * @保存渠道和中间人多对多的关系
    * @date 2015年10月12日 上午11:33:56 
    * @auhthor wangyadong
    */
	public String addBondSourceUserNewRelation(String userId, Long bondSourceId);

}
