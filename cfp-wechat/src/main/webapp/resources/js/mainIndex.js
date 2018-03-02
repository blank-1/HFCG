// JavaScript Document
//rem自适应字体大小方法
var docEl = document.documentElement,
    //当设备的方向变化（设备横向持或纵向持）此事件被触发。绑定此事件时，
    //注意现在当浏览器不支持orientationChange事件的时候我们绑定了resize 事件。
    //总来的来就是监听当然窗口的变化，一旦有变化就需要重新设置根字体的值
resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
recalc = function() {
    //设置根字体大小
    docEl.style.fontSize = 10 * (docEl.clientWidth / 320) + 'px';
};

//绑定浏览器缩放与加载时间
window.addEventListener(resizeEvt, recalc, false);
document.addEventListener('DOMContentLoaded', recalc, false);
window.addEventListener('orientationchange', function() {
    window.location.reload();
}, false);
//按钮点击增加变色效果代码
$("body").delegate("#next","touchstart", function (e) {
	e.stopPropagation();
    $(this).addClass("iconShadow");
    });
$("body").delegate("#next","touchend", function (e) {
	e.stopPropagation();
    $(this).removeClass("iconShadow");
    });

//密码切换效果
$(".l_pswBtn").on("touchend", function (e){
	e.stopPropagation();
	if ($(".l_pswBtn").hasClass("l_pswC")) {
		$(".l_pswBtn").removeClass("l_pswC");
		$("#passWord").prop("type","password");
	}else{
		$(".l_pswBtn").addClass("l_pswC");
		$("#passWord").prop("type","text");
	}
});
//验证码倒计时效果
var num=60;
function clock(){
	
	if (num == 0) {
		$("#checkCodeB").val("获取验证码");
		$("#checkCodeB").removeAttr('disabled');
		$("#checkCodeB").removeClass("l_disable");
		clearTimeout("clock()");
		num=60;
	}else{
		$("#checkCodeB").attr("disabled","");
		$("#checkCodeB").addClass("l_disable");
		$("#checkCodeB").val(num + "秒");
		num=num-1;
		setTimeout("clock()",1000);
	}
};
//错误提示效果1-非空验证
function errrTip(val,id){
		var $id=$('#'+id);	
		// $("input[name='submit']").attr('disabled','');
		// $("input[name='submit']").addClass('l_disable');
		$("body").append("<div class='l_errr'>请输入" + val + "</div>");
		$(".l_errr").show();
		// $id.attr("value","");
		// $id.focus();
		$id.css('border','solid 1px rgba(254,42,77,1.0)');
		$(".l_errr").fadeOut(3000,function(){
		$(".l_errr").remove();
		});	
}
//错误提示效果2-特异错误
function errrTip2(val,id){
		var $id=$('#'+id);	
		// $("input[name='submit']").attr('disabled','');
		// $("input[name='submit']").addClass('l_disable');
		$("body").append("<div class='l_errr'>" + val + "</div>");
		$(".l_errr").show();
		// $id.attr("value","");
		// $id.focus();
		$id.css('border','solid 1px rgba(254,42,77,1.0)');
		$(".l_errr").fadeOut(3000,function(){
		$(".l_errr").remove();
		});	
}
//全屏滚动效果
	function swiper(){
		$(".l_focusDot").remove();
		$(".l_sections:visible").css("left","0");
		var winWid = $("body").width(),
			eqSpan = 0,
			leg = $(".l_sections:visible li").length,
			focusDot = $("<p>").addClass("l_focusDot").appendTo("#l_swiperBox"),
			left, startX,startY,X,Y;
		$(".l_sections:visible").css("width",100*leg+"%");//计算父级ul的宽度
		$(".l_sections:visible li").css("width",winWid);//设置子li的宽度
		//插入焦点span
		for (var i = 0; i < leg; i++) {
			$("<span>").appendTo(focusDot);
		};
        $(".l_focusDot").css({"width":leg+"rem","margin-left":-leg/2+"rem"});
        $(".l_focusDot span:eq("+eqSpan+")").addClass("l_pointFocus");
		// console.log((leg-1)*winWid);

		// 监听touchmove事件
		$(".l_sections:visible").on("touchstart", function(e) {
		    e.preventDefault();
		    e.stopPropagation();
		    startX = e.originalEvent.changedTouches[0].pageX,
		    startY = e.originalEvent.changedTouches[0].pageY;
		    left1 = $(".l_sections:visible").offset().left;
		});
		$(".l_sections:visible").on("touchmove", function(e) {
		    e.preventDefault();
		    e.stopPropagation();
		    moveEndX = e.originalEvent.changedTouches[0].pageX,
		    moveEndY = e.originalEvent.changedTouches[0].pageY,
		    X = moveEndX - startX,
		    Y = moveEndY - startY;
		    $(".l_sections:visible").css("left",left1+X);
		});
		$(".l_sections:visible").on("touchend", function(e) {
		    e.preventDefault();
		    e.stopPropagation();
		    moveEndX = e.originalEvent.changedTouches[0].pageX,
		    moveEndY = e.originalEvent.changedTouches[0].pageY,
		    X2 = moveEndX - startX,
		    // console.log(X2);
		    // console.log(left1);
		    left2 = $(".l_sections:visible").offset().left;
		    // console.log(left2);
		    // console.log(left1);
		if (Math.abs(X) < winWid/2 && X2!=0 && X!=0) {//当滑动距离没超过半屏
			$(".l_sections:visible").css("left",left2-X);//返回原位
		}else if(Math.abs(X) >= winWid/2 &&X>0&& X2!=0){ 
			if (left1 >= 0) {
	  			$(".l_sections:visible").css("left","0");
	  		}else{
	  			eqSpan -= 1
	  			$(".l_sections:visible").css("left",left1+winWid);
	  			$(".l_focusDot span").removeClass("l_pointFocus");
	  			$(".l_focusDot span:eq("+eqSpan+")").addClass("l_pointFocus");
	  		}
	  	}else if(Math.abs(X) >= winWid/2 &&X<0&& X2!=0){
	  		if (left1 <= -(leg-1)*winWid) {
	  			$(".l_sections:visible").css("left",-(leg-1)*winWid);
	  		}else{
	  			eqSpan += 1
	  			$(".l_sections:visible").css("left",left1-winWid);
	  			$(".l_focusDot span").removeClass("l_pointFocus");
	  			$(".l_focusDot span:eq("+eqSpan+")").addClass("l_pointFocus");
	  		}
	  		
	  	}else{
	  		// 点击时获取页面当前li的序号;
	  		if (X2==0) {
	  			var legEq = -left2/winWid,
	  				linkTo = $(".l_sections:visible li:eq("+ legEq +")").attr("data-href")
	  			// alert(legEq)
	  			if (linkTo) {
	  				location.href = $(".l_sections:visible li:eq("+ legEq +")").attr("data-href");
	  			}
	  			
	  		};
	  		return false;
		}	  	
		});
	}
//回车键自动换行方法
var $load = $("#load"),
	inputLeg = $("input:visible[name='input']").length;
$("input[name='input']").on("input keydown", function(){   
	var inputNam = $(this).attr("data-inputNum");
	console.log(inputNam)
	console.log(inputLeg);
    if(event.keyCode ==13)   
    {   
	   if (inputNam == inputLeg) {
	   		$load.focus();
	   		return load();
	   }else{
	   		 var newNum = Math.floor(inputNam)+1;
	   		$(".input"+newNum).focus();
	   };
    }   
 })
// /**
// × JQUERY 模拟淘宝控件银行帐号输入
// * @Author 312854458@qq.com 旭日升
// **/

// function formatBankNo (BankNo){
// 	if (BankNo.value == "") return;
// 	var account = new String (BankNo.value);
// 	account = account.substring(0,22); /*帐号的总数, 包括空格在内 */
// 	if (account.match (".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}") == null){
// 		/* 对照格式 */
// 		if (account.match (".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}|" + ".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}|" +
// 		".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}|" + ".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}") == null){
// 			var accountNumeric = accountChar = "", i;
// 			for (i=0;i<account.length;i++){
// 				accountChar = account.substr (i,1);
// 				if (!isNaN (accountChar) && (accountChar != " ")) accountNumeric = accountNumeric + accountChar;
// 			}
// 			account = "";
// 			for (i=0;i<accountNumeric.length;i++){	/* 可将以下空格改为-,效果也不错 */
// 				if (i == 4) account = account + " "; /* 帐号第四位数后加空格 */
// 				if (i == 8) account = account + " "; /* 帐号第八位数后加空格 */
// 				if (i == 12) account = account + " ";/* 帐号第十二位后数后加空格 */
// 				account = account + accountNumeric.substr (i,1)
// 			}
// 		}
// 	}else{
// 		account = " " + account.substring (1,5) + " " + account.substring (6,10) + " " + account.substring (14,18) + "-" + account.substring(18,25);
// 	}
// 	if (account != BankNo.value) BankNo.value = account;
// }
// function checkBankNo (BankNo){
// 	if (BankNo.value == "") return;
// 	if (BankNo.value.match (".[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{7}") == null){
// 		if (BankNo.value.match ("[0-9]{19}") != null)
// 			formatBankNo (BankNo)
// 	}
// }