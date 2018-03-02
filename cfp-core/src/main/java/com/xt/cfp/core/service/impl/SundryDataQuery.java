package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CgBankBranch;
import com.xt.cfp.core.service.ISundryDataQuery;
import com.xt.cfp.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * TODO
 * </pre>
 *
 * @author LUYANFENG @ 2017/12/4
 */
@Service
public class SundryDataQuery implements ISundryDataQuery {
    @Autowired
    private MyBatisDao myBatisDao;

    /**
     * 查询银行支行
     */
    public List<CgBankBranch> queryBankBranch(String key) {
        CgBankBranch cgBankBranch = new CgBankBranch();
        if(StringUtils.isNull(key)){
            return Collections.emptyList();
        }
        cgBankBranch.setBranchName("%" + key.trim() + "%");
        return myBatisDao.getList("CG_BANK_BRANCH.queryBankBranch", cgBankBranch);
    }
}
