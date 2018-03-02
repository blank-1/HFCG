// JavaScript Document
//修改密码验证
$(function(){
	

	//原密码输入验证
	$("#password_1").blur(function(){
		
		passwordf($(this),1);
	});
	//新密码输入验证
	$("#password_2").blur(function(){
		
		password_1f2($(this));
	});
	//确认新密码输入验证
	$("#password_3").blur(function(){

		password_1f3($(this));
	});
	
	//原密码验证方法
	function passwordf(passval1,pa10){
		if(passval1.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if(pa10==0){
			massage="请您输入密码";
			passval1.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}else{
				massage="";
				passval1.removeClass("ipt-error").parent().siblings("em");
			}
		}else{
			if(passval1.val().length>=6 && passval1.val().length<=16 && /^[0-9a-zA-Z]+$/.test(passval1.val())){
				massage="";
				passval1.removeClass("ipt-error").parent().siblings("em").html(massage);
			}else{
				massage="密码为6 -16 位字符，支持字母及数字,字母区分大小写";
				passval1.addClass("ipt-error").parent().siblings("em").html(massage);
			}
		}
		return massage;
	}
	//新密码验证方法
	function password_1f2(passval2){
		if($("#password_1").val()==$("#password_2").val())
			{
				massage="原密码与新密码不能相同";
				$("#password_2").addClass("ipt-error").parent().siblings("em").html(massage);
			}else if($("#password_2").val()==""){
				massage="请输入新密码";
				$("#password_2").addClass("ipt-error").parent().siblings("em").html(massage);
				
			}else{
				massage="";
				$("#password_2").removeClass("ipt-error").parent().siblings("em").html("");
			}
		return massage;
	}
	//确认密码验证方法
	function password_1f3(passval3){
		if($("#password_2").val()!=$("#password_3").val())
			{
				massage="新密码与确认密码不相同";
				$("#password_3").addClass("ipt-error").parent().siblings("em").html(massage);
			}else{
				massage="";
				$("#password_3").removeClass("ipt-error").parent().siblings("em").html("");
			}
		return massage;
	}
	//点击确认修改时执行
	$("#submit-findpwd22").click(function(){
		var t1=passwordf($("#password_1"),0);
			if(t1=="" ){
				var t2=password_1f2($("#password_2"))
				if(t2=="" ){
					var t3=password_1f3($("#password_3"))
					if(t3=="" ){ 
						
						// 提交操作-开始。
						var login_old = $("#password_1").val();
						var login_new = $("#password_2").val();
						var token = $("#token").val();
						$.post(rootPath+'/person/savePass',
			        		{
								pass_type:'login',
								login_old:login_old,
								login_new:login_new,
								token:token
			        		},
			        		function(data){
			        	    	if(data.result == 'success'){
			        	    		showAlert("操作提示","操作成功！","success");
			        	    	}else if(data.result == 'error'){
			        	    		if(data.errCode == 'check'){
			        	    			showAlert("验证提示",data.errMsg,"error");
			        	    		}else{
			        	    			showAlert("系统提示",data.errMsg,"error");
			        	    		}
			        	    	}else{
			        	    		showAlert("系统提示","网络异常，请稍后操作！","error");
			        	    	}
			        	},'json');
						// 提交操作-结束。
					}
				}
			}
	});
	
})//function end

//交易密码验证
$(function(){
	
	//原密码输入验证
	$("#jiaoyi_1").blur(function(){
	
		jiaoyi_pwd($(this),1);
	});
	//新密码输入验证
	$("#jiaoyi_2").blur(function(){
	
		jiaoyi2_pwd($(this));
	});
	//确认新密码输入验证
	$("#jiaoyi_3").blur(function(){
	
		jiaoyi3_pwd($(this));
	});
	
	//原密码验证方法
	function jiaoyi_pwd(jiaoyival1,ji1){
		if(jiaoyival1.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if(ji1==0){
			massage="请您输入原密码";
			jiaoyival1.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}else{
				massage="";
				jiaoyival1.removeClass("ipt-error").parent().siblings("em");
			}
		}else{
			if(jiaoyival1.val().length>=6 && jiaoyival1.val().length<=16 && /^[0-9a-zA-Z]+$/.test(jiaoyival1.val())){
				massage="";
				jiaoyival1.removeClass("ipt-error").parent().siblings("em").html(massage);
			}else{
				massage="密码为6 -16 位字符，支持字母及数字,字母区分大小写";
				jiaoyival1.addClass("ipt-error").parent().siblings("em").html(massage);
			}
		}
		return massage;
	}
	//新密码验证方法
	function jiaoyi2_pwd(jiaoyival2){
		if($("#jiaoyi_1").val()==$("#jiaoyi_2").val())
			{
				massage="原密码与新密码不能相同";
				$("#jiaoyi_2").addClass("ipt-error").parent().siblings("em").html(massage);
				
			}else if($("#jiaoyi_2").val()==""){
				
				massage="请输入新密码";
				$("#jiaoyi_2").addClass("ipt-error").parent().siblings("em").html(massage);
				
			}else{
				massage="";
				$("#jiaoyi_2").removeClass("ipt-error").parent().siblings("em").html("");
			}
		return massage;
	}
	//确认密码验证方法
	function jiaoyi3_pwd(jiaoyival3){
		if($("#jiaoyi_2").val()!=$("#jiaoyi_3").val())
			{
				massage="新密码与确认密码不相同";
				$("#jiaoyi_3").addClass("ipt-error").parent().siblings("em").html(massage);
			}else{
				massage="";
				$("#jiaoyi_3").removeClass("ipt-error").parent().siblings("em").html("");
			}
		return massage;
	}
	//点击下一步时执行
	$("#submit-jiaoyi").click(function(){ 
	
		var t4=jiaoyi_pwd($("#jiaoyi_1"),0);
			if(t4=="" ){
				var t5=jiaoyi2_pwd($("#jiaoyi_2"))
				if(t5=="" ){
					var t6=jiaoyi3_pwd($("#jiaoyi_3"))
					if(t6=="" ){ 
						
						// 提交操作-开始。
						var trade_old = $("#jiaoyi_1").val();
						var trade_new = $("#jiaoyi_2").val();
						var token = $("#token").val();
						
						$.post(rootPath+'/person/savePass',
			        		{
								pass_type:'trade',
								trade_old:trade_old,
								trade_new:trade_new,
								token:token
			        		},
			        		function(data){
			        	    	if(data.result == 'success'){
			        	    		showAlert("操作提示","操作成功！","success");
			        	    	}else if(data.result == 'error'){
			        	    		if(data.errCode == 'check'){
			        	    			showAlert("验证提示",data.errMsg,"error");
			        	    		}else{
			        	    			showAlert("系统提示",data.errMsg,"error");
			        	    		}
			        	    	}else{
			        	    		showAlert("系统提示","网络异常，请稍后操作！","error");
			        	    	}
			        	},'json');
						// 提交操作-结束。
						
					}
				}
			}
	});
	
})//function end

//弹出提示框
function showAlert(title,content,type){
	var img_src = "../images/img/true.jpg";
	if(type == 'error'){
		img_src = "../images/img/false.jpg";
	}
	$("#passManage_alert_h").html(title);
	$("#passManage_alert_p").html(content);
	$("#passManage_alert_img").attr("src", img_src);
	$("#passManage_alert").slideDown(500);
	$(".zhezhao5").show();
}
//关闭提示框
function closeAlert(){
	$("#passManage_alert").hide();
	$(".zhezhao5").hide();
	window.location.reload();
}

//密码强弱的方法
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

function CheckIntensity2(pwd){ 
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
	  document.getElementById('rejc2').style.backgroundColor = "#fe2a4d"; 
	  document.getElementById('rejc2').style.border = "1px solid #fe2a4d"; 
    break; 
    case 2 : 
      Color_Html="中"; 
	  document.getElementById('rejc2').style.backgroundColor = "#ffae6c"; 
	  document.getElementById('rejc2').style.border = "1px solid #ffae6c"; 
    break; 
    case 3 : 
      Color_Html="强"; 
	  document.getElementById('rejc2').style.backgroundColor = "#12b391"; 
	  document.getElementById('rejc2').style.border = "1px solid #12b391"; 
    break; 
    default : 
      Color_Html="无"; 
	  document.getElementById('rejc2').style.backgroundColor = "#aaaaaa"; 
	  document.getElementById('rejc2').style.border = "1px solid #868686"; 
    break; 
  } 
  document.getElementById('rejc2').innerHTML=Color_Html; 
}
