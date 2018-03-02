package com.xt.cfp.core.service.container;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.pojo.UserAccountHis;
import com.xt.cfp.core.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by luqinglin on 2015/6/16.
 */
@Transactional
public abstract class AccountValueChangedOperater {

    @Autowired
    private UserAccountService userAccountService;

    /**
     * 资金操作
     */
    @Transactional
    public void operate(AccountValueChanged accountValueChanged, UserAccount userAccount) {
        //判断参数是否为null
        if (accountValueChanged == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("accountValueChanged", "null");

        //初始化账户历史信息
        UserAccountHis userAccountHis = initUserAccountHis(userAccount, accountValueChanged);
        //具体的账户操作
        userAccount = execute(userAccount, accountValueChanged);
        //记录修改后的账户信息，并保存
        UserAccountHis _userAccountHis = userAccountService.createUserAccountHis(fillChangeInfo(userAccount, userAccountHis));
        //回调
        CallBack callBack = accountValueChanged.getCallBack();
        if (callBack!=null)
            callBack.callBack(_userAccountHis);
    }

    /**
     * 装填变化前后的相关信息
     *
     * @param userAccountHis
     * @return
     */
    private UserAccountHis fillChangeInfo(UserAccount userAccount, UserAccountHis userAccountHis) {
        //判断参数是否为null
        if (userAccount == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userAccount", "null");

        if (userAccountHis == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userAccountHis", "null");

        //修改后数据保存
        userAccountHis.setValueAfter(userAccount.getValue());
        userAccountHis.setValueAfter2(userAccount.getValue2());
        userAccountHis.setAvailValueAfter(userAccount.getAvailValue());
        userAccountHis.setAvailValueAfter2(userAccount.getAvailValue2());
        userAccountHis.setFrozeValueAfter(userAccount.getFrozeValue());
        userAccountHis.setFrozeValueAfter2(userAccount.getFrozeValue2());
        userAccountHis.setStatusAfter(userAccount.getAccStatus());
        return userAccountHis;
    }

    /**
     * 根据变更数据初始化账户历史信息
     *
     * @param userAccountHistory
     * @param accountValueChanged
     * @return
     */
    private UserAccountHis initUserAccountHis(UserAccount userAccountHistory, AccountValueChanged accountValueChanged) {
        //判断参数是否为null
        if (accountValueChanged == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("accountValueChanged", "null");

        if (userAccountHistory == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userAccountHistory", "null");

        UserAccountHis userAccountHis = new UserAccountHis();
        userAccountHis.setAccId(accountValueChanged.getAccId());
        userAccountHis.setChangeTime(accountValueChanged.getChangeTime());
        userAccountHis.setFromId(accountValueChanged.getFromId());
        userAccountHis.setFromType(accountValueChanged.getFromType());
        userAccountHis.setOwnerId(accountValueChanged.getOwnerId());
        userAccountHis.setOwnerType(accountValueChanged.getOwnerType());
        userAccountHis.setDesc(accountValueChanged.getDesc());
        userAccountHis.setChangeValue(accountValueChanged.getChangeValue());
        userAccountHis.setChangeValue2(accountValueChanged.getChangeValue2());
        userAccountHis.setBusType(accountValueChanged.getBusType());
        userAccountHis.setChangeType(accountValueChanged.getChangeType());
        userAccountHis.setIsVisible(accountValueChanged.getIsVisbled());
        //修改前数据保存
        userAccountHis.setAccTypeCode(userAccountHistory.getAccTypeCode());
        userAccountHis.setValueBefore(userAccountHistory.getValue());
        userAccountHis.setValueBefore2(userAccountHistory.getValue2());
        userAccountHis.setAvailValueBefore(userAccountHistory.getAvailValue());
        userAccountHis.setAvailValueBefore2(userAccountHistory.getAvailValue2());
        userAccountHis.setFrozeValueBefore(userAccountHistory.getFrozeValue());
        userAccountHis.setFrozeValueBefore2(userAccountHistory.getFrozeValue2());
        userAccountHis.setStatusBefore(userAccountHistory.getAccStatus());
        return userAccountHis;
    }

    /**
     * 变更值加至总价值
     *
     * @param userAccount
     * @param accountValueChanged
     * @return
     */
    public UserAccount addToValue(UserAccount userAccount, AccountValueChanged accountValueChanged) {
        userAccount.setValue(userAccount.getValue().add(accountValueChanged.getChangeValue()));
        userAccount.setValue2(userAccount.getValue2().add(accountValueChanged.getChangeValue2()));
        return userAccount;
    }

    /**
     * 变更值加至可用价值
     *
     * @param userAccount
     * @param accountValueChanged
     * @return
     */
    public UserAccount addToAvailValue(UserAccount userAccount, AccountValueChanged accountValueChanged) {
        userAccount.setAvailValue(userAccount.getAvailValue().add(accountValueChanged.getChangeValue()));
        userAccount.setAvailValue2(userAccount.getAvailValue2().add(accountValueChanged.getChangeValue2()));
        return userAccount;
    }

    /**
     * 变更值加至冻结价值
     *
     * @param userAccount
     * @param accountValueChanged
     * @return
     */
    public UserAccount addToFrozeValue(UserAccount userAccount, AccountValueChanged accountValueChanged) {
        userAccount.setFrozeValue(userAccount.getFrozeValue().add(accountValueChanged.getChangeValue()));
        userAccount.setFrozeValue2(userAccount.getFrozeValue2().add(accountValueChanged.getChangeValue2()));
        return userAccount;
    }

    /**
     * 总价值减去变更值
     *
     * @param userAccount
     * @param accountValueChanged
     * @return
     */
    public UserAccount subtractFromValue(UserAccount userAccount, AccountValueChanged accountValueChanged) {
        userAccount.setValue(userAccount.getValue().subtract(accountValueChanged.getChangeValue()));
        userAccount.setValue2(userAccount.getValue2().subtract(accountValueChanged.getChangeValue2()));
        return userAccount;
    }

    /**
     * 可用价值减去变更值
     *
     * @param userAccount
     * @param accountValueChanged
     * @return
     */
    public UserAccount subtractFromAvailValue(UserAccount userAccount, AccountValueChanged accountValueChanged) {
        userAccount.setAvailValue(userAccount.getAvailValue().subtract(accountValueChanged.getChangeValue()));
        userAccount.setAvailValue2(userAccount.getAvailValue2().subtract(accountValueChanged.getChangeValue2()));
        return userAccount;
    }

    /**
     * 冻结价值减去变更值
     *
     * @param userAccount
     * @param accountValueChanged
     * @return
     */
    public UserAccount subtractFromFrozeValue(UserAccount userAccount, AccountValueChanged accountValueChanged) {
        userAccount.setFrozeValue(userAccount.getFrozeValue().subtract(accountValueChanged.getChangeValue()));
        userAccount.setFrozeValue2(userAccount.getFrozeValue2().subtract(accountValueChanged.getChangeValue2()));
        return userAccount;
    }


    /**
     * 修改账户具体操作
     *
     * @param userAccount
     * @param accountValueChanged
     * @return
     */
    public abstract UserAccount execute(UserAccount userAccount, AccountValueChanged accountValueChanged);

}
