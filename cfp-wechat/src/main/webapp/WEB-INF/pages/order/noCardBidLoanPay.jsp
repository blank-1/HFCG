<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>

<!doctype html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<meta name="apple-mobile-web-app-capable" content="yes" />    
	<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
	<meta name="format-detection" content="telephone=no"/>
	<meta name="msapplication-tap-highlight" content="no" />
	<%@include file="../common/common_js.jsp"%>
<title>确认支付</title>
<link rel="stylesheet" href="${ctx }/css/Payment.css" type="text/css">
<style>
/*确认支付*/
.imgtop{
	width: 100%;
	height: 6rem;
	background: #e2f3fa url(../images/titlebg2.png)  no-repeat 95% 0;
	background-size: 2.5rem 3.5rem;

}
.imgtop p{
	color: #949DC6;
	padding-left: 1rem;
}
.imgtop p:first-child{
	padding: 1rem 0 0 1rem;
}
.telnum2{
	margin:0 0 4% 0;
}
/*银行协议*/
.masks{
	width: 100%;
	height: 100%;
	background: rgba(0,0,0,.5);
	position: fixed;
	top: 0;
	left: 0;
	display: none;
}
.agreements,.agreementss{
  width: 100%;
  height: 70%;
  background: #fff;
  position: absolute;
  bottom: 0;
  left: 0;
  overflow: hidden;
  text-align: center;
}
.agreements img{
  width: 100%;
}

.agreementss p,.agreements p{
  font-size: 1.6rem;
  color: #616E9C;
  text-indent: 1rem;
  position: relative;
  font-weight: bold;
  text-align: left;
}
.agreementss p span,.agreements p span{
	width: 2.2rem;
	height: 2.2rem;
	background:url(../images/close.png) no-repeat;
	display: block;
	background-size: cover;
	position: absolute;
	right: 1rem;
	top: 0;
}
.agreementss article{
	padding: 1rem 1.5rem;
	line-height: 1.5rem;
	color: #666;
}
.l_title::before{content: "省"!important;position: absolute;width: 1.4rem;height: 1.4rem;border: solid 1px #ff5e61;color: #ff5e61;font-size: 1rem;line-height: 1.4rem;text-align: center;top: 50%;margin-top: -.7rem;left:0;text-indent: 0;border-radius: 2px;}
</style>
</head>
<% session.removeAttribute("smsCode");%>
<body style="background:#f7f7f7;overflow-x:hidden;">
	<input type="hidden" value="0" id="syfp">
	<input type="hidden" value="<fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/>" id="payAccount" />
	<span id="alart"></span>
	<div class="payWrapper l_NewScroll" style="width:100%;height:100%;background:#f7f7f7;position:absolute; overflow-y:auto; -webkit-overflow-scrolling: touch; top:0; left:0; bottom:0; right:0">
		<%-- <div class="imgtop"><!-- 头部信息 -->
			<p>&nbsp;</p>
       		<p>${order.lendOrderName}&nbsp;	&nbsp;${order.timeLimit}
       			<c:if test="${order.timeLimitType eq '1'}">天</c:if>
           		<c:if test="${order.timeLimitType eq '2'}">个月</c:if>
       		</p>
		</div>
		<div class="actualPay">
			<p class="paymentinfo" style="margin:4% 0 0 0;">支付金额：&nbsp;<span id="pay"><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/></span>元</p>
			<p class="paymentinfo" style="margin:0 0 2% 0;">预期收益：&nbsp;<span>${expectedInteresting}元</p>
		</div> --%>
		<header>
	          <ul>
	               <li
	               		<c:if test="${not empty sxFlag}">class="l_title text-overflow"</c:if>
	               		<c:if test="${empty sxFlag}">
	               			<c:if test="${loanApplication.loanType ne '0' && loanApplication.loanType ne '1' && loanApplication.loanType ne '7'}">class="l_con"</c:if>	
	               		</c:if>
	               >
	               		${order.lendOrderName}
	               </li>
	               <li id="pay"><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/></li>
	               <c:if test="${empty sxFlag}">
               			<li>${expectedInteresting}</li>
               	   </c:if>
	          </ul>
	     </header>
	     <c:if test="${not empty sxFlag}">
		     <section class="l_checkRewardWay">
		       <span>收益分配方式:</span>
		       <p class="l_wayChecked" data-tipText="回款利息收益将会在省心期内自动循环投资，收益更高" data-btn="0">收益复投</p>
		       <p data-tipText="回款利息收益将进入可用余额，资金灵活" data-btn="1">收益提至余额</p>
		       <i>回款利息收益将会在省心期内自动循环投资，收益更高</i>
	     	 </section>
     	 </c:if>
	     <p id="username" style="width:100%;height:3rem;text-indent:5%;background:#fff;color:#666;line-height:3rem;margin:1rem 0;font-size:1.2rem;"> ${userExtInfo.realName}</p>
	     <section>
			  <input id="voucherNum" type="hidden" value="${fn:length(vouchers)+fn:length(rateUsers)}"/>
	          <ul>
	               <li>可用余额<span id="AccountBalance"><fmt:formatNumber value="${userCashBalance}" pattern="#,##0.00"/></span>元</li>
	               <c:if test="${empty sxFlag}">
		               	<c:if test="${fn:length(vouchers)+fn:length(rateUsers) > 0}">
							<li id="voucherInfo" class="w_VoucherNew">${fn:length(vouchers)+fn:length(rateUsers)}张</li>
		               </c:if>
	               </c:if>
	               
	          </ul>
	     </section>
		<form method="POST" name="frm" class="frm" id="f1" >
			<div  id="frm" action="/view" method="post" target="test">
		    
		     <!--/账户余额信息开始/-->
		    <%-- <div id="PaymentAccount" class="uncheck">
		    	<span>账户支付</span>
		        <span style="margin:0 0 0 10%;">可用余额&nbsp;<span id="AccountBalance"><fmt:formatNumber value="${userCashBalance}" pattern="#,##0.00"/></span>元</span>    
		    </div> --%>
     		<!--/账户余额信息结束/-->
		     <!--/代金券信息开始/-->
		    <%-- <c:if test="${fn:length(vouchers)+fn:length(rateUsers) > 0}">
		    <div id="CashcCoupon" class="l_CashcCoupon">
		        <span>财富券支付</span>
		            <span id="CashcCouponInfo" style="margin:0 0 0 10%;">
		              <span id="w_zhang">${fn:length(vouchers)+fn:length(rateUsers)}</span>张可用
		            </span>
		    </div>
		    </c:if> --%>
     		<!--/代金券信息结束/-->
		    <div id="page1">
			    <p class="paymenttips" style="text-indent:5%;">*<span>剩余款项<span id="surplusMoney"></span>元</span>将由银行卡支付</p>
				
				<c:if test="${empty customerCard }">
					<c:if test="${not empty ybBindCard }">
						<c:if test="${!support}">
							<!-- 易宝绑定连连不支持 显示 -->
							<div id="l_oldBank" style="display: block;">
							    <p>由于更换第三方支付通道，您之前绑定的银行卡将不再支持，请使用其他银行的储蓄卡</p>
					    	</div>
						</c:if>
					</c:if>
				</c:if>
				
				<!--/银行卡号输入框开始/-->
				<div id="fgt1">
	                <p style="margin:0; "><a class="fgt1" style="font-size:1rem;margin: 1rem 0;text-indent:5%;" onclick="alertpProtocol()">支持银行及限额></a></p>
	            </div>
			    <input id="cardNo" class="inputm" name="cardNo"  value="请输入银行卡号" style="color:#afafaf;width:82%;margin:0 5%;padding:0 4%;font-size:1rem;" onfocus="if(value=='请输入银行卡号'){this.style.color='#4b4b4b';value=''}" onblur="if(value==''){this.style.color='#afafaf';value='请输入银行卡号'};return checkinput1()"  oninput="checkSueccess();"/>
			    <!--/银行卡号输入框结束/-->
			    <p  style="font-size:1rem;color:#9b9ea6;padding-left:5%;">* 仅支持本人的储蓄卡</p>
			    <!--/手机号开始/
			    	<input type="tel" name="phone" class="telnum2" maxlength="11" value="请输入银行预留手机号" id="phone"  style="color:#afafaf;" onfocus="if(value=='请输入银行预留手机号'){this.style.color='#4b4b4b';value=''}" onblur="if(value==''){this.style.color='#afafaf';value='请输入银行预留手机号'};return checkinput1()"  oninput="checkSueccess();"/>
			        /手机号结束/
			    	/验证码开始/
					<div class="ic ic2" style="margin-top: 4%;">
					    <input type="text" name="yzm" id="yzm"  maxlength="6" value="请输入短信验证码" class="yzm" style="color:#afafaf;" onfocus="if(value=='请输入短信验证码'){this.style.color='#4b4b4b';value=''}" onblur="if(value==''){this.style.color='#afafaf';value='请输入短信验证码'};return checkinput1()"  oninput="checkSueccess();"/>
					    <input type="button" name="btn" id="btn" class="btn_mfyzm" value="获取验证码" />
					</div>
			        <div id="fgt1">
			            <p><a class="fgt1" style="font-size:1.5rem">连连支付认证开通协议></a></p>
			        </div>-->
			</div>
			</div>    
		    <input type="hidden" name="payType" id="payType" value="2"/>
		    <input type="hidden" name="token" id="token" value="${token}"/>
		    <c:if test="${empty sxFlag}">
		    	<input type="hidden" id="loanApplicationId" name = "loanApplicationId" value="${loanApplication.loanApplicationId}"/>
		    </c:if>
		    <c:if test="${not empty sxFlag}">
		    	<input type="hidden" id="sxFlag" name = "sxFlag" value="sx"/>
		    	<input type="hidden" name="lendProductPublishId" id="lendProductPublishId" value="${lendProductPublish.lendProductPublishId}"/>
		    </c:if>
		    <input type="hidden" id="amount" name="amount" value="${order.buyBalance}"/>
		    <input type="hidden" name="productType" id="productType" value="1"/>
		    <input type="hidden" name="accountPayValue" id="accountPayValue" value="0"/>
		    <input type="hidden" name="rechargePayValue" id="rechargePayValue" value="0"/>
		    <input type="hidden" name="userVoucherId" id="userVoucherId" value=""/>
		    <input type="hidden" name="rateUserIds" id="rateUserIds" value=""/>
		    <input type="hidden" name="lendOrderId" id="lendOrderId" value="${order.lendOrderId}"/>
    	    <!--密码输入框开始-->
   			<!-- <div class="box" style="margin:4% 0 0 0;">
   	        	<span id=box><input type="password" placeholder="请输入支付密码" name="psw"  id="psw" value=""  maxlength="16" min="6" size="16" style="color:#afafaf; " onblur="return checkinput1();"  oninput="checkSueccess();"/></span>
   				<span id='clicks' class="click1"></span>
   	        </div> 
   	        <span class="tipspsw" style="font-size:1.2rem;color:#9b9ea6;" >*初始支付密码与注册时的登录密码一致，如已修改支付密码请使用修改后的密码</span>
   	        <div id ="building"> 
				<a href="#" class="fgt fgt1" style="font-size:1.3rem;margin-top:8%;">忘记支付密码></a>
			</div>-->
   	        <div class="l_PSWinput">
				<input type="password" placeholder="请输入支付密码" name="psw"  id="psw" value=""  maxlength="16" min="6" size="16" onblur="return checkinput1();" oninput="checkSueccess();" onclick="callAndroid()">
				<span>初始支付密码与注册时的登录密码一致，如已修改支付密码请使用修改后的密码</span>
				<span id ="building">忘记支付密码</span><i class="l_showPSW"></i>
			</div>
    		<!--密码输入框结束-->
	        
			<input type='button' name="btn2" id="okpay" class="btnall" disabled="disabled" style="width:95%;margin:3rem 2.5%;" value="确认支付" />
		</form>
		<form action="${llWapPayUrl }" id="llNoCardForm" method="post">
			<input type="hidden" name="req_data" id="req_data" value='' />
		</form>
	</div>
    <!-- 协议弹窗开始 -->
    <div class="masks" id="masks">
        <div class="agreements">
            <p>认证支付限额表<span id="off" onclick="offProtocol()"></span></p>
            <div style="overflow-y: scroll;height: 86%;">
            </div>
        </div>
        <div class="agreementss">
            <p>认证支付服务协议<span id="off" onclick="offProtocol()"></span></p>
            <article></article>
        </div>
        <!-- 协议弹窗结束 -->
    </div>
	<!--弹窗开始-->
	<div id="errorPopup" class="light">
	   <p>
	   	<img id="rechargeImg" src="${ctx}/images/iconnot.png" />
	   	<span id="errorPrompt"></span>
	   </p>
	   <a href="${ctx }/person/account/overview" class="closebt3" id="closebt2">确定</a>
	</div>
	<div id="errorFade" class="fade"></div>
	<!--弹窗结束-->
	<!--弹窗开始-->
	<div id="light1" class="light1">
		<p>请您关注并登录财富派公众号，在个人中心页面重置支付密码。
		<br/>打开微信>添加朋友>搜索“财富派官微”</p>
	    <a id="closebtn">我知道了</a>
	</div>
	<div id="fade" class="fade"></div>
	<!--弹窗结束-->
	<div id="loading" class="fade" style="position:fixed;"><p style="font-size:2rem;color:white;text-align:center; margin:80% auto 0 auto;"><img src="${ctx }/images/loading.gif" style="width:10%;" /></p></div>
	<!-- 弹出窗开始 -->
	<%-- <div class="w_Voucher" id="w_vouPag" style="overflow-y:auto;background:#fff;position: fixed;top: 0;left:0;display: none;"><!--w_vouPag为可用代金券-->
	  <p id="l_NotUse" style="color:#7085ba;  border:solid #7085ba 1px;padding: 2% 0;text-align: center; width: 90%;margin:8% auto;font-size:1.8rem;">不使用财富券</p>
	  <c:forEach items="${vouchers}" var="voucher" varStatus="var">
	  <dl class="w_vouList">
	    <dt><span>${voucher.voucherValue}</span>元</dt>
	    <em style="display: none;">${voucher.voucherId}</em>
	    <dd>
	      <h2>${voucher.voucherName}</h2>
	      <p>${voucher.voucherRemark}</p>
	      <p>
	      	<c:if test="${empty voucher.endDate}">长期有效</c:if>
	          <c:if test="${not empty voucher.endDate}"><fmt:formatDate value="${voucher.createDate}" pattern="yyyy.MM.dd"/>~<fmt:formatDate value="${voucher.endDate}" pattern="yyyy.MM.dd"/></c:if>
	      </p>
	    </dd>
	  </dl>
	  </c:forEach>
	</div> --%>
	<div class="w_mask">
          
     </div>
     <%@include file="../common/payVoucher.jsp"%>
<!-- 弹出窗结束 -->
<script>

function init(){//初始化函数
	showDialog()
}
//弹出框
function showDialog(){
	$("#building").on("click",function(){
		$("#fade").show();
		$("#light1").show();	
	})
	$("#closebtn").on("click",function(){//关闭
		$("#fade").hide();
		$("#light1").hide();	
	})
}
init()
document.onkeydown=function(event){
			var e = event || window.event || arguments.callee.caller.arguments[0];
			if(e && e.keyCode==13){ // enter 键
			//要做的事情
				$("#okpay").click();
			}
		}
</script> 
<script src="${ctx }/js/payment.js" type="text/javascript"></script> 
<script src="${ctx }/js/mainIndex.js" type="text/javascript"></script> 
<script>
var overscroll = function(el) {
	  el.addEventListener('touchstart', function() {
	    var top = el.scrollTop
	      , totalScroll = el.scrollHeight
	      , currentScroll = top + el.offsetHeight
	    //If we're at the top or the bottom of the containers
	    //scroll, push up or down one pixel.
	    //
	    //this prevents the scroll from "passing through" to
	    //the body.
	    if(top === 0) {
	      el.scrollTop = 1
	    } else if(currentScroll === totalScroll) {
	      el.scrollTop = top - 1
	    }
	  })
	  el.addEventListener('touchmove', function(evt) {
	    //if the content is actually scrollable, i.e. the content is long enough
	    //that scrolling can occur
	    if(el.offsetHeight < el.scrollHeight)
	      evt._isScroller = true
	  })
	}
	overscroll(document.querySelector('.l_NewScroll'));
	document.querySelector('.l_NewScroll').addEventListener('touchmove', function(evt) {
	  //In this case, the default behavior is scrolling the body, which
	  //would result in an overflow.  Since we don't want that, we preventDefault.
	  if(!evt._isScroller) {
	    evt.preventDefault()
	  }
	})
</script>
</body>
</html>