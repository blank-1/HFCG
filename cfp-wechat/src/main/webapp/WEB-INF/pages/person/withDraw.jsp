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
<title>提现</title>
<%@include file="../common/common_js.jsp"%>
<link rel="stylesheet" href="${ctx}/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx}/css/s_Withdrawals.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx}/css/sweetAlert.css?${version}" type="text/css">
<script data-main="${ctx}/js/s_Withdrawals.js?${version}" src="${ctx}/js/lib/require.js?${version}"></script>
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

<body class="l_NewScroll ">

<input id="usemoney" type="hidden" value="${cashAccount.availValue2}"/>
<input id="isVerified" type="hidden" value="${userExt.isVerified}"/>
<input id="isCustomerCard" type="hidden"  value="${not empty customerCard}"/>
<input id="voucherCount" type="hidden"  value="${voucherCount}"/>
<input id ="mobileNo" type="hidden" value=" ${sessionScope.currentUser.mobileNo}"/>
<input id ="mobileNo_star" type="hidden" value=" ${sessionScope.currentUser.encryptMobileNo}"/>
<input id="surplusAmount" type="hidden" value="
    <c:if test="${surplusAmount>=200000}">
        <c:if test="${cashAccount.availValue2>=200000}">200000</c:if>
        <c:if test="${cashAccount.availValue2<200000}">${cashAccount.availValue2}</c:if>
    </c:if>
    <c:if test="${surplusAmount<200000}">
        <c:if test="${cashAccount.availValue2>=200000}">${surplusAmount}</c:if>
        <c:if test="${cashAccount.availValue2<200000}">${cashAccount.availValue2}</c:if>
    </c:if>
">

<section class="container">
    <div class="header">
        <c:if test="${userExt.isVerified eq '2' }">
            <c:if test="${not empty customerCard}">
                <div class="imgBox">
                    <img src="${ctx}/images/banklogo/${customerCard.bankCode}_1.png" alt="">
                </div>
                <div class="bankCard">尾号${customerCard.encryptCardNo}银行卡</div>
            </c:if>
        </c:if>
    </div>
    <div class="balanceBox">
        <p>账户余额：￥<span id="tiXianRi">${cashAccount.availValue2}</span></p>
    </div>
    <div class="wrapper">
        <div class="title">提现金额</div>
        <div class="moneyBox">
            <span>￥</span>
            <input type="tel" class="moeny" id="moeny" name="moeny" maxlength="9">
        </div>
        <div class="liner"></div>
        <div class="balance">
            <div class="errorPrompt" id="errorPrompt">单笔最大可提￥
                <span id="surplusAmount_text">
                    <c:if test="${surplusAmount>=200000}">
                        <c:if test="${cashAccount.availValue2>=200000}">200000</c:if>
                        <c:if test="${cashAccount.availValue2<200000}">${cashAccount.availValue2}</c:if>
                    </c:if>
                    <c:if test="${surplusAmount<200000}">
                        <c:if test="${cashAccount.availValue2>=200000}">${surplusAmount}</c:if>
                        <c:if test="${cashAccount.availValue2<200000}">${cashAccount.availValue2}</c:if>
                    </c:if>
                </span>
            </div>
            <div class="allOut" id="allOut">
                <c:if test="${cashAccount.availValue2>=100}">全部提现</c:if>
            </div>
        </div>
        <div class="PunchClock">
            <p>到账金额</p>
            <p id="Arrival" id="Arrival">0</p>
        </div>
    </div>
    <div class="pwd_container">
        <input type="password" class="pwd_frame" id="pwd_frame" name="pwd_frame" placeholder="请输入交易密码">
    </div>
    <div class="button_pack">
        <button class="sure_Button" id="sure_Button" disabled="disabled">确认提现</button>
        <span>每日可提现3次，总额为50万元</span>
    </div>
</section>

<% session.removeAttribute("recharge_smsCode");%>
</body>
</html>