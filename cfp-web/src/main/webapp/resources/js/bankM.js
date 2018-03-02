// JavaScript Document
$(function(){
	
	
	//银行卡输入框失去焦点时
	$("#bankid").blur(function(){
		
		bankf($(this),"0");
	});
	
	//点击提交订单，去绑卡时
	$("#recharge").click(function(){
		
		var b2=$("#bankid").is(":visible")?bankf($("#bankid"),"1"):'';

		//借款金额输入验证
		var b3 = moneyf($("#moneyp"));
		if( b2==""&&b3){
			
			// 提交操作-【开始】。
			var bankid = $("#bankid").val();//银行卡号

			$.ajax({
				url:rootPath+'/bankcard/save_bankcard_add',
				type:"post",
				data:{
					bankid:bankid,
					amount:$("#moneyp").val()
				},
				error : function(data) {
					var data = data.responseText;
					var _data = eval('('+data+')');
					$("#errorMsg").val(_data.errorMsg);
					$("#form_result").attr("action", rootPath + "/recharge/rechargeFailure").submit();
				},
				success:function(data){
					var _data =  eval("("+data+")");
					if(!_data.isSuccess){
						if(_data.id=='redirect'){
							//跳转值错误页面
							$("#errorMsg").val(_data.info);
							$("#form_result").attr("action", rootPath + "/recharge/rechargeFailure").submit();
						}else{
							//校验错误
							if(typeof(_data.tocken)!="undefined"){
								$("#token").val(_data.tocken);
							}
							//$(".zhezhao5").hide();
							$("#"+_data.id).addClass("ipt-error").parent().siblings("em").html(_data.info).removeClass("hui");
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



	});

	$("#llPaySuccess,#llPayQuestion").click(function(){
		$("#recharge_result").attr("action",rootPath+"/recharge/getRechargeResult");
		$("#recharge_result").submit();
	});


	//判断银行卡号是否正确
	function bankf(bankv,pa){
		
		if(bankv.val()=="" )//
		{
			if(pa=="0"){
				massage="";
				bankv.removeClass("ipt-error").parent().siblings("em").html('').addClass("hui");
				$("#bankshow").hide();
				
			}else{
				massage="请您输入银行卡号！";
				bankv.removeClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
				
				$("#bankshow").hide();
			}
				
		}else{
			if(bankv.val().replace(/\s/g, "").length<=19 && bankv.val().replace(/\s/g, "").length>=15){

					checkCard(bankv.val());//校验卡号
				
				massage="";
				bankv.removeClass("ipt-error").parent().siblings("em").html('').addClass("hui");
				$("#bankshow").show();
			}else{
				massage="银行卡号格式错误！";
				bankv.removeClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
				
				$("#bankshow").hide();
			}
			 
		}
		return massage;
	}
	
	// 字符替换（原始文本，替换目标，替换结果）
	function ReplaceAll(strAll, strp_old, strp_new){
		while (strAll.indexOf(strp_old) >= 0){
			strAll = strAll.replace(strp_old, strp_new);
		}
		return strAll;
	}



	//隐藏正在等待
	function HiddenDiv(){
		$("#bankshow").html('');
	}
	//显示正在等待
	function ShowDiv(){
		$("#bankshow").html('<img src="../images/loading220.gif" />');
	}
	//校验卡号(根据银行卡号获取所属银行名称)
	function checkCard(cardNo){
		$.ajax({
			url:rootPath+'/bankcard/check_card',
			type:"post",
			data:{
				cardNo:cardNo,
			},
			beforeSend: function () {
				ShowDiv();
			},
			success:function(data){
				HiddenDiv();
				if(data.result == 'success'){
					$("#bankshow").html('<img src="../images/pay_04.png" class="buttonimgdetail" /><font id="bankshow_name">'+data.data.bankname+'</font>');
					//$("#bankshow_name").html(data.data.bankname);
				}else if(data.result == 'error'){
					if(data.errCode == 'check'){
						$("#bankid").removeClass("ipt-error").parent().siblings("em").html(data.errMsg).removeClass("hui");
					}else{
						$("#bankid").removeClass("ipt-error").parent().siblings("em").html(data.errMsg).removeClass("hui");
					}
				}else{
					$("#bankid").removeClass("ipt-error").parent().siblings("em").html("网络异常，请稍后操作！").removeClass("hui");
				}
			}
		});
	}
	
	//金额输入验证
	$("#moneyp").blur(function(){
		if(!$(this).val()==""){
			moneyf($(this));	
			
		}else{
			massage="";
			$(this).removeClass("ipt-error").parent().siblings("em").html('*充值最小金额不得小于<font color="#fe2a4d">0.01</font>元').addClass("hui");;
		}
	});
	
		//借款金额输入验证
	function moneyf(mond){
		
		var mondint;
		if(mond.val()==""|| !/^([0-9.]+)$/.test(mond.val())){
			mondint=0;
			massage="请输入正确的充值金额";
			mond.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			
			return false;
		}else{
			mondint=parseFloat(mond.val());
		}
		if(mondint<0.01 ){
			massage="请输入大于0.01元的金额！";
			mond.addClass("ipt-error").parent().siblings("em").html(massage).removeClass("hui");
			return false;
		}else{
				massage="";
				
				mond.removeClass("ipt-error").parent().siblings("em").html('*充值最小金额不得小于<font color="#fe2a4d">0.01</font>元').addClass("hui");
			
		}
		return true;
	}


})

//弹出提示框
function showAlert(title,content,type){
	var img_src = "../images/img/true.jpg";
	if(type == 'error'){
		img_src = "../images/img/false.jpg";
	}
	$("#bankcard_add_alert_h").html(title);
	$("#bankcard_add_alert_p").html(content);
	$("#bankcard_add_alert_img").attr("src", img_src);
	$("#bankcard_add_alert").slideDown(500);
	$(".zhezhao5").show();
}
//关闭提示框
function closeAlert(){
	$("#bankcard_add_alert").hide();
	$(".zhezhao5").hide();
	window.location.reload();
}

$(document).ready(function() {
	//默认触发银行卡是焦点事件
	$("#bankid").blur();
	formatBankNo($("#bankid").get(0));
});

var flag = 0;
//function start(){
//	var text = document.getElementById("bindcard");
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

	
