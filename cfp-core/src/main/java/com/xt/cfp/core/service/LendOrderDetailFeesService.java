package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.LendOrderFeesDetail;

import java.util.List;
import java.util.Map;

/**
 * Created by ren yulin on 15-7-8.
 */
public interface LendOrderDetailFeesService {
    List<LendOrderFeesDetail> getDetailByLendOrderIdAndSectionCode(Long lendOrderId, int sectionCode);

    void insert(LendOrderFeesDetail lendOrderFeesDetail);

    void update(Map orderFeesDetailMap);
}
