package com.xt.cfp.core.context;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ren yulin on 15-6-30.
 */
public class ParaContext {

    public Map<String,Object> map = new HashMap<String,Object>();

    public void put(String key, Object value) {
        if (map.containsKey(key))
            throw new SystemException(ValidationErrorCode.ERROR_KEY_DUPLICATE).set("key", key);
        map.put(key,value);
    }

    public void replace(String key,Object value) {
        map.put(key,value);
    }

    public <T> T get(String key) {
        return (T) map.get(key);
    }
}
