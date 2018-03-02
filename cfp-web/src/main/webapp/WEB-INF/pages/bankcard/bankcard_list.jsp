<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<!doctype html>
<html xmlns="http://www.w3.org/1999/xhtml">
	
<head>
<meta charset="utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" /> 
<!-- <meta http-equiv="X-UA-Compatible" content="IE=7;IE=9;IE=8;IE=10;IE=11"> -->
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<title>账户中心-资金管理-银行卡管理</title>
<%@include file="../common/common_js.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/css/hDate.css" /><!-- index css -->
<script type="text/javascript" src="${ctx}/js/login.js"></script><!-- login javascript -->
<script type="text/javascript" src="${ctx}/js/bankM.js"></script><!-- public javascript -->
</head>

<body class="body-back-gray">
<!-- line2 start -->
<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="3"/>
<!-- navindex end -->

<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a href="javascript:;">资金管理</a>></li>
        <li><span>银行卡管理</span></li>
    </ul>
</div>
<!-- person-link end -->

<!-- tabp start -->
<%request.setAttribute("tab","4-5");%>
<input type="hidden" id="titleTab" value="2-5" />
<!-- tabp end -->

<!-- container start -->
<div class="container clearFloat">
    <!-- pLeft start -->
   <div class="pLeft clearFloat">
    </div>
    <!-- pLeft end -->
    
     <!-- pRight start -->
    <div class="pRight clearFloat">
        <div class="p-Right-top">
			<p class="yhkgl-title">银行卡管理</p>
			<small>*为保证您的资金安全，我们与连连支付合作，您在平台绑定的银行卡将作为"连连支付-认证支付"的支付银行卡及平台提现银行卡使用</small>
			<div class="clear_10"></div>
		</div>
        <div class="p-Right-bot yhkgl-mt0">
        
			<!-- thisbank start -->
			<div class="thisbank clearFloat">

				<c:if test="${not empty customerCardEnable}">
					<div class="bankblock bankuse">
						<div class="titlet clearFloat">
							<span class="img floatLeft" style="background-image:url(${ctx}/images/banklogo/${customerCardEnable.bankCode}.png)"></span>
							<span class="floatRight"><small>尾号：</small>${customerCardEnable.cardCodeShort}</span>
						</div>
						<p>累计提现：<big>${customerCardEnable.withdrawAmountSum}</big>元</p>
						<div class="clearFloat lastl">
							<span class="floatRight"><a href="${ctx}/person/toWithDraw" style="display:block;margin-left:20px;">提现</a></span> <%--<span class="floatRight" ><a href="${ctx}/bankcard/to_edit_bank">编辑</a></span>--%>&nbsp; &nbsp; &nbsp;
						</div>
					</div>
				</c:if>

				<c:if test="${empty customerCardEnable}">
					<div>
						没有可用银行卡
					</div>
				</c:if>

				<div class="bankdetailr">
					<h2>换卡流程</h2>
					<p>如遇银行卡丢失或需更换银行卡，请出示以下信息：<br/>
						1、手持“身份证以及已绑定银行卡”的清晰照片<br/>
						2、身份证清晰照片<br/>
						3、已绑定银行卡的清晰照片<br/>
						4、如银行卡丢失，需提供银行出示的挂失凭证照<br/>
						请将以上信息发送至财富派客服邮箱：myservice@mayitz.com，我们将在1-3个工作日进行信息审核及解绑操作，解绑前我们会与您电话沟通，请保持手机畅通，感谢您的配合。
						详情可拨打客服电话进行咨询：400-061-8080</p>
				</div>
			</div>
			<!-- thisbank end -->

			<!-- hostray start -->
			<div class="hostray clearFloat">
				<h2>历史银行卡</h2>

				<c:if test="${not empty customerCardDisableList}">
					<!-- 循环体-开始 -->
					<c:forEach items="${customerCardDisableList}" var="card" >
						<!-- hosbank start -->
						<div class="bankblock hosbank firsth">
							<div class="titlet clearFloat">
								<span class="img floatLeft" style="background-image:url(${ctx}/images/banklogo/${card.bankCode}.png)"></span>
								<span class="floatRight"><small>尾号：</small>${card.cardCodeShort}</span>
							</div>
							<p>累计提现：<big>${card.withdrawAmountSum}</big>元</p>
							<div class="clearFloat lastl img_mt">
								<span class="floatRight">解绑日期：<fmt:formatDate value="${card.updateTime}" pattern="yyyy-MM-dd"/> </span>
							</div>
						</div>
						<!-- hosbank end -->
					</c:forEach>
				<!-- 循环体-结束 -->
				</c:if>

				<c:if test="${empty customerCardDisableList}">
					没有历史银行卡
				</c:if>

			</div>
			<!-- hostray end -->
		</div>
		<div class="tcdPageCode mt-20"></div>
    </div>
    <!--  Rigth end  -->
</div>



<%@include file="../common/hengfengCard.jsp"%>
<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->

</body>
</html>

