<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8" />
  <meta name="keywords" content="" />
  <meta name="description" content="" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
  <title>出借债权 - 财富派</title>
  <%@include file="../common/common_js.jsp"%>
  <script type="text/javascript" src="${ctx}/js/jquery_page.js"></script>
  <script type="text/javascript" src="${ctx}/js/zh_center_cjzq_mx.js"></script>
  <script type="text/javascript" src="${ctx}/js/login.js"></script>
</head>

<body  class="body-back-gray">
<input type="hidden" id="hidem" data-value="545247000" />
<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="3"/>
<!-- navindex end -->
<!-- tabp start -->
<%request.setAttribute("tab","2-1");%>
<input type="hidden" id="titleTab" value="1-0" />
<%-- <%@include file="../person/accountCommon.jsp"%> --%>
<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a href="javascript:;">我的理财</a>></li>
        <li><span>省心计划</span></li>
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
        	  <c:if test="${count.countNum>0}">
        		<a href="${ctx }/finance/downloadMyCreditRights" class="l_downloadPDF" target="_blank"><i></i>下载回款中债权</a>
        	</c:if>
			<p class="cjzq-title">出借债权</p>
			<div class="pre_zt">
				<div class="th-sorts clearFloat">
					<span class="a-head">债权状态</span>
					<a  href="javascript:;"  name="zt"    class="c a-head" value="">全部</a>
					<a href="javascript:;"  name="zt"     class="c" value="1">回款中</a>
					<a href="javascript:;"  name="zt"     class="c" value="2">回款逾期</a>
					<a href="javascript:;"  name="zt"     class="c" value="3">已结清</a>
				</div>
				
				<div class="th-sorts clearFloat">
					<span class="a-head">排&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;序</span>
					<a class="a-head zqsort mr" href="javascript:;" >默认</a>
					<a href="javascript:;" data-value="0" id="backDate"  class="zqsort a-midden ">回款日期</a>
					<a href="javascript:;" data-value="0" id="buyDate"  class="zqsort a-midden ">投标时间</a>
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
					<li class="cjzq_list_hide3" >最近回款日</li>
					<li class="cjzq_list_hide">债权状态</li>
					<li class="cjzq_list_hide2 cjzq_list_hide">投标时间</li>
					<li class="pre_ul_50 ">明细</li>
				</ul>
				<ul class="pre_ulbig" id="pre_ul_big"></ul>
				<div style="height:50px;clear:both;"></div>
				
			</div>
		<div class="tcdPageCode mt-20"></div>
    </div>
    <!-- pRight start -->
</div>
<!-- container end -->

<div style="height:10px;clear:both;"></div>
<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->

</body>

</html>
