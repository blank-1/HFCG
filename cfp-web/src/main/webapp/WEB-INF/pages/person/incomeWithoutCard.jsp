<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta charset="utf-8" />
  <meta name="keywords" content="" />
  <meta name="description" content="" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">

<title>账户中心-资金管理-充值</title>
  <%@include file="../common/common_js.jsp"%>
  <link rel="stylesheet" type="text/css" href="${ctx}/css/hDate.css" /><!-- index css -->
  <script type="text/javascript" src="${ctx}/js/recharge.js"></script><!-- public javascript -->
  <script type="text/javascript" src="${ctx}/js/jquery_page.js"></script><!-- public javascript -->
  <script type="text/javascript" src="${ctx}/js/llbankinfo.js"></script><!-- public javascript -->
</head>
<% session.removeAttribute("smsCode");%>
<body onload="start()" class="body-back-gray">
<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->
<script type="text/javascript" >
$(function(){

		var ifIep=$("#ieshowpay");
		var Sys = {};
		var ua = navigator.userAgent.toLowerCase();
		var s;
		(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
		(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
		(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
		(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
		(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
		
		//以下进行测试
		if (Sys.ie ){
			if(parseInt(Sys.ie)>=8){
				$("#browse_ie_tip").hide();
				ifIep.show();
			}else{
				$("#browse_ie_tip").show();
				ifIep.show();
			}
				
		}else if (Sys.firefox){
			ifIep.show();
		}else if (Sys.chrome){
			ifIep.show();
		}else if (Sys.opera){
			ifIep.show();
		}else if (Sys.safari){
			ifIep.show();
		}else{
			ifIep.show();
		}
})
</script>

<!-- navindex start -->
<customUI:headLine action="3"/>
<!-- navindex end -->


<!-- tabp start -->
<%request.setAttribute("tab","4-2");%>
 <input type="hidden" id="titleTab" value="2-2" />
<%@include file="../login/login.jsp"%>
<!-- tabp end -->

<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a href="javascript:;">资金管理</a>></li>
        <li><span>充值</span></li>
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
			<p class="cz-title">充值</p>
		</div>
        <div class="p-Right-bot yhkgl-mt0">
			<!-- rechan_grc start -->
		   <div class="rechan_gro">
				 <!-- bankpayd start -->
				<div class="bankpayd2">
					<!-- 网银支付 -->
					<h2 class="botyibao clearFloat s_hide" id="h21">
						<input id="rdi-inter" class="label2_1" type="radio" name="bankPayChannel" />
						<img class="renzheng" src="${ctx }/images/pay/internatePay.jpg" />
						<i>跳转至银行页面，支付限额高</i>
					</h2>
					<div class="inter-group">
							<p class="ieshowpay" id="ieshowpay">
								<span id="browse_ie_tip">由于各银行支持浏览器限制，建议您使用IE（8.0及以上版本）进行网银支付</span>
								<a href="javascript:;" class="inter_more">展开更多>></a>
							</p>
							<div class="clear_10"></div>
							<%@include file="../common/bankList.jsp" %>
							<div class="clear_20"></div>
							<form action="" method="post" class="" id="rechargeEBank">
							<input type="hidden" name="bkcode" id="bkcode" value="" />
								<div class="input-group">
									<label>
										<c:if test="${userExt.isVerified ne '1'}">
										<small>＊您未进行身份验证，请先进行
											<a  data-mask='mask' data-id="shenfen" href="javascript:;">身份验证</a>再进行充值操作
										</small>
	                            		</c:if>
									</label>
									<label for="rechange_amount">
										<span class="cz-span fash-3">充值金额：</span>
										<input type="text" id="moneybk" name="rechargeAmount" maxlength="10" style="float:left;" onkeyup="this.value=this.value.replace(/[^\d\.]/g,'') " onafterpaste="this.value=this.value.replace(/[^\d\.]/g,'') "  class="ipt-input" />
									</label>
									<div class="clear_0"></div>
									<em class="hui cz-em">*充值最小金额不得小于<font color="#fe2a4d">100</font>元</em>
								</div>
								<div class="mb-30">
									<button type="button" id="re_mo_btn" class="btn btn-error user-cz-btn">确认支付</button>
								</div>
							</form>
				   </div><!-- 网银支付 end -->
				</div>
				<!-- bankpayd start -->

			 <!-- bankpayd start -->
				<div class="bankpayd2">
					<h2 class="botyibao clearFloat s_show" id="show">
						<input id="rdi-quick" class="label2_1" checked type="radio"  name="bankPayChannel" />
						<img class="renzheng" src="${ctx }/images/pay/quickPay.jpg" />
						<i>一键支付，快捷方便，付款过程安全流畅</i>
					</h2>
					<!-- quick-group start -->
					<div class="inter-group clearFloat">
					<form id="rechargeForm" method="post">
					
						  <input type="hidden" name="payType" id="payType" value="2"/>
						  <input type="hidden" name="token" id="token" value="${token}"/>
						
						  <input type="hidden" id="regitxt" data-val="<c:if test="${userExt.isVerified eq '1'}">2</c:if><c:if test="${userExt.isVerified ne '1'}">1</c:if>"/>
   
						
						<c:if test="${empty customerCard}">
				            <%@include file="noCardIncome.jsp" %>
				        </c:if>
				        <c:if test="${not empty customerCard}">
				            <%@include file="hasCardIncome.jsp" %>
				        </c:if>
        
      				  <div class="clear_0"></div>
					 <div class="bankphonebtyn cz-btn mb-30">
							<button type="button" id="recharge"  class="btn btn-error">确认协议并充值</button>
							<a  href="javascript:;" data-mask="mask" data-id="lianlian" >《连连支付认证支付开通协议》</a>
					</div>
					<div class="clear_30"></div>
				</form>
				</div>
				
			</div><!-- bankpayd end -->
		</div>    
		<!-- rechan_grc end -->
       <form id="form_result" action="" method="post">
		   <input type="hidden" id="errorMsg" name="errorMsg" value=""/>
	  </form>
		
	  <div class="zhezhao5">
	    <img class="zheimg" src="${ctx}/images/2013092022533423.gif"/>
	    <p>支付中...</p>
	  </div>
			  
			  
		</div>
		<div class="tx_list">
			<div class="p-Right-top" style="margin-top:0px;">
				<p class="czlist-title">充值记录</p>
			</div>
			<div class="txjl-list">
				<ul class="txjl-ul-big" id="rechargeRecords">
					<li>
						<ul class="tx-ul">
							<li>充值时间</li>
							<li>充值金额(元)</li>
							<li>充值银行</li>
							<li>状态</li>
						</ul>
					</li>
				</ul>
				<div class="clear_0"></div>
			</div>
			<div class="tcdPageCode mt-20"></div>
		</div>
		
	</div>
    <!-- pRight end -->
    </div>
    <!-- container end -->
    <script type="text/javascript">
    $(function(){

      $(".zhezhao5").css("height",document.body.clientHeight);
      $(".zhezhao5 .zheimg").css("margin-top",document.body.clientHeight/2-100);
    })
  </script>
  <script type="text/javascript">
    <c:if test="${userExt.isVerified ne '1'}">
    $("input.ipt-input[type=text]:visible,button.btn,input[name='rechargeAmount']").each(function() {
      $(this).attr("disabled",true);
    });
    </c:if>
  </script>
  



<!-- masklayer start  -->
<div class="masklayer masklback heimask" id="interdvi">
	<h2 class="clearFloat"><span>提示</span> <a href="javascript:;" data-id="close" style="display:none;"></a></h2>

	<!-- phonevacs end -->
    <div class="interp">
    	<p>请您在新打开的网上银行页面进行充值，充值完成前请不要关闭此窗口</p>
        <div class="btn-inter-group">
        	<a href="javascript:;" class="btn btn-error payresult"  >支付成功</a>
        	<a href="http://help.caifupad.com/guide/common/wangyinzhifu/" class="payresult"  target="_blank">支付遇到问题?</a>
        </div>
    </div>
</div>
<%@include file="../common/lianlianProtocol.jsp"%>

<div class="masklayer masklback heimask" id="payshowstate">
    <h2 class="clearFloat"><span>提示</span> <a href="javascript:;" data-id="close" style="display:none;"></a></h2>

    <div class="interp">
        <p>请您在新打开的页面进行支付，支付完成前请不要关闭此窗口</p>
        <div class="btn-inter-group">
            <a href="javascript:;" class="btn btn-error" id="llPaySuccess">支付成功</a>
            <a href="http://help.caifupad.com/guide/common/wangyinzhifu/" id="llPayQuestion"  target="_blank" >支付遇到问题?</a>
        </div>
    </div>
</div>
<!-- masklayer start  -->
<!-- p_bank start -->
<%@include file="../common/limit.jsp"%>
<!-- p_bank end -->

<%@include file="authenticationShenfen.jsp"%>
<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->

<script>
  //分页

  $(function(){
		$("#rdi-quick").click();
	});
</script>



<!-- 找回交易密码-开始 -->
<script src="${ctx}/js/ed_ex_psd.js" type="text/javascript"></script>
<div class="zhezhao6"></div>
<div class="masklayer masklback" id="ed_ex_psd">
		<h2 class="clearFloat"><span>找回交易密码</span><a href="javascript:;" data-id="close"></a></h2>
		<div class="xiugai_phone_main" id="xigai1">
        	<img src="${ctx}/images/ed_ex_psd1.jpg" class="mt-30 mb-30"/>
			<form action="" class="form" method="post">
				<div class="input_box_phone">
					<label>
						<span>绑定的手机号码</span>
						<i class="tal ex-plone">${sessionScope.currentUser.encryptMobileNo}</i>
					</label>
					<em></em>
				</div>
				<div class="input_box_phone">
					<label>
						<span>手机验证码</span>
						<input type="text" id="ex_valid" value="" autocomplete="off" maxlength="6" placeholder="请输入验证码" style=""/>
						<button type="button" id="ex_get_valid" class="huoqu_yanzm" >获取验证码</button>
					</label>
					<em></em>
				</div>
				<div class="input_box_phone ipt_box_phone">
					<button type="button" id="next_sub1">下一步</button>
				</div>
			</form>
		</div>
		<div class="xiugai_phone_main display-none" id="phone_d2">
        	<img src="${ctx}/images/ed_ex_psd2.jpg" class="mt-30 mb-30"  />
			<form action="" class="form" method="post">
					<div class="input_box_phone input_box ipt-box-ex">
						<label>
							<span>输入新交易密码</span>
							<input type="password" class="width200" id="ed_ex_psd1" value="" maxlength="16" style="width:160px;" autocomplete="off" value="" onFocus="if(value==defaultValue){value=''}" onBlur="if(!value){value=defaultValue}" onKeyUp="ed_ex_Check(this.value)" />
								
								<div type="button" id="rejc_ex_psd" class="Tcolor floatLeft">无</div>
						</label>
						<em class="hui fontsize12">交易密码为6 -16 位字符，支持字母及数字,字母区分大小写</em>
					</div>
					<div class="input_box_phone">
						<label>
							<span>再次输入新交易密码</span>
							<input type="password" class="width200" value="" onFocus="if(value==defaultValue){value=''}" onBlur="if(!value){value=defaultValue}" id="ed_ex_psd2" value="" maxlength="16" autocomplete="off" />
						</label>
						<em></em>
					</div>
					<div class="input_box_phone ipt_box_phone">
						<button type="button" id="next_sub2">下一步</button>
					</div>
			</form>
		</div>
		<div class="xiugai_phone_main display-none" id="phone_d3">
				
        	<img src="${ctx}/images/ed_ex_psd3.jpg" class="mt-30 mb-30" />
			<p class="mt-50"><img src="${ctx}/images/img/true.jpg" /><span>交易密码重置成功！</span></p>
				
			<div class="input_box_phone ipt_box_phone" style="margin-top:85px;">
				<a href="javascript:;" id="ed_ex_psda"><button>确认</button></a>
			</div>
		</div>
</div>

<!-- 找回交易密码-结束 -->
	<form id="recharge_result" action="${ctx}/payment/getRechargeResult" method="post">
	  <input type="hidden" id="type"  name="type" value="recharge"/>
	  <input type="hidden" id="rechargeCode"  name="rechargeCode"  value=""/>
	</form>
<style>
/*------------------------------------------- 充值（快捷支付和网银支付） -------------------------------------------*/
.quick-css,.inter-css{ border:1px solid #FE2A4D; margin-top:15px; padding-left:15px;}
.label2_1{ margin-right:7px;}
.cardbind2 .bindcard{top:156px;}

</style>
</body>
</html>
