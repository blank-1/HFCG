<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<link href="/images/favicon.ico" rel="shortcut icon">
<link href="/images/favicon.ico" rel="Bookmark">
<meta name="keywords" content="" />
<meta name="description" content="" /> 
<!-- <meta http-equiv="X-UA-Compatible" content="IE=7;IE=9;IE=8;IE=10;IE=11"> -->
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">

<title>财富派</title>
<%@include file="../../common/common_js.jsp"%>
<link rel="stylesheet" type="text/css" href="http://caifupad.com/zt/css/base.css" /><!-- index css -->
<link rel="stylesheet" type="text/css" href="${ctx }/css/sudokuSweepstake/zty.css" />
<link rel="stylesheet" type="text/css" href="${ctx }/css/sudokuSweepstake/cj2.css" />
</head>

<body>
	<%--登陆--%>
<!-- line2 start -->
<%@include file="../../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="0"/>
<!-- navindex end -->
<%@include file="../../login/login.jsp"%>
<%-- <div class="detail-zty d001"></div>
<input type="hidden" name="pastUrl" value="/activity/toWebTurntable">
<div <c:if test="${not empty sessionScope.currentUser }">class="detail-zty d002"</c:if> <c:if test="${empty sessionScope.currentUser }">class="detail-zty d005"</c:if> >
    <div class="wrapper">
    <a href="javascript:;" data-id="login" data-mask="mask" class="login"></a>
    <span class="t001"><fmt:formatNumber value="${allBuyBalance }" pattern="#,##0"/><em>元</em></span>
    <span class="t002">${shareNum }<em>次</em></span>
    </div>
</div> --%>

<!-- navindex end -->
<div class="cj1"></div>
<div class="cj23">	
	<div class="cj-con">
    	<a href="javascript:;" data-id="login" data-mask="mask" class="loginq btn-general btn-3d btn-apply btn-apply-enabled" style="display:none">登录</a>
    	<div style="display:none">
            <div class="money">
                <b>102,000</b>
                <span>元</span>
            </div>
            <div class="num">
                <i id="i-id">3</i>
                <em>次</em>
            </div>
        </div>
        <div class="erweima">
            <ul>
                <li>
                    <img src="${ctx}/images/e1.png" alt="">
                    <p>财富派APP<br>苹果版下载</p>
                </li>
                <li>
                    <img src="${ctx}/images/e2.png" alt="">
                    <p>财富派APP<br>安卓版下载</p>
                </li>
                <li>
                    <img src="${ctx}/images/e3.png" alt="">
                    <p>财富派<br>微信公众号</p>
                </li>
            </ul>
        </div>
        <div class="cj-wrap">
        	<ol class="cj-ol" id="lottery">
            	<li class="lottery-unit lottery-unit-0"><img src="${ctx}/images/img1.png" alt=""><div class="div0"></div></li>
                <li class="lottery-unit lottery-unit-1"><img src="${ctx}/images/img2.png" alt=""><div class="div1"></div></li>
                <li class="lottery-unit lottery-unit-2"><img src="${ctx}/images/img3.png" alt=""><div class="div2"></div></li>
                <li class="lottery-unit lottery-unit-3"><img src="${ctx}/images/img4.png" alt=""><div class="div3"></div></li>
                <li class="lottery-unit lottery-unit-4"><img src="${ctx}/images/img5.png" alt=""><div class="div4"></div></li>
                <li class="lottery-unit lottery-unit-5"><img src="${ctx}/images/img6.png" alt=""><div class="div5"></div></li>
                <li class="lottery-unit lottery-unit-6"><img src="${ctx}/images/img7.png" alt=""><div class="div6"></div></li>
                <li class="lottery-unit lottery-unit-7"><img src="${ctx}/images/img8.png" alt=""><div class="div7"></div></li>
                <li class="lottery-unit lottery-unit-8"><img src="${ctx}/images/img9.png" alt=""><div class="div8"></div></li>
            </ol>
        </div>
        <div class="cj-mark" id="cj-mark"></div>
        <div class="cj-djs2"></div>
        <div class="cj-hand" id="parent">
        	<div class="cj-hanr" id="div1"></div>
        </div>
    </div>
</div>
<div class="cj-new2"></div>
<div class="cj3"></div>
<div class="cj4"></div>
<div class="cj5"></div>
<style>
</style>
</body>
</html>
