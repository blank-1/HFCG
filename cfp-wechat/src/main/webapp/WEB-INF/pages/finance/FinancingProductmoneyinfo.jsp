<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<%-- <%@ page import="com.xt.cfp.core.constants.Constants" %>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
    pageContext.setAttribute("picPath", Constants.picPath);
%> --%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />    
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<%@include file="../common/common_js.jsp"%>
    <title>理财详情</title>
    <script type="text/javascript">
 
    </script>
</head>
<body style="background-color:#ebeef6;margin:0;padding:0;" id="">
${bangDingDetailsBySeesion}
<%-- <input id ="uuid1" type="hidden" value="${bangDingDetailsBySeesion}"></input>   --%>


    <a id="moneyinfo2" class="moneyinfo2" href="#"><img id="moneyinfoimg" class="moneyinfoimg" src="${ctx }/images/moneyinfo.png" /></a>
    <div id="moneyinfo3" class="moneyinfo3">
    	<p>此标已撤销，还有其他高收益产品</p>
    	<a class="moneybtn">去看看</a>
    </div>
</body>
</html>