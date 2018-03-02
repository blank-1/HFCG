<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@include file="../common/taglibs.jsp"%>
<!doctype html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" /> 
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	
	<title>账户中心-资金管理-银行卡管理</title>
	<%@include file="../common/common_js.jsp"%>
	<script type="text/javascript" src="${ctx}/js/person5.4.js"></script><!-- public javascript -->

</head>

<body class="body-back-gray">
<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="3"/>
<!-- navindex end -->

<!-- tabp start -->
<input type="hidden" id="titleTab" value="2-5" />
<!-- tabp end -->

<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a href="javascript:;">资金管理</a>></li>
        <li><span>银行卡管理</span></li>
    </ul>
</div>
<!-- person-link end -->

<!-- container start -->
<div class="container clearFloat">
    <!-- pLeft start -->
    <div class="pLeft clearFloat"></div>
    <!-- pLeft end -->

    <!-- pRight start -->
    <form id="editBank" action="${ctx}/bankcard/editBank" method="post">
    <div class="pRight clearFloat">
        <div class="p-Right-top">
			<p class="yhkgl-title">银行卡管理</p>
			<div class="clear_10"></div>
		</div>
		<p class="quickPay" style="line-height:5px;">
			<img src="${ctx }/images/user-center/pay_061.jpg" style="margin-left:25px;" /> 认证支付
			<small>为保证您的资金安全，我们与连连支付合作，您在平台绑定的银行卡将作为"连连支付-认证支付"的支付银行卡及平台提现银行卡使用</small>
		</p>
        <div class="p-Right-bot yhkgl-pd">
			<div class="input-group">
            	<label>
            		<span class="fsp1 kbj-span">开户名：</span>
            		<c:if test="${userExt.isVerified ne '1'}">
			          <small lass="small_name">＊您未进行身份验证，请先进行<a  data-mask='mask' data-id="shenfen"  href="javascript:;">身份验证</a>再进行绑卡充值操作</small>
			        </c:if>
			        <c:if test="${userExt.isVerified eq '1'}">
			          ${userExt.realName}
			        </c:if>
                </label>
            </div>
        	<div class="input-group mt-30">
            	<label for="bankid">
            		<span class="fsp1 kbj-span fash-3">银行卡号：</span>
                	<input type="text" id="bankid" flag="true" size="25" onKeyUp="formatBankNo(this)" onKeyDown="formatBankNo(this)"  name="bankid" class="ipt-input" value="${card.cardCode}" disabled="disabled" /><i id="bankshow"><img src="${ctx}/images/news_icon/bangka.png" class="buttonimgdetail ml-20" />${bank.constantName}</i>
                </label>
                <em class="hui kbj-em"></em>
               
            </div>
                 
        	<div class="input-group mt-20">
            	<label for="">
            		<span class="fsp1 lian_khh kbj-span" >开户省市：</span>
					<!-- select start -->
		            <dl class="select lian_sel"  >
		                <dt class="lian_dt" id="address" >--省份--</dt>
		                <dd style="top:40px;">
		                   <ul class="lian_ul" >
								  <c:forEach items="${province}" var="pro">
									  <li  <c:if test="${provinceSelect.provinceCityId eq pro.provinceCityId}"> data-select="select"</c:if>  data-id="${pro.provinceCityId}"><a href="javascript:;" onclick="getCity('${pro.provinceCityId}')">${pro.provinceName}</a></li>
								  </c:forEach>
							  </ul>
		                </dd>
		            </dl><!-- select end -->
					<!-- select start -->
		            <dl class="select lian_sel"  >
		                <dt  class="lian_dt" id="lian_city" >--城市--</dt>
		                <dd style="top:40px;">
		                   <ul class="lian_ul" id="cityList">
								  <c:forEach items="${citys}" var="ci">
									  <li  <c:if test="${city.provinceCityId eq ci.provinceCityId}"> data-select="select"</c:if>  data-id="${ci.provinceCityId}"><a href="javascript:;" onclick="getCity('${ci.provinceCityId}')">${ci.cityName}</a></li>
								  </c:forEach>
							  </ul>
		                </dd>
		            </dl><!-- select end -->
                </label>
				<div style="clear:both;"></div>
				<em class="hui kbj-em"></em>
            </div>
        	<div class="input-group mt-20">
            	<label for="text" style="position:relative;">
            		<span class="fsp1 kbj-span fash-3">开户支行：</span>
            		<input type="text" id="text" name="text" flag="true" class="ipt-input" value="${card.registeredBank}" /><span><img src="${ctx}/images/search.png" class="yh_search" width="30" height="30"/></span>
                </label>
				<em class="hui kbj-em iphone6-p" style=""></em>
				 <p class="yh_tip" style="">为提高您提现的效率，请您尽量从开户行列表中选择支行名称</p>
				<div class="yh_name">
					<p class="yh_mian_text">行名称</p>
					<p class="yh_mian_text">永安里行</p>
					<p class="yh_mian_text">永安行永安行永安行永安行永安行永安行永安</p>
					<p class="yh_mian_text">永安里行</p>
					<p class="yh_mian_text">永安里行</p>
					<p class="yh_mian_text">永行永安里行</p>
					<p class="yh_mian_text">永安里行</p>
					<p class="yh_mian_text">永安里行</p>
				</div>
            </div>
			<div class="clear_30"></div>
        	<div class="bankphonebtyn yhk-bjbtn">
                <button type="button" id="recharge" class="btn btn-error mt-30">保存</button>
            </div>
			<div class="clear_50"></div>
		</div>
    </div>
    	<input id="cityId" type="hidden" name="cityId"/>
		<input id="prcptcd" type="hidden" name="prcptcd"/>
		<input id="flag" type="hidden" name="flag" value="1"/>
    </form>
    <!-- pRight start -->
</div>
<!-- container end -->


<%@include file="../person/authenticationShenfen.jsp"%>
<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->

</body>
</html>

