package com.xt.cfp.core.service.container;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.util.ReflectionUtil;
import com.xt.cfp.core.util.ValidationUtil;

import java.util.*;

/**
 * created by yulei on 2015/6/12.
 * 此类用于执行账户价值变更的操作。
 * 由于一个业务操作可能会引起多个账户的价值变更，而多个账户的操作在未排序的情况下很可能造成锁表，因此需要本类作为价值变更操作的载体，将各个操作按照一定的顺序排序后，由@see com.xt.cfp.core.service.UserAccountOperateService处理
 */
public class AccountValueChangedQueue {

    private final Map<Long, List<AccountValueChanged>> map = new TreeMap<Long, List<AccountValueChanged>>();

    /**
     * 向账户价值变更事件队列中添加事件
     *
     * @param accountValueChanged
     */
    public void addAccountValueChanged(AccountValueChanged accountValueChanged) throws SystemException {
        //判断参数是否为null
        if (accountValueChanged == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("accountValueChanged", "null");

        checkAccountValueChanged(accountValueChanged);

        List<AccountValueChanged> accountValueChangeds = map.get(accountValueChanged.getAccId());
        if (accountValueChangeds != null)
            accountValueChangeds.add(accountValueChanged);
        else {
            map.put(accountValueChanged.getAccId(), new ArrayList<AccountValueChanged>());
            map.get(accountValueChanged.getAccId()).add(accountValueChanged);
        }
    }

    /**
     * 检查对象属性值是否为空
     *
     * @param accountValueChanged
     */
    private void checkAccountValueChanged(AccountValueChanged accountValueChanged) throws SystemException {
        List<String> filedNames = ReflectionUtil.getFiledName(accountValueChanged);
        ValidationUtil.checkRequiredFieldNotEmpty(accountValueChanged, filedNames.toArray(new String[filedNames.size()]));
    }

    /**
     * 获取排序好的账户价值变更事件队列
     *
     * @return
     */
    public Collection<AccountValueChanged> getAccountValueChangedList(Long accountId) {
        return map.get(accountId);
    }

    /**
     * 获取排好的账户队列
     *
     * @return
     */
    public Collection<Long> getAccountIdList() {
        return map.keySet();
    }

}
