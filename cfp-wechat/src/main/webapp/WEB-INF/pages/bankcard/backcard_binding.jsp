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

</head>
<body id="backgd">
<input id="isVerified" type="hidden" value="${userExt.isVerified}"/>

<!-- 为绑卡提示开始 -->
<img class="l_iconAccountB" src="${ctx}/images/icon_Accountbinding.png" alt="">
<h1 style="color:#777778;margin:16% auto 14%;text-align: center;font-size: 2.2rem;line-height: 3rem">您尚未绑定银行卡<br>请绑定后进行操作</h1>
<button class="l_redBTN" style="background: " onclick="window.location.href='${ctx}/bankcard/to_bidding';">绑定银行卡</button>   
<!-- 为绑卡提示结束 -->

<!--身份认证提示弹窗开始-->
<div id="light1" class="light" >
    <p>
    <img src="${ctx}/images/iconnot.png" />
    <span>抱歉，您尚未身份认证</span>
    </p>
    <a href="${ctx }/person/identityAuthenticationBy " class="closebt3" id="closebt2">去认证</a>
</div>
<div id="fade1" class="fade"></div>
<!--身份认证提示弹窗结束--> 

</body>
<script type="text/javascript">
$(function(){
	//判断是否实名认证
	var isVerified =document.getElementById("isVerified");
	if('1'==isVerified.value){
		light1.style.display='none';
		fade1.style.display='none';
		backgd.style.overflow='visible'
	}else{
		light1.style.display='block';
		fade1.style.display='block';
		backgd.style.overflow='hidden'; 
	}
});
</script>
</html>
