// JavaScript Document

var publicJS = {
	"resizeForREM":function(){
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
	},

	"BTNcolor":function(){
		//按钮点击增加变色效果代码
		$("#next").on("touchstart", function (e) {
			e.stopPropagation();
			e.preventDefault();
		    $(this).addClass("iconShadow");
	    });
		$("#next").on("touchend", function (e) {
			e.stopPropagation();
			e.preventDefault();
		    $(this).removeClass("iconShadow");
	    });
	},

	"PasswordChange":function(){
		//密码切换效果
		$(".l_pswBtn").on("touchend", function (e){
			e.stopPropagation();
			e.preventDefault();
			if ($(".l_pswBtn").hasClass("l_pswC")) {
				$(".l_pswBtn").removeClass("l_pswC");
				$("#passWord").prop("type","password");
			}else{
				$(".l_pswBtn").addClass("l_pswC");
				$("#passWord").prop("type","text");
			}
		});
	},
	"num":60,
	"countDown":function(){
		//验证码倒计时效果
			if (publicJS.num == 0) {
				$("#checkCodeB").text("获取验证码");
				$("#checkCodeB").removeAttr('disabled');
				$("#checkCodeB").removeClass("l_disable");
				publicJS.num=60;
			}else{
				$("#checkCodeB").attr("disabled","");
				$("#checkCodeB").addClass("l_disable");
				$("#checkCodeB").text(publicJS.num + "秒");
				console.log(publicJS.num);
				window.setTimeout(function(){publicJS.countDown(publicJS.num--)},1000);
			}

	},

	"errrTip":function(val,id){
		//错误提示效果2-特异错误
		var $id=$('#'+id);
		$("body").append("<div class='l_errr'>" + val + "</div>");
		$(".l_errr").show();
		$id.css('border','solid 1px rgba(254,42,77,1.0)');
		$(".l_errr").fadeOut(3000,function(){
			$(".l_errr").remove();
		});
	},

	"errrTipN":function(val,id){
		//错误提示效果-新
		var $id=$('#'+id);
		$(".l_errrNew").text(val).removeClass("opac0");
		$id.css('border','solid 1px rgba(254,42,77,1.0)');
	},

	"swiper":function(){
		//全屏滚动效果
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
		    leftStart = $(".l_sections:visible").offset().left;
		    // console.log(leftStart);
		});
		$(".l_sections:visible").on("touchmove", function(e) {
		    e.preventDefault();
		    e.stopPropagation();
		    moveEndX = e.originalEvent.changedTouches[0].pageX,
		    moveEndY = e.originalEvent.changedTouches[0].pageY,
		    X = moveEndX - startX,
		    Y = moveEndY - startY;
		    $(".l_sections:visible").css("left",leftStart+X);
		});
		$(".l_sections:visible").on("touchend", function(e) {
		    e.preventDefault();
		    e.stopPropagation();
		    moveEndX = e.originalEvent.changedTouches[0].pageX,
		    moveEndY = e.originalEvent.changedTouches[0].pageY,
		    X2 = moveEndX - startX,
		    // console.log(X2);
		    // console.log(leftStart);
		    leftEnd = $(".l_sections:visible").offset().left;
		    // console.log(leftEnd);
		    // console.log(leftStart);
			if (Math.abs(X) < winWid/3 && X2!=0 && X!=0) {//当滑动距离没超过半屏
				$(".l_sections:visible").css("left",leftEnd-X);//返回原位
			}else if(Math.abs(X) >= winWid/3 &&X>0&& X2!=0){
				if (leftStart >= 0) {
					//防止滑动超出边界
		  			$(".l_sections:visible").animate({left:"0"},"fast","swing");
		  		}else{
		  			eqSpan -= 1
		  			$(".l_sections:visible").animate({left:leftStart+winWid},"fast","swing");
		  			$(".l_focusDot span").removeClass("l_pointFocus");
		  			$(".l_focusDot span:eq("+eqSpan+")").addClass("l_pointFocus");
		  		}
		  	}else if(Math.abs(X) >= winWid/3 &&X<0&& X2!=0){
		  		if (leftStart <= -(leg-1)*winWid) {
		  			//防止滑动超出边界
		  			$(".l_sections:visible").animate({left:-(leg-1)*winWid},"fast","swing");
		  		}else{
		  			eqSpan += 1
		  			$(".l_sections:visible").animate({left:leftStart-winWid},"fast","swing");
		  			$(".l_focusDot span").removeClass("l_pointFocus");
		  			$(".l_focusDot span:eq("+eqSpan+")").addClass("l_pointFocus");
		  		}

		  	}else{
		  		// 点击时获取页面当前li的序号;
		  		// if (X2==0) {
		  		// 	var legEq = -leftEnd/winWid,
		  		// 		linkTo = $(".l_sections:visible li:eq("+ legEq +")").attr("data-href")
		  		// 	// alert(legEq)
		  		// 	if (linkTo) {
		  		// 		location.href = $(".l_sections:visible li:eq("+ legEq +")").attr("data-href");
		  		// 	}

		  		// };
		  		return false;
			}
		});
	},

	"AutoEnter":function(){
		//回车键自动换行方法
		var $load = $("#load"),
		inputLeg = $("input:visible[name='input']").length;
		$("input[name='input']").on("input keydown", function(){
			var inputNam = $(this).attr("data-inputNum");
			// console.log(inputNam)
			// console.log(inputLeg);
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
	},

	"limitNUM":function(n){
		//输入框字符数量限制方法
	    var leg = $(".limitNUM"+n).val().length;
	    if (leg > n) {
			$(".limitNUM"+n).val($(".limitNUM"+n).val().substring(0,n));
	    }
	},

	"inpputFocus":function(el,id){
		$("#"+id).text("请输入4~20位字符,支持汉字,字母,数字及\"-\",\"_\"组合");
		$("#"+id).addClass("linkB");
		el.css("border-radius","0 0 4px 4px");
		el.attr("placeholder","");
		$("#"+id).slideDown();
	},
	notNUM:function(num,val){
		var leg = val.length;
		console.log($(".limitNUMs").val());
        if ((/[^\d]+/.test(val))) {
        	console.log($(".limitNUMs").val()+"===");
        	alert(1)
             $(".limitNUMs").val($(".limitNUMs").val().substring(0,leg-1));
             return false;//$(".limitNUM").val().replace(".","")
        }
        if(leg>num){
        	$(".limitNUMs").val($(".limitNUMs").val().substring(0,leg-1));
        	return false;
        }
	}
}

$(function(){
	publicJS.BTNcolor();
	publicJS.PasswordChange();
	publicJS.AutoEnter();
});
publicJS.resizeForREM();
var num=60;
function clock(){
	if (num == 0) {
		$("#checkCodeB").val("获取验证码");
		$("#checkCodeB").removeAttr('disabled');
		$("#checkCodeB").removeClass("l_disable");
		clearTimeout("clock()");
		num=60;
	}else{
		alert(1);
		$("#checkCodeB").attr("disabled","");
		$("#checkCodeB").addClass("l_disable");
		$("#checkCodeB").text(num + "秒");
		num--;
		setTimeout("clock()",1000);
	}
};
