<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta charset="utf-8" />
  <meta name="keywords" content="" />
  <meta name="description" content="" />
  <title>支付失败 - 财富派</title>
  <%@include file="../common/common_js.jsp"%>
</head>

<body>
<header>
  <!-- navindex start -->
<customUI:headLine />
<!-- navindex end -->

</header>
<!-- person-link start -->
 <c:if test="${empty lendOrder}">
	<div class="person-link">
	    <ul class="container clearFloat">
	        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
	        <li><a href="javascript:;">资金管理</a>></li>
	        <li><span>充值</span></li>
	    </ul>
	</div>
</c:if>
<c:if test="${not empty lendOrder}">
	<div class="th-pro-title"></div>
</c:if>
<!-- person-link end -->
<!-- article start -->
<article >
  <div class="wrapper">
    <div class="regisuss paysuc ">
      <c:if test="${not empty errorMsg}">
        <h2><img src="${ctx}/images/pay_02.png" />${errorMsg}</h2>
      </c:if>
      <c:if test="${empty errorMsg}">
        <h2><img src="${ctx}/images/pay_02.png" />订单支付失败</h2>
      </c:if>
      <c:if test="${not empty lendOrder}">
        <p class="payr "><span class="">订单金额：<fmt:formatNumber value="${lendOrder.buyBalance}" pattern="#,##0.00"/>元</span>   <span class="ml-50">订单编号：${lendOrder.orderCode}</span></p>
      </c:if>
      <p>如您的银行账户已经扣款，我们将在24小时内将</p>
      <p>资金充值到您的账户余额，请您随时查看，谢谢！</p>
      <c:if test="${ordercode ne '500'}">
        <c:if test="${lendOrder.productType eq '1'}">
          <a href="${ctx}/finance/toPayForLender?lendOrderId=${lendOrder.lendOrderId}" class="btn btn-error mt-30">重新支付</a>
        </c:if>
        <c:if test="${lendOrder.productType eq '2'}">
          <a href="${ctx}/finance/toPayFinanceOrder?lendOrderId=${lendOrder.lendOrderId}" class="btn btn-error mt-30">重新支付</a>
        </c:if>
      </c:if>

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
