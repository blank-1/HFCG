<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta charset="utf-8" />
  <meta name="keywords" content="" />
  <meta name="description" content="" />
  <title>充值 - 财富派</title>
  <%@include file="../common/common_js.jsp"%>
  <script type="text/javascript" src="${ctx}/js/recharge.js"></script><!-- public javascript -->
  <script type="text/javascript" src="${ctx}/js/jquery_page.js"></script><!-- public javascript -->
</head>
<% session.removeAttribute("recharge_smsCode");%>
<body>
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
				
				ifIep.hide();
			}else{
				
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
<%@include file="../person/accountCommon.jsp"%>
<%@include file="../login/login.jsp"%>
<!-- tabp end -->
<!-- wrapper end -->
<!-- ebank start -->
<div class="wrapper bankM">
  <h2 class="bankimg3">充值</h2>
   <!-- rechan_grc start -->
       <div class="rechan_gro clearFloat">
			<div class="bankpayd bankpayd2  pb-0">
			            
			     <!-- 网银支付 -->
			     <h2 class="botyibao clearFloat" id="h21"><span class="floatLeft"><label><input id="rdi-inter" class="label2_1" type="radio" name="bankPayChannel"  /><img class="renzheng" src="${ctx}/images/pay/internatePay.jpg" /></label><i>跳转至银行页面，支付限额高</i></span>
			     
			     </h2>
			     <div class="inter-group inter-css">
			         <div class="input-group input-css   mt-30">
					      <label>
					        <c:if test="${userExt.isVerified ne '1'}">
					          <small class="ml-45">＊您未进行身份验证，请先进行<a  data-mask='mask' data-id="shenfen"  href="javascript:;">身份验证</a>再进行充值操作</small>
					        </c:if>
					      </label>
					    </div>
				 <p class="ieshowpay" id="ieshowpay">由于各银行支持浏览器限制，建议您使用IE（8.0及以上版本）进行网银支付</p>
			     <div class="internateBank clearFloat">
			     <div style="clear:both;"></div>
                    <span id="gongShang"  code="ICBC-NET-B2C"  class="choose" ><img src="${ctx}/images/banklogo/328.png" /><i></i></span>
                    <span  id="jianShe" code="CCB-NET-B2C" ><img src="${ctx}/images/banklogo/329.png" /><i></i></span>
                    <span id="nongYe"  code="ABC-NET-B2C" ><img src="${ctx}/images/banklogo/330.png" /><i></i></span>
                    <span  id="jiaoTong" code="BOCO-NET-B2C" ><img src="${ctx}/images/banklogo/340.png" /><i></i></span>
                </div>
			         <table cellpadding="0" cellspacing="0" class="internateTable">
			           <tr class="interTitle"><td colspan="7">工商银行（普通）</td></tr><tr class="interTitle"><td>覆盖区域</td><td>支持卡种</td><td>用户类型</td><td>单卡单笔消费上限（元）</td><td>单卡每日消费上限（元）</td><td>总限额累计上限（元）</td><td>开通方法</td></tr><tr><td rowspan="8">全国</td><td rowspan="8">借记卡<br /></td><td>2006年9月1日起未申请口令卡和USBKEY的</td><td>300</td><td>300</td><td>300</td><td rowspan="8">柜台申请开通  <br />申请电子口令卡每张口令卡使用次数为1000次</td></tr><tr><td>E支付</td><td>1000</td><td>1000</td><td>无限额</td></tr><tr><td>手机短信认证网上银行口令卡</td><td>2000</td><td>5000</td><td>无限额</td></tr><tr><td>未认证的网上银行口令卡</td><td>500</td><td>1000</td><td>无限额</td></tr><tr><td>电子密码器</td><td>50万</td><td>100万</td><td>无限额</td></tr><tr><td>一代U盾（无手机验证）</td><td>50万</td><td>100万</td><td>无限额</td></tr><tr><td>一代U盾（手机验证）</td><td>100万</td><td>100万</td><td>无限额</td></tr><tr><td>二代U盾</td><td>100万</td><td>100万</td><td>无限额</td></tr><tr><td colspan="7">备注：① 开通网上银行是进行电子支付的前提条件 </td></tr>
			         </table>
			         <div class="internateBank clearFloat">
                    <span id="youZheng"  code="POST-NET-B2C" ><img src="${ctx}/images/banklogo/331.png" /><i></i></span>
                    <span id="zhaoShang" code="CMBCHINA-NET-B2C" ><img src="${ctx}/images/banklogo/327.png" /><i></i></span>
                    <span id="zhongGuo" code="BOC-NET-B2C" ><img src="${ctx}/images/banklogo/326.png" /><i></i></span>
                    <span id="ningSheng" code="CMBC-NET-B2C" ><img src="${ctx}/images/banklogo/339.png" /><i></i></span>
                </div>
                <div class="internateBank display-none cange clearFloat">
                    <span id="pingAn" code="PINGANBANK-NET-B2C" ><img src="${ctx}/images/banklogo/337.png" /><i></i></span>
                    <span id="zhongXin" code="ECITIC-NET-B2C" ><img src="${ctx}/images/banklogo/332.png" /><i></i></span>
                    <span id="guangDa" code="CEB-NET-B2C" ><img src="${ctx}/images/banklogo/333.png" /><i></i></span>
                    <span id="guangFa" code="GDB-NET-B2C" ><img src="${ctx}/images/banklogo/338.png" /><i></i></span>
                    <%-- <span id="huaXia" code="HXB-NET-B2C" ><img src="${ctx}/images/banklogo/334.png" /><i></i></span> --%>
                </div>
                <div class="internateBank display-none cange clearFloat">
                    <span id="beiJing"  code="BCCB-NET-B2C"   ><img src="${ctx}/images/banklogo/beiJing.jpg" /><i></i></span>
                    <%-- <span id="xingYe" code="CIB-NET-B2C" ><img src="${ctx}/images/banklogo/335.png" /><i></i></span> --%>
                  <%--   <span id="puFa" code="SPDB-NET-B2C" ><img src="${ctx}/images/banklogo/336.png" /><i></i></span> --%>
                    <%-- <span id="shenFa" code="SDB-NET-B2C"  ><img src="${ctx}/images/banklogo/shenFa.jpg" /><i></i></span> --%>
                </div>
               <%-- <div class="internateBank display-none cange clearFloat">
                     <span id="shangHai"  code="SHB-NET-B2C"   ><img src="${ctx}/images/banklogo/shangHai.jpg" /><i></i></span> 
                </div>--%>
			         <p class="inter_more">更多>></p>
			         <form action="" method="post" class="" id="rechargeEBank">
			         <input type="hidden" name="bkcode" id="bkcode" value="" />
			                <!--
			                 <div class="input-group">
			                     <label for="password">
			                         <span>交易密码：</span>
			                         <input type="password" id="jypassword" name="password" class="ipt-input" />
			                     </label>
			                     <em></em>
			                 </div>
			                 -->
			                 <div class="input-group mt-30">
								<label for="moneyp">
									<span class="fsp1">充值金额：</span>
									 <c:if test="${userExt.isVerified ne '1'}">
							       		 <input id="moneybk" class="ipt-input" disabled type="text" maxlength="10" name="rechargeAmount">
							        </c:if>
							        <c:if test="${userExt.isVerified eq '1'}">
							      	   <input id="moneybk" class="ipt-input" type="text" maxlength="10" name="rechargeAmount">
							       </c:if>
								</label>
								<em class="hui">
									*充值最小金额不得小于
									<font color="#fe2a4d">100</font>
									元
								</em>
							</div>

			                 <div class="btn-group mb-30">
			                       <c:if test="${userExt.isVerified ne '1'}">
							        <button type="button" id="re_mo_btn" disabled class="btn btn-error mt-0 ml-50">确认支付</button>
							      </c:if>
							       <c:if test="${userExt.isVerified eq '1'}">
							        <button type="button" id="re_mo_btn" class="btn btn-error mt-0 ml-50">确认支付</button>
							      </c:if>
			                 </div>
			         </form>
			    </div><!-- 网银支付 end -->
			 </div>
			 <!-- ebank end -->

  <!-- bankpayd start -->
  <form id="rechargeForm" method="post">
  <div class="bankpayd bankpayd2  pb-0">
   <%--  <h2 class="clearFloat"><span class="floatLeft"><img src="${ctx}/images/pay_06.png" class="renzheng buttonimgdetail" /><i>一键支付</i></span></h2> --%>
	<h2 class="clearFloat">
	<span class="floatLeft"><label><input id="rdi-quick" class="label2_1"  type="radio" name="bankPayChannel" /><img class="renzheng  buttonimgdetail" src="${ctx}/images/pay/quickPay.jpg" /></label>
	<i>一键支付，快捷方便，付款过程安全流畅</i>
	</span>
	</h2>
                
      <!-- quick-group start -->
	<div class="quick-group quick-css">
    <div class="input-group input-css  mt-30">
        <p class="quickPay mt-30 mb-30"><img src="${ctx}/images/pay_061.jpg" style="margin-left:42px;"/>一键支付</p>
        <label>
            <span class="fsp1">开户名：</span>
            <c:if test="${userExt.isVerified ne '1'}">
                <small>＊您未进行身份验证，请先进行<a data-mask='mask' data-id="shenfen" href="javascript:;">身份验证</a>再进行充值操作</small>
            </c:if>
            <c:if test="${userExt.isVerified eq '1'}">
                ${userExt.realName}
            </c:if>
        </label>
    </div>

        <div class="input-group mt-30 clearFloat">
            <label for="">
                <span class="fsp1 floatLeft">银行卡：</span>
            </label>

            <div class="bankblock bankuse">
                <div class="titlet clearFloat">
                    <span class="img floatLeft"
                          style="background-image: url('${ctx}/images/banklogo/${customerCard.bankCode}.png')">&nbsp;</span>
                    <span class="floatRight"><small>尾号：</small>
                        ${customerCard.encryptCardNo}</span>
                </div>
                <!--  <p>累计提现：<big>22,222.22</big>元</p>
                 <div class="clearFloat last2">
                     <span class="floatRight widthr" ><a href="/cfp-web/person/toWithDraw">提现</a></span>
                 </div> -->
            </div>
            <div class="bankdetailr">
                <h2 class="personh2">换卡流程</h2>

                <p>如遇银行卡丢失或需更换银行卡，请出示以下信息：<br/>
                    1、手持“身份证以及已绑定银行卡”的清晰照片<br/>
                    2、身份证清晰照片<br/>
                    3、已绑定银行卡的清晰照片<br/>
                    4、如银行卡丢失，需提供银行出示的挂失凭证照<br/>
                    请将以上信息发送至财富派客服邮箱：myservice@mayitz.com，我们将在1-3个工作日进行信息审核及解绑操作，解绑前我们会与您电话沟通，请保持手机畅通，感谢您的配合。
                    详情可拨打客服电话进行咨询：400-061-8080</p>
            </div>
        </div>

        <div class="input-group mt-20">
            <label for="password">
                <span class="fsp1">付款限额：</span>
                <a href="javascript:;" data-id="p_bank" data-mask="mask">点击查看</a>
            </label>
        </div>
        <div class="input-group mt-30">
            <label for="moneyp">
                <span class="fsp1">充值金额：</span>
                <input type="text" id="moneyp" name="rechargeAmount" maxlength="10" class="ipt-input"/>
            </label>
            <em class="hui">*充值最小金额不得小于<font color="#fe2a4d">100</font>元</em>
        </div>
        <div class="input-group">
            <label for="jypassword">
                <span class="fsp1">交易密码：</span>
                <input id="jypassword" name="password"
                       onfocus="if(this.value==defaultValue) {this.value='';this.type='password'}"
                       onblur="if(!value) {value=defaultValue; this.type='text';}" autocomplete="off" type="password"
                       class="ipt-input"/>
                <a href="#" id="ed_ex_psdad" class="ml-20">找回交易密码</a>
            </label>
            <c:if test="${isBidEqualLoginPass eq 'true'}">
                <em class="hui">*交易密码默认与登录密码相同，为保证您的账号安全，请尽快修改！</em>
            </c:if>
            <c:if test="${isBidEqualLoginPass eq 'false'}">
                <em></em>
            </c:if>
            <c:if test="${isBidEqualLoginPass eq 'true'}">
                <span style="display: none;" id="bidpass">*交易密码默认与登录密码相同，为保证您的账号安全，请尽快修改！</span>
            </c:if>
        </div>
        <div class="input-group  mt-20">
            <label for="phone">
                <span class="fsp1">银行预留手机号：</span>
                ${customerCard.encryptMobileNo}
            </label>
            <em></em>
        </div>

        <div class="input-group">
            <label for="valid" class="floatLeft">
                <span class="floatLeft linheight">验证码：</span>
                <input type="text" value="" id="valid" name="valid" maxlength="6" class="ipt-input widthvalid"/>

            </label>

            <c:if test="${userExt.isVerified ne '1'}">
                <button type="button" disabled class="btn btn-blue mt-0 floatLeft" style="font-size:12px; width:117px">
                    获取验证码
                </button>
            </c:if>
            <c:if test="${userExt.isVerified eq '1'}">
                <button type="button" id="getvalid" class="btn btn-blue mt-0 floatLeft">获取验证码</button>
            </c:if>
            <div class="clearFloat"></div>
            <em></em>
        </div>
        <div class="btn-group mt-30 bankphonebtyn">
            <label for="">
                <%--<a href="#">《连连支付认证支付开通协议》</a>--%>
            </label>
        </div>
        <div class="btn-group bankphonebtyn">

            <c:if test="${userExt.isVerified ne '1'}">
                <button type="button" disabled class="btn btn-error mb-30" style="font-size:12px;">确认协议并充值</button>
            </c:if>
            <c:if test="${userExt.isVerified eq '1'}">
                <button type="button" id="okPay" class="btn btn-error mb-30">确认协议并充值</button>
            </c:if>
        </div>
  </div>
    <input type="hidden" id="regitxt" data-val="<c:if test="${userExt.isVerified ne '1'}">1</c:if><c:if test="${userExt.isVerified eq '1'}">2</c:if>"/><!-- 2:不可禁用　1:禁用-->
    <input type="hidden" name="payType" id="payType" value="1"/>
    <input type="hidden" name="token" id="token" value="${token}"/>

    <div class="zhezhao5">
      <img class="zheimg" src="${ctx}/images/2013092022533423.gif"/>
      <p>支付中...</p>
    </div>
    <script>
      $(function(){

        $(".zhezhao5").css("height",document.body.clientHeight);
        $(".zhezhao5 .zheimg").css("margin-top",document.body.clientHeight/2-100);
      })
    </script>
  </form><!-- bankpayd end -->
<form id="form_result" action="" method="post">

  <input type="hidden" id="errorMsg" name="errorMsg" value=""/>
</form>

  </div>
  </div>
   <h2 class="bankimg4">充值记录</h2>
  <table class="tablep" cellpadding="0" cellspacing="0" border="0">
    <tr class="titlep">
      <td>充值时间</td>
      <td>充值金额</td>
      <td>充值银行</td>
      <td>状态</td>
    </tr>
  </table>

  <div class="tcdPageCode mt-20">
  </div>
</div>
</div>
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
<!-- masklayer start  -->
<%@include file="../common/limit.jsp"%>

<%@include file="authenticationShenfen.jsp"%>
<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->

<script>
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

</style>
</body>
</html>
