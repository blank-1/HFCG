package comm;

import com.external.deposites.model.response.QueryBalanceResponse;
import com.external.deposites.utils.SecurityUtils;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.ApplicationContextUtil;
import com.xt.cfp.core.util.JsonUtil;
import com.xt.cfp.core.util.XMLUtil;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * <pre>
 * TODO
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/21
 */
public class JdkTest {
    @Test
    public void test1() throws IOException {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<ap>")
                .append("<plain>")
                .append("<resp_code>0000</resp_code> ")
                .append("<resp_desc>响应消息</resp_desc>")
                .append("<mchnt_cd>0002900F0040281</mchnt_cd>")
                .append("<mchnt_txn_ssn>A000000000001</mchnt_txn_ssn> ")
                .append("</plain> ")
                .append("<signature>Sx0234nsdi123basd9asdn91f</signature>")
                .append("</ap>");

        String json = XMLUtil.xml2json(xml.toString(), false);
        System.out.println(json);
        HashMap hashMap = JsonUtil.toBean(json, HashMap.class);
        System.out.println(hashMap);
    }

    private JedisPool jedisPool;

    @Before
    public void begin() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(2);
        config.setMaxIdle(100);
        config.setMaxWait(100);
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool(config, "localhost", 6379, 2000, "123456");

    }

    @Test
    public void test2() {
//        CollectionUtils.
        // 1
//        Jedis jedis = new Jedis("localhost", 6379);
//        String auth = jedis.auth("123456");

        // 2
        Jedis jedis = jedisPool.getResource();
        Jedis jedis2 = jedisPool.getResource();
        Jedis jedis3 = jedisPool.getResource();

        Set<String> keys = jedis2.keys("*");

        for (String key : keys) {
            System.out.println(key);
        }
    }

    @Test
    public void test3() {
        String xml = "<text>" +
                "<resp_code>返回码</resp_code> <mchnt_cd>商户代码</mchnt_cd> <mchnt_txn_ssn>商户流水号</mchnt_txn_ssn> " +
                "<results>" +
                "<result>" +
                "<user_id>用户名</user_id> " +
                "</result>"
                + "<result>" +
                "<user_id>用户名d</user_id> " +
                "</result>" +
                "</results>" +
                "</text>";

        String results = XMLUtil.xml2json(xml, "results");
        QueryBalanceResponse queryBalanceResponse = JsonUtil.toBean(results, QueryBalanceResponse.class);
        System.out.println(queryBalanceResponse);

    }

    @Test
    public void testRedis() {
        System.out.println("-------------------");
        RedisCacheManger redisCacheManger = ApplicationContextUtil.getBean(RedisCacheManger.class);
        Object redisCacheInfo = redisCacheManger.getRedisCacheInfo("address:province");
        System.out.println(redisCacheInfo);

        boolean setOk = redisCacheManger.setRedisCacheInfo("test:abc", "admin", 60);
        assert setOk : "set failure.";
        String obj = redisCacheManger.getRedisCacheInfo("test:abc");
        System.out.println(obj);
        System.out.println("-------------------");
    }

    @Test
    public void test() {
        String s = "<plain><resp_code>5343</resp_code><mchnt_cd>0003310F0352406</mchnt_cd><mchnt_txn_ssn>3cb4f00fce924ca8b819f79c7565c</mchnt_txn_ssn></plain>";
        String n = "uVOw9EXiSmV+s5UGyeySNJQl6cmSy6JoExabsen6IY23CH1IGPbbZDedADwVklhIg/5qSS//Sq2bPqzbA7ySolDP7sU52M9ct1RyyBinROFUn2zLh5zqpX/6AbZWWWRLHzEf4MG+zZ2WM0KZ9Ok7i70MUgf4N0QoLqzlG4pf1h0=";
        System.out.println(SecurityUtils.verifySign(s, n));
    }

    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(2));
        System.out.println(Integer.toBinaryString(3));
        System.out.println(Integer.toBinaryString(4));
        int[] array = {2, 3, 4, 4, 3, 5, 6, 6, 5};
        int v = 0;
        for (int i = 0; i < array.length; i++) {
            v ^= array[i];
//            System.out.println("v=" + v + "     array=" + array[i] + "     与运算=" + (v ^ 2));
//            erjinzhi(array[i]);
        }

        System.out.println("只出现一次的数是:" + v);


    }

    public static void erjinzhi(int number) {
        //1.假设现在有一个int为20，需要转换为二进制输出
        //2.需要一个长度为32的int数组来存储结果二进制
        int[] bit = new int[32];
        //3.循环，把原始数除以2取得余数，这个余数就是二进制数，原始的数等于商。
        //商如果不能再除以二，结束循环。
        for (int i = 0; number > 1; i++) {
            //取得除以2的余数
            int b = number % 2;
            //数字赋值为除以2的商
            number = number / 2;
            bit[i] = b;
            if (number < 2) {
                //已经不能再把数除以2，就把上直接放到数组的下一位
                bit[i + 1] = number;
            }
            System.out.print(number);
        }
    }


}
