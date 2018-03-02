/**
 * 
 */
package com.xt.cfp.core.pojo;

import java.io.Serializable;

/**
 * @author 1
 *
 */
public class WhiteTabs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Long userId ;
	
	private String source ;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	
}
