package com.xt.cfp.core.pojo;

import java.util.Date;

public class CrmFranCustomer {
    private Long id;

    private Long customerId;

    private Long pCustomerId;

    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getpCustomerId() {
        return pCustomerId;
    }

    public void setpCustomerId(Long pCustomerId) {
        this.pCustomerId = pCustomerId;
    }


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}