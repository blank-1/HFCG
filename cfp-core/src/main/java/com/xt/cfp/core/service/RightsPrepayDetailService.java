package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.RightsPrepayDetail;
import com.xt.cfp.core.pojo.SupplementaryDifference;

import java.util.Map;

/**
 * Created by ren yulin on 15-7-8.
 */
public interface RightsPrepayDetailService {
    void insert(RightsPrepayDetail rightsPrepayDetail);

    RightsPrepayDetail findByNewRightsId(long newRightsId);

    void update(Map prepayDetailMap);

    /**
     * 记录平台误差
     * @param sd
     */
    void recordSupplementaryDifference(SupplementaryDifference sd);
}
