<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
<%@include file="../common/common_js.jsp"%>
<link rel="stylesheet" href="${ctx }/css/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx }/css/ProductsInfo.css" type="text/css">
<link rel="stylesheet" href="${ctx }/css/sweetAlert.css" type="text/css">
<link rel="stylesheet" href="${ctx }/css/swiper.min.css">
<script data-main="${ctx }/js/ProductsInfo.js" src="${ctx }/js/lib/require.js"></script>
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
<title>详情页</title>
</head>
<body class="l_NewScroll l_addM"><!--加息券的页面需要给body加class="l_addM"******定向标页面加class="dingxiang" -->
  <h1 class="l_title text-overflow">省心计划b-16051279218-43-1440万</h1>
  <header>
    <ul>
      <li class="l_headerInfo1 pjred">
        <p><i class="l_tipIcon l_backing">10</i>%</p>
        <font>还款方式：周期还息，到期还本</font>
        <font class="l_zName">已使用加息券2%,周年庆平台奖励加息1%</font>
        <p>预期年化收益</p>

      </li>
      <li class="l_headerInfo2">
        <p><i class="pjred">6</i>个月</p>
        <p>借款时长</p>
      </li>
      <li class="l_headerInfo3">
        <p><i class="pjred" id="money">329,119,260.22</i>元</p>
        <p>剩余本金</p>
      </li>
      <li class="l_headerInfo4">
        <p><i class="pjred" id="money">329,119,260.22</i>元</p>
        <p>转出价格</p>
      </li>
    </ul>
    </header>
    <section class="l_midInfo">
      <ul>
        <li>
          <p>回款日期</p>
          <p>应回本息</p>
          <p>状态</p>
        </li>
        <li>
          <p>2016.09.05</p>
          <p>888,888.88</p>
          <p>已还清</p>
        </li>
        <li>
          <p>2016.09.05</p>
          <p>888,888.88</p>
          <p>已还清</p>
        </li>
        <li>
          <p>2016.09.05</p>
          <p>888,888.88</p>
          <p>已还清</p>
        </li>
        <li>
          <p>2016.09.05</p>
          <p>888,888.88</p>
          <p>已还清</p>
        </li>
        <li>
          <p>2016.09.05</p>
          <p>888,888.88</p>
          <p>已还清</p>
        </li>

      </ul>
    </section>

</body>
</html>
