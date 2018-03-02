package com.external.deposites.model.fyResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Results;

import com.external.deposites.model.response.AbstractResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <pre>
 * 查询交易
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/27
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FyTradeResponse extends AbstractResponse {

	private Set<FyTradeResponseItem> results = new HashSet<>();

	public Set<FyTradeResponseItem> getResults() {
		return results;
	}

	public void setResults(Set<FyTradeResponseItem> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "BalanceResponse{" + "results=" + results + "} " + super.toString();
	}
}
