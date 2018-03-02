// JavaScript Document
$(function(){

				
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
				
				massage="邀请码6位数字";
				vali.addClass("ipt-error").parent().siblings("em").html(massage);
			}
		}
		return massage;
	}
	
	
	/*确定是否可以禁用
	if($("#regitxt").attr("data-val")=='2'){
		
		$("#getvalid").attr("disabled",false);
	}*/
	
	//用户名输入验证
	$("#username").blur(function(){
		
		usernamef($(this),1);
	});
	$("#username").change(function(){
			
			if (/[^a-zA-Z0-9\u4E00-\u9FA5\_-]/.test($(this).val())) {
				massage='请输入4 - 20位字符，支持汉字、字母、数字及 "-"、"_"组合';
				$(this).addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			} else {
				massage="";
				$(this).removeClass("ipt-error").parent().siblings("em").html(massage).html('请输入4 - 20位字符，支持汉字、字母、数字及 "-"、"_"组合').addClass("hui");
			}
			
	});
	
	$("#phone").change(function(){
		if (/^([0-9.]+)$/.test($(this).val())) {
				
			massage="该手机号将用于手机号登录和找回密码";
			$(this).removeClass("ipt-error").parent().siblings("em").html(massage).html(massage).addClass("hui");
		} else {
			massage="手机号码应为 11 位数字";
			$(this).addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
		}
			
	});
	$("#valid").change(function(){
		if (/^([0-9.]+)$/.test($(this).val())) {
				
			massage="";
			$(this).removeClass("ipt-error").parent().siblings("em").html(massage).html(massage);
		} else {
			massage="验证码应为 6 位数字";
			$(this).addClass("ipt-error").parent().siblings("em").html(massage);
		}
	});
	$("#password").change(function(){
		
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

		phonef($(this),1); 
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
		//var t6= $("#visate").val()==""? t6="" : visatef($("#visate"),0);
		if( t2=="" && t3=="" && t4=="" && t5==""){
			//$("form").submit();
			
			window.location.href="register_success.html";
		}
		
	});
	
	//手机验证
	function phonef(phone,ph1){
		
		if(phone.val()=="" )
		{
			if(ph1==0){
			massage="请输入手机号码";
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
				
				massage="手机格式错误";
				phone.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}
			
		}
		return massage;
	}
	
	//用户验证码
	function usernamef(user,us1){
		if(user.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if(us1==0){
				
				massage="请您输入用户名";
				user.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}else{
				massage="";
				user.removeClass("ipt-error").parent().siblings("em").html('请输入4~20位字符，支持汉字、字母、数字及"-"、"_组合"').addClass("hui");
				
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
				
				massage='请输入4~20位字符，支持汉字、字母、数字及"-"、"_组合"';
				user.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}
		}
		return massage;
	}

	function passwordf(passval1,pa1){
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
				
				massage="请输入6~16位字符，支持字母及数字,字母区分大小写";
				passval1.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}
			
		}
		return massage;
	}
	//验证码
	function validf(vali,va1){
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

				massage="";
				vali.removeClass("ipt-error").parent().siblings("em").html(massage);
				
			}else{
				
				massage="验证码6位数字";
				vali.addClass("ipt-error").parent().siblings("em").html(massage);
			}
		}
		return massage;
	}
	
	//倒计时
	$("#getvalid").click(function(){
		
		if(phonef($("#phone"),0)==""){
			timer($(this));
		}
	});
	clearTimeout(interval); 
	var intDiff = parseInt(59);//倒计时总秒数量
	function timer(timeval){
		
		timeval.attr("disabled",true);
		timeval.val('60s后重新获取');
		$("#getvalid").attr("disabled",true);
		intDiff=59;
		interval=window.setInterval(function(){
		timeval.val(intDiff+'s后重新获取');

		if(intDiff<=0){
			timeval.attr("disabled",false);
			$("#getvalid").attr("disabled",false);
			timeval.val('重新获取');
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
	  document.getElementById('rejc').style.backgroundColor = "#d2d4dc"; 
	  document.getElementById('rejc').style.border = "1px solid #d2d4dc"; 
    break; 
  } 
  document.getElementById('rejc').innerHTML=Color_Html; 
}
