package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.service.UserAccountOperateService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedOperater;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.ApplicationContextUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Created by yulei on 2015/6/12.
 */
@Service
@Transactional
public class UserAccountOperateServiceImpl implements UserAccountOperateService {

    @Autowired
    private UserAccountService userAccountService;

    @Override
    @Transactional
    public void execute(AccountValueChangedQueue accountValueChangedQueue) {
        Collection<Long> accountIds = accountValueChangedQueue.getAccountIdList();
        //对排过序的账户进行账户操作
        for (Long accountId : accountIds) {
            Collection<AccountValueChanged> accountValueChangeds = accountValueChangedQueue.getAccountValueChangedList(accountId);
            //锁定账户
            UserAccount userAccount = userAccountService.lock(accountId);
            //修改账户并记录操作后的历史记录
            for (AccountValueChanged accountValueChanged : accountValueChangeds) {
                AccountConstants.AccountOperateEnum aoe = AccountConstants.AccountOperateEnum.getAccountOperateByValue(accountValueChanged.getChangeType());
                AccountValueChangedOperater operater = (AccountValueChangedOperater) ApplicationContextUtil.getBean(aoe.name());
                operater.operate(accountValueChanged, userAccount);
            }
            //更新账户数据
            userAccountService.updateUserAccount(userAccount);
        }
    }
}
