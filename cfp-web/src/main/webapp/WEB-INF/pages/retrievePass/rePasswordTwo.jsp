<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>

<!DOCTYPE HTML>
<html>
<head>
	<meta name="keywords" content="" />
	<meta name="description" content="" /> 
	<title>财富派-找回密码</title>
	<%@include file="../common/common_js.jsp"%>
</head>

<body class="body-back-gray">
<%request.setAttribute("pastUrl", "/"); %>
<header>
<!-- navtopbg start-->
	<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- navtopbg end-->
<!-- navindex start -->
<customUI:headLine/>
<!-- navindex end -->
<nav class="clearFloat">
</nav>
</header>
<!-- person-link start -->
<div class="person-link">
    <div class="container clearFloat">
        <div class="re-password"><img src="${ctx}/images/pw/pw_02.jpg" /></div>
    </div>
</div>
<!-- person-link end -->
<%@include file="../login/login.jsp"%>

<!-- container start -->
<div class="container clearFloat">
	<div class="re-password-link">
		<ul>
			<li><a href="/">财富派首页</a>></li>
			<li><a href="javascript:;" id="crumbs_1">找回密码</a>></li>
			<li><span id="crumbs_2">信息核实</span></li>
		</ul>
	</div>
	<div class="clear_50"></div>
	<div class="re_Step1_main">
		<form action="${ctx }/user/rePasswordThree" class="form" method="post" id="rePaTwo">
			<input type="hidden" name="token" value="${token }">
			<div class="re_Step1_mainle">
				<div class="input-group">
					<span>注册时间：</span>
					<div class="re_Step1_mainletop_ri">
						<label><input name="time" type="radio" value="" autocomplete="off" /> &nbsp;<span id="time0"></span></label> 
						<label><input name="time" type="radio" value="" autocomplete="off" /> &nbsp;<span id="time1"></span></label> 
						<label><input name="time" type="radio" value="" autocomplete="off" /> &nbsp;<span id="time2"></span></label> 
						<label><input name="time" type="radio" value="" autocomplete="off" /> &nbsp;<span id="time3"></span></label>
						<p id="tip1">请选择您在网站的注册时间</p>
					</div>
				</div>
				<div class="clear_30"></div>
				<c:if test="${verified eq '1'}">
					<div class="input-group">
						<label for="idcard">
							<span>身份证号：</span>
							<!-- <input type="text" class="ipt-input phone-input" value="" id="re_password_card" maxlength="18" name="idcard" placeholder="请输入您在平台绑定的身份证信息" autocomplete="off"  /> -->
							<div class="re_Step1_mainlebot_ri">
								<div id="idcardall">
										<input class="idcard" type="text" id="t1" onkeyup="chk1(this,t2)" autocomplete="off" maxlength="1">
										<input class="idcard" type="text" id="t2" onkeyup="chk1(this,t3)" autocomplete="off" maxlength="1">
										<input class="idcard" type="text" id="t3" onkeyup="chk1(this,t4)" autocomplete="off" maxlength="1">
										<input class="idcard2" type="text" id="t4" onkeyup="chk1(this,t5)" autocomplete="off" maxlength="1">
										<span>* * * * * * * * * *</span>
										<input class="idcard" type="text" id="t5" onkeyup="chk1(this,t6)" autocomplete="off" maxlength="1">
										<input class="idcard" type="text" id="t6" onkeyup="chk1(this,t7)" autocomplete="off" maxlength="1">
										<input class="idcard" type="text" id="t7" onkeyup="chk1(this,t8)" autocomplete="off" maxlength="1">
										<input class="idcard2" type="text" id="t8" onkeyup="chk1(this,t9)" autocomplete="off" maxlength="1">
								</div>
								<div style="clear:both;"></div>
								<p id="tip">请输入您在平台绑定的身份证信息</p>
							</div>
							
						</label>
						<em class="new2-emtip" ></em>
					</div>
				</c:if>
			</div>
			<div class="re_Step1_mainri">
				<p>人工找回密码：</p>
				<p>如遇手机丢失等特殊情况无法找回密码 ，请将您的身份证信息发送至客服邮箱：myservice@mayitz.com，或拨打客服电话：400-061-8080。我们会尽快帮您找回密码。</p>
			</div>
			<div style="clear:both;"></div>
			<div class="btn-group mt-50">
					<button type="button" id="submit-register" class="btn btn-error widbtn new-btn">下一步</button>
			</div>
			<div class="clear_50"></div>
		</form>
	</div>
</div>
<!-- container end -->

<script>
			window.onload = function()
			{
				$.ajax({
					url:"${ctx}/user/getRegistrationTime",
					type:"post",
					data:{},
					success:function(data){
						
						$("input[name='time']").each(function(index){
							if(index == 0)
							{
								$(this).val(data.dateList[0])
							}
							if(index == 1)
							{
								$(this).val(data.dateList[1])
							}
							if(index == 2)
							{
								$(this).val(data.dateList[2])
							}
							if(index == 3)
							{
								$(this).val(data.dateList[3])
							}
						});
						$("#time0").text(data.dateList[0]);
						$("#time1").text(data.dateList[1]);
						$("#time2").text(data.dateList[2]);
						$("#time3").text(data.dateList[3]);
						if(data.identityType){
							$("#identyInfo").hide();
						}
					}
				});
			};
				$(function(){
					$("#submit-register").click(function(){
						$("#tip1").html("");
						$("#tip").html("");
						if($("input[name='time']:checked").val()==undefined){
							    $("#tip1").css("color","red");
							    $("#tip1").html("请选择您在网站的注册时间");
							    return false;
						}else{
							document.getElementById('tip1').style.color = "#666";
							/*if($("input[name='time']:checked").val()!==""){
							    document.getElementById('tip1').style.color = "#666";
							    var registTime = $("input[name='time']:checked").val();
							     if($("#identyInfo").is(":hidden")){
							    	$.ajax({
										url:"${ctx}/user/identityMatch?registTime="+registTime,
										type:"post",
										data:{},
										success:function(data){
											var result = eval("("+data+")");
											if(result.isSuccess){
												$(".form").submit();
											}
											else{
												$("#tip1").text(result.info);
												document.getElementById('tip1').style.color = "red";
											}
										}
									});
							    } 
							}*/
						}
						var registTime = $("input[name='time']:checked").val();
						var cardlength=$("#idcardall").find("input").length;
						var before ,after ="";
						if(cardlength>0){
							var index=0;
							$("#idcardall").find("input").each(function(){
								var obj = $(this);
								if(obj.attr("type") == "text"){
									if(obj.val().length == 0){
									    index++;
									    $("#tip").css("color","red");
									    $("#tip").html("请输入您在平台绑定的身份证信息");
										return false;
									}
								}
							 	/* if(index > 0){
									document.getElementById("tip").style.color = "red";
								}else{
									document.getElementById("tip").style.color = "#666";
								} */
							});
							if($("input[name='time']:checked").val()!=="" && index == 0){
									before = $("#t1").val()+""+$("#t2").val()+""+$("#t3").val()+""+$("#t4").val();
									after = $("#t5").val()+""+$("#t6").val()+""+$("#t7").val()+""+$("#t8").val();
							}
						}
						$.ajax({
							url:"${ctx}/user/identityMatch?before="+before+"&after="+after+"&registTime="+registTime,
							type:"post",
							data:{},
							success:function(data){
								var result = eval("("+data+")");
								if(result.isSuccess){
									$(".form").submit();
								}
								else{
									if(result.id == "indent"){
										$("#tip").text(result.info);
										document.getElementById('tip').style.color = "red";
										$("#tip1").text("");
									}
									if(result.id == "regist"){
										$("#tip1").text(result.info);
										document.getElementById('tip1').style.color = "red";
										$("#tip").text("");
									}
								}
							}
						});
						
					});
				});
				
				function chk1(obj,next)
				{  
					if(obj.value.length==obj.maxLength)next.focus();
				}
							
	</script>
<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->
</body>
</html>