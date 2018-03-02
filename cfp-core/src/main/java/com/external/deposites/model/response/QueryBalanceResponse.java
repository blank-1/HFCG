package com.external.deposites.model.response;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 * 查询余额
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/27
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryBalanceResponse extends AbstractResponse {

	private Set<QueryBalanceResponseItem> results = new HashSet<>();

	public Set<QueryBalanceResponseItem> getResults() {
		return results;
	}

	public void setResults(Set<QueryBalanceResponseItem> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "BalanceResponse{" + "results=" + results + "} " + super.toString();
	}
}
