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
<link rel="stylesheet" href="${ctx}/css/financialPsw.css" type="text/css">
<link rel="stylesheet" href="${ctx}/css/financialPswReset.css" type="text/css">
<title>理财产品</title>
</head>

<body>
<section>
    <img src="${ctx}/images/icon_psw.png" alt="">
    <p>此标为定向标，请输入定向密码</p>
    <form action="${ctx}/finance/toBuyBidLoanByPayAmount" id="financialPswForm" name="financialPswForm">
		<input type="hidden" id="loanApplicationId" name="loanApplicationId" value="${loanApplicationId }">
    	<input type="hidden" id="amount" name="amount" value="${amount }">
    	<input type="hidden" id="targetPass" name="targetPass" >
    </form>
    
    <div>
        <ul class="l_psw">
            <li><span id="s01"></span></li>
            <li><span id="s02"></span></li>
            <li><span id="s03"></span></li>
            <li><span id="s04"></span></li>
            <li><span id="s05"></span></li>
            <li><span id="s06"></span></li>
        </ul>
        <input id="psw" oninput="checkPsw();" type="text" maxlength="6" />
    </div>
    <p class="saoFen" id="PswTip">密码错误,请重新尝试</p>
</section>
<script src="${ctx}/js/jquery-1.8.3.min.js" type="text/javascript"></script>
<script src="${ctx}/js/financialPsw.js" type="text/javascript"></script>
</body>
</html>


