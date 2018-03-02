package com.xt.cfp.web.controller.api;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.BalanceDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * <pre>
 * TODO
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/24
 */
@Controller
@RequestMapping(value = "/api/query")
public class QueryApiController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IhfApi ihfApi;

    @RequestMapping
    public Object home() {
        return "forward:/api/query/toQuery";
    }

    @RequestMapping("toQuery")
    public Object toQuery(Model model) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String nowDateStr = sdf.format(new Date());
        model.addAttribute("now", nowDateStr);
        return "api/toQuery";
    }

    /**
     * 查询
     */
    @ResponseBody
    @RequestMapping(value = "doQuery")
    public Object doQuery(BalanceDataSource dataSource) {
        try {
            return ihfApi.queryBalance(dataSource);
        } catch (final HfApiException e) {
            logger.error(e.getMessage(), e);

            return new HashMap<String, Object>() {{
                put("error", e.getMessage());

            }};
        }
    }
}
