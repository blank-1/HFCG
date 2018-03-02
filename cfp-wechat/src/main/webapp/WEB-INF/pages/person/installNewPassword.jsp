<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=.5,maximum-scale=1.5,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<title>设置新密码</title>
<%@include file="../common/common_js.jsp"%>
<link rel="stylesheet" href="${ctx}/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx}/css/new_password.css?${version}">
<link rel="stylesheet" href="${ctx}/css/sweetAlert.css?${version}" type="text/css">
<script data-main="${ctx}/js/new_password.js?${version}" src="${ctx}/js/lib/require.js?${version}"></script>
<script type="text/javascript">
//rem自适应字体大小方法
var docEl = document.documentElement,
resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
recalc = function() {
    //设置根字体大小
    docEl.style.fontSize = 10 * (docEl.clientWidth / 320) + 'px';
};
//绑定浏览器缩放与加载时间
window.addEventListener(resizeEvt, recalc, false);
document.addEventListener('DOMContentLoaded', recalc, false);
</script>
</head>
<body class="l_NewScroll">
	<section class="pwd-theme">
		<header>
			<img src="${ctx}/images/pwd-icon.png" alt="">
		</header>
		<div class="pwd-container">
			<input type="password" class="pwd-number limitNUM16" id="pwd-number" placeholder="请输入新密码">
			<span class="eye eye-close" id="eye"></span>
		</div>
		<a href="#" class="next-btn" id="next-btn">下一步</a>
	</section>
	<script>
		//密码切换效果
		$(".l_pswBtn").on("touchend", function (e){
			e.stopPropagation();
			if ($(".l_pswBtn").hasClass("l_pswC")) {
				$(".l_pswBtn").removeClass("l_pswC");
				$("#passWord").prop("type","password");
			}else{
				$(".l_pswBtn").addClass("l_pswC");
				$("#passWord").prop("type","text");
			}
		});

		//判断密码是否合规
		$("#w_InstalBtn").on("touchend",function(){
		  var phoneVal=$("#passWord").val();
		      if(phoneVal==""){
		        $("#w_pattern").fadeIn(500,function(){
		        	$(this).fadeOut(3000)
		        }).html("输入内容不能为空"); 
		      }else if( phoneVal.length < 6){
		          $("#w_pattern").fadeIn(500,function(){
		          	$(this).fadeOut(3000)
		          }).html("密码必须大于6位数以上")
		      }else if(phoneVal.length > 16){
		        $("#w_pattern").fadeIn(500,function(){
		        	$(this).fadeOut(3000)
		        }).html("密码必须小于16位以下")
		      }else{
		    	  
		    		//【server】保存新密码【开始】
					var newPass = $("#passWord").val();
					var pass_type = $("#pass_type").val();
					var mobile_no = $("#mobile_no").val();
					var dtoken = $("#dtoken").val();
					$.ajax({
						url:rootPath+'/person/installNewPassword_savePass',
						type:"post",
						data:{
								"pass_type":pass_type,
								"mobile_no":mobile_no,
								"newPass":newPass,
								"dtoken":dtoken
							},
						async:false,
						success:function(data){
							if(data.result == 'success'){
								var susTip = $("<p>").text("保存密码成功").addClass("l_susTip").appendTo($("body"));
								$(".l_susTip").fadeOut(1500,function(){
									$("#installNewPassword_Form").submit();
								});
							}else if(data.result == 'error'){
								$("#w_pattern").fadeIn(500,function(){$(this).fadeOut(3000)}).html(data.errMsg); 
							}else{
								$("#w_pattern").fadeIn(500,function(){$(this).fadeOut(3000)}).html("网络异常，请稍后重试！");
							}
						},
						error: function(e) {
							$("#w_pattern").fadeIn(500,function(){$(this).fadeOut(3000)}).html("网络异常，请稍后重试！");
						}
					});
					//【server】保存新密码【结束】
		            
		      }
		})

	</script>
	
<!-- 隐藏表单【开始】 -->
<form action="${ctx}/person/finishInstallNewPassword" id="installNewPassword_Form" method="post">
	<input type="hidden" id="pass_type" name="pass_type" value="${pass_type}">
	<input type="hidden" id="mobile_no" name="mobile_no" value="${mobile_no}">
	<input type="hidden" id="token" name="token" value="${token}">
	<input type="hidden" id="dtoken" name="dtoken" value="${dtoken}">
</form>
<!-- 隐藏表单【结束】 -->
</body>
</html>
