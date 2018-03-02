<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta charset="utf-8" />
  <meta name="keywords" content="" />
  <meta name="description" content="" />
  <title>充值成功 - 财富派</title>
  <%@include file="../common/common_js.jsp"%>
</head>

<body>
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
<article>
  <div class="wrapper">
    <div class="regisuss paysuc">
      <h2><img src="${ctx}/images/img/true.jpg" />恭喜您，充值成功！</h2>

      <p class="payr">每成功邀请一位好友成功投标，即奖励<i class="c_red">5</i>元现金和<i class="c_red">10</i>元财富券！</p>


      <a href="${ctx}/finance/list" class="btn btn-error mt-30">继续理财</a>
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
