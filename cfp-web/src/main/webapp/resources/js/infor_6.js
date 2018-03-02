// JavaScript Document
	window.onload = function() {
		//点击修改
		$("#xiugai").click(function(){
			$("#per-keep").show();
			$("#xiugai2").hide();
			$('input').attr("disabled",false); 
			$('select').attr("disabled",false); 
			
		});
		//点击修改
		$("#xiugai2").click(function(){
			$("#per-keep").show();
			$('input').attr("disabled",false); 
			$('select').attr("disabled",false); 
			$("#xiugai2").hide();
		});
		
	}; 

//修改密码验证
$(function(){
	
	//用户名验证
	$("#per-username").blur(function(){

		var length=user($(this));
		shenf_1($(this),length);
		
	});
	function user(namelength){
		   
      var n=0;   
      for(var i=0;i<namelength.val().length;i++)   
      {   
      //charCodeAt()可以返回指定位置的unicode编码,这个返回值是0-65535之间的整数   
        if(namelength.val().charCodeAt(i)<128)   
        {   
         n++;    
        }   
        else   
        {   
         n+=2;   
        }   
      }   
      return n;  
    }   
        
	
	//用户名验证方法
	function shenf_1(name,leg){
			
		var	reg = /[^a-zA-Z0-9\u4E00-\u9FA5\_-]/;
		if(name.val()==""){
				massage="请输入用户名";
				$("#per-username").addClass("ipt-error").parent().siblings("em").html(massage);
			}else if(leg<4 || leg>=20 || reg.test(name.val())){
				massage='请输入4 - 20位字符，支持汉字、字母、数字及 "-"、"_"组合';
				$("#per-username").addClass("ipt-error").parent().siblings("em").html(massage);
			}else{
				massage="";
				$("#per-username").removeClass("ipt-error").parent().siblings("em").html(massage);
			}
		return massage;
	}
	
	/*常住地址验证验证
	$("#per-province").blur(function(){
	
		shenf_2($(this));
	});*/
	
	//常住地址验证方法
		function shenf_2(province){
		if(province.val()==""){
				massage="请输入常住省份";
				$("#per-province").addClass("ipt-error").parent().siblings("em").html(massage);
			}else{
				massage="";
				$("#per-province").removeClass("ipt-error").parent().siblings("em").html("");
			}
		return massage;
	}
	/*学历验证
	$("#per-education option").click(function(){
		shenf_4();
	});*/
	
	//学历验证方法
		function shenf_4(){
			if($("#per-education").val()==""){
				massage="请选择学历";
				$("#per-education").parent().siblings("em").html(massage);
			}else{
				massage="";
				$("#per-education").parent().siblings("em").html("");
				//$("#per-education").removeClass("ipt-error").parent().siblings("em").html(massage);
			}
		return massage;
	}
	/*详细地址验证验证
	$("#per-address").blur(function(){
		
		shenf_3($(this));
	});*/
	
	//详细地址验证方法
		function shenf_3(address){
		if(address.val()==""){
				massage="请输入常住地址";
				$("#per-address").addClass("ipt-error").parent().siblings("em").html(massage);
			}else{
				massage="";
				$("#per-address").removeClass("ipt-error").parent().siblings("em").html("");
			}
		return massage;
	}
	//点击确认修改时执行
	$("#per-keep").click(function(){
		var t1=shenf_1($("#per-username"),user($("#per-username")));
		/*
		 * 
		var t2=shenf_2($("#per-province"));
		var t3=shenf_3($("#per-address"));
		var t4=shenf_4($("#per-education option")); && t2=="" && t3=="" && t4==""
		 * */

			if(t1==""){
				
				// 保存操作-开始。
				var per_username = $("#per-username").val();
				var per_education = $("#per-education").val();
				var per_province = $("#per-province").val();
				var per_city = $("#per-city").val();
				var per_address = $("#per-address").val();
				
				$.post(rootPath+'/person/saveUserInfo?r='+Math.random(),
	        		{
						username:per_username,
						education:per_education,
						province:per_province,
						city:per_city,
						address:per_address
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
				// 保存操作-结束。
				
			}
	});
	
	
})	

//弹出提示框
function showAlert(title,content,type){
	var img_src = "../images/img/true.jpg";
	if(type == 'error'){
		img_src = "../images/img/false.jpg";
	}
	$("#userInfo_alert_h").html(title);
	$("#userInfo_alert_p").html(content);
	$("#userInfo_alert_img").attr("src", img_src);
	$("#userInfo_alert").slideDown(500);
	$(".zhezhao5").show();
}

	