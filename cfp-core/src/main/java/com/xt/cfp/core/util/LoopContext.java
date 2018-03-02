package com.xt.cfp.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 循环执行的上下文，记录诸如执行次数等自定义变量
 * User: yulei
 * Date: 14-5-7
 * Time: 上午11:50
 */
public class LoopContext {

    /**
     * 初始循环次数
     */
    protected int startLoopTimes;

    /**
     * 当前循环次数
     */
    protected int currentLoopTimes;

    /**
     * 最大循环次数
     */
    protected int maxLoopTimes;

    /**
     * 自定义参数
     */
    protected Map customParams;

    protected LoopContext() {}

    protected LoopContext(int startLoopTimes, int maxLoopTimes) {
        this.startLoopTimes = startLoopTimes;
        this.currentLoopTimes = startLoopTimes;
        this.maxLoopTimes = maxLoopTimes;
        this.customParams = new HashMap();
    }

    /**
     * 判断是否可以进入下次循环
     * @return
     */
    public boolean next() {
        if (this.currentLoopTimes + 1 > maxLoopTimes)
            return false;
        else
            this.currentLoopTimes++;

        return true;
    }

    /**
     * 批量添加自定义参数
     * @param map
     */
    public void addParams(Map map) {
        this.customParams.putAll(map);
    }

    /**
     * 添加自定义参数
     * @param key
     * @param value
     */
    public void addParam(Object key, Object value) {
        this.customParams.put(key, value);
    }

    /**
     * 初始化一个LoopContext
     * @return
     */
    public static LoopContext newOne(int maxLoopTimes) {
        return new LoopContext(1, maxLoopTimes);
    }

    public int getCurrentLoopTimes() {
        return currentLoopTimes;
    }

    public void setCurrentLoopTimes(int currentLoopTimes) {
        this.currentLoopTimes = currentLoopTimes;
    }

    public int getStartLoopTimes() {
        return startLoopTimes;
    }

    public void setStartLoopTimes(int startLoopTimes) {
        this.startLoopTimes = startLoopTimes;
    }

    public int getMaxLoopTimes() {
        return maxLoopTimes;
    }

    public void setMaxLoopTimes(int maxLoopTimes) {
        this.maxLoopTimes = maxLoopTimes;
    }

    public Map getCustomParams() {
        return customParams;
    }

    public void setCustomParams(Map customParams) {
        this.customParams = customParams;
    }
}
