package com.external.deposites.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/22
 */
public class PropertiesUtils {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);
    private static final ConcurrentHashMap<String, ResourceBundle> propertiesMap = new ConcurrentHashMap<>();


    private PropertiesUtils() {
    }

    public static ResourceBundle resource(String resourceFile) {
        ResourceBundle resourceBundle = propertiesMap.get(resourceFile);
        if (resourceBundle == null) {
            synchronized (PropertiesUtils.class) {
                if (resourceBundle == null) {
                    resourceBundle = ResourceBundle.getBundle(resourceFile);
                    logger.info("load new property file :{}.", resourceFile);
                }
            }
            propertiesMap.putIfAbsent(resourceFile, resourceBundle);
        }
        return resourceBundle;
    }

    public static String property(String resourceFile, String key) {
        try {
            ResourceBundle resource = resource(resourceFile);
            return resource.getString(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }
}
