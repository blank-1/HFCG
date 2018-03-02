// JavaScript Document

var $load = $("#load"),
	$realName = $("#realName"),
	$userCode = $("#userCode"),
	$checkCode = $("#checkCode"),
	$checkCodeB = $("#checkCodeB"),
	userName ,//用户账号
	pageType ;//假设页面三种状态为1:（未实名）；2，（已实名）3，（注册历程）根据此数据决定显示哪几个输入框
userName = $("#JMmobileNo").val();
var verifiedState = $("#verifiedState").val();
var type = $("#type").val();

if("verified" == type){
	if (1 == verifiedState) {// 已认证
		pageType = 2;
	} else {// 未认证
		pageType = 1;
	}
} else {
	pageType = 3;
}

if ($realName.val() != "") {
	$load.addClass("test1");
}
if (pageType == 2) {
	$realName.remove();
	$userCode.attr({
		"class":"l_userId input1",
		"data-inputNum":"1"
	});
	$checkCode.attr({
		"class":"l_userId input2",
		"data-inputNum":"2"
	});
}
if (pageType == 3) {
	$(".l_checkCode").remove();
}
$(function(){
	$("#userName").text(userName);//加入用户账号信息
})
//真实姓名前端验证
$realName.on("blur",function(){
		//真实姓名为空
	if($(this).val()==""){
		var a=$(this).attr("id");
		var b=$(this).attr("title");
		errrTip(b,a);
		$load.removeClass("test1");
		// console.log(index);

		//真实姓名不合法
	}else if((/[^\u4e00-\u9fa5,·]/g.test($(this).val()))){
		b="您输入的真实姓名含有非法字符";
		a="realName";
		errrTip2(b,a);
		$load.removeClass("test1");
	}else{
		$load.addClass("test1");
	}
})
//身份证前端验证
$userCode.on("blur",function(){
		//身份证为空
	if ($userCode.val() == "") {
		var a="userCode";
			var b="身份证号码";
			errrTip(b,a);
			$load.removeClass("test2");
	}else if($userCode.val().length != 15 && $userCode.val().length != 18 ){
		b="您输入的身份证号码位数不正确";
		a="userCode";
		errrTip2(b,a);
		$load.removeClass("test2");
	}else if ($userCode.val().length == 18) {
		if(!(/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/.test($("#userCode").val()))){//
			b="您输入的身份证号码含有非法字符";
			a="userCode";
			errrTip2(b,a);
			$load.removeClass("test2");
		}else if((/^1{7,}|2{7,}|3{7,}|4{7,}|5{7,}|6{7,}|7{7,}|8{7,}|9{7,}|0{7,}$/.test($("#userCode").val()))){
			b="您输入的身份证号码含有非法字符";
			a="userCode";
			errrTip2(b,a);
			$load.removeClass("test2");
		}else{
			$load.addClass("test2");
		}
	}else if ($userCode.val().length == 15) {
		if(!(/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/.test($("#userCode").val()))){
			b="您输入的身份证号码含有非法字符";
			a="userCode";
			errrTip2(b,a);
			$load.removeClass("test2");
		}else if((/^1{7,}|2{7,}|3{7,}|4{7,}|5{7,}|6{7,}|7{7,}|8{7,}|9{7,}|0{7,}$/.test($("#userCode").val()))){
			b="您输入的身份证号码含有非法字符";
			a="userCode";
			errrTip2(b,a);
			$load.removeClass("test2");
		}else{
			$load.addClass("test2");
		}
	}else{
		return false;
	}
})
//短信验证码发送
$checkCodeB.on("click",function(e){
	e.stopPropagation();
	//此处添加向用户手机发送验证码短信功能
	$.ajax({
		url : rootPath + "/cfhRelation/sendSms",
		type : "post",
		data : {
			mobileNo : $("#mobileNo").val()
		},
		error : function(data) {
			alertMsg('登录状态失效');
		},
		success : function(data) {
			if (data.result != 'success') {
				alertMsg(data.errMsg);
			}
		}
	});
	clock();
});

// 短信验证码前端验证
$checkCode.on("blur", function() {
	// 验证码为空
	if ($(this).val() == "") {
		var a = $(this).attr("id");
		var b = $(this).attr("title");
		errrTip(b, a);
		$load.removeClass("test3");
		// 验证码不合法
	} else if ((/[^0-9]/g.test($(this).val()))) {
		b = "您输入的验证码含有非法字符";
		a = "checkCode";
		errrTip2(b, a);
		$load.removeClass("test3");

		// 验证码长度不合法
	} else if ($(this).val().length > 6) {
		b = "您输入的验证码长度不正确";
		a = "checkCode";
		errrTip2(b, a);
		$load.removeClass("test3");
	} else {
		$load.addClass("test3");
	}
})

// 登录按钮异步验证
function load() {
	if (pageType == 1) {
		// 真实姓名身份证验证码都不为空
		if ($load.hasClass("test1") && $load.hasClass("test2") && $load.hasClass("test3")) {
			var name = $("#realName").val();
			$.ajax({
				type : "POST",
				url : rootPath + "/cfhRelation/bind",
				dataType : "json",
				data : {
					"realName" : $("#realName").val(),
					"idCard" : $("#userCode").val(),
					"validCode" : $("#checkCode").val(),
					"hToken" : $("#hToken").val(),
					"type" : $("#type").val()
				},
				success : function(data) {
					if (data.result == 'success') {
						reqCfh(data.data);
						window.location.href = rootPath + "/finance/list"; // 定向到首页
					} else {
						alertMsg(data.errMsg);
					};
				},
				error : function() {
					alertMsg('登录状态失效');
				}
			})
		} else {
			errrTip2("请完善用户信息",null);
			return false;
		}
	} else if (pageType == 2) {
		// 身份证验证码都不为空
		if ($load.hasClass("test2") && $load.hasClass("test3")) {
			var name = userName;
			$.ajax({
				type : "POST",
				url : rootPath + "/cfhRelation/bind",
				dataType : "json",
				data : {
					"idCard" : $("#userCode").val(),
					"validCode" : $("#checkCode").val(),
					"hToken" : $("#hToken").val(),
					"type" : $("#type").val()
				},
				success: function(data) {
					if (data.result == 'success') {
						reqCfh(data.data);
						window.location.href = rootPath + "/finance/list"; // 定向到首页
					} else {
						alertMsg(data.errMsg);
					};
				},
				error : function() {
					alertMsg('登录状态失效');
				}
			})
		} else {
			errrTip2("请完善用户信息",null);
			return false;
		}
	} else {
		// 真实姓名身份证都不为空
		if ($load.hasClass("test1") && $load.hasClass("test2")) {
			var name = $("#realName").val();
			$.ajax({
				type : "POST",
				url : rootPath + "/cfhRelation/bind",
				dataType : "json",
				data : {
					"realName" : $("#realName").val(),
					"idCard" : $("#userCode").val(),
					"hToken" : $("#hToken").val(),
					"type" : $("#type").val()
				},
				success : function(data) {
					if (data.result == 'success') {
						reqCfh(data.data);
						window.location.href = rootPath + "/finance/list"; // 定向到首页
					} else {
						alertMsg(data.errMsg);
					};
				},
				error : function() {
					alertMsg('登录状态失效');
				}
			})
		} else {
			errrTip2("请完善用户信息",null);
			return false;
		}
	}
}

function reqCfh(relationId){
	$.ajax({
		type : "POST",
		url : rootPath + "/cfhRelation/sendRelationVerified",
		dataType : "json",
		data : {
			"relationId" : relationId
		},
		success : function(data) {
			
		},
		error : function() {
			alert("发生错误：" + jqXHR.status);
		}

	})
}

function alertMsg(msg) {
	$("body").append("<div class='l_errr'>" + msg + "</div>");// 加入错误提示
	$(".l_errr").show();
	$(".l_errr").fadeOut(3000, function() {
		$(".l_errr").remove();
	});
}