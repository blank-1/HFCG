package com.xt.cfp.test.controller;
import com.xt.cfp.core.service.UserInfoService;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class demo {
    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/demo")
    public String login(HttpServletRequest request, HttpServletResponse response) {

        return "index";
    }

    @RequestMapping(value = "/dd")
    public String dd() {

        userInfoService.dd();

        return null;
    }


}
