<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="keywords" content="" />
    <meta name="description" content="" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	
	<title>账户中心-资金管理-订单查询</title>
	<%@include file="../common/common_js.jsp"%>
	<script type="text/javascript" src="${ctx}/js/login.js"></script><!-- login javascript -->
	<script type="text/javascript" src="${ctx}/js/script/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery_page.js"></script><!-- jquery_page javascript 分页 -->
    <script type="text/javascript" src="${ctx}/js/zh_center_ddcx.js"></script>
    <script type="text/javascript" src="${ctx}/js/hDate.js"></script><!-- hDate javascript -->
    <link rel="stylesheet" type="text/css" href="${ctx}/css/hDate.css" /><!-- index css -->
</head>

<body class="body-back-gray">
	<!-- line2 start -->
	<%@include file="../common/headLine1.jsp"%>
	<!-- line2 start -->
	<!-- navindex start -->
	<customUI:headLine action="3"/>
	<!-- navindex end -->
	<!-- tabp start -->
	<%request.setAttribute("tab","4-1");%>
	 <input type="hidden" id="titleTab" value="2-1" />
	<%-- <%@include file="accountCommon.jsp"%> --%>
	<!-- tabp end -->
	
	<!-- person-link start -->
	<div class="person-link">
	    <ul class="container clearFloat">
	        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
	        <li><a href="javascript:;">资金管理</a>></li>
	        <li><span>订单查询</span></li>
	    </ul>
	</div>
<!-- person-link end -->
<input type="hidden" id="hidem" data-value="545247000" />
<!-- container start -->
<div class="container clearFloat">
    <!-- pLeft start -->
    <div class="pLeft clearFloat">
    </div>
    <!-- pLeft end -->

    <!-- pRight start -->
    <div class="pRight clearFloat">
        <div class="p-Right-top">
			<p class="ddcx-title">订单查询</p>
			<div class="pre_zt_box_chaxun">
					<label class="label_1"><span class="label_title">订单查询</span><input type="text" placeholder="订单号或债权/产品标题"  id="orderCode"  /></label>
					<label class="label_2"><span class="label_title" style="float:left;">订单状态</span>
						<!-- select start -->
						<span>
							<dl class="select" id="Ftype" style="float:left;">
								<dt id="orderState" data-id="0">全部</dt>
								<dd>
								   <ul style="height:auto;">
									   <li data-id="0"><a href="javascript:">全部</a></li>
									   <li data-id="1"><a href="javascript:">未支付</a></li>
									   <li data-id="2"><a href="javascript:">支付成功</a></li>
									   <li data-id="3"><a href="javascript:">订单失效</a></li>
								   </ul>
								</dd>
							</dl><!-- select end -->
						</span>
						</label>
					<input type="hidden" id="orderCode1" value="">
					<input type="hidden" id="orderState1" value="">
					<input type="hidden" id="startDate1" value="">
					<input type="hidden" id="endDate1" value="">
					<label class="label_3">
						<span class="label_title">订单日期</span>
						<input type="text" value="" id="startDate"  class="hdate" onmousedown="return false" />
						<span class="fdsp">-</span>
						<input type="text" value="" id="endDate" class="hdate"onmousedown="return false" /> 
					</label>
					<label class="label_4"><button class="chaxun_btn"  onclick="ddlbcx()">查询</button></label>
			</div>
			<div class="clear_10"></div>
		</div>
        <div class="p-Right-bot">
			<div class="pre_zqlist_chaxun" id="zqlist_chaxun">
					<ul class="pre_tabbox_chaxun">
						<li class="pre_tabboxtitle_chaxun" >
							<ul>
								<li class="pre_tabbox1_150 ddcx-hide">日期</li>
								<li class="pre_tabbox1_150 ddcx-hide">订单号</li>
								<li class="pre_tabbox1_200">购买债权/产品</li>
								<li class="pre_tabbox1_80">订单金额(元)</li>
								<li class="pre_tabbox1_70 ddcx-hide ddcx-zqlc-hide">债权/理财</li>
								<li class="pre_tabbox1_80">订单状态</li>
								<li class="pre_tabbox1_100">操作</li>
							</ul>
						</li>
					</ul>
					<ul class="pre_tabbox2_chaxun flcontexts">
					</ul>
			</div>
		</div>
		<div id="lendorder_list_page">
			<div class="tcdPageCode mt-20"></div>
		</div>
    </div>
    <!-- pRight start -->
</div>
<!-- container end -->

	<!-- tab start -->
	<div style="text-align:center;"><a id="alertDialog" style="display: none;" href="javascript:;" data-mask='mask' data-id="with" >&nbsp</a></div>


	<div class="zhezhao"></div>
	<div class="zhezhao1"></div>
	<!-- masklayer start  -->
	<div class="masklayer" id="with">
		<h2 class="clearFloat"><span>提示</span><a id="close_withdraw" href="javascript:;" data-id="close"></a></h2>
		<div class="shenf_yanz_main">
			<div style="height:70px;clear:both;"></div>
			<p><img src="${ctx}/images/img/false.jpg" /><span id="tip"></span></p>
			<div style="height:50px;clear:both;"></div>
			<div class="input_box_shenf">
				<a href="javascript:;" data-id="close" id="queren_withdraw"><button>确认</button></a>
				<div style="height:90px;clear:both;"></div>
			</div>
		</div>
	</div>

<!-- masklayer start  -->
<%@include file="../login/login.jsp"%>
<!-- masklayer end -->
<div style="height:50px;clear:both;"></div>

<!-- fbottom start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->

</body>
</html>
