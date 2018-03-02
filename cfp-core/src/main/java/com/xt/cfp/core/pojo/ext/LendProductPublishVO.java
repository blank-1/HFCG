package com.xt.cfp.core.pojo.ext;

import java.util.Date;

import com.xt.cfp.core.pojo.LendProductPublish;

public class LendProductPublishVO extends LendProductPublish {

	private Date now ;
	
	protected String timeLimitType;
	protected int timeLimit;

	public Date getNow() {
		return now;
	}

	public void setNow(Date now) {
		this.now = now;
	}

	public String getTimeLimitType() {
		return timeLimitType;
	}

	public void setTimeLimitType(String timeLimitType) {
		this.timeLimitType = timeLimitType;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	
}
