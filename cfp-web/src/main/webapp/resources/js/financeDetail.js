// JavaScript Document
$(function(){

	//全部余额
	$("#allbalance").click(function(){
			var money1=rmoney($("#mony2").html());//剩余金额
			var limied=rmoney($("#limited").html());//限投
			var yue=rmoney($("#yue").html());//账户余额
			var qitou=rmoney($("#qitou").html());//起投金额
			var moneyipt=0;
		var ques=0;//判断是否提示
		var _money=function(){ques=1;$("#money").addClass("ipt-error").parent().siblings("em").html("投标金额不能小于起投金额");};//限投提示
		if($("#jiliang").html()=="元"){
			
			moneyipt=Math.min(money1,limied,yue)>=qitou?Math.min(money1,limied,yue):_money;

			if($("#limited").html()=="--" && $("#mony2").html()!="--"){

				moneyipt=Math.min(money1,yue)>=qitou?Math.min(money1,yue):_money;
			}

			if($("#mony2").html()=="--" && $("#limited").html()!="--"){
				moneyipt=Math.min(limied,yue)>=qitou?Math.min(limied,yue):_money;
			}

			if($("#mony2").html()=="--" && $("#limited").html()=="--"){
				moneyipt=yue>=qitou?yue:_money;
			}
			$("#money").val(moneyipt,2);
			$("#bay_ipt_money").val(moneyipt,2);
			
		}else{
			qitou=qitou/10000;
			moneyipt=Math.min(money1,limied,yue)>=qitou?Math.min(money1,limied,yue):_money;
			if($("#limited").html()=="--" && $("#mony2").html()!="--"){

				moneyipt=Math.min(money1,yue)>=qitou?Math.min(money1,yue):_money;
			}

			if($("#mony2").html()=="--" && $("#limited").html()!="--"){
				moneyipt=Math.min(limied,yue)>=qitou?Math.min(limied,yue):_money;
			}

			if($("#mony2").html()=="--" && $("#limited").html()=="--"){
				moneyipt=yue>=qitou?yue:_money;
			}
			$("#money").val(moneyipt,4);
			$("#bay_ipt_money").val(moneyipt,4);
		}
		yqsyCalc($("#money").get(0));
		monyqsy(ques,$("#money"));
	});
	
	//最大可投
	$("#firstfinan").click(function(){
			var money1=rmoney($("#mony2").html());//剩余金额
			var limied=rmoney($("#limited").html());//限投
			var qitou=rmoney($("#qitou").html());//起投金额
			var moneyipt=0;
		var ques=0;//判断是否提示
		if($("#jiliang").html()=="元"){
			moneyipt=Math.min(money1,limied)>=qitou?Math.min(money1,limied):0;
			if($("#limited").html()=="--" && $("#mony2").html()!="--"){

				moneyipt=money1>=qitou?money1:0;
			}

			if($("#mony2").html()=="--" && $("#limited").html()!="--"){
				moneyipt=limied>=qitou?limied:0;
			}

			if($("#mony2").html()=="--" && $("#limited").html()=="--"){
				moneyipt=qitou;
			}
			//moneyipt=Math.max(Math.min(money1,limied),qitou);
			$("#money").val(moneyipt,2);
			$("#bay_ipt_money").val(moneyipt,2);
			
		}else{
			qitou=qitou/10000;
			//moneyipt=Math.max(Math.min(money1,limied),qitou);
			moneyipt=Math.min(money1,limied)>=qitou?Math.min(money1,limied):0;
			if($("#limited").html()=="--" && $("#mony2").html()!="--"){

				moneyipt=money1>=qitou?money1:0;
			}

			if($("#mony2").html()=="--" && $("#limited").html()!="--"){
				moneyipt=limied>=qitou?limied:0;
			}

			if($("#mony2").html()=="--" && $("#limited").html()=="--"){
				moneyipt=qitou;
			}
			$("#money").val(moneyipt,4);
			$("#bay_ipt_money").val(moneyipt,4);
		}
		yqsyCalc($("#money").get(0));
		monyqsy(ques,$("#money"));
	});

	//提示进行身份验证
	$("#unpay").click(function(){
		$("#money").addClass("ipt-error");
		$("#errorMoneyInfo").html('为保证您的账户安全，请先进行<a href="javascript:;" data-id="shenfen" data-mask="mask">身份验证</a>，再投资！');
	});
	$("#un_pay").click(function(){
		$("#bay_ipt_money").addClass("ipt-error").parent().siblings("em").html('为了您的账户安全，请先进行<a href="javascript:;" data-id="shenfen" data-mask="mask" style="color: #fff;">身份验证</a>');
	});


	//点击圆形小背景时
	$(".yuan1,#bay-yuan").click(function(){
		var moneymon=parseFloat($("#mony2").attr("data-money"));
		var moneymons=$("#mony2").attr("data-money");
		var mn;
		var mm;
		var moneyji=0;
		var moneyyuan="";
		var myl;
		if($(this).html()=="元"){
			
			//本期剩余金额
			moneyji=moneymon/10000;
			$("#mony2").html();
			$(".yuan1").html("万");
			//飘窗万元
			$("#bay-yuan").html("万");
			$("#jiliang").html("万");
			$("#mony2").html(moneyji<=0?"--":moneyji);
			//限投
			$("#limited").html($("#limited").html()=="--"?"--":rmoney($("#limited").html())/10000).next("span").html("万");
			
			//文本框
			if($("#money").val()!="" && $("#money").val()>=0){
				$("#money").val(parseFloat($("#money").val())/10000+"");
			}
			
			//文本框
			if($("#bay_ipt_money").val()!="" && $("#bay_ipt_money").val()>=0 ){
				$("#bay_ipt_money").val(parseFloat($("#bay_ipt_money").val())/10000+"");
			}
		}else{
			//本期剩余金额
			$(".yuan1").html("元");
			$("#bay-yuan").html("元");
			$("#jiliang").html("元");
			myl=(moneymon+"").length;
			
			for(var i=1; i<=myl ;i){
				if(myl<=3){
					mm=moneymons+"";
					moneyyuan=mm+","+moneyyuan;
					break;
				}else{
					mm=moneymons.substring(myl-3,myl);
					moneyyuan=mm+","+moneyyuan;
					moneymons=moneymons.substring(0,myl-3);
				}
				myl=myl-3;
			}
			$("#mony2").html(moneyyuan.substring(0,moneyyuan.length-1)<=0?"--":moneyyuan.substring(0,moneyyuan.length-1)+".00");
			
			//限投
			$("#limited").html($("#limited").html()=="--"?"--":fmoney(parseFloat($("#limited").html())*10000,2)).next("span").html("元");
			//文本框
			if($("#money").val()!="" && $("#money").val()>=0){
				$("#money").val(Math.floor(parseFloat($("#money").val())*10000)+"");
			}
			if($("#bay_ipt_money").val()!="" && $("#bay_ipt_money").val()>=0){

				$("#bay_ipt_money").val(Math.floor(parseFloat($("#bay_ipt_money").val())*10000)+"");
			}
		}
	});
	
	//输入金额时计算预期收益     预期收益={（投标金额*年化利率）/12}*借款期限
	$("#money,#bay_ipt_money").keyup(function(){
		if($("#login_flag").val()=='0'){
			massage="你还没有登陆，";
			$(this).removeClass("ipt-error").parent().siblings("em").html(massage);
		}

		if(!/^[0-9]+([.]{1}[0-9]+){0,1}$/.test($(this).val())){
			var _thisval=0;
			if($(".yuan1")=="元"){
				_thisval=$(this).val();
			}else{
				_thisval=parseFloat($(this).val())*10000;
			}
			if(_thisval==""){
				$("#yqsy").html("0.00元");
				massage="";
				$(this).removeClass("ipt-error").parent().siblings("em").html(massage);
				
			}else{
				$("#yqsy").html("0.00元");
				massage="请输入正确的理财金额";
				$(this).addClass("ipt-error").parent().siblings("em").html(massage);
			}
		}else{
			$("#money").val($(this).val());
			$("#bay_ipt_money").val($(this).val());
			var ques=0;//判断是否提示
			monyqsy(ques,$(this));
		}
	});
	
	//计算预期收益
	function monyqsy(ques, _this) {
		var moneyT = parseFloat(_this.val()); // {（投标金额
		moneyT = moneyT + "" != "NaN" ? moneyT : parseFloat(0.00);
		var profitRate = $("#profitRate").val() * 0.01;// 年化利
		var timeLimit = $("#timeLimit").val();// 产品期限
		var timeLimitType = $("#timeLimitType").val();// 产品期限类型
		if (ques == 0) {
			massage = "";
			_this.removeClass("ipt-error").parent().siblings("em").html(massage);
		}
		if ($(".yuan1").html() == "万") {
			moneyT = moneyT * 10000;
		}
		
		var day = 0;
		if(timeLimitType=='1'){//期限类型为天
			day = 365;
		}else if(timeLimitType=='2'){//期限类型为月
			day = 12;			
		}
		$("#yqsy").html((Math.floor(((moneyT * profitRate) / day) * timeLimit * 100)) / 100  + "元");
	}
	
	
	//省心计划详情页面 点击提交订单，去支付
	$("#finance_detail_pay").click(function(){
		if($(this).attr("data-id")=="login"){
			return ;
		}
		var b1=moneyf($("#money"));
		if( b1==""){
			massage='';
			var yue = $("#yue_input").val();
			var buy = $("#money").val();
			//计算可用余额是否足够
			if((yue-buy)<0){
				$("#amount").val(buy);
				$("#finance_detail_form").attr("action", rootPath + "/finance/toBuyFinanceByPayAmount").submit();
			}else{
				$("#money").removeClass("ipt-error").parent().siblings("em").html(massage);
				$("#buymoney").html($("#money").val());
				$("#expected").html($("#yqsy").html().substring(0,$("#yqsy").html().length-1));
				$("#cjd").slideDown(500);
				$(".zhezhao1").show();
			}
		}
		
	});
	
	//借款金额输入验证
	function moneyf(mond) {
		var money1 = rmoney($("#mony2").html());// 剩余金额
		var limied = rmoney($("#limited").html());// 限投
		var qitou = rmoney($("#qitou").html());// 起投金额
		var mondmoney = 0;
		var flag = true;
		if ($(".yuan1").html() == "万") {
			mondmoney = parseFloat(mond.val()) * 10000;
		} else {
			mondmoney = parseFloat(mond.val());
		}
		
		//检查金额是否符合规则
		if (mond.val() == "" || !/^(([0-9]|([1-9][0-9]{0,9}))((\.[0-9]{0,9})?))$/.test(mondmoney) || (mond.val().length > 10)) {
			massage = "请输入正确的理财金额";
			$("#errorMoneyInfo").removeClass("hui").html(massage);
			flag = false;
		} else if (mondmoney < qitou) {
			massage = "请输入大于" + qitou + " 的金额！";
			$("#errorMoneyInfo").removeClass("hui").html(massage);
			flag = false;
		} else if (mondmoney < 100 || mondmoney % 100 != 0) {
			massage = "请输入大于100 并且是100 倍数的金额！";
			$("#errorMoneyInfo").removeClass("hui").html(massage);
			flag = false;
		}
		//金额与剩余金额、限投金额比较
		if(flag){
			if ($("#mony2").html() == "--") {
				if ($("#limited").html() != "--" && mondmoney > limied) {
					massage = "购买金额不能超过限投金额";
					$("#errorMoneyInfo").removeClass("hui").html(massage);
					flag = false;
				}
			} else if ($("#limited").html() == "--"){
				if ($("#mony2").html() != "--" && mondmoney > money1) {
					massage = "购买金额不能超过剩余金额";
					$("#errorMoneyInfo").removeClass("hui").html(massage);
					flag = false;
				}
			} else {
				var moneyipt=0;
				if($("#jiliang").html()=="元"){
					moneyipt=moneyipt=Math.min(money1,limied)>=qitou?Math.min(money1,limied):0;
				}else{
					qitou=qitou/10000;
					moneyipt=moneyipt=Math.min(money1,limied)>=qitou?Math.min(money1,limied):0;
				}

				if((parseFloat(mond.val())-moneyipt)>0){
					massage="购买金额超出最大可投限额";
					$("#errorMoneyInfo").removeClass("hui").html(massage);
					flag = false;
				}
			}
		}
		
		if (!flag) {
			mond.addClass("ipt-error").parent().parent().siblings("#errorMoneyInfo").html(massage);
		} else {
			massage = "";
			mond.removeClass("ipt-error").parent().parent().siblings("#errorMoneyInfo").html(massage);
		}
		return massage;
	}


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
	//点击确认支付
	$("#repay").click(function(){
		var b1 = passwordf($("#jypassword"), "1");
		if (b1 == "") {
			if ($("#checkBox").is(':checked')) {
				massage = '';
				$("#checkBox").parent().next("em").html(massage);

				$("#amount").val($("#buymoney").html());
				//理财
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
							$("#finance_detail_form").attr("action", rootPath + "/finance/buyFinanceByAccountAmount").submit();
						}
					}
				});

			} else {
				massage = '请勾选购买协议前复选框！';
				$("#checkBox").parent().next("em").html(massage);
			}
		}
	});

	//省心计划详情 点击其他方式支付
	$("#payrepay").click(function(){
		$("#amount").val($("#money").val());
		$("#finance_detail_form").attr("action", rootPath + "/finance/toBuyFinanceByPayAmount").submit();
	});
	
	$("#tbpay").click(function(){
		var b1 = passwordf($("#jypassword"), "1");
		if (b1 == "") {
			if ($("#checkBox").is(':checked')) {
				massage = '';
				$("#checkBox").parent().next("em").html(massage);
				$("#amount").val($("#buymoney").html());
				//投标
				var loginStatus = true;
				$.ajax({
					url:rootPath+"/finance/checkBidLoanByAccountBalance",
					type:"post",
					data:{"bidPass":$("#jypassword").val()},
					async : false,
					error : function(XHR) {
						loginStatus = false;// session无效
					},
					success:function(data){
						var _data =  eval("("+data+")");
						if(!_data.isSuccess){
							result = false;
							$("#"+_data.id).addClass("ipt-error").parent().siblings("em").html(_data.info);
						}else{
							$("#lendForm").attr("action", rootPath + "/finance/bidLoanByAccountBalance").submit();
						}
					}
				});
				// 登录无效 弹出登录框
				if (!loginStatus) {
					$("#cjd").hide();
					$("#login").slideDown(500);
					return false;
				}

			} else {
				massage = '请勾选购买协议前复选框！';
				$("#checkBox").parent().next("em").html(massage);
			}
		}
	});

	//判断交易密码
	$("#jypassword").blur(function(){
		passwordf($(this),"0");
	});
	
});

//页面tab切换
$(function(){
    $(".list_tab>h6>span").click(
        function(){
            $(this).addClass("current").siblings().removeClass("current");
            $(".list_tab>div").eq($(".list_tab>h6>span").index(this)).show().siblings("div").hide();
        }
    )
    
	
});

var message = "";
function yqsyCalc(v){
	if(!$(v).val()==""){
		var id = $("#loanApplicationId").val();
		var mone=moneyf($(v));
		var money = $(".yuan1").html()=="万"?parseFloat($(v).val())*10000:$(v).val();
		//加缓存
		var cache = $("#cacheMoney");
		if(cache!=null){
			var _money = cache.html();
			if(money==_money){
				return;
			}
		}
		if(mone==""){
			$.ajax({
				url:rootPath+"/finance/getExpectProfit",
				type:"post",
				data:{"loanApplicationId":id,"amount":money},
				async : false,
				success:function(data){
					if(data.indexOf(",")!=-1){
						var arr = data.split(",");
						$("#yqsy1").html(fmoney(arr[0],2)+"元+"+fmoney(arr[1],2)+"元(奖励)<span style='display: none;' id='cacheMoney'>"+money+"</span>");
						$("#yqsy").html(fmoney(arr[0],2)+"元");
					}else{
						$("#yqsy1").html(fmoney(data,2)+"元<span style='display: none;' id='cacheMoney'>"+money+"</span>");
						$("#yqsy").html(fmoney(data,2)+"元");
					}
				}
			});
		}
	}else{
		massage="";
		$(v).removeClass("ipt-error").parent().siblings("em").html(massage);
	}
}
