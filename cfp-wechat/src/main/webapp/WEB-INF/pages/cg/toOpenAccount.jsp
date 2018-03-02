<%@ page language="java"  pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<title>实名认证</title>
<link rel="stylesheet" href="${ctx}/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx}/css/RealNameIdent.css?${version}">
<link rel="stylesheet" href="${ctx}/css/sweetAlert.css?${version}">
<script data-main="${ctx}/js/openAccount.js?${version}" src="${ctx}/js/lib/require.js"></script>
<script type="text/javascript">
    //rem自适应字体大小方法
    var docEl = document.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function() {
            //设置根字体大小
            docEl.style.fontSize = 10 * (docEl.clientWidth / 320) + 'px';
        };
    //绑定浏览器缩放与加载时间
    window.addEventListener(resizeEvt, recalc, false);
    document.addEventListener('DOMContentLoaded', recalc, false);
</script>
</head>
<body class="l_NewScroll">
<section class="real-theme param_box">
    <div class="name-box">
        <span>姓<i> </i>名</span>
        <input type="text" placeholder="请输入姓名" name="cust_nm" value="${userInfoExt.realName}" class="name-case" id="name-case">
    </div>
    <div class="id-box">
        <span>身份证</span>
        <input type="text" placeholder="请输入身份证" name="certif_id" value="${userInfoExt.idCard}" class="id-case" id="id-case">
    </div>
    <div class="id-box">
        <span>银行卡号</span>
        <input type="text" placeholder="请输入银行卡号" name="capAcntNo" value="" class="name-case" id="bank-case">
    </div>
    <div class="id-box">
        <span>手机号</span>
        <input type="text" placeholder="请输入手机号" name="mobile_no" value="${currentUser.mobileNo}" class="name-case" id="mobile-case">
    </div>
    <div class="id-box">
        <span>开户行地区代码</span>
        <input type="text" placeholder="" name="city_id" value="1000" class="name-case" id="city-case">
    </div>
    <div class="id-box">
        <span>开户行行别</span>
        <input type="text" placeholder="" name="parent_bank_id" value="0305" class="name-case" id="bank-type-case">
    </div>
    <button id="complete" class="complete">完成</button>
    <div class="loading" id="loading">
        <img src="${ctx}/images/loading.gif" alt="">
    </div>
</section>
</body>
</html>