package com.external.deposites.model.fydatasource;

import java.util.HashSet;
import java.util.Set;

import com.external.deposites.model.response.AbstractResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionQueryResponse extends AbstractResponse{

	private static final long serialVersionUID = -4536226470866933004L;

	private String busi_tp; //查询类型
	
	private String total_number;//总数
	
    private Set<TransactionQueryResponseIteam> results = new HashSet<>();

	public Set<TransactionQueryResponseIteam> getResults() {
		return results;
	}

	public void setResults(Set<TransactionQueryResponseIteam> results) {
		this.results = results;
	}

	public String getBusi_tp() {
		return busi_tp;
	}

	public void setBusi_tp(String busi_tp) {
		this.busi_tp = busi_tp;
	}

	public String getTotal_number() {
		return total_number;
	}

	public void setTotal_number(String total_number) {
		this.total_number = total_number;
	}
    
    
}
