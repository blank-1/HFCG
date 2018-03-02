package com.xt.cfp.core.service.container;

import com.xt.cfp.core.pojo.UserAccount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 收入
 * Created by luqinglin on 2015/6/15.
 */
@Component("INCOM")
class IncomeAccountValueChangedOperater extends AccountValueChangedOperater {

    @Override
    public UserAccount execute(UserAccount userAccount, AccountValueChanged accountValueChanged) {
        //操作内容：将变更的值分别加到“价值总额”、“可用价值”两项里
        BigDecimal changeValue = accountValueChanged.getChangeValue();
        BigDecimal changeValue2 = accountValueChanged.getChangeValue2();
        //可用价值
        addToAvailValue(userAccount, accountValueChanged);
        //价值总额
        addToValue(userAccount, accountValueChanged);
        return userAccount;
    }

}
