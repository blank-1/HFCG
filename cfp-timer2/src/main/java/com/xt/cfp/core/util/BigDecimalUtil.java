package com.xt.cfp.core.util;

import java.math.BigDecimal;

public class BigDecimalUtil {
    private static final String UNIT = "万千佰拾亿千佰拾万千佰拾元角分";

    private static final String DIGIT = "零壹贰叁肆伍陆柒捌玖";

    private static final double MAX_VALUE = 9999999999999.99D;


    public static synchronized int compareTo(BigDecimal source, BigDecimal dest, int scale) throws Exception {
        return compareTo(source, dest, false, scale);
    }

    public static synchronized int compareTo(BigDecimal source, BigDecimal dest, boolean checked, int scale) throws Exception {

		return compareTo(source,dest,checked,scale, BigDecimal.ROUND_CEILING);

	}
	
	public static synchronized int compareTo(BigDecimal source,BigDecimal dest,boolean checked,int scale,int roundType) throws Exception {

		if(null==source || null==dest)
			throw new IllegalArgumentException();
		if(checked){
			BigDecimal theSource = source.setScale(scale, roundType);
            BigDecimal theDest = dest.setScale(scale, roundType);
            return theSource.compareTo(theDest);
        } else {
            return source.compareTo(dest);
        }

    }

    /**
     * 进位处理
     * @param source
     * @param scale
     * @return
     */
    public static synchronized BigDecimal up(BigDecimal source, int scale) {

        if (null == source) {
            return null;
        }
        source = source.stripTrailingZeros();
        if (source.scale() <= scale) {
            return source;
        }
        return source.setScale(scale, BigDecimal.ROUND_UP);
    }

    /**
     * 降位处理
     * @param source
     * @param scale
     * @return
     */
    public static synchronized BigDecimal down(BigDecimal source,int scale) {
        if (null == source) {
            return null;
        }
//        source = source.stripTrailingZeros();
        if (source.scale() <= scale) {
            return source;
        }
        return source.setScale(scale, BigDecimal.ROUND_DOWN);
    }

    /**
     * 计算unit在源数据上的最大公约数
     *
     * @param source 源数据
     * @param unit
     * @return
     */
    public static int getGCM(double source, int unit) {
        int result = 0;
        double unitCount = source / unit;
        for (int i = 0; i <= unitCount; i++) {
            if (source >= unit) {
                source -= unit;
                result+=unit;
            }
        }
        return result;
    }

    public static String change(double v) {
        if (v < 0 || v > MAX_VALUE)
            return "参数非法!";
        long l = Math.round(v * 100);
        if (l == 0)
            return "零元整";
        String strValue = l + "";
        // i用来控制数
        int i = 0;
        // j用来控制单位
        int j = UNIT.length() - strValue.length();
        String rs = "";
        boolean isZero = false;
        for (; i < strValue.length(); i++, j++) {
            char ch = strValue.charAt(i);

            if (ch == '0') {
                isZero = true;
                if (UNIT.charAt(j) == '亿' || UNIT.charAt(j) == '万' || UNIT.charAt(j) == '元') {
                    rs = rs + UNIT.charAt(j);
                    isZero = false;
                }
            } else {
                if (isZero) {
                    rs = rs + "零";
                    isZero = false;
                }
                rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);
            }
        }

        if (!rs.endsWith("分")) {
            rs = rs + "整";
        }
        rs = rs.replaceAll("亿万", "亿");
        return rs;
    }


    public static void main(String[] args) {
        System.out.println(BigDecimalUtil.getGCM(200010,50));
    }

}
