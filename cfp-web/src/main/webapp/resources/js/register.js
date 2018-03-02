// JavaScript Document
$(function(){
	//$("input:visible").val("");

	//上下箭头
	$("#viste_i_zc").click(function(){
		if($(this).hasClass("viste_zc_bottom")){
			
			$(this).removeClass("viste_zc_bottom").addClass("viste_zc_top");	
			$("#visate").slideDown(500);
		}else{
				
			$(this).removeClass("viste_zc_top").addClass("viste_zc_bottom");
			$("#visate").slideUp(500);	
		}
	});
				
	//用户名输入验证
	$("#username").blur(function(){

		if(usernamef($(this),1)){
			isLoginNameExist($(this))
		};
	});
	$("#username").change(function(){
			var massage="";
			if (/[^a-zA-Z0-9\u4E00-\u9FA5\_-]/.test($(this).val())) {
				massage='请输入4 - 20位字符，支持汉字、字母、数字及 "-"、"_"组合';
				$(this).addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			} else {
				massage="";
				$(this).removeClass("ipt-error").parent().siblings("em").html(massage).html("请输入4 - 20位字符，支持汉字、字母、数字及 '-'、'_组合'").addClass("hui");
			}
			
	});
	
	$("#phone").change(function(){
		var massage="";
		if (/^([0-9.]+)$/.test($(this).val())) {
			massage="该手机号将用于手机号登录和找回密码";
			$(this).removeClass("ipt-error").parent().siblings("em").html(massage).html(massage).addClass("hui");
		} else {
			massage="手机号码应为 11 位数字";
			$(this).addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
		}
			
	});
	$("#valid").change(function(){
		var massage="";

		if (/^([0-9.]+)$/.test($(this).val())) {
				
			massage="";
			$(this).removeClass("ipt-error").parent().siblings("em").html(massage).html(massage);
		} else {
			massage="验证码应为 6 位数字";
			$(this).addClass("ipt-error").parent().siblings("em").html(massage);
		}
	});
	$("#password").change(function(){
		var massage="";
		if (/^[0-9a-zA-Z]+$/.test($(this).val())) {
				
			massage="密码为6 -16 位字符，支持字母及数字,字母区分大小写";
			$(this).removeClass("ipt-error").parent().siblings("em").html(massage).html(massage).addClass("hui");
		} else {
			massage="密码为6 -16 位字符，支持字母及数字,字母区分大小写";
			$(this).addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
		}
			
	});

	//密码输入验证
	$("#password").blur(function(){

		passwordf($(this),1);
	});
	//手机号码输入验证
	$("#phone").blur(function(){

		if(phonef($(this),1)){
			isMobileNoExist($(this));

		}
	});
	//验证码输入验证
	$("#valid").blur(function(){

		validf($(this),1);
	});
	
	//点击注册保存时
	$("#submit-register").click(function(){

		var t2=usernamef($("#username"),0);
		var t3=passwordf($("#password"),0);
		var t4=phonef($("#phone"),0);
		var t5=validf($("#valid"),0);
		var t6= $("#visate").val()==""? t6=true : visatef($("#visate"),0);

		if(t2&t3&t4&t5&t6){//所有前台验证

			//验证短信验证码,用户名、手机号
			$.ajax({
				url:rootPath+"/user/regist/validate",
				type:"post",
				data:{"mobile":$("#phone").val(),"validCode":$("#valid").val(),"loginName":$("#username").val(),"password":$("#password").val(),"inviteCode":$("#visate").val()},
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
					if(validError(valid)&validError(password)&validError(phone)&validError(username))
						$("#registForm").submit();
				}
			});
		}
		
	});

	//邀请码
	$("#visate").blur(function(){
		visatef($(this),1)
	});


	//邀请码
	function visatef(vali,va1){
		if(vali.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if(va1==0){
				massage="请您输入邀请码";
				vali.addClass("ipt-error").parent().siblings("em").html(massage);
			}else{

				massage="";
				vali.removeClass("ipt-error").parent().siblings("em").html(massage);
			}
		}else{

			if(vali.val().length==6 && /^([0-9.]+)$/.test(vali.val())){

				massage="";
				vali.removeClass("ipt-error").parent().siblings("em").html(massage);

			}else{

				massage="邀请码为6位数字";
				vali.addClass("ipt-error").parent().siblings("em").html(massage);
			}
		}

		if(massage==""){
			return true;
		}
		return false;
	}


	function validError(v){
		if(!v.isSuccess){
			var id = v.id;
			$("#"+id).addClass("ipt-error").parent().siblings("em").html(v.info).removeClass("hui");
		}
		return v.isSuccess;
	}
	
	//手机验证
	function phonef(phone,ph1){
		var massage="";
		if(phone.val()=="" )
		{
			if(ph1==0){
				massage="请输入手机号码！";
				phone.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}else{
				massage="";
				phone.removeClass("ipt-error").parent().siblings("em").html("该手机号将用于手机号登录和找回密码").addClass("hui");
			}
		}else
		{
			if (/(^1[3|4|5|7|8][0-9]{9}$)/.test(phone.val())) {
				massage="";
				phone.removeClass("ipt-error").parent().siblings("em").html(massage).html("该手机号将用于手机号登录和找回密码").addClass("hui");

			} else {
				massage="手机号码格式错误";
				phone.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}
			
		}
		if(massage==""||massage==null){
			return true;
		}
		return false;
	}

	function isMobileNoExist(phone){

		if(phone.val()!=""){
			var massage="";
			/*判断手机号是否存在*/
			$.ajax({
				url:rootPath+"/user/regist/isMobileNoExist",
				type:"post",
				data:{"mobileNo":phone.val()},
				async:false,
				success:function(data){
					var result = eval("("+data+")");
					if(result.isSuccess){
						massage="";
						phoneV = true;
						phone.removeClass("ipt-error").parent().siblings("em").html(massage).html("该手机号将用于手机号登录和找回密码").addClass("hui");
					}else{
						massage=result.info;
						phone.removeClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
					}
				}
			});
			if(massage==''){
				return true;
			}else{
				return false;
			}
		}
	}
	
	//用户验证码
	function usernamef(user,us1){
		var massage="";
		if(user.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if(us1==0){
				
				massage="请您输入用户名";
				user.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}else{
				massage="";
				user.removeClass("ipt-error").parent().siblings("em").html('请输入4 - 20位字符，支持汉字、字母、数字及"-"、"_组合"').addClass("hui");
				
			}
		}else{

			if(user.val().length>=4 && user.val().length<=20 && !/[^a-zA-Z0-9\u4E00-\u9FA5\_-]/.test(user.val())){
				if(/(^1[3|4|5|7|8][0-9]{9}$)/.test(user.val())){
					massage="手机号不可作为用户名使用";
					user.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
				}else{
					massage="";
					user.removeClass("ipt-error").parent().siblings("em").html('请输入4~20位字符，支持汉字、字母、数字及"-"、"_组合"').addClass("hui");
				}
			}else{
				
				massage="请输入4 - 20位字符，支持汉字、字母、数字及 '-'、'_组合'";
				user.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}
		}
		if(massage==""){
			return true;
		}
		return false;
	}
	function isLoginNameExist(user){
		/*判断用户名是否存在*/
		if(user.val()!=""){

			$.ajax({
				url:rootPath+"/user/regist/isLoginNameExist",
				type:"post",
				data:{"loginName":user.val()},
				async:false,
				success:function(data){
					var result = eval("("+data+")");
					if(result.isSuccess){
						massage="";
						nameV=true;
						user.removeClass("ipt-error").parent().siblings("em").html('请输入4~20位字符，支持汉字、字母、数字及"-"、"_组合"').addClass("hui");
						return false;
					}else{
						massage=result.info;
						user.removeClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
						return true;
					}
				}
			});
		}
	}
	//
	function passwordf(passval1,pa1){
		var massage="";
		if(passval1.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if(pa1==0){
				massage="请您输入密码";
				passval1.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}else{
				massage="";
				passval1.removeClass("ipt-error").parent().siblings("em").html('请输入6~16位字符，支持字母及数字,字母区分大小写').addClass("hui");
			}
		}else{
			
			if(passval1.val().length>=6 && passval1.val().length<=16 && /^[0-9a-zA-Z]+$/.test(passval1.val())){
				massage="";
				passval1.removeClass("ipt-error").parent().siblings("em").html(massage).html('请输入6~16位字符，支持字母及数字,字母区分大小写').addClass("hui");
			}else{
				
				massage="密码为6 -16 位字符，支持字母及数字,字母区分大小写";
				passval1.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
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
				vali.addClass("ipt-error").parent().siblings("em").html(massage);
			}else{
				
				massage="";
				vali.removeClass("ipt-error").parent().siblings("em").html(massage);
			}
		}else{
			
			if(vali.val().length==6 && /^([0-9.]+)$/.test(vali.val())){
				/*
				var djs=0;
				djs=1;
				if(djs==1){
						
					$("#getvalid").attr("disabled",false);
					$("#getvalid").addClass("btn-blue").removeClass("btn-gray");
					$("#getvalid").html('重新获取');
					clearTimeout(interval); 
				}
				
				*/
				
				massage="";
				vali.removeClass("ipt-error").parent().siblings("em").html(massage);
				
			}else{
				
				massage="验证码6位数字";
				vali.addClass("ipt-error").parent().siblings("em").html(massage);
			}
		}
		if(massage==""){
			return true;
		}
		return false;
	}
	
	//倒计时
	var phoneV = false;
	var nameV = false;
	$("#getvalid").click(function(){

		if(phonef($("#phone"),0)&&usernamef($("#username"),0)){
			var phoneNo = $("#phone").val();
			$.ajax({
				url:rootPath+"/user/regist/validate_name_phone",
				type:"post",
				data:{"mobile":$("#phone").val(),"validCode":$("#valid").val(),"loginName":$("#username").val(),"password":$("#password").val()},
				success:function(data){
					var phone =  eval("("+data.phone+")");
					var username =  eval("("+data.username+")");
					var tkString = data.token;
					if(validError(phone)&validError(username)){
						timer($("#getvalid"));
						$.post(rootPath+'/user/regist/getRegisterMsg',{"mobileNo":phoneNo, "token": tkString});
					}
				}
			});

		}
	});

	var intDiff = parseInt(59);//倒计时总秒数量
	function timer(timeval){
		
		timeval.attr("disabled",true);
		timeval.html('60s后重新获取');
		$("#getvalid").addClass("btn-gray").removeClass("btn-blue");
		intDiff=59;
		interval=window.setInterval(function(){
		timeval.html(intDiff+'s后重新获取');
		if(intDiff<=0){
			timeval.attr("disabled",false);
			$("#getvalid").addClass("btn-blue").removeClass("btn-gray");
			timeval.html('重新获取');
			clearTimeout(interval); 
			return;
		}
		intDiff--;
		}, 1000);
	}

	
})//function end


function CheckIntensity(pwd){ 
  var Mcolor,Wcolor,Scolor,Color_Html; 
  var m=0; 
  var Modes=0; 
  for(i=0; i<pwd.length; i++){ 
    var charType=0; 
    var t=pwd.charCodeAt(i); 
    if(t>=48 && t <=57){charType=1;} 
    else if(t>=65 && t <=90){charType=2;} 
    else if(t>=97 && t <=122){charType=4;} 
    else{charType=4;} 
    Modes |= charType; 
  } 
  for(i=0;i<4;i++){ 
  if(Modes & 1){m++;} 
      Modes>>>=1;
  } 
  if(pwd.length<=4){m=1;} 
  if(pwd.length<=0){m=0;} 
 switch(m){ 
    case 1 : 
      Color_Html="弱"; 
	  document.getElementById('rejc').style.backgroundColor = "#fe2a4d"; 
	  document.getElementById('rejc').style.border = "1px solid #fe2a4d"; 
    break; 
    case 2 : 
      Color_Html="中"; 
	  document.getElementById('rejc').style.backgroundColor = "#ffae6c"; 
	  document.getElementById('rejc').style.border = "1px solid #ffae6c"; 
    break; 
    case 3 : 
      Color_Html="强"; 
	  document.getElementById('rejc').style.backgroundColor = "#12b391"; 
	  document.getElementById('rejc').style.border = "1px solid #12b391"; 
    break; 
    default : 
      Color_Html="无"; 
	  document.getElementById('rejc').style.backgroundColor = "#aaaaaa"; 
	  document.getElementById('rejc').style.border = "1px solid #868686"; 
    break; 
  } 
  document.getElementById('rejc').innerHTML=Color_Html; 
}
