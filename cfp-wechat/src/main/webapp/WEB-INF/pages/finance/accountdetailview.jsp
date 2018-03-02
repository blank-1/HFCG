<%@ page language="java"  pageEncoding="UTF-8"%>
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
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache, must-revalidate">
<meta http-equiv="expires" content="0">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<link rel="stylesheet" href="${ctx }/css/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx }/css/s_totalAssets.css" type="text/css">
<script type="text/javascript">
//rem自适应字体大小方法
var docEl = document.documentElement,
resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
recalc = function() {
    //设置根字体大小
    docEl.style.fontSize = 10 * (docEl.clientWidth / 320) + 'px';
};
//绑定浏览器缩放与加载时间
window.addEventListener(resizeEvt, recalc, false);
document.addEventListener('DOMContentLoaded', recalc, false);
</script>
<title>我的资产</title>
</head>
<body class="bodyBj">
	<div class="totalassets_main">
		<p class="totalText1">总资产（元）</p>
		<p class="totalText2"><fmt:formatNumber value="${netAsset}" pattern="#,##0.00"/></p>
		<p class="totalText3">总资产=账户余额+待回+收益+推广收益+省心</p>
	</div>
	<div class="totalassets_list">
		<dl>
			<dd>在投资金</dd>
			<dt><fmt:formatNumber value="${capitalRecive}" pattern="#,##0.00"/>元</dt>
		</dl>
		<dl>
			<dd>账户余额</dd>
			<dt><fmt:formatNumber value="${cashAccount.value2}" pattern="#,##0.##"/>元</dt>
		</dl>
		<dl>
			<dd>冻结余额</dd>
			<dt><fmt:formatNumber value="${cashAccount.frozeValue2}" pattern="#,##0.##"/>元</dt>
		</dl>
		<dl>
			<dd>待回本金</dd>
			<dt><fmt:formatNumber value="${capitalRecive}" pattern="#,##0.00"/>元</dt>
		</dl>
		<dl>
			<dd>待回利息</dd>
			<dt><fmt:formatNumber value="${interestRecive}" pattern="#,##0.00"/>元</dt>
		</dl>
		<dl>
			<dd>已获收益</dd>
			<dt><fmt:formatNumber value="${allProfit}" pattern="#,##0.00"/> 元</dt>
		</dl>
		<a href="${ctx }/person/toAllMyFinanceList">
			<dl>
				<dd>省心账户</dd>
				<dt><fmt:formatNumber value="${financeTotalValue }" pattern="#,##0.00"/>元</dt>
				<img src="${ctx }/images/right_arrow.png" />
			</dl>
		</a>
		<%-- <a hef="${ctx }/person/toAllMyFinanceList">
		<dd>省心账户</dd>
			<dl>
			<dt><fmt:formatNumber value="${financeTotalValue }" pattern="#,##0.00"/>元</dt>
			<img src="${ctx }/images/right_arrow.png" />
			</dl>
		</a> --%>
		<dl>
			<dd>参与总项</dd>
			<dt><fmt:formatNumber value="${financeTotalCounts }" pattern="#,##0"/>项</dt>
		</dl>
		<dl>
			<dd>省心收益</dd>
			<dt><fmt:formatNumber value="${financeTotalInterest }" pattern="#,##0.00"/>元</dt>
		</dl>
		<dl>
			<dd>省心待回利息</dd>
			<dt><fmt:formatNumber value="${financeWaitInterest }" pattern="#,##0.00"/>元</dt>
		</dl>
	</div>
</body>
</html>
