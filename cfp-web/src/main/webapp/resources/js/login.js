// JavaScript Document
$(function(){
	//回车登录
	$('#pwlogin').keydown(function(e){
		var ev=document.all?window.event:e;
		if(ev.keyCode==13){
			$("#submit-login").click();
		}
	})
	//用户名输入验证
	$("#unlogin").blur(function(){
		
		unloginf($(this),1);
	});
	
	
	//密码输入验证
	$("#pwlogin").blur(function(){
		
		pwloginf($(this),1);
	});
	
	//点击注册保存时
	$("#submit-login").click(function(){

		var t2=unloginf($("#unlogin"),0);
		var t3=pwloginf($("#pwlogin"),0);
		if( t2=="" && t3=="" ){
			
			//$("form").submit();
			login();
		}
		
	});


	function login(){
		var form = $(".login form");
		$.ajax({
			url:rootPath+"/user/login",
			type:"post",
			data:form.serialize(),
			success:function(data){
				var result = true;
				for(var i=0;i<data.length;i++){
					var _data =  eval("("+data[i]+")");
					if(!_data.isSuccess){
						result = false;
						//登陆失败
						$("#"+_data.id).addClass("ipt-error").parent().siblings("em").html(_data.info);
					}
				}
				//登陆成功
				var pastUrl = $("#pastUrl").val();
				if(result){
					if(pastUrl!=null&&pastUrl!=''){
						window.location = rootPath+pastUrl;
					}else{
						window.location.reload();
					}
				}

			}
		});
	}
	
	//用户验证码
	function unloginf(user,us7){
		
		if(user.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if(us7==0){
				
				massage="请您输入用户名/手机号";
				user.addClass("ipt-error").parent().siblings("em").html(massage);
			}else{
				massage="";
				user.removeClass("ipt-error").parent().siblings("em").html('');
			}
		}else{
					/*判断手机号是否存在
					$.ajax({
						url:"",
						type:"post",
						success:function(data){
							if(data=="1"){
								
								massage="该手机号未注册";
								user.removeClass("ipt-error").parent().siblings("em").html(massage);
							}else{
								
								massage="";
								user.removeClass("ipt-error").parent().siblings("em").html(massage);
							}
						}
					});
					*/
				//用户输入为 手机号
				massage="";
				user.removeClass("ipt-error").parent().siblings("em").html(massage);
			
		}
		return massage;
	}
	
	//
	function pwloginf(passval1,pa7){
		if(passval1.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if(pa7==0){
			massage="请您输入密码";
			passval1.addClass("ipt-error").parent().siblings("em").html(massage);
			}else{
				massage="";
				passval1.removeClass("ipt-error").parent().siblings("em").html('');
			}
		}else{
			
			if(passval1.val().length>=6 && passval1.val().length<=16 && /^[0-9a-zA-Z]+$/.test(passval1.val())){
				
					/*判断密码是否正确
					$.ajax({
						url:"",
						type:"post",
						success:function(data){
							if(data=="1"){
								
								massage="该密码未注册";
								passval1.removeClass("ipt-error").parent().siblings("em").html(massage);
							}else{
								
								massage="";
								passval1.removeClass("ipt-error").parent().siblings("em").html(massage);
							}
						}
					});
					*/
				massage="";
				passval1.removeClass("ipt-error").parent().siblings("em").html(massage).html(massage);
			}else{
				
				massage="密码不正确";
				passval1.addClass("ipt-error").parent().siblings("em").html(massage);
			}
			
		}
		return massage;
	}

	
})//function end


