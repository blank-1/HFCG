/**
 * 
 */
package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.RateLendOrder;

/**
 * @author 1
 *
 */
public class RateLendOrderVO extends RateLendOrder {

	/**
	 * 
	 */
	public RateLendOrderVO() {
		 
	}
	
	private Long creditRightsId ;

	public Long getCreditRightsId() {
		return creditRightsId;
	}

	public void setCreditRightsId(Long creditRightsId) {
		this.creditRightsId = creditRightsId;
	}
	

}
