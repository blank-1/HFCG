<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@include file="../common/taglibs.jsp" %>
<%@ page import="com.xt.cfp.core.constants.Constants" %>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
    pageContext.setAttribute("picPath", Constants.picPath);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="msapplication-tap-highlight" content="no"/>
    <title>理财列表</title>
    <link rel="stylesheet" href="${ctx }/css/reset.css?${version}">
    <link rel="stylesheet" href="${ctx }/css/financialManagement.css?${version}">
    <style>
        .noNum img {
            width: 30%;
            margin-top: 40%;
        }

        .noNum p {
            width: 70%;
            margin-left: 15%;
            line-height: 1.8;
        }
    </style>
</head>
<script type="text/javascript">
    var rootPath = '<%=ctx%>';
</script>
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
<body class="l_NewScroll">
<div class="w_container">
    <header class="w_header">
        <ul class="w_vouList" id="w_vouList">
            <li><a id="li1" class="" href="javascript:void(0);">省心计划</a></li>
            <li><a id="li2" href="javascript:void(0);">精英标 </a></li>
            <li><a id="li3" href="javascript:void(0);">债权转让</a></li>
        </ul>
    </header>
    <div class="loading_mask"></div>
    <div class="orderList">
        <div class="w_page page0">
            <ul class="regular pay_page0">

            </ul>
        </div>
        <div class="w_page page1"><!--   收入的数据 -->
            <ul class="regular pay_page1">

            </ul>
        </div>
        <div class="w_page page2"><!--冻结的数据 -->
            <ul class="regular pay_page2">

            </ul>
        </div>
    </div>
</div>
<input value="${tab }" id="tab" type="hidden"/>
<script src="${ctx }/js/lib/jquery-1.11.0.min.js"></script>
<script src="${ctx }/js/lib/radialIndicator.js?${version}"></script>
<script src="${ctx }/js/financiaManagement.js?${version}"></script>
</body>
<%@include file="../common/navTag.jsp" %>
</html>
