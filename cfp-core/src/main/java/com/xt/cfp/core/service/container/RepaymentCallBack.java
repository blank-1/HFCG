package com.xt.cfp.core.service.container;

import com.xt.cfp.core.pojo.SupplementaryDifference;
import com.xt.cfp.core.pojo.UserAccountHis;
import com.xt.cfp.core.service.RightsPrepayDetailService;
import com.xt.cfp.core.util.ApplicationContextUtil;

import java.util.HashMap;

/**
 * Created by lenovo on 2015/10/26.
 */
public class RepaymentCallBack implements CallBack {

    public RepaymentCallBack(SupplementaryDifference sd) {
        this.sd = sd;
    }

    private SupplementaryDifference sd = new  SupplementaryDifference();

    @Override
    public void callBack(UserAccountHis userAccountHis) {
        if (sd==null)
            return ;
        RightsPrepayDetailService rightsPrepayDetailService = ApplicationContextUtil.getBean("rightsPrepayDetailServiceImpl");
        sd.setHisId(userAccountHis.getHisId().longValue());
        rightsPrepayDetailService.recordSupplementaryDifference(sd);

    }

    public SupplementaryDifference getSd() {
        return sd;
    }

    public void setSd(SupplementaryDifference sd) {
        this.sd = sd;
    }

}
