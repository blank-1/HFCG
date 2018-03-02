// JavaScript Document
var interval;
var vlnum=0;
$(function(){

	var tkString;
	
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
	
	//邀请码
	$("#visate").blur(function(){
		visatef($(this),1);
	});
	
	
	//邀请码
	function visatef(vali,va1){
		var massage = "" ;
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
	
	
	//确定是否可以禁用
	if($("#regitxt").attr("data-val")=='2'){
		
		$("#getvalid").attr("disabled",false);
	}
	
	//用户名输入验证
	$("#username").blur(function(){

		if(usernamef($(this),1)){
			isLoginNameExist($(this));
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
/*	$("#valid").change(function(){
		var massage="";

		if (/^([0-9.]+)$/.test($(this).val())) {
				
			massage="";
			$(this).removeClass("ipt-error").parent().siblings("em").html(massage).html(massage);
		} else {
			massage="验证码应为 6 位数字";
			$(this).addClass("ipt-error").parent().siblings("em").html(massage);
		}
	});*/

	$(document).on("change","#password",function(){
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
	$(document).on("blur","#password",function(){

		passwordf($(this),1);
	});
	//手机号码输入验证
	$("#phone").blur(function(){

		if(phonef($(this),1)){
			isMobileNoExist($(this));

		}
	});
	//验证码输入验证
//	$("#valid").blur(function(){

	//	validf($(this),1);
	//});
	
	//点击注册保存时
	$("#submit-register").click(function(){
		$("#getvalids").html('60 秒后重新获取验证');
		var imgCode = $('#valid').val();
		var v0_ = checkImgCode(imgCode,0);

		var t2=usernamef($("#username"),0);
		var t3=passwordf($("#password"),0);
		var t4=phonef($("#phone"),0);
		var t6= $("#visate").val()==""? t6=true : visatef($("#visate"),0);


		if(!v0_){
			myReload();
		}
		if(v0_&t2&t3&t4&t6){//所有前台验证

			//验证短信验证码,用户名、手机号
			$.ajax({
				url:rootPath+"/user/regist/validateImg",
				type:"post",
				data:{"mobile":$("#phone").val(),"validCode":$("#valid").val(),"loginName":$("#username").val(),"password":$("#password").val(),"inviteCode":$("#visate").val()},
				success:function(data){
				//	var valid =  eval("("+data.valid+")");
					var password =  eval("("+data.password+")");
					var phone =  eval("("+data.phone+")");
					var username =  eval("("+data.username+")");
					if(data.visate!=null){
						var visate =  eval("("+data.visate+")");
						if(!validError(visate))
							return ;
					}
					
					if(//validError(valid)&
							validError(password)&validError(phone)&validError(username)){
						//$("#registForm").submit();
							//$("form").submit();
						//window.location.href="register_success.html";
						sendRegisterMessage();
					}
				}
			});
		}
		
	});
	function noneipt(){
	
		$("#piptime").val("");
		$("#piptime2").val("");
		$("#piptime3").val("");
		$("#piptime4").val("");
		$("#piptime5").val("");
		$("#piptime6").val("");
		
		$(".imgre").html('');
		$(".zhuan").html('');
		
	}
	function nonecishu(){
			vlnum=0;
	}
	function havecishu(){
		vlnum=1;
	}
	$("#phclose,#phclose2").click(function(){
		
		clearTimeout(interval); 
		$(this).parents(".masklayer").hide();
		$(".zhezhao1").hide();
	});
	var hidden=0;//次数
	
	//倒计时
	$("#getvalids").click(function(){
		if($(this).attr("disabledflag")!="true"){
			sendRegisterMessage();
		}
		return ;
	});
	//文本框禁用
	function iptdisno(){
		$("#piptime").attr("disabled",true).css("cursor","no-drop");
		$("#piptime2").attr("disabled",true).css("cursor","no-drop");
		$("#piptime3").attr("disabled",true).css("cursor","no-drop");
		$("#piptime4").attr("disabled",true).css("cursor","no-drop");
		$("#piptime5").attr("disabled",true).css("cursor","no-drop");
		$("#piptime6").attr("disabled",true).css("cursor","no-drop");
		$("#getvalids").attr("disabled",true).css("cursor","no-drop").css({"cursor":"no-drop","background-color:":"#f1f1f1"});
		$("#getvalids").attr("disabledflag","true");
	}
	//文本框启用
	function iptdisyes(){
		
		$("#piptime").attr("disabled",false).css("cursor","auto");
		$("#piptime2").attr("disabled",false).css("cursor","auto");
		$("#piptime3").attr("disabled",false).css("cursor","auto");
		$("#piptime4").attr("disabled",false).css("cursor","auto");
		$("#piptime5").attr("disabled",false).css("cursor","auto");
		$("#piptime6").attr("disabled",false).css("cursor","auto");
		$("#getvalids").attr("disabled",false).css({"cursor":"auto","background-color:":"#fff"});
		$("#getvalids").attr("disabledflag","false");
	}
	$("#getvalids").attr("disabled",true);
	$("#getvalids").attr("disabledflag","true");
	
	$("#piptime2").focus(function(){
		
	});
	//手机密码验证
	$("#piptime").keyup(function(){

		if (/^([a-z]|[A-Z]|[0-9])$/.test($(this).val())) {
			$("#piptime2").focus();
			tijiao();
		}
		
		
	});
	$("#piptime2").keyup(function(e){
		
		if (/^([a-z]|[A-Z]|[0-9])$/.test($(this).val())) {
			$("#piptime3").focus();
			tijiao();
		}
	   if(e.keyCode == 8){
		
			$("#piptime").focus();
	   }
	});
	$("#piptime3").keyup(function(e){

		if (/^([a-z]|[A-Z]|[0-9])$/.test($(this).val())) {
			$("#piptime4").focus();
			tijiao();
		}
	   if(e.keyCode == 8){
		
			$("#piptime2").focus();
	   }
	});
	$("#piptime4").keyup(function(e){

		if (/^([a-z]|[A-Z]|[0-9])$/.test($(this).val())) {
			$("#piptime5").focus();
			tijiao();
		}
		
	   if(e.keyCode == 8){
		
			$("#piptime3").focus();
	   }
	});
	$("#piptime5").keyup(function(e){

		if (/^([a-z]|[A-Z]|[0-9])$/.test($(this).val())) {
			$("#piptime6").focus();
			tijiao();
		};
	   if(e.keyCode == 8){
		
			$("#piptime4").focus();
	   };
	});
	$("#piptime6").keyup(function(e){

	   if(e.keyCode == 8){
		
			$("#piptime5").focus();
	   }
	});
	function keyfocus(pict){
		pict.prevAll("input").each(function() {
            if($(this).val()!=""){
				
			}
			
        });
	}
	$("#piptime6").keyup(function(){
		if(/^([a-z]|[A-Z]|[0-9])$/.test($(this).val())){
			tijiao();
		}else{
			$(".imgre").html('');
		}
	});
	
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

	//眼睛
	$("#passexchange").click(function (){

		if($("#password").attr("type")=='password'){

			var pass_cup = $("#password").val();
			$(this).css("background-image","url('"+rootPath+"/images/zheng.png')");
			$("#password").remove();
			$(this).after('<input type="text" autocomplete="off" maxlength="16" name="loginPass" id="password" value="' + pass_cup +'" placeholder="请输入密码" class="ipt-input widpass" onKeyUp="CheckIntensity(this.value)" />');

		}else{

			var pass_cup = $("#password").val();

			$(this).css("background-image","url('"+rootPath+"/images/bi.png')");
			$("#password").remove();
			$(this).after('<input type="password" autocomplete="off" maxlength="16" name="loginPass" id="password" value="' + pass_cup +'" placeholder="请输入密码" class="ipt-input widpass" onKeyUp="CheckIntensity(this.value)" />');

		}
	});
	
	//提交验证码
	function tijiao(){
			var i1=$("#piptime").val();
			var i2=$("#piptime2").val();
			var i3=$("#piptime3").val();
			var i4=$("#piptime4").val();
			var i5=$("#piptime5").val();
			var i6=$("#piptime6").val();
			if( i1!="" &&  i2!="" &&  i3!="" &&  i4!="" &&  i5!="" && i6!=""  ){
				
				$(".imgre").html('<img src="'+rootPath+'/images/loading220.gif">');
				var smsCode = i1+i2+i3+i4+i5+i6 ;
				checkMessage(smsCode);
			}
			
			/*$(this).blur();
			//触发手机密码验证
			$(".imgre").html('<img src="../images/pay_04.png">');
			if($(".dhidden").is(":visible")){
				
			}
			*/
	}
	
	//验证码
	$("#valid3").keyup(function(){
		if($(this).val().length==4){
			var imgCode = $(this).val();
			if(checkImgCode(imgCode,1)){
				//触发验证事件
				$("#valid3").val("");	
				//$(".zhuan").html('<img src="../images/pay_04.png">');
				$(".dhidden").slideUp(500);
				$(".zhuan").html('<img src="'+rootPath+'/images/pay_04.png">');
				iptdisyes();
				$("#pcolor").html('短信验证码已发送，请注意查收');
				timer($("#getvalids"));
				$.post(rootPath+'/user/regist/getRegisterMsg',{"mobileNo":$("#phone").val(),"token":tkString});
			}
		}
	});
	var intDiff = parseInt(59);//倒计时总秒数量
	function timer(timeval){
		noneipt();
		if(hidden>=1){
			if(vlnum==0){
				iptdisno();
				$(".dhidden").slideDown(500);
				vlnum=1;
				return
			}else{
				iptdisyes();
				vlnum=0;
			}
		}
		if(hidden==2){
			$("#title").html("验证码发送过于频繁，请输入图形验证码再次获取短信");
		}
		$(".pcolor").html('短信验证码已发送，请注意查收');
		timeval.attr("disabled",true);
		timeval.attr("disabledflag","true");
		timeval.html('60 秒后重新获取验证');
		intDiff=59;
		interval=window.setInterval(function(){
		timeval.html(intDiff+' 秒后重新获取验证');
		if(intDiff<=0){
			//hidden=hidden+1;
			timeval.attr("disabled",false);
			timeval.attr("disabledflag","false");
			timeval.html('重新获取');
			$(".pcolor").html('&nbsp;');
			clearTimeout(interval); 
			return;
		}
		intDiff--;
		}, 1000);
	}
	
	function sendRegisterMessage(type){
		$.ajaxSetup({async : false});
		$(".pcolor").html('');
		if(phonef($("#phone"),0)&&usernamef($("#username"),0)){
			$.ajax({
				url:rootPath+"/user/regist/validate_name_phone_img",
				type:"post",
				async : false,
				data:{"mobile":$("#phone").val(),"validCode":$("#valid").val(),"loginName":$("#username").val(),"password":$("#password").val()},
				success:function(data){
					var count = eval("(" + data.count + ")");
					var phone =  eval("("+data.phone+")");
					var username =  eval("("+data.username+")");
					tkString = data.token;
					var phoneNo = data.phoneNo;
					$(".ptitle").text("(已发送至"+phoneNo.substring(0,3)+"****"+phoneNo.substring(7,11)+")");
					$("#kefu").hide();
					if(count >= 3 && count<100){
						
						$(".zhezhao1").show();
						$("#phonemask").slideDown(500);
						noneipt();
//						havecishu();
						nonecishu();
						$("#valid3").val("");	
						hidden= 2;
						timer($("#getvalids"));
//						$("#dhidden").show();
						$(".dhidden1").hide();
					}else if(count<3){
						hidden = 0;
						$(".zhezhao1").show();
						$("#phonemask").slideDown(500);
						noneipt();
						havecishu();
						$("#valid3").val("");	
						if(validError(phone)&validError(username)){
							iptdisyes();
							timer($("#getvalids"));
							$.post(rootPath+'/user/regist/getRegisterMsg',{"mobileNo":phoneNo,"token":tkString});
						}else{
//							alert("error");
						}
						$(".dhidden").hide();
						$(".dhidden1").hide();
					}else if(count >= 100){
						$(".zhezhao1").show();
						$("#phonemask").slideDown(500)
						//$("#kefu").show();
						iptdisno();
						$(".dhidden1").show();
						$(".dhidden").hide();
						
					}
					myReload();
				}
			});

		}
		$.ajaxSetup({async : true});
	}
	
	
	
	 var ranString = getRandomString(10);
	function checkImgCode(imgCode,v){
		var flag = false;
		var url = rootPath + "/user/regist/validImgCode?r=" + Math.random() ;
		$.ajax({
			url: url,
			type:"post",
			async:false,
			data:{"auth":ranString,"imgCode":imgCode},
			success:function(returnData){
				var msg = eval("("+returnData.imgCode+")");
				if(v==0){
					validError1('valid', msg);
				} else {
					validErrorParents($('#valid3'), msg);
				}
				if(msg.isSuccess == true || msg.isSuccess == "true"){
					flag = true;
					$(".zhuan").html('<img src="'+rootPath+'/images/pay_04.png" />');
				}else{
					$(".zhuan").html('<img src="'+rootPath+'/images/res_03.png" />');
				}
			}
		});
		return flag;
	}
	
	  function myReload(){  
		   var url=rootPath + "/user/regist/image";
		   $(".imgCodeValid").attr("src",url + "?v=" + Math.random() + "&auth=" + ranString);
	    }  
	  
	  $('.imgCodeValid').click(function(){
		  myReload();
	  })
	  
	  myReload();
	  
	  
	  function checkMessage(smsCode){
		  var url = rootPath + "/user/regist/validSmsCode?v="+Math.random();
		  var phoneNo = $("#phone").val();
		  $.ajax({
			  url:url,
			  type:"post",
			  data:{"validCode":smsCode,"mobile":phoneNo,"token":tkString},
			  async:false,
			  dataType:"json",
			  success:function(returnData){
				  var msg = eval("(" + returnData.valid+")");
				  validErrorSMS($(".pcolor"), msg);
				  if(msg.isSuccess){
					  $("#validCode").val(smsCode);
					  $("#phclose").click();
					  $("#registForm").submit();
				  }
			  }
		  });
		  $(".imgre").html('');
	  }
	  
})//function end
	
	function validError(v){
		if(!v.isSuccess){
			var id = v.id;
			$("#"+id).addClass("ipt-error").parent().siblings("em").html(v.info).removeClass("hui");
		}
		return v.isSuccess;
	}

	function validError1(v,msg){
		var id = v;
		if(!msg.isSuccess){
			$("#"+id).addClass("ipt-error").parent().siblings("em").html(msg.info).removeClass("hui");

		}else{
			$("#"+id).removeClass("ipt-error").parent().siblings("em").html('');
		}
		return msg.isSuccess;
	}

function validErrorParents(obj,msg){
	if(!msg.isSuccess){
		obj.addClass("ipt-error").parents(".dhidden").find("em").html(msg.info);
		//$("#"+id).addClass("ipt-error").parent().siblings("em").html(v.info).removeClass("hui");
	}else{
		obj.removeClass("ipt-error").parents(".dhidden").find("em").html('');
	}
	return msg.isSuccess;
}

function validErrorSMS(obj,msg){
	if(!msg.isSuccess){
		obj.html(msg.info);
		//$("#"+id).addClass("ipt-error").parent().siblings("em").html(v.info).removeClass("hui");
	}else{
		obj.html('');
	}
	return msg.isSuccess;
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
				user.removeClass("ipt-error").parent().siblings("em").html('请输入4 - 20位字符，支持汉字、字母、数字及"-"、"_"组合').addClass("hui");
				
			}
		}else{
			
			/*判断用户名是否存在
			$.ajax({
				url:"",
				type:"post",
				success:function(data){
				    if(data=="1"){
						
						massage="";
						user.removeClass("ipt-error").parent().siblings("em").html('请输入4~20位字符，支持汉字、字母、数字及"-"、"_组合"').addClass("hui");
					}else{
						
						massage="用户名已存在";
						user.removeClass("ipt-error").parent().siblings("em").removeClass("hui");
					}
				}
			});
			*/
			
			if(user.val().length>=4 && user.val().length<=20 && !/[^a-zA-Z0-9\u4E00-\u9FA5\_-]/.test(user.val())){
				if(/(^1[3|4|5|7|8][0-9]{9}$)/.test(user.val())){
					massage="手机号不可作为用户名使用";
					user.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
					
				}else{
					massage="";
					user.removeClass("ipt-error").parent().siblings("em").html('请输入4~20位字符，支持汉字、字母、数字及"-"、"_"组合').addClass("hui");
						
				}
			}else{
				
				massage='请输入4 - 20位字符，支持汉字、字母、数字及 "-"、"_"组合';
				user.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}
		}
		if(massage==""){
			return true;
		}
		return false;
	}

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
			
			if(vali.val().length==4 ){
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
				if(va1==9){
					massage="验证码错误，请点击图片重新获取";
					vali.addClass("ipt-error").parent().siblings("em").html(massage);
				}else{
					massage="";
					vali.removeClass("ipt-error").parent().siblings("em").html(massage);
				}
			}else{
				
				massage="验证码4位数字";
				vali.addClass("ipt-error").parent().siblings("em").html(massage);
			}
		}
		if(massage==""){
			return true;
		}
		return false;
	}
	
	//验证码
	function validforSms(vali,va1){
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

function getRandomString(len){
	  var chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	  len = len || 18;
	  var pos = chars.length;
	  var res = "";
	  for(var i = 0 ; i < len ; i++){
		  res += chars.charAt(Math.floor(Math.random() * pos));
	  }
	  return res;
}

