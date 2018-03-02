<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<%@include file="../common/common_js.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />    
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<link rel="stylesheet" href="${ctx }/css/financialPswReset.css" type="text/css">
<link rel="stylesheet" href="${ctx }/css/cfhVerified.css" type="text/css">
<title>欢迎登录财富派</title>
</head>

<body>
    <header></header>
    <section>
        <p class="link">为了保证账户安全，请完成身份认证</p>
        <span id="userName"></span>
        <input type="hidden" id="verifiedState" value="${verifiedState }"/>
        <input type="hidden" id="type" value="${type }"/>
        <input type="hidden" id="mobileNo" value="${mobileNo }"/>
        <input type="hidden" id="JMmobileNo" value="${JMmobileNo }"/>
        <input type="hidden" name=hToken id="hToken" value="${hToken}">
        <input name="input" class="l_userId input1" data-inputNum="1" title="真实姓名" id="realName" placeholder="请输入真实姓名" type="text" onfocus="$('#realName').attr('placeholder','');$('#realName').css('border','solid 1px #3bc2ff')" onblur="$('#realName').attr('placeholder','请输入真实姓名');$('#realName').css('border','')">
        <input name="input" class="l_userId input2" data-inputNum="2" title="身份证号码" id="userCode" placeholder="请输入身份证号码" type="text" onfocus="$('#userCode').attr('placeholder','');$('#userCode').css('border','solid 1px #3bc2ff')" onblur="$('#userCode').attr('placeholder','请输入身份证号码');$('#userCode').css('border','')">
        <div class="l_checkCode">
            <input class="l_userId  input3" type="tel" title="验证码" name="input" data-inputNum="3" maxlength="6" placeholder="请输入短信验证码" id="checkCode" onfocus="$('#checkCode').attr('placeholder','');$('#checkCode').css('border','solid 1px #3bc2ff')" onblur="$('#checkCode').attr('placeholder','请输入短信验证码');$('#checkCode').css('border','')" >
            <input type="button" value="获取验证码" id="checkCodeB" class="checkCodeB linkB" >
        </div>
        <button class="l_load saoFenB" id="load" onclick="load();">完成</button>
    </section>
<script src="${ctx }/js/cfhVerified.js" type="text/javascript"></script>
<script src="${ctx }/js/cfhMain.js" type="text/javascript"></script>
</body>
</html>

