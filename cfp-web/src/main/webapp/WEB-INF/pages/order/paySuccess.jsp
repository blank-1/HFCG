<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta charset="utf-8" />
  <meta name="keywords" content="" />
  <meta name="description" content="" />
  <title>支付成功 - 财富派</title>
  <%@include file="../common/common_js.jsp"%>
</head>

<body>
<header>
  <!-- navtopbg start-->
  <%@include file="../common/headLine1.jsp"%>
  <!-- navtopbg end-->
  <!-- navindex start -->
<customUI:headLine />
<!-- navindex end -->

</header>
<div class="th-pro-title"></div>
<!-- article start -->
<article>
  <div class="wrapper">
    <div class="regisuss paysuc">
      <h2><img src="${ctx}/images/img/true.jpg" />恭喜您，支付成功！</h2>
      <p class="payr "><span>订单金额：<fmt:formatNumber value="${lendOrder.buyBalance}" pattern="#,##0.00"/>元</span>
        <span class="ml-50">订单编号：${lendOrder.orderCode}</span></p>
  <!-- <p class="payr">邀请好友，好友成功出借即返<i class="c_red">50</i>元平台通用财富券！</p> -->

      <a href="${ctx}/finance/list" class="btn btn-error mt-30">继续理财</a>
      <div class="mt-30">

        <a href="${ctx}/person/account/overview">查看账户</a>
        <a href="${ctx}/person/to_lendorder_list" class="ml-10">查看订单</a>
      </div>
    </div>
  </div>
</article><!-- article end -->

<!-- footer start -->
<%@include file="../common/footLine3.jsp"%>
<!-- footer end -->
</body>
</html>
