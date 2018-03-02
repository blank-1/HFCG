<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	
	<title>账户中心-验证信息</title>
	<%@include file="../common/common_js.jsp"%>
	<script type="text/javascript" src="${ctx }/js/infor_5_phone.js"></script><!-- action javascript -->
</head>

<body class="body-back-gray">
	<input type="hidden" id="hidem" data-value="545247000" />
	
		<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="3"/>
<!-- navindex end -->
<!-- tabp start -->
<%request.setAttribute("tab","5-1");%>
 <input type="hidden" id="titleTab" value="3-1" />

<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a href="javascript:;">个人信息</a>></li>
        <li><span>验证信息</span></li>
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
			<p class="yzxx-title">验证信息</p>
			
		</div>
        <div class="p-Right-yzxx">
			<div class="pre_zqlist">
				<ul class="yanz_infor">
					<li>
						<ul>
						    <li class="yz-infor-ml" id="identity">
								<c:if test="${idCard eq ''}"><img src="${ctx }/images/yanz_02.jpg" /></c:if>
							    <c:if test="${idCard ne ''}"><img src="${ctx }/images/yanz_01.jpg" /></c:if>身份验证
						    </li>  
							<li class="yz-infor-45">${idCard }</li>
							<li id="success"><c:if test="${idCard == ''}">
								<a href="javascript:;" data-mask='mask' data-id="hengfengCard" >身份认证</a></c:if>
								<c:if test="${idCard ne ''}">已认证</c:if>
							</li>
							
							
						</ul>
					</li>
					<li>
						<ul>
							<li class="yz-infor-ml"><img src="${ctx }/images/yanz_01.jpg" />手机验证</li>
							<li class="yz-infor-45">${mobileNo}</li>
							<%--<li id="success2">
								<a href="javascript:;" data-mask='mask' data-id="xiugai_phone" >修改</a>
							</li>--%>
						</ul>
					</li>
					<li>
						<ul>
							<li class="yz-infor-ml"><c:if test="${bind == '2'}"><img src="${ctx }/images/yanz_01.jpg" /></c:if>恒丰开户</li>
							<li class="yz-infor-45">
								<c:if test="${bind eq '2'}">已开户</c:if>
								<c:if test="${bind ne '2'}"><a href="javascript:;" data-mask='mask' data-id="hengfengCard" >未开户</a></c:if>
							</li>
						</ul>
					</li>
				</ul>
				<div class="clear_100"></div>
			</div>
		</div>
    </div>
    <!-- pRight start -->
</div>
<!-- container end -->
		
	<input type="hidden" id="token"  value="${token }" />
<%--<%@include file="authenticationShenfen.jsp"%>--%>
<%@include file="../common/hengfengCard.jsp"%>
<!-- masklayer end -->
<div class="zhezhao1"></div>
<!-- masklayer start  -->
<div class="masklayer" id="xiugai_phone">
	
		<h2 class="clearFloat"><span>修改手机号</span><a href="javascript:;" data-id="close"></a></h2>
		<div class="xiugai_phone_main" id="phont_hide">
			<form action="" class="form" method="post">
					<div class="input_box_phone">
					<div style="height:50px;clear:both;"></div>	
						<label>
							<span>手机号</span>
							<input type="text" id="phone" value="请输入手机号" autocomplete="off" onfocus="if(this.value==defaultValue) {this.value='';this.type=''}" onblur="if(!value) {value=defaultValue; this.type='text';}" style="width:222px;"/>
						</label>
						<em></em>
					</div>
					<div class="input_box_phone">
						<label>
							<span>验证码</span>
							<input type="text" id="valid" value="请输入验证码" autocomplete="off" maxlength="6" onfocus="if(this.value==defaultValue) {this.value='';this.type=''}" onblur="if(!value) {value=defaultValue; this.type='text';}" />
							<button type="button" id="getvalid" class="huoqu_yanzm" style="width:102px;">获取验证码</button>
						</label>
						<em></em>
					</div>
					<div class="input_box_phone">
						<div style="height:20px;clear:both;"></div>	
						<button type="button" id="submit-register">下一步</button>
						<div style="height:70px;clear:both;"></div>					
					</div>
			</form>
		</div>
		<div class="xiugai_phone_main" id="phont_show" style="display:none;">
				<div style="height:70px;clear:both;"></div>
				<p><img src="${ctx }/images/img/true.jpg" style=""/><span>手机号修改成功！</span></p>
				<div style="height:30px;clear:both;"></div>
				<p>该手机号将作为您登录平台的用户名，及账户资金相关验证码接收手机号，<br />请妥善保管您的手机，避免账户损失，谢谢！</p>
				<div style="height:50px;clear:both;"></div>
				<div class="input_box_phone">
					<div style="height:20px;clear:both;"></div>	
					<a href="javascript:;" data-id="close"  id="queren2"><button>确认</button></a>
					<div style="height:70px;clear:both;"></div>					
				</div>
		</div>

</div>
<!-- masklayer end -->

<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->

</body>
</html>