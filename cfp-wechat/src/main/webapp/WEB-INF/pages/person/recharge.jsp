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
<title>充值</title>
<link rel="stylesheet" href="${ctx }/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx }/css/s_Recharge.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx }/css/sweetAlert.css?${version}" type="text/css">
<script data-main="${ctx }/js/s_Recharge.js?${version}" src="${ctx }/js/lib/require.js?${version}"></script>
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

<body <c:if test="${empty customerCard && empty ybBindCard}">class="bodyBj l_noCard"</c:if><c:if test="${not empty customerCard || not empty ybBindCard}">class="bodyBj"</c:if>>
<div class="clear_20"></div>

<input id="isVerified" type="hidden" value="${userExt.isVerified}"/>
<input id="isCustomerCard" type="hidden"  value="${not empty customerCard}"/>
<% session.removeAttribute("recharge_smsCode");%>

<form method="POST" name="frm" class="psw" id="rechargeForm" >
    <input type="hidden" name="payType" id="payType" value="1"/>
    <input type="hidden" name="token" id="token" value="${token}"/>

    <div class="yinhBox">
        <div class="yinkBox_top"><c:if test="${not empty customerCard}">${customerCard.branchName}</c:if></div>
        <c:if test="${userExt.isVerified eq '2'}">
            <div class="yinkBox_bot">
                <c:if test="${not empty customerCard}">
                    <span class="yinkBox_botLe">
                            <img src="${ctx}/images/banklogo/${customerCard.bankCode}_1.png" />
                    </span>
                    <span class="yinkBox_botRi">＊＊＊＊＊＊＊＊＊＊＊＊${customerCard.encryptCardNo}</span>
                    <input type="hidden" name="cardId" id="cardId" value="${customerCard.customerCardId}"/>
                    <input type="hidden" name="cardNo" id="cardNo" value="${customerCard.cardCode}"/>
                </c:if>
                <c:if test="${not empty ybBindCard }">
                    <span class="yinkBox_botLe">
                        <img src="${ctx}/images/banklogo/${ybBindCard.bankCode}_1.png" />
                    </span>
                    <span class="yinkBox_botRi">＊＊＊＊＊＊＊＊＊＊＊＊${ybBindCard.encryptCardNo}</span>
                    <c:if test="${support}">
                        <input type="hidden" name="cardId" id="cardId" value="${ybBindCard.customerCardId}"/>
                        <input type="hidden" name="cardNo" id="cardNo" value="${ybBindCard.cardCode}"/>
                    </c:if>
                </c:if>
            </div>
        </c:if>
    </div>
    <div class="clear_20"></div>

    <div class="czjeBox l_cardNum">
        <span>银行卡号</span>
        <input type="tel" id="cardNumber" name="cardNo"  oninput="formatBankNo(this)" placeholder="请输入银行卡号" />
    </div>

    <div class="czjeBox">
        <span>充值金额</span>
        <input type="text" id="moneyInput" maxlength="5" name="rechargeAmount" placeholder="充值金额100元起" />
    </div>

</form>

<div class="tips"></div>

<div class="czjeBox_bot">
	<a href="${ctx }/bankList" style="float:left;">支付及限额></a>
	<a href="${ctx }/person/toIncomeList" style="float:right;">充值记录></a>
</div>
<div class="clear_0"></div>


<form action="${llWapPayUrl }" id="llRechargeForm" method="post">
    <input type="hidden" name="req_data" id="req_data" value='' />
</form>
<div style="display: none" id="doAppRecharge">
    <form id="FM" action="" method="post">
        <input type="hidden" name="mchnt_cd" value=""/>
        <input type="hidden" name="mchnt_txn_ssn" value=""/>
        <input type="hidden" name="login_id" value=""/>
        <input type="hidden" name="amt" value=""/>
        <input type="hidden" name="page_notify_url" value=""/>
        <input type="hidden" name="back_notify_url" value=""/>
        <input type="hidden" name="signature" value=""/>
    </form>
</div>

<button class="nextBtn" id="nextBtn">下一步</button>

<div class="loading" id="loading">
    <img src="${ctx}/images/loading.gif" alt="">
</div>

</body>
</html>