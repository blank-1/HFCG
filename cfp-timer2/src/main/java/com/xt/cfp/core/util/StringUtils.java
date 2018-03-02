package com.xt.cfp.core.util;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ReflectionErrorCode;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yu on 14-3-2.
 */
public class StringUtils {
    public static SimpleDateFormat yyyyMMddHHmmssSSSFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    public static String getDateTimeRadomStr(int length) {
        return yyyyMMddHHmmssSSSFormat.format(new Date())+getRadomStr(length);
    }
    public static String getRadomStr(int length){
        StringBuilder result = new StringBuilder("");
        char[] allChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'X'};
        for (int i = 0; i < length; i++) {
            int index = new Random().nextInt(allChars.length);
            result.append(allChars[index]);
        }
        return result.toString();
    }

    public static String getRadomStrLower(int length){
        StringBuilder result = new StringBuilder("");
        char[] allChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        for (int i = 0; i < length; i++) {
            int index = new Random().nextInt(allChars.length);
            result.append(allChars[index]);
        }
        return result.toString();
    }


    /**
     * 判断字符串是否是汉字
     * @param c
     * @return
     */
    public static boolean isChineseChar(char c){
        // 如果字节数大于1，是汉字
        try {
            return String.valueOf(c).getBytes("GBK").length > 1;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 前端显示字符串加掩码
     * @param str
     * @return
     */
    public static String getJmName(String str) {
        if(org.apache.commons.lang.StringUtils.isEmpty(str)){
            return null;
        }
        int length = str.length();
        if (length<4)
            return str;
        //取出前两位
        String prefix = "";
        if(isChineseChar(str.charAt(0))){
            prefix = str.charAt(0)+"";
        }else{
            if (isChineseChar(str.charAt(1))){
                prefix = str.substring(0,2);
            }else{
                prefix = str.substring(0,2);
            }
        }
        //取出后两位
        String last = "";
        last = str.substring(str.length()-2);
        String mid = str.substring(2,length-2);
        if (org.apache.commons.lang.StringUtils.isEmpty(mid)){
            mid = "*";
            last = str.substring(str.length()-1);
        }else{
            String c = "";
            for (int i=0;i<mid.length();i++){
                c += "*";
            }
            mid = c;
        }
        return prefix+mid+last;
    }

    /**
     * 将一个字符串按照指定分隔符分割成特定类型的集合
     * @param src
     * @param c
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> Collection<T> splitToSpcType(String src, String c, Class<T> tClass) {
        List<T> re = null;

        try {
            String[] split = src.split(c);
            if (split.length > 0)
                re = new ArrayList<T>();
            Method valueOf = tClass.getDeclaredMethod("valueOf", String.class);
            for (String ss : split)
                re.add((T) valueOf.invoke(null, ss));

        } catch (Exception e) {
            throw SystemException.wrap(e, ReflectionErrorCode.CANNOT_SPLIT_TO_SPCTYPE).set("string", src).set("splitChar", c).set("spcType", tClass.getSimpleName());
        }

        return re;
    }

    /**
     * 判断一个字符串是否是整数或小数
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        return str.matches("[0-9]+((.)[0-9]+)?");
    }

    /**
     * 将String字符串转换为整数型List
     * @param str
     * @return
     */
    public static List<Integer> stringToIntegerList(String str){
        List<Integer> list = new ArrayList<Integer>();
        String a[] = str.split(",");
        for (int i = 0; i < a.length; i++) {
            list.add(Integer.parseInt(a[i]));
        }
        return list;
    }

    /**
     * 判断是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 将int转换为指定长度的字符串，前缀以0补位
     *
     * @param intValue
     * @param strLength
     * @return
     */
    public static String int2Str(int intValue, int strLength) {
        if (strLength > 0) {
            char padding = '0';
            StringBuilder result = new StringBuilder();
            String fromStr = String.valueOf(intValue);
            for (int i = fromStr.length(); i < strLength; i++) {
                result.append(padding);
            }
            result.append(fromStr);
            return result.toString();
        } else {
            return null;
        }
    }

    /**
     * 根据模板和参数转换成字符串
     * @param template
     * @param objects
     * @return
     */
    public static String t2s(String template, Object... objects) {
        return MessageFormat.format(template, objects);
    }

    /**
     * 根据身份证号获取生日
     * @param idCode 身份证号
     */
    public static String getBirthdayByIdCode(String idCode){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String bdStr = null;
        try {
            String y,m,d = "";
            if(null != idCode && idCode.length() > 14){
                y = idCode.substring(6, 10);
                m = idCode.substring(10, 12);
                d = idCode.substring(12, 14);
                Date bd = sdf.parse(y+"-"+m+"-"+d);
                bdStr = sdf.format(bd);
            }
        } catch (Exception e) {
            return null;
        }
        return bdStr;
    }

    /**
     * 根据身份证号获取性别(1男；0女)
     * @param idCode 身份证号
     */
    public static String getSexByByIdCode(String idCode){
        String sexStr = null;
        try {
            if(null != idCode && idCode.length() > 14){
                String nStr = "0";
                if(idCode.length() == 18){
                    nStr = idCode.substring(16, 17);
                }else if (idCode.length() == 15) {
                    nStr = idCode.substring(14, 15);
                }
                Integer n = Integer.valueOf(nStr);
                Integer nr = n%2;
                sexStr = String.valueOf(nr);
            }
        } catch (Exception e) {
            return null;
        }
        return sexStr;
    }

    /**
     * 字符串是否匹配正则规则
     * @param rexp 正则
     * @param str 字符串
     * @return 符合：true；不符：false
     */
    public static boolean isPattern(String rexp,String str){
        Pattern p = Pattern.compile(rexp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 获取银行卡号后4位
     * @param cardNo
     * @return
     */
    public static String getLast4CardNo(String cardNo){
        return getLastStr(cardNo,4);
    }

    /**
     * 获取字符串后几位
     * @param str
     * @param length
     * @return
     */
    public static String getLastStr(String str,int length){
        if(isNull(str))
            return null;
        if(str.length()<=length)
            return str;
        String shortNo = str.substring(str.length()-length);
        return shortNo;
    }

    /**
     * 获取字符串前几位
     * @param str
     * @param length
     * @return
     */
    public static String getPreStr(String str,int length){
        if(isNull(str))
            return null;
        if(str.length()<=length)
            return str;
        String shortNo = str.substring(0,length);
        return shortNo;
    }

    /**
     * 获取银行卡号前6位
     * @param cardNo
     * @return
     */
    public static String getPrefix6ardNo(String cardNo){
        return getPreStr(cardNo,6);
    }

    /**
     * 加密后的手机号
     * @param mobile
     * @return
     */
    public static String getEncryptMobileNo(String mobile){
        if(isNull(mobile))
            return null;

        String encryptMobileNo = "";
        boolean b = mobileNoValidate(mobile);
        if(b){
            encryptMobileNo = mobile.substring(3,8);
            mobile = mobile.replace(encryptMobileNo,"*****");
            return mobile;
        } else {
            return null;
        }
//        throw new SecurityException("手机号码格式错误");
    }

    /**
     * 加密后的身份证号
     * @author wangyadong
     * @param idCard
     * @time 2015年9月17日  9：34 am
     * @return
     */
    public static String getEncryptIdCard(String idCard){
        if(isNull(idCard))
            return null;

        String encryptMobileNo = "";
        boolean b = idCardNoValidate(idCard);
        if(b){
            //211  1211 9900  816  2214
            encryptMobileNo = idCard.substring(3,14);
            idCard = idCard.replace(encryptMobileNo,"*****");
            return idCard;
        } else {
            return null;
        }
//        throw new SecurityException("手机号码格式错误");
    }

    /**
     * 加密后的身份证号
     * @author wangyadong
     * @param idCard
     * @time 2015年9月17日  9：35 am
     * @return
     */
    private static boolean idCardNoValidate(String idCard) {
        //(^1[3|5|7|8][0-9]{9}$)
        String regex = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(idCard);
        boolean b = m.matches();
        return b;
    }
    /**
     * 生成六位数的随机验证码
     *
     * @return
     */
    public static String generateVerifyCode(){
        Random random = new Random();
        StringBuilder sRand = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
        }

        return sRand.toString();
    }

    /**
     * 生成N位的随机数
     * @param length 随机数长度
     */
    public static String generateRandomNumber(Integer length){
        Random random = new Random();
        StringBuilder sRand = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
        }
        return sRand.toString();
    }

    /**
     * 手机验证
     * @param mobileNo
     * @return
     */
    public static boolean mobileNoValidate(String mobileNo){
        String regex = "(^1[3|5|4|7|8][0-9]{9}$)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobileNo);
        boolean b = m.matches();
        return b;
    }


    public static String getIpAddr(HttpServletRequest request) {
        if (null == request) {
            return null;
        }

        String proxs[] = { "X-Forwarded-For", "Proxy-Client-IP",
                "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR" };

        String ip = null;

        for (String prox : proxs) {
            ip = request.getHeader(prox);
            if (org.apache.commons.lang.StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                continue;
            } else {
                break;
            }
        }

        if (org.apache.commons.lang.StringUtils.isBlank(ip)) {
            return request.getRemoteAddr();
        }

        return ip;
    }

    /**
     * 获取固定长度的userId  长度默认11位
     * @param userId
     * @return
     */
    public static String getFixLengthUserId(String userId){
        return getFixLengthUserId(userId,11);
    }

    /**
     * 获取固定长度的userId
     * @param userId
     * @param length
     * @return
     */
    public static String getFixLengthUserId(String userId,int length){
        String tmpString = userId;
        for (int i = userId.length(); i < length; i++) {
            tmpString = "0" + tmpString;
        }
        return tmpString;
    }

    /**
     * 获取当前时间戳（精确到秒）
     * @return
     */
    public static String getTimeStr(){
        long time = new Date().getTime();
        long result = time/1000L;
        return String.valueOf(result);
    }

    /**
     * 金额格式话
     * @param amount
     * @return
     */
    public static String getAmountStr(BigDecimal amount){
        return null;
    }


    /** *转换方法 */

    public static String parseMoney(String pattern,BigDecimal bd){
        DecimalFormat df=new DecimalFormat(pattern);
        return df.format(bd);
    }



    public static void main(String[] args) {
        System.out.println("idCard="+getEncryptIdCard("211121199008162214"));
        BigDecimal bd=new BigDecimal("123456789");
        System.out.println(parseMoney(",###,###",bd)); //out: 123,456,789

        System.out.println(parseMoney("##,####,###",bd)); //out: 123,456,789

        System.out.println(parseMoney("######,###",bd)); //out: 123,456,789
        System.out.println(isNumeric("10.99"));
        System.out.println(isNumeric("10.9"));
        System.out.println(isNumeric("10."));
        System.out.println(isNumeric("10.99.1"));
        System.out.println(isNumeric("10.99."));
        System.out.println(isNumeric("-10.99"));
        System.out.println(isNumeric("+10.99"));
        System.out.println(isNumeric("10.9933334"));
        System.out.println(isNumeric("1043434.9945432"));
        System.out.println(getFixLengthUserId("1891171751"));
        System.out.println(getPrefix6ardNo("111111111111119876"));
        System.out.println(InterestCalculation.getWithDrawFee(new BigDecimal("100")));
    }

}
