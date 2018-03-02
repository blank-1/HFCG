<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="../common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0"> 
	<title>用户注册 - 财富派</title>
	<%@include file="../common/common_js.jsp"%>
	<!-- public javascript -->
	<!-- <script type="text/javascript" src="${ctx}/js/register.js"></script>action javascript -->
	<script type="text/javascript" src="${ctx}/js/register02.js"></script><!-- action javascript -->
</head>


<body>
<header>
  <!-- navtopbg start-->
  <%@include file="../common/headLine1.jsp"%>
</header>
<!-- navindex start -->
<customUI:headLine action="999" />
<!-- navindex end -->

<div class="clear"></div>

<%@include file="../login/login.jsp"%>
<!-- article start -->
<div class="zhuce_bj">
	<!-- article start -->
	<div class="container clearFloat zhuce_mainbg">


  <!-- register-left start -->
  <div class="register-left">
    <h2>财富派账号<small class="floatRight">已有账号
      <%--<a href="login.html">登录财富派</a>--%>
      <a href="${ctx }/user/to_login" data-mask='' data-id="">登录财富派</a>
    </small></h2>

    <form id="registForm" action="${ctx}/user/regist/register" class="form mt-40" method="post">
      <input type="hidden" name="token" value="${token}"   />
      <input type="hidden" name="inviteUserId" value="${inviteUserId}" />
      <input type="hidden" name="validCode"  id="validCode"  />
				<div class="input-group">
					<label for="username"> <span>用户名：</span> <input type="text"
						value="" id="username" maxlength="20" name="loginName" flag = "true"
						placeholder="请输入用户名" class="ipt-input" />
					</label> <em class="hui">请输入4~20位字符，支持汉字、字母、数字及"-"、"_"组合</em>
				</div>

				<div class="input-group">
					<label for="phone"> <span>手机号：</span> <input type="text"
						value=""  id="phone" maxlength="11" name="mobileNo" flag = "true"
						placeholder="请输入手机号" class="ipt-input" />
					</label> <em class="hui">该手机号将用于手机号登录和找回密码</em>
				</div>
            <!-- input-group end -->

	     <div class="input-group clearFloat">
        <label for="password" class="floatLeft" id="passexchangelbl">
          <span class="floatLeft linheight">密&nbsp;&nbsp;&nbsp;码：</span>
         <input  maxlength="16" type="password" autocomplete="off" name="loginPass" id="password"  flag = "true" placeholder="请输入密码"  value=""  class="ipt-input widpass pwlogin2"  style="" onKeyUp="CheckIntensity(this.value)" /><i id="passexchange"></i>
        </label>
        <div type="button" id="rejc" class="Tcolor floatLeft mt-0">无</div>
        <div class="clear"></div>
        <em class="hui">请输入6~16位字符，支持字母及数字,字母区分大小写</em>
      </div>

				<!-- input-group start -->
				<div class="input-group clearFloat">
					<label for="valid"> <span class="floatLeft linheight">验证码：</span>
						<input type="text" autocomplete="off" value="" id="valid"
						name="valid" maxlength="4" placeholder="请输入验证码"
						class="ipt-input widthvalid" />
					</label> <a class="picturevalid" href="javascript:;"  ><img
						class="imgCodeValid" src="" /></a> <em></em>
				</div>
				<!-- input-group end -->

      <div class="input-group">
        <label for="visate">
          <span>邀请码：</span>
          <c:if test="${not empty invite_code}">	
            <input type="text" value="${invite_code}" readonly="readonly" id="visate" maxlength="6" name="inviteCode" placeholder="邀请码" class="ipt-display-input ipt-disabled" />
            <i id="viste_i_zc" class="viste_zc_top"></i>
          </c:if>
          <c:if test="${ empty invite_code}">
            <input type="text" value="${invite_code}"  id="visate" maxlength="6" name="inviteCode" placeholder="邀请码" class="ipt-display-input display-none" />
            <i id="viste_i_zc" class="viste_zc_bottom"></i>
          </c:if>
<style>

  .ipt-display-input{  border: 1px solid #dddddd;
    color: #333333;
    height: 35px;
    line-height: 35px;
    padding-left: 9px;}

</style>

        </label>
        <em></em>
      </div>
<style>input.ipt-disabled{ background:#f1f1f1; cursor: no-drop;}

</style>
				<div class="btn-group">
					<label for="">
						<span class="shu"></span><a data-mask="mask" data-id="payshowstate" href="javascript:;">《财富派注册协议》</a>
					</label>
					<button type="button" id="submit-register"
						class="btn btn-error widbtn">确认协议并注册</button>
				</div>
			</form>
		</div>
		<!-- register-left end -->

		
		<div class="clear"></div>
	</div><!-- article end -->
</div>


	<style>
#phonemask {
	
}

.footer_small{margin-top:0px;}

.picturevalid {
	display: inline-block;
	height: 36px;
	width: 95px;
}

.picturevalid img {
	width: 100%;
	height: 100%;
	vertical-align: middle;
	margin-top: -3px;
}

.phonevacs {
	margin-top: 20px;
	text-align: center;
}

.phonevacs .ph4 {
	font-size: 28px;
}

.phonevacs .pipt {
	width: 50px!important;
	margin-left: -5px;
	height: 50px;
	border: 1px solid #ccc;
	margin-top: 15px;
	margin-bottom: 10px;
	text-align: center;
	font-size: 20px;
}

.phonevacs #piptime {
	margin-left: 30px;
}

.phonevacs #piptime4 {
	margin-left: -6px
}

.phonevacs .pcolor {
	color: #C30;
}

.phonebotom {
	text-align: center;
	border-top: 1px solid #ccc;
	margin: 15px 30px;
}

.phonebotom a,.phonebotom .validc {
	border: none;
	background: none;
	margin-top: 10px;
	display: inline-block;
	line-height: 25px;
	padding: 0 20px;
	text-decoration: none;
	color: #333;
	cursor: pointer
}

.phonebotom a.close {
	margin-left: 30px;
}

.phonebotom .validc {
	width: 180px;
	margin-right: 50px;
}

.phonebotom #phclose {
	width: 53px
}

.phonebotom a:hover,.phonebotom .validc:hover {
	background: #f1f1f1;
}

.phonebotom a:active,.phonebotom .validc:active {
	color: #03004C;
}

.phonebotom #title {
	padding-left: 121px;
	padding-top: 15px;
	margin-top: 10px;
	border-top: 1px dashed #ccc;
	text-align: left
}

.phonebotom .dhidden {
	display: none
}
.phonebotom .dhidden1{
	display: none
}
.heimask {
	padding-bottom: 0 !important;
}

.imgre {
	display: inline-block;
	height: 25px;
	width: 25px;
}

.validc[disabled] {
	cursor: no-drop;
	background: #f1f1f1
}
</style>

	<!-- masklayer start  -->
	<div class="masklayer masklback heimask" id="phonemask">
		<h2 class="clearFloat">
			<span>手机验证完成注册</span> <a href="javascript:;" id="phclose2"></a>
		</h2>
		<!-- phonevacs start -->
		<div class="phonevacs">
			<h4 class="ph4">请输入短信验证码</h4>
			<p class="ptitle">(已发送至 138****6325)</p>
			<input type="text" autocomplete="off" maxlength="1"
				onkeyup="value=value.replace(/[^0-9]/g,'')" class="pipt"
				id="piptime"> <input type="text" autocomplete="off"
				maxlength="1" onkeyup="value=value.replace(/[^0-9]/g,'')"
				class="pipt" id="piptime2"> <input type="text"
				autocomplete="off" maxlength="1"
				onkeyup="value=value.replace(/[^0-9]/g,'')" class="pipt"
				id="piptime3"> <input type="text" autocomplete="off"
				maxlength="1" onkeyup="value=value.replace(/[^0-9]/g,'')"
				class="pipt" id="piptime4"> <input type="text"
				autocomplete="off" maxlength="1"
				onkeyup="value=value.replace(/[^0-9]/g,'')" class="pipt"
				id="piptime5"> <input type="text" autocomplete="off"
				maxlength="1" onkeyup="value=value.replace(/[^0-9]/g,'')"
				class="pipt" id="piptime6"> <span class="imgre"></span>
			<div class="pcolor" style="width:315px;float:left;padding-left:85px;">短信验证码已发送，请注意查收</div>
			<div style="width:199px;float:right;"><a href="http://help.caifupad.com/guide/common/reg/" target="_black" style="text-decoration:none;" >收不到验证码？</a></div>
		</div>
		<div style="clear:both;"></div>
		<!-- phonevacs end -->
		<div class="phonebotom">
			<button type="button" id="getvalids" class="validc">60秒后重新获取验证</button>
			| <a href="javascript:;" id="phclose" class="close">关闭</a>
			<div class="dhidden">

				<p id="title">验证码发送过于频繁，请输入图形验证码再次获取短信</p>
				<!-- input-group start -->
				<div class="input-group clearFloat">
					<label for="valid3"> <input type="text" autocomplete="off"
						value="" id="valid3" name="valid3" maxlength="4"
						placeholder="请输入验证码" class="ipt-input widthvalid" />
					</label> <a class="picturevalid" href="#"><img class="imgCodeValid"
						src="" /></a><br> <span class="zhuan"><img
						src="${ctx}/images/res_03.png" /></span> <em style="display: inline; float:left;margin-left:10px; width:auto;"></em>
				</div>
				<!-- input-group end -->
			</div>
			<div class="dhidden1" style="display: none;">
				<p id="title">您已达到今日短信验证码发送上限，如有问题请联系客服。</p>
			</div>
		</div>
	</div>
	<!-- masklayer end -->
	<input type="hidden" id="regitxt" data-val="2" /><!-- 2:不可禁用　1:禁用-->
 <script>
  </script>
  <!-- register-left end -->
<!--registerer protocol-->
  <%@include file="../common/registerProtocol.jsp"%>
 

<!-- footer start -->
<%@include file="../common/footLine3.jsp"%>
<!-- footer end -->
</body>
</html>
