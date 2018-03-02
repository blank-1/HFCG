// JavaScript Document
$(function () {
	perLineAnimate();
	littleWin();
})

//进度条动画
function  perLineAnimate() {
	var money = $("#money").attr("data-money"),
			hasmoney = $("#hasmoney").attr("data-hasmoney"),
			per = Math.round(hasmoney/money*100),
			perbgin = 0;
	$(".l_perLine>i").css("width",per+"%");
	(function perAnimate(){
		if (perbgin == per) {
				$(".l_perLine>i>b").text(per+"%");
		}else{
				$(".l_perLine>i>b").text(perbgin+"%");
				setTimeout(function(){perAnimate(perbgin++)},2000/per);
		}
	})();
}

function littleWin(){
	var mask = $("<div>").addClass("l_mask").appendTo($("body")),
			win = $("<section>").addClass("l_win").appendTo(mask),
			p = $("<p>").text("您还未勾选《省心计划投资协议》").appendTo(win),
			btn = $("<button>").text("确定").appendTo(win);
			btn.on("touchend",function(e){
				e.stopPropagation();
				e.preventDefault();
				mask.hide();
				win.hide();
			})
}

function BTNcheck() {
	if (!$(".l_propto>i").hasClass("l_checked")) {
		$(".l_mask").show();
		$(".l_win").show();
		return false;
	}
	var val=$("#moneyInput").val();
	var qitou=$("#sx_startsAt").val();
	var dizen=$("#sx_upAt").val();
	var yue=$("#sx_yue").val();
	if (yue == ""||yue == undefined) {
		errrTip("请用户登录",null);
		return false;
	}
	if (val == "") {
		errrTip("请填写省心金额",null);
		return false;
	}
	val=parseInt(val);
	qitou=parseInt(qitou);
	dizen=parseInt(dizen);
	var type=$("#sx_type").val();
	if (val<qitou) {
		errrTip("输入大于起投的省心金额",null);
		return false;
	}else if (val%dizen != 0) {
		errrTip("请输入正确的省心金额",null);
		return false;
	}
	if(type=="2"){
		var balance=$("#sx_balance").val();
		balance=parseInt(balance);
		if(val>balance){
			errrTip("请输入正确的省心金额",null);
			return false;
		}
	}
	var isVerified=$("#isVerified").val();
	if(isVerified=="1"){
		$("#amount").val(val);
		$("#finance_detail_form").attr("action", rootPath + "/finance/toBuyFinanceByPayAmount").submit();
	}else{
		location.href=rootPath+"/finance/toRealName?url="+location.href;
	}
	
}
function errrTip(val,id){
	//错误提示效果2-特异错误
	var $id=$('#'+id);
	$("body").append("<div class='l_errr'>" + val + "</div>");
	$(".l_errr").show();
	$id.css('border','solid 1px rgba(254,42,77,1.0)');
	$(".l_errr").fadeOut(3000,function(){
		$(".l_errr").remove();
	});
}
function proCheck(el) {
	el.toggleClass("l_checked");
}

function testNum(obj){
	var num=obj.val();
	//console.log(num.length+"----------");
	if (!(/[\d]*/.test(num))) {
    	console.log(num+"===");
         obj.val(num.replace(/[^\d]/g,""));
         return false;
    }
    if(num.length>11){
    	obj.val(num.substring(0,num.length-1));
    	return false;
    }
}

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