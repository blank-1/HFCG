// JavaScript Document
var userOriDesc ;
$(function(){
	
	//点击银行卡时效果
	$(".paybank span").click(function(){
		$(this).css({ "border":"1px solid #fe2a4d"}).children("i").show().parent().siblings("span").css({"border":"1px solid #e1e1e1"}).children("i").hide();
	});
	$("input[type=checkbox]").each(function() {
        $(this).attr("checked", false);
    });
	
	//银行卡输入框失去焦点时
	$("#bankid").blur(function(){
		
		bankf($(this),"0");
	});


	$("#okpayWithCard").click(function(){
		var type = $("input[name='bankPayChannel']:checked").val();
		//0--网银支付，1--快捷支付和其他支付
		var t1 = $("#vouchbig").html()
		if(type==0 && t1!='0.00'){
//			paymentOnline();
			payLLGateWay();
		}else {
			//易宝支付
			//hasCardPay();
			llpay();
		}
		
	});

	//连连支付
	function llpay(){
		var b1=passwordf($("#jypassword"),"1");
		var _true=true;
		var flag = true;//标识有无银行卡支付
		//开始判断
		if(accipt_ched()){
			//有银行卡 支付
			(b1=="")?_true=true:_true=false;
			if(_true){
				//确定账户支付金额
				if($("#cb_account").attr('checked') =='checked'){
					$("#accountPayValue").val($("#accm").html());
				} else {
					$("#accountPayValue").val(0);
				}
				//确定财富券金额 todo
				var userVoucherIds = "";
				$(".voucher .red em").each(function() {
					userVoucherIds+=$(this).html()+",";
				});
				$("#userVoucherId").val(userVoucherIds);
				//确定第三方充值金额
				if ($("#cb_recharge").get(0).checked) {
					$("#rechargePayValue").val(rmoney($("#bankmyid").html()));
				} else {
					$("#rechargePayValue").val(0);
				}
				//先进行交易密码的验证，通过后再提交请求
				$.ajax({
					url:rootPath+"/recharge/llpay",
					type:"post",
					//async:true,
					data:$("#f1").serialize(),
					error : function(data,e,a) {
						var data = data.responseText;
						var _data = eval('('+data+')');
						$("#errorMsg").val(_data.errorMsg);
						$("#form_result").attr("action", rootPath + "/recharge/payError").submit();
					},
					success:function(data){
						var _data =  eval("("+data+")");
						if(!_data.isSuccess){
							if(_data.id=='redirect'){
								//跳转值错误页面
								$("#errorMsg").val(_data.info);
								$("#form_result").attr("action", rootPath + "/recharge/payError").submit();
							}else{
								//校验错误
								if(typeof(_data.tocken)!="undefined"){
									$("#token").val(_data.tocken);
								}
								$(".zhezhao5").hide();
								var node = $("#"+_data.id);
								if(node.length>0){
									node.addClass("ipt-error").parent().siblings("em").html(_data.info).removeClass("hui");
								}else{
									$("#errorMsg").html(_data.info);
									$("#lendOrderId").val("");
									$("#with_falure").click();
								}
							}
						}else{
							$("#rechargeCode").val(_data.rechargeCode);
							$("#payshowstate").slideDown(500);
							$(".zhezhao1").show();
							//window.open(_data.info);

							//ie判断
							var referForm=document.createElement("form");
							referForm.action= _data.info;
							referForm.method="post";
							referForm.target="_blank";
							document.body.appendChild(referForm);
							referForm.submit();

						}
					}
				});
			}
		}else{
			//无银行卡，账户余额、财富券
			if (b1=="") {
				$.ajax({//校验交易密码
					url:rootPath+"/finance/checkBidLoanByAccountBalance",
					type:"post",
					data:{"bidPass":$("#jypassword").val()},
					error : function(data) {
						var data = data.responseText;
						var _data = eval('('+data+')');
						$("#errorMsg").val(_data.errorMsg);
						$("#form_result").attr("action", rootPath + "/recharge/payError").submit();
					},
					success:function(data){
						var _data =  eval("("+data+")");
						if(!_data.isSuccess){
							result = false;
							$("#"+_data.id).addClass("ipt-error").parent().siblings("em").html(_data.info);
						}else{
							//余额、财富券支付
							//确定账户支付金额
							if ($("#cb_account").attr('checked') =='checked') {
								$("#accountPayValue").val($("#shijiyue").html());
							} else {
								$("#accountPayValue").val(0);
							}
							//确定财富券金额 todo
							var userVoucherIds = "";
							$(".voucher .red em").each(function() {
								userVoucherIds+=$(this).html()+",";
							});
							$("#userVoucherId").val(userVoucherIds);
							$("#f1").attr("action", rootPath + "/finance/buyFinanceByAccountAmount").submit();

						}
					}
				});
			}
		}
	}

	function getResult(times){

		$.post(rootPath + "/recharge/confirmRecharge",function(data){
			if(data=='false'){//失败
				$(".zhezhao5").hide();
				$("#form_result").attr("action", rootPath + "/recharge/payError").submit();
			}
			if(data=='success'){//成功
				$(".zhezhao5").hide();
				$("#form_result").attr("action", rootPath + "/recharge/paySuccess").submit();
			}
			if(data=='recharging'){//支付中
				if(times==1){
					//跳到错误页面
					$(".zhezhao5").hide();
					$("#form_result").attr("action", rootPath + "/recharge/paying").submit();
				}
			}

		})

	}
	

	//判断是否需要判断验证码手机号银行卡
	function accipt_ched(){
		if(rmoney($("#vouchbig").html())==0){
			return false;
		}else{
			return true;	
		}
		
	}

	//网银支付
	function paymentOnline(){
		var b1=passwordf($("#jypassword"),"1");
		var _true=true;
		var flag = true;//标识有无银行卡支付
		//开始判断
		(b1=="")?_true=true:_true=false;
		if(_true){
			//确定账户支付金额
			if($("#cb_account").attr('checked') =='checked'){
				$("#accountPayValue").val($("#accm").html());
			} else {
				$("#accountPayValue").val(0);
			}
			//确定财富券金额 todo
			var userVoucherIds = "";
			$("#ticket-group .red .voucherId").each(function() {
				userVoucherIds+=$(this).html()+",";
			});
			$("#userVoucherId").val(userVoucherIds);
			//确定第三方充值金额
			if ($("#cb_recharge").get(0).checked) {
				$("#rechargePayValue").val(rmoney($("#bankmyid").html()));
			} else {
				$("#rechargePayValue").val(0);
			}
			//先进行交易密码的验证，通过后再提交请求
			$.ajax({
				url:rootPath+"/payment/payOnline",
				type:"post",
				//async:true,
				data:$("#f1").serialize(),
				error : function(data,e,a) {
					var data = data.responseText;
					var _data = eval('('+data+')');
					$("#errorMsg").val(_data.errorMsg);
					$("#form_result").attr("action", rootPath + "/recharge/payError").submit();
				},
				success:function(data){
					var _data =  eval("("+data+")");
					if(!_data.isSuccess){
						if(_data.id=='redirect'){
							//跳转值错误页面
							$("#errorMsg").val(_data.info);
							$("#form_result").attr("action", rootPath + "/recharge/payError").submit();
						}else{
							//校验错误

							if(typeof(_data.tocken)!="undefined"){

								$("#token").val(_data.tocken);
							}
							$(".zhezhao5").hide();
							$("#"+_data.id).addClass("ipt-error").parent().siblings("em").html(_data.info).removeClass("hui");
						}
					}else{
						$("#rechargeCode").val(_data.rechargeCode);
						paymentMask();
						window.open(_data.info);
					}
				}
			});
		}
	}
	
	//网银支付
	function payLLGateWay(){
		var b1=passwordf($("#jypassword"),"1");
		var _true=true;
		var flag = true;//标识有无银行卡支付
		//开始判断
		(b1=="")?_true=true:_true=false;
		if(_true){
			//确定账户支付金额
			if($("#cb_account").attr('checked') =='checked'){
				$("#accountPayValue").val($("#accm").html());
			} else {
				$("#accountPayValue").val(0);
			}
			//确定财富券金额 todo
			var userVoucherIds = "";
			$("#ticket-group .red .voucherId").each(function() {
				userVoucherIds+=$(this).html()+",";
			});
			$("#userVoucherId").val(userVoucherIds);
			//确定第三方充值金额
			if ($("#cb_recharge").get(0).checked) {
				$("#rechargePayValue").val(rmoney($("#bankmyid").html()));
			} else {
				$("#rechargePayValue").val(0);
			}
			//先进行交易密码的验证，通过后再提交请求
			$.ajax({
				url:rootPath+"/recharge/llGatewayPay",
				type:"post",
				//async:true,
				data:$("#f1").serialize(),
				error : function(data,e,a) {
					var data = data.responseText;
					var _data = eval('('+data+')');
					$("#errorMsg").val(_data.errorMsg);
					$("#form_result").attr("action", rootPath + "/recharge/payError").submit();
				},
				success:function(data){
					var _data =  eval("("+data+")");
					if(!_data.isSuccess){
						if(_data.id=='redirect'){
							//跳转值错误页面
							$("#errorMsg").val(_data.info);
							$("#form_result").attr("action", rootPath + "/recharge/payError").submit();
						}else{
							//校验错误

							if(typeof(_data.tocken)!="undefined"){

								$("#token").val(_data.tocken);
							}
							$(".zhezhao5").hide();
							$("#"+_data.id).addClass("ipt-error").parent().siblings("em").html(_data.info).removeClass("hui");
						}
					}else{
						$("#rechargeCode").val(_data.rechargeCode);
						paymentMask();
//						window.open(_data.info);
						//ie判断
						var referForm=document.createElement("form");
						referForm.action= _data.info;
						referForm.method="post";
						referForm.target="_blank";
						document.body.appendChild(referForm);
						referForm.submit();
					}
				}
			});
		}
	}
	
	
	$(".payresult").click(function(){
		$("#pay_result").attr("action",rootPath+"/recharge/getOrderResult");
		$("#pay_result").submit();
	});

	$("#llPaySuccess,#llPayQuestion").click(function(){
		$("#pay_result").attr("action",rootPath+"/recharge/getOrderResult");
		$("#pay_result").submit();
	});
	

	
	//点击确认支付
	$("#okpay").click(function(){
		
		var profitReturnConfig = $(".yixuan").attr("data");//选中的收益分配方式
		var lendOrderId = $("#lendOrderId").val();
		
		//省心计划收益分配方式(开始)
		$.ajax({
			url:rootPath+"/finance/profitReturnConfig",
			type:"post",
			data:{
				"lendOrderId":lendOrderId,
				"profitReturnConfig":profitReturnConfig
			},
			error : function(data,e,a) {
				$("#form_result").attr("action", rootPath + "/recharge/payError").submit();
			},
			success:function(data){
				var _data = eval("("+data+")");
				if(!_data.isSuccess){
					$("#errorMsg").val(_data.info);
					$("#form_result").attr("action", rootPath + "/recharge/payError").submit();
				}else{
					//支付（开始）
					var type = $(".s_show input[name='bankPayChannel']").val();
					//0--网银支付，1--快捷支付和其他支付
					var t1 = $("#vouchbig").html();
					if(type==0 && t1!='0.00'){
						payLLGateWay();
					}else {
						llpay();
					}
					//支付（结束）
				}
			}
		});
		//省心计划收益分配方式(结束)

	});
	
	//判断交易密码
	$("#jypassword").blur(function(){
		passwordf($(this),"0");
	});
	
	$("#phone").change(function(){
		if (/^([0-9.]+)$/.test($(this).val())) {
				
			massage="";
			$(this).removeClass("ipt-error").parent().siblings("em").html(massage).html(massage);
		} else {
			massage="手机号码应为 11 位数字";
			$(this).addClass("ipt-error").parent().siblings("em").html(massage);
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
	//手机号码输入验证
	$("#phone").blur(function(){
		
		phonef($(this),1); 
	});
	//验证码输入验证
	$("#valid").blur(function(){
		
		validf($(this),1);
	});
	//手机验证
	function phonef(phone,ph2){
		
		if(phone.val()=="" )
		{
			if(ph2==0){
				massage="请输入手机号码！";
			phone.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}else{
				
				massage="";
				phone.removeClass("ipt-error").parent().siblings("em").html(massage).addClass("hui");
			}
		}else
		{
			if (/(^1[3|4|5|7|8][0-9]{9}$)/.test(phone.val())) {
				massage="";
				phone.removeClass("ipt-error").parent().siblings("em").html(massage).html(massage).addClass("hui");
			} else {
				
				massage="手机格式错误";
				phone.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}
			
		}
		return massage;
	}
	//验证码
	function validf(vali,va2){
		if(vali.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if(va2==0){
				massage="请您输入验证码";
				vali.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}else{
				
				massage="";
				vali.removeClass("ipt-error").parent().siblings("em").html(massage).addClass("hui");
			}
		}else{
			
			if(vali.val().length==6 && /^([0-9.]+)$/.test(vali.val())){
				
				massage="";
				vali.removeClass("ipt-error").parent().siblings("em").html(massage).addClass("hui");
				
			}else{
				
				massage="验证码6位数字";
				vali.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			}
		}
		return massage;
	}

	//余额购买理财
	//余额投标

	//隐藏正在等待
	function HiddenDiv(){
		$("#bankshow").html('');
	}
	//显示正在等待
	function ShowDiv(){
		$("#bankshow").html('<img src="../images/loading220.gif" />');
	}
	//倒计时
	$("#getvalid").click(function(){
		b3=0;
		var url;
		var data;
		if ($("#payType").val() == "1") {//有卡
			timer($(this));

			//获取支付验证码
			//确定账户支付金额
			if ($("#cb_account").attr('checked') =='checked') {
				$("#accountPayValue").val($("#accm").html());
			} else {
				$("#accountPayValue").val(0);
			}
			//确定财富券金额 todo
			var userVoucherIds = "";
			$("#ticket-group .red .voucherId").each(function() {
				userVoucherIds+=$(this).html()+",";
			});
			$("#userVoucherId").val(userVoucherIds);
			//确定第三方充值金额
			var rechargeBox = $("#cb_recharge").get(0);
			if (rechargeBox.checked) {
				$("#rechargePayValue").val($("#bankmyid").html());
			} else {
				$("#rechargePayValue").val(0);
			}
			url = rootPath + "/recharge/invokePay";
			data = {
				lendOrderId: $("#lendOrderId").val(),
				cardId: $("#cardId").val(),
				accountPayValue: $("#accountPayValue").val(),
				userVoucherId: $("#userVoucherId").val(),
				rechargePayValue: $("#rechargePayValue").val()
			}
		}else{//无卡
			if(phonef($("#phone"),0)==""&&bankf($("#bankid"),"1")=="") {
				timer($(this));
				//获取绑卡验证码
				url = rootPath+"/recharge/invokeBindCard";
				data = {
					cardNo : $("#bankid").val(),
					phone : $("#phone").val()
				};
			}
		}
		if(url!=''){
			//console.log(data);
			$.ajax({//校验交易密码
				url:url,
				type:"post",
				data:data,
				error : function(data) {
					var data = data.responseText;
					var _data = eval('('+data+')');
					$("#errorMsg").val(_data.errorMsg);
					$("#form_result").attr("action", rootPath + "/recharge/payError").submit();
				},
				success:function(data1){
					var dd = eval('('+data1+')');
					if(dd.id=='redirect'){
						//跳转值错误页面
						$("#errorMsg").val(dd.info);
						$("#form_result").attr("action", rootPath + "/recharge/payError").submit();
					}else if (!dd.isSuccess) {
						$("#"+dd.id).addClass("ipt-error").parent().siblings("em").html(dd.info).removeClass("hui");
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

//校验卡号(根据银行卡号获取所属银行名称)
	function checkCard(cardNo){
		$.ajax({
			url:rootPath+'/bankcard/check_card',
			type:"post",
			data:{
				cardNo:cardNo
			},
			beforeSend: function () {
				ShowDiv();
			},
			error : function(XHR) {
				var res = eval("(" + XHR.responseText + ")");
				if(res.errorCode==4){
					window.location.href  = rootPath+"/user/to_login";
				}
			},
			success:function(data){
				HiddenDiv();
				if(data.result == 'success'){
					$("#bankshow").html('<img src="../images/pay_04.png" class="buttonimgdetail" /><font id="bankshow_name">'+data.data.bankname+'</font>');
					$("#userBankName").html(userOriDesc);
				}else if(data.result == 'error'){
					if(data.errCode == 'check'){
						$("#bankid").addClass("ipt-error").parent().siblings("em").html(data.errMsg).removeClass("hui");
					}else{
						$("#bankid").addClass("ipt-error").parent().siblings("em").html(data.errMsg).removeClass("hui");
					}
				}else{
					$("#bankid").addClass("ipt-error").parent().siblings("em").html("网络异常，请稍后操作！").removeClass("hui");
				}

			}
		});

	}


	//判断银行卡号是否正确
	function bankf(bankv,pa){
		var userDesc = $("#userBankName").html();
		if(bankv.val()=="" )//
		{
			if(pa=="0"){
				massage="";
				bankv.removeClass("ipt-error").parent().siblings("em").html(userDesc).addClass("hui");
				$("#bankshow").hide();

			}else{
				massage="请您输入银行卡号！";
				bankv.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");

				$("#bankshow").hide();
			}

		}else{

			if(bankv.val().replace(/\s/g, "").length<=19 && bankv.val().replace(/\s/g, "").length>=15){

				checkCard(bankv.val());//校验卡号

				massage="";
				bankv.removeClass("ipt-error").parent().siblings("em").html(userDesc).addClass("hui");
				$("#bankshow").show();
			}else{
				massage="银行卡号格式错误！";
				bankv.removeClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");

				$("#bankshow").hide();
			}
		}
		return massage;
	}
	
	//判断交易密码
	function passwordf(passval1,pa){

		var tip = $("#bid_pass").html();
		if(passval1.val()=="" )//只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if(pa=="0"){
				massage="";
				passval1.removeClass("ipt-error").parent().siblings("em").html(tip).addClass("hui");

			}else{
				massage="请您输入交易密码！";
				passval1.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");

			}
		}else{

			if(passval1.val().length>=6 && passval1.val().length<=16 && /^[0-9a-zA-Z]+$/.test(passval1.val())){
				massage="";
				passval1.removeClass("ipt-error").parent().siblings("em").html(tip).addClass("hui");
			}else{
				massage="交易密码错误";
				passval1.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");;
			}

		}
		return massage;
	}

	$("#bankpay").find("input").prop("checked",true);
	//点击账户支付时
	$("#accountpay").click(function(){
		editUse($(this));
		var allmoney=rmoney($("#buyBalance").html());//产品总金额
		var accm=rmoney($("#accm").html());//怅户余额
		var vouchbig=$("#vouchbig").html();//银行卡支付金额
		var djqaccount=0;//财富券
		$("#ticket-group .red .ticket_s_m").each(function() {
			djqaccount+=parseInt($(this).html());
		});
//		console.log("allmoney,accm,vouchbig,"+allmoney+","+ accm+","+vouchbig);
		var bank=0;
		if (ifUse($(this))) {
			$("#shijiyue").html(fmoney(Math.min(allmoney,accm),2));
			if(ifUse($("#voucherp"))){//判断是否可用财富券
				//如果小于账户余额
				if(allmoney-djqaccount<accm){
					$("#shijiyue").html(fmoney((allmoney-djqaccount),2));
				}else{
					$("#shijiyue").html(fmoney($("#accm").html(),2));
				}
				bank=(fmoney(allmoney-djqaccount-accm,2));
			}else{
				bank=(fmoney(allmoney-accm,2));
			}
		}else{
			$("#shijiyue").html("0.00");
			if(ifUse($("#voucherp"))){//判断是否可用财富券
				bank=(fmoney(allmoney-djqaccount,2));
			}else{
				bank=(fmoney(allmoney,2));
			}
		}
		//判断是否需要银行卡支付
		if(rmoney(bank)>=0){
			$("#vouchbig").html(bank);
			$("#bankmyid").html(bank);
		}else{
			$("#vouchbig").html("0.00");
			$("#bankmyid").html("0.00");
		}
		//判断是否该隐藏标杯
		hide_show_bank_pay();
	});
	
	


	//设置财富券点击金额
	var vouchipt=0;
	var djq=0;
	//点击财富券时效果
	$(".voucher>div.red,.voucher>div.writes").click(function(){

		if($(this).hasClass("red")){
			$(this).removeClass("red").addClass("writes");
		}else{
			$(this).removeClass("writes").addClass("red");
		}
		$(".voucher>div.red.repeat").removeClass("red").addClass("writes");
		/*$(this).removeClass("writes").addClass("red").siblings(".voucher>div.red,.voucher>div.writes").addClass("writes").removeClass("red");
		 $("#vouchersp").html(fmoney(parseInt($(".voucher .red big").html()),2));

		 */
		var vouch_money=0;
		$(".voucher .red big").each(function() {
			vouch_money+=parseInt($(this).html());
		});

		$("#vouchersp").html(fmoney(vouch_money,2));

		//判断财富券让不让
		if(vouse_ipt($(this),vouch_money)){

			bankpersonMoney(vouch_money);
			ishidden();
		}

	});
	//判断财富券让不让用
	function vouse_ipt(ipt,vouch_money){
		if( vouch_money >(rmoney($(".payright").find("big").html()))){
			//alert("您所选的财富券大于可支付余额");
			myAlert('提示','您所选的财富券大于可支付余额','false');
			ipt.removeClass("red").addClass("writes");
			$("#vouchersp").html(fmoney(vouch_money-(rmoney(ipt.find("big").html())),2));
			return false;
		}else{
			rmoney(ipt.find("big").html());
			return true;
		}

	}
	//点击财富券时效果
	$(".voucher>div.red.repeat,.voucher>div.writes.repeat").click(function(){

		$(this).removeClass("writes").addClass("red").siblings(".voucher>div.red,.voucher>div.writes").addClass("writes").removeClass("red");
		var vouch_money=fmoney(parseInt($(".voucher .red big").html()),2);
		$("#vouchersp").html(vouch_money);


		bankpersonMoney(vouch_money);
		ishidden();

	});
	//银行卡支付和账户余额应付金额
	function bankpersonMoney(cach){
		var allmoney=rmoney($("#buyBalance").html());//产品总金额
		var djqaccount=cach;//财富券
		var accm=rmoney($("#accm").html());//怅户余额
		var vouchbig=$("#vouchbig");//银行卡支付金额
		var bank=0;//临时金额
		if(ifUse($("#accountpay"))){

			//如果小于账户余额
			//if(allmoney-djqaccount<accm){

			//	$("#accountpay .pright big").html(fmoney((allmoney-djqaccount),2));

			//}else{

			//	$("#accountpay .pright big").html(fmoney(($("#accm").html()),2));
			//}
			//如果小于账户余额
			if(allmoney-djqaccount<accm){
				$("#shijiyue").html(fmoney((allmoney-djqaccount),2))
			}else{

				$("#shijiyue").html(fmoney(($("#accm").html()),2));
			}
			bank=allmoney-djqaccount-accm;
			if(bank<0){
				bank=0;
			}
			//选中账户支付

		}else{

			//如果小于账户余额
			//if(allmoney-djqaccount<accm){

			//	$("#accountpay .pright big").html(allmoney-djqaccount);
			//}else{

			//}
			$("#shijiyue").html("0.00");
			bank=allmoney-djqaccount;

		}
		$("#vouchbig").html(fmoney(bank,2));
		$("#bankmyid").html(fmoney(bank,2));
	}
	function ishidden(){

		//判断是否该隐藏标杯
		if($("#vouchbig").html()=='0.00'){
			//$("#bankpay").find("input").prop("checked",false);
			//$("#bankpay").find(".pright").hide();
			ifUse($("#card_pay_span"));
			$("#bankpay").next().slideUp(300)
		}else{

			if(!$("#bankpayd").is(":hidden")){

				$("#bankpay").find("input").prop("checked",true);
				$("#bankpay").find(".pright").show();
				$("#bankpay").next().slideDown(300)
			}
		}
	}
	$("#voucherp").click(function(){
		
		var allmoney=rmoney($("#buyBalance").html());//产品总金额
		var djqaccount=rmoney($("#ticket-group .red .ticket_s_m").html()?$("#ticket-group .red .ticket_s_m").html():'0.00');//财富券
		var accm=rmoney($("#accm").html());//怅户余额
		var vouchbig=$("#vouchbig");//银行卡支付金额
		
		var bank=$(".ticket_s_m").html();
		
		$("#vouchersp").html(fmoney(djqaccount,2));
		if(ifUse($(this))){
			
			if(ifUse($("#accountpay"))){
				
				bank=allmoney-djqaccount-accm>0 ? allmoney-djqaccount-accm : 0;//计算银行卡需支付金额
				//计算帐户余额
				accm=allmoney-djqaccount>0 ? Math.min(allmoney-djqaccount,accm):0;
				
			}else{
				
				bank=allmoney-djqaccount>0 ? allmoney-djqaccount : 0;//计算银行卡需支付金额
				accm=0;
			
			}
			$(this).parent().siblings(".ticket-group").slideDown(300,function(){bottomB();});
		}else{
			$("#ticket-group .red").addClass("writes").removeClass("red");
			if(ifUse($("#accountpay"))){
				bank=allmoney-accm>0 ? allmoney-accm : 0;//计算银行卡需支付金额
				//计算帐户余额
				accm= Math.min(allmoney,accm);
			}else{
				bank=allmoney;//计算银行卡需支付金额
				accm=0;
			}
			$("#vouchersp").html("0.00");
			$(this).parent().siblings(".ticket-group").slideUp(300,function(){bottomB();});
			
		}
		$("#vouchbig").html(fmoney(bank,2));
		$("#bankmyid").html(fmoney(bank,2));
		
		$("#shijiyue").html(fmoney(accm,2));
		
		//判断是否该隐藏标杯
		hide_show_bank_pay();
		
	});
	
});

$(document).ready(function() {
    if(rmoney($("#accm").html())<=0){
		
		$("#accountpay input").attr("disabled",true);
	}else{
		$("#accountpay input").attr("disabled",false);
	}

	//默认触发银行卡是焦点事件
	$("#bankid").blur();
	formatBankNo($("#bankid").get(0));
});


function myAlert(title,msg,error){
	var malert = $("#myAlert").html();
	if(malert==null){
		$("body").append('<div id="myAlert" style="text-align:center;"><a id="with_success_111" style="display: none;" href="javascript:" data-mask="mask" data-id="with" >&nbsp</a></div>' +
			'<div class="masklayer" id="with"><h2 class="clearFloat"><span>'+title+'</span><a id="close_withdraw"  href="javascript:alertHide()" data-id="close"></a></h2> <div class="shenf_yanz_main"> ' +
			'<div style="height:70px;clear:both;"></div><p><img src="'+rootPath+'/images/img/'+error+'.jpg" /><span>'+msg+'</span></p> ' +
			'<div style="height:50px;clear:both;"></div> ' +
			'<div class="input_box_shenf">' +
			'<a href="javascript:" data-id="close" id="queren_withdraw"><button>确认</button></a>' +
			'<div style="height:90px;clear:both;"></div> </div> </div> </div>');
	}
	$("#with_success_111").click();
}


$(function(){
	//快捷支付和网银支付的切换
	//各个银行卡切换
	$(".internateBank span").click(function(){
		$(this).addClass("choose").siblings("span").removeClass("choose");
		$(this).addClass("choose");
		$(this).parent().siblings().find("span").removeClass("choose");
		$(this).parent().after($(".internateTable")[0]);
		var PHtml='';
		PHtml=LLBankInfo[$(this).attr("id")].tableInfo;
		$(".internateTable").html(PHtml);
		setBankCode($(this));
	});

	//银行卡（网银支付 和 快捷支付）
	$(".bank_pay_term h2").click(function(){
		$(this).addClass("s_show").removeClass("s_hide");
		$(this).siblings(".shower").slideDown();
		$(this).find("input").attr("checked",true);
		$(this).parent().siblings().find("h2").removeClass("s_show").addClass("s_hide");
		$(this).parent().siblings().find(".shower").slideUp();
		$(this).parent().siblings().find("h2>input").attr("checked",false);
	});

	var yi=1;
    $(".inter_more").click(function(){
    	if(yi==1){
    		$(".cange").slideDown(500);
    		$(".inter_more").html("收起");
    		yi=0;
    	}else{
    		$(".cange").slideUp(500);
    		$(".inter_more").html("展开更多>>");
    		yi=1;
    	}
    });
	
	$(".internateBank span").each(function(){
		if($(this).hasClass("choose")){
			setBankCode($(this));
		}
	});
	
    
  
    
    //$(".inter-group").find(".choose").find("img").click();
    
});

function setBankCode(obj){
	$("#bkcode").val(obj.attr("code"));
}

function paymentMask(){
	if($("#rdi-quick").is(":checked") || $("#rdi-inter").is(":checked")){
		$(".zhezhao5").show();
		$(".zhezhao5").css("height",($("body").height() + $(".footer").height()));
		$(".heimask").css("top",document.documentElement.clientHeight/2-(parseInt($(".heimask").height())/2)+'px');
		$("#interdvi1").slideDown(500);
	}
}


//var flag = 0;
//function start(){
//	var text = document.getElementById("bindcard");
//	if(text==null)
//		return;
//	if (!flag)
//	{
//		text.style.boxShadow = "0px 0px 8px #ff7088";
//		flag = 1;
//	}else{
//		text.style.boxShadow = "none";
//		flag = 0;
//	}
//	setTimeout("start()",1000);
//}

/**
 * 判断是否隐藏银行卡支付div框
 */
function hide_show_bank_pay(){
	if ($("#vouchbig").html() == '0.00') {
		$("#card_pay_span").addClass("ptt_Choose_not").removeClass("ptt_Choose");
		$("#card_pay_span").children("input").attr("checked", false);
		$("#bankpay").next().slideUp(300)
	} else {
		$("#card_pay_span").removeClass("ptt_Choose_not").addClass("ptt_Choose");
		$("#card_pay_span").children("input").attr("checked", true);
		if (!$("#bankpayd").is(":hidden")) {
			$("#bankpay").find("input").prop("checked", true);
			$("#bankpay").next().slideDown(300)
		}
	}
}

function ishidden(){

	//判断是否该隐藏标杯
	if($("#vouchbig").html()=='0.00'){
		ifUse($("#card_pay_span"));
		$("#bankpay").next().slideUp(300)
	}else{

		if(!$("#bankpayd").is(":hidden")){

			$("#bankpay").find("input").prop("checked",true);
			$("#bankpay").find(".pright").show();
			$("#bankpay").next().slideDown(300)
		}
	}
}

/**
 * 财富卷是否选中
 */
function ifUse(_pay){
	if(_pay.hasClass("ptt_Choose_not")){
		return false;
	}else{
		return true;
	}
}


/**
 * 更改财富卷选中状态
 */
function editUse(_pay){
	if(_pay.hasClass("ptt_Choose_not")){
		_pay.removeClass("ptt_Choose_not").addClass("ptt_Choose");
		_pay.children("input").attr("checked", true);
	}else{
		_pay.addClass("ptt_Choose_not").removeClass("ptt_Choose");
		_pay.children("input").attr("checked", false);
	}
}

$(function(){
	//省心计划收益分配方式按钮切换
	$(".shengXinpay span").click(function(){
		$(this).addClass("yixuan").siblings("span").removeClass("yixuan");
		if($(".yixuan").html().length == 6){
			
			$(".shengxinTips").html("<i style='color:#FF5655;vertical-align:middle;'>*</i> 回款利息收益将会在省心期内自动循环投资，收益更高");
		}else{
			
			$(".shengxinTips").html("<i style='color:#FF5655;vertical-align:middle;'>*</i> 回款利息收益将进入可用余额，资金灵活");
		}
	})
})
