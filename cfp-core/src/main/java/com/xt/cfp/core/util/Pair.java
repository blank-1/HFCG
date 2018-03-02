package com.xt.cfp.core.util;

/**
 * User: yulei
 * Date: 14-2-18
 * Time: 下午4:56
 */
public class Pair<K, V> {

    private K k;

    private V v;

    public Pair() {

    }

    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getK() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }
}
