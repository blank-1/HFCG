<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="../common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="msapplication-tap-highlight" content="no"/>
    <%@include file="../common/common_js.jsp" %>
    <link rel="stylesheet" href="${ctx }/css/reset2.css" type="text/css">
    <link rel="stylesheet" href="${ctx }/css/register2.css" type="text/css">
    <title>银行卡</title>
</head>
<body id="backgd">
<div class="recharge1" style="margin:-4% 0 0 0;background-image:url(${ctx}/images/banklogo/${customerCardEnable.bankCode}.png);">
    <span>(${customerCardEnable.cardCodeShort})</span>
</div>
<a class="bankinfobtn" href="${ctx }/finance/list">去理财></a>
<img class="bankinfoimg" src="${ctx}/images/bankinfoimg.png"/>


</body>
</html>