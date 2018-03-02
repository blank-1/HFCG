<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>
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
	<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
	<link rel="stylesheet" href="${ctx }/css/reset2.css" type="text/css">
	<link rel="stylesheet" href="${ctx }/css/PayeeDetails.css">
	<title>资产记录详情</title>
</head>
<body>
	<div class="w_wrppaer">
		<dl class="w_explain">
			<c:if test="${userHis.changeType eq '2' || userHis.changeType eq '3'}">
				<dd style="background:url(${ctx }/images/expenses.png) no-repeat 33% center;background-size: contain; ">出借人支出</dd>
			</c:if>
			<c:if test="${userHis.changeType eq '1'}">
				<dd style="background:url(${ctx }/images/income.png) no-repeat 33% center;background-size: contain; ">出借人收入</dd>
			</c:if>
			<c:if test="${userHis.changeType eq '4'}">
				<dd style="background:url(${ctx }/images/Frozen.png) no-repeat 33% center;background-size: contain; ">出借人冻结</dd>
			</c:if>
			<c:if test="${userHis.changeType eq '5'}">
				<dd style="background:url(${ctx }/images/Thaw.png) no-repeat 33% center;background-size: contain; ">出借人解冻</dd>
			</c:if>
			<dt>
				<p>
					<fmt:formatNumber value="${userHis.changeValue2}" pattern="#,##0.00"/><span>元</span>
				</p>
				<p>${userHis.desc}</p>
			</dt>
		</dl>
		<ul class="w_details">
			<li>
				<p>流水类型</p>
				<p>
					<c:if test="${userHis.changeType eq '2' || userHis.changeType eq '3'}">支出</c:if>
					<c:if test="${userHis.changeType eq '1'}">收入</c:if>
					<c:if test="${userHis.changeType eq '4'}">冻结</c:if>
					<c:if test="${userHis.changeType eq '5'}">解冻</c:if>
				</p>
			</li>
			<li>
				<p>交易金额</p>
				<p class="w_record">
					<c:if test="${userHis.changeType eq '1'}">+<fmt:formatNumber value="${userHis.changeValue2}" pattern="#,##0.00"/></c:if>
					<c:if test="${userHis.changeType eq '2' || userHis.changeType eq '3'}">-<fmt:formatNumber value="${userHis.changeValue2}" pattern="#,##0.00"/></c:if>
					<c:if test="${userHis.changeType eq '4' || userHis.changeType eq '5'}">0.00</c:if>
				</p>
			</li>
			<li>
				<p>冻结/解冻</p>
				<p class="w_record">
					<c:if test="${userHis.changeType eq '1' ||userHis.changeType eq '2' || userHis.changeType eq '3'}">0.00</c:if>
					<c:if test="${userHis.changeType eq '4' || userHis.changeType eq '5'}"><fmt:formatNumber value="${userHis.changeValue2}" pattern="#,##0.00"/></c:if>
				</p>
			</li>
			<li>
				<p>账户余额</p>
				<p class="w_record"><fmt:formatNumber value="${userHis.valueAfter2}" pattern="#,##0.00"/></p>
			</li>
			<li>
				<p>日期</p>
				<p><fmt:formatDate value="${userHis.changeTime}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
			</li>
		</ul>
	</div>

</body>
</html>
