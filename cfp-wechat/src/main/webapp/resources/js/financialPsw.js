// JavaScript Document
$(function(){
	$("#psw").attr("value","");
});
function checkPsw(){
	var psw = $("#psw").val();
	if (psw.length == 0) {
		$(".l_psw span").hide();
	}else if (psw.length == 1) {
		$(".l_psw span").hide();
		$("#s01").css("display","block");
	}else if (psw.length == 2) {
		$(".l_psw span").hide();
		$("#s01").css("display","block");
		$("#s02").css("display","block");
	}else if (psw.length == 3) {
		$(".l_psw span").hide();
		$("#s01").css("display","block");
		$("#s02").css("display","block");
		$("#s03").css("display","block");
	}else if (psw.length == 4) {
		$(".l_psw span").hide();
		$("#s01").css("display","block");
		$("#s02").css("display","block");
		$("#s03").css("display","block");
		$("#s04").css("display","block");
	}else if (psw.length == 5) {
		$(".l_psw span").hide();
		$("#s01").css("display","block");
		$("#s02").css("display","block");
		$("#s03").css("display","block");
		$("#s04").css("display","block");
		$("#s05").css("display","block");
	}else if (psw.length == 6) {
		$(".l_psw span").hide();
		$("#s01").css("display","block");
		$("#s02").css("display","block");
		$("#s03").css("display","block");
		$("#s04").css("display","block");
		$("#s05").css("display","block");
		$("#s06").css("display","block");
		$("#psw").blur();
		var loanApplicationId = $("#loanApplicationId").val();
		// 请求服务器端验证
		$.ajax({
	        url:rootPath+"/finance/getPass",
	        type:"post",
	        data: {
	          "pass":psw,
	          "loanApplicationId": loanApplicationId
	        },
	        success: function (data) {
	        	if(data=="success"){
	        		$("#targetPass").attr("value",psw);
	        		$("#financialPswForm").submit();	        		
	        	}else if(data=="fail"){
	        		$("#PswTip").show();// 错误提示	
	        		$("#PswTip").fadeOut(2000,function(){//提示消失
	        			$(".l_psw span").hide();//黑点清空
	        			$("#psw").attr("value","");//隐藏域清空
	        		});
	        	}
	        },error:function(data){
	        	$("#PswTip").show();// 错误提示	
	        }
	      });
		
	};
}