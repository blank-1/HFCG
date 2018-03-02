// JavaScript Document
window.onload = function() {
	showHidePwd();
}
// 登录页的非空验证
function vali() {
	var telnum = document.getElementById("telnum");
	var psw = document.getElementById("psw");
	var alart = document.getElementById("alart");
	if (telnum.value == "手机号/用户名") {
		document.getElementById("alart").innerHTML = '请输入手机号/用户名';
		telnum.style.border = "solid 1px red"
		alart.style.display = "block";
		setTimeout("clean()", 3000);
		return;
	}
	if (psw.value == "") {
		document.getElementById("alart").innerHTML = '请输入登录密码';
		psw.style.border = "solid 1px red"
		alart.style.display = "block";
		setTimeout("clean()", 3000);
		return;
	} else {
		var form = $("#frm");
		var t2 = unloginf($("#telnum"), 0);
		var t3 = pwloginf($("#psw"), 0);
		if (t2 == "" && t3 == "") {
			$.ajax({
				url : rootPath + "/cfhRelation/login",
				type : "post",
				data : form.serialize(),
				error : function(data) {
					window.location.href = rootPath + "/404";
				},
				success : function(_data) {
					var data = eval("("+_data+")");
					// 登陆成功
					if (data.isSuccess) {
						$("#toVerifiedForm").attr("action", rootPath + "/cfhRelation/toVerified").submit();    
					} else{
						document.getElementById("alart").innerHTML = data.info;
   					 	alart.style.display="block";
   					 	setTimeout("clean()",3000);
					}
				}
			});
		}
	}
};
// 注册
function regist() {
	$("#registForm").submit();
};
function clean() {
	alart.style.display = "none";
	telnum.style.border = "solid 1px #d2d4dc"
	psw.style.border = "solid 1px #d2d4dc"
};
var flag = 0;
function showHidePwd() {
	$("#clicks").on("click", function() {
		if (flag == 0) {
			$("#psw").prop("type", "text");
			$(this).addClass("click");
			flag = 1;
		} else if (flag == 1) {
			$("#psw").prop("type", "password");
			$(this).removeClass("click");
			flag = 0;
		}
	})
}
// 用户验证
function unloginf(user, us7) {
	if (user.val() == "")// 只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
	{
		if (us7 == 0) {
			massage = "请您输入用户名/手机号";
			document.getElementById("alart").innerHTML = massage;
			$("#telnum").style.border = "solid 1px red"
			alart.style.display = "block";
			setTimeout("clean()", 3000);
		} else {
			massage = "";
		}
	} else {
		massage = "";
	}
	return massage;
}

// 密码验证
function pwloginf(passval1, pa7) {
	if (passval1.val() == "")// 只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
	{
		if (pa7 == 0) {
			massage = "请您输入密码";
			document.getElementById("alart").innerHTML = massage;
			$("#telnum").style.border = "solid 1px red"
			alart.style.display = "block";
			setTimeout("clean()", 3000);
		} else {
			massage = "";
		}
	} else {
		if (passval1.val().length >= 6 && passval1.val().length <= 16
				&& /^[0-9a-zA-Z]+$/.test(passval1.val())) {
			massage = "";
		} else {
			massage = "密码不正确";
			document.getElementById("alart").innerHTML = massage;
			alart.style.display = "block";
			setTimeout("clean()", 3000);
		}
	}
	return massage;
}