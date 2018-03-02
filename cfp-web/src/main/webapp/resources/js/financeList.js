// JavaScript Document
$(function(){
	
	
	$(".flcontext").hover(function(){
		$(this).addClass("flcontexthover").siblings(".flcontext").removeClass("flcontexthover");
	},function () {

		$(this).removeClass("flcontexthover");
		
		$(".flcontext").eq(0).addClass("flcontexthover");
	});
	
	$(".uldiv").hover(function(){
		$(this).children("ul").show();
	},function(){
		
		$(this).children("ul").hide();
	});
	
	$(".uldiv .la li").click(function(){
		$(this).addClass("action").siblings().removeClass("action");
		$(this).parent().hide().siblings().html($(this).children().html());
		
	});
	
	//金额输入验证
	$(document).on('blur',".flright .ipt-input",function(){
	//$(".flright .ipt-input").blur(function(){
		
		moneyf($(this),"0");
	});
	
	function moneyipt(element){
		//console.log(element);
		moneyf(moneyipt,"0");
	}
	//yumoney保存余额
	//点击注册保存时
	$(document).on("click",".flbtn",function(){
		var _input =$(this).parent().siblings("div").children().children("input");
		var b1=moneyf(_input,"1");
		if( b1==""){
			var loginStatus = true;
			var canPay = true;
			var isVerified = true;
			var lendProductPublishId = $(this).parent().siblings().find('[data-id=lendProductPublishId]').attr("data-value");
			var amount = $(this).parent().siblings("div").children().children("input").val();
			$.ajax({
				url : rootPath + "/finance/checkBuyFinanceByPayAmount",
				type : "post",
				data : {
					lendProductPublishId : lendProductPublishId,
					buyAmount : amount
				},
				async : false,
				error : function(XHR) {
					loginStatus = false;// session无效
				},
				success : function(data) {
					var data = eval("(" + data + ")");
					$("#token").val(data.token);
					if (typeof (data.isVerified) != "undefined" && !data.isVerified) {
						isVerified = false;
					}
					if (typeof (data.canPayByAccountAmount) != "undefined" && !data.canPayByAccountAmount) {
						canPay = false;
					}
				}
			});
			// 登录无效 弹出登录框
			if (!loginStatus) {
				$(".zhezhao1").show();
				$("#login").slideDown(500);
				return false;
			}
			// 未进行身份验证
			if (!isVerified) {
				_input.addClass("ipt-error").parent().siblings("em").html('为保证账户安全 请先进行<a href="javascript:;" data-id="shenfen" data-mask="mask">身份验证</a> 再投资！');
				return false;
			}
			//如果余额不足 跳转至其他支付方式
			if (!canPay) {
				$("#lendProductPublishId").val(lendProductPublishId);
				$("#amount").val(amount);
				$("#finance_list_form").attr("action", rootPath + "/finance/toBuyFinanceByPayAmount").submit();
			} else {
				var flm = $(this).parent().siblings("div").children().children("input");
				var timeLimitType=$(this).parent().siblings("div").children("[data-id=timeLimitType]").attr("data-value");
				var timeLimit=$(this).parent().siblings("div").children("[data-id=timeLimit]").attr("data-value");
				
				// 清空帐户余额
				$("#account").html("");
				// 利率
				$("#expected").html("");
				// 清空总金额
				$("#buymoney").html("");
				
				massage='';
				$("#savechb").removeClass("ipt-error").parent().siblings("em").html(massage);
				//window.location.href="";
				$("#"+$(this).attr("data-id")).slideDown(500);
				
				//遮罩
				$(".zhezhao").show();
				
				//标题
				$("#title").html($(this).parents(".flright").siblings().find(".title").children("a").html());
				
				//financePublishId传值
				$("#finance_list_form #lendProductPublishId").val(lendProductPublishId);
				
				//计算金额和利率
				$("#buymoney").html(fmoney(parseFloat(flm.val()),2));
				$("#finance_list_form #amount").val(flm.val());
				
				//计算利率   预期收益={（投标金额*年化利率）/12}*借款期限
				var liximoney=(parseFloat(flm.val()) * parseFloat($(this).parents(".flright").siblings().find(".lilv").html())/100);
				var money8 = 0;
				if(timeLimitType=='1'){//期限类型为天					
					money8=parseFloat(liximoney)/365;
				}else if(timeLimitType=='2'){//期限类型为月
					money8=parseFloat(liximoney)/12;
				}
				//预期收益
				var tmyk9=money8 * parseFloat(timeLimit);
				$("#expected").html(Math.floor(tmyk9*100)/100);
				
				//帐户余额
				$("#account").html(fmoney(parseFloat($("#hidem").attr("data-value")),2));
				
			}
		}
		
	});
	//格式化金额
	function _Money(money){
		
		var moneyindex;//小数点后几位
		if((money+"").indexOf(".")=="-1"){
			moneyindex=".00";
		}else{
			moneyindex="."+((money+"").split(".")[1]);
		}
			
		var moneylength=((money+"").split(".")[0]).length;//金额长度
		var mLocal="";//临时存储金额
		var moneyStr="";//最后金额
			
		for(var i=1; i<=moneylength ;i){
			if(moneylength<=3){
				mLocal=money+"";
				moneyStr=mLocal+","+moneyStr;
				break;
			}else{
				mLocal=(money+"").substring(parseFloat(moneylength)-3,parseFloat(moneylength));
				moneyStr=mLocal+","+moneyStr;
				money=(money+"").substring(0,moneylength-3);
					
			}
			moneylength=moneylength-3;
		}
			
		return moneyStr.substring(0,moneyStr.length-1)+moneyindex;
	}
	
	//点击确认支付
	$("#repay").click(function(){
		var b1 = passwordf($("#jypassword"), "1");
		if (b1 == "") {
			if ($("#checkBox").is(':checked')) {
				massage = '';
				$("#checkBox").parent().next("em").html(massage);
				$.ajax({
					url:rootPath+"/finance/checkBidLoanByAccountBalance",
					type:"post",
					data:{"bidPass":$("#jypassword").val()},
					success:function(data){
						var _data =  eval("("+data+")");
						if(!_data.isSuccess){
							result = false;
							$("#"+_data.id).addClass("ipt-error").parent().siblings("em").html(_data.info);
						}else{
							$("#finance_list_form").attr("action", rootPath + "/finance/buyFinanceByAccountAmount").submit();
						}
					}
				});

			} else {
				massage = '请勾选购买协议前复选框！';
				$("#checkBox").parent().next("em").html(massage);
			}
		}
	});
	
	//点击确认支付
	$("#payrepay").click(function(){
		$("#finance_list_form").attr("action", rootPath + "/finance/toBuyFinanceByPayAmount").submit();
	});
	//判断交易密码
	$("#jypassword").blur(function(){
		passwordf($(this),"0");
	});
	
	//判断交易密码
	function passwordf(passval1, pa) {

		if (passval1.val() == "")// 只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if (pa == "0") {
				massage = "";
				passval1.removeClass("ipt-error").parent().siblings("em").html(massage);
			} else {
				massage = "请您输入交易密码！";
				passval1.addClass("ipt-error").parent().siblings("em").html(massage);
			}
		} else if (passval1.val().length < 6 || passval1.val().length > 16 || !(/^[0-9a-zA-Z]+$/.test(passval1.val()))) {
			massage = "交易密码错误";
			passval1.addClass("ipt-error").parent().siblings("em").html(massage);
		}else{
			massage = "";
			passval1.removeClass("ipt-error").parent().siblings("em").html(massage);
		}
		return massage;
	}
	

	// 借款金额输入验证
	function moneyf(mond, mytext) {
		// 验证金额格式 是否包含特殊符号
		if (!(/^[0-9]*[1-9][0-9]*$/.test(mond.val()))) {
			massage = "理财金额为正整数";
			mond.addClass("ipt-error").parent().siblings("em").html(massage);
			return massage;
		}
		var mondint;
		if (mond.val() == "") {
			if (mytext == "0") {
				mondint = 0;
				massage = "";
				mond.removeClass("ipt-error").parent().siblings("em").html(massage);
				return massage;
			} else {
				massage = "请输入正确的理财金额";
				mond.addClass("ipt-error").parent().siblings("em").html(massage);
				return massage;
			}
		} else {
			mondint = parseInt(mond.val());
		}
		if (mondint < 100 || mondint % 100 != 0) {
			massage = "请输入大于100 并且是100 倍数的金额！";
			mond.addClass("ipt-error").parent().siblings("em").html(massage);
		} else {
			if (parseInt($("#yue").html()) <= mondint) {
				massage = "余额不足";
				mond.addClass("ipt-error").parent().siblings("em").html(massage);
			} else {
				massage = "";
				mond.removeClass("ipt-error").parent().siblings("em").html(massage);
			}
		}
		return massage;
	}
	
});