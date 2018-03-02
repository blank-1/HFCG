// JavaScript Document
$(function(){
	var data = $("#mobile_no").val();//已登录，带出的手机号
	//判断后台是否传回值，确定显示何种样式
	if (data == "") {//未登陆
		$(".l_userTel").hide();
		$("#telNum").show();
	}else{//已登录
		$(".l_userTel").show();
		$("#telNum").hide();
		$("#UserTel").html(data);
	}
})

//发送验证码
$(".l_checkCode").delegate("#checkCodeB","touchend", function (e) {
	e.stopPropagation();
	//判断用户是否输入错误10次
	// if (val <= 10) {
	// 	//将下方判断粘贴进来
	// }else{
	// 	var wrongTip = $("<p>").text("由于你输入的验证码错误次数超过10次，请明日再试！").addClass("l_susTip").appendTo($("body"));
	// 	$(".l_susTip").fadeOut(1000);
	// }
	if ($("#telNum").is(':visible')) {//手机号，输入框可用时
		if ($("#telNum").val() == "") {
			var a="telNum";
			var b="请输入手机号码";
			errrTip(b,a);
		}else if (!(/^1[3|4|5|8|7][0-9]\d{4,8}$/.test($("#telNum").val()))) {
			b="请输入正确的手机号码";
			a="telNum";
			errrTip(b,a);
		}else{
			//【server】发送短信【开始】
			var telNum = $("#telNum").val();
			var pass_type = $("#pass_type").val();
			$.ajax({
				url:rootPath+'/person/forgetPswFinal_sendMsg',
				type:"post",
				data:{
						"pass_type":pass_type,
						"telNum":telNum
					},
				async:false,
				success:function(data){
					if(data.result == 'success'){
						clock();//倒计时读秒
					}else if(data.result == 'error'){
						errrTip(data.errMsg, "telNum");
					}else{
						errrTip("网络异常，请稍后重试！", "telNum");
					}
				},
				error: function(e) {
					errrTip("网络异常，请稍后重试！", "telNum");
				}
			});
			//【server】发送短信【结束】
		};
	}else{//手机号，输入框不可用时
		//【server】发送短信【开始】
		var telNum = $("#telNum").val();
		var pass_type = $("#pass_type").val();
		$.ajax({
			url:rootPath+'/person/forgetPswFinal_sendMsg',
			type:"post",
			data:{
					"pass_type":pass_type,
					"telNum":telNum
				},
			async:false,
			success:function(data){
				if(data.result == 'success'){
					clock();//倒计时读秒
				}else if(data.result == 'error'){
					errrTip(data.errMsg, "telNum");
				}else{
					errrTip("网络异常，请稍后重试！", "telNum");
				}
			},
			error: function(e) {
				errrTip("网络异常，请稍后重试！", "telNum");
			}
		});
		//【server】发送短信【结束】
	}
});
//验证码输入框的非空验证
$("input:visible").on("focus",function(){
	$("#next").addClass("forKeyBoard");//按钮会向上移动
});
$("input:visible").on("blur",function(){
	$("#next").removeClass("forKeyBoard");
 })

//重置密码提示弹窗
$("#FPTip").on("touchend",function(e){
	e.stopPropagation();
	$(".l_mask").show();
})
$(".l_light button").on("touchend",function(e){
	e.stopPropagation();
	$(".l_mask").hide();
})

//按钮点击验证
$("#next").on("touchend",function(e){
	e.stopPropagation();
	if ($("#telNum").is(':visible')) {//手机号，输入框可用时
		if ($("#telNum").val() == "" ) {
			var a="telNum";
			var b="请输入手机号码";
			errrTip(b,a);
		}else if(!(/^1[3|4|5|8|7][0-9]\d{4,8}$/.test($("#telNum").val()))) {
			b="请输入正确的手机号码";
			a="telNum";
			errrTip(b,a);
		}else if($("#checkCode").val() == "") {
			var a="checkCode";
			var b="请输入验证码";
			errrTip(b,a);
		}else{
			//【server】验证短信验证码【开始】
			var telNum = $("#telNum").val();
			var checkCode = $("#checkCode").val();
			var pass_type = $("#pass_type").val();
			$.ajax({
				url:rootPath+'/person/forgetPswFinal_checkMsg',
				type:"post",
				data:{
						"pass_type":pass_type,
						"telNum":telNum,
						"checkCode":checkCode
					},
				async:false,
				success:function(data){
					if(data.result == 'success'){
						$("#dtoken").attr("value",data.data);
						$("#mobile_no").attr("value",telNum);
						$("#forgetPswFinal_Form").submit();
					}else if(data.result == 'error'){
						errrTip(data.errMsg, "checkCode");
					}else{
						errrTip("网络异常，请稍后重试！", "checkCode");
					}
				},
				error: function(e) {
					errrTip("网络异常，请稍后重试！", "checkCode");
				}
			});
			//【server】验证短信验证码【结束】
		}
	}else{//手机号，输入框不可用时
			//验证输入内容是否含有合法字符
		if ($("#checkCode").val() == "") {
			var a="checkCode";
				var b="请输入验证码";
				errrTip(b,a);
		}else if($("#checkCode").val().length < 6){
			b="您输入的验证码位数不足";
			a="checkCode";
			errrTip2(b,a);
		}else if((/[^0-9]/g.test($("#checkCode").val()))){
			b="您输入的验证码含有非法字符";
			a="checkCode";
			errrTip2(b,a);
		}else{
			//【server】验证短信验证码【开始】
			var telNum = $("#telNum").val();
			var checkCode = $("#checkCode").val();
			var pass_type = $("#pass_type").val();
			$.ajax({
				url:rootPath+'/person/forgetPswFinal_checkMsg',
				type:"post",
				data:{
						"pass_type":pass_type,
						"telNum":telNum,
						"checkCode":checkCode
					},
				async:false,
				success:function(data){
					if(data.result == 'success'){
						$("#dtoken").attr("value",data.data);
						$("#mobile_no").attr("value",telNum);
						$("#forgetPswFinal_Form").submit();
					}else if(data.result == 'error'){
						errrTip(data.errMsg, "checkCode");
					}else{
						errrTip("网络异常，请稍后重试！", "checkCode");
					}
				},
				error: function(e) {
					errrTip("网络异常，请稍后重试！", "checkCode");
				}
			});
			//【server】验证短信验证码【结束】
		}
	}
})