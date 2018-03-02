package com.xt.cfp.core.constants;

import java.util.Comparator;

import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;

public class LoanApplicationListVOComparator implements Comparator<LoanApplicationListVO> {

	@Override
	public int compare(LoanApplicationListVO o1, LoanApplicationListVO o2) {
		return (int)(o2.getAnnualRate().doubleValue()-o1.getAnnualRate().doubleValue());
	}

}
