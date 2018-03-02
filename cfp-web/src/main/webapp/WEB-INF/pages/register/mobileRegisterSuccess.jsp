<%@ page import="com.xt.cfp.core.constants.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<%
  String ctx = request.getContextPath();
  pageContext.setAttribute("ctx", ctx);
  String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
  pageContext.setAttribute("basePath", basePath);
  pageContext.setAttribute("picPath", Constants.picPath);
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>手机注册成功 - 财富派</title>
	<link href="${ctx}/mobile/m_css/index.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="${ctx}/mobile/m_js/jquery-1.8.2.min.js"></script>
	<script type="text/javascript">
	  var rootPath = '<%=ctx%>';
	</script>
</head>
<body>
<!-- nav start -->
<div class="nav">
  <img src="${ctx}/mobile/m_image/web2_01.jpg" />
</div>
<!-- nav start -->

<!-- inbody start -->
<div class="inbody">
  <div class="title">
    <img class="img1" src="${ctx}/mobile/m_image/web1_05.jpg" /> 恭喜您注册成功！<br />
  </div>
  <div class="title2"><span>您只需参与投标就可获得5元现金奖励喔！</span></div>
  <div class="title">
    <img class="img2" src="${ctx}/mobile/m_image/web1_09.png" />
  </div>

  <div class="btn-group mt-50">
    <a href="${ctx}/" class="btn btn-error">进入财富派官网</a>
  </div>

</div>
<div class="bottom">
  <p class="text-p">北京汇聚融达网络科技有限公司</p>

  <p class="text-p">财富派京ICP备14051030号</p>

  <p class="text-p">客服电话400-061-8080</p>
</div>
<!-- inbody end -->

</body>
</html>