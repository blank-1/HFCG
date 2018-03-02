// JavaScript Document
function limitNUM(n){
    var leg = $(".limitNUM"+n).val().length;
    if (leg > n) {
		$(".limitNUM"+n).val($(".limitNUM"+n).val().substring(0,n));
    }
}
window.onload=function(){
	
	var isVerified =document.getElementById("isVerified");

	var isCustomerCard =document.getElementById("isCustomerCard");

	//判断是否实名认证
	if('1'==isVerified.value){
		light1.style.display='none';
		fade1.style.display='none';
		backgd.style.overflow='visible'
	}else{
		light1.style.display='block';
		fade1.style.display='block';
		backgd.style.overflow='hidden'; 
	}
	//判断绑卡
//	if('false' == isCustomerCard.value){
//		light.style.display='block';
//		fade.style.display='block';
//		backgd.style.overflow='hidden'; 
//	}else{
//		light.style.display='none';
//		fade.style.display='none';
//		backgd.style.overflow='visible'
//	}
//验证码点击效果    
//var wait=60;
//document.getElementById("btn").disabled = false;  
//function time(o) {
//        if (wait == 0) {
//            o.removeAttr("disabled",false);          
//            o.val("获取验证码");
//            wait = 60;
//        } else {
//            o.attr("disabled", true);
//            o.val("" + wait + "秒");
//            wait--;
//            setTimeout(function() {
//                time(o)
//            },
//            1000)
//        }
//    }
//document.getElementById("btn").onclick=function(){time(this);}

//倒计时
//$("#btn").click(function(){
//	var url;
//	var data;
//	if ($("#payType").val() == "1") {
//		if(moneyf($("#recharge"))=="") {
//			time($("#btn"));
//			//获取支付验证码
//			url = rootPath + "/recharge/invokeRecharge";
//			data = {
//				cardId: $("#cardId").val(),
//				rechargeAmount: $("#recharge").val()
//			}
//		}
//	}else{
//		if(phonef($("#phone"),0)==""&&bankf($("#bankid"),"1")==""&&moneyf($("#recharge"))=="") {
//			timer(this);
//
//			// 获取验证码-【开始】。
//			url = rootPath+"/recharge/invokeBindCardRecharge";
//			data = {
//				cardNo : $("#bankid").val(),
//				phone : $("#phone").val()
//			};
//		}
//	}
//
//	$.post(url, data, function(_data){
//		//异常处理
//		if (!_data.isSuccess) {
//			$("#alart").text(_data.info);
//			alart.style.display="block";
//			setTimeout("clean()",3000);
//		}else{
//			
//		}
//	}, "json");
//	// 获取验证码-【结束】。
//
//});
//点击提交订单，去支付时(无卡)
$("#nocardbtn").bind("click",function(){
	$("#loading").show();
	var b2=$("#cardNo").is(":visible")?bankf($("#cardNo"),"1"):'';
	$("#cardNo").val($("#cardNo").val().replace(/\s/g, ""));
	var b3=phonef($("#phone"),0);
	var b4=validf($("#yzm"),0);

	if( b2==""){
		
		$.ajax({
			url:rootPath+"/recharge/confirmRechargeValidateNoCard",
			type:"post",
			data:$("#rechargeForm").serialize(),
			error : function(data) {
				$("#loading").hide();
				var data = data.responseText;
				var _data = eval('('+data+')');
				$("#errorMsg").val(_data.errorMsg);
				//$("#errorTitle").text(error);
				$("#errorPrompt").text(_data.errorMsg);
				errorPopup.style.display='block';
				errorFade.style.display='block';
				backgd.style.overflow='hidden';
			},
			success:function(data){
				$("#loading").hide();
				var _data =  eval("("+data+")");
				if(!_data.isSuccess){
					if(_data.id=='redirect'){
						//跳转值错误页面
						$("#errorPrompt").text(_data.info);
						errorPopup.style.display='block';
						errorFade.style.display='block';
						backgd.style.overflow='hidden';
					}else{
						//校验错误
						if(typeof(_data.tocken)!="undefined"){
							$("#token").val(_data.tocken);
						}
						if(_data.id=='valid'){
							$("#alart").text(_data.info);
							alart.style.display="block";
							setTimeout("clean()",3000);
						}else{
							$("#errorPrompt").text(_data.info);
							errorPopup.style.display='block';
							errorFade.style.display='block';
							backgd.style.overflow='hidden';
						}
					}
				}else{
					var times = 5
					var interval = window.setInterval(function(){
						getResult(times);
						times = times - 1;
						if(times==0){
							clearTimeout(interval);
						}
					}, 2000);
				}
			}
		});

		// 提交操作-【结束】。
	}
});


//点击提交订单，去支付时（有卡）
$("#btn2").bind("click",function(){
	
	//var b4=validf($("#yzm"),0);
	$("#loading").show();
	setTimeout(function(){
		var b2 = moneyf($("#rechargeAmount"));
		$("#cardNo").val($("#cardNo").val().replace(/\s/g, ""));
		if(b2==""){
			$.ajax({
				url:rootPath+"/recharge/llRecharge",
				type:"post",
				data:$("#rechargeForm").serialize(),
				error : function(data) {
					var data = data.responseText;
					var _data = eval('('+data+')');
					$("#errorMsg").val(_data.errorMsg);
					//$("#errorTitle").text(error);
					$("#errorPrompt").text(_data.errorMsg);
					errorPopup.style.display='block';
					errorFade.style.display='block';
					backgd.style.overflow='hidden';
					$("#loading").hide();
				},
				success:function(data){
					var _data =  eval("("+data+")");
					if(!_data.isSuccess){
						$("#loading").hide();
						if(_data.id=='redirect'){
							$("#loading").hide();
							//跳转值错误页面
							//$("#errorMsg").val(_data.info);
							//$("#form_result").attr("action", rootPath + "/recharge/rechargeFailure").submit();
							$("#errorPrompt").text(_data.info);
							errorPopup.style.display='block';
							errorFade.style.display='block';
							backgd.style.overflow='hidden';
						}else{
							//校验错误
							if(typeof(_data.tocken)!="undefined"){
								$("#token").val(_data.tocken);
							}
							$("#loading").hide();
							//$("#"+_data.id).addClass("ipt-error").parent().siblings("em").html(_data.info).removeClass("hui");
							if(_data.id=='valid' || _data.id=='cardNo' || _data.id=='bankid'){
								$("#alart").text(_data.info);
								alart.style.display="block";
								setTimeout("clean()",3000);
							}else{
								$("#errorPrompt").text(_data.info);
								errorPopup.style.display='block';
								errorFade.style.display='block';
								backgd.style.overflow='hidden';
							}
							
						}
					}else{
						/*var times = 5
						var interval = window.setInterval(function(){
							getResult(times);
							times = times - 1;
							if(times==0){
								clearTimeout(interval);
							}
						}, 2000);*/
						$("#req_data").val(_data.info);
						$("#llRechargeForm").submit();
					}
				}
			});

			// 提交操作-【结束】。
			//document.getElementById("btn").disabled = false; 
		}else{
			$("#loading").hide();
		}
	},2000);
	
});

};
	

function getResult(times){
	$.post(rootPath + "/recharge/confirmRechargeIncome",function(data){
		if(data=='false'){//失败
			$("#loading").hide();
			//$("#form_result").attr("action", rootPath + "/recharge/rechargeFailure").submit();
			$("#errorPrompt").text("失败");
			$("#rechargeImg").attr('src',rootPath+'/images/iconwrong.png'); 
			errorPopup.style.display='block';
			errorFade.style.display='block';
			backgd.style.overflow='hidden';
		}
		if(data=='success'){//成功
			$("#loading").hide();
			//$("#form_result").attr("action", rootPath + "/recharge/rechargeSuccess").submit();
			$("#errorPrompt").text("成功");
			$("#rechargeImg").attr('src',rootPath+'/images/iconyes.png'); 
			errorPopup.style.display='block';
			errorFade.style.display='block';
			backgd.style.overflow='hidden';
		}
		if(data=='recharging'){//支付中
			if(times==1){
				//跳到错误页面
				$("#loading").hide();
				//$("#form_result").attr("action", rootPath + "/recharge/recharging").submit();
				$("#errorPrompt").text("充值中...");
				$("#rechargeImg").attr('src',rootPath+'/images/iconnot.png'); 
				errorPopup.style.display='block';
				errorFade.style.display='block';
				backgd.style.overflow='hidden';
			}
		}

	})

}




$('#test').load(function(){
    var res = $(this).contents().find('body').text();
    //console.log(res);
});


//充值页面的非空验证
function checkinput3(){
	var b1="";
	var b2="";
	
	if ($("#rechargeAmount").val() == '最少充值金额100.00元'){
		/*document.getElementById('btn2').disabled="true";
		document.getElementById('btn2').style.backgroundColor="#cacaca";
		document.getElementById('btn2').style.color="#989898";*/
	}else{
		b1 = moneyf($("#rechargeAmount"));
	}
	
	if($("#cardNo").is(":visible") && $("#cardNo").val() != ''){
		b2 = bankf($("#cardNo"),"1");
	}
	if(b1=="" && b2==""){
		/*document.getElementById('btn2').disabled="";
		document.getElementById('btn2').style.backgroundColor="#fe2a4d";
		document.getElementById('btn2').style.color="white";*/
	}
};

function checkSueccess(){
	bankf($("#cardNo"),"1");
}
function checkMoney(){
	var message="";
	var bankv=$("#cardNo");
	if(bankv.val()==""||bankv.val()=="请输入银行卡号"){
		$("#alart").text("请输入银行卡号");
		alart.style.display="block";
		setTimeout("clean()",3000);
		return;
	}
	if(bankv.val().replace(/\s/g, "").length<=19 && bankv.val().replace(/\s/g, "").length>=15){			
		message=checkCard(bankv.val());//校验卡号	
		if(message.indexOf("您已经绑过一张尾号")>=0){
			message="";
		}
	}else{
		massage="银行卡号格式错误！";
		$("#alart").text(massage);
		alart.style.display="block";
		setTimeout("clean()",3000);
	}
	if(message!=""){
		return;
	}
	message="";
	if(message==""){
		message=moneyf($("#rechargeAmount"));
		if(message==""){
			/*document.getElementById('btn2').disabled= "";
			document.getElementById('btn2').style.backgroundColor="#fe2a4d";
			document.getElementById('btn2').style.color="white";*/
		}
	}
	
}
//手机验证
//function phonef(phone,ph9){
//	var massage="";
//	if(phone.val()=="" )
//	{
//		if(ph9==0){
//			massage="手机号码格式错误";
//			$("#alart").text(massage);
//			alart.style.display="block";
//			setTimeout("clean()",3000);
//		}else{	
//			massage="";
//
//		}
//	}else
//	{
//		if (/(^1[3|4|5|7|8][0-9]{9}$)/.test(phone.val())) {
//			massage="";
//
//		} else {			
//			massage="手机格式错误";
//			$("#alart").text(massage);
//			alart.style.display="block";
//			setTimeout("clean()",3000);
//		}
//		
//	}
//	return massage;
//};
//验证码
//function validf(vali,va9){
//	if(vali.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
//	{
//		if(va9==0){
//			massage="请您输入验证码";
//			$("#alart").text(massage);
//			alart.style.display="block";
//			setTimeout("clean()",3000);
//		}else{
//			
//			massage="";
//
//		}
//	}else{	
//		if(vali.val().length==6 && /^([0-9.]+)$/.test(vali.val())){
//			massage="";		
//		}else{	
//			massage="验证码6位数字";
//			$("#alart").text(massage);
//			alart.style.display="block";
//			setTimeout("clean()",3000);
//		}
//	}
//	return massage;
//};
//借款金额输入验证
function moneyf(mond){
	var massage="";
	var mondint;
	if(mond.val()==""|| !/^([0-9.]+)$/.test(mond.val())){
		mondint=0;
		massage="请输入正确的充值金额";
		$("#alart").text(massage);
		alart.style.display="block";
		setTimeout("clean()",3000);
		/*document.getElementById('btn2').disabled="disabled";
		document.getElementById('btn2').style.backgroundColor="#cacaca";
		document.getElementById('btn2').style.color="#989898";*/
		return massage;
	}else{
		mondint=parseInt(mond.val());
	}

	if(mondint<100 ){
		massage="请输入大于100元的金额！";
		$("#alart").text(massage);
		alart.style.display="block";
		setTimeout("clean()",3000);
		/*document.getElementById('btn2').disabled="disabled";
		document.getElementById('btn2').style.backgroundColor="#cacaca";
		document.getElementById('btn2').style.color="#989898";*/
	}else{
			massage="";		

	}
	return massage;
}
//判断银行卡号是否正确
function bankf(bankv,pa){
	var massage="";
	var tip = $("#userBankName").html();
	if(bankv.val()=="" )//
	{
		if(pa=="0"){
			massage="";
		}else{
			massage="请您输入银行卡号！";
			$("#alart").text(massage);
			alart.style.display="block";
			setTimeout("clean()",3000);
		}
			
	}else{
		if(bankv.val().replace(/\s/g, "").length<=19 && bankv.val().replace(/\s/g, "").length>=15){			
			$.ajax({
				url:rootPath+'/bankcard/check_card',
				type:"post",
				data:{
					cardNo:bankv.val(),
				},
				success:function(data){
					console.log(data);
					if(data.result == 'success'){
						var money=$("#rechargeAmount");
						if(money!=""){
							massage="";
							massage=moneyf(money);
							if(massage==""){
								var money=$("#rechargeAmount");
								if(money!=""){
									massage=moneyf(money);
									if(massage==""){
										/*document.getElementById('btn2').disabled= "";
										document.getElementById('btn2').style.backgroundColor="#fe2a4d";
										document.getElementById('btn2').style.color="white";*/
									}
								}
							}
						}
					}else if(data.result == 'error'){
						$("#alart").text(data.errMsg);
						alart.style.display="block";
						setTimeout("clean()",3000);
						massage=data.errMsg;
						/*document.getElementById('btn2').disabled="disabled";
						document.getElementById('btn2').style.backgroundColor="#cacaca";
						document.getElementById('btn2').style.color="#989898";*/
					}else{
						$("#alart").text("网络异常，请稍后操作！");
						massage="网络异常，请稍后操作！";
						/*document.getElementById('btn2').disabled="disabled";
						document.getElementById('btn2').style.backgroundColor="#cacaca";
						document.getElementById('btn2').style.color="#989898";*/
					}
				}
			});
		}else{
			massage="银行卡号格式错误！";
			$("#alart").text(massage);
			alart.style.display="block";
			setTimeout("clean()",3000);
		}
		 
	}
	return massage;
}
function checkCard(cardNo){
	var massage="";
	$.ajax({
		url:rootPath+'/bankcard/check_card',
		type:"post",
		data:{
			cardNo:cardNo,
		},
		success:function(data){
			console.log(data);
			if(data.result == 'success'){
				
			}else if(data.result == 'error'){
				var isVerified=$("#isVerified").val();
				if(isVerified!="1"){
					$("#alart").text(data.errMsg);
					alart.style.display="block";
					setTimeout("clean()",3000);
					massage=data.errMsg;
				}
			}else{
				$("#alart").text("网络异常，请稍后操作！");
				massage="网络异常，请稍后操作！";
			}
			return massage;
		}
	});
	return massage;
}
//弹出协议
function alertpProtocol(){
    location.href=rootPath+"/bankList";
}
function offProtocol(){
    $("#masks").hide();
    $("body").css("overflow","auto");
}
function showProtocol(){
    $("#masks").show();
    $(".agreementss").show();
    $(".agreements").hide();
    $("body").css("overflow","hidden");
}