package com.xt.cfp.core.service.container;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserAccountErrorCode;
import com.xt.cfp.core.pojo.UserAccount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 冻结资金支出
 * Created by luqinglin on 2015/6/16.
 */
@Component("PAY_FROZEN")
@Deprecated
public class PayFrozenAccountValueChangedOperater extends AccountValueChangedOperater {
    @Override
    public UserAccount execute(UserAccount userAccount, AccountValueChanged accountValueChanged) {
        //操作内容：将“冻结价值”，“价值总额”减去变更值
        BigDecimal changeValue = accountValueChanged.getChangeValue();
        BigDecimal changeValue2 = accountValueChanged.getChangeValue2();
        //相减前检查会不会出现负数
        if (accountValueChanged.isNeedCheck()) {
            //冻结价值
//            if (changeValue.compareTo(userAccount.getFrozeValue()) > 0)
//                throw new SystemException(UserAccountErrorCode.ACCOUNT_BALANCE_FROZEVALUE_INSUFFICIENT).set("frozeValue", userAccount.getFrozeValue()).set("changeValue", changeValue);
            //冻结价值2
            if (changeValue2.compareTo(userAccount.getFrozeValue2()) > 0)
                throw new SystemException(UserAccountErrorCode.ACCOUNT_BALANCE_FROZEVALUE_INSUFFICIENT).set("frozeValue2", userAccount.getFrozeValue2()).set("changeValue2", changeValue2);  //可用价值
            //总价值
//            if (changeValue.compareTo(userAccount.getValue()) > 0)
//                throw new SystemException(UserAccountErrorCode.ACCOUNT_BALANCE_VALUE_INSUFFICIENT).set("value", userAccount.getValue()).set("changeValue", changeValue);
            //总价值2
            if (changeValue2.compareTo(userAccount.getValue2()) > 0)
                throw new SystemException(UserAccountErrorCode.ACCOUNT_BALANCE_VALUE_INSUFFICIENT).set("value2", userAccount.getValue2()).set("changeValue2", changeValue2);
        }
        //冻结价值
        subtractFromFrozeValue(userAccount, accountValueChanged);
        //价值总额
        subtractFromValue(userAccount, accountValueChanged);

        return userAccount;
    }
}
