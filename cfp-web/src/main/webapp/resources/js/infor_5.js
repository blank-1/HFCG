// JavaScript Document
//修改密码验证
$(function(){
	var b1=0;
	var b2=0;
	reg = /^[\u4E00-\u9FA5]{2,4}$/;
	//姓名验证
	$("#sf_name").blur(function(){
		b1=1;
		shenf_1($(this));
	});
	//身份证号验证
	$("#sf_card").blur(function(){
		b1=1;
		shenf_2($(this));
	});
	//姓名验证方法
		function shenf_1(name){
			reg = /^[\u4E00-\u9FA5]{2,4}$/;
		if(name.val()==""){
			
				massage="请输入姓名";
				$("#sf_name").addClass("ipt-error").parent().siblings("em").html(massage);
				
			}else if(!reg.test(name.val()))  {
				
				massage="姓名不符合标准";
				$("#sf_name").addClass("ipt-error").parent().siblings("em").html(massage);
				
			}else{
				massage="";
				$("#sf_name").removeClass("ipt-error").parent().siblings("em").html("");
			}
		return massage;
	}
	//身份证号验证方法
		function shenf_2(number){
			idcard = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
		if(number.val()==""){
			
				massage="请输入身份证号";
				$("#sf_card").addClass("ipt-error").parent().siblings("em").html(massage);
				
			}else if(!idcard.test(number.val())){
				
				massage="身份证号输入不正确";
				$("#sf_card").addClass("ipt-error").parent().siblings("em").html(massage);
				
			}else{
				massage="";
				$("#sf_card").removeClass("ipt-error").parent().siblings("em").html("");
			}
		return massage;
	}
		//点击确认修改时执行
		$("#sf_submit").click(function(){
			$("#sf_submit").html("验证中...");
			var t1=shenf_1($("#sf_name"));
			var t2=shenf_2($("#sf_card"));
				if(t1=="" &&  t2==""){
					// 身份验证提交-【开始】。
					var sf_name = $("#sf_name").val();
					var sf_card = $("#sf_card").val();
					$.ajax({
						url : rootPath + "/person/identityAuthentication",
						type : "post",
						data : {
							trueName:sf_name,
							idCard:sf_card
						},
						async : false,
						error : function(XHR) {
							$("#sf_card").addClass("ipt-error").parent().siblings("em").html("现在认证的人数过多正在排队，请您稍后再试");
							$("#sf_submit").html("验证");
						},
						success : function(data) {
							if(data.result == 'success'){
		        	    		$("#shenf_hide").hide();
		        				$("#shenf_show").show();
		        	    	}else if(data.result == 'error'){
		        	    		if(data.errCode == 'idCardEmpty'){
		        	    			//alert("验证提示:"+data.errMsg);
		        	    			$("#sf_card").addClass("ipt-error").parent().siblings("em").html(data.errMsg);
		        	    		}else if(data.errCode == 'idCardCheck'){
		        	    			//alert("系统提示:"+data.errMsg);
		        	    			$("#sf_card").addClass("ipt-error").parent().siblings("em").html(data.errMsg);
		        	    		}else if(data.errCode == 'trueNameEmpty'){
		        	    			//alert("系统提示:"+data.errMsg);
		        	    			$("#sf_name").addClass("ipt-error").parent().siblings("em").html(data.errMsg);		        	    			//alert("系统提示:"+data.errMsg);
		        	    		}else if(data.errCode == 'identityError'){
		        	    			//alert("系统提示:"+data.errMsg);
		        					$("#sf_card").addClass("ipt-error").parent().siblings("em").html(data.errMsg);
		        					$("#idenhelp").css("display","block");
		        	    		}
		        	    		$("#sf_submit").html("验证");
		        	    	}else{
		        	    		//alert("系统提示:"+"网络异常，请稍后操作！");
		        	    		$("#sf_card").addClass("ipt-error").parent().siblings("em").html("网络异常，请稍后操作！");
		        	    		$("#sf_submit").html("验证");
		        	    	}
						}
					});
					// 身份验证提交-【结束】。			
				}
		});
		
		$(function() {
			//$(document).on("click","#queren",function(){
			//身份验证成功
			$("#queren").click(function(){
				//$("#success").text("身份验证成功");
				//window.location.href=rootPath+"/person/toAuthentication";
				window.location.reload();
			});
			//手机验证成功
			$("#queren2").click(function(){
				//window.location.href=rootPath+"/person/toAuthentication";
				window.location.reload();
			});
			//邮箱验证成功
			$("#queren3").click(function(){
				$("#success3").text("邮箱绑定成功");
				$("#bangding").text("已绑定").removeClass("yanz_red");
				$("#youxiang img").attr("src","../images/yanz_01.jpg");
			});
		})
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//邮箱验证
	
	$("#email").change(function(){
			if (/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/.test($(this).val())) 
			{
				massage="";
				$(this).removeClass("ipt-error").parent().siblings("em").html(massage).html(massage);
			} else {
				massage="邮箱格式不正确";
				$(this).addClass("ipt-error").parent().siblings("em").html(massage);
			}
	});
	//验证码验证
	$("#valid2").change(function(){
			if (/^([0-9.]+)$/.test($(this).val())) {
				massage="";
				$(this).removeClass("ipt-error").parent().siblings("em").html(massage).html(massage);
			} else {
				massage="验证码应为 6 位数字";
				$(this).addClass("ipt-error").parent().siblings("em").html(massage);
			}
	});
	//邮箱验证
	$("#email").blur(function(){
		b5=1;
		email($(this));
	});
	//验证码验证
	$("#valid2").blur(function(){
		b4=1;
		validf2($(this));
	});
	//邮箱验证方法
	function email(emailval){
		if(emailval.val()=="" )
		{
			if(emailval.val()==""){
				massage="请您输入邮箱地址";
				emailval.addClass("ipt-error").parent().siblings("em").html(massage);
			}else{
				
				massage="";
				emailval.removeClass("ipt-error").parent().siblings("em").html(massage);
			}
		}
		return massage;
	}
	//验证码验证方法
	function validf2(vali){
		if(vali.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if(vali.val()==""){
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
	//点击下一步时执行
	$("#nextstep").click(function(){
		
		var c1=email($("#email"));
		var c2=validf2($("#valid2"));
		if( c1=="" && c2==""){
			$("#email_hide").hide();
			$("#email_show").show();
		}
		
	});
	
	//点击验证码时的倒计时
	$("#getvalid2").click(function(){
		b5=0;
		if(email($("#email"))==""){
			timer($(this));
		}
	});
	var intDiff = parseInt(59);//倒计时总秒数量
	function timer(timeval){
		
		timeval.attr("disabled",true);
		timeval.html('60s后重新获取');
		$("#getvalid2").addClass("btn-gray").removeClass("huoqu_yanzm");
		intDiff=59;
		interval=window.setInterval(function(){
		timeval.html(intDiff+'s后重新获取');
		if(intDiff<=0){
			timeval.attr("disabled",false);
			$("#getvalid2").addClass("huoqu_yanzm").removeClass("btn-gray");
			timeval.html('重新获取');
			clearTimeout(interval); 
			return;
		}
		intDiff--;
		}, 1000);
	}
	
})//function end

