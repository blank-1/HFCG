<%@ page language="java"  pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>
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
<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
<link rel="stylesheet" href="${ctx }/css/financialProductsInfo_borrowInfo_sheng.css" type="text/css">
<script data-main="${ctx }/js/financialProductsInfo_borrowInfo_sheng.js" src="${ctx }/js/lib/require.js"></script>

<title>省心详情</title>
</head>
<body class="l_NewScroll">
  <header>
    <p class="l_focus">省心介绍</p>
    <p>加入记录</p>
  </header>
  <section class="l_shengTip">
  <input type="hidden" id="lendProductPublishId" value="${lendProductPublishId }" />
    <ul>
      <li class="Q1">
        <h1>什么是省心计划？</h1>
        <p>“省心计划”是财富派推出的自动投资工具，用户参与该计划可以按照事先确定的预期收益率自动投资财富派平台上的借款标的。通过自动投资所产生的每月回款（含本金和利息）将在省心期内继续自动投资，直至省心期结束。省心期结束后，省心计划将停止自动投标与回款复投。特别说明：省心期结束日期不等于用户收取全部投资本金和收益的日期。用户投资标的到期后收回投资本金和相应的收益。</p>
      </li>
      <li  class="Q2">
        <h1>省心计划安全吗？</h1>
        <img src="${ctx }/images/img_por.png" alt="">
      </li>
      <li  class="Q3">
        <h1>常见问题</h1>
        <p>省心计划安全吗？</p><span>省心计划投资标的为平台优质标的，多重安全保障，风险准备金护航，至今平台0逾期。</span>
        <p>参与省心计划后，哪里可以看到我购买的债权和合同？</p><span>您可以在【账户中心】-我的理财-省心计划中查看您所参与的省心计划所购买的债权以及合同。</span>
        <p>省心计划可以中途退出吗？</p><span>省心计划开启后，系统将自动为您投资优质标的，为了保证您投资的收益与安全，无法中途暂停省心计划。所以请在购买前选择适合您的省心计划类型 。</span>
        <p>为什么省心计划预期年化收益是范围值，而不是具体收益？</p><span>省心计划帮您自动投标，免去资金闲置所造成的损失。财富派平台标的十分火热，为了保证您的资金持续出借，平台将自动帮您的资金出借到最新的标的中，由于多项出借标的预期年化收益不同，所以您的省心计划预期年化收益为范围值。</span>
      </li>
    </ul>
  </section>
  <section class="l_shengList">
    <ul id="shenglist">
    </ul>
    <p class="l_lastTip">向下滑动加载更多</p>
  </section>
</body>
<script type="text/javascript">
var rootPath = '<%=ctx%>';
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
</html>
