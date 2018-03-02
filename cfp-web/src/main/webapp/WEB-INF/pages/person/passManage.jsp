<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@include file="../common/taglibs.jsp"%>
<%@include file="../common/common_js.jsp"%>
<html>
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" /> 
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	
	<title>账户中心-密码管理</title>
	<script type="text/javascript" src="${ctx}/js/infor_3.js"></script><!-- public javascript -->
</head>

<body class="body-back-gray">
	<input type="hidden" name="token" id="token" value="${token}"/>
	<input type="hidden" id="hidem" data-value="545247000" />
	<!-- line2 start -->
	<%@include file="../common/headLine1.jsp"%>
	<%@include file="../login/login.jsp"%>
	<!-- line2 start -->

	<!-- navindex start -->
	<customUI:headLine action="3"/>
	<!-- navindex end -->
<!-- tabp start -->
	<%request.setAttribute("tab","5-2");%>
	 <input type="hidden" id="titleTab" value="3-2" />
<!-- tabp end -->

<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a href="javascript:;">个人信息</a>></li>
        <li><span>密码管理</span></li>
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
			<p class="mmgl-title">密码管理</p>
		</div>
        <div class="p-Right-mmgl">
			<div class="pre_zqlist">
				<ul class="mmgl_ul mmgl-bor">
					<li class="mmgl_ul_450">登陆密码</li>
					<li class="mmgl_ul_450"><a class="id_edit" href="#">修改</a></li>
				</ul>
				<div class="mmgl_show display-none">
					<form action="" class="form" method="post">
						<div class="input_box">
							<label>
								<span>原密码</span>
								<input type="password" id="password_1" value="" maxlength="16" placeholder="请输入原密码" />
							</label>
							<em></em>
						</div>
						<div class="input_box">
							<label>
								<span>新密码</span>
								<input type="password" id="password_2" value="" maxlength="16" style="width:192px;" placeholder="请输入新密码"  onKeyUp="CheckIntensity(this.value)" />
								<div type="button" id="rejc" class="Tcolor floatLeft">无</div>
							</label>
							<em></em>
						</div>
						<div class="input_box">
							<label>
								<span>确认密码</span>
								<input type="password" id="password_3" value="" maxlength="16" placeholder="请再次输入新密码"  />
							</label>
							<em></em>
						</div>
						<div class="mmgl_btn">
							<button type="button" id="submit-findpwd22" >确认修改</button>
						</div>
					</form>
				</div>
				<div class="zhezhao1"></div>
        		<ul class="mmgl_ul">
					<li class="mmgl_ul_450">交易密码</li>
					<li class="mmgl_ul_450"><a href="javascript:;" class="id_edit">修改</a><a id="ed_ex_psdad" class="ml-30" href="javascript:;">找回</a></li>
				</ul>
				<div class="mmgl_show display-none">
					<form action="" class="form" method="post">
						<div class="input_box">
							<label>
								<span>原密码</span>
								<input type="password" id="jiaoyi_1" value="" maxlength="16" placeholder="请输入原密码"  />
							</label>
							<em></em>
						</div>
						<div class="input_box">
							<label>
								<span>新密码</span>
								<input type="password" id="jiaoyi_2" value="" maxlength="16" style="width:192px;" placeholder="请输入新密码"  onKeyUp="CheckIntensity2(this.value)" />
								<div type="button" id="rejc2" class="Tcolor floatLeft">无</div>
							</label>
							<em></em>
						</div>
						<div class="input_box">
							<label>
								<span>确认密码</span>
								<input type="password" id="jiaoyi_3" value="" maxlength="16" placeholder="请再次输入新密码"  />
							</label>
							<em></em>
						</div>
						<div class="mmgl_btn">
							<button type="button" id="submit-jiaoyi" >确认修改</button>
						</div>
					</form>
				</div>
				<div class="clear_50"></div>
			</div>
		</div>
    </div>
    <!-- pRight start -->
</div>
<!-- container end -->

		<div style="clear:both;height:100px;"></div>
	
<div class="zhezhao"></div>
<div class="zhezhao1"></div>
<!-- masklayer start  -->
<div class="masklayer" id="login">
	<div class="logind">
	<h2><span>登录财富派</span> <a href="javascript:" class="logina" data-id="close"></a></h2>
             <!-- equity start -->
            <div class="login">
                <form action="" method="post" name="form" class="">
                    <div class="input-group">
                        <label for="username">
                            <input type="text" value="" id="unlogin" maxlength="20" name="username" placeholder="用户名/手机号" class="ipt-input" />
                        </label>
                        <em class="hui"></em>
                    </div>
                    <div class="input-group clearFloat">
                        <label for="password">
                            <input type="password" value="" id="pwlogin" maxlength="16" name="password" placeholder="密码" class="ipt-input" />
                        </label>
                        <em class="passwordem floatLeft"></em><a class="write floatRight passworda mr-10"  href="re_password1.html">忘记密码？</a>
                    </div>
                    <div class="btn-group">
                        <button type="button" id="submit-login" class="btn btn-error mt-0">登录</button>
                        <a class="write floatRight passworda mr-10" href="register.html">账号</a>
                    </div>
                </form>
                
            </div><!-- equity start -->
	</div>
</div><!-- masklayer end -->
<div style="height:50px;clear:both;"></div>
 
<!-- alert start  -->
<div class="zhezhao5" style="height:100%;"></div>
<div class="masklayers masklback" id="passManage_alert">
	<h2 class="clearFloat"><span id="passManage_alert_h"></span></h2>
	<div class="shenf_yanz_main">
		<p class="myp"><img id="passManage_alert_img" src="" /><span id="passManage_alert_p"></span></p>
		<div class="input_box_shenf myp2">
			<a href="javascript:void(0);" onclick="closeAlert()" data-id="close" ><button type="button">确认</button></a>
		</div>
	</div>
</div>
<!-- alert end -->

	
<!-- footerindex start -->
	<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->


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
					<p style="width:425px;text-align:right;"><a href="http://help.caifupad.com/guide/common/reg/" target="_black" style="text-decoration:none;">收不到验证码？</a></p>
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
				<a href="javascript:void(0);" onclick="closeAlert()" data-id="close" id="ed_ex_psda"><button type="button">确认</button></a>
			</div>
		</div>
</div>
<!-- 找回交易密码-结束 -->
<script type="text/javascript">
$(function(){
	$(".id_edit").click(function(){
		if($(this).html()=="修改"){
		$(this).parents("ul.mmgl_ul").next().slideDown(500,function(){bottomB();}).siblings(".mmgl_show").slideUp(500);
			$(this).html("取消");
			$(this).parents("ul.mmgl_ul").siblings("ul").find("a.id_edit").html("修改");
		}else{
		$(this).parents("ul.mmgl_ul").next().slideUp(500,function(){bottomB();}).siblings(".mmgl_show").slideUp(500);
			$(this).html("修改");
			$(this).parents("ul.mmgl_ul").siblings("ul").find("a.id_edit").html("修改");
		}	
	});
	
})	
</script>

</body>
</html>

