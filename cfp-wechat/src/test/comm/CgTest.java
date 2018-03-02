package comm;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.OpenAccount4ApiPersonalDataSource;
import com.external.deposites.model.response.CommonOpenAccount4ApiResponse;
import com.external.deposites.utils.HfUtils;
import com.xt.cfp.core.pojo.CgBank;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.service.impl.CgBizService;
import com.xt.cfp.core.service.impl.StoreServiceImpl;
import com.xt.cfp.core.service.impl.SundryDataQuery;
import com.xt.cfp.core.util.Pagination;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Map;

/**
 * <pre>
 * TODO
 * </pre>
 *
 * @author LUYANFENG @ 2017/12/4
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:spring-servlet-resource.xml"})
@WebAppConfiguration
public class CgTest {

    @Autowired
    private SundryDataQuery apiDataQuery;
    @Autowired
    private StoreServiceImpl storeService;
    @Autowired
    private CgBizService cgBizService;

    @Autowired
    private IhfApi hfApi;
    @Test
    public void test() {
        CgBank bankInfo = cgBizService.getBankInfo("0102", CgBank.IdTypeEnum.PERSON);
        System.out.println("--------------\n" + bankInfo);
    }

    @Test
    public void testOldUserOpenAccount() throws Exception {
        for (int i = 0; i < 100_000_000; i++) {
            Pagination<UserInfoExt> pagination = cgBizService.oldUserOpenAccount(1 + i, 100);
            if(pagination.getRows().isEmpty()){
                break;
            }
            for (UserInfoExt userInfoExt : pagination.getRows()) {

                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByName("javascript");
                Reader file = new FileReader("resources/js/newjs/bankCityAddress.js");
                engine.eval(file);

                Object cityAddressArr = engine.get("cityAddressArr");
                System.out.println(cityAddressArr);


//                OpenAccount4ApiPersonalDataSource dataSource = new OpenAccount4ApiPersonalDataSource();
//                {
//                    dataSource.setCust_nm(userInfoExt.getRealName());
//                    dataSource.setCertif_tp(HfUtils.CertifTpEnum.ID_CARD.code());
//                    dataSource.setCertif_id(userInfoExt.getIdCard());
//                    dataSource.setMobile_no(userInfoExt.getMobileNo());
//                    //
//                    dataSource.setCity_id("1000");
//                    dataSource.setParent_bank_id("0305"); // 民生
//                    dataSource.setCapAcntNo("6226221205600024");
//                    dataSource.setBank_nm("夺在");
//                    dataSource.setLpassword("25d55ad283aa400af464c76d713c07ad");
//                    dataSource.setPassword("25d55ad283aa400af464c76d713c07ad");
//
//                }
//                CommonOpenAccount4ApiResponse commonOpenAccount4ApiResponse = null;
//                try {
//                    commonOpenAccount4ApiResponse = hfApi.openAccount(dataSource);
//                    System.out.println("is ok = " + commonOpenAccount4ApiResponse.verifySign(false));
//                } catch (HfApiException e) {
//                    e.printStackTrace();
//                }
            }
        }

    }

    @Test
    public void testJS() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        Reader file = new FileReader("/Users/hanpeng/IdeaWorkspace/git/bank-deposits/cfp-wechat/src/main/webapp/resources/js/newjs/bankCityAddress.js");
        engine.eval(file);

        ScriptObjectMirror cityAddressArr = (ScriptObjectMirror) engine.get("cityAddressArr");
        if(cityAddressArr.isArray()){
            for (Map.Entry<String, Object> entry : cityAddressArr.entrySet()) {
                Object area = entry.getValue();
            }

        }
        System.out.println(cityAddressArr);
    }
}
