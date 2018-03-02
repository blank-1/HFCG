<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>

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
<%@include file="../common/common_js.jsp"%>
<title>账号绑定</title>

<script src="${ctx }/js/Accountbinding.js" type="text/javascript"></script> 
</head>
<body id="backgd">
	<!--/顶部错误提示栏开始/-->
	<span id="alart"></span>
    <!--/顶部错误提示栏结束/-->
    
    <form  id="frm" action="/view" method="post" target="test">
     <input id="isVerified" type="hidden" value="${userExt.isVerified}"/>
    <p id="username" class="bankinfo username">${userExt.realName}</p>
	<!--/银行卡号输入框开始/-->
    <input id="bankid" class="inputm"  name="bankid" onKeyUp="formatBankNo(this)" onKeyDown="formatBankNo(this)"  value="请输入银行卡号" style="color:#afafaf;" onfocus="if(value=='请输入银行卡号'){this.style.color='#4b4b4b';value=''}" onblur="if(value==''){this.style.color='#afafaf';value='请输入银行卡号'};return checkInput5()" oninput="checkSueccess()"/>
    <!--/银行卡号输入框结束/-->
    <p  style="font-size:1.2rem;color:#9b9ea6;">* 仅支持本人的储蓄卡</p>
    <!--/手机号开始/-->
    	<input type="tel" name="telnum2" maxlength="11" value="请输入银行预留手机号" id="telnum2"  style="color:#afafaf;" onfocus="if(value=='请输入银行预留手机号'){this.style.color='#4b4b4b';value=''}" onblur="if(value==''){this.style.color='#afafaf';value='请输入银行预留手机号'};return checkInput5()" oninput="checkSueccess()" />
        <!--/手机号结束/-->
    <!--验证码开始-->
<div class="ic ic2" style="margin:4% 0 8% 0;">
    <input type="text" name="yzm" id="yzm" maxlength="6" value="请输入短信验证码" class="yzm" style="color:#afafaf;" onfocus="if(value=='请输入短信验证码'){this.style.color='#4b4b4b';value=''}" onblur="if(value==''){this.style.color='#afafaf';value='请输入短信验证码'};return checkInput5()" oninput="checkSueccess()"/>
    <input type="button" name="btn" id="btn" class="btn_mfyzm" value="获取验证码" oninput="checkSueccess()"/>
</div>
<!--验证码结束-->
        <p style="margin:20% 0 0 0;"><a class="fgt1"  style="font-size:1.5rem" onClick="">连连支付认证开通协议></a></p>
<input type="button" name="btn2" id="btn2" class="btnall" disabled="true" style="margin:4% 0 0 0;"  value="完成" />
</form>
<iframe id="test" name="test" style="display:none;"></iframe>
 <!--弹窗开始-->
     <div id="light" class="light" >
    <p>
    <img src="${ctx}/images/iconyes.png" />
    <span>您已成功绑定银行卡！</span>
    </p>
     <a href="${ctx}/bankcard/to_bankcard_list" class="closebt3" id="closebt2">完成</a>
    </div>
<div id="fade" class="fade"></div>
<!--弹窗结束-->
		<!--弹窗开始-->
<div id="light1" class="light" >
    <p>
    <img src="${ctx}/images/iconnot.png" />
    <span>抱歉，您尚未身份认证</span>
    </p>
    <a href="${ctx }/person/identityAuthenticationBy " class="closebt3" id="closebt2">去认证</a>
</div>
<div id="fade1" class="fade"></div>
		<!--弹窗结束--> 
</body>
</html>
