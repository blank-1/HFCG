<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta charset="utf-8" />
  <meta name="keywords" content="" />
  <meta name="description" content="" />
  <title>充值 - 财富派</title>
  <%@include file="../common/common_js.jsp"%>
</head>

<body class="position-rela" >
<header>
<%@include file="../login/login.jsp"%>
  <!-- navtopbg start-->
  <%@include file="../common/headLine1.jsp"%>
  <!-- navtopbg end-->

</header>

  <!-- navindex start -->
<customUI:headLine />
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
    <div class="regisuss paysuc">
      <h2>充值进行中……</h2>
      <p>如您的银行账户已经扣款，我们将在24小时内将</p>
      <p>资金充值到您的账户余额，请您随时查看，谢谢！</p>
      <div class="mt-30">

        <a href="${ctx}/person/account/overview" class="btn btn-error mt-30">查看账户</a>
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
