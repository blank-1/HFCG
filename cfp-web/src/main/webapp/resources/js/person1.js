// JavaScript Document
var massage="";
$(function(){
	
	//计算器
	//借款金额输入验证
	$("#pborrowm").blur(function(){
		var emHtml=$(this).parent().siblings("em");
		
		moneyf2($(this),"0",emHtml,"请输入出借金额");
	});
	$("#pbloan_money").blur(function(){
		var emHtml=$(this).parent().siblings("em");

		moneyf2($(this),"0",emHtml,"请输入借款金额");
	});
		
	//点击计算机按钮时
	$("#jsqimg").each(function(){
		var s=$(this);
		var jsq=$("#jsqdiv");
		var _jsshow=function(){jsq.show();};	
		var _jshide=function(){

			jsq.hide();
			$(".borjs em").each(function() {
				$(this).html("&nbsp;");
			});
			$(".borjs input").each(function() {
				$(this).removeClass("ipt-error");
			});
			$(".hengtiao").parent().find(".bbordetail").slideUp(500);
			$("#rate_Type").children("dt").attr("data-id","0").html("付息类型");
			$("#brate").children("dt").attr("data-id","0").html("选择出借利率");
			$("#lbrate").children("dt").attr("data-id","0").html("选择借款利率");
			$("#bdata").children("dt").attr("data-id","0").html("选择出借期数");
			$("#mdata").children("dt").attr("data-id","0").html("选择借款期数");
			$("#lrate_Type").children("dt").attr("data-id","0").html("付息类型");
			/* 2015-09-14 */
		};

		$("body").click(function(i){

			if($(i.target).is($("a.closejsq"))){
				_jshide();
			}
		});
	});
	//点击计算按钮时
	$("#jsbtnLoan").click(function(){
		var emHtml=$("#pbloan_money").parent().siblings("em");

		var b1=moneyf2($("#pbloan_money"),"1",emHtml,"请输入借款金额");
		if(b1==""){

			var thtml="";
			thtml = "<tr><td>还款期数</td><td>应还本金</td><td>应还利息</td><td>应还总额</td></tr>";

			//后台计算  calculator
			var balance = $("#pbloan_money").val();
			var annualRate = $("input[name='radio6']:checked").val();
			var months = $("input[name='radio3']:checked").val();
			var method = $("input[name='radio5']:checked").val();

			$.ajax({
				url:rootPath+"/calculator/loan",
				type:"post",
				data: {
					"balance": balance,
					"annualRate": annualRate,
					"months": months,
					"method":method
				},
				error : function(data) {
					var data = data.responseText;
					var _data = eval('('+data+')');
					//参数错误
					alert(_data.errorMsg);
				},
				success: function (data) {
					$("#loan_balance1").html(fmoney(data.balance,2));
					$("#fee1").html(fmoney(data.interest,2));
					$("#allBalance1").html(fmoney(data.all,2));
					var list = data.list;

					for(var j=0;j<list.length;j++){
						thtml+="<tr><td>"+list[j].key+"</td><td>"+list[j].value.calital+"元</td><td>"+list[j].value.interest+"元</td><td>"+list[j].value.balance+"元</td></tr>";
					}

					$("#jsbtnLoanTab").html(thtml);
					$("#jsbtnLoan").parent().siblings(".bordetail").show();
					//footer重现
					$(".footer").css({"position":"inherit"});
				}
			});
		}

		
		bottomB();
		//alert("jsbtnLoan");
	});
	
	//点击计算按钮时
	$("#jsbtnLend").click(function(){
		var emHtml=$("#pborrowm").parent().siblings("em");
		
		var b1=moneyf2($("#pborrowm"),"1",emHtml,"请输入出借金额");
		
		var thtml="";
		thtml = "<tr><td>回款期数</td><td>应回本金</td><td>应回利息</td><td>应回总额</td></tr>";
		if(b1==""){
			//后台计算  calculator
			var balance = $("#pborrowm").val();
			var annualRate = $("input[name='radio1']:checked").val();
			var months = $("input[name='radio2']:checked").val();
			var method = $("input[name='radio4']:checked").val();
			$.ajax({
				url:rootPath+"/calculator/lend",
				type:"post",
				data: {
					"balance": balance,
					"annualRate": annualRate,
					"months": months,
					"method":method
				},
				error : function(data) {
					var data = data.responseText;
					var _data = eval('('+data+')');
					//参数错误
					alert(_data.errorMsg);
				},
				success: function (data) {
					$("#balance1").html(fmoney(data.balance,2));
					$("#interest1").html(fmoney(data.interest,2));
					$("#all1").html(fmoney(data.all,2));
					var list = data.list;

					for(var j=0;j<list.length;j++){
						thtml+="<tr><td>"+list[j].key+"</td><td>"+list[j].value.calital+"元</td><td>"+list[j].value.interest+"元</td><td>"+list[j].value.balance+"元</td></tr>";
					}
					$("#jsbtnLendTab").html(thtml);
					$("#jsbtnLend").parent().siblings(".bordetail").show();
					//footer重现
					$(".footer").css({"position":"inherit"});
				}
			});
		}
		//alert("jsbtnLend");		
	});
	
	//借款金额输入验证
	$("#bormonbtn2").blur(function(){
		var emHtml=$(this).parent().siblings("em");
		
		moneyf($(this),"0",emHtml,"请输入借款金额");
	});
	//出借金额输入验证
	$("#borrmon").blur(function(){
		var emHtml=$(this).parent().siblings("em");
		
		moneyf($(this),"0",emHtml,"请输入出借金额");
	});
	//出借利率验证
	function borratef(rate,msg){
		var massage="";
		massage=" 请选择出借利率";
		if(msg!=null){
			massage=" 请选择借款利率";
		}
		if(rate.children("dt").attr("data-id")=="0"){
			rate.siblings("em").html(massage);
		}else{
			massage="&nbsp;";
			rate.siblings("em").html(massage);
		}
		return massage;
	}
	//出借利率验证
	function bordataf(bdata,msg){
		var massage="";
		massage=" 请选择出借期数";
		if(msg!=null){
			massage=" 请选择借款期数";
		}
		if(bdata.children("dt").attr("data-id")=="0"){
			bdata.siblings("em").html(massage);
		}else{
			massage="&nbsp;";
			bdata.siblings("em").html(massage);
		}
		return massage;
	}
	//付息类型验证
	function rate_Typef(rate_T){
		var massage="";
		massage=" 请选择付息类型";
		if(rate_T.children("dt").attr("data-id")=="0"){
			rate_T.siblings("em").html(massage);
		}else{
			massage="&nbsp;";
			rate_T.siblings("em").html(massage);
		}
		return massage;
	}
	//出借按钮点击验证
	$("#bormonbtn").click(function(){
		var emHtml=$("#borrmon").parent().siblings("em");
		var t1=moneyf($("#borrmon"),"1",emHtml,"请输入出借金额");
		var t2=borratef($("#brate"));
		var t3=bordataf($("#bdata"));
		var t4=rate_Typef($("#rate_Type"));
		if(t1=="&nbsp;" && t2=="&nbsp;" && t3=="&nbsp;" && t4=="&nbsp;"){
			//后台计算  calculator
			var balance = $("#borrmon").val();
			var annualRate = $("#annualRate").attr("data-id");
			var months = $("#months").attr("data-id");
			var method = $("#rateType").attr("data-id");
			$.ajax({
				url:rootPath+"/calculator/lend",
				type:"post",
				data: {
					"balance": balance,
					"annualRate": parseFloat(annualRate),
					"months": months,
					"method":method
				},
				error : function(data) {
					var data = data.responseText;
					var _data = eval('('+data+')');
					//参数错误
					alert(_data.errorMsg);
				},
				success: function (data) {
					$("#balance").html(fmoney(data.balance,2));
					$("#interest").html(fmoney(data.interest,2));
					$("#all").html(fmoney(data.all,2));
					$(".borrd2").slideDown(500);

					$("#data_balance").val(balance);
					$("#data_annualRate").val(annualRate);
					$("#data_months").val(months);
					$("#data_method").val(method);

					if($("#aboutid").val()=="0"){
						var timerId2 = null;
						itemShowmy($(".jsq"));	
					}
				}
			});
			
		}
	});
	function itemShowmy(_this){
		timerId2 = setInterval(function(){
			_this.animate({top: "20%" }, 500);
		}, 500);
	}
	//借款利率验证
	function mdataf(mdata){
		var massage="";
		massage=" 请选择出借期数";
		if(mdata.children("dt").attr("data-id")=="0"){
			mdata.siblings("em").html(massage);
		}else{
			massage="&nbsp;";
			mdata.siblings("em").html(massage);
		}
		return massage;
	}
	//借款按钮点击验证
	$("#monborr").click(function(){
		var emHtml=$("#loanmoney").parent().siblings("em");
		var t1=moneyf($("#loanmoney"),"1",emHtml,"请输入借款金额");
		var t2=borratef($("#lbrate"),"1");
		var t3=bordataf($("#mdata"));
		var t4=rate_Typef($("#lrate_Type"));
		if(t1=="&nbsp;" && t2=="&nbsp;" && t3=="&nbsp;" && t4=="&nbsp;"){
			var balance = $("#loanmoney").val();
			var months = $("#dueTime").attr("data-id");
			var annualRate = $("#lannualRate").attr("data-id");
			var method = $("#lrateType").attr("data-id");
			$.ajax({
				url:rootPath+"/calculator/loan",
				type:"post",
				data: {
					"balance": balance,
					"annualRate": parseFloat(annualRate),
					"months": months,
					"method":method
				},
				error : function(data) {
					var data = data.responseText;
					var _data = eval('('+data+')');
					//参数错误
					alert(_data.errorMsg);
				},
				success: function (data) {
					$("#loan_balance").html(fmoney(data.balance,2));
					$("#fee").html(fmoney(data.interest,2));
					//$("#monthBalance").html(fmoney(data.monthBalance,2));
					$("#allBalance").html(fmoney(parseFloat(data.all),2));
					$(".bor2").slideDown(500);

					$("#data_balance").val(balance);
					$("#data_annualRate").val(annualRate);
					$("#data_months").val(months);
					$("#data_method").val(method);
				}
			});

		}
	});


	//点击向上按钮时
	$(".hengtiao").click(function(){
		$(this).parent().find(".bbordetail").slideUp(500);
	})
	
	/*---------------------------------- 帮助中心 --------------------------------*/
	$(".hright>h2").click(function(){
		if($(this).find("i").hasClass("ibottom")){
			//console.log("aa");
			$(this).next().slideUp(500).siblings("div").slideUp(500);
			$(this).find("i").removeClass("ibottom").addClass("itop").parent().siblings("h2").find("i").removeClass("ibottom").addClass("itop");
		}else{
			//console.log("b");
			
			$(this).next().slideDown(500).siblings("div").slideUp(500);
			$(this).find("i").removeClass("itop").addClass("ibottom").parent().siblings("h2").find("i").removeClass("ibottom").addClass("itop");
		}
	bottomB();
	});
}) 

	//借款金额输入验证
	function moneyf2(mond,mytext,emHtml,str1){

		if(undefined===emHtml){
			emHtml=mond.parent().siblings("em").html(massage);
		}
		var mondint;
		if(mond.val()==""){
			if(mytext=="0"){
				mondint=0;
				massage="";
				mond.removeClass("ipt-error");
				emHtml.html(massage);
				return massage;
			}else{

				massage=str1;
				mond.addClass("ipt-error");
				emHtml.html(massage);

				return massage;
				
			}
			
		}else{
				mondint=parseInt(mond.val());
			
		}

		if(mondint>=1000000000 ){
			massage="请输入正确的金额！";
			mond.addClass("ipt-error");
				emHtml.html(massage);
		}else{
				massage="";
				mond.removeClass("ipt-error");
				emHtml.html(massage);
				mond.val(Math.floor(parseFloat(mond.val())*100)/100);
//			}
		}
		return massage;
	}
	//借款金额输入验证
	function moneyf(mond,mytext,emHtml,str1){
		if(undefined===emHtml){
			emHtml=mond.parent().siblings("em").html(massage);
		}
		var mondint;
		if(mond.val()==""){
			
			if(mytext=="0"){
				mondint=0;
				massage="&nbsp;";
				mond.removeClass("ipt-error");
				emHtml.html(massage);
				return massage;
			}else{
				massage=str1;
				mond.addClass("ipt-error");
				emHtml.html(massage);
				return massage;
				
			}
			
		}else{

			if(/^([0-9.]+)$/.test(mond.val())){
				mondint=parseInt(mond.val());
			}else{

				massage="请输入正确的数字";
				mond.addClass("ipt-error");
				emHtml.html(massage);
				return massage;
			}
		}
		
		if(mondint<100 || mondint % 100!=0){
			massage="请输入大于100 并且是100 倍数的金额！";
			mond.addClass("ipt-error");
				emHtml.html(massage);
		}else{
				massage="&nbsp;";
				
				mond.removeClass("ipt-error");
				emHtml.html(massage);
//			}
		}
		return massage;
	}
$(document).ready(function(){
	var timerId = null;
	function itemShow(_this){
	
		if (timerId) return;
		timerId = setInterval(function(){
			_this.find(".grun").children("span").eq(0).animate({top: "-110px" }, 200);
			_this.find(".grun").children("span").eq(1).animate({top: "-110px" }, 200);
		}, 200);
	}
	function itemHide(){
		if (!timerId) return;
		$(".current span").animate({top: "0px" }, 200);
		$(".usq_gr").removeClass("current");
		clearInterval(timerId);
		timerId = null;
	}
	$(".usq_gr").hover(function(){
	
		$(this).addClass("current");
		itemShow($(this));
	}, itemHide);
});

function detailShow(v){
	if(v=='0'){
		//出借
		$("#data_jsq").attr("action",rootPath+"/calculator/lendDetail");
		$("#data_jsq").submit();
	}else{
		//借款
		$("#data_jsq").attr("action",rootPath+"/calculator/loanDetail");
		$("#data_jsq").submit();
	}

}