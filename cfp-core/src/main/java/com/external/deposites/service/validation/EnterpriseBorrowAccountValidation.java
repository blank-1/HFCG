package com.external.deposites.service.validation;

import com.external.deposites.api.IConditionType;
import com.external.deposites.api.ValidationSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 企业开户 业务条件 校验器
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/17
 */
@Service
public class EnterpriseBorrowAccountValidation extends ValidationSupport {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @PostConstruct
    @Override
    public List<? extends IConditionType> loadConditions() {
        //todo
        return new ArrayList<>();
    }
}
