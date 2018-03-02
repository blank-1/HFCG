// JavaScript Document
var massage;
$(function(){
	// 页面tab切换
	$(function() {
		$(".list_tab>h6>span").click(
			function() {
				$(this).addClass("current").siblings().removeClass("current");
				$(".list_tab>div").eq($(".list_tab>h6>span").index(this)).show().siblings("div").hide();
				bottomB();
			})
	});
	// 财富券点击显示隐藏
	$(function() {
		$("#list-cfq-icon-i").click(function() {
			if ($(this).hasClass("shang")) {
				$(this).removeClass("shang").addClass("xia");
				$(".list-cfq-xl").css("display", "block");
			} else {
				$(this).removeClass("xia").addClass("shang");
				$(".list-cfq-xl").css("display", "none");
			}
		})
	});

	// 全部余额,最大可投
	$("#allbalance,#firstfinan").click(function() {
		var shengyu = rmoney($("#mony2").html());// 剩余金额
		var yue = rmoney($("#yue").html());// 账户余额
		var moneyipt = Math.min(shengyu, yue);
		if ($("#jiliang").html() == "元") {
			$("#money").val(moneyipt, 2);
			$("#bay_ipt_money").val(moneyipt, 2);
		} else {
			$("#money").val(moneyipt, 4);
			$("#bay_ipt_money").val(moneyipt, 4);
		}
		yqsyCalc($("#money").get(0));
	});
	
	// 提示进行身份验证
	$("#unpay").click(
		function() {
			$("#money").addClass("ipt-error");
			$("#errorMoneyInfo").html('为保证您的账户安全，请先进行<a href="javascript:;" data-id="shenfen" data-mask="mask">身份验证</a>，再投资！');
			$("#errorMoneyInfo").removeClass("hui");
		});
	$("#un_pay").click(
		function() {
			$("#bay_ipt_money").addClass("ipt-error");
			$("#errorMoneyInfo").html('为了您的账户安全，请先进行<a href="javascript:;" data-id="shenfen" data-mask="mask" style="color: #fff;">身份验证</a>');
		});


	// 点击圆形小背景时
	$(".yuan1,#bay-yuan").click(
		function() {
			var moneymon = $("#mony2").attr("data-money");// 剩余金额
			var money = $("#money").val();// 购买金额
			if (!checkDecimalLength()) {
				return;
			}
			var buylen = $("#money").val().toString().split(".");// 购买金额的小数位数
			if (buylen.length == 2) {
				buylen = buylen[1].length;
			} else {
				buylen = 0;
			}
			var slen = moneymon.toString().split(".");// 剩余金额的小数位数
			if (slen.length == 2) {
				slen = slen[1].length;
			} else {
				slen = 0;
			}
			
			if ($(this).html() == "元") {// 元转万
				// 本期剩余金额
				$("#mony2").html();
				$(".yuan1").html("万");
				// 飘窗万元
				$("#bay-yuan").html("万");
				$("#jiliang").html("万");

				// 剩余金额
				$("#mony2").html(formatDecimalLength(moneymon / 10000 , slen + 4));
				// 出售金额
				$("#limited").html(rmoney($("#limited").html()) / 10000).next("span").html("万");

				// 文本框
				if ($("#money").val() != "" && $("#money").val() >= 0) {
					$("#money").val(formatDecimalLength(money / 10000, buylen + 4));
				}

				// 文本框
				if ($("#bay_ipt_money").val() != "" && $("#bay_ipt_money").val() >= 0) {
					$("#bay_ipt_money").val(parseFloat($("#bay_ipt_money").val()) / 10000 + "");
				}
			} else {// 万转元
				$(".yuan1").html("元");
				$("#bay-yuan").html("元");
				$("#jiliang").html("元");
				$("#mony2").html(fmoney(moneymon, 2)).next("span").html("元");
				// 限投
				$("#limited").html(fmoney(($("#limited").html() * 10000), 2)).next("span").html("元");
				// 文本框
				if ($("#money").val() != "" && $("#money").val() >= 0) {
					$("#money").val(money * 10000);
				}
				if ($("#bay_ipt_money").val() != "" && $("#bay_ipt_money").val() >= 0) {
					$("#bay_ipt_money").val(Math.floor(parseFloat($("#bay_ipt_money").val()) * 10000) + "");
				}
			}
		});

	function getShowCFQ(toubim) {
		var massage = "<img src='" + rootPath + "/images/fdetail/dingpai.png' style='vertical-align:middle; margin-right:7px'/>";
		if (toubim < 5000) {
			massage += "实际支付金额满5000元，奖5元财富券";
		} else if (toubim >= 5000 && toubim < 10000) {
			massage += "实际支付金额满10000元，奖10元财富券";
		} else if (toubim >= 10000 && toubim < 20000) {
			massage += "实际支付金额满20000元，奖20元财富券";
		} else if (toubim >= 20000 && toubim < 50000) {
			massage += "实际支付金额满50000元，奖50元财富券";
		} else if (toubim >= 50000) {
			massage += "实际支付金额满100000元，奖100元财富券";
		}
		return massage;
	}
	
	// todo
	function formatDecimalLength(val,afterLen){
		if(val){			
			var len = val.toString().split(".");
			if(len.length = 2){
				if(len[1].length>6){
					val = val.toFixed(afterLen);
				}
			}
		}
		return val;
	}

	// 购买金额小数位数限制
	function checkDecimalLength() {
		var result = true;
		var len = $("#money").val().toString().split(".");// 购买金额的小数位数
		if (len.length == 2) {
			var len2 = len[1].length;
			var checkLen = $(".yuan1").html() == "万" ? 6 : 2;
			if (len2 - checkLen > 0) {
				result = false;
				massage = "输入金额的小数点位数太长";
				$("#money").addClass("ipt-error");
				$("#errorMoneyInfo").html(massage);
			}
		}
		return result;
	}
	
	//输入金额时计算预期收益     预期收益={（投标金额*年化利率）/12}*借款期限
	$("#money,#bay_ipt_money").keyup(function(){
		$("#errorMoneyInfo").removeClass("hui");
		if($("#login_flag").val()=='0'){
			massage="你还没有登陆，";
			$(this).removeClass("ipt-error");$("#errorMoneyInfo").html(massage);
		}
		
		if(!checkDecimalLength()){
			return ;
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
				$(this).removeClass("ipt-error");$("#errorMoneyInfo").html(massage);
			}else{
				$("#yqsy").html("0.00元");
				massage="请输入正确的理财金额";
				$(this).addClass("ipt-error");$("#errorMoneyInfo").html(massage);
			}
		}else{
			$("#money").val($(this).val());
			$("#bay_ipt_money").val($(this).val());
//			var ques=0;//判断是否提示
//			var mas=getShowCFQ($(this).val());
			$(this).removeClass("ipt-error");
			$("#errorMoneyInfo").html('').addClass("hui");
		}
	});
	

	// 金额输入验证
	$("#money,#bay_ipt_money").blur(function() {
		if ($(this).attr("id") == "money") {
			$("#bay_ipt_money").val($(this).val());
		} else {
			$("#money").val($(this).val());
		}
		if (checkDecimalLength()) {
			yqsyCalc(this);
		}
	});

	// 计算预期收益
	function yqsyCalc(v){
		if(!$(v).val()==""){
			var id = $("#creditorRightsApplyId").val();
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
					url:rootPath+"/finance/getExpectRightProfit",
					type:"post",
					data:{"creditorRightsApplyId":id,"amount":money},
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
			$(v).removeClass("ipt-error");$("#errorMoneyInfo").html(massage);
		}
	}

	// 投标详情页面点击提交订单，去支付时
	$("#pay,#bay_pay").click(function() {
		var b1;
		if ($(this).attr("id") == "bay_pay") {
			b1 = moneyf($("#bay_ipt_money"));
		} else {
			b1 = moneyf($("#money"));
		}
		if (b1 == "") {
			massage = '';

			var yue = $("#yue_input").val();
			var buy = $(".yuan1").html()=="万"?parseFloat($("#money").val())*10000:$("#money").val();

			//计算可用余额是否足够
			if((yue-buy)<0){
				$("#amount").val(buy);
				$("#lendForm").attr("action", rootPath + "/finance/toBuyRightsByPayAmount").submit();
			}else{
				$("#money").removeClass("ipt-error");$("#errorMoneyInfo").html(massage);
				//如果选择的是万则乘10000，否则原样显示
				$("#buymoney").html(fmoney(buy,2));
				var sy = $("#yqsy1").html();
				if(sy.indexOf("+")!=-1){
					var span = 	$("#expected");
					span.parent().html("").append(span);
					$("#expected").html(sy);
				}else{
					sy = sy.replace("元","");
					$("#expected").html(sy);
				}
				$("#cjd").slideDown(500);
				$(".zhezhao1").show();
			}
//			var mas=getShowCFQ($("#money").val());
//			$("#money").removeClass("ipt-error");$("#errorMoneyInfo").html(mas).addClass("hui");
		}
	});
	
	// 借款金额输入验证
	function moneyf(mond){
		$("#errorMoneyInfo").removeClass("hui");
		var shengyu = $("#mony2").attr("data-money");//剩余金额
		var mondmoney=0;

		// 计算万元前验证
		if (mond.val() == "" || !/^(([0-9]|([1-9][0-9]{0,9}))((\.[0-9]{0,9})?))$/.test(mond.val()) || (mond.val().length > 10)) {
			massage = "请输入正确的理财金额";
			mond.addClass("ipt-error");
			$("#errorMoneyInfo").html(massage);
			return massage;
		}
		
		if(!checkDecimalLength()){
			return massage;
		}

		if($(".yuan1").html()=="万"){
			mondmoney=(parseFloat(mond.val())*10000);
		}else{
			mondmoney=parseFloat(mond.val());
		}
		
		// 检查剩余金额是否满足
		if (shengyu - mondmoney < 0) {
			massage = "购买金额超出剩余金额";
			mond.addClass("ipt-error");
			$("#errorMoneyInfo").html(massage);
			return massage;
		}
		
		if(mondmoney<100 && mondmoney != shengyu){
			massage="购买金额最少为100元，小于100的债权需要全部购买";
			mond.addClass("ipt-error");$("#errorMoneyInfo").html(massage);
		} else if(mondmoney % 100==0){
			if(shengyu-mondmoney>0 && shengyu-mondmoney<100){
				massage="用户购买完成后剩下的债权不能小于100！";
				mond.addClass("ipt-error");$("#errorMoneyInfo").html(massage);
			}else{
				massage="";
				mond.removeClass("ipt-error");$("#errorMoneyInfo").html(massage).addClass("hui");
			}
		} else {
			if(mondmoney!=shengyu){
				massage="购买金额必须为100的整数倍或者全部购买！";
				mond.addClass("ipt-error");$("#errorMoneyInfo").html(massage);
				return massage;
			} else{
				massage="";
				mond.removeClass("ipt-error");
				$("#errorMoneyInfo").html(massage).addClass("hui");
			}
		}
		return massage;
	}


	// 判断交易密码
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
		} else {
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
							$("#errorMoneyInfo").html(_data.info);
						}else{
							$("#lendForm").attr("action", rootPath + "/finance/buyRightsByAccountBalance").submit();
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
	
	
})

function countDown(maxtime, fn) {
	var timer = setInterval(function() {
		if (maxtime >= 0) {
			d = parseInt(maxtime / 3600 / 24);
			h = parseInt((maxtime / 3600) % 24);
			minutes = parseInt((maxtime / 60) % 60);
			seconds = parseInt(maxtime % 60);

			// minutes = Math.floor(maxtime/60);
			// seconds = Math.floor(maxtime%60);
			msg = d + "天" + h + "小时" + minutes + "分" + seconds + "秒";
			fn(msg);
			// if(maxtime == 5*60) //alert('注意，还有5分钟!');
			--maxtime;
		} else {
			clearInterval(timer);
			fn("时间到，结束!");
		}
	}, 1000);
}
countDown($("#residualTime").val(), function(msg) // 6000服务器时间差 单位为妙
{
	$(".residualTime").html(msg);
});

