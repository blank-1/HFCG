<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8" />
  <meta name="keywords" content="" />
  <meta name="description" content="" />
  <title>支付失败 - 财富派</title>
  <%@include file="../common/common_js.jsp"%>
</head>

<body>
<%@include file="../login/login.jsp"%>
<header>
  <!-- navtopbg start-->
  <%@include file="../common/headLine1.jsp"%>
  <!-- navtopbg end-->

</header>

  <!-- navindex start -->
<customUI:headLine action="3"/>
<!-- navindex end -->
<!-- tabp start -->
<%request.setAttribute("tab","4-2");%>
 <input type="hidden" id="titleTab" value="2-2" />
<%-- <%@include file="../person/accountCommon.jsp"%> --%>
<%@include file="../login/login.jsp"%>
<!-- tabp end -->

<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a href="javascript:;">资金管理</a>></li>
        <li><span>充值</span></li>
    </ul>
</div>
<!-- person-link end -->


<!-- article start -->
<article >
  <div class="wrapper">
    <div class="regisuss paysuc ">
      <%--<p class="payr clearFloat"><span class="floatLeft">订单金额：<fmt:formatNumber value="${lendOrder.buyBalance}" pattern="#,##0.00"/>元</span>   <span class="floatRight">订单编号：${lendOrder.orderCode}</span></p>--%>
      <c:if test="${empty errorMsg}">
        <h2 class=""> <img src="${ctx}/images/icon3_03.png" />支付失败</h2>
      </c:if>
      <c:if test="${not empty errorMsg}">
        <h2 class=""> <img src="${ctx}/images/icon3_03.png" />${errorMsg}</h2>
      </c:if>

      <p>如您的银行账户已经扣款，我们将在24小时内将</p>
      <p>资金充值到您的账户余额，请您随时查看，谢谢！</p>
      <div class="mt-30">
        <a href="${ctx}/person/account/overview">查看账户</a>
      </div>
    </div>
  </div>
</article><!-- article end -->

<!-- footer start -->
<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- footer end -->
</body>
</html>
