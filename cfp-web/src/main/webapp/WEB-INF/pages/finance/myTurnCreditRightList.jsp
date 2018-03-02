<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">

<title>账户中心-债权转让</title>
<%@include file="../common/common_js.jsp"%>
<script type="text/javascript" src="${ctx}/js/jquery_page.js"></script>
<script type="text/javascript" src="${ctx}/js/zh_center_zqzr_mx.js"></script>
</head>

<body class="body-back-gray">
	<input type="hidden" id="hidem" data-value="545247000" />
	<!-- line2 start -->
	<%@include file="../common/headLine1.jsp"%>
	<!-- line2 start -->

	<!-- navindex start -->
	<customUI:headLine action="3" />
	<!-- navindex end -->
	<!-- tabp start -->
	<%-- <%request.setAttribute("tab","2-3");%>
	<%@include file="../person/accountCommon.jsp"%> --%>
	<input type="hidden" id="titleTab" value="1-2" />
	<!-- tabp end -->
	<!-- person-link start -->
	<div class="person-link">
		<ul class="container clearFloat">
			<li><a href="${ctx }/person/account/overview">账户中心</a>></li>
			<li><a href="javascript:;">我的理财</a>></li>
			<li><span>债权转让</span></li>
		</ul>
	</div>
	<!-- person-link end -->

	<!-- container start -->
	<div class="container clearFloat">
		<!-- pLeft start -->
		<div class="pLeft clearFloat"></div>
		<!-- pLeft end -->

		<!-- pRight start -->
		<div class="pRight clearFloat">
			<div class="p-Right-top">
				<p class="zqzr-title">债权转让</p>
				<div class="pre_zt">
					<div class="th-sorts clearFloat">
						<span class="a-head">转让状态</span>
						<a class="a-head c" href="javascript:;" value="">全部</a>
						<a href="javascript:;" class="c" value="6">可转让</a>
						<a href="javascript:;" class="c" value="8">转入记录</a>
						<a href="javascript:;" class="c" value="7">转出记录</a>
					</div>
					<div class="th-sorts clearFloat">
						<span class="a-head">排&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;序</span>
						<a class="a-head zqsort mr" href="javascript:;">默认</a>
						<a href="javascript:;" data-value="0" id="backDate" class="a-midden zqsort">回款日期</a>
						<a href="javascript:;" data-value="0" id="buyDate" class="a-midden zqsort">投标时间</a>
					</div>
				</div>
			</div>
			<div class="pre_zqlist">
				<ul class="pre_ul50">
					<li class="pre_ul_170">债权名称</li>
					<li class="pre_ul_80 cjzq_list_hide">借款人</li>
					<li>出借金额(元)</li>
					<li class="cjzq_list_hide2">待收回款(元)</li>
					<li class="cjzq_list_hide">已回金额(元)</li>
					<li class="cjzq_list_hide3">最近回款日</li>
					<li class="cjzq_list_hide">债权状态</li>
					<li class="cjzq_list_hide2  cjzq_list_hide">投标时间</li>
					<li class="pre_ul_50 ">明细</li>
				</ul>
				<ul class="pre_ulbig" id="pre_ul_big">

				</ul>

				<div style="height: 50px; clear: both;"></div>
				<div id="pageList" class="tcdPageCode mt-20"></div>
			</div>
			
		</div>
		<!-- pRight start -->
	</div>
	<!-- container end -->

	<div style="height: 10px; clear: both;"></div>
	<!-- footerindex start -->
	<%@include file="../common/footLine3.jsp"%>
	<!-- fbottom end -->

</body>

</html>
