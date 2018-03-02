package com.xt.cfp.core.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class IncludeField implements ExclusionStrategy{

	private String[] fieldNames;
	
	public IncludeField(String...fieldNames) {
		this.fieldNames = fieldNames;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		if (f != null) {
			for (String fieldName : fieldNames) {
				if (f.getName().equals(fieldName)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

}
