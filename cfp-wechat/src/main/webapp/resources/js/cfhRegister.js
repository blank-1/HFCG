// JavaScript Document
window.onload=function(){
	showHidePwd();
	var btn2=document.getElementById("btn2");
	var light=document.getElementById('light');
	var fade=document.getElementById('fade');
	var closebt=document.getElementById("closebt");
	var backgd=document.getElementById("backgd");
	
//设定数字位数随字符数改变字体
$.each($(".xsize"),function(){
	var len = $(this).text().length;
	if(len<8){
		$(this).css("font-size","2rem");
	}else if(len>=8 && len<11){
	$(this).css("font-size","1.6rem");
	}else if(len>=11){
	$(this).css("font-size","1.2rem");	
	}
});
$.each($(".ysize"),function(){
	var len = $(this).text().length;
	if(len<8){
		$(this).css("font-size","3rem");
	}else if(len>=8 && len<11){
	$(this).css("font-size","2.6rem");
	}else if(len>=11){
	$(this).css("font-size","2.2rem");	
	}
});
    
    
var wait=60;
document.getElementById("btn").disabled = false;  
function time(o) {
        if (wait == 0) {
            o.removeAttribute("disabled");          
            o.value="获取验证码";
            wait = 60;
        } else {
            o.setAttribute("disabled", true);
            o.value="" + wait + "秒";
            wait--;
            setTimeout(function() {
                time(o)
            },
            1000)
        }
    }
document.getElementById("btn").onclick=function(){
	var t4=phonef($("#telnum2"),0);
	if(t4){
		var phoneNo = document.getElementById("telnum2").value;
		$.ajax({
			url:rootPath+"/user/regist/validate_name_phone",
			type:"post",
			data:{"mobile":$("#telnum2").val(),"loginName":$("#userid").val()},
			success:function(data){
				var phone =  eval("("+data.phone+")");
				var username =  eval("("+data.username+")");
				if(validError(phone)&validError(username)){
					time(document.getElementById("btn"));
					$.post(rootPath+'/user/regist/getRegisterMsg',{"mobileNo":phoneNo});
				}
			}
		});
	}
}


closebt.onclick=function(){
light.style.display='none';
fade.style.display='none';
backgd.style.overflow='visible'
}

$("#btn2").click(function(){
	var t2=usernamef($("#userid"),0);
	var t3=passwordf($("#psw"),0);
	var t4=phonef($("#telnum2"),0);
	var t5=validf($("#yzm"),0);
	if(t2&t3&t4&t5){//所有前台验证
		$.ajax({
			url:rootPath+"/user/regist/validate",
			type:"post",
			data:{"mobile":$("#telnum2").val(),"validCode":$("#yzm").val(),"loginName":$("#userid").val(),"password":$("#psw").val(),"inviteCode":$("#yqm").val()},
			success:function(data){
				var valid =  eval("("+data.valid+")");
				var password =  eval("("+data.password+")");
				var phone =  eval("("+data.phone+")");
				var username =  eval("("+data.username+")");
				if(data.visate!=null){
					var visate =  eval("("+data.visate+")");
					if(!validError(visate))
						return ;
				}
				if(validError(valid)&validError(password)&validError(phone)&validError(username)){
					light.style.display='block';
					fade.style.display='block';
					backgd.style.overflow='hidden';
					frmsubmit();
				}
					
			}
		});
	}
});
};
//密码显示和隐藏的效果
var flag=0;
function showHidePwd(){
	$("#clicks").on("click",function(){
		if(flag==0){
			$("#psw").prop("type","text");
			$(this).addClass("click");
			flag=1;
		}else if(flag==1){
			$("#psw").prop("type","password");
			$(this).removeClass("click");
			flag=0;
		}
	})
}


$('#test').load(function(){
    var res = $(this).contents().find('body').text();
    //console.log(res);
});

function checkSueccess(){
	
	if(userid.value != '用户名' && userid.value != '' && telnum2.value != '手机号' && telnum2.value != '' && psw.value != '' && yzm.value != '请输入短信验证码' && yzm.value != ''){
		document.getElementById('btn2').disabled= "";
		document.getElementById('btn2').style.backgroundColor="#fe2a4d";
		document.getElementById('btn2').style.color="white";
	}
	else{
		document.getElementById('btn2').disabled="disabled";
	}

}
//注册页面的非空验证
function checkinput(){
if (userid.value == '用户名' || telnum2.value == '手机号' || psw.value == '' || yzm.value == '请输入短信验证码'){
document.getElementById('btn2').disabled="true";
document.getElementById('btn2').style.backgroundColor="#cacaca";
document.getElementById('btn2').style.color="#989898";
}else{
		
document.getElementById('btn2').disabled="";
document.getElementById('btn2').style.backgroundColor="#fe2a4d";
document.getElementById('btn2').style.color="white";
}
};
function validError(v){
	if(!v.isSuccess){
		 document.getElementById("alart").innerHTML = v.info;
		 alart.style.display="block";
		 setTimeout("clean()",3000);
	}
	return v.isSuccess;
}
//用户名验证
function usernamef(user,us1){
	var massage="";
	if(user.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
	{
		if(us1==0){
			
			massage="请您输入用户名";
			document.getElementById("alart").innerHTML = massage;
			alart.style.display="block";
			setTimeout("clean()",3000);
		}else{
			massage="";			
		}
	}else{

		if(user.val().length>=4 && user.val().length<=20 && !/[^a-zA-Z0-9\u4E00-\u9FA5\_-]/.test(user.val())){
			if(/(^1[3|4|5|7|8][0-9]{9}$)/.test(user.val())){
				massage="手机号不可作为用户名使用";
				document.getElementById("alart").innerHTML = massage;
				 alart.style.display="block";
				 setTimeout("clean()",3000);
			}else{
				massage="";
			}
		}else{
			
			massage="用户名格式不符合";
			document.getElementById("alart").innerHTML = massage;
			alart.style.display="block";
			setTimeout("clean()",3000);
		}
	}
	if(massage==""){
		return true;
	}
	return false;
}


//密码验证
function passwordf(passval1,pa1){
	var massage="";
	if(passval1.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
	{
		if(pa1==0){
			massage="请您输入密码";
			document.getElementById("alart").innerHTML = massage;
			alart.style.display="block";
			setTimeout("clean()",3000);
		}else{
			massage="";
		}
	}else{
		
		if(passval1.val().length>=6 && passval1.val().length<=16 && /^[0-9a-zA-Z]+$/.test(passval1.val())){
			massage="";
		}else{
			
			massage="密码格式不符合";
			document.getElementById("alart").innerHTML = massage;
			alart.style.display="block";
			setTimeout("clean()",3000);
		}
		
	}
	if(massage==""){
		return true;
	}
	return false;
}

//验证码
function validf(vali,va1){
	var massage="";
	if(vali.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
	{
		if(va1==0){
			massage="请您输入验证码";
			document.getElementById("alart").innerHTML = massage;
			alart.style.display="block";
			setTimeout("clean()",3000);
		}else{
			massage="";
		}
	}else{
		
		if(vali.val().length==6 && /^([0-9.]+)$/.test(vali.val())){	
			massage="";			
		}else{
			
			massage="验证码6位数字";
			document.getElementById("alart").innerHTML = massage;
			alart.style.display="block";
			setTimeout("clean()",3000);
		}
	}
	if(massage==""){
		return true;
	}
	return false;
}

//手机验证
function phonef(phone,ph1){
	var massage="";
	if(phone.val()=="" || phone.val()=="手机号")
	{
		if(ph1==0){
			massage="请输入手机号码！";
			document.getElementById("alart").innerHTML = massage;
			alart.style.display="block";
			setTimeout("clean()",3000);
		}else{
			massage="";
		}
	}else
	{
		if (/(^1[3|4|5|7|8][0-9]{9}$)/.test(phone.val())) {
			massage="";
		} else {
			massage="手机号码格式错误";
			document.getElementById("alart").innerHTML = massage;
			alart.style.display="block";
			setTimeout("clean()",3000);
		}
		
	}
	if(massage==""||massage==null){
		return true;
	}
	return false;
}

//邀请码
function visatef(vali,va1){
	if(vali.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
	{
		if(va1==0){
			massage="请您输入邀请码";
			document.getElementById("alart").innerHTML = massage;
			alart.style.display="block";
			setTimeout("clean()",3000);
		}else{
			massage="";
		}
	}else{

		if(vali.val().length==6 && /^([0-9.]+)$/.test(vali.val())){
			massage="";
		}else{

			massage="邀请码为6位数字";
			document.getElementById("alart").innerHTML = massage;
			alart.style.display="block";
			setTimeout("clean()",3000);
		}
	}

	if(massage==""){
		return true;
	}
	return false;
}
//弹出协议
function showProtocol(){
    $("#masks").show();
    $(".agreementss").show();
	$("body")
}
function offProtocol(){
    $("#masks").hide();
}
function frmsubmit() {
	$.ajax({
		url : rootPath + "/cfhRelation/register",
		type : "post",
		data : $("#frm").serialize(),
		success : function(_data) {
			var data = eval("(" + _data + ")");
			if (data.isSuccess) {
				$("#toVerifiedForm").attr("action", rootPath + "/cfhRelation/toVerified").submit();
			} else {
				document.getElementById("alart").innerHTML = data.info;
				alart.style.display = "block";
				setTimeout("clean()", 3000);
			}
		}
	});
}
