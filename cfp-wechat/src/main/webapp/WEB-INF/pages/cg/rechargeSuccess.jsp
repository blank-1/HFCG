<%@ page  pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache, must-revalidate">
<meta http-equiv="expires" content="0">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<link rel="stylesheet" href="${ctx}/css/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx}/css/openSuccess.css" type="text/css">
<link rel="stylesheet" href="${ctx}/css/sweetAlert.css" type="text/css">
<script data-main="${ctx}/js/openSuccess.js?${ctx}" src="${ctx}/js/lib/require.js?${ctx}"></script>
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
<title>银行存管交易充值</title>
</head>
<body class="l_NewScroll">
    <section class="wrapper">
        <header>
            <p class="BankName">银行存管系统</p>
            <p class="bankLogo">
                <img src="${ctx}/images/hengfeng_logo.png" alt="">
            </p>
        </header>
        <c:if test="${!success}">
            <div class="successBox">
                <dl class="success">
                    <dd>
                        <img src="${ctx}/images/open_success.png" alt="">
                    </dd>
                    <dt>
                        充值失败！
                    </dt>
                </dl>
                <p class="Prompt">${message}</p>
            </div>
        </c:if>
        <c:if test="${success}">
        <div class="successBox">
            <dl class="success">
                <dd>
                    <img src="${ctx}/images/open_success.png" alt="">
                </dd>
                <dt>
                    恭喜您！
                </dt>
            </dl>
            <p class="Prompt">成功充值<fmt:formatNumber type="currency" value="${amt}" />元</p>
        </div>
        </c:if>
        <button class="autoSkip"><span id="autoSkip">4</span>秒后自动跳转</button>
    </section>
</body>
</html>
