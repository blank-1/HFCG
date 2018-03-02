<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xt.cfp.core.constants.Constants" %>
<%@include file="../common/taglibs.jsp"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
    pageContext.setAttribute("picPath", Constants.picPath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<%-- <%@include file="../common/common_js.jsp"%> --%>
<link rel="stylesheet" href="${ctx }/css/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx }/css/style.css" type="text/css">
<script type="text/javascript">
var rootPath = '<%=ctx%>';
function getDateStr(date, num, ymd) {
    var now = null;
    if (typeof(date) == 'undefined' || date == null) {
        now = new Date();
    }
    else {
        now = date;
    }
    var yy = now.getFullYear();//getYear();
    if (ymd == "yy" && num != null && num != "") {
        yy = yy + num;
    }
    if (ymd == "mm" && num != null && num != "") {
        now.setMonth(now.getMonth() + num);
    }
    if ((ymd == null || ymd == "dd") && num != null && num != "") {
        now.setDate(now.getDate() + num);
    }
    var dd = now.getDate();
    if (dd < 10) dd = "0" + dd;
    var mm = now.getMonth() + 1;
    if (mm < 10) mm = "0" + mm;
    var currdate = now.getFullYear() + "-" + mm + "-" + dd;
    return currdate;
    
}
</script>
<title>理财记录</title>
</head>
<body style="background:#EBEEF7;">
	<header>
	<input id ="flag1" value="${flag }" type="hidden" > </input>
		<ul class="list" id="list">
			<li id="curById" class="cur">在投中</li>
			<li id="curById1">所有记录</li>
		</ul>
	</header>
	<section id="straining">
		<ul class="page1"  id="listByFinanceByIng" data-type="1">
		 
		</ul>
		<ul class="page2" id="listByFinanceBy" data-type="1">
			 
		</ul>
		 
	</section>
	
<script src="${ctx}/js/zepto.min.js"></script>
<script src="${ctx}/js/financingRecord.js" type="text/javascript"></script> 
<script src="${ctx}/js/public.js" type="text/javascript"></script> 
</body>
</html>