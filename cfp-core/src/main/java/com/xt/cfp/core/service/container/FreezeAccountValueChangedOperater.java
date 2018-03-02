package com.xt.cfp.core.service.container;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserAccountErrorCode;
import com.xt.cfp.core.pojo.UserAccount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 冻结
 * Created by luqinglin on 2015/6/16.
 */
@Component("FREEZE")
public class FreezeAccountValueChangedOperater extends AccountValueChangedOperater {
    @Override
    public UserAccount execute(UserAccount userAccount, AccountValueChanged accountValueChanged) {
        //操作内容：将“冻结价值”+变更值，“可用价值”-变更值
        BigDecimal changeValue = accountValueChanged.getChangeValue();
        BigDecimal changeValue2 = accountValueChanged.getChangeValue2();
        //相减前检查会不会出现负数
        if (accountValueChanged.isNeedCheck()) {
            //可用价值
//            if (changeValue.compareTo(userAccount.getAvailValue()) > 0)
//                throw new SystemException(UserAccountErrorCode.ACCOUNT_BALANCE_AVAIVALUE_INSUFFICIENT).
//                        set("availValue", userAccount.getAvailValue()).
//                        set("changeValue", changeValue).set("userAccountId",userAccount.getAccId());
            //可用价值2
            if (changeValue2.compareTo(userAccount.getAvailValue2()) > 0)
                throw new SystemException(UserAccountErrorCode.ACCOUNT_BALANCE_AVAIVALUE_INSUFFICIENT).set("availValue2", userAccount.getAvailValue2()).set("changeValue2", changeValue2);  //可用价值
        }
        //冻结价值
        addToFrozeValue(userAccount, accountValueChanged);
        //可用价值
        subtractFromAvailValue(userAccount, accountValueChanged);

        return userAccount;
    }
}
