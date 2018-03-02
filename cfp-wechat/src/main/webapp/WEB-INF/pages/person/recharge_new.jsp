<%@ page pageEncoding="UTF-8"%>
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
    <link rel="stylesheet" href="${ctx }/css/reset.css" type="text/css">
    <link rel="stylesheet" href="${ctx }/css/s_Recharge.css" type="text/css">
    <link rel="stylesheet" href="${ctx }/css/sweetAlert.css" type="text/css">
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
    <title>充值</title>
</head>
<body class="bodyBj l_noCard">
<div class="clear_20"></div>
<div class="yinhBox">
    <div class="yinkBox_top">中国民生银行</div>
    <div class="yinkBox_bot">
        <span class="yinkBox_botLe"><img src="${ctx }/images/userCenter/yinh_icon.png" /></span>
        <span class="yinkBox_botRi">＊＊＊＊＊＊＊＊＊＊＊＊8888</span>
    </div>
</div>
<div class="clear_20"></div>
<div class="czjeBox">
    <span>充值金额</span>
    <input type="text" id="moneyInput" placeholder="充值金额100元起" />
</div>
<div class="Prompt">
    提示：单笔可充值金额为100万，单日不限
</div>
<div class="tips"></div>
<div class="czjeBox_bot">
    <a href="s_paymenLimit.html" style="float:left;">支付及限额></a>
    <a href="s_rechargeRecord.html" style="float:right;">充值记录></a>
</div>
<div class="clear_0"></div>
<button class="nextBtn" id="nextBtn">下一步</button>
</body>
</html>
