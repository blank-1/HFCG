// JavaScript Document
//用户中心左面菜单栏
/*$(function(){
	$(".p-nav-g>h2").click(function(){
		if($(this).find("i").hasClass("ibottom")){
			$(this).next().slideUp(500,function(){
			}).siblings("ul").slideUp(500);
			$(this).find("i").removeClass("ibottom").addClass("itop").parent().siblings("h2").find("i").removeClass("ibottom").addClass("itop");
			
		}else{
		
			$(this).next().slideDown(500,function(){
			}).siblings("ul").slideUp(500);
			$(this).find("i").removeClass("itop").addClass("ibottom").parent().siblings("h2").find("i").removeClass("ibottom").addClass("itop");
			
		}
	});
});*/

$(document).ready(function() {
	
    /**
     * 屏蔽小于750时 导航栏的显示或隐藏
     */
    var header=1;
    $(".header .dlogo i").click(function(){
        if(header==1){

            $(".header .dNav").slideDown(500);
            header=0;
        }
        else{
            $(".header .dNav").slideUp(500);
            header=1;

        }
    });

	
	//确定是否可以禁用
	if($("#regitxt").attr("data-val")=='2'){

		$("#getvalid").attr("disabled",false);
		$("input.ipt-input[type=text]:visible,button.btn").each(function() {
			$(this).attr("disabled",false);
		});
	}
	bottomB();
	
	$(window).resize(function(){
		bottomB();
	});

	//文本框清空
	$("input.ipt-input[type=text]:visible,input.ipt-input[type=password]").each(function() {
		var flag = $(this).attr("flag");
		
		if(flag!='true'){
			$(this).val("");
		}
    });

	$("#pwlogin_txt").val("请您输入密码");
	$("#unlogin1").val("用户名/手机号");
	$(".pwlogin_txt2").val("请您输入密码");

	

	//个人中心导航下拉
	$(".tabulper li").hover(function(){
		$(".pre_main .ding").hide();
		$(".pre_main").find(".pre_maintitle").hide();
		$(".pre_main").find(".pre_maintitle").eq($(this).attr("data-id")).show();
	},function(){ 
		
	});
	$(".personindex ul li,.personindex .pre_main").hover(function(){
		if($(this).find("a").html()=="账户总览"){
			$(".pre_main").slideUp(100);	
		}
		else{
			$(".pre_main").slideDown(100);	
		}
	});
	$(".personindex").hover(function(){ 
	},function(){
		$(".pre_main").slideUp(100);	
	});
	$(".tabpul").hover(function(){
	},function(){
		$(".pre_main").find(".pre_maintitle").hide();
		$(".pre_main .ding").show();
	});
	
	$(".tabul li,.jsq>h2 span").click(function(){
		$(this).addClass("action").siblings("li,span").removeClass("action");
		$("#"+$(this).attr("data-id")).siblings(".tab").hide();
		$("#"+$(this).attr("data-id")).fadeIn(500);
		bottomB();
	});

	//个人中心左部下拉列表
	$('a.pp').click(function(){
		$(this).next('ul.child').toggle('fast');
		$(this).parent('li').siblings().find('ul.child').hide('fast');
	});
	
	//下拉框
	$(".select").each(function(){
		var s=$(this);
		var z=parseInt(s.css("z-index"));
		var dt=$(this).children("dt");
		var dd=$(this).children("dd");
		var _show=function(){dd.slideDown(200);dt.addClass("cur");s.css("z-index",z+1);};   //展开效果
		var _hide=function(){dd.slideUp(200);dt.removeClass("cur");s.css("z-index",z);};    //关闭效果
		dt.click(function(){dd.is(":hidden")?_show():_hide();});
		dd.find("a").click(function(){dt.attr("data-id",$(this).parent().attr("data-id")); dt.html($(this).html());_hide();
			borratef($(this).parents("dl"),'请选择');
		});
		$("body").click(function(i){ !$(i.target).parents(".select").first().is(s) ? _hide():"";});
	});
	
	var _docu=document.documentElement.clientHeight/2-(parseInt($(".masklayer").height()))*0.5;
	$(".masklayer").css("top",_docu+'px');
	$(".masklayer").each(function(){
		$(this).css("right",document.body.clientWidth/2-(parseInt($(this).width()))*0.5);
	});
	//$(".masklayers").css("top", document.documentElement.offsetHeight/8);
	$(".masklayers").css("top",document.documentElement.clientHeight/2-(parseInt($(".masklayers").height())/2)+'px');
	$(".masklayers").css("right",document.body.clientWidth/2-(parseInt($(".masklayers").width()))*0.5);
	$("#login").css("top", document.documentElement.clientHeight/2-(parseInt($(".masklayer").height()))*0.5);

	$(".clearFloat").parent().css("top","10px");
	$("#payshowstate").css("top","10px");
	$(".masklaym3").css("top","10px");
	
	//遮罩层
	$(document).on("click","a[data-mask=mask],button[data-mask=mask]",function(){
		$(".masklayer").each(function(){
			$(this).css("top", document.documentElement.clientHeight/2-(parseInt($(".masklayers").height())/2)+'px');
			$(this).css("right",document.body.clientWidth/2-(parseInt($(this).width()))*0.5);

		});
		$(".masklaym3,#p_bank").css("top","10px");
		$("#"+$(this).attr("data-id")).slideDown(500);
		$(".zhezhao1").show();
		$(".zhezhao1,.zhezhao,.zhezhao5,.zhezhao2,.zhezhao3,.zhezhao6").css("height",($("body").height() + $(".footer").height() + 20));

	});
	$("a[data-id='close'],i[data-id='close']").click(function(){
		$(this).parents(".masklayer").hide();
		$(this).parents(".masklayers").hide();
		$(".zhezhao").hide();
		$(".zhezhao1").hide();
		$(".zhezhao2").hide();
		$(".zhezhao3").hide();
		$(".zhezhao5").hide();
		$(".zhezhao6").hide();
		
		if($("#cjd").is(":visible")){
			$(".zhezhao1").show();
		}
	});
	
	$("[data-id='httpClose']").click(function(){
		$(this).parents(".masklayer").hide();
		$(this).parents(".masklayers").hide();
		$(".zhezhao1").hide();
	});
	
	//导航栏js下拉列表导航
	$("li.fb").hover(function(){
		$(this).children("ul.nav_list").show("fast");
	},function(){
		$(this).children("ul.nav_list").hide("fast");
	});
	//顶部js下拉列表导航
	$(".posirelcur").hover(function(){
		$(this).children(".our").show("fast");
	},function(){
		$(this).children(".our").hide("fast");
	});
	
	//单击时间时，弹出的时间事件
	$(".hdate").click(function (e) {
		var ths = this;
		calendar.show({
			id: this, ok: function () {
			}
		});
	});	
	
	//显示与隐藏解释
	$("#img_quest").hover(function(){
		$(this).siblings("em").show();
	},function(){
		
		$(this).siblings("em").hide();
	});
	
});

	//格式化金额
	function fmoney(s, n) {  
		n = n > 0 && n <= 20 ? n : 2;  
		s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";  
		var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];  
		t = "";  
		for (i = 0; i < l.length; i++) {  
			t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");  
		}  
		return t.split("").reverse().join("") + "." + r;  
	} 

	//还原小数
	function rmoney(s) {
		if (typeof (s) == "undefined") {
			s = "0";
		}
		return parseFloat(s.replace(/[^\d\.-]/g, ""));
	}

/**
× JQUERY 模拟淘宝控件银行帐号输入
* @Author 312854458@qq.com 旭日升
**/

function formatBankNo (BankNo){
	if (!BankNo) return ;
	if (BankNo.value == "") return ;
	var account = new String (BankNo.value);
	account = account.substring(0,22); /*帐号的总数, 包括空格在内 */
	if (account.match (".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}") == null){
		/* 对照格式 */
		if (account.match (".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}|" + ".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}|" +
		".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}|" + ".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}") == null){
			var accountNumeric = accountChar = "", i;
			for (i=0;i<account.length;i++){
				accountChar = account.substr (i,1);
				if (!isNaN (accountChar) && (accountChar != " ")) accountNumeric = accountNumeric + accountChar;
			}
			account = "";
			for (i=0;i<accountNumeric.length;i++){	/* 可将以下空格改为-,效果也不错 */
				if (i == 4) account = account + " "; /* 帐号第四位数后加空格 */
				if (i == 8) account = account + " "; /* 帐号第八位数后加空格 */
				if (i == 12) account = account + " ";/* 帐号第十二位后数后加空格 */
				account = account + accountNumeric.substr (i,1)
			}
		}
	}else{
		account = " " + account.substring (1,5) + " " + account.substring (6,10) + " " + account.substring (14,18) + "-" + account.substring(18,25);
	}
	if (account != BankNo.value) BankNo.value = account;
}
function checkBankNo (BankNo){
	if (BankNo.value == "") return;
	if (BankNo.value.match (".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}") == null){
		if (BankNo.value.match ("[0-9]{19}") != null)
			formatBankNo (BankNo);
	}
}

//兼容火狐
//总在最底部
	function bottomB(){
		
		var footerHeight = 0,
		footerTop = 0,
		$footer = $(".footer");
		$footer.css({
			position: "absolute",
			top:"-1"
		});
		//取到div#footer高度
		footerHeight = $footer.height();
		//div#footer离屏幕顶部的距离
//		footerTop = ( $(window).scrollTop()+$(window).height()-footerHeight)+"px";
		var srcreeHeight = $(window).height();
		var pageHeight = $('body').height();
		//如果页面内容高度小于屏幕高度，div#footer将绝对定位到屏幕底部，否则div#footer保留它的正常静态定位
		if( (pageHeight+footerHeight) > srcreeHeight){
			footerTop = pageHeight  ;
		}else{
			footerTop = srcreeHeight - footerHeight  ;
		}
		//footerTop += 100;
		$footer.css({
			position: "absolute",
			top: footerTop
		});
		$(".zhezhao1,.zhezhao,.zhezhao5,.zhezhao2,.zhezhao3,.zhezhao6").css("height",(pageHeight+$(".footer").height() + 20) );
		$(".masklaym3").css("top","50px");
	}
	
	function checkEnterForFindListing(e){
		var characterCode;
		if(e && e.which){
			e = e;
			characterCode = e.which ;
		}else{
			e = event;
			characterCode = e.keyCode;
		}
		if(characterCode == 22){
			document.forms[getNetuiTagName("findListingForm")].submit();
			return false;
		}else{
			return true ;
		}
	}


	//格式化时间
	function dateTimeFormatter(val) {

		if (val == undefined || val == "")
			return "";
		var date;
		if(val instanceof Date){
			date = val;
		}else{
			date = new Date(val);
		}
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();

		var h = date.getHours();
		var mm = date.getMinutes();
		var s = date.getSeconds();

		var dateStr = y + '-' + (m < 10 ? ('0' + m) : m) + '-'
			+ (d < 10 ? ('0' + d) : d);
		var TimeStr = h + ':' + (mm < 10 ? ('0' + mm) : mm) + ':'
			+ (s < 10 ? ('0' + s) : s);

		return dateStr + ' ' + TimeStr;
	}
	//让字符串中间显示*号
	function plusXing (str,frontLen,endLen) { 
	    var len = str.length-frontLen-endLen;
	    var xing = '';
	    for (var i=0;i<len;i++) {
	        xing+='*';
	    }
	    return str.substr(0,frontLen)+xing+str.substr(str.length-endLen);
	}
	//百度统计代码

//	var _hmt = _hmt || [];
//	(function() {
//		var hm = document.createElement("script");
//		hm.src = "//hm.baidu.com/hm.js?a170dfa53a64a17e8bc1477c5d7f9ac6";
//		var s = document.getElementsByTagName("script")[0];
//		s.parentNode.insertBefore(hm, s);
//	})();
	
	$(function(){
		//用户信息展示
		var basePath = $("#bsPath").val();
		var obj = $("div.container div.pLeft");
		if(obj.attr("class")){
			obj.load(basePath + "/person/getUserCommonInfo?_v="+Math.random());
		}
		
		//省心计划，出借列表，债权转让切换
		$(".th-pro-title.wylc li").click(function(){
			$(this).addClass("th-action").siblings("li,span").removeClass("th-action");
			$("#"+$(this).attr("data-id")).siblings(".tab").hide();
			$("#"+$(this).attr("data-id")).fadeIn(500);
			bottomB();
		});
		
	});
	