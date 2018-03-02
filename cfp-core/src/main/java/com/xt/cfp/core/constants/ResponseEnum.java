package com.xt.cfp.core.constants;

public enum ResponseEnum{
	Success(1), Failue(2), Partial_success(0);

	private int value;

	private ResponseEnum(int value) {
		this.value = value;
	}
	
	public static ResponseEnum valueOf(int value) {
		ResponseEnum[] values = ResponseEnum.values();
        for (ResponseEnum anEnum:values) {
            if (anEnum.getValue() == (value)) {
                return anEnum;
            }
        }
		return null;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
}
