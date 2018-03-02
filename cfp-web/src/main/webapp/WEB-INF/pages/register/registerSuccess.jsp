<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta charset="utf-8" />
  <meta name="keywords" content="" />
  <meta name="description" content="" />
  <title>注册成功 - 财富派</title>
  <%@include file="../common/common_js.jsp"%>
</head>

<body>
<header>
  <!-- navtopbg start-->
  <%@include file="../common/headLine1.jsp"%>
 <customUI:headLine />
</header>
<div class="th-pro-title"></div>
<!-- article start -->
<article >
  <div class="wrapper">
    <div class="regisuss">
      <h2><img src="${ctx}/images/img/true.jpg" />恭喜您，注册成功！</h2>
      <p>为保证您的账户资金安全，请到恒丰进行开户。</p>
      <%--<p>立即完成<a href="${ctx}/person/toAuthentication" class="ared">身份验证</a>，将获得10元的现金奖励！</p>--%>
      <a  href="${ctx}/person/toAuthentication" type="button" class="btn btn-error mb-30">去开户</a><br />
      <a href="${ctx}/person/toIncome">账户充值</a>
      <a href="${ctx}/" class="ml-10">浏览网站</a>
    </div>
  </div>
</article><!-- article end -->

<!-- footer start -->
<%@include file="../common/footLine3.jsp"%>
<!-- footer end -->
</body>
</html>

