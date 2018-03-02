<%@ page import="com.xt.cfp.core.constants.VoucherConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta charset="utf-8" />
  <meta name="keywords" content="" />
  <meta name="description" content="" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<title>账户中心-资金管理-财富券</title>
  <%@include file="../common/common_js.jsp"%>
  <script type="text/javascript" src="${ctx}/js/bankM.js"></script><!-- public javascript -->
  <script type="text/javascript" src="${ctx}/js/hDate.js"></script><!-- hDate javascript -->
  <script type="text/javascript" src="${ctx}/js/person4.1.js"></script><!-- person4 javascript -->
  <script type="text/javascript" src="${ctx}/js/jquery_page.js"></script><!-- public javascript -->
  <%
    //状态
    VoucherConstants.VoucherFrontStatus[] voucherStatus = VoucherConstants.VoucherFrontStatus.values();
    request.setAttribute("voucherStatus",voucherStatus);
  %>
</head>
<body class="body-back-gray">
<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="3"/>
<%@include file="../login/login.jsp"%>
<!-- navindex end -->
<!-- tabp start -->
<%request.setAttribute("tab","4-4");%>
 <input type="hidden" id="titleTab" value="2-4" />
<%-- <%@include file="accountCommon.jsp"%> --%>
<!-- tabp end -->

<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a href="javascript:;" id="crumbs_1">我的理财</a>></li>
        <li><span id="crumbs_2">财富券1</span></li>
    </ul>
</div>
<!-- person-link end -->

<!-- container start -->
<div class="container clearFloat">
    <!-- pLeft start -->
    <div class="pLeft clearFloat">
    </div>
    <!-- pLeft end -->

    <!-- pRight start -->
    <div class="pRight clearFloat">
        <div class="p-Right-top">
			<p class="cfq-title">优惠券</p>
			<div class="pre_zt_box_chaxun">
				<label class="label_2 label_2_width"><span class="label_title" style="float:left;">优惠券类型</span>
						<!-- select start -->
						<span>
							<dl class="select" id="VCtype" style="float:left;">
								<dt  id="zindex1"  data-id="-1">全部</dt>
								<dd >
								   <ul style="height:119px;">
									   <li data-id="-1"><a href="javascript:;">全部</a></li>
							            <li data-id="1"><a href="javascript:;">财富券</a></li>
							            <li data-id="2"><a href="javascript:;">加息券</a></li>
							            <li data-id="3"><a href="javascript:;">提现券</a></li>
								   </ul>
								</dd>
							</dl><!-- select end -->
						</span>
					</label>
					
					<label class="label_2 label_2_width"><span class="label_title" style="float:left;">优惠券状态</span>
						<!-- select start -->
						<span>
							<dl class="select" id="Ftype" style="float:left;">
								<dt  id="zindex"  data-id="-1">全部</dt>
								<dd>
								   <ul style="height:89px;">
									   <li data-id="-1"><a href="javascript:;">全部</a></li>
							            <c:forEach items="${voucherStatus}" var="type">
							                <li data-id="${type.value}"><a href="javascript:;">${type.desc}</a></li>
							            </c:forEach>
								   </ul>
								</dd>
							</dl><!-- select end -->
						</span>
					</label>
					<label class="label_3">
						<span class="label_title">获取时间</span>
						<input type="text" value="" id="date1" class="hdate"  />
						<span class="fdsp">-</span>
						<input type="text" value="" id="date2" class="hdate" /> 
					</label>
					<label class="label_4"><button class="chaxun_btn" id="select" >查询</button></label>
			</div>
			<div>
				<a href="http://help.caifupad.com/guide/caifuquan/" style="float:right;" target="_blank">如何获取优惠券？</a>
			</div>
			<div class="clear_0"></div>
		</div>
        <div class="p-Right-bot">
			<div class="pre_zqlist_cfq">
					<ul class="pre_tabbox_cfq">
						<li class="pre_tabboxtitle_cfq" >
							<ul>
								<li>优惠券</li>
								<li class="cfq-hide">面额</li>
								<li class="cfq_li_170 cfq-hide">获取时间</li>
								<li>到期时间</li>
								<li class="cfq-hide">来源</li>
								<li>状态</li>
								<li class="cfq_li_205">备注</li>
							</ul>
						</li>
					</ul>
					<ul class="pre_tabbox2_cfq" id="pageList">
						
						<li>
							<ul class="cfq-border">
								<li>5元</li>
								<li class="cfq-hide">5元</li>
								<li class="cfq_li_170 cfq-hide">2015-12-18 20:58:01</li>
								<li>2016-02-01</li>
								<li class="cfq-hide">活动奖励</li>
								<li>已过期</li>
								<li class="cfq_li_205">满500元使用</li>
							</ul>
						</li>
						
						
					</ul>
			</div>
			<div class="tcdPageCode mt-20"></div>
		</div>
    </div>
    <!-- pRight start -->
</div>
<!-- container end -->


<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->

</body>
</html>

