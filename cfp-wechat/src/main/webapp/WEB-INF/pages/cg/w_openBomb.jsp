<%@ page  pageEncoding="UTF-8"%>
<%--<%@include file="../common/taglibs.jsp"%>--%>
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
<link rel="stylesheet" href="${ctx}/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx}/css/openBomb.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx}/css/sweetAlert.css?${version}" type="text/css">
<script data-main="${ctx}/js/openBomb.js?${version}" src="${ctx}/js/lib/require.js?${version}"></script>
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
<title>开通引导页</title>
</head>
<body class="l_NewScroll">
    <div class="bombMask" id="bombMask">
        <div class="bombBox">
            <div class="bombHead">
                <div class="bombClose"></div>
                <div class="bombHead">
                    <img src="${ctx}/images/hengfeng_logo.png" alt="">
                </div>
            </div>
            <div style="width: 100%;background:#fff;padding: 3rem 0;">
                <div class="bombContent">
                    <p>恒丰银行资金存管</p>
                    <span>个人独立存管账户  资金安全全程保障</span>
                </div>
            </div>
            <div class="bombBtn">
                <button class="Temporarily" id="Temporarily">暂不开通</button>
                <button class="immediately" id="immediately" onclick="location.href='/person/identityAuthenticationBy?url=/person/account/overview'">立即开通</button>
            </div>
        </div>
    </div>
</body>
</html>
