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
    <link rel="stylesheet" href="${ctx }/css/reset.css?${version}" type="text/css">
    <link rel="stylesheet" href="${ctx }/css/PersonalCenter.css?${version}">
    <script data-main="${ctx }/js/PersonalCenter.js?${version}" src="${ctx }/js/lib/require.js?${version}"></script>
    <script type="text/javascript">
        //rem自适应字体大小方法
        var docEl = document.documentElement,
            resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
            recalc = function () {
                //设置根字体大小
                docEl.style.fontSize = 10 * (docEl.clientWidth / 320) + 'px';
            };
        //绑定浏览器缩放与加载时间
        window.addEventListener(resizeEvt, recalc, false);
        document.addEventListener('DOMContentLoaded', recalc, false);
    </script>
</head>
<body class="l_NewScroll">
<section class="personal">
    <header>
        <div class="header-left">
            <p>${user.loginName}</p>
            <p>${user.mobileNo}</p>
        </div>
        <div class="header-right" id="header-right">
            <div class="imgBtn">
                <img src="${ctx }/images/header_code.png" alt="">
            </div>
        </div>
    </header>
    <section class="info-list-box">
        <ul class="info-list">
            <li onclick="location.href='${ctx}/person/singlePersonInformation';">
                <p>个人信息</p>
                <p></p>
            </li>
            <li onclick="location.href='${ctx}/person/personInformationVerified';">
                <p>认证信息</p>
                <p></p>
            </li>
            <li onclick="location.href='${ctx}/person/resetLoginPass';">
                <p>重置登录密码</p>
                <p></p>
            </li>
            <li onclick="location.href='${ctx}/person/resetTransPass';">
                <p>重置支付密码</p>
                <p></p>
            </li>
            <li onclick="location.href='${ctx}/person/aboutus';">
                <p>关于我们</p>
                <p></p>
            </li>
        </ul>
        <a href="${ctx}/user/logout" class="out-btn" id="out-btn">安全退出</a>
    </section>
</section>
<section class="Floating-layer" id="Floating-layer">
    <div class="layer">
        <h2 id="close"></h2>
        <div class="logo">
            <img src="${ctx }/images/logo.png" alt="">
        </div>
        <div class="headline">
            <p>我是<span>${name}</span></br>扫描我的二维码,一起来赚钱吧！</p>
        </div>
        <div class="code-box">
            <img src="${ctx }/person/getErWeiMa?invitecode=${invitecode}" alt="">
        </div>
    </div>
</section>
<%@include file="/WEB-INF/pages/common/navTag.jsp" %>
</body>
</html>