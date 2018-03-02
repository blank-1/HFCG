// JavaScript Document
$(function(){
	//页面tab切换
	$(function(){
	    $(".list_tab>h6>span").click(
	        function(){
	            $(this).addClass("current").siblings().removeClass("current");
	            $(".list_tab>div").eq($(".list_tab>h6>span").index(this)).show().siblings("div").hide();
	            bottomB();
	        }
	    )
	});
	//财富券点击显示隐藏
	$(function(){
		$("#list-cfq-icon-i").click(function(){
				if($(this).hasClass("shang")){
						$(this).removeClass("shang").addClass("xia");
						$(".list-cfq-xl").css("display","block");
				}else{
						$(this).removeClass("xia").addClass("shang");
						$(".list-cfq-xl").css("display","none");
				}
			}
		)
	});
	//全部余额
	$("#allbalance").click(function(){
			var money1=rmoney($("#mony2").html());//剩余金额
			var limied=rmoney($("#limited").html());//限投
			var yue=rmoney($("#yue").html());//账户余额
			var qitou=rmoney($("#qitou").html());//起投金额
			var moneyipt=0;
		var ques=0;//判断是否提示
		var _money=function(){ques=1;$("#money").addClass("ipt-error");$("#errorMoneyInfo").html("投标金额不能小于起投金额");};//限投提示
		if($("#jiliang").html()=="元"){
			
			moneyipt=Math.min(money1,limied,yue)>=qitou?Math.min(money1,limied,yue):_money;
			
			$("#money").val(moneyipt,2);
			$("#bay_ipt_money").val(moneyipt,2);
			
		}else{
			qitou=qitou/10000;
			moneyipt=Math.min(money1,limied,yue)>=qitou?Math.min(money1,limied,yue):_money;
			$("#money").val(moneyipt,4);
			$("#bay_ipt_money").val(moneyipt,4);
		}
		yqsyCalc($("#money").get(0));
		//monyqsy(ques,$("#money"));
	});
	
	//最大可投
	$("#firstfinan").click(function(){
			var money1=rmoney($("#mony2").html());//剩余金额
			var limied=rmoney($("#limited").html());//限投
			var qitou=rmoney($("#qitou").html());//起投金额
			var moneyipt=0;
		var ques=0;//判断是否提示
		if($("#jiliang").html()=="元"){
			
			//moneyipt=Math.max(Math.min(money1,limied),qitou);
			moneyipt=Math.min(money1,limied)>=qitou?Math.min(money1,limied):0;
			$("#money").val(moneyipt,2);
			$("#bay_ipt_money").val(moneyipt,2);
			
		}else{
			qitou=qitou/10000;
			//moneyipt=Math.max(Math.min(money1,limied),qitou);
			moneyipt=Math.min(money1,limied)>=qitou?Math.min(money1,limied):0;
			$("#money").val(moneyipt,4);
			$("#bay_ipt_money").val(moneyipt,4);
		}
		yqsyCalc($("#money").get(0));
		//monyqsy(ques,$("#money"));
	});

	//提示进行身份验证
	$("#unpay").click(function(){
		$("#money").addClass("ipt-error");
		$("#errorMoneyInfo").html('为保证您的账户安全，请先去<a href="javascript:;" data-id="hengfengCard" data-mask="mask">恒丰绑卡</a>，再投资！');
		$("#errorMoneyInfo").removeClass("hui");
	});
	$("#un_pay").click(function(){
		$("#bay_ipt_money").addClass("ipt-error").parent().parent().siblings("em").html('为了您的账户安全，请先进行<a href="javascript:;" data-id="shenfen" data-mask="mask" style="color: #fff;">身份验证</a>');
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
			$("#mony2").html(moneyji);
			//限投
			$("#limited").html(rmoney($("#limited").html())/10000).next("span").html("万");
			
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
			$("#mony2").html(moneyyuan.substring(0,moneyyuan.length-1)+".00");
			
			//限投
			$("#limited").html(fmoney(parseFloat($("#limited").html())*10000,2)).next("span").html("元");
			//文本框
			if($("#money").val()!="" && $("#money").val()>=0){
				$("#money").val(Math.floor(parseFloat($("#money").val())*10000)+"");
			}
			if($("#bay_ipt_money").val()!="" && $("#bay_ipt_money").val()>=0){

				$("#bay_ipt_money").val(Math.floor(parseFloat($("#bay_ipt_money").val())*10000)+"");
			}
		}
	});

	function getShowCFQ(toubim){
		var massage="<img src='"+rootPath+"/images/fdetail/dingpai.png' style='vertical-align:middle; margin-right:7px'/>";
		if(toubim<5000){
			massage+="实际支付金额满5000元，奖5元财富券";
		}else if(toubim>=5000 && toubim<10000){
			massage+="实际支付金额满10000元，奖10元财富券";
			
		}else if(toubim>=10000 && toubim<20000){
			massage+="实际支付金额满20000元，奖20元财富券";
			
		}else if(toubim>=20000 && toubim<50000){
			massage+="实际支付金额满50000元，奖50元财富券";
			
		}else if(toubim>=50000){
			massage+="实际支付金额满100000元，奖100元财富券";
			
		}
		return massage;
	}
	//输入金额时计算预期收益     预期收益={（投标金额*年化利率）/12}*借款期限
	$("#money,#bay_ipt_money").keyup(function(){
		$(this).parent().siblings("em").removeClass("hui");
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
			var mas=getShowCFQ($(this).val());
			$(this).removeClass("ipt-error").parent().siblings("em").html(mas).addClass("hui");
		}
	});
	
	//计算预期收益
	function monyqsy(ques,_this){
		var moneyT=  parseFloat(_this.val()); //{（投标金额
			moneyT=moneyT+""!="NaN"? moneyT:parseFloat(0.00);
			var rate= parseFloat($(".money [data-pay]").attr("data-pay"))*0.01;//年化利
			var daym=  parseFloat($(".money [data-term]").attr("data-term"));//封闭期
			if(ques==0){
				massage="";
				_this.removeClass("ipt-error").parent().siblings("em").html(massage);
			}
			if($(".yuan1").html()=="万"){
				moneyT=moneyT*10000;
			}
			$("#yqsy").html((Math.floor(((moneyT * rate)/12)*daym*100))/100+"元");
	} 
	
	//金额输入验证
	$("#money,#bay_ipt_money").blur(function(){
		if($(this).attr("id")=="money"){
			$("#bay_ipt_money").val($(this).val());
		}else{
			$("#money").val($(this).val());
		}
		yqsyCalc(this);
	});
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

	//投标详情页面点击提交订单，去支付时
	$("#pay,#bay_pay").click(function(){
		var b1=moneyf($("#money"));
		if($(this).attr("id")=="bay_pay"){
			
			b1=moneyf($("#bay_ipt_money"));
		}else{
			b1=moneyf($("#money"));
			
		}
		if( b1==""){
			massage='';

			var yue = $("#yue_input").val();
			var buy = $(".yuan1").html()=="万"?parseFloat($("#money").val())*10000:$("#money").val();

			//计算可用余额是否足够
			if((yue-buy)<0){
				$("#amount").val(buy);
				$("#lendForm").attr("action", rootPath + "/finance/toBuyBidLoanByPayAmount").submit();
			}else{
				$("#money").removeClass("ipt-error").parent().siblings("em").html(massage);
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
			var mas=getShowCFQ($("#money").val());
			$("#money").removeClass("ipt-error").parent().siblings("em").html(mas).addClass("hui");
		}
	});
	
	
	//省心计划详情页面 点击提交订单，去支付
	$("#finance_detail_pay").click(function(){
		var b1=moneyf($("#money"));
		if( b1==""){
			massage='';
			var yue = $("#yue_input").val();
			var buy = $("#money").val();
			//计算可用余额是否足够
			if((yue-buy)<0){
				var lendProductPublishId = $("#lendProductPublishId").val();
				window.location.href = rootPath + "/finance/toBuyFinanceByPayAmount?lendProductPublishId=" + lendProductPublishId + "&amount=" + buy;
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
	function moneyf(mond){
		mond.parent().siblings("em").removeClass("hui");
		var money1=rmoney($("#mony2").html());//剩余金额
		var limied=rmoney($("#limited").html());//限投
		var qitou=rmoney($("#qitou").html());//起投金额
		var mondmoney=0;

		//计算万元前验证
		if(mond.val()==""||!/^(([0-9]|([1-9][0-9]{0,9}))((\.[0-9]{0,9})?))$/.test(mond.val())
			||(mond.val().length>10)){
			massage="请输入正确的理财金额";
			mond.addClass("ipt-error");
			$("#errorMoneyInfo").removeClass("hui").html(massage);
			return massage;
		}

		if($(".yuan1").html()=="万"){
			mondmoney=parseFloat(mond.val())*10000;
		}else{
			mondmoney=parseFloat(mond.val());
		}
		var mondint;
		//计算万元后验证
		if(mond.val()==""||!/^(([0-9]|([1-9][0-9]{0,9}))((\.[0-9]{0,9})?))$/.test(mondmoney)
			||(mond.val().length>10)){
			mondint=0;
			massage="请输入正确的理财金额";
			mond.addClass("ipt-error");
			$("#errorMoneyInfo").removeClass("hui").html(massage);
			return massage;
		}else{
			mondint=parseFloat(mond.val());
		}
		
		if(mondmoney<qitou){
			
			massage="请输入大于"+qitou+" 的金额！";
			mond.addClass("ipt-error");
			$("#errorMoneyInfo").removeClass("hui").html(massage);
		}else{
			
			if(mondmoney<100 || mondmoney % 100!=0){
				massage="请输入大于100 并且是100 倍数的金额！";
				mond.addClass("ipt-error");
				$("#errorMoneyInfo").removeClass("hui").html(massage);
			}else{

				massage=getShowCFQ(mondmoney);
				mond.removeClass("ipt-error");$("#errorMoneyInfo").addClass("hui").html(massage);
				massage="";

				//检查剩余金额是否满足
				if(money1<mond.val()){
					massage="购买金额超出剩余金额";
					mond.addClass("ipt-error");
					$("#errorMoneyInfo").removeClass("hui").html(massage);
				}
				var moneyipt=0;
				if($("#jiliang").html()=="元"){
					moneyipt=Math.min(money1,limied)>=qitou?Math.min(money1,limied):0;
				}else{
					qitou=qitou/10000;
					moneyipt=Math.min(money1,limied)>=qitou?Math.min(money1,limied):0;
				}

				if((parseFloat(mond.val())-moneyipt)>0){
					massage="购买金额超出最大可投限额";
					mond.addClass("ipt-error");
					$("#errorMoneyInfo").removeClass("hui").html(massage);
				}
			}
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
	
	
})
/*$(function(){

function getCookieVal(offset){
	var endstr=document.cookie.indexOf(";",offset);
	if(endstr==-1)
		endstr=document.cookie.length;
	return unescape(document.cookie.substring(offset,endstr));
}
function GetCookie(name){
	var arg=name+"=";
	var alen=arg.length;
	var clen=document.cookie.length;
	var i=0;
	while(i<clen){
		var j=i+alen;
		if(document.cookie.substring(i,j)==arg)
			return getCookieVal(j);
			i=document.cookie.indexOf(" ",i)+1;
			if(i==0)
				break;
	}
	return null;
}
function SetCookie(name,value){
	var argv=SetCookie.arguments;
	var argc=SetCookie.arguments.length;
	var expires=(2<argc)?argv[2]:null;
	var path=(3<argc)?argv[3]:null;
	var domain=(4<argc)?argv[4]:null;
	var secure=(5<argc)?argv[5]:false;
	document.cookie=name+"="+escape(value)+((expires==null)?"":("; expires="+expires.toGMTString()))+((path==null)?"":("; path="+path))+((domain==null)?"":("; domain="+domain))+((secure==true)?"; secure":"");
}
function ResetCounts(name){
	visits=0;
	SetCookie("visits",visits,expdate,"/",null,false);
	location.reload();
}
//-->

	var expdate=new Date();
	var visits;//以下设置COOKIES时间为1年,自己随便设置该时间..

	expdate.setTime(expdate.getTime()+(24*60*60*1000*365));

	if(!(visits=GetCookie("visits")))

	visits=0;
	visits++;
	SetCookie("visits",visits,expdate,"/",null,false);//以下信息显示可以使用标准的HTML语法,自己随便设置。
	$(".finimg3,.finimg4,.finimg5,.finimg6,.finimg,.finimg2,.finimg7,.finimg8,.btnbai").css({"z-index":"auto","display":"none"});
	$(".zhezhao").hide();	
	if(visits==1){
	var session = $("#sessionScope").val();
//		if (!session && typeof(session)!="undefined" && session!=0){
		if(session!=null && session!=""){
//		$(".zhezhao").show();
			$(".finimg3,.finimg4,.finimg5,.finimg6,.finimg,.finimg2,.finimg7,.finimg8,.btnbai").css({"z-index":"9999","display":"block"});
			$(".zhezhao").show();
		}
	}
	
	getCaiState();
	$(".caicion").click(function(){
		
		getCaiState();
	});
	$("#closeCFQ").click(function(){
		getCloseCFQTips();
	});
	//打开财富券提示
	function getCFQTips(){
		
		$("#toopInfo").show();
		$(".zhezhao").show();
	}
	$(".btnbai").click(function(){
		
		$(".finimg3,.finimg4,.finimg5,.finimg6,.finimg,.finimg2,.finimg7,.finimg8,.btnbai").css({"z-index":"auto","display":"none"});
		$(".zhezhao").hide();
	});
	//关闭财富券提示
	function getCloseCFQTips(){

		$("#toopInfo").hide();
		$(".zhezhao").hide();
	}
	
	//隐藏显示财富券
	function getCaiState(){
		var cai=$(".caicion");
		if(cai.hasClass("caitop")){
			cai.removeClass("caitop").addClass("caibottom");
			$(".pailie").slideDown(500);
			cai.find("img").attr("src","../images/fdetail/cfq_03.jpg");

		}else{
			
			cai.removeClass("caibottom").addClass("caitop");
			$(".pailie").slideUp(500);
			cai.find("img").attr("src","../images/fdetail/cfq_04.jpg");
		}
	}
	
	var nnt1=1;
	var nnt2=1;
	$(".finleft").hide();
	$(".finright").hide();
	$(".fin-djq h2").click(function(){
		
		if(nnt1==1){
			nnt1=0;
			$(".findinone").slideDown(500);
			$(this).css({"background-image":"url(../images/fdetail/cfq_03.jpg)","border-bottom":"1px solid #d7d7d7","vertical-align":"middle"});
			$(this).siblings(".finleft").show();
			$(this).siblings(".finright").show();
			
		}else{
			$(".findinone").slideUp(500);
			$(this).css({"background-image":"url(../images/fdetail/cfq_04.jpg)","border":"none"});
			$(this).siblings(".finleft").hide();
			$(this).siblings(".finright").hide();
			nnt1=1;
		}
	});
})*/


$(document).ready(function(e) {
        var counter = 0;
        var winNameOld = window.name;
        window.name = '';
        if (window.history && window.history.pushState) {
                         $(window).on('popstate', function () {
//                                        window.history.pushState('forward', null, '#');
//                                        window.history.forward(1);
                                        window.name = winNameOld;
                            });
          }

//          window.history.pushState('forward', null, '#'); //在IE中必须得有这两行
//          window.history.forward(1);
});
