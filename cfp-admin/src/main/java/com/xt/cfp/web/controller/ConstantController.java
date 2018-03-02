package com.xt.cfp.web.controller;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.pojo.BondSource;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.WithDraw;
import com.xt.cfp.core.pojo.ext.WithDrawExt;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.service.WithDrawService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.TimeInterval;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luqinglin on 2015/6/19.
 */
@Controller
@RequestMapping(value = "/constant")
public class ConstantController extends BaseController {

    private static Logger logger = Logger.getLogger(ConstantController.class);

    @Autowired
    private ConstantDefineService constantDefineService;

    /**
     * 跳转至字典列表
     * @return
     */
    @RequestMapping(value = "to_constant_list")
    public String toContantList() {
        return "jsp/constant/constantList";
    }

    /**
     * 字典列表
     * @param request
     * @param constantDefine
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "showConstantList")
    @ResponseBody
    public Object showConstantList(HttpServletRequest request,
                                 @ModelAttribute ConstantDefine constantDefine,
                                 @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                                 @RequestParam(value = "page", defaultValue = "1") int pageNo) {

        Pagination<ConstantDefine> constantDefinePaging = constantDefineService.getConstantDefinePaging(pageNo, pageSize, constantDefine, null);
        return constantDefinePaging;
    }

    /**
     * 跳至添加页面
     * @param request
     * @param constantDefineId
     * @return
     */
    @RequestMapping(value = "add")
    public String add(HttpServletRequest request,Long constantDefineId,String addChild){
        ConstantDefine constantDefine = new ConstantDefine();
        if(StringUtils.isNotEmpty(addChild)){
            constantDefine.setParentConstant(constantDefineId);
        }else{
            if (constantDefineId!=null){
                //修改操作
                constantDefine = constantDefineService.findById(constantDefineId);
            }
        }
        request.setAttribute("constantDefine", constantDefine);
        return "jsp/constant/constantAdd";
    }

    /**
     * 保存渠道
     * @param constantDefine
     * @return
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public String save(@ModelAttribute ConstantDefine constantDefine){
        try {
            if (constantDefine.getConstantDefineId()==0)
                constantDefineService.addConstantDefine(constantDefine);
            else{
                constantDefineService.updateConstantDefine(constantDefine);
            }
            return "success";
        }catch (SystemException se){
            logger.error(se.getDetailDesc(),se);
            se.printStackTrace();
            return se.getMessage();
        }

    }

}
