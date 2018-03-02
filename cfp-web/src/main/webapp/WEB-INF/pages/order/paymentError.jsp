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
<%@include file="../login/login.jsp"%>
  <!-- navtopbg start-->
  <%@include file="../common/headLine1.jsp"%>
     <!-- navindex start -->
<customUI:headLine />
<!-- navindex end -->

</header>
<div class="th-pro-title"></div>
<!-- article start -->
<article >
  <div class="wrapper">
    <div class="regisuss paysuc ">
      <h2><img src="${ctx}/images/img/false.jpg" />支付失败</h2>
      <p>如您的银行账户已经扣款，我们将在24小时内将</p>
      <p>资金充值到您的账户余额，请您随时查看，谢谢！</p>

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
