package interfaces;

import com.xt.cfp.core.pojo.CustomerCard;
import com.xt.cfp.core.service.impl.CgBizService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <pre>
 * TODO
 * </pre>
 *
 * @author LUYANFENG @ 2017/12/12
 */
public class ServiceTest extends ServiceTestWrapper{
    @Autowired
    private CgBizService cgBizService;
    @Test
    public void test(){
        CustomerCard currentCard = cgBizService.findCurrentCard(9765L);
        System.out.println(currentCard);
    }
}
